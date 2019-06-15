package cn.bfreeman.core.common.validate;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * @Author : lhr
 * @Date : 17:59 2019/6/13
 */
public interface Validator {

    /**
     * 校验添加了JSR校验注解的对象
     *
     * @param t   校验的对象
     * @param <T> 对象类型
     * @return 校验的对象
     */
    <T> T validate(T t);

    /**
     * 取手动配置的错误信息
     *
     * @param constraintViolations
     * @return
     */
    String message(Set<ConstraintViolation<?>> constraintViolations);
}
