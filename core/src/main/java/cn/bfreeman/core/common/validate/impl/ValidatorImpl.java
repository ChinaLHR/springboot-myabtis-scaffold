package cn.bfreeman.core.common.validate.impl;

import cn.bfreeman.common.exception.FatalException;
import cn.bfreeman.core.common.validate.Validator;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import strman.Strman;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author : lhr
 * @Date : 18:17 2019/6/13
 */
@Component
public class ValidatorImpl implements Validator {

    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final javax.validation.Validator VALIDATOR = FACTORY.getValidator();
    private static final Joiner RETURN_JOINER = Joiner.on('\n').skipNulls();

    @Override
    public <T> T validate(T t) {
        Preconditions.checkNotNull(t, "被校验参数不能为空");
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(t);

        if (!violations.isEmpty()) {
            String msg = RETURN_JOINER.join(violations.stream().map(buildMessage()).collect(Collectors.toList()));
            throw new FatalException(msg);
        }
        return t;
    }

    @Override
    public String message(Set<ConstraintViolation<?>> constraintViolations) {
        String msg = RETURN_JOINER.join(constraintViolations.stream()
                .map(ConstraintViolation::getMessageTemplate)
                .collect(Collectors.toList()));
        return msg;
    }

    private static String SPACE = " ";

    private Function<ConstraintViolation<?>, String> buildMessage() {
        return constraintViolation -> {
            String append = Strman.append(constraintViolation.getPropertyPath().toString(),
                    SPACE, constraintViolation.getMessage());
            return append;
        };
    }
}
