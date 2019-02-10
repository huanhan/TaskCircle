package com.tc.security.core.support;

public class SimpleResponse {
    private Object message;

    public SimpleResponse(Object content) {
        this.message = content;
    }

    public Object getContent() {
        return message;
    }

    public void setContent(Object message) {
        this.message = message;
    }
}
