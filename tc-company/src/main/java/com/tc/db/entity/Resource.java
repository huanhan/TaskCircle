package com.tc.db.entity;

import com.tc.dto.enums.ResourceState;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Cyg
 * 资源表
 */
@NamedEntityGraph(name = "resource.all",attributeNodes = {
        @NamedAttributeNode("creation")
})
@Entity
public class Resource implements Serializable {

    public static final String SORT_CREATETIME = "createTime";
    public static final String SORT_CLASSNAME = "className";


    private Long id;
    private User creation;
    private String name;
    private String path;
    private String info;
    private Timestamp createTime;
    private Collection<AuthorityResource> authorityResources;
    private String method;
    private String type;
    private String className;

    private String resourceState = ResourceState.CONTROLLER_HAS_NONE.getState();
    private boolean isNormal = false;

    public Resource() {
    }

    public Resource(Long id) {
        this.id = id;
    }

    public Resource(Long id, String name) {
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
    @CreationTimestamp
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

    @Basic
    @Column(name = "method")
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "class_name")
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Transient
    public String getResourceState() {
        return resourceState;
    }

    public void setResourceState(String resourceState) {
        this.resourceState = resourceState;
    }

    @Transient
    public boolean isNormal() {
        return isNormal;
    }

    public void setNormal(boolean normal) {
        isNormal = normal;
    }


    public static Resource newResource(){
        return new Resource();
    }


    public void initResource() {

        this.setCreation(new User(this.getCreation().getId(), this.getCreation().getName()));
        if (!this.getAuthorityResources().isEmpty()){
            this.getAuthorityResources().forEach(ar -> {
                ar.setAuthority(new Authority(ar.getAuthority().getId(),ar.getAuthority().getName()));
                ar.setResource(null);
            });
        }

    }

    public static List<Long> toKeys(List<Resource> resources){
        List<Long> result = new ArrayList<>();
        if (!resources.isEmpty()){
            resources.forEach(resource -> result.add(resource.getId()));
        }
        return result;
    }

}
