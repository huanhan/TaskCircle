package com.tc.dto.resource;

import com.tc.db.entity.Resource;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * 用户选择的资源
 * @author Cyg
 */
public class SelectAddResource {

    private Long creation;

    @NotEmpty
    private List<Resource> list;

    public Long getCreation() {
        return creation;
    }

    public void setCreation(Long creation) {
        this.creation = creation;
    }

    public List<Resource> getList() {
        return list;
    }

    public void setList(List<Resource> list) {
        this.list = list;
    }
}
