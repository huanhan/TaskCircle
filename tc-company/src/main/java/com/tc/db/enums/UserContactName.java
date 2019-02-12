package com.tc.db.enums;

import com.tc.dto.TransEnum;

import java.util.ArrayList;
import java.util.List;

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

    public static List<TransEnum> toList() {
        List<TransEnum> result = new ArrayList<>();
        for (UserContactName value : UserContactName.values()) {
            result.add(TransEnum.init(value.name(),value.getContactName()));
        }
        return result;
    }

    public String getContactName() {
        return contactName;
    }
}
