package com.tc.validator.impl;

import com.tc.exception.ValidException;
import com.tc.service.BasicService;
import com.tc.service.impl.AbstractBasicServiceImpl;
import com.tc.validator.Name;
import com.tc.validator.until.StringResourceCenter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class NameValidator implements ConstraintValidator<Name,Object> {

    @Autowired
    private Set<BasicService> basicServices;

    private BasicService basicService;


    @Override
    public void initialize(Name name) {

            for (BasicService b :
                    basicServices) {
                if (b.getClass().getName().equals(name.service().getName())){
                    basicService = b;
                }
            }
            //basicService = name.service().newInstance();

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (basicService.isNullByName((String)o)){return true;}
        return false;
    }
}
