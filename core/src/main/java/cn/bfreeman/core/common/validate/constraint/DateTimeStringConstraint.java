package cn.bfreeman.core.common.validate.constraint;


import cn.bfreeman.common.util.DateUtil;
import cn.bfreeman.core.common.validate.DateTimeString;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateTimeStringConstraint implements ConstraintValidator<DateTimeString, String> {


    @Override
    public void initialize(DateTimeString dateTimeString) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(s)) {
            return false;
        }
        try {
            DateUtil.parseDateTimeString(s);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}