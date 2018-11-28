package com.tc.db.enums;

/**
 * @author Cyg
 * 用户类别
 */

public enum UserCategory {

    NORMAL("普通用户"),
    HUNTER("猎刃"),
    ADMINISTRATOR("管理员"),
    DEVELOPER("开发人员")
    ;

    private String category;

    UserCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
