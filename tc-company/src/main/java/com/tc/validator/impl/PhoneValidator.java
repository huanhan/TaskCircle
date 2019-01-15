package com.tc.validator.impl;

import com.tc.until.TelephoneUtil;
import com.tc.validator.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone,Object> {

    @Override
    public void initialize(Phone phone) {

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return TelephoneUtil.isAlpha((String) o,TelephoneUtil.TelephoneType.Telephone);
    }
}
