package cn.bfreeman.core.config.web;

import cn.bfreeman.common.exception.FatalException;
import cn.bfreeman.common.exception.IgnorableException;
import cn.bfreeman.common.result.ApiResult;
import cn.bfreeman.common.result.ApiResultCode;
import cn.bfreeman.common.util.RequestUtil;
import cn.bfreeman.common.util.ResponseUtil;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

import static cn.bfreeman.common.util.JsonUtil.encodeQuietly;

/**
 * @author lhr
 * @date 2019/6/14
 */
@Slf4j
public class GlobalWebExceptionHandler implements HandlerExceptionResolver, Ordered {
    @Setter
    @Getter
    private int order = 1;

    private static final ModelAndView emptyView = new ModelAndView();

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        //目前只考虑了json接口
        // 区分业务异常, 授权异常, Fatal异常 和 其他未捕获异常
        ApiResult apiResult;

        if (e instanceof IgnorableException) {
            IgnorableException ignorable = (IgnorableException) e;
            apiResult = ApiResult.failure(ignorable.getMessage(), ApiResultCode.IGNORABLE_EXCEPTION);

            logWarn(httpServletRequest, e, "bizException");
        } else if (e instanceof FatalException) {
            throw (FatalException) e;
        } else {
            throw new FatalException(e.getMessage(), e);
        }

        ResponseUtil.sendJsonNoCache(httpServletResponse, encodeQuietly(apiResult));
        return emptyView;
    }

    private void logWarn(HttpServletRequest request, Throwable ex, String tag) {
        log.warn(tag + " MSG: {}, URI: {}, HTTP_METHOD: {}, IP: {}, HEADER:{}, PARAMETER:{}, POST_BODY:{}",
                ex.getMessage(),
                request.getRequestURI(),
                request.getMethod(),
                RequestUtil.getIpAddress(request),
                queryHeader(request),
                encodeQuietly(request.getParameterMap()));
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

}
