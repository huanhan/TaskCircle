package com.tc.controller;

import com.tc.db.entity.HunterTask;
import com.tc.db.entity.HunterTaskStep;
import com.tc.db.entity.Task;
import com.tc.db.entity.TaskStep;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.TaskState;
import com.tc.dto.Result;
import com.tc.dto.task.*;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.HunterTaskService;
import com.tc.service.HunterTaskStepService;
import com.tc.service.TaskService;
import com.tc.service.TaskStepService;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * APP用户控制器
 * 所有的用户编号都是用来校验用户，
 * 有用户编号的地方，说明该接口需要用户授权访问
 *
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/app/task")
public class AppTaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private HunterTaskService hunterTaskService;

    @Autowired
    private TaskStepService taskStepService;

    @Autowired
    private HunterTaskStepService hunterTaskStepService;

    /**
     * 根据状态获取指定用户的任务列表
     *
     * @param id        用户编号
     * @param queryTask 状态不能为空
     * @return
     */
    @PostMapping("/user/{id:\\d+}")
    @ApiOperation(value = "根据状态获取指定用户的任务列表")
    public Result taskByUser(@PathVariable("id") Long id, @RequestBody QueryTask queryTask) {
        if (!hasState(queryTask.getState())) {
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        Page<Task> taskPage = taskService.findByQueryTask(queryTask);
        return Result.init(taskPage);
    }

    /**
     * 根据状态获取指定猎刃的任务列表
     *
     * @param id              用户编号
     * @param queryHunterTask 状态不能为空
     * @return
     */
    @PostMapping("/hunter/{id:\\d+}")
    @ApiOperation(value = "根据状态获取指定猎刃的任务列表")
    public Result taskByHunter(@PathVariable("id") Long id, @RequestBody QueryHunterTask queryHunterTask) {
        if (!hasState(queryHunterTask.getState())) {
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        Page<HunterTask> hunterTaskPage = hunterTaskService.findByQueryHunterTask(queryHunterTask);
        return Result.init(hunterTaskPage);
    }

    /**
     * 获取所有已发布的任务，排序条件由外部传入，QueryTask支持
     * 格式
     * sort：[{
     * "direction": "ASC",
     * "property": "auditTime",
     * "ignoreCase": false,
     * "nullHandling": "NATIVE",
     * "ascending": true,
     * "descending": false
     * }
     * ]
     *
     * @param queryTask
     * @return
     */
    @PostMapping("/query")
    @ApiOperation(value = "根据排序条件获取已发布的任务")
    public Result taskByAll(@RequestBody QueryTask queryTask) {
        queryTask.setState(TaskState.ISSUE);
        Page<Task> taskPage = taskService.findByQueryTask(queryTask);
        return Result.init(taskPage);
    }

    /**
     * 获取任务详情信息,该任务详情是任何人都可以看的任务详情
     * <p>
     * 如果需要在任务详情信息中显示任务步骤等，
     * 请在Service中进行查询拼装
     *
     * @param id
     * @return
     */
    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "获取任务详情信息")
    public Task look(@PathVariable("id") String id) {
        //根据Id获取任务
        Task task = taskService.findOne(id);

        //判断状态是否为已发布
        if (task.getState() == TaskState.ISSUE) {
            throw new ValidException("任务状态异常");
        }

        //加入任务步骤
        List<TaskStep> taskSteps = taskStepService.findByTaskId(id, new Sort(Sort.Direction.ASC, TaskStep.STEP));
        task.setTaskSteps(taskSteps);

        return task;
    }

    /**
     * todo 未完善 还未加入步骤数据
     * 根据任务编号，获取本人发布的任务的猎刃执行者列表，通过该列表点击获取执行详情
     *
     * @param id
     * @param queryHunterTask
     * @return
     */
    @PostMapping("/hunterTask/{id:\\d+}")
    @ApiOperation(value = "根据任务编号获取猎刃执行者列表")
    public Result hunterTaskByTaskId(@PathVariable("id") Long id, @RequestBody QueryHunterTask queryHunterTask) {
        //任务编号不允许为空
        if (queryHunterTask.getTaskId() == null) {
            throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_FAILED);
        }
        //获取当前所有的猎刃信息
        Page<HunterTask> hunterTaskPage = hunterTaskService.findByQueryHunterTask(queryHunterTask);

        return Result.init(hunterTaskPage);
    }

    /**
     *
     * 普通用户将任务提交审核
     * <p>
     * 需要审核的任务有（用户新建任务，用户放弃的任务）
     *
     * @param id
     * @param taskId
     */
    @GetMapping("/user/upAudit/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "将用户的任务提交审核")
    public void upAuditByUser(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {
        //根据任务编号获取任务
        Task task = taskService.findOne(taskId);

        //判断查询的任务是否存在
        if (task == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //判断任务的发布者与提交任务审核的用户是否一致
        if (!task.getUser().getId().equals(id)){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }

        //判断任务的状态是否允许提交审核
        if (!hasCommit(task)){
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        //提交审核，即修改状态
        boolean isSuccess = taskService.commitAudit(taskId,task.getState());

        //验证修改是否成功
        if (!isSuccess){
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
    }

    /**
     * todo 审核押金 未完善
     * 猎刃将任务提交审核
     * <p>
     * 需要审核的任务有（猎刃放弃任务，猎刃完成的任务被用户拒绝）
     *
     * @param id
     * @param taskId
     */
    @GetMapping("/hunter/upAudit/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "将用户的任务提交审核")
    public void upAuditByHunter(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {
        //根据猎刃任务编号获取任务
        HunterTask hunterTask = hunterTaskService.findOne(taskId);
        //判断任务的接收者与提交任务审核的用户是否一致
        if (hunterTask.getHunterId() == id) {
            //判断任务的状态是猎刃放弃任务
            if (hunterTask.getState() == HunterTaskState.TASK_ABANDON) {
                //修改任务状态为猎刃放弃任务COMMIT_TO_ADMIN
                hunterTaskService.updateState(hunterTask.getTaskId(), HunterTaskState.COMMIT_TO_ADMIN, new Date());
            }

            //判断任务的状态是猎刃完成任务需要审核
            if (hunterTask.getState() == HunterTaskState.TASK_COMPLETE) {
                //完成任务需要审核COMMIT_ADMIN_ADUIT
                hunterTaskService.updateState(hunterTask.getTaskId(), HunterTaskState.COMMIT_ADMIN_ADUIT, new Date());
            }

        }

    }

    /**
     * todo 有可能 要审核押金 未完善
     * 猎刃将完成的任务提交用户
     *
     * @param id
     * @param taskId 猎刃任务表的任务id
     */
    @GetMapping("/hunter/toUser/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "将完成的任务交给用户查看")
    public void upAuditByHunterToUser(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {
        //根据编号获取猎刃任务详情（包括执行步骤）
        HunterTask hunterTask = hunterTaskService.findOne(taskId);
        //所有改任务的步骤
        List<HunterTaskStep> hunterTaskSteps = hunterTaskStepService.findByHunterTaskId(hunterTask.getId(), new Sort(Sort.Direction.ASC, HunterTaskStep.STEP));

        //进行简要的系统判断（比如步骤是否都有完成）
        List<TaskStep> taskSteps = taskStepService.findByTaskId(hunterTask.getTaskId(), new Sort(Sort.Direction.ASC, TaskStep.STEP));
        if (taskSteps.size() == hunterTaskSteps.size()) {
            //都有提交信息算是完成了
            //修改猎刃任务状态为任务完成，后面用户需要查看猎刃完成的任务情况，根据情况点击确认完成
            hunterTaskService.updateState(taskId, HunterTaskState.TASK_COMPLETE, new Date());
        }


    }

    /**
     * 添加任务已实现
     *
     * @param id            用户编号
     * @param addTask
     * @param bindingResult
     * @return
     */
    @PostMapping("/add/{id:\\d+}")
    @ApiOperation("添加我的新任务")
    public Task add(@PathVariable("id") Long id, @Valid @RequestBody AddTask addTask, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Task task = taskService.save(AddTask.toTask(addTask));
        return Task.toDetail(task);
    }


    /**
     * 修改任务信息
     *
     * @param id            用户编号
     * @param modifyTask
     * @param bindingResult
     * @return
     */
    @PostMapping("/modify/{id:\\d+}")
    @ApiOperation("修改我的任务")
    public Task update(@PathVariable("id") Long id, @Valid @RequestBody ModifyTask modifyTask, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }


        return new Task();
    }

    /**
     * 删除我的任务
     *
     * @param id
     * @param taskId
     */
    @DeleteMapping("/remove/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "删除我的任务")
    public void delete(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {
        //根据任务编号获取任务
        Task task = taskService.findOne(taskId);

        //判断查询的任务是否存在
        if (task == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //判断任务的发布者与删除任务的用户是否一致
        if (!task.getUser().getId().equals(id)){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }

        //判断当前的任务状态是否允许被删除

        //删除任务，将任务状态修改为删除状态
    }

    /**
     * 猎刃接任务
     *
     * @param id
     * @param taskId
     */
    @GetMapping("/accept/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "猎刃接任务")
    public void acceptTask(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {
        //获取用户详情信息

        //判断该用户是否可以接任务

        //在service中添加猎刃任务信息，需要做如下判断：任务是否允许被接，接完后是否需要修改任务状态，接完任务猎刃需要缴纳的押金
    }

    /**
     * 任务执行步骤情况
     *
     * @param id
     * @param addHTS
     * @param bindingResult
     */
    @PostMapping("/hts/{id:\\d+}")
    @ApiOperation(value = "添加猎刃任务执行步骤情况")
    public void addHunterTaskStep(@PathVariable("id") Long id, @Valid @RequestBody AddHTS addHTS, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }


    }


    /**
     * 用户发布任务
     * <p>
     * task需要替换成DTO
     *
     * @param id   用户编号
     * @param task 任务信息
     */
    @GetMapping("/issue/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "发布我的任务")
    public void issueTask(@PathVariable("id") Long id, @Valid @RequestBody Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }

        //获取任务信息

        //判断任务状态是否允许发布

        //修改任务状态为发布状态，并且在修改完后，从用户账户中扣除发布需要的押金
    }

    /**
     * 用户撤回
     *
     * @param id     用户编号
     * @param taskId 任务编号
     */
    @GetMapping("/out/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "撤回我的任务")
    public void outTask(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {
        //获取任务信息

        //判断任务状态是否可被撤回

        //如果不可撤回，发送异常报告，通知用户当前任务已被接取，并询问用户是否走猎刃协商流程

        //如果可以撤回，则修改任务状态，并将押金退回用户账户
    }

    /**
     * 用户点击放弃任务
     * 放弃成功的任务将直接退还押金，并且不能重新发布
     * @param id
     * @param taskId
     */
    @GetMapping("/user/abandon/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "用户点击放弃任务")
    public void abandonTaskByUser(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {
        //根据任务编号获取任务
        Task task = taskService.findOne(taskId);

        //判断查询的任务是否存在
        if (task == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //判断任务的发布者与放弃任务的用户是否一致
        if (!task.getUser().getId().equals(id)){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }

        //如果该放弃的任务不需要协商，不需要审核，则直接放弃任务，退还押金，并退还猎刃的押金，和将猎刃的任务状态修改为任务被放弃
        int count = taskService.abandonTask(id,task);

        if (count != 0){
            throw new ValidException("目前有" + count + "猎刃正在执行该任务！已将任务做下架处理");
        }
    }


    /**
     * 猎刃点击放弃任务
     *
     * @param id
     * @param taskId
     */
    @GetMapping("/user/abandon/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "用户点击放弃任务")
    public void abandonTaskByHunter(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {
        //根据Id判断放弃任务的是用户还是猎刃

        //根据taskId获取任务详情

        //判断该任务的放弃条件

        //如果满足直接放弃的条件，则在这里直接放弃，并退回用户押金或者猎刃押金

        //如果不满足，则抛除异常并交由用户自己选择
    }

    /**
     * 判断传入的猎刃任务状态是否允许访问
     * 允许访问返回True，不允许访问返回False
     *
     * @param state
     * @return
     */
    private boolean hasState(HunterTaskState state) {
        if (state == null) {
            return false;
        }
        return true;
    }

    /**
     * 判断传入的任务状态是否允许访问
     * 允许访问返回True，不允许访问返回False
     *
     * @param state
     * @return
     */
    private boolean hasState(TaskState state) {

        if (state == null) {
            return false;
        }

        return true;
    }


    /**
     * 判断用户提交审核时的状态是否允许提交
     * @param task
     * @return
     */
    private boolean hasCommit(Task task){
        TaskState taskState = task.getState();

        //任务新建状态可提交审核
        if (taskState.equals(TaskState.NEW_CREATE)){
            return true;
        }

        //猎刃拒绝状态可提交审核
        if (taskState.equals(TaskState.HUNTER_REJECT)){
            return true;
        }

        return false;
    }



}
