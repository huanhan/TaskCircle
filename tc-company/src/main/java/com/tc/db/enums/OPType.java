package com.tc.db.enums;

import com.tc.dto.TransEnum;

import java.util.ArrayList;
import java.util.List;

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

    public static List<TransEnum> toList() {
        List<TransEnum> result = new ArrayList<>();
        for (OPType value : OPType.values()) {
            result.add(TransEnum.init(value.name(),value.getType()));
        }
        return result;
    }

    public String getType() {
        return type;
    }
}
