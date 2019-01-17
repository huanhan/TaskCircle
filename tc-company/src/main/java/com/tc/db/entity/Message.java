package com.tc.db.entity;

import com.tc.until.ListUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Cyg
 * 用户
 */
@Entity
public class Message implements Serializable {

    public static final String ID = "id";
    public static final String CONTEXT = "context";
    public static final String TITLE = "title";
    public static final String CREATE_TIME = "createTime";
    public static final String CREATION = "creation";
    public static final String TYPE = "type";
    public static final String STATE = "state";


    private Long id;
    private Long creationId;
    private String context;
    private String title;
    private Timestamp createTime;
    private Admin creation;
    private String type;
    private String state;

    private int sendCount = 0;
    private int lookCount = 0;
    private Collection<MessageCondition> messageConditions;

    public static List<Message> toListByIndex(List<Message> content) {
        if (!ListUtils.isEmpty(content)){
            content.forEach(message -> {
                if (message.creation != null){
                    message.setCreation(new Admin(message.getCreation().getUserId(),message.getCreation().getUser()));
                }
            });
        }
        return content;
    }

    public static Message toDetail(Message message) {
        if (message != null){
            if (message.getCreation() != null){
                message.setCreation(new Admin(message.getCreation().getUserId(),message.getCreation().getUser()));
            }
        }
        return message;
    }

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "creation")
    public Long getCreationId() {
        return creationId;
    }

    public void setCreationId(Long creationId) {
        this.creationId = creationId;
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

    @Transient
    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

    @Transient
    public int getLookCount() {
        return lookCount;
    }

    public void setLookCount(int lookCount) {
        this.lookCount = lookCount;
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
    public Collection<MessageCondition> getMessageConditions() {
        return messageConditions;
    }

    public void setMessageConditions(Collection<MessageCondition> messageConditions) {
        this.messageConditions = messageConditions;
    }
}
