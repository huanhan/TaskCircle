package com.tc.dto.resource;

import com.tc.db.entity.Resource;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * @author Cyg
 */
public class BasicResource {
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

    public Resource toResource(BasicResource addResource){
        Resource resource = Resource.newResource();
        resource.setClassName(addResource.className);
        resource.setMethod(addResource.method);
        resource.setPath(addResource.path);
        resource.setType(addResource.type);
        resource.setInfo(addResource.info);
        return resource;
    }

}
