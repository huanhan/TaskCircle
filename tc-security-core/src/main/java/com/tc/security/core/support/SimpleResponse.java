package com.tc.security.core.support;

public class SimpleResponse {
    private Object message;

    public SimpleResponse(Object content) {
        this.message = content;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
