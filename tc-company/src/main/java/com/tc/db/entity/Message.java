package com.tc.db.entity;

import com.tc.db.enums.MessageState;
import com.tc.db.enums.MessageType;
import com.tc.until.ListUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

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
    private MessageState state;
    private MessageType type;
    private String lookCondition;

    private long sendCount = 0;
    private long lookCount = 0;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @CreationTimestamp
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
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    public MessageState getState() {
        return state;
    }

    public void setState(MessageState state) {
        this.state = state;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    @Basic
    @Column(name = "look_condition")
    public String getLookCondition() {
        return lookCondition;
    }

    public void setLookCondition(String lookCondition) {
        this.lookCondition = lookCondition;
    }

    @Transient
    public long getSendCount() {
        return sendCount;
    }

    public void setSendCount(long sendCount) {
        this.sendCount = sendCount;
    }

    @Transient
    public long getLookCount() {
        return lookCount;
    }

    public void setLookCount(long lookCount) {
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
                Objects.equals(state, message.state);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, context, title, createTime, creation, state);
    }

}
