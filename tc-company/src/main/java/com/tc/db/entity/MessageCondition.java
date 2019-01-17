package com.tc.db.entity;

import com.tc.db.entity.pk.MessageConditionPK;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "message_condition", schema = "tc-company")
@IdClass(MessageConditionPK.class)
public class MessageCondition {
    private Long conditionId;
    private Long messageId;
    private Condition condition;
    private Message message;

    @Id
    @Column(name = "condition_id")
    public Long getConditionId() {
        return conditionId;
    }

    public void setConditionId(Long conditionId) {
        this.conditionId = conditionId;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageCondition that = (MessageCondition) o;
        return conditionId.equals(that.conditionId) &&
                messageId.equals(that.messageId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(conditionId, messageId);
    }

    @ManyToOne
    @JoinColumn(name = "condition_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @ManyToOne
    @JoinColumn(name = "message_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
