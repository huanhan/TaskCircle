package com.tc.validator.impl;

import com.tc.service.BasicService;
import com.tc.validator.Name;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

@Component
public class NameValidator implements ConstraintValidator<Name,Object> {

    @Autowired
    private Set<BasicService> basicServices;

    private BasicService basicService;


    @Override
    public void initialize(Name name) {

        for (BasicService b :
                basicServices) {
            if (StringUtils.startsWithIgnoreCase(b.getClass().getName(),name.service().getName())){
                basicService = b;
            }
        }

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (basicService.isNullByName((String)o)){return true;}
        return false;
    }
}
