package com.tc.db.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Cyg
 * 资源表
 */
@Entity
public class Resource implements Serializable {

    private Long id;
    private User creation;
    private String name;
    private String path;
    private String info;
    @CreationTimestamp
    private Timestamp createTime;
    private Collection<AuthorityResource> authorityResources;


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
    @Column(name = "path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Resource resource = (Resource) o;
        return id.equals(resource.getId()) &&
                creation.getId().equals(resource.getCreation().getId()) &&
                Objects.equals(name, resource.name) &&
                Objects.equals(path, resource.path) &&
                Objects.equals(info, resource.info) &&
                Objects.equals(createTime, resource.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, path, info, creation.getId(), createTime);
    }

    @OneToMany(mappedBy = "resource")
    public Collection<AuthorityResource> getAuthorityResources() {
        return authorityResources;
    }

    public void setAuthorityResources(Collection<AuthorityResource> authorityResourcesById) {
        this.authorityResources = authorityResourcesById;
    }

    @ManyToOne
    @JoinColumn(name = "creation", referencedColumnName = "id", nullable = false)
    public User getCreation() {
        return creation;
    }

    public void setCreation(User userByCreation) {
        this.creation = userByCreation;
    }
}
