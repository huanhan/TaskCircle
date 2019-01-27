package com.tc.dto.enums;

public enum IEType {
    IN("转入"),
    OUT("转出"),

    ;
    private String type;

    IEType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
