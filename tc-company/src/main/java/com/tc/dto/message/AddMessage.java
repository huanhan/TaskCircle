package com.tc.dto.message;

import com.tc.db.entity.Message;
import com.tc.db.enums.MessageState;
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

    @NotNull
    @Min(1)
    private Long creationId;
    @NotEmpty
    @Length(max = 255)
    private String context;
    @NotEmpty
    @Length(max = 30)
    private String title;



    public Long getCreationId() {
        return creationId;
    }

    public void setCreationId(Long creationId) {
        this.creationId = creationId;
    }

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


    public static Message toMessage(AddMessage addMessage) {
        Message message = new Message();
        BeanUtils.copyProperties(addMessage,message);
        message.setState(MessageState.STOP);
        return message;
    }
}
