package com.tc.dto.message;

import com.tc.db.entity.Message;
import com.tc.db.enums.MessageState;
import com.tc.db.enums.MessageType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 添加消息
 * @author Cyg
 */
public class AddMessage {

    @NotEmpty
    @Length(max = 255)
    private String context;
    @NotEmpty
    @Length(max = 30)
    private String title;


    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public static Message toMessage(AddMessage addMessage, Long me) {
        Message message = new Message();
        BeanUtils.copyProperties(addMessage,message);
        message.setState(MessageState.STOP);
        message.setCreationId(me);
        message.setType(MessageType.IS_NULL);
        return message;
    }
}
