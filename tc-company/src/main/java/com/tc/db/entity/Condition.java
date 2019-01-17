package com.tc.db.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Condition {
    private long id;
    private String context;
    private String name;
    private String value;
    private Timestamp creationTime;
    private long adminId;
    private Admin admin;
    private Collection<MessageCondition> messageConditions;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "context")
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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
    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Basic
    @Column(name = "creation_time")
    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    @Basic
    @Column(name = "admin_id")
    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Condition condition = (Condition) o;
        return id == condition.id &&
                adminId == condition.adminId &&
                Objects.equals(context, condition.context) &&
                Objects.equals(name, condition.name) &&
                Objects.equals(value, condition.value) &&
                Objects.equals(creationTime, condition.creationTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, context, name, value, creationTime, adminId);
    }

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "user_id", nullable = false,insertable = false,updatable = false)
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @OneToMany(mappedBy = "condition")
    public Collection<MessageCondition> getMessageConditions() {
        return messageConditions;
    }

    public void setMessageConditions(Collection<MessageCondition> messageConditions) {
        this.messageConditions = messageConditions;
    }
}
