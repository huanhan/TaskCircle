package com.tc.db.enums;

import com.tc.dto.TransEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 审核类别
 * @author Cyg
 */

public enum  AuditType {

    HUNTER("猎刃审核"),
    WITHDRAW("提现审核"),
    TASK("新建任务审核"),
    USER_FAILURE_TASK("用户放弃任务"),
    HUNTER_FAILURE_TASK("猎刃放弃任务"),
    HUNTER_OK_TASK("猎刃完成任务"),
    ;
    private String type;

    public String getType() {
        return type;
    }

    AuditType(String type) {
        this.type = type;
    }


    /**
     * 判断是否任务审核需要的类别
     * @param type
     * @return
     */
    public static boolean isTask(AuditType type){
        switch (type){
            case TASK:
                return true;
            case USER_FAILURE_TASK:
                return true;
            case HUNTER_FAILURE_TASK:
                return true;
            case HUNTER_OK_TASK:
                return true;
            default:
                return false;
        }
    }

    /**
     * 判断是否是审核用户
     * @param type
     * @return
     */
    public static boolean isAuditUser(AuditType type){
        switch (type){
            case WITHDRAW:
                return true;
            case TASK:
                return true;
            case USER_FAILURE_TASK:
                return true;
                default:
                    return false;
        }
    }

    public static List<TransEnum> toList() {
        List<TransEnum> result = new ArrayList<>();
        for (AuditType auditType : AuditType.values()) {
            result.add(TransEnum.init(auditType.name(),auditType.getType()));
        }
        return result;
    }
}
