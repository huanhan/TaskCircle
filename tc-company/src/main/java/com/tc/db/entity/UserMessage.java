package com.tc.db.entity;

import com.tc.db.entity.pk.UserMessagePK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Cyg
 * 用户消息接收情况实体
 */
@Entity
@Table(name = "user_message", schema = "tc-company")
@IdClass(UserMessagePK.class)
public class UserMessage implements Serializable {
    private byte isLook;
    private Message message;
    private User user;


    @Id
    @ManyToOne
    @JoinColumn(name = "message_id", referencedColumnName = "id", nullable = false)
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message messageByMessageId) {
        this.message = messageByMessageId;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Basic
    @Column(name = "is_look")
    public byte getIsLook() {
        return isLook;
    }

    public void setIsLook(byte isLook) {
        this.isLook = isLook;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        UserMessage that = (UserMessage) o;
        return message.getId().equals(that.getMessage().getId()) &&
                user.getId().equals(that.getUser().getId()) &&
                isLook == that.isLook;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message.getId(), user.getId(), isLook);
    }


}
