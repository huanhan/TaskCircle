package com.tc.db.entity.pk;

import com.tc.db.entity.Message;
import com.tc.db.entity.User;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class UserMessagePK implements Serializable {
    private Message message;
    private User user;

    @Column(name = "message_id")
    @Id
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message messageByMessageId) {
        this.message = messageByMessageId;
    }

    @Column(name = "user_id")
    @Id
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMessagePK that = (UserMessagePK) o;
        return message.getId().equals(that.getMessage().getId()) &&
                user.getId().equals(that.getUser().getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(message.getId(), user.getId());
    }
}
