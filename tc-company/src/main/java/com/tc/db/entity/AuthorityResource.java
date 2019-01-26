package com.tc.db.entity;

import com.tc.db.entity.pk.AuthorityResourcePK;
import com.tc.dto.Show;
import com.tc.dto.enums.QueryEnum;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Cyg
 * 权限与资源关系实体
 */
@Entity
@Table(name = "authority_resource", schema = "tc-company")
@IdClass(AuthorityResourcePK.class)
public class AuthorityResource implements Serializable {

    public static final String AUTHORITY = "authority";
    public static final String RESOURCE = "resource";
    public static final String AUTHORITY_ID = "authorityId";
    public static final String RESOURCE_ID = "resourceId";


    private Authority authority;
    private Resource resource;
    private Long authorityId;
    private Long resourceId;



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
    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    @Id
    @Column(name = "resource_id")
    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
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
                resource.getId().equals(that.getResource().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(authority.getId(), resource.getId());
    }

    public static List<Show> toShows(List<AuthorityResource> ars, QueryEnum queryEnum){
        List<Show> result = new ArrayList<>();
        if (!ArrayUtils.isEmpty(ars.toArray())){
            if (queryEnum.equals(QueryEnum.AUTHORITY)) {
                ars.forEach(authorityResource -> result.add(
                        new Show(authorityResource.getAuthorityId(),
                                authorityResource.getAuthority().getName(),
                                authorityResource.getResourceId())
                ));
            }else if (queryEnum.equals(QueryEnum.RESOURCE)){
                ars.forEach(authorityResource -> result.add(
                        new Show(authorityResource.getResourceId(),
                                authorityResource.getAuthority().getName(),
                                authorityResource.getAuthorityId())
                ));
            }
        }
        return result;
    }


    public static List<AuthorityResourcePK> toKeys(List<AuthorityResource> authorityResources){
        List<AuthorityResourcePK> result = new ArrayList<>();
        if (!authorityResources.isEmpty()){
            authorityResources.forEach(authorityResource -> {
                result.add(new AuthorityResourcePK(authorityResource.authorityId,authorityResource.resourceId));
            });
        }
        return result;
    }

    public static List<Long> toKey(List<AuthorityResource> authorityResources,QueryEnum queryEnum){
        List<Long> result = new ArrayList<>();
        if (!authorityResources.isEmpty()){
            if (queryEnum.equals(QueryEnum.RESOURCE)){
                authorityResources.forEach(authorityResource -> {
                    result.add(authorityResource.resourceId);
                });
            }else if (queryEnum.equals(QueryEnum.AUTHORITY)){
                authorityResources.forEach(authorityResource -> {
                    result.add(authorityResource.authorityId);
                });
            }
        }
        return result;
    }
}
