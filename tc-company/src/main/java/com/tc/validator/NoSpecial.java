package com.tc.validator;

import com.tc.validator.impl.SpecialValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SpecialValidator.class)
public @interface NoSpecial {
    String message() default "违规输入";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
