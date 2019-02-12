package com.tc.db.enums;

import com.tc.dto.TransEnum;

import java.util.ArrayList;
import java.util.List;

public enum UserIMGName {

    IDCARD_FRONT("身份证正面"),
    IDCARD_BACK("身份证背面"),
    REAL_HEAD("真实头像"),
    VIRTUAL_HEAD("虚拟头像"),
    CERTIFICATE_ONE("证书1"),
    CERTIFICATE_TWO("证书2"),
    CERTIFICATE_THREE("证书3"),
    ;

    private String IMGName;

    UserIMGName(String IMGName) {
        this.IMGName = IMGName;
    }

    public static List<TransEnum> toList() {
        List<TransEnum> result = new ArrayList<>();
        for (UserIMGName value : UserIMGName.values()) {
            result.add(TransEnum.init(value.name(),value.getIMGName()));
        }
        return result;
    }

    public String getIMGName() {
        return IMGName;
    }
}
