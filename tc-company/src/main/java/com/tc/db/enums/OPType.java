package com.tc.db.enums;

/**
 * 操作类型
 * @author Cyg
 */

public enum OPType {

    DELETE("删除"),
    ADD("添加"),
    MODIFY("修改")
    ;

    private String type;

    OPType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
