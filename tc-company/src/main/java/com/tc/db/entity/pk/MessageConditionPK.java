package com.tc.db.entity.pk;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class MessageConditionPK implements Serializable {
    private long conditionId;
    private long messageId;

    @Column(name = "condition_id")
    @Id
    public long getConditionId() {
        return conditionId;
    }

    public void setConditionId(long conditionId) {
        this.conditionId = conditionId;
    }

    @Column(name = "message_id")
    @Id
    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageConditionPK that = (MessageConditionPK) o;
        return conditionId == that.conditionId &&
                messageId == that.messageId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(conditionId, messageId);
    }
}
