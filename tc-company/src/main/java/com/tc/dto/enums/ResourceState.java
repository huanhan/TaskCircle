package com.tc.dto.enums;

public enum ResourceState {
    NORMAL("正常"),
    CONTROLLER_HAS_NONE("控制器中不存在相同资源")
    ;

    private String state;

    ResourceState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
