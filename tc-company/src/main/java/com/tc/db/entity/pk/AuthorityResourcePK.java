package com.tc.db.entity.pk;


import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;


public class AuthorityResourcePK implements Serializable {
    private long authorityId;
    private long resourceId;

    @Column(name = "authority_id")
    @Id
    public long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(long authorityId) {
        this.authorityId = authorityId;
    }

    @Column(name = "resource_id")
    @Id
    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
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
        return authorityId == that.authorityId &&
                resourceId == that.resourceId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(authorityId, resourceId);
    }
}

