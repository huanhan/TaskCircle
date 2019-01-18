package com.tc.db.entity;

import com.tc.until.ListUtils;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 消息条件实体
 * @author Cyg
 */
@Entity(name = "my_condition")
public class Condition {

    public static final String ID = "id";
    public static final String CONTEXT = "context";
    public static final String NAME = "name";
    public static final String VALUE = "value";
    public static final String CREATION_NEME = "creationTime";
    public static final String ADMIN_ID = "adminId";
    public static final String ADMIN = "admin";


    private Long id;
    private String context;
    private String name;
    private String value;
    private Timestamp creationTime;
    private Long adminId;
    private Admin admin;
    private Collection<MessageCondition> messageConditions;




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
    @CreationTimestamp
    @Column(name = "creation_time")
    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    @Basic
    @Column(name = "admin_id")
    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
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
        return id.equals(condition.id) &&
                adminId.equals(condition.adminId) &&
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

    public static Condition toDetail(Condition condition) {
        if (condition != null){
            if (condition.admin != null){
                condition.admin = new Admin(condition.adminId,condition.admin.getUser());
            }
            condition.messageConditions = null;
        }
        return condition;
    }

    public static List<Condition> toListInIndex(List<Condition> content) {
        if (!ListUtils.isEmpty(content)){
            content.forEach(condition -> {
                condition.messageConditions = null;
                if (condition.admin != null){
                    condition.admin = new Admin(condition.adminId,condition.admin.getUser());
                }
            });
        }
        return content;
    }
}
