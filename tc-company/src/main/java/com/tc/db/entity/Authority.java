package com.tc.db.entity;

import com.tc.dto.Show;
import com.tc.until.ListUtils;
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

    public static final String ADMIN_AUTHORITY = "adminAuthorities";
    public static final String INFO = "info";

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

    public static Authority toDetail(Authority authority) {
        authority.setAdmin(new Admin(authority.getAdmin().getUserId(),authority.getAdmin().getUser().getName(),authority.getAdmin().getUser().getUsername()));
        authority.setAdminAuthorities(null);
        authority.setAuthorityResources(null);
        authority.setUserAuthorities(null);
        return authority;
    }

    public static List<Authority> toDetail(List<Authority> queryAuthorities) {
        if (!ListUtils.isEmpty(queryAuthorities)){
            queryAuthorities.forEach(Authority::toDetail);
        }
        return queryAuthorities;
    }


    public static List<Long> toKeys(List<Authority> authorities){
        List<Long> result = new ArrayList<>();
        if (!authorities.isEmpty()){
            authorities.forEach(authority -> result.add(authority.getId()));
        }
        return result;
    }

    public static List<Show> toShows(List<Authority> authorities){
        List<Show> result = new ArrayList<>();
        if (!authorities.isEmpty()){
            authorities.forEach(authority -> result.add(new Show(authority.getId(),authority.getName())));
        }
        return result;
    }
}
