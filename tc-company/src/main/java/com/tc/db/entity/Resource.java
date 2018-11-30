package com.tc.db.entity;

import com.tc.service.impl.ResourceServiceImpl;
import com.tc.validator.Name;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
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

    public static final String SORT_CREATETIME = "createTime";

    private Long id;
    private User creation;
    @NotBlank(message = "资源标识不能为空")
    @Size(max = 30,message = "最大值30")
    @Name(message = "存在相同标识",service = ResourceServiceImpl.class)
    private String name;
    @NotBlank(message = "资源路径不能为空")
    @Size(max = 100,message = "最大值100")
    private String path;
    @Size(max = 100,message = "最大值100")
    private String info;
    @CreationTimestamp
    private Timestamp createTime;
    private Collection<AuthorityResource> authorityResources;
    @NotBlank(message = "方法名不能为空")
    @Size(max = 30,message = "最大值30")
    private String method;
    @NotBlank(message = "类型不能为空")
    @Size(max = 20,message = "最大值20")
    private String type;
    @NotBlank(message = "类名不能为空")
    @Size(max = 50,message = "最大值50")
    private String className;

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
}
