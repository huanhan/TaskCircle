package com.tc.dto.app;


import com.tc.db.entity.UserIeRecord;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TransferAppDto {

    private String id;
    private Timestamp createTime;
    private Float money;
    private String context;
    private Long me;
    private String meName;
    private Long to;
    private String toName;

    public static List<TransferAppDto> toList(List<UserIeRecord> content) {
        ArrayList<TransferAppDto> transferAppDtos = new ArrayList<>();
        for (UserIeRecord userIeRecord : content) {
            TransferAppDto transferAppDto = new TransferAppDto();
            BeanUtils.copyProperties(userIeRecord, transferAppDto);
            transferAppDto.setMeName(userIeRecord.getUserByMe().getName());
            transferAppDto.setToName(userIeRecord.getUserByTo().getName());
            transferAppDtos.add(transferAppDto);
        }

        return transferAppDtos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Long getMe() {
        return me;
    }

    public void setMe(Long me) {
        this.me = me;
    }

    public String getMeName() {
        return meName;
    }

    public void setMeName(String meName) {
        this.meName = meName;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }
}
