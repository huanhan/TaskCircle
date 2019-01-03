package com.tc.dto;

import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

public class Ids {

    @NotBlank(message = "集合必须存在元素")
    private List<Long> lIds;

    private List<String> sIds;

    public List<Long> getlIds() {
        return lIds;
    }

    public void setlIds(List<Long> lIds) {
        this.lIds = lIds;
    }

    public List<String> getsIds() {
        return sIds;
    }

    public void setsIds(List<String> sIds) {
        this.sIds = sIds;
    }
}
