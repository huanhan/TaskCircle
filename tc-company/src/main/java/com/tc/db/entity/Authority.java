package com.tc.db.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * @author Cyg
 * 权限表
 */
@Entity
public class Authority implements Serializable,GrantedAuthority {

    private Long id;
    private String name;
    private String info;
    private Timestamp createTime;
    private Long creation;
    private Admin admin;
    private Collection<UserAuthority> userAuthorities;
    private Collection<AuthorityResource> authorityResources;
    private Collection<AdminAuthority> adminAuthorities;
    public Authority() {
    }

    public Authority(Long id) {
        this.id = id;
    }

    public Authority(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "info")
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Basic
    @CreationTimestamp
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "creation")
    public Long getCreation() {
        return creation;
    }

    public void setCreation(Long creation) {
        this.creation = creation;
    }

    @ManyToOne
    @JoinColumn(name = "creation", referencedColumnName = "user_id", nullable = false,insertable = false,updatable = false)
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin creation) {
        this.admin = creation;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Authority authority = (Authority) o;
        return id.equals(authority.id) &&
                admin.getUser().getId().equals(authority.getAdmin().getUser().getId()) &&
                Objects.equals(name, authority.name) &&
                Objects.equals(info, authority.info) &&
                Objects.equals(createTime, authority.createTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, info, admin.getUser().getId(), createTime);
    }

    @Override
    @Transient
    public String getAuthority() {
        return this.name;
    }


    @OneToMany(mappedBy = "authority",cascade = CascadeType.REMOVE)
    public Collection<UserAuthority> getUserAuthorities() {
        return userAuthorities;
    }

    public void setUserAuthorities(Collection<UserAuthority> userAuthorities) {
        this.userAuthorities = userAuthorities;
    }

    @OneToMany(mappedBy = "authority",cascade = CascadeType.REMOVE)
    public Collection<AuthorityResource> getAuthorityResources() {
        return authorityResources;
    }

    public void setAuthorityResources(Collection<AuthorityResource> authorityResourcesById) {
        this.authorityResources = authorityResourcesById;
    }

    @OneToMany(mappedBy = "authority",cascade = CascadeType.REMOVE)
    public Collection<AdminAuthority> getAdminAuthorities() {
        return adminAuthorities;
    }

    public void setAdminAuthorities(Collection<AdminAuthority> adminAuthorities) {
        this.adminAuthorities = adminAuthorities;
    }

    public static Authority reset(Authority authority){
        authority.setAdmin(new Admin(authority.getAdmin().getUserId(),authority.getAdmin().getUser().getName(),authority.getAdmin().getUser().getUsername()));
        authority.setAdminAuthorities(null);
        authority.setAuthorityResources(null);
        authority.setUserAuthorities(null);
        return authority;
//        if (!userAuthorities.isEmpty()){
//            userAuthorities.forEach(userAuthority -> userAuthority.setAuthority(new Authority(userAuthority.getAuthorityId(),userAuthority.getAuthority().getName())));
//        }
//        if (!authorityResources.isEmpty()){
//            authorityResources.forEach(authorityResource -> {
//                authorityResource.setResource(new Resource(authorityResource.getResourceId(),authorityResource.getResource().getName()));
//                authorityResource.setAuthority(new Authority(authorityResource.getAuthorityId(),authorityResource.getAuthority().getName()));
//            });
//        }
//        if (!adminAuthorities.isEmpty()){
//            adminAuthorities.forEach(adminAuthority -> {
//                adminAuthority.setAdmin(new Admin(adminAuthority.getUserId(),
//                        adminAuthority.getAdmin().getUser().getName(),
//                        adminAuthority.getAdmin().getUser().getUsername()
//                        )
//                );
//                adminAuthority.setAuthority(new Authority(adminAuthority.getAuthorityId(),adminAuthority.getAuthority().getName()));
//            });
//        }
    }


    public static List<Long> toKeys(List<Authority> authorities){
        List<Long> result = new ArrayList<>();
        if (!authorities.isEmpty()){
            authorities.forEach(authority -> result.add(authority.getId()));
        }
        return result;
    }

}
