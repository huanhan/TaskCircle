package com.tc.dto.app;

import org.hibernate.validator.constraints.NotEmpty;

public class HunterAuditReq {
    @NotEmpty(message = "身份证号不能为空")
    private String idCard;

    @NotEmpty(message = "家庭地址不能为空")
    private String address;

    @NotEmpty(message = "电话不能为空")
    private String phone;

    @NotEmpty(message = "身份证正面图不能为空")
    private String idCardImgFront;

    @NotEmpty(message = "身份证反面图不能为空")
    private String idCardImgBack;

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
}
