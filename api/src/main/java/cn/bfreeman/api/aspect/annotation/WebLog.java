package cn.bfreeman.api.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author : lhr
 * @Date : 12:03 2019/6/17
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebLog {

    /**
     * 默认打印请求参数
     * @return
     */
    boolean printReq() default true;

    /**
     * 默认不打印响应参数
     * @return
     */
    boolean printRes() default false;
}
