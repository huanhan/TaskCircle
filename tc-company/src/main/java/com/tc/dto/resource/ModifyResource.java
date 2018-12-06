package com.tc.dto.resource;

import com.tc.db.entity.Resource;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Cyg
 * 修改资源交互
 */
public class ModifyResource extends BasicResource{

    @NotNull(message = "标识不能为空")
    private Long id;
    @NotBlank(message = "资源标识不能为空")
    @Size(max = 30,message = "最大值30")
    private String name;

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

    @Override
    public Resource toResource(BasicResource addResource) {
        Resource resource = super.toResource(addResource);
        resource.setId(id);
        resource.setName(name);
        return resource;
    }

    public static BasicResource newBasicResource(){
        return new ModifyResource();
    }
}
