package com.tc.dto.app;

import com.tc.db.entity.UserHunterInterflow;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ChatDtoReq {

    @NotNull
    private Long hunterId;
    @NotNull
    private Long userId;
    @NotEmpty
    private String hunterTaskId;
    @NotEmpty
    private String context;

    public static UserHunterInterflow init(ChatDtoReq req) {
        UserHunterInterflow userHunterInterflow = new UserHunterInterflow();
        BeanUtils.copyProperties(req,userHunterInterflow);
        return userHunterInterflow;
    }

    public Long getHunterId() {
        return hunterId;
    }

    public void setHunterId(Long hunterId) {
        this.hunterId = hunterId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
