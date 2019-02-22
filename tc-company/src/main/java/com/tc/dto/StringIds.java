package com.tc.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public class StringIds {
    @NotNull
    @Min(value = 1)
    private Long id;

    @NotEmpty(message = "集合必须存在元素")
    private List<String> ids;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
