package com.tc.dto.resource;

import com.tc.db.entity.Resource;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Cyg
 * 修改资源交互
 */
public class ModifyResource{

    private boolean isModify = false;

    @NotNull(message = "标识不能为空")
    private Long id;

    @NotBlank(message = "资源标识不能为空")
    @Size(max = 100,message = "最大值100")
    private String name;

    @Size(max = 100,message = "最大值100")
    private String info;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isModify() {
        return isModify;
    }


    public Resource toResource(Resource oldResource) {

        if (!StringUtils.isEmpty(name)) {
            oldResource.setName(name);
            isModify = true;
        }
        if (!StringUtils.equals(oldResource.getInfo(),info)) {
            oldResource.setInfo(info);
            isModify = true;
        }
        return oldResource;

    }
}
