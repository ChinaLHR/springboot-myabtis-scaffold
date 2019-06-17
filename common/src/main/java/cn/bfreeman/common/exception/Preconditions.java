package cn.bfreeman.common.exception;

/**
 * @Author : lhr
 * @Date : 15:08 2019/6/17
 */
public class Preconditions {

    public static final Precondition fatal = new Precondition(new Precondition.ExceptionBuilder() {
        @Override
        public AbstractBizException newException() {
            return new FatalException();
        }

        @Override
        public AbstractBizException newException(String msg) {
            return new FatalException(msg);
        }

        @Override
        public AbstractBizException newException(String msg, Throwable cause) {
            return new FatalException(msg, cause);
        }
    });

    public static final Precondition ignore = new Precondition(new Precondition.ExceptionBuilder() {
        @Override
        public AbstractBizException newException() {
            return new IgnorableException();
        }

        @Override
        public AbstractBizException newException(String msg) {
            return new IgnorableException(msg);
        }

        @Override
        public AbstractBizException newException(String msg, Throwable cause) {
            return new IgnorableException(msg, cause);
        }
    });

}
