package com.tc.validator.impl;

import com.tc.validator.IDCard;
import com.tc.until.IDCardHelper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IDCardValidator implements ConstraintValidator<IDCard,Object> {
    @Override
    public void initialize(IDCard idCard) {

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return IDCardHelper.isValidIdNo(o.toString());
    }
}
