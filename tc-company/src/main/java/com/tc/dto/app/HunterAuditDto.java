package com.tc.dto.app;

import com.tc.db.entity.AuditHunter;
import com.tc.db.enums.UserState;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Collection;

public class HunterAuditDto {
    private String idCard;

    private String address;

    private String phone;

    private String idCardImgFront;

    private String idCardImgBack;

    private UserState state;

    private Collection<AuditDto> audits;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCardImgFront() {
        return idCardImgFront;
    }

    public void setIdCardImgFront(String idCardImgFront) {
        this.idCardImgFront = idCardImgFront;
    }

    public String getIdCardImgBack() {
        return idCardImgBack;
    }

    public void setIdCardImgBack(String idCardImgBack) {
        this.idCardImgBack = idCardImgBack;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public Collection<AuditDto> getAudits() {
        return audits;
    }

    public void setAudits(Collection<AuditDto> audits) {
        this.audits = audits;
    }
}
