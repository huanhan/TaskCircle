package com.tc.validator.impl;

import com.tc.validator.IDCard;
import com.tc.until.IDCardHelper;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IDCardValidator implements ConstraintValidator<IDCard,Object> {
    @Override
    public void initialize(IDCard idCard) {

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o == null || StringUtils.isEmpty(o.toString())){
            return true;
        }
        return IDCardHelper.isValidIdNo(o.toString());
    }
}
