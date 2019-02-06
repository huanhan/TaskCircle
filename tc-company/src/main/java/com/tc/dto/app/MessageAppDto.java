package com.tc.dto.app;


import com.google.gson.Gson;
import com.tc.db.entity.Message;
import com.tc.dto.LongIds;
import com.tc.until.TranstionHelper;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MessageAppDto {
    private Long id;
    private String context;
    private String title;
    private Timestamp createTime;

    public static List<MessageAppDto> initList(List<Message> content) {
        List<MessageAppDto> results=new ArrayList();
        MessageAppDto temp;
        for (Message message : content) {
            temp=new MessageAppDto();
            BeanUtils.copyProperties(message,temp);
            results.add(temp);
        }
        return results;
    }

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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
