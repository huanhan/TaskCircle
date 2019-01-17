package com.tc.db.entity.pk;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class MessageConditionPK implements Serializable {
    private Long conditionId;
    private Long messageId;

    @Column(name = "condition_id")
    @Id
    public Long getConditionId() {
        return conditionId;
    }

    public void setConditionId(Long conditionId) {
        this.conditionId = conditionId;
    }

    @Column(name = "message_id")
    @Id
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
        MessageConditionPK that = (MessageConditionPK) o;
        return conditionId.equals(that.conditionId) &&
                messageId.equals(that.messageId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(conditionId, messageId);
    }
}
