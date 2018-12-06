package com.tc.dto.resource;

import com.tc.db.entity.Resource;
import com.tc.service.impl.ResourceServiceImpl;
import com.tc.validator.Name;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * @author Cyg
 * 添加资源交互
 */
public class AddResource extends BasicResource{
    @NotBlank(message = "资源标识不能为空")
    @Size(max = 30,message = "最大值30")
    @Name(message = "存在相同标识",service = ResourceServiceImpl.class)
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Resource toResource(BasicResource addResource) {
        Resource resource = super.toResource(addResource);
        resource.setName(name);
        return resource;
    }

    public static BasicResource newBasicResource(){
        return new AddResource();
    }
}
