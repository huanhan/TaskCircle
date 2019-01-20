package com.tc.dto.message;

import com.tc.db.entity.Message;
import com.tc.db.enums.MessageState;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 修改消息
 * @author Cyg
 */
public class ModifyMessage {
    @NotNull
    @Min(1)
    private Long id;
    @NotEmpty
    @Length(max = 255)
    private String context;
    @NotEmpty
    @Length(max = 30)
    private String title;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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



    public static boolean isUpdate(Message message, ModifyMessage modifyMessage) {
        boolean isUpdate = false;
        if (!message.getContext().equals(modifyMessage.context)){
            message.setContext(modifyMessage.context);
            isUpdate = true;
        }
        if (!message.getTitle().equals(modifyMessage.title)){
            message.setTitle(modifyMessage.title);
            isUpdate = true;
        }
        message.setState(MessageState.STOP);
        return isUpdate;

    }
}
