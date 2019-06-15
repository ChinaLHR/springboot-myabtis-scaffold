package cn.bfreeman.common.exception;

/**
 * @Author : lhr
 * @Date : 16:20 2019/6/12
 * <p>
 * 业务异常(可忽略)
 */
public class IgnorableException extends AbstractBizException {
    public IgnorableException() {
        super();
    }

    public IgnorableException(String message) {
        super(message);
    }

    public IgnorableException(String msgTemplate, Object... args) {
        super(msgTemplate, args);
    }

    public IgnorableException(String message, Throwable cause) {
        super(message, cause);
    }

    public IgnorableException(String msgTemplate, Throwable cause, Object... args) {
        super(msgTemplate, cause, args);
    }

    public IgnorableException(Throwable cause) {
        super(cause);
    }
}
