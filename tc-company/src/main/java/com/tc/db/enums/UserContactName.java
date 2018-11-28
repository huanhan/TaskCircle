package com.tc.db.enums;

public enum UserContactName {

    QQ("QQ"),
    WCHART("微信"),
    PHONT("手机电话"),
    FL_PHONT("固定电话"),
    EMAIL("邮箱")
    ;

    private String contactName;

    UserContactName(String contactName) {
        this.contactName = contactName;
    }
}
