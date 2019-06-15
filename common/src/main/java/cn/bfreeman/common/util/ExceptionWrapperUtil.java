package cn.bfreeman.common.util;

import cn.bfreeman.common.exception.FatalException;
import org.springframework.web.util.NestedServletException;

import java.util.Objects;

/**
 * @author lhr
 * @date 2019/6/13
 *
 * 异常包裹Util
 */
public class ExceptionWrapperUtil {

    /**
     * 递归得到真正的异常
     * FatalException 是自定义的包裹异常
     * NestedServletException 是 spring的包裹异常, 会裹住用户抛的异常
     *
     * @param e
     * @return
     */
    public static Throwable queryRealException(Throwable e) {
        // 出口1, e 不是 包裹异常
        boolean isWrapper = e instanceof NestedServletException
                || e instanceof FatalException;
        if (!isWrapper) {
            return e;
        }

        // 出口2, e 在异常链尾部
        Throwable cause = e.getCause();
        if (Objects.isNull(cause)) {
            return e;
        }
        return queryRealException(cause);
    }

}
