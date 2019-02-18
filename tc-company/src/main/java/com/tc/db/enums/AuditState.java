package com.tc.db.enums;

import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

public enum AuditState {

    PASS("通过"),
    NO_PASS("不通过"),
    REWORK("重做任务"),
    ABANDON_COMPENSATE("放弃任务，并且补偿"),
    ABANDON_NOT_COMPENSATE("放弃任务，并且不用补偿补偿"),
    TASK_OK("任务直接完成")
    ;


    private String state;

    public String getState() {
        return state;
    }

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

    public static List<TransEnum> toList() {
        List<TransEnum> result = new ArrayList<>();
        for (AuditState auditState : AuditState.values()) {
            result.add(TransEnum.init(auditState.name(),auditState.getState()));
        }
        return result;
    }
}
