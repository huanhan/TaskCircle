package com.tc.dto;

public class TransEnum {
    private String key;
    private String value;

    public TransEnum() {
    }

    public TransEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static TransEnum init(String key,String value){
        return new TransEnum(key,value);
    }

}
