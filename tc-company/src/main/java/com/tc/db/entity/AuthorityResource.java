package com.tc.db.entity;

import com.tc.db.entity.pk.AuthorityResourcePK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Cyg
 * 权限与资源关系实体
 */
@Entity
@Table(name = "authority_resource", schema = "tc-company")
@IdClass(AuthorityResourcePK.class)
public class AuthorityResource implements Serializable {

    private Authority authority;
    private Resource resource;

    @Id
    @ManyToOne
    @JoinColumn(name = "authority_id", referencedColumnName = "id", nullable = false)
    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authorityByAuthorityId) {
        this.authority = authorityByAuthorityId;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "resource_id", referencedColumnName = "id", nullable = false)
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
        AuthorityResource that = (AuthorityResource) o;
        return authority.getId().equals(that.getAuthority().getId()) &&
                resource.getId() == that.getResource().getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(authority.getId(), resource.getId());
    }
}
