package com.tc.db.enums;

import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 审核类别
 *
 * @author Cyg
 */

public enum AuditType {

    HUNTER("猎刃审核"),
    WITHDRAW("提现审核"),
    PAY("充值审核"),
    TASK("新建任务审核"),
    USER_FAILURE_TASK("用户放弃任务"),
    HUNTER_FAILURE_TASK("猎刃放弃任务"),
    HUNTER_OK_TASK("猎刃完成任务"),;
    private String type;

    /**
     * 根据任务状态获取审核类别
     *
     * @param state
     * @return
     */
    public static AuditType getByTaskState(TaskState state) {
        switch (state) {
            case AUDIT:
                return TASK;
            case ADMIN_NEGOTIATE:
                return USER_FAILURE_TASK;
            default:
                return null;
        }
    }

    /**
     * 根据猎刃任务状态获取审核类别
     *
     * @param state
     * @return
     */
    public static AuditType getByHtState(HunterTaskState state) {
        switch (state) {
            case ADMIN_AUDIT:
                return HUNTER_OK_TASK;
            case WITH_ADMIN_NEGOTIATE:
                return HUNTER_FAILURE_TASK;
            default:
                return null;
        }
    }

    /**
     * 根据提现与充值状态获取审核类别
     * @param state
     * @return
     */
    public static AuditType getByWithdrawState(WithdrawState state) {
        switch (state) {
            case AUDIT_CENTER:
                return WITHDRAW;
            case PAY_AUDIT_CENTER:
                return PAY;
            default:
                return null;
        }
    }


    public String getType() {
        return type;
    }

    AuditType(String type) {
        this.type = type;
    }


    /**
     * 判断是否任务审核需要的类别
     *
     * @param type
     * @return
     */
    public static boolean isTask(AuditType type) {
        switch (type) {
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
     *
     * @param type
     * @return
     */
    public static boolean isAuditUser(AuditType type) {
        switch (type) {
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
            result.add(TransEnum.init(auditType.name(), auditType.getType()));
        }
        return result;
    }
}
