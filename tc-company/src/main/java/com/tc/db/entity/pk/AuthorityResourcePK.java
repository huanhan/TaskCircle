package com.tc.db.entity.pk;

import com.tc.db.entity.Authority;
import com.tc.db.entity.Resource;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class AuthorityResourcePK implements Serializable {

    private Authority authority;
    private Resource resource;

    @Column(name = "authority_id")
    @Id
    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authorityByAuthorityId) {
        this.authority = authorityByAuthorityId;
    }

    @Column(name = "resource_id")
    @Id
    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resourceByResourceId) {
        this.resource = resourceByResourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        AuthorityResourcePK that = (AuthorityResourcePK) o;
        return authority.getId().equals(that.getAuthority().getId()) &&
                resource.getId().equals(that.getResource().getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(authority.getId(), resource.getId());
    }
}
