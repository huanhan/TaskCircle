package com.tc.dto.enums;

public enum  AuthorityState {

    NONE_AUTHORITY("没有权限的管理员"),
    HAVE_AUTHORITY("有权限的管理员"),
    ALL("全部")

    ;

    private String state;

    AuthorityState(String state) {
        this.state = state;
    }

}
