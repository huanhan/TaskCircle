package com.tc.controller;

import com.tc.db.entity.HunterTask;
import com.tc.db.entity.HunterTaskStep;
import com.tc.db.entity.Task;
import com.tc.db.entity.pk.HunterTaskStepPK;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.TaskState;
import com.tc.dto.ModifyHunterTaskStep;
import com.tc.dto.audit.AuditContext;
import com.tc.dto.huntertask.AddHunterTaskStep;
import com.tc.dto.huntertask.DeleteHunterTaskStep;
import com.tc.dto.huntertask.ModifyHunterTask;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.HunterTaskService;
import com.tc.service.HunterTaskStepService;
import com.tc.service.TaskService;
import com.tc.until.StringResourceCenter;
import com.tc.until.TimestampHelper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;

/**
 * 猎刃任务控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/app/ht")
public class AppHunterTaskController {

    /**
     * 允许提交管理员协商的用户审核必要次数
     */
    private static final int OK_AUDIT_ADMIN_COUNT = 2;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HunterTaskService hunterTaskService;

    @Autowired
    private HunterTaskStepService hunterTaskStepService;


    /**
     * HunterTask步骤1：猎刃点击接取任务按钮，接取成功后猎刃的任务状态为RECEIVE("任务接取"),
     * 如果接完后对应的任务不可接了，那么需要设置任务状态为FORBID_RECEIVE("任务禁止被接取")
     * 猎刃点击接取任务按钮
     * 此时新增一条猎刃任务信息
     * 并在猎刃的账户中扣除任务需要的押金
     * @param id
     * @param taskId
     */
    @GetMapping("/accept/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "猎刃点击按钮接任务")
    public void acceptTask(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {

        //在service中添加猎刃任务信息，需要做如下判断：任务是否允许被接，接完后是否需要修改任务状态，接完任务猎刃需要缴纳的押金
        boolean isSuccess = hunterTaskService.acceptTask(id,taskId);
        if (!isSuccess){
            throw new DBException("接任务失败！");
        }

    }

    /**
     * HunterTask步骤2：猎刃点击开始任务，如果任务成功开始，则设置状态为BEGIN("开始")
     *
     *
     *
     * @param id
     * @param taskId
     */
    @GetMapping("/begin/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "猎刃点击按钮开始任务")
    public void beginTask(@PathVariable("id") Long id, @PathVariable("taskId") String taskId){
        //获取猎刃任务详情
        HunterTask hunterTask = hunterTaskService.findOne(taskId);
        if (hunterTask == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        //判断猎刃任务状态
        if (!hunterTask.getState().equals(HunterTaskState.RECEIVE) || hunterTask.getStop()){
            throw new DBException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }
        //获取任务详情
        Task task = hunterTask.getTask();
        if (task == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        //判断任务是否可被开始
        //判断任务的状态
        if (!task.getState().equals(TaskState.ISSUE) || !task.getState().equals(TaskState.FORBID_RECEIVE)){
            throw new ValidException("任务状态异常");
        }
        //判断可以执行的时间
        if (TimestampHelper.today().after(task.getBeginTime())){
            throw new ValidException("还没到开始时间");
        }
        boolean isBegin = hunterTaskService.beginTask(taskId);
        if (!isBegin){
            throw new DBException("任务未能开始！");
        }
    }

    /**
     * HunterTask步骤3：猎刃开始执行任务，
     * 如果是第一次添加步骤，则修改任务的状态为EXECUTORY("正在执行")
     * 如果添加的是最后一次步骤，则修改任务的状态为TASK_COMPLETE("任务完成")
     * @param id
     * @param addHunterTaskStep
     * @param bindingResult
     * @return
     */
    @PostMapping("/add/step")
    @ApiOperation(value = "添加猎刃的任务步骤")
    public HunterTaskStep add(@PathVariable("id") Long id, @Valid @RequestBody AddHunterTaskStep addHunterTaskStep, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        //获取猎刃任务详情
        HunterTask query = hunterTaskService.findOne(addHunterTaskStep.getId());
        if (query == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //验证指定的猎刃任务是否可以添加
        if (!query.getState().equals(HunterTaskState.BEGIN)
                || !query.getState().equals(HunterTaskState.EXECUTE)
                || query.getStop()){
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        HunterTaskStep news = AddHunterTaskStep.toHunterTaskStep(addHunterTaskStep);
        news.setHunterTask(query);

        //添加步骤
        HunterTaskStep result = hunterTaskStepService.save(news);
        if (result == null){
            throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
        }
        return HunterTaskStep.toDetail(result);
    }

    /**
     * HunterTask步骤3：猎刃在执行过程中修改步骤内容，此时猎刃任务的状态一定是EXECUTORY("正在执行")
     * @param id
     * @param modifyHunterTaskStep
     * @param bindingResult
     * @return
     */
    @PostMapping("/update/step/{id:\\d+}")
    @ApiOperation(value = "修改猎刃的任务步骤")
    public HunterTaskStep update(@PathVariable("id") Long id, @Valid @RequestBody ModifyHunterTaskStep modifyHunterTaskStep, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        HunterTaskStep hunterTaskStep = hunterTaskStepService.findOne(new HunterTaskStepPK(modifyHunterTaskStep.getId(),modifyHunterTaskStep.getStep()));
        if (hunterTaskStep == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //对状态进行判断
        if (!hunterTaskStep.getHunterTask().getState().equals(HunterTaskState.EXECUTE)
                || hunterTaskStep.getHunterTask().getStop()){
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        if(!ModifyHunterTaskStep.isUpdate(hunterTaskStep,modifyHunterTaskStep)){
            throw new ValidException(StringResourceCenter.VALIDATOR_UPDATE_ABNORMAL);
        }

        HunterTaskStep result = hunterTaskStepService.update(hunterTaskStep);
        if (result == null){
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }

        return HunterTaskStep.toDetail(result);

    }

    /**
     * HunterTask步骤3：删除猎刃的任务步骤，需要判断任务状态，满足指定状态才允许删除，删除成功后将修改状态为EXECUTORY("正在执行")
     * @param id
     * @param deleteHunterTaskStep
     * @param bindingResult
     */
    @PostMapping("/delete/step")
    @ApiOperation(value = "删除猎刃的任务步骤")
    public void delete(@PathVariable("id") Long id, @Valid @RequestBody DeleteHunterTaskStep deleteHunterTaskStep, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        //获取删除的任务步骤详情
        HunterTaskStep hunterTaskStep = hunterTaskStepService.findOne(new HunterTaskStepPK(deleteHunterTaskStep.getHunterTaskId(),deleteHunterTaskStep.getStep()));
        if (hunterTaskStep == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //对状态进行判断
        if (!hunterTaskStep.getHunterTask().getState().equals(HunterTaskState.EXECUTE)
                || !hunterTaskStep.getHunterTask().getState().equals(HunterTaskState.TASK_COMPLETE)
                || hunterTaskStep.getHunterTask().getStop()){
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        boolean isDelete = hunterTaskStepService.deleteById(new HunterTaskStepPK(deleteHunterTaskStep.getHunterTaskId(),deleteHunterTaskStep.getStep()));
        if (!isDelete){
            throw new DBException(StringResourceCenter.DB_DELETE_FAILED);
        }
    }


    /**
     * HunterTask步骤3：
     * 猎刃修改执行任务的内容
     * @param id
     * @param modifyHunterTask
     * @param bindingResult
     * @return
     */
    @PutMapping("/update/{id:\\d+}")
    @ApiOperation(value = "猎刃修改执行的任务的内容")
    public HunterTask update(@PathVariable("id") Long id, @Valid @RequestBody ModifyHunterTask modifyHunterTask, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        //获取猎刃任务详情
        HunterTask old = hunterTaskService.findOne(modifyHunterTask.getId());
        if (old == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //验证指定的猎刃任务是否可以修改
        if (!old.getState().equals(HunterTaskState.BEGIN)
                || !old.getState().equals(HunterTaskState.EXECUTE)
                || !old.getState().equals(HunterTaskState.TASK_COMPLETE)
                || old.getStop()){
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }
        if (old.getContext().equals(modifyHunterTask.getContext())){
            throw new ValidException(StringResourceCenter.VALIDATOR_UPDATE_ABNORMAL);
        }

        //修改内容
        HunterTask hunterTask = hunterTaskService.update(modifyHunterTask.getId(),modifyHunterTask.getContext());
        if (hunterTask == null){
            throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
        }
        return HunterTask.toDetail(hunterTask);
    }

    /**
     * HunterTask步骤4：提交用户审核，当前猎刃任务的状态必须为TASK_COMPLETE("任务完成")
     * 提交成功后，任务状态变为AWAIT_USER_AUDIT("等待用户审核")
     * @param id
     * @param htId
     */
    @GetMapping("/user/audit/{htId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "猎刃将任务提交用户审核")
    public void upAuditToUser(@PathVariable("id") Long id,@PathVariable("htId") String htId){
        //获取猎刃任务信息
        HunterTask hunterTask = hunterTaskService.findOne(htId);
        if (hunterTask == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        //判断猎刃任务与当前猎刃的关系
        if (!hunterTask.getHunterId().equals(id)){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        //对猎刃任务的状态进行判断
        if (!HunterTaskState.isUpAuditToUser(hunterTask.getState()) || hunterTask.getStop()){
            throw new ValidationException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }
        //提交用户审核（即修改状态）
        boolean isSuccess = hunterTaskService.updateState(htId,HunterTaskState.AWAIT_USER_AUDIT);
        if (!isSuccess){
            throw new ValidationException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
    }

    /**
     * HunterTask步骤5：猎刃点击重做任务，将任务状态修改为TASK_COMPLETE("任务完成")
     * @param id
     * @param htId
     */
    @GetMapping("/rework/{htId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "猎刃点击重做任务")
    public void reworkTask(@PathVariable("id") Long id,@PathVariable("htId") String htId){
        //获取猎刃任务信息
        HunterTask hunterTask = hunterTaskService.findOne(htId);
        if (hunterTask == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        //判断任务状态是否允许重做
        if (!HunterTaskState.isRework(hunterTask.getState()) || hunterTask.getStop()){
            throw new ValidException("当前任务不允许重做");
        }
        //重新设置任务的状态
        boolean isSuccess = hunterTaskService.updateState(hunterTask.getId(),HunterTaskState.TASK_COMPLETE);
        if (!isSuccess){
            throw new ValidationException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
    }

    /**
     * HunterTask步骤5：猎刃点击放弃任务
     * 如果成功放弃任务，则将任务状态置为TASK_ABANDON("任务放弃")
     * 如果任务完成了但是不通过用户审核而放弃时，将任务状态设置为END_NO（“结束未完成”）
     * 如果不允许直接放弃，则将任务状态置为WITH_USER_NEGOTIATE("与用户协商"),
     * @param id
     * @param context
     */
    @PostMapping("/abandon/{id:\\d+}")
    @ApiOperation(value = "猎刃点击放弃任务")
    public void abandonTask(@PathVariable("id") Long id, @Valid @RequestBody AuditContext context, BindingResult bindingResult){
        //获取猎刃任务信息
        HunterTask hunterTask = hunterTaskService.findOne(context.getId());
        if (hunterTask == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        if (!hunterTask.getStop() && !hunterTask.getTask().getState().equals(TaskState.ABANDON_COMMIT)) {
            //任务没有暂停时，判断任务的状态是否允许放弃
            if (!HunterTaskState.isAbandon(hunterTask.getState())) {
                throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
            }
        }else {
            //任务被暂停时，判断任务暂停是否没有被完成
            if (HunterTaskState.isOk(hunterTask.getState())){
                throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
            }
        }

        //猎刃放弃任务
        boolean isSuccess = hunterTaskService.abandonTask(hunterTask,context.getContext());
        if (!isSuccess){
            throw new ValidationException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
    }

    /**
     * HunterTask步骤5：猎刃将任务提交给管理员审核
     * 如果是放弃任务的审核则设置状态为COMMIT_TO_ADMIN("提交管理员放弃申请")
     * 如果是任务完成的审核则设置状态为COMMIT_ADMIN_AUDIT("任务完成，提交管理员审核")
     * 猎刃需要无条件服从管理员审核
     * @param id
     * @param htId
     */
    @GetMapping("/admin/audit/{htId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "猎刃点击提交管理员审核")
    public void auditToAdmin(@PathVariable("id") Long id,@PathVariable("htId") String htId){
        //获取猎刃任务信息
        HunterTask hunterTask = hunterTaskService.findOne(htId);
        if (hunterTask == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //判断任务的状态
        if (!HunterTaskState.isUpAuditToAdmin(hunterTask.getState())){
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        //判断与用户协商的次数是否达标
        if (hunterTask.getUserRejectCount() < OK_AUDIT_ADMIN_COUNT){
            throw new ValidException("请先与用户协商" + OK_AUDIT_ADMIN_COUNT + "次后，在提交审核");
        }

        //将任务提交管理员审核
        boolean isSuccess = hunterTaskService.toAdminAudit(htId,hunterTask.getState());
        if (!isSuccess){
            throw new ValidationException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
    }

    /**
     * Task步骤5：猎刃同意用户放弃任务,此时会设置猎刃任务的状态为任务被放弃TASK_BE_ABANDON
     * 并且如果放弃任务的猎刃时猎刃中的最后一个，则会将用户的任务设置成放弃状态，并且退回用户押金
     * @param id
     * @param taskId
     */
    @GetMapping("/abandon/success/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "猎刃点击提交管理员审核")
    public void abandonPassByHunter(@PathVariable("id") Long id,@PathVariable("taskId") String taskId){
        //获取需要猎刃同意的任务
        Task task = taskService.findOne(taskId);
        if (task == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        //判断任务的状态是否需要猎刃同意
        if (!task.getState().equals(TaskState.ABANDON_COMMIT)){
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }
        //获取任务对应的本人的猎刃任务(猎刃被暂停的猎刃任务在指定的任务的猎刃任务中只允许存在1个)
        HunterTask hunterTask = hunterTaskService.findOne(taskId,id);
        if (hunterTask == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        //猎刃同意用户放弃任务
        boolean isSuccess = hunterTaskService.abandonPassByHunter(hunterTask);
        if (!isSuccess){
            throw new ValidationException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
        //判断是否需要放弃用户的任务
        isSuccess = taskService.hasAbandon(task);
        if (!isSuccess){
            throw new ValidationException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
    }

    /**
     * Task步骤5：猎刃不同意用户放弃任务，此时将猎刃任务的状态设置成HUNTER_REPULSE("猎刃拒绝用户放弃")，并修改对应次数
     * @param id
     * @param context
     * @param bindingResult
     */
    @PostMapping("/abandon/failure/{id:\\d+}")
    @ApiOperation(value = "猎刃点击用户的放弃申请不通过")
    public void abandonNotPassByHunter(@PathVariable("id") Long id, @Valid @RequestBody AuditContext context, BindingResult bindingResult){
        //获取需要猎刃不同意的任务
        Task task = taskService.findOne(context.getId());
        if (task == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        //判断任务的状态是否需要猎刃同意
        if (!task.getState().equals(TaskState.ABANDON_COMMIT)){
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }
        //获取任务对应的本人的猎刃任务
        HunterTask hunterTask = hunterTaskService.findOne(context.getId(),id);
        if (hunterTask == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        //设置猎刃任务状态为不同意用户放弃
        boolean isSuccess = hunterTaskService.abandonNotPassByHunter(hunterTask.getId(),context.getContext());
        if (!isSuccess){
            throw new ValidationException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
        //判断用户的任务是否完全被拒绝
        isSuccess = taskService.taskIsReject(task.getId());
        if (!isSuccess){
            throw new ValidationException("还有猎刃没有拒绝用户");
        }
    }

}
