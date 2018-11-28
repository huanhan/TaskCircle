package com.tc.validator;

import com.tc.validator.impl.IDCardValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IDCardValidator.class)
public @interface IDCard {
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
