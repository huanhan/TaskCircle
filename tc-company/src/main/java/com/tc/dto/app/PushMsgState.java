package com.tc.dto.app;

public enum PushMsgState {
    TASK("任务"),
    HUNTER_TASK("猎刃任务"),
    HUNTER_LIST("猎刃执行列表"),
    CHAT("聊天"),
    NOTICE("公告");

    private String state;

    PushMsgState(String state) {
        this.state = state;
    }
}
