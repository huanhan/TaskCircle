package com.tc.dto.authority;

import com.tc.db.enums.UserCategory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public class RemoveUser {
    /**
     * 标识
     */
    @NotNull
    @Min(value = 1)
    private Long id;

    /**
     * 操作的集合
     */
    @NotEmpty(message = "集合必须存在元素")
    private List<UserCategory> ids;

    public RemoveUser() {
    }

    public RemoveUser(Long id, List<UserCategory> ids) {
        this.id = id;
        this.ids = ids;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<UserCategory> getIds() {
        return ids;
    }

    public void setIds(List<UserCategory> ids) {
        this.ids = ids;
    }

}
