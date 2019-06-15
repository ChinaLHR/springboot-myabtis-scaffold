package cn.bfreeman.common.exception;

/**
 * @Author : lhr
 * @Date : 16:17 2019/6/12
 * <p>
 * 业务异常(重点)
 */
public class FatalException extends AbstractBizException {
    public FatalException() {
        super();
    }

    public FatalException(String message) {
        super(message);
    }

    public FatalException(String msgTemplate, Object... args) {
        super(msgTemplate, args);
    }

    public FatalException(String message, Throwable cause) {
        super(message, cause);
    }

    public FatalException(String msgTemplate, Throwable cause, Object... args) {
        super(msgTemplate, cause, args);
    }

    public FatalException(Throwable cause) {
        super(cause);
    }
}
