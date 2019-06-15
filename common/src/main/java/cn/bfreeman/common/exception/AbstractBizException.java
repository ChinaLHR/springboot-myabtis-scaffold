package cn.bfreeman.common.exception;

/**
 * @Author : lhr
 * @Date : 16:55 2019/6/12
 * <p>
 * 业务层异常抽象
 */
public abstract class AbstractBizException extends RuntimeException {

    public AbstractBizException() {
    }

    public AbstractBizException(String message) {
        super(message);
    }

    public AbstractBizException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractBizException(Throwable cause) {
        super(cause);
    }

    public AbstractBizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AbstractBizException(String msgTemplate, Object... args) {
        super(String.format(msgTemplate, args));
    }

    public AbstractBizException(String msgTemplate, Throwable cause, Object... args) {
        super(String.format(msgTemplate, args), cause);
    }
}
