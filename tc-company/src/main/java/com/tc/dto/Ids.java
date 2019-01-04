package com.tc.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Cyg
 */
public class Ids {

    @NotNull
    @Min(value = 1)
    private Long id;

    @NotEmpty(message = "集合必须存在元素")
    private List<Long> lIds;

    private List<String> sIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
