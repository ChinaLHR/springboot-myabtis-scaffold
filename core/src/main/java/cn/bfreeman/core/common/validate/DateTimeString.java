package cn.bfreeman.core.common.validate;



import cn.bfreeman.core.common.validate.constraint.DateTimeStringConstraint;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * todo 支持 bindResult, 参照下hibernate的扩展注解
 * todo 支持messageSource
 */
@Constraint(validatedBy = DateTimeStringConstraint.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeString {
    String message() default "不是合法的日期字符串";

    Class<?>[] groups() default {};

    Class<? extends javax.validation.Payload>[] payload() default {};
}