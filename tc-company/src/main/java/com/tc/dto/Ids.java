package com.tc.dto;

import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

public class Ids {

    @NotBlank(message = "集合必须存在元素")
    private List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
