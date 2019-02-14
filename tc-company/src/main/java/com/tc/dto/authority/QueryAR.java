package com.tc.dto.authority;

import com.tc.until.PageRequest;
import org.springframework.data.domain.Sort;

public class QueryAR extends PageRequest {

    private Long resourceId;
    private Long authorityId;
    private String authorityName;
    private String resourceName;
    private String authorityInfo;

    public QueryAR(){
        super(0,10);
    }

    public QueryAR(int page, int size) {
        super(page, size);
    }

    public QueryAR(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryAR(int page, int size, Sort sort) {
        super(page, size, sort);
    }


    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public String getAuthorityInfo() {
        return authorityInfo;
    }

    public void setAuthorityInfo(String authorityInfo) {
        this.authorityInfo = authorityInfo;
    }
}
