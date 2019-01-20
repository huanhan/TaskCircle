package com.tc.dto.message;


import com.tc.db.entity.Message;
import com.tc.db.enums.MessageType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 条件
 * @author Cyg
 */
public class MyCondition {

    @NotNull
    @Min(1)
    private Long id;
    @NotNull
    private MessageType messageType;
    @Length(max = 4096)
    private String context;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public static Message toMessage(Message message, MyCondition condition) {
        message.setLookCondition(condition.context);
        message.setType(condition.messageType);
        return message;
    }
}
