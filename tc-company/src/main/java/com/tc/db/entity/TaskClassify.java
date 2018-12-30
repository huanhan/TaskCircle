package com.tc.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Cyg
 * 任务分类实体
 */
@Entity
@Table(name = "task_classify", schema = "tc-company", catalog = "")
public class TaskClassify implements Serializable {
    private Long id;
    private String name;
    private Admin creation;
    private Timestamp createTime;
    private TaskClassify parents;
    private String info;
    private Collection<TaskClassifyRelation> taskClassifyRelations;
    private Collection<TaskClassify> taskClassifies;

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
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @ManyToOne
    @JoinColumn(name = "parents_id",referencedColumnName = "id",nullable = false)
    public TaskClassify getParents() {
        return parents;
    }

    public void setParents(TaskClassify parents) {
        this.parents = parents;
    }

    @Basic
    @Column(name = "info")
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        TaskClassify that = (TaskClassify) o;
        return id.equals(that.getId()) &&
                creation.getUser().getId().equals(that.getCreation().getUser().getId()) &&
                parents.getId().equals(that.getParents().getId()) &&
                Objects.equals(name, that.name) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(info, that.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createTime, creation, parents.getId(), info);
    }

    @OneToMany(mappedBy = "taskClassify")
    public Collection<TaskClassifyRelation> getTaskClassifyRelations() {
        return taskClassifyRelations;
    }

    public void setTaskClassifyRelations(Collection<TaskClassifyRelation> taskClassifyRelationsById) {
        this.taskClassifyRelations = taskClassifyRelationsById;
    }

    @OneToMany(mappedBy = "parents")
    public Collection<TaskClassify> getTaskClassifies() {
        return taskClassifies;
    }

    public void setTaskClassifies(Collection<TaskClassify> taskClassifies) {
        this.taskClassifies = taskClassifies;
    }

    @ManyToOne
    @JoinColumn(name = "creation",referencedColumnName = "user_id",nullable = false,insertable = false,updatable = false)
    public Admin getCreation() {
        return creation;
    }

    public void setCreation(Admin creation) {
        this.creation = creation;
    }
}
