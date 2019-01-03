package com.tc.dto.resource;

import com.tc.db.entity.Resource;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * 用户选择的资源
 * @author Cyg
 */
public class SelectAddResource {
    @NotBlank
    private List<Resource> list;

    public List<Resource> getList() {
        return list;
    }

    public void setList(List<Resource> list) {
        this.list = list;
    }
}
