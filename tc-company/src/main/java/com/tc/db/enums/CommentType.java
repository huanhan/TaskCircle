package com.tc.db.enums;

public enum CommentType {

    USER_COMMENT_HUNTER("评论猎刃"),
    HUNTER_COMMENT_USER("评论用户"),
    COMMENT_TASK("评论任务")

    ;
    private String type;

    CommentType(String type) {
        this.type = type;
    }
}
