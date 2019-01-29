package com.tc.db.enums;

import java.util.ArrayList;
import java.util.List;

public enum HunterTaskState{

    /**
     * 猎刃点击接取任务
     */
    RECEIVE("任务接取"),
    BEGIN("开始"),
    EXECUTE("正在执行"),
    TASK_COMPLETE("任务完成"),
    AWAIT_USER_AUDIT("等待用户审核"),
    USER_AUDIT("用户审核中"),
    AWAIT_SETTLE_ACCOUNTS("等待结算"),
    SETTLE_ACCOUNTS_SUCCESS("结算成功"),
    SETTLE_ACCOUNTS_EXCEPTION("结算异常"),
    END_OK("任务结束并且完成"),
    END_NO("任务结束并且未完成"),
    USER_AUDIT_FAILURE("用户审核失败"),
    COMMIT_ADMIN_AUDIT("任务完成，提交管理员审核"),
    ADMIN_AUDIT("管理员审核中"),
    ALLOW_REWORK_ABANDON_HAVE_COMPENSATE("允许重做，放弃要补偿"),
    ALLOW_REWORK_ABANDON_NO_COMPENSATE("允许重做，放弃不用补偿"),
    NO_REWORK_NO_COMPENSATE("不能重做，不用补偿"),
    NO_REWORK_HAVE_COMPENSATE("不能重做，要补偿"),
    AWAIT_COMPENSATE("等待补偿"),
    COMPENSATE_SUCCESS("补偿成功"),
    COMPENSATE_EXCEPTION("补偿异常"),
    TASK_ABANDON("任务放弃"),
    TASK_BE_ABANDON("任务被放弃"),
    WITH_USER_NEGOTIATE("与用户协商"),
    USER_REPULSE("用户拒绝猎刃放弃"),
    HUNTER_REPULSE("猎刃拒绝用户放弃"),
    COMMIT_TO_ADMIN("提交管理员放弃申请"),
    WITH_ADMIN_NEGOTIATE("管理员参与协商")
    ;

    private String state;

    HunterTaskState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    /**
     * 当用户放弃任务时，允许猎刃直接放弃的猎刃任务状态
     * @return
     */
    public static List<HunterTaskState> isAbandon(){
        List<HunterTaskState> result = new ArrayList<>();
        result.add(RECEIVE);
        result.add(USER_REPULSE);
        result.add(COMMIT_TO_ADMIN);
        result.add(WITH_ADMIN_NEGOTIATE);
        return result;
    }

    public static List<HunterTaskState> notAbandon(){
        List<HunterTaskState> result = new ArrayList<>();
        result.add(BEGIN);
        result.add(EXECUTE);
        result.add(TASK_COMPLETE);
        result.add(AWAIT_USER_AUDIT);
        result.add(USER_AUDIT);
        result.add(AWAIT_SETTLE_ACCOUNTS);
        result.add(SETTLE_ACCOUNTS_SUCCESS);
        result.add(SETTLE_ACCOUNTS_EXCEPTION);
        result.add(USER_AUDIT_FAILURE);
        result.add(COMMIT_ADMIN_AUDIT);
        result.add(ADMIN_AUDIT);
        result.add(ALLOW_REWORK_ABANDON_HAVE_COMPENSATE);
        result.add(ALLOW_REWORK_ABANDON_NO_COMPENSATE);
        result.add(NO_REWORK_NO_COMPENSATE);
        result.add(NO_REWORK_HAVE_COMPENSATE);
        result.add(AWAIT_COMPENSATE);
        result.add(COMPENSATE_SUCCESS);
        result.add(COMPENSATE_EXCEPTION);
        result.add(WITH_USER_NEGOTIATE);
        result.add(HUNTER_REPULSE);
        return result;
    }

    /**
     * 是否允许提交用户审核
     * @return
     */
    public static boolean isUpAuditToUser(HunterTaskState state){
        switch (state){
            case TASK_COMPLETE:
                return true;
            case ALLOW_REWORK_ABANDON_HAVE_COMPENSATE:
                return true;
            case ALLOW_REWORK_ABANDON_NO_COMPENSATE:
                return true;
            case NO_REWORK_NO_COMPENSATE:
                return true;
            case NO_REWORK_HAVE_COMPENSATE:
                return true;
            default:
                return false;
        }
    }

    /**
     * 用户是否能审核
     * @return
     */
    public static boolean isAuditToUser(HunterTaskState state){
        switch (state){
            case AWAIT_USER_AUDIT:
                return true;
            default:
                return false;
        }
    }

    /**
     * 根据用户任务设置的选项获取对应的猎刃状态
     * @param isRework 是否可重做
     * @param isCompensate 是否要赔偿
     * @return 对应的状态
     */
    public static HunterTaskState getBy(boolean isRework,boolean isCompensate){
        if (isRework && isCompensate){
            return HunterTaskState.ALLOW_REWORK_ABANDON_HAVE_COMPENSATE;
        }else if (isRework){
            return HunterTaskState.ALLOW_REWORK_ABANDON_NO_COMPENSATE;
        }else if (isCompensate){
            return HunterTaskState.NO_REWORK_HAVE_COMPENSATE;
        }else {
            return HunterTaskState.NO_REWORK_NO_COMPENSATE;
        }
    }

    /**
     * 判断猎刃是否可以重做任务
     * @return
     */
    public static boolean isRework(HunterTaskState state){
        switch (state){
            case ALLOW_REWORK_ABANDON_HAVE_COMPENSATE:
                return true;
            case ALLOW_REWORK_ABANDON_NO_COMPENSATE:
                return true;
            default:
                return false;
        }
    }

    /**
     * 判断哪些状态猎刃是允许放弃任务的
     * @param state
     * @return
     */
    public static boolean isAbandon(HunterTaskState state){
        boolean isFailure;
        switch (state){
            case RECEIVE:
                isFailure = true;
                break;
            case BEGIN:
                isFailure = true;
                break;
            case EXECUTE:
                isFailure = true;
                break;
            case TASK_COMPLETE:
                isFailure = true;
                break;
            case ALLOW_REWORK_ABANDON_HAVE_COMPENSATE:
                isFailure = true;
                break;
            case ALLOW_REWORK_ABANDON_NO_COMPENSATE:
                isFailure = true;
                break;
            case NO_REWORK_NO_COMPENSATE:
                isFailure = true;
                break;
            case NO_REWORK_HAVE_COMPENSATE:
                isFailure = true;
                break;
            default:
                isFailure = false;
                break;
        }
        return isFailure;
    }

    /**
     * 判断状态是否允许提交管理员审核
     * @param state
     * @return
     */
    public static boolean isUpAuditToAdmin(HunterTaskState state){
        switch (state){
            case USER_REPULSE:
                return true;
            case ALLOW_REWORK_ABANDON_HAVE_COMPENSATE:
                return true;
            case ALLOW_REWORK_ABANDON_NO_COMPENSATE:
                return true;
            case NO_REWORK_NO_COMPENSATE:
                return true;
            case NO_REWORK_HAVE_COMPENSATE:
                return true;
            default:
                return false;
        }
    }

    /**
     * 判断状态是否允许提交管理员审核
     * @param state
     * @return
     */
    public static boolean isDiUpAuditToAdmin(HunterTaskState state){
        switch (state){
            case COMMIT_TO_ADMIN:
                return true;
            case WITH_ADMIN_NEGOTIATE:
                return true;
            case COMMIT_ADMIN_AUDIT:
                return true;
            case ADMIN_AUDIT:
                return true;
            default:
                return false;
        }
    }

    /**
     * 获取非正在执行的猎刃任务状态
     * @param state
     * @return
     */
    public static boolean isOk(HunterTaskState state) {
        switch (state){
            case END_NO:
                return true;
            case END_OK:
                return true;
            case TASK_ABANDON:
                return true;
            case TASK_BE_ABANDON:
                return true;
            default:
                return false;
        }
    }

    /**
     * 用户判断猎刃是否都拒绝了用户
     * @return
     */
    public static List<HunterTaskState> notAbandonState(){
        List<HunterTaskState> result = new ArrayList<>();
        result.add(END_NO);
        result.add(END_OK);
        result.add(TASK_ABANDON);
        result.add(TASK_BE_ABANDON);
        result.add(HUNTER_REPULSE);
        return result;
    }

    public static List<HunterTaskState> byStr(List<String> items) {
        List<HunterTaskState> result = new ArrayList<>();
        items.forEach(s -> result.add(HunterTaskState.valueOf(s)));
        return result;
    }
}
