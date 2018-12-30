package com.tc.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Cyg
 * 用户
 */
@Entity
public class Message implements Serializable {
    private Long id;
    private String context;
    private String title;
    private Timestamp createTime;
    private Admin creation;
    private String type;
    private String state;
    private Collection<UserMessage> userMessage;

    @Id
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
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
    @JoinColumn(name = "creation", referencedColumnName = "user_id", nullable = false,insertable = false,updatable = false)
    public Admin getCreation() {
        return creation;
    }

    public void setCreation(Admin creation) {
        this.creation = creation;
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
    @Column(name = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Message message = (Message) o;
        return id.equals(message.getId()) &&
                creation.getUser().getId().equals(message.getCreation().getUser().getId()) &&
                Objects.equals(context, message.context) &&
                Objects.equals(title, message.title) &&
                Objects.equals(createTime, message.createTime) &&
                Objects.equals(type, message.type) &&
                Objects.equals(state, message.state);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, context, title, createTime, creation, type, state);
    }

    @OneToMany(mappedBy = "message")
    public Collection<UserMessage> getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(Collection<UserMessage> userMessagesById) {
        this.userMessage = userMessagesById;
    }
}
