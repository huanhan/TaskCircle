package com.tc.db.entity.pk;


import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;


/**
 * 权限与资源关系主键
 * @author Cyg
 */
public class AuthorityResourcePK implements Serializable {
    private Long authorityId;
    private Long resourceId;

    @Column(name = "authority_id")
    @Id
    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    @Column(name = "resource_id")
    @Id
    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthorityResourcePK that = (AuthorityResourcePK) o;
        return authorityId.equals(that.authorityId) &&
                resourceId.equals(that.resourceId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(authorityId, resourceId);
    }
}

