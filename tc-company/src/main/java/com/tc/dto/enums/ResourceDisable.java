package com.tc.dto.enums;

public enum  ResourceDisable {

    DISABLE_ADD("获取已添加至数据库的资源"),
    DISABLE_NO_ADD("获取未添加至数据库的资源")

    ;

    private String state;

    ResourceDisable(String state) {
        this.state = state;
    }


}
