package cn.bfreeman.core.filter;

import cn.bfreeman.common.exception.FatalException;
import cn.bfreeman.common.exception.IgnorableException;
import cn.bfreeman.common.result.ApiResult;
import cn.bfreeman.common.result.ApiResultCode;
import cn.bfreeman.common.util.JsonUtil;
import cn.bfreeman.common.util.RequestUtil;
import cn.bfreeman.common.util.ResponseUtil;
import cn.bfreeman.core.common.validate.Validator;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static cn.bfreeman.common.util.ExceptionWrapperUtil.queryRealException;

/**
 * @Author : lhr
 * @Date : 18:40 2019/6/13
 * <p>
 * 全局的异常处理filter
 * 该filter务必排在最外层, 也就是说做为第一个用户自定义filter
 * 需要对默认的ExceptionHandler做修改， 默认的ExceptionHandler不要吞掉异常
 */
@Slf4j
public class GlobalWebExceptionFilter implements Filter {


    @Getter
    @Setter
    private boolean resolveBindingException = true;

    @Setter
    @Getter
    private MessageSource messageSource;

    @Setter
    @Getter
    private Validator validator;


    /**
     * 异常处理
     *
     * @param request
     * @param response
     * @param e
     */
    private void resolveException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        Throwable ex = queryRealException(e);
        // 区分业务异常 和 其他未捕获异常
        ApiResult apiResult;
        //业务异常(可忽略)
        if (ex instanceof IgnorableException) {
            IgnorableException ignorable = (IgnorableException) ex;
            apiResult = ApiResult.failure(ignorable.getMessage(), ApiResultCode.IGNORABLE_EXCEPTION);
            // 处理业务异常
            log.warn("Ignorable BizException.", request.getRequestURI(), ex.getMessage());
        }

        //业务异常(重要)
        else if (ex instanceof FatalException) {
            String msg = Optional.ofNullable(ex.getMessage()).orElse("系统异常");
            apiResult = ApiResult.failure(msg, ApiResultCode.FATAL_EXCEPTION);
            // 处理其他未捕获异常
            logError(request, ex, "exception:fatal_exception");
            //todo 后续引入监控需要记录继续Error异常
        } else if (ex instanceof HttpMessageNotReadableException) {
            apiResult = ApiResult.failure("错误的请求", ApiResultCode.BAD_REQUEST);
            logError(request, ex, "exception: http_uncaught_params_exception");
        }

        // 处理可能包含binding exception的异常
        else if (resolveBindingException && ex instanceof ServletRequestBindingException) {
            List<ObjectError> errors = ex.getCause() instanceof BindException ? ((BindException) ex.getCause()).getBindingResult().getAllErrors() : null;
            apiResult = tryResolveBindException(request, errors);
            logError(request, ex, "exception: http_param_validate_exception");
        } else if (resolveBindingException && ex instanceof BindException) {
            apiResult = tryResolveBindException(request, ((BindException) ex).getAllErrors());
            logError(request, ex, "exception: http_param_validate_exception");
        } else if (resolveBindingException && ex instanceof MethodArgumentNotValidException) {
            apiResult = tryResolveBindException(request, ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors());
            logError(request, ex, "exception: http_param_validate_exception");
        } else if (resolveBindingException && ex instanceof ConstraintViolationException) {
            apiResult = tryResolveConstraintViolationException(request, ((ConstraintViolationException) ex).getConstraintViolations());
            logError(request, ex, "exception: http_param_validate_exception");
        }

        //处理404
        else if (ex instanceof NoHandlerFoundException){
            apiResult = ApiResult.failure("错误的请求路径",ApiResultCode.BAD_REQUEST_PATH);
            logError(request, ex, "exception: http_path_exception");
        }

        else {
            apiResult = ApiResult.failure("系统繁忙,请稍后再试", ApiResultCode.UNCAUGHT_EXCEPTION);
            // 处理其他未捕获异常
            logError(request, ex, "exception: uncaught_exception");
        }

        ResponseUtil.sendJsonNoCache(response, JsonUtil.encodeQuietly(apiResult));

    }

    /**
     * 取 ConstraintViolation 的错误信息
     *
     * @param request
     * @param constraintViolations
     * @return
     */
    private ApiResult tryResolveConstraintViolationException(HttpServletRequest request, Set<ConstraintViolation<?>> constraintViolations) {
        StringBuilder sb = new StringBuilder("参数错误 ");
        if (messageSource != null && CollectionUtils.isNotEmpty(constraintViolations)) {
            String message = validator.message(constraintViolations);
            sb.append(message);
        }
        return ApiResult.failure(sb.toString(), ApiResultCode.BAD_REQUEST);
    }

    private ApiResult tryResolveBindException(HttpServletRequest request, List<ObjectError> errors) {
        StringBuilder sb = new StringBuilder("参数错误");
        if (messageSource != null && CollectionUtils.isNotEmpty(errors)) {
            resolveErrors(errors, sb);
        }

        return ApiResult.failure(sb.toString(), ApiResultCode.BAD_REQUEST);
    }

    /**
     * @see org.springframework.validation.DefaultMessageCodesResolver
     */
    private void resolveErrors(List<ObjectError> errors, StringBuilder sb) {
        try {
            if (CollectionUtils.isNotEmpty(errors)) {
                for (ObjectError error : errors) {
                    sb.append(',').append(messageSource.getMessage(error, Locale.getDefault()));
                }
            }
        } catch (Exception e) {
            log.error("错误消息配置有误", e);
        }
    }

    /**
     * @param request
     * @param ex
     * @param tag
     */
    private void logError(HttpServletRequest request, Throwable ex, String tag) {
        log.error(tag + " URI: {}, HTTP_METHOD: {}, IP: {}, HEADER:{}, PARAMETER:{}, POST_BODY:{}",
                request.getRequestURI(),
                request.getMethod(),
                RequestUtil.getIpAddress(request),
                queryHeader(request));
    }

    private Map<String, Object> queryHeader(HttpServletRequest request) {
        Map<String, Object> headers = Maps.newHashMap();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headers.put(key, value);
        }
        return headers;
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            //进行全局异常处理
            resolveException((HttpServletRequest) request, (HttpServletResponse) response, e);
        }
    }

    @Override
    public void destroy() {

    }
}
