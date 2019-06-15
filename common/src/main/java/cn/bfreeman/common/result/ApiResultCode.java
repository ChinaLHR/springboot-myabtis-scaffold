package cn.bfreeman.common.result;

/**
 * @author xiang.rao
 * @version 16/8/9
 */
public interface ApiResultCode {

    int SUCCESS = 0;
    int FAILURE = -1;

    /**
     * 重要业务异常
     */
    int FATAL_EXCEPTION = 10001;
    /**
     * 已知业务异常
     */
    int IGNORABLE_EXCEPTION = 10002;
    /**
     * 错误的请求方式
     */
    int BAD_REQUEST = 10003;
    /**
     * 未知异常
     */
    int UNCAUGHT_EXCEPTION = 10004;
    /**
     * 错误的请求路径
     */
    int BAD_REQUEST_PATH = 10005;
}
