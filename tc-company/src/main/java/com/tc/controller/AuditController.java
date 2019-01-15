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
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 审核控制器
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

    /**
     * 审核时长
     */
    public static final long AUDIT_LONG = 15 * 60 * 1000;

    /**
     * 审核记录
     * @param queryAudit 查询条件
     * @return
     */
    @PostMapping("/record")
    @ApiOperation(value = "审核记录列表")
    public Result auditByMe(@RequestBody QueryAudit queryAudit){
        Page<Audit> result = auditService.findByQueryAudit(queryAudit);
        return Result.init(Audit.toListInIndex(result.getContent()),queryAudit);
    }

    /**
     * 查看审核详情信息
     * @param id 审核编号
     * @return
     */
    @GetMapping("/detail/{id:\\d+}")
    @ApiOperation(value = "查看审核详情信息")
    public Audit detail(@PathVariable("id") String id){
        Audit result = auditService.findOne(id);
        return Audit.toDetail(result);
    }

    /**
     * 获取用户的任务审核列表
     * 根据任务状态获取审核列表
     * 包括：用户放弃任务的审核，用户新建任务的审核
     * @param queryTask 查询用户放弃的任务
     * @return
     */
    @PostMapping("/user/task")
    @ApiOperation(value = "用户任务审核列表")
    public Result allByTask(@RequestBody QueryTask queryTask){

        if (queryTask.getState() == null || (!queryTask.getState().equals(TaskState.COMMIT_AUDIT) && !queryTask.getState().equals(TaskState.AWAIT_AUDIT))){
            throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_FAILED);
        }

        //更新审核超时的任务的状态为未审核
        taskService.updateState(queryTask.getState());

        queryTask.setSort(new Sort(Sort.Direction.ASC,Task.AUDIT_TIME));

        Page<Task> result = taskService.findByQueryTask(queryTask);
        return Result.init(Task.toIndexAsList(result.getContent()),queryTask);
    }

    /**
     * 获取猎刃的任务审核列表
     * 包括：猎刃放弃任务,猎刃完成并且需要管理员审核的任务
     * @param queryHunterTask
     * @return
     */
    @PostMapping("/hunter/task")
    @ApiOperation(value = "猎刃任务审核列表")
    public Result allByTask(@RequestBody QueryHunterTask queryHunterTask){

        if (queryHunterTask.getState() == null || (!queryHunterTask.getState().equals(HunterTaskState.COMMIT_TO_ADMIN) && !queryHunterTask.getState().equals(HunterTaskState.COMMIT_ADMIN_ADUIT))){
            throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_FAILED);
        }

        //更新审核超时的任务的状态为未审核
        hunterTaskService.updateState(queryHunterTask.getState());

        queryHunterTask.setSort(new Sort(Sort.Direction.ASC,HunterTask.AUDIT_TIME));
        Page<HunterTask> result = hunterTaskService.findByQueryHunterTask(queryHunterTask);
        return Result.init(HunterTask.toIndexAsList(result.getContent()),queryHunterTask);
    }

    /**
     * 获取用户提现审核列表
     * @param queryFinance 查询条件
     * @return
     */
    @PostMapping("/finance")
    @ApiOperation(value = "用户提现审核列表")
    public Result allByUserWithdraw(@RequestBody QueryFinance queryFinance){

        //更新审核超时的用户提现的状态为未审核
        userWithdrawService.updateState(queryFinance.getState());

        queryFinance.setType(WithdrawType.WITHDRAW);
        queryFinance.setState(WithdrawState.AUDIT);
        queryFinance.setSort(new Sort(Sort.Direction.ASC,UserWithdraw.CREATE_TIME));
        Page<UserWithdraw> result = userWithdrawService.findByQueryFinance(queryFinance);
        return Result.init(UserWithdraw.toIndexAsList(result.getContent()),queryFinance);
    }

    /**
     * 用户申请成为猎刃列表
     * @param queryUser 查询用户申请猎刃条件
     * @return
     */
    @PostMapping("/hunter")
    @ApiOperation(value = "用户申请成为猎刃列表")
    public Result allByUserToHunter(@RequestBody QueryUser queryUser){

        if (queryUser.getState() == null || !queryUser.getState().equals(UserState.AUDIT_HUNTER)){
            throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_FAILED);
        }

        //更新审核超时的用户审核的状态为未审核
        userService.updateState(queryUser.getState());

        queryUser.setState(UserState.AUDIT_HUNTER);
        Page<User> result = userService.findByQueryUser(queryUser);
        return Result.init(User.toIndexAsList(result.getContent()),queryUser);
    }


    /**
     * 获取待审核的任务详情信息
     * @param type 任务状态（用户放弃，用户新建任务）
     * @param id 任务编号
     * @return
     */
    @GetMapping("/task/detail/{type}/{id:\\d+}")
    @ApiOperation(value = "待审核的任务详情信息")
    public Task taskDetail(@PathVariable("type") TaskState type, @PathVariable("id") String id){

        Task result = null;

        switch (type){
            case COMMIT_AUDIT:
                result = taskService.findByIdAndHunters(id);
                break;
            case AWAIT_AUDIT:
                result = taskService.findOne(id);
                break;
            default:
                break;
        }

        if (result == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        hasAudit(result.getAdminAuditTime());

        Date now = new Date();

        //将当前正在审核的任务加锁
        boolean isSuccess = taskService.updateState(id,TaskState.AUDIT,now);
        if (!isSuccess){
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }


        result.setAdminAuditTime(new Timestamp(now.getTime()));
        return Task.toDetail(result);
    }

    /**
     * 获取猎刃放弃任务详情与猎刃完成任务详情
     * @param type
     * @param id
     * @return
     */
    @GetMapping("/hunter/task/detail/{type}/{id:\\d+}")
    @ApiOperation(value = "待审核的任务详情信息")
    public HunterTask hunterTaskDetail(@PathVariable("type") HunterTaskState type, @PathVariable("id") String id){
        HunterTask result = hunterTaskService.findByIdAndState(id,type);
        if (result == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        hasAudit(result.getAdminAuditTime());

        Date now = new Date();

        //将当前正在审核的任务加锁
        boolean isSuccess = hunterTaskService.updateState(id,HunterTaskState.ADMIN_ADUIT,now);
        if (!isSuccess){
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }

        result.setAdminAuditTime(new Timestamp(now.getTime()));
        result.toDetail();
        return result;
    }

    /**
     * 获取用户提交的提现审核详情信息
     * @param id
     * @return
     */
    @GetMapping("/finance/detail/{id:\\d+}")
    @ApiOperation(value = "待审核的提现详情")
    public UserWithdraw financeDetail(@PathVariable("id") String id){
        UserWithdraw result = userWithdrawService.findByIdAndState(id,WithdrawState.AUDIT);
        if (result == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        hasAudit(result.getAdminAuditTime());

        Date now = new Date();
        boolean isSuccess = userWithdrawService.updateState(id,WithdrawState.AUDIT_CENTER,now);
        if (!isSuccess){
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }

        result.setAdminAuditTime(new Timestamp(now.getTime()));
        return UserWithdraw.toDetail(result);

    }


    /**
     * 添加任务审核结果
     * @param addTaskAudit 要添加的任务审核信息
     * @param bindingResult 检查校验结果
     * @return
     */
    @PostMapping("/task/result")
    @ApiOperation(value = "添加任务审核结果")
    public AuditTask addTaskAudit(@Valid @RequestBody AddTaskAudit addTaskAudit,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        //对状态与类别进行判断
        if (addTaskAudit.getType().equals(AuditType.HUNTER_OK_TASK)){
            if (addTaskAudit.getResult().equals(AuditState.PASS) || addTaskAudit.getResult().equals(AuditState.PASS)){
                throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_FAILED);
            }
        }else {
            if (!addTaskAudit.getResult().equals(AuditState.PASS) || !addTaskAudit.getResult().equals(AuditState.PASS)){
                throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_FAILED);
            }
        }

        Audit audit = auditService.save(addTaskAudit.toAudit());
        return AuditTask.getBy(audit);
    }

    /**
     * 添加猎刃申请审核结果
     * @param addHunterAudit 要添加的用户审核信息
     * @param bindingResult 检查校验结果
     * @return
     */
    @PostMapping("/user/result")
    @ApiOperation(value = "添加猎刃申请审核结果")
    public AuditHunter addHunterAudit(@Valid @RequestBody AddHunterAudit addHunterAudit,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Audit audit = auditService.save(addHunterAudit.toAudit());
        return AuditHunter.getBy(audit);
    }

    /**
     * 添加提现审核结果
     * @param addWithdrawAudit 提现审核信息
     * @param bindingResult 检查校验结果
     * @return
     */
    @PostMapping("/withdraw/result")
    @ApiOperation(value = "添加提现审核结果")
    public AuditWithdraw addHunterAudit(@Valid @RequestBody AddWithdrawAudit addWithdrawAudit,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Audit audit = auditService.save(addWithdrawAudit.toAudit());
        return AuditWithdraw.getBy(audit);
    }





    /**
     * 获取审核任务的参照信息
     * @param id 任务编号
     * @return
     */
    @GetMapping("/task/refer/{id:\\d+}")
    @ApiOperation(value = "获取审核任务的参照信息")
    public TaskAuditRefer getTaskAuditRefer(@PathVariable("id") Long id){
        return new TaskAuditRefer();
    }



    /**
     * 判断任务是否可以被审核
     * @param timestamp
     */
    private void hasAudit(Timestamp timestamp) {

        //判断任务是否被其他人审核
        if (timestamp != null) {
            Timestamp begin = timestamp;
            Timestamp end = new Timestamp(System.currentTimeMillis());
            if (end.getTime() - begin.getTime() < AUDIT_LONG) {
                throw new ValidException("锁定中，" + new Timestamp(end.getTime() - begin.getTime()));
            }
        }
    }
}
