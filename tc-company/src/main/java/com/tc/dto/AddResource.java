package com.tc.dto;

import com.tc.db.entity.AuthorityResource;
import com.tc.db.entity.Resource;
import com.tc.db.entity.User;
import com.tc.service.impl.ResourceServiceImpl;
import com.tc.validator.Name;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

public class AddResource {
    @NotBlank(message = "资源标识不能为空")
    @Size(max = 30,message = "最大值30")
    @Name(message = "存在相同标识",service = ResourceServiceImpl.class)
    private String name;
    @NotBlank(message = "资源路径不能为空")
    @Size(max = 100,message = "最大值100")
    private String path;
    @Size(max = 100,message = "最大值100")
    private String info;
    @NotBlank(message = "方法名不能为空")
    @Size(max = 30,message = "最大值30")
    private String method;
    @NotBlank(message = "类型不能为空")
    @Size(max = 20,message = "最大值20")
    private String type;
    @NotBlank(message = "类名不能为空")
    @Size(max = 50,message = "最大值50")
    private String className;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    public static Resource toResource(AddResource addResource){
        Resource resource = Resource.newResource();
        resource.setName(addResource.name);
        resource.setClassName(addResource.className);
        resource.setMethod(addResource.method);
        resource.setPath(addResource.path);
        resource.setType(addResource.type);
        resource.setInfo(addResource.info);
        return resource;
    }
}
