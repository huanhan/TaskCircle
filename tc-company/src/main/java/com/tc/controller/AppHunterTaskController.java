package com.tc.controller;

import com.tc.db.entity.HunterTask;
import com.tc.db.entity.HunterTaskStep;
import com.tc.db.entity.Task;
import com.tc.db.entity.pk.HunterTaskStepPK;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.TaskState;
import com.tc.dto.ModifyHunterTaskStep;
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
import java.sql.Timestamp;

/**
 * 猎刃任务控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/app/ht")
public class AppHunterTaskController {


    @Autowired
    private TaskService taskService;

    @Autowired
    private HunterTaskService hunterTaskService;

    @Autowired
    private HunterTaskStepService hunterTaskStepService;


    /**
     * 步骤1：猎刃点击接取任务按钮，接取成功后猎刃的任务状态为RECEIVE("任务接取"),
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
     * 步骤2：猎刃点击开始任务，如果任务成功开始，则设置状态为BEGIN("开始")
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
     * 步骤3：猎刃开始执行任务，
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
                || !query.getState().equals(HunterTaskState.EXECUTORY)){
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
     * 步骤3：猎刃在执行过程中修改步骤内容，此时猎刃任务的状态一定是EXECUTORY("正在执行")
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
        if (!hunterTaskStep.getHunterTask().getState().equals(HunterTaskState.EXECUTORY)){
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
     * 步骤3：删除猎刃的任务步骤，需要判断任务状态，满足指定状态才允许删除，删除成功后将修改状态为EXECUTORY("正在执行")
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

        boolean isDelete = hunterTaskStepService.deleteById(new HunterTaskStepPK(deleteHunterTaskStep.getHunterTaskId(),deleteHunterTaskStep.getStep()));
        if (!isDelete){
            throw new DBException(StringResourceCenter.DB_DELETE_FAILED);
        }
    }


    /**
     * 步骤3：
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
                || !old.getState().equals(HunterTaskState.EXECUTORY)
                || !old.getState().equals(HunterTaskState.TASK_COMPLETE)){
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
     * 步骤4：提交用户审核，当前猎刃任务的状态必须为TASK_COMPLETE("任务完成")
     * 提交成功后，任务状态变为AWAIT_USER_AUDIT("等待用户审核")
     * @param id
     * @param htId
     */
    @GetMapping("/user/audit/{htId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "猎刃将任务提交用户审核")
    public void upAuditToUser(@PathVariable("id") Long id,@PathVariable("htId") Long htId){

    }

}
