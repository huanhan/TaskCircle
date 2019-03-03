package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.db.enums.*;
import com.tc.dto.Result;
import com.tc.dto.audit.QueryAudit;
import com.tc.dto.audit.*;
import com.tc.dto.finance.QueryFinance;
import com.tc.dto.task.QueryHunterTask;
import com.tc.dto.task.QueryTask;
import com.tc.dto.user.QueryUser;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.*;
import com.tc.until.FloatHelper;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 审核控制器
 *
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/audit")
public class AuditController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HunterTaskService hunterTaskService;

    @Autowired
    private UserWithdrawService userWithdrawService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 审核时长
     */
    public static final long AUDIT_LONG = 15 * 60 * 1000;

    /**
     * 我的审核记录
     *
     * @param id         用户编号
     * @param queryAudit 查询条件
     * @return
     */
    @PostMapping("/record/{id:\\d+}")
    @ApiOperation(value = "我的审核记录列表")
    public Result auditByMe(@PathVariable("id") Long id, @RequestBody QueryAudit queryAudit) {
        queryAudit.setAdminId(id);
        queryAudit.setSort(new Sort(Sort.Direction.DESC, Audit.CREATE_TIME));
        Page<Audit> result = auditService.findByQueryAudit(queryAudit);
        return Result.init(Audit.toListInIndex(result.getContent()), queryAudit
                .append(result.getTotalElements(), (long) result.getTotalPages()).clearSort());
    }




    /**
     * 查看我的审核详情信息
     *
     * @param id 审核编号
     * @return
     */
    @GetMapping("/detail/{aid:\\d+}/{id:\\d+}")
    @ApiOperation(value = "查看我的审核详情信息")
    public Audit detailByMe(@PathVariable("id") Long id, @PathVariable("aid") String aid) {
        Audit result = auditService.findOne(aid);
        if (!id.equals(result.getAdminId())) {
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        return Audit.toDetail(result);
    }

    /**
     * 审核记录列表
     *
     * @param queryAudit 查询条件
     * @return
     */
    @PostMapping("/record")
    @ApiOperation(value = "审核记录列表")
    public Result auditByAll(@RequestBody QueryAudit queryAudit) {
        queryAudit.setSort(new Sort(Sort.Direction.DESC, Audit.CREATE_TIME));
        Page<Audit> result = auditService.findByQueryAudit(queryAudit);
        return Result.init(Audit.toListInIndex(result.getContent()), queryAudit);
    }

    /**
     * 查看审核详情信息
     *
     * @param id 审核编号
     * @return
     */
    @GetMapping("/detail/{id:\\d+}")
    @ApiOperation(value = "查看我的审核详情信息")
    public Audit detail(@PathVariable("id") String id) {
        Audit result = auditService.findOne(id);
        return Audit.toDetail(result);
    }




    /**
     * 获取用户的任务审核列表
     * 根据任务状态获取审核列表
     * 包括：用户放弃任务的审核，用户新建任务的审核
     *
     * @param queryTask 查询用户放弃的任务
     * @return
     */
    @PostMapping("/user/task")
    @ApiOperation(value = "用户任务审核列表")
    public Result allByTask(HttpServletRequest request, @RequestBody QueryTask queryTask) {
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (queryTask.getState() != null) {
            if (!TaskState.isAudit(queryTask.getState())) {
                throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_FAILED);
            }
        } else {
            queryTask.setStates(TaskState.getAudit());
        }

        //更新审核超时的任务的状态为未审核
        taskService.updateState();

        queryTask.setSort(new Sort(Sort.Direction.ASC, Task.AUDIT_TIME));
        Page<Task> result = taskService.findByQueryTask(queryTask);
        return Result.init(Task.toIndexAsList(result.getContent(),me), queryTask
                .append(result.getTotalElements(), (long) result.getTotalPages()).clearSort());
    }

    /**
     * 获取猎刃的任务审核列表
     * 包括：猎刃放弃任务,猎刃完成并且需要管理员审核的任务
     *
     * @param queryHunterTask
     * @return
     */
    @PostMapping("/hunter/task")
    @ApiOperation(value = "猎刃任务审核列表")
    public Result allByHunterTask(HttpServletRequest request, @RequestBody QueryHunterTask queryHunterTask) {
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (queryHunterTask.getState() != null) {
            if (!HunterTaskState.isAudit(queryHunterTask.getState())) {
                throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_FAILED);
            }
        } else {
            queryHunterTask.setStates(HunterTaskState.getAudit());
        }

        //更新审核超时的任务的状态为未审核
        hunterTaskService.updateState();

        queryHunterTask.setSort(new Sort(Sort.Direction.ASC, HunterTask.AUDIT_TIME));
        Page<HunterTask> result = hunterTaskService.findByQueryHunterTask(queryHunterTask);
        return Result.init(HunterTask.toIndexAsList(result.getContent(),me), queryHunterTask
                .append(result.getTotalElements(), (long) result.getTotalPages()).clearSort());
    }

    /**
     * 获取用户提现审核列表
     *
     * @param queryFinance 查询条件
     * @return
     */
    @PostMapping("/finance")
    @ApiOperation(value = "用户提现审核列表")
    public Result allByUserWithdraw(HttpServletRequest request, @RequestBody QueryFinance queryFinance) {
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (queryFinance.getState() != null) {
            if (!WithdrawState.isAudit(queryFinance.getState())) {
                throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_FAILED);
            }
        } else {
            queryFinance.setStates(WithdrawState.getAudit());
        }

        //更新审核超时的用户提现的状态为未审核
        userWithdrawService.updateState();
        queryFinance.setSort(new Sort(Sort.Direction.ASC, UserWithdraw.CREATE_TIME));
        Page<UserWithdraw> result = userWithdrawService.findByQueryFinance(queryFinance);
        return Result.init(UserWithdraw.toIndexAsList(result.getContent(),me),
                queryFinance
                        .append(result.getTotalElements(), (long) result.getTotalPages())
                        .clearSort());
    }

    /**
     * 用户申请成为猎刃列表
     *
     * @param queryUser 查询用户申请猎刃条件
     * @return
     */
    @PostMapping("/hunter")
    @ApiOperation(value = "用户申请成为猎刃列表")
    public Result allByUserToHunter(HttpServletRequest request, @RequestBody QueryUser queryUser) {
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (queryUser.getState() != null) {
            if (!UserState.isAudit(queryUser.getState())) {
                throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_FAILED);
            }
        } else {
            queryUser.setStates(UserState.getAudit());
        }
        //更新审核超时的用户审核的状态为未审核
        userService.updateState();

        Page<User> result = userService.findByQueryUser(queryUser);
        return Result.init(User.toIndexAsList(result.getContent(),me), queryUser
                .append(result.getTotalElements(), (long) result.getTotalPages()).clearSort());
    }


    /**
     * 获取待审核的任务详情信息
     *
     * @param id 任务编号
     * @return
     */
    @GetMapping("/task/detail/{id:\\d+}")
    @ApiOperation(value = "待审核的任务详情信息")
    public Task taskDetail(HttpServletRequest request, @PathVariable("id") String id) {
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        Task result = taskService.findOne(id);
        if (result == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        TaskState updateState = null;
        Long currentAdmin = 0L;
        switch (result.getState()) {
            case COMMIT_AUDIT:
                updateState = TaskState.ADMIN_NEGOTIATE;
                result.setState(TaskState.ADMIN_NEGOTIATE);
                break;
            case AWAIT_AUDIT:
                updateState = TaskState.AUDIT;
                result.setState(TaskState.AUDIT);
                break;
            case ADMIN_NEGOTIATE:
                currentAdmin = result.getAdminId();
                break;
            case AUDIT:
                currentAdmin = result.getAdminId();
                break;
            default:
                throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }


        if (updateState != null) {

            Integer count = taskService.countByAdmin(me);
            if (count >= 1){
                throw new ValidException("请先做完当前工作后在继续下一个工作");
            }

            Date now = new Date();
            //将当前正在审核的任务加锁
            boolean isSuccess = taskService.updateState(id, updateState, now, me);
            if (!isSuccess) {
                throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
            }
            result.setAdminAuditTime(new Timestamp(now.getTime()));
            result.setAudit(true);
            return Task.toDetail(result);
        }

        if (FloatHelper.isNotNull(currentAdmin)) {
            if (currentAdmin.equals(me)) {
                result.setAudit(true);
                return Task.toDetail(result);
            } else {
                throw new ValidException("当前任务正在被审核中");
            }
        } else {
            throw new ValidException("未获取到表中当前审核管理员的信息");
        }

    }

    /**
     * 获取猎刃放弃任务详情与猎刃完成任务详情
     *
     * @param id
     * @return
     */
    @GetMapping("/hunter/task/detail/{id:\\d+}")
    @ApiOperation(value = "待审核的猎刃任务详情信息")
    public HunterTask hunterTaskDetail(HttpServletRequest request, @PathVariable("id") String id) {
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        HunterTask result = hunterTaskService.findOne(id);
        if (result == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        HunterTaskState updateState = null;
        Long currentAdmin = 0L;
        switch (result.getState()) {
            case COMMIT_ADMIN_AUDIT:
                updateState = HunterTaskState.ADMIN_AUDIT;
                result.setState(HunterTaskState.ADMIN_AUDIT);
                break;
            case COMMIT_TO_ADMIN:
                updateState = HunterTaskState.WITH_ADMIN_NEGOTIATE;
                result.setState(HunterTaskState.WITH_ADMIN_NEGOTIATE);
            case WITH_ADMIN_NEGOTIATE:
                currentAdmin = result.getAdminId();
                break;
            case ADMIN_AUDIT:
                currentAdmin = result.getAdminId();
                break;
            default:
                throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }
        if (updateState != null) {

            Integer count = hunterTaskService.countByAdmin(me);
            if (count >= 1){
                throw new ValidException("请先做完当前工作后在继续下一个工作");
            }


            Date now = new Date();
            //将当前正在审核的任务加锁
            boolean isSuccess = hunterTaskService.updateState(id, updateState, now, me);
            if (!isSuccess) {
                throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
            }
            result.setAdminAuditTime(new Timestamp(now.getTime()));
            return HunterTask.toDetail(result);
        }

        if (FloatHelper.isNotNull(currentAdmin)) {
            if (currentAdmin.equals(me)) {
                return HunterTask.toDetail(result);
            } else {
                throw new ValidException("当前猎刃任务正在被审核中");
            }
        } else {
            throw new ValidException("未获取到Redis中当前用户的信息");
        }
    }

    /**
     * 获取用户提交的提现审核详情信息
     *
     * @param id
     * @return
     */
    @GetMapping("/finance/detail/{id:\\d+}")
    @ApiOperation(value = "待审核的提现详情")
    public UserWithdraw financeDetail(HttpServletRequest request, @PathVariable("id") String id) {
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        UserWithdraw result = userWithdrawService.findOne(id);
        if (result == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        WithdrawState updateState = null;
        Long currentAdmin = 0L;
        switch (result.getState()) {
            case AUDIT:
                updateState = WithdrawState.AUDIT_CENTER;
                result.setState(WithdrawState.AUDIT_CENTER);
                break;
            case PAY_AUDIT:
                updateState = WithdrawState.PAY_AUDIT_CENTER;
                result.setState(WithdrawState.PAY_AUDIT_CENTER);
                break;
            case AUDIT_CENTER:
                currentAdmin = result.getAdminId();
                break;
            case PAY_AUDIT_CENTER:
                currentAdmin = result.getAdminId();
                break;
            default:
                throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        if (updateState != null) {

            Integer count = userWithdrawService.countByAdmin(me);
            if (count >= 1){
                throw new ValidException("请先做完当前工作后在继续下一个工作");
            }


            Date now = new Date();
            //将当前正在审核的任务加锁
            boolean isSuccess = userWithdrawService.updateState(id, updateState, now, me);
            if (!isSuccess) {
                throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
            }
            result.setAdminAuditTime(new Timestamp(now.getTime()));
            return UserWithdraw.toDetail(result);
        }

        if (FloatHelper.isNotNull(currentAdmin)) {
            if (currentAdmin.equals(me)) {
                result.setAudit(true);
                return UserWithdraw.toDetail(result);
            } else {
                throw new ValidException("当前用户提现与收支正在被审核中");
            }
        } else {
            throw new ValidException("未获取到用户收支与提现中的当前用户信息");
        }

    }

    /**
     * 获取待审核用户申请猎刃详情信息
     *
     * @param id
     * @return
     */
    @GetMapping("/user/detail/{id:\\d+}")
    @ApiOperation(value = "待审核用户申请猎刃详情")
    public User userDetail(HttpServletRequest request, @PathVariable("id") Long id) {
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        User result = userService.findOne(id);
        if (result == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }


        UserState updateState = null;
        Long currentAdmin = 0L;

        switch (result.getState()) {
            case AUDIT_HUNTER:
                updateState = UserState.AUDIT_CENTER;
                result.setState(UserState.AUDIT_CENTER);
                break;
            case AUDIT_CENTER:
                currentAdmin = result.getAdminId();
                break;
            default:
                throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }
        if (updateState != null) {

            Integer count = userService.countByAdmin(me);
            if (count >= 1){
                throw new ValidException("请先做完当前工作后在继续下一个工作");
            }

            Date now = new Date();
            //将当前正在审核的任务加锁
            boolean isSuccess = userService.updateState(id, UserState.AUDIT_CENTER, now, me);
            if (!isSuccess) {
                throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
            }
            result.setAdminAuditTime(new Timestamp(now.getTime()));
            return User.toDetail(result);
        }

        if (FloatHelper.isNotNull(currentAdmin)) {
            if (currentAdmin.equals(me)) {
                return User.toDetail(result);
            } else {
                throw new ValidException("当前用户成为猎刃申请正在被审核中");
            }
        } else {
            throw new ValidException("未获取到用户中当前用户的信息");
        }
    }

    /**
     * 添加任务审核结果
     *
     * @param addTaskAudit  要添加的任务审核信息
     * @param bindingResult 检查校验结果
     * @return
     */
    @PostMapping("/task/result")
    @ApiOperation(value = "添加任务审核结果")
    public AuditTask addTaskAudit(HttpServletRequest request, @Valid @RequestBody AddTaskAudit addTaskAudit, BindingResult bindingResult) {
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        addTaskAudit.setAdminId(me);
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }

        Task task = taskService.findOne(addTaskAudit.getTaskId());
        if (task == null) {
            throw new DBException("任务：" + StringResourceCenter.DB_QUERY_FAILED);
        }

        Long currentAdmin;
        switch (task.getState()) {
            case AUDIT:
                currentAdmin = task.getAdminId();
                addTaskAudit.setType(AuditType.TASK);
                break;
            case ADMIN_NEGOTIATE:
                currentAdmin = task.getAdminId();
                addTaskAudit.setType(AuditType.USER_FAILURE_TASK);
                break;
            default:
                throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        if (FloatHelper.isNotNull(currentAdmin)){
            if (!currentAdmin.equals(me)){
                throw new ValidException("当前任务正在被别人审核");
            }
        }else {
            throw new DBException("在任务中未找到可以审核的人");
        }

        //判断审核结果是否符合要求
        if (!AuditState.isOther(addTaskAudit.getResult())) {
            throw new ValidException("审核结果有误");
        }

        Audit audit = auditService.save(addTaskAudit.toAudit());
        return AuditTask.getBy(audit);
    }


    /**
     * 添加猎刃任务审核结果
     *
     * @param addHtAudit    要添加的猎刃任务审核信息
     * @param bindingResult 检查校验结果
     * @return
     */
    @PostMapping("/hunterTask/result")
    @ApiOperation(value = "添加猎刃任务审核结果")
    public AuditTask addHunterTaskAudit(HttpServletRequest request, @Valid @RequestBody AddHunterTaskAudit addHtAudit, BindingResult bindingResult) {
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        addHtAudit.setAdminId(me);
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }

        HunterTask task = hunterTaskService.findOne(addHtAudit.getHtId());
        if (task == null) {
            throw new DBException("猎刃任务：" + StringResourceCenter.DB_QUERY_FAILED);
        }

        Long currentAdmin;
        switch (task.getState()) {
            case ADMIN_AUDIT:
                currentAdmin = task.getAdminId();
                addHtAudit.setType(AuditType.HUNTER_OK_TASK);
                break;
            case WITH_ADMIN_NEGOTIATE:
                currentAdmin = task.getAdminId();
                addHtAudit.setType(AuditType.HUNTER_FAILURE_TASK);
                break;
            default:
                throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        if (FloatHelper.isNotNull(currentAdmin)){
            if (!currentAdmin.equals(me)){
                throw new ValidException("当前猎刃任务正在被别人审核");
            }
        }else {
            throw new DBException("在Redis中未找到可以审核的人");
        }

        //判断审核结果是否符合要求
        if (!AuditState.isHunterTaskOkAudit(addHtAudit.getResult())) {
            throw new ValidException("审核结果有误");
        }

        Audit audit = auditService.save(addHtAudit.toAudit());
        return AuditTask.getBy(audit);
    }

    /**
     * 添加猎刃申请审核结果
     *
     * @param addHunterAudit 要添加的用户审核信息
     * @param bindingResult  检查校验结果
     * @return
     */
    @PostMapping("/user/result")
    @ApiOperation(value = "添加猎刃申请审核结果")
    public AuditHunter addHunterAudit(HttpServletRequest request, @Valid @RequestBody AddHunterAudit addHunterAudit, BindingResult bindingResult) {
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        addHunterAudit.setAdminId(me);
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }
        User user = userService.findOne(addHunterAudit.getUserId());
        if (user == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        Long currentAdmin;
        switch (user.getState()) {
            case AUDIT_CENTER:
                currentAdmin = user.getAdminId();
                addHunterAudit.setType(AuditType.HUNTER);
                break;
            default:
                throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }
        if (FloatHelper.isNotNull(currentAdmin)){
            if (!currentAdmin.equals(me)){
                throw new ValidException("当前用户的猎刃申请正在被别人审核");
            }
        }else {
            throw new DBException("在用户中未找到可以审核的人");
        }
        //判断审核结果是否符合要求
        if (!AuditState.isOther(addHunterAudit.getResult())) {
            throw new ValidException("审核结果有误");
        }
        Audit audit = auditService.save(addHunterAudit.toAudit());
        return AuditHunter.getBy(audit);
    }

    /**
     * 添加财务审核结果
     *
     * @param addWithdrawAudit 提现审核信息
     * @param bindingResult    检查校验结果
     * @return
     */
    @PostMapping("/withdraw/result")
    @ApiOperation(value = "添加财务审核结果")
    public AuditWithdraw addFinanceAudit(HttpServletRequest request, @Valid @RequestBody AddWithdrawAudit addWithdrawAudit, BindingResult bindingResult) {
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        addWithdrawAudit.setAdminId(me);
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }

        UserWithdraw withdraw = userWithdrawService.findOne(addWithdrawAudit.getWithdrawId());
        if (withdraw == null) {
            throw new DBException("财务记录：" + StringResourceCenter.DB_QUERY_FAILED);
        }

        Long currentAdmin;
        switch (withdraw.getState()) {
            case AUDIT_CENTER:
                currentAdmin = withdraw.getAdminId();
                addWithdrawAudit.setType(AuditType.WITHDRAW);
                break;
            case PAY_AUDIT_CENTER:
                currentAdmin = withdraw.getAdminId();
                addWithdrawAudit.setType(AuditType.PAY);
                break;
            default:
                throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        if (FloatHelper.isNotNull(currentAdmin)){
            if (!currentAdmin.equals(me)){
                throw new ValidException("当前用户充值与提现正在被别人审核");
            }
        }else {
            throw new DBException("在财务详情中未找到可以审核的人");
        }

        //判断审核结果是否符合要求
        if (!AuditState.isOther(addWithdrawAudit.getResult())) {
            throw new ValidException("审核结果有误");
        }

        //设置用户设置的金额
        addWithdrawAudit.setUserMoney(withdraw.getMoney());
        Audit audit = auditService.save(addWithdrawAudit.toAudit());
        return AuditWithdraw.getBy(audit);
    }

    /**
     * 获取审核任务的参照信息
     *
     * @param id 任务编号
     * @return
     */
    @GetMapping("/task/refer/{id:\\d+}")
    @ApiOperation(value = "获取审核任务的参照信息")
    public TaskAuditRefer getTaskAuditRefer(@PathVariable("id") Long id) {
        return new TaskAuditRefer();
    }

}
