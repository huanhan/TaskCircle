package com.tc.exception;

import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class ValidException extends RuntimeException {

    private List<FieldError> errors = new ArrayList<>();

    public ValidException(String message) {
        super(message);
    }

    public ValidException(List<FieldError> errors) {
        super("");
        this.errors.addAll(errors);
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldError> errors) {
        this.errors = errors;
    }
}
