package com.tc.dto.app;

import com.tc.db.entity.User;
import com.tc.dto.user.CashPledge;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CashPledgeAppDto {
    private String id;
    private Long userId;
    private Float money;
    private Timestamp createTime;
    private String name;

    public static List<CashPledgeAppDto> toList(List<CashPledge> result) {
        ArrayList<CashPledgeAppDto> cashPledgeAppDtos = new ArrayList<>();
        for (CashPledge cashPledge : result) {
            CashPledgeAppDto cashPledgeAppDto = new CashPledgeAppDto();
            BeanUtils.copyProperties(cashPledge, cashPledgeAppDto);
            cashPledgeAppDtos.add(cashPledgeAppDto);
        }
        return cashPledgeAppDtos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
