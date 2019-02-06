package com.tc.dto.app;

import com.tc.db.entity.UserHunterInterflow;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ChatDto {

    private Long hunterId;
    private Long userId;
    private String taskId;
    private Long sender;
    private Timestamp createTime;
    private String context;
    private String userIcon;
    private String hunterIcon;

    public static List<ChatDto> initList(List<UserHunterInterflow> content) {
        ArrayList<ChatDto> chatDtos = new ArrayList<>();
        ChatDto chatDto;
        for (UserHunterInterflow userHunterInterflow : content) {
            chatDto = new ChatDto();
            BeanUtils.copyProperties(userHunterInterflow, chatDto);
            chatDto.setUserIcon(userHunterInterflow.getUser().getHeadImg());
            chatDto.setHunterIcon(userHunterInterflow.getHunter().getUser().getHeadImg());

            chatDtos.add(chatDto);
        }
        return chatDtos;
    }

    public static ChatDto init(UserHunterInterflow content) {
        ChatDto chatDto = new ChatDto();
        BeanUtils.copyProperties(content, chatDto);
        //这里没设置图片
        return chatDto;
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

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getHunterIcon() {
        return hunterIcon;
    }

    public void setHunterIcon(String hunterIcon) {
        this.hunterIcon = hunterIcon;
    }
}
