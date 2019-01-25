package com.tc.dto.app;

public class ResultApp {
    private String msg;

    public ResultApp(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static ResultApp init(String msg) {
        return new ResultApp(msg);
    }
}
