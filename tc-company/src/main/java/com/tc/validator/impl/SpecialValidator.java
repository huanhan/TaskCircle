package com.tc.validator.impl;

import com.tc.until.ValidateUtil;
import com.tc.validator.NoSpecial;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class SpecialValidator implements ConstraintValidator<NoSpecial,Object> {




    @Override
    public void initialize(NoSpecial name) {


    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isEmpty(o.toString())){
            return true;
        }
        return ValidateUtil.isSpecialChar(o.toString());

    }
}
