package com.tc.db.entity.pk;


import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * 用户与消息关系主键
 * @author Cyg
 */
public class UserMessagePK implements Serializable {
    private Long userId;
    private Long messageId;

    @Id
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "message_id")
    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserMessagePK that = (UserMessagePK) o;
        return messageId.equals(that.getMessageId()) &&
                userId.equals(that.getUserId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(messageId, userId);
    }
}
