package com.tc.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author Cyg
 * 用户操作错误日志记录实体
 */
@Entity
@Table(name = "user_operation_log", schema = "tc-company")
public class UserOperationLog implements Serializable {
    private String id;
    private Timestamp operationTime;
    private String form;
    private String context;
    private String operationMode;
    private User user;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "operation_time")
    public Timestamp getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Timestamp operationTime) {
        this.operationTime = operationTime;
    }

    @Basic
    @Column(name = "form")
    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
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
    @Column(name = "operation_mode")
    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        UserOperationLog that = (UserOperationLog) o;
        return user.getId().equals(that.getUser().getId()) &&
                Objects.equals(id, that.getId()) &&
                Objects.equals(operationTime, that.getOperationTime()) &&
                Objects.equals(form, that.getForm()) &&
                Objects.equals(context, that.getContext()) &&
                Objects.equals(operationMode, that.getOperationMode());
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, user.getId(), operationTime, form, context, operationMode);
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User userByUserId) {
        this.user = userByUserId;
    }
}
