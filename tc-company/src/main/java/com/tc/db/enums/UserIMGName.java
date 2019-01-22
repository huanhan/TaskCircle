package com.tc.db.enums;

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

    public String getIMGName() {
        return IMGName;
    }
}
