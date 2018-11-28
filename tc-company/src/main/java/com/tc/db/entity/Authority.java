package com.tc.db.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
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
    private Admin admin;
    private Collection<UserAuthority> userAuthorities;
    private Collection<AuthorityResource> authorityResources;

    @Id
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
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }



    @OneToMany(mappedBy = "authority")
    public Collection<UserAuthority> getUserAuthorities() {
        return userAuthorities;
    }

    public void setUserAuthorities(Collection<UserAuthority> userAuthorities) {
        this.userAuthorities = userAuthorities;
    }

    @ManyToOne
    @JoinColumn(name = "creation", referencedColumnName = "user_id", nullable = false)
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin creation) {
        this.admin = creation;
    }

    @OneToMany(mappedBy = "authority")
    public Collection<AuthorityResource> getAuthorityResources() {
        return authorityResources;
    }

    public void setAuthorityResources(Collection<AuthorityResource> authorityResourcesById) {
        this.authorityResources = authorityResourcesById;
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
}
