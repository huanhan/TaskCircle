package com.tc.validator.impl;

import com.tc.exception.ValidException;
import com.tc.service.BasicService;
import com.tc.validator.Name;
import com.tc.validator.until.StringResourceCenter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<Name,Object> {

    private BasicService basicService;

    @Override
    public void initialize(Name name) {
        try {
            basicService = name.service().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ValidException(StringResourceCenter.SERVICE_INIT_FAILED);
        }
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (basicService.isNullByName((String)o)){return true;}
        return false;
    }
}
