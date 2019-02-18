package com.tc.db.enums;

import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

public enum CommentType {

    USER_COMMENT_HUNTER("评论猎刃"),
    HUNTER_COMMENT_USER("评论用户"),
    COMMENT_TASK("评论任务")

    ;
    private String type;

    public String getType() {
        return type;
    }

    CommentType(String type) {
        this.type = type;
    }

    public static List<TransEnum> toList() {
        List<TransEnum> result = new ArrayList<>();
        for (CommentType commentType : CommentType.values()) {
            result.add(TransEnum.init(commentType.name(),commentType.getType()));
        }
        return result;
    }
}
