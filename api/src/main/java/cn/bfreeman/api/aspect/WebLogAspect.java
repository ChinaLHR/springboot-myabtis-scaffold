package cn.bfreeman.api.aspect;

import cn.bfreeman.api.aspect.annotation.WebLog;
import cn.bfreeman.common.util.RequestUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

import static cn.bfreeman.common.util.JsonUtil.encodeQuietly;
import static cn.bfreeman.core.constant.LoggerConstant.LOGGER_HTTP;

/**
 * @Author : lhr
 * @Date : 14:06 2019/6/17
 */
@Aspect
@Component
@Order(value = 10)
public class WebLogAspect {

    private final Logger logger = LoggerFactory.getLogger(LOGGER_HTTP);

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "&& @annotation(webLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, WebLog webLog) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        Map<String, Object> headers = RequestUtil.queryHeader(request);
        if (webLog.printReq()) {
            logger.info("[REQUEST] URL: {}, HTTP_METHOD: {}, IP: {}, HEADER:{}, PARAMETER:{}, ARGS: {}, CLASS_METHOD: {}",
                    request.getRequestURL().toString(),
                    request.getMethod(),
                    request.getRemoteAddr(),
                    headers,
                    encodeQuietly(request.getParameterMap()),
                    encodeQuietly(queryArgs(joinPoint)),
                    joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()
            );
        }

        Object ret = null;
        try {
            ret = joinPoint.proceed();
            return ret;
        } finally {
            if (webLog.printRes()) {
                logger.info("[RESPONSE] URL: {},  RET: {}",
                        request.getRequestURL().toString(),
                        encodeQuietly(ret)
                );
            }
        }
    }

    private Object[] queryArgs(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Object[] objects = Arrays.stream(args).filter(this::isNotCircleReference).toArray();
        return objects;
    }

    private boolean isNotCircleReference(Object o) {
        return !isCircleReference(o);
    }

    /**
     * 可能导致循环引用的类应该过滤掉, 不然在toString 的时候可能会有栈溢出
     *
     * @param o
     * @return
     */
    private boolean isCircleReference(Object o) {
        if (o instanceof HttpServletRequest) {
            return true;
        }
        if (o instanceof HttpServletResponse) {
            return true;
        }
        return false;
    }
}

