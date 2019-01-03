package com.tc.dto.resource;

import com.tc.db.entity.Resource;
import com.tc.db.entity.User;
import com.tc.dto.enums.ResourceState;

import java.sql.Timestamp;

public class ResourceDetail {
    private Long id;
    private User creation;
    private String name;
    private String path;
    private String info;
    private Timestamp createTime;
    private String method;
    private String type;
    private String className;
    private String resourceState = ResourceState.CONTROLLER_HAS_NONE.getState();
    private boolean isNormal = false;


    public String getResourceState() {
        return resourceState;
    }

    public void setResourceState(String resourceState) {
        this.resourceState = resourceState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreation() {
        return creation;
    }

    public void setCreation(User creation) {
        this.creation = creation;
    }

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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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

    public boolean isNormal() {
        return isNormal;
    }

    public void setNormal(boolean normal) {
        isNormal = normal;
    }

    public static ResourceDetail byResource(Resource resource){
        ResourceDetail resourceDetail = new ResourceDetail();
        resourceDetail.setId(resource.getId());
        resourceDetail.setClassName(resource.getClassName());
        resourceDetail.setCreateTime(resource.getCreateTime());
        resourceDetail.setCreation(new User(resource.getCreation().getId(),resource.getCreation().getName(),resource.getCreation().getUsername()));
        resourceDetail.setInfo(resource.getInfo());
        resourceDetail.setMethod(resource.getMethod());
        resourceDetail.setName(resource.getName());
        resourceDetail.setPath(resource.getPath());
        resourceDetail.setType(resource.getType());
        return resourceDetail;
    }
}
