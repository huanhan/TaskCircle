package com.tc.dto.resource;

import com.tc.db.entity.Resource;
import com.tc.db.entity.User;
import com.tc.dto.enums.ResourceState;

import java.sql.Timestamp;

public class ResourceDetail extends Resource{

    private String resourceState = ResourceState.CONTROLLER_HAS_NONE.getState();
    private boolean isNormal = false;

    @Override
    public String getResourceState() {
        return resourceState;
    }

    @Override
    public void setResourceState(String resourceState) {
        this.resourceState = resourceState;
    }

    @Override
    public boolean isNormal() {
        return isNormal;
    }

    @Override
    public void setNormal(boolean normal) {
        isNormal = normal;
    }

    public static ResourceDetail byResource(Resource resource){
        ResourceDetail resourceDetail = (ResourceDetail) resource;
//        resourceDetail.setId(resource.getId());
//        resourceDetail.setClassName(resource.getClassName());
//        resourceDetail.setCreateTime(resource.getCreateTime());
        resourceDetail.setCreation(new User(resource.getCreation().getId(),resource.getCreation().getName(),resource.getCreation().getUsername()));
//        resourceDetail.setInfo(resource.getInfo());
//        resourceDetail.setMethod(resource.getMethod());
//        resourceDetail.setName(resource.getName());
//        resourceDetail.setPath(resource.getPath());
//        resourceDetail.setType(resource.getType());
        return resourceDetail;
    }
}
