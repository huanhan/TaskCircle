package com.tc.db.enums;

import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

public enum TaskState {

    /**
     * 用户新建一条任务
     */
    NEW_CREATE("新建"),
    /**
     * 用户将新建的任务提交审核
     */
    AWAIT_AUDIT("等待审核"),
    /**
     * 任务被管理员查看
     */
    AUDIT("审核中"),
    /**
     * 管理员点击提交审核，并设置审核状态
     */
    AUDIT_FAILURE("任务审核失败"),
    /**
     * 管理员点击提交审核，并设置审核状态
     */
    AUDIT_SUCCESS("任务审核成功"),
    /**
     * 审核成功的任务，用户缴纳定金后
     */
    OK_ISSUE("任务可以发布"),
    /**
     * 用户点击发布
     */
    ISSUE("任务发布中"),
    /**
     * 用户主动禁止，系统自动禁止（任务被接数量超出可接数量，用户账户金额不足）
     */
    FORBID_RECEIVE("任务禁止被接取"),
    /**
     * 任务被用户撤回
     */
    OUT("任务被撤回"),
    /**
     * 用户发布任务被猎刃完成
     */
    FINISH("任务完成"),
    /**
     * 用户点击了任务删除按钮，并经过一系列的判断后
     */
    DELETE_OK("任务被删除"),
    /**
     * 用户提交放弃申请
     */
    ABANDON_COMMIT("用户提交放弃的申请"),
    /**
     * 用户点击放弃任务按钮，并经过一系列判断后
     */
    ABANDON_OK("任务被放弃"),
    /**
     * 任务被猎刃接取后，用户放弃任务，并且系统默认不允许用户放弃时
     */
    USER_HUNTER_NEGOTIATE("与猎刃协商中"),
    /**
     * 猎刃拒绝与用户协商
     */
    HUNTER_REJECT("猎刃拒绝协商"),
    /**
     * 用户将放弃申请提交给管理员
     */
    COMMIT_AUDIT("提交管理员协商"),
    /**
     * 猎刃拒绝用户放弃任务，并且用户提交管理员协调解决
     */
    ADMIN_NEGOTIATE("管理员协商中"),
    /**
     * 查询专用
     */
    HUNTER_COMMIT("猎刃放弃任务");


    private String state;

    TaskState(String state) {
        this.state = state;
    }

    public static List<TransEnum> toList() {
        List<TransEnum> result = new ArrayList<>();
        for (TaskState value : TaskState.values()) {
            result.add(TransEnum.init(value.name(), value.getState()));
        }
        return result;
    }




    public String getState() {
        return state;
    }

    /**
     * 判断哪些状态时的任务允许发布
     *
     * @param taskState
     * @return
     */
    public static boolean isIssue(TaskState taskState) {
        switch (taskState) {
            case AUDIT_SUCCESS:
                return true;
            case OK_ISSUE:
                return true;
            default:
                return false;
        }
    }

    /**
     * 判断任务是否需要重新发布
     *
     * @param taskState
     * @return
     */
    public static boolean isReIssue(TaskState taskState) {
        switch (taskState) {
            case FORBID_RECEIVE:
                return true;
            default:
                return false;
        }
    }

    /**
     * 哪些状态允许用户将任务提交管理员审核
     *
     * @param taskState
     * @return
     */
    public static boolean isToAdminAudit(TaskState taskState) {
        switch (taskState) {
            case NEW_CREATE:
                return true;
            case HUNTER_REJECT:
                return true;
            default:
                return false;
        }
    }

    /**
     * 哪些状态允许用户取消管理员审核
     *
     * @param state
     * @return
     */
    public static boolean isDiAuditByAdmin(TaskState state) {
        switch (state) {
            case AWAIT_AUDIT:
                return true;
            case AUDIT:
                return true;
            case ADMIN_NEGOTIATE:
                return true;
            case COMMIT_AUDIT:
                return true;
            default:
                return false;
        }
    }

    public static List<TaskState> byStrs(List<String> strings) {
        List<TaskState> result = new ArrayList<>();
        strings.forEach(s -> result.add(TaskState.valueOf(s)));
        return result;
    }

    /**
     * 判断任务状态是否允许放弃
     *
     * @param state
     * @return
     */
    public static boolean isAbandon(TaskState state) {
        switch (state) {
            case FORBID_RECEIVE:
                return true;
            case OUT:
                return true;
            default:
                return false;
        }
    }

    /**
     * 任务状态是否在审核中被允许查询
     *
     * @param state
     * @return
     */
    public static boolean isAudit(TaskState state) {
        switch (state) {
            case AWAIT_AUDIT:
                return true;
            case COMMIT_AUDIT:
                return true;
            case AUDIT:
                return true;
            case ADMIN_NEGOTIATE:
                return true;
            default:
                return false;
        }
    }

    /**
     * 任务状态是否允许提交审核结果
     * @param state
     * @return
     */
    public static boolean isAuditCenter(TaskState state) {
        switch (state) {
            case AUDIT:
                return true;
            case ADMIN_NEGOTIATE:
                return true;
            default:
                return false;
        }
    }

    /**
     * 获取被允许查询的所有状态
     *
     * @return
     */
    public static List<TaskState> getAudit() {
        List<TaskState> result = new ArrayList<>();
        result.add(TaskState.AWAIT_AUDIT);
        result.add(TaskState.COMMIT_AUDIT);
        result.add(TaskState.AUDIT);
        result.add(TaskState.ADMIN_NEGOTIATE);
        return result;
    }

    public static List<TaskState> isAuditNotOk() {
        List<TaskState> result = new ArrayList<>();
        result.add(TaskState.AWAIT_AUDIT);
        result.add(TaskState.COMMIT_AUDIT);
        return result;
    }
}
