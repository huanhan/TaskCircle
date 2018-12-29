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
    private long authorityId;
    private long resourceId;



    public AuthorityResource() {
    }

    public AuthorityResource(Long aId,Long rId) {

        authority = new Authority(aId);
        resource = new Resource(rId);
        authorityId = aId;
        resourceId = rId;

    }

    public AuthorityResource(Authority authority, Resource resource) {
        this.authority = authority;
        this.resource = resource;
        authorityId = authority.getId();
        resourceId = resource.getId();
    }


    @Id
    @Column(name = "authority_id")
    public long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(long authorityId) {
        this.authorityId = authorityId;
    }

    @Id
    @Column(name = "resource_id")
    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    @ManyToOne
    @JoinColumn(name = "authority_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authorityByAuthorityId) {
        this.authority = authorityByAuthorityId;
    }

    @ManyToOne
    @JoinColumn(name = "resource_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
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
