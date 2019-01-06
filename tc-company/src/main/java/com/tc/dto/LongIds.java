package com.tc.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Cyg
 */
public class LongIds {

    @NotNull
    @Min(value = 1)
    private Long id;

    @NotEmpty(message = "集合必须存在元素")
    private List<Long> ids;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

}
