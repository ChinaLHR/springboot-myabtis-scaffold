package cn.bfreeman.common.result;


import lombok.Data;

import java.io.Serializable;
import java.util.Objects;


/**
 * 符合最新前后端规范（V1）json返回值格式的辅助对象
 * 该规范详情请参考：
 * 度假tts前后端接口规范
 * </a>
 *
 * @author kris.zhang
 * @since 1.0.0
 */
@Data
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 5873691451224497616L;

    private String message;
    private T data;
    private Integer code;

    public boolean isSuccess() {
        return Objects.equals(this.getCode(), ApiResultCode.SUCCESS);
    }

    public static <T> ApiResult<T> success() {
        return new ApiResult<>(true);
    }

    public static <T> ApiResult<T> failure() {
        return new ApiResult<>(false);
    }

    public static <T> ApiResult<T> success(String message) {
        return new ApiResult<>(true, message);
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(true, null, data);
    }

    public static <T> ApiResult<T> failure(String message) {
        return new ApiResult<>(false, message);
    }

    public static <T> ApiResult<T> failure(Integer code, String message) {
        return new ApiResult<>(code, message);
    }

    public static <T> ApiResult<T> failure(String message, Integer code) {
        return new ApiResult<>(code, message);
    }

    public static <T> ApiResult<T> error(String message, Integer code) {
        return new ApiResult<>(code, message);
    }

    public ApiResult() {
        super();
    }


    private ApiResult(boolean success) {
        this(success, "");
    }

    private ApiResult(boolean success, String message) {
        this(success, message, null);
    }

    private ApiResult(boolean success, String message, T data) {
        this.message = message;
        this.data = data;
        this.code = success ? ApiResultCode.SUCCESS : ApiResultCode.FAILURE;
    }

    private ApiResult(int code, String message, T data) {
        this.message = message;
        this.data = data;
        this.code = code;
    }

    private ApiResult(int code, String message) {
        this.message = message;
        this.code = code;
    }

    public ApiResult<T> message(String message) {
        this.message = message;
        return this;
    }

    public ApiResult<T> data(T data) {
        this.data = data;
        return this;
    }

    public ApiResult<T> code(Integer code) {
        this.code = code;
        return this;
    }
}