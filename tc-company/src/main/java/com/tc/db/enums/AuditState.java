package com.tc.db.enums;

public enum AuditState {

    PASS("通过"),
    NO_PASS("不通过"),
    REWORK("重做任务"),
    ABANDON_COMPENSATE("放弃任务，并且补偿"),
    ABANDON_NOT_COMPENSATE("放弃任务，并且不用补偿补偿"),
    TASK_OK("任务直接完成")
    ;


    private String state;

    AuditState(String state) {
        this.state = state;
    }


    public static boolean isOther(AuditState state){
        switch (state){
            case PASS:
                return true;
            case NO_PASS:
                return true;
            default:
                return false;
        }
    }

    public static boolean isHunterTaskOkAudit(AuditState state){
        switch (state){
            case REWORK:
                return true;
            case ABANDON_COMPENSATE:
                return true;
            case ABANDON_NOT_COMPENSATE:
                return true;
            case TASK_OK:
                return true;
            default:
                return false;
        }
    }
}
