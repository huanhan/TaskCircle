package com.tc.validator.impl;

import com.tc.service.UserService;
import com.tc.validator.Username;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Cyg
 */
public class UsernameValidator implements ConstraintValidator<Username,Object> {


    @Autowired
    private UserService userService;


    @Override
    public void initialize(Username username) {

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return userService.isNullByUsername((String)o);
    }
}
