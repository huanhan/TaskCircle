package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.TaskState;
import com.tc.dto.Result;
import com.tc.dto.app.*;
import com.tc.dto.audit.AuditContext;
import com.tc.dto.task.*;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.*;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
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

    /**
     * 允许用户放弃任务的最大猎刃任务执行人数限制
     */
    public static final int USER_ABANDON_NUMBER = 5;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HunterTaskService hunterTaskService;

    @Autowired
    private TaskClassifyService taskClassifyService;

    @Autowired
    private PushMsgService pushMsgService;

    /**
     * Task步骤1：新建任务在保存成功后的状态为NEW_CREATE("新建")
     * 用户添加任务
     *
     * @param addTaskReq
     * @param bindingResult
     * @return
     */
    @PostMapping("{id:\\d+}")
    @ApiOperation("添加任务")
    public ResultApp add(@PathVariable("id") Long id, @Valid @RequestBody AddTaskDto addTaskReq, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Task task = AddTaskDto.toTask(addTaskReq);
        task.setUserId(id);
        taskService.save(task);

        return ResultApp.init("增加成功");
    }

    /**
     * 获取任务类别列表
     *
     * @return
     */
    @GetMapping("tcl")
    @ApiOperation(value = "获取任务类别列表")
    public List<TaskClassifyAppDto> all() {
        //查询全部
        List<TaskClassify> classifies = taskClassifyService.findAll();
        //重制
        List<TaskClassify> resetTaskClassify = TaskClassify.reset(classifies);

        ArrayList<TaskClassifyAppDto> taskClassifyAppDtos = new ArrayList<>();
        ArrayList<TaskClassifyAppDto> childTaskClassifyAppDtos;

        //循环重制结果
        for (TaskClassify taskClassify : resetTaskClassify) {
            //拷贝重制后的父属性
            TaskClassifyAppDto taskClassifyAppDto = new TaskClassifyAppDto();
            BeanUtils.copyProperties(taskClassify, taskClassifyAppDto);
            //判断是否有子
            if (taskClassify.getTaskClassifies() != null) {
                childTaskClassifyAppDtos = new ArrayList<>();
                TaskClassifyAppDto childTaskClassifyAppDto;
                //循环增加子
                for (TaskClassify classify : taskClassify.getTaskClassifies()) {
                    childTaskClassifyAppDto = new TaskClassifyAppDto();
                    BeanUtils.copyProperties(classify, childTaskClassifyAppDto);
                    childTaskClassifyAppDtos.add(childTaskClassifyAppDto);
                }
                taskClassifyAppDto.setTaskClassifies(childTaskClassifyAppDtos);
            }
            taskClassifyAppDtos.add(taskClassifyAppDto);
        }

        return taskClassifyAppDtos;
    }


    /**
     * Task步骤1：属于添加任务
     * 可能需要判断任务的状态，属于指定状态的任务才允许添加步骤
     * 用户添加任务步骤
     * @param addTaskStep
     * @param bindingResult
     * @return
     */
    /*@PostMapping("/step")
    @ApiOperation("添加任务步骤")
    public TaskStep add(@Valid @RequestBody AddTaskStep addTaskStep,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        //根据任务编号获取任务
        Task task = taskService.findOne(addTaskStep.getTaskId());

        //判断查询的任务是否存在
        if (task == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        if (task.getState().equals(TaskState.NEW_CREATE)){
            throw new ValidException("只有新建状态下的任务才允许添加步骤");
        }

        TaskStep taskStep = taskStepService.save(AddTaskStep.toTaskStep(addTaskStep));
        return TaskStep.toDetail(taskStep);
    }*/

    /**
     * 普通用户将任务提交审核
     * Task步骤2：用户提交新建任务的审核，此时任务状态被修改为AWAIT_AUDIT("等待审核")
     * <p>
     * 需要审核的任务有（用户新建任务，用户放弃的任务）
     *
     * @param id
     * @param taskId
     */
    @GetMapping("/user/upAudit/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "将用户的任务提交给管理员审核")
    public ResultApp upAuditByUser(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {
        //根据任务编号获取任务
        Task task = taskService.findOne(taskId);

        //判断查询的任务是否存在
        if (task == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //判断任务的发布者与提交任务审核的用户是否一致
        if (!task.getUser().getId().equals(id)) {
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }

        //判断任务的状态是否允许提交审核
        if (!TaskState.isToAdminAudit(task.getState())) {
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        //提交审核，即修改状态
        boolean isSuccess = taskService.commitAudit(taskId, task.getState());

        //验证修改是否成功
        if (!isSuccess) {
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }

        return ResultApp.init("提交成功");
    }

    /**
     * Task步骤2：取消审核
     *
     * @param id
     * @param taskId
     */
    @GetMapping("/user/di/upAudit/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "取消审核")
    public ResultApp diUpAuditByUser(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {
        //根据任务编号获取任务
        Task task = taskService.findOne(taskId);

        //判断查询的任务是否存在
        if (task == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //判断任务的发布者与提交任务审核的用户是否一致
        if (!task.getUser().getId().equals(id)) {
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }

        //判断任务的状态是否允许取消
        if (!TaskState.isDiAuditByAdmin(task.getState())) {
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        //取消审核
        boolean isSuccess = taskService.diCommitAudit(taskId, task.getState());

        //验证修改是否成功
        if (!isSuccess) {
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
        return ResultApp.init("取消成功");
    }


    /**
     * Task步骤3：用户点击发布任务按钮，如果发布成功，状态会变成ISSUE("任务发布中")
     * <p>
     * 用户发布任务，要求补充完整任务信息
     * 用户只能修改允许修改的内容，允许修改的内容由IssueTaskDTO指定
     * 点击了发布按钮后，会从用户账户中扣除需要的押金
     * <p>
     * <p>
     * task需要替换成DTO
     *
     * @param id        用户编号
     * @param issueTask 任务信息
     */
    @PostMapping("/issue/{id:\\d+}")
    @ApiOperation(value = "发布我的任务")
    public TaskDetailAppDto issueTask(@PathVariable("id") Long id, @Valid @RequestBody IssueTaskDto issueTask, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }
        //根据任务编号获取任务
        Task task = taskService.findOne(issueTask.getId());

        //判断查询的任务是否存在
        if (task == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //判断任务状态是否允许发布
        if (!TaskState.isIssue(task.getState())) {
            throw new DBException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        //将DTO转成Entity
        IssueTaskDto.toTask(task, issueTask);

        //判断用户的罚金是否在允许范围之内
//        if (task.getPeopleNumber() <= 10){
//            if (task.getCompensateMoney() > FloatHelper.multiply(task.getMoney(),0.1f) || task.getCompensateMoney() <= 0) {
//                throw new ValidException("罚金不符合规范");
//            }
//        }else {
//            if (task.getCompensateMoney() >
//                    FloatHelper.multiply(task.getMoney(),FloatHelper.divied(1f,(float)task.getPeopleNumber())) ||
//                    task.getCompensateMoney() <= 0){
//                throw new ValidException("罚金不符合规范");
//            }
//        }

        //修改任务状态为发布状态，并且在修改完后，从用户账户中扣除发布需要的押金
        Task result = taskService.updateAndUserMoney(task);

        return TaskDetailAppDto.toDetail(taskService.findOne(result.getId()));

    }

    /**
     * Task步骤4：用户点击撤回按钮，如果撤回成功，则修改任务状态OUT（任务被撤回）
     * 撤回任务的目的时为了不让人继续接任务
     * 用户不可以修改撤回的任务，只能选择上架
     * 只有在发布中的任务才可撤回
     * 任务的撤回会将满足放弃条件的猎刃任务强行放弃掉
     * 放弃的条件：猎刃任务状态为RECEIVE("任务接取")，并且接取的时间少于用户设置的允许放弃时间
     * 如果满足撤回条件：需要退回猎刃押金，并将猎刃任务状态设置成TASK_BE_ABANDON("任务被放弃")
     * 任务的撤回不影响任务进行中的猎刃进行任务
     *
     * @param id     用户编号
     * @param taskId 任务编号
     */
    @GetMapping("/out/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "撤回我的任务")
    public ResultApp outTask(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {
        //根据任务编号获取任务
        Task task = taskService.findOne(taskId);

        //判断查询的任务是否存在
        if (task == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //判断任务状态是否可被撤回
        if (!task.getState().equals(TaskState.ISSUE)) {
            throw new ValidationException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        //撤回任务
        boolean isSuccess = taskService.outTask(task);
        if (!isSuccess) {
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }

        return ResultApp.init("撤回成功");
    }

    /**
     * Task步骤4：用户如果撤回了任务，用户可以选择继续上架或者放弃任务，
     * 该步骤针对的是上架按钮，上架成功，任务状态变成ISSUE("任务发布中")
     *
     * @param id
     * @param taskId
     */
    @GetMapping("/put/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "用户点击重新上架按钮功能")
    public ResultApp putTask(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {

        //根据任务编号获取任务
        Task task = taskService.findOne(taskId);

        //判断查询的任务是否存在
        if (task == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //判断任务状态是否可重新上架
        if (!task.getState().equals(TaskState.OUT)) {
            throw new ValidationException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        //重新上架任务
        taskService.updateState(taskId, TaskState.ISSUE);

        return ResultApp.init("重新上架完成");
    }

    /**
     * Task步骤5：用户点击放弃任务，
     * 只有下架或者禁止接取的任务才允许放弃
     * 放弃失败时，会将任务状态置为ABANDON_COMMIT("用户提交放弃的申请")，并且将猎刃的任务暂停
     * 放弃失败时，需要先走用户-猎刃协商流程，单状态变成ABANDON_COMMIT即开始改流程
     * 放弃成功时，会将任务状态置为ABANDON_OK("任务被放弃")，
     * 并且任务将直接退还押金，并且不能重新发布，此时一个任务的开始-放弃流程完毕
     * 用户放弃任务需要谨慎，因为用户点击放弃任务后，也会直接将猎刃放弃任务的申请直接通过
     *
     * @param id
     * @param taskId
     */
    @GetMapping("/user/abandon/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "用户点击放弃任务")
    public ResultApp abandonTaskByUser(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {
        //根据任务编号获取任务
        Task task = taskService.findOne(taskId);

        //判断查询的任务是否存在
        if (task == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //判断任务的发布者与放弃任务的用户是否一致
        if (!task.getUser().getId().equals(id)) {
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }

        //判断可放弃任务的状态
        if (!TaskState.isAbandon(task.getState())) {
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        //如果该放弃的任务不需要协商，不需要审核，则直接放弃任务，退还押金，并退还猎刃的押金，和将猎刃的任务状态修改为任务被放弃
        int count = taskService.abandonTask(id, task);

        if (count != 0) {
            String context = "";
            if (count <= AppTaskController.USER_ABANDON_NUMBER) {
                context = "目前有" + count + "猎刃正在执行该任务！已禁止猎刃继续执行任务";
            } else {
                context = "目前有" + count + "猎刃正在执行该任务！不能放弃任务，剩余猎刃将继续执行任务，可以选择与猎刃交流让猎刃放弃";
            }
            throw new ValidException(context);
        }

        return ResultApp.init("放弃任务成功");
    }

    /**
     * Task步骤5：用户点击取消放弃任务，修改任务为撤回状态OUT("任务被撤回")
     * ，并且将猎刃任务继续，
     *
     * @param id
     * @param taskId
     */
    @GetMapping("/user/di/abandon/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "用户点击取消放弃任务")
    public ResultApp diPassAbandon(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {
        //根据任务编号获取任务
        Task task = taskService.findOne(taskId);

        //判断查询的任务是否存在
        if (task == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //判断任务的发布者与放弃任务的用户是否一致
        if (!task.getUser().getId().equals(id)) {
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }

        //判断任务状态是否是放弃状态
        if (!task.getState().equals(TaskState.ABANDON_COMMIT)) {
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        //取消放弃任务
        boolean isSuccess = taskService.diAbandonTask(id, task);
        if (!isSuccess) {
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
        return ResultApp.init("取消放弃成功");
    }

    /**
     * HunterTask步骤4：用户点击审核成功按钮，通过后修改猎刃任务的状态为END_OK("任务结束并且完成")
     * 并且将押金退回与发放赏金
     * 此时猎刃任务的流程 新建-接完并完成 的流程完成
     * 并判断自己的任务是否全部被完成了，是的话就修改自己的任务的状态为FINISH("任务完成")
     * 此时，自身的任务流程 新建-完成 的流程完成
     * 猎刃将完成的任务提交用户审核
     * 用户点击审核通过
     *
     * @param id
     * @param htId
     */
    @GetMapping("/audit/success/{htId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "用户点击审核猎刃任务通过")
    public ResultApp auditSuccess(@PathVariable("id") Long id, @PathVariable("htId") String htId) {
        //根据编号获取猎刃任务
        HunterTask hunterTask = hunterTaskService.findOne(htId);

        //判断查询的任务是否存在
        if (hunterTask == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //判断哪些猎刃任务状态允许被用户审核
        if (!HunterTaskState.isAuditToUser(hunterTask.getState())) {
            throw new ValidationException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        //提交审核完成结果,并且判断本人任务是否被全部完成
        boolean isSuccess = hunterTaskService.auditPassByUser(hunterTask);
        if (!isSuccess) {
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
        return ResultApp.init("审核猎刃任务通过");
    }

    /**
     * HunterTask步骤4：用户点击审核不通过，并提交不通过内容，此时会根据添加任务时设置的是否可重做，是否要补偿来设置猎刃任务的状态
     * 状态包括
     * ALLOW_REWORK_ABANDON_HAVE_COMPENSATE("允许重做，放弃要补偿"),
     * ALLOW_REWORK_ABANDON_NO_COMPENSATE("允许重做，放弃不用补偿"),
     * NO_REWORK_NO_COMPENSATE("不能重做，不用补偿"),
     * NO_REWORK_HAVE_COMPENSATE("不能重做，要补偿"),
     *
     * @param id
     * @param context
     * @param bindingResult
     */
    @PostMapping("/audit/failure/{id:\\d+}")
    @ApiOperation(value = "用户点击审核猎刃任务不通过")
    public ResultApp auditFailure(@PathVariable("id") Long id, @Valid @RequestBody AuditContext context, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }

        //根据编号获取猎刃任务
        HunterTask hunterTask = hunterTaskService.findOne(context.getId());

        //判断查询的任务是否存在
        if (hunterTask == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //判断哪些猎刃任务状态允许被用户审核
        if (!HunterTaskState.isAuditToUser(hunterTask.getState())) {
            throw new ValidationException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        //获取对应的任务
        Task task = hunterTask.getTask();

        //获取判断需要的状态
        boolean isRework = task.getTaskRework();
        boolean isCompensate = task.getCompensate();
        HunterTaskState hunterTaskState = HunterTaskState.getBy(isRework, isCompensate);

        //设置状态，并添加不同意的原因
        boolean isSuccess = hunterTaskService.auditNotPassByUser(hunterTask.getId(), hunterTaskState, context.getContext());
        if (!isSuccess) {
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
        //返回结果
        return ResultApp.init("提交成功，当前用户"
                + (isRework ? "可重做" : "不可重做") + "任务，" +
                "放弃任务" + (isCompensate ? "不需要" : "需要") + "赔偿");
    }

    /**
     * HunterTask步骤5：用户点击不同意猎刃放弃按钮，此时回将猎刃任务状态设置为USER_REPULSE（用户拒绝猎刃放弃）
     *
     * @param id
     * @param context
     * @param bindingResult
     */
    @PostMapping("/abandon/failure/{id:\\d+}")
    @ApiOperation(value = "用户点击猎刃任务放弃申请不通过")
    public ResultApp abandonNotPass(@PathVariable("id") Long id, @Valid @RequestBody AuditContext context, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }
        //根据编号获取猎刃任务
        HunterTask hunterTask = hunterTaskService.findOne(context.getId());

        //判断查询的任务是否存在
        if (hunterTask == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //判断任务的状态
        if (!hunterTask.getState().equals(HunterTaskState.WITH_USER_NEGOTIATE)) {
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        //设置状态为用户拒绝放弃
        boolean isSuccess = hunterTaskService.auditNotPassByUser(hunterTask.getId(), HunterTaskState.USER_REPULSE, context.getContext());
        if (!isSuccess) {
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
        return ResultApp.init("已拒绝猎刃放弃任务申请");
    }

    /**
     * HunterTask步骤5：用户点击同意猎刃放弃按钮，此时回将猎刃任务状态设置为TASK_ABANDON（任务放弃）
     *
     * @param id
     * @param htId
     */
    @GetMapping("/abandon/success/{htId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "用户点击审核猎刃任务放弃通过")
    public ResultApp abandonPass(@PathVariable("id") Long id, @PathVariable("htId") String htId) {
        //根据编号获取猎刃任务
        HunterTask hunterTask = hunterTaskService.findOne(htId);

        //判断查询的任务是否存在
        if (hunterTask == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //判断任务的状态
        if (!hunterTask.getState().equals(HunterTaskState.WITH_USER_NEGOTIATE)) {
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        //设置状态为用户同意放弃
        boolean isSuccess = hunterTaskService.abandonPassByUser(hunterTask);
        if (!isSuccess) {
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
        return ResultApp.init("已同意猎刃放弃任务申请");
    }

    /**
     * 搜索任务
     *
     * @return
     */
    @GetMapping("/search/{key}/{page:\\d+}/{size:\\d+}/{id:\\d+}")
    @ApiOperation(value = "搜索任务")
    public AppPage search(@PathVariable("key") String key,
                          @PathVariable("page") int page,
                          @PathVariable("size") int size,
                          @PathVariable("id") Long id) {
//        QueryTask queryTask = new QueryTask(page, size);
//        queryTask.setSort(new Sort(Sort.Direction.DESC, Task.CREATE_TIME));
//        //        queryTask.setContext(key);
//        queryTask.setTaskName(key);
//        queryTask.setState(TaskState.ISSUE);
//        Page<Task> taskPage = taskService.findByQueryTask(queryTask);

        Pageable pageable = new PageRequest(page, size, new Sort(Sort.Direction.DESC, Task.CREATE_TIME));

        Page<Task> taskPage = taskService.search(key, pageable);

        List<Task> content = taskPage.getContent();
        List<TaskAppDto> taskAppDtos = new ArrayList<>();
        for (Task task : content) {
            taskAppDtos.add(TaskAppDto.toDetail(task));
        }

        return AppPage.init(taskAppDtos, taskPage);
    }

    /**
     * 根据状态获取指定用户的任务列表
     *
     * @param state 任务状态 查询全部用ALL,其余参考TaskState
     * @param id    用户编号
     * @return
     */
    @GetMapping("/user/{state}/{page:\\d+}/{size:\\d+}/{id:\\d+}")
    @ApiOperation(value = "根据状态获取指定用户的任务列表")
    public AppPage taskByUser(@PathVariable("state") String state,
                              @PathVariable("page") int page,
                              @PathVariable("size") int size,
                              @PathVariable("id") Long id) {
        QueryTask queryTask = new QueryTask(page, size);
        queryTask.setSort(new Sort(Sort.Direction.DESC, Task.CREATE_TIME));
        /*if (!hasState(queryTask.getState())) {
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }*/
        queryTask.setUserId(id);
       /* if (!state.equals("ALL")) {
            queryTask.setState(TaskState.valueOf(state));
        }*/
        ArrayList<TaskState> taskStates = new ArrayList<>();
        switch (state) {
            case "ALL"://全部
                //queryTask.setState(TaskState.valueOf(state)); 查询全部不需要设置
                taskStates.add(TaskState.NEW_CREATE);
                taskStates.add(TaskState.AWAIT_AUDIT);
                taskStates.add(TaskState.AUDIT);
                taskStates.add(TaskState.AUDIT_FAILURE);
                taskStates.add(TaskState.AUDIT_SUCCESS);
                taskStates.add(TaskState.OK_ISSUE);
                taskStates.add(TaskState.ISSUE);
                taskStates.add(TaskState.FORBID_RECEIVE);
                taskStates.add(TaskState.OUT);
                taskStates.add(TaskState.FINISH);
                taskStates.add(TaskState.ABANDON_COMMIT);
                taskStates.add(TaskState.ABANDON_OK);
                taskStates.add(TaskState.USER_HUNTER_NEGOTIATE);
                taskStates.add(TaskState.HUNTER_REJECT);
                taskStates.add(TaskState.COMMIT_AUDIT);
                taskStates.add(TaskState.ADMIN_NEGOTIATE);
                taskStates.add(TaskState.HUNTER_COMMIT);
                break;
            case "NEW"://新建
                taskStates.add(TaskState.NEW_CREATE);//新建
                taskStates.add(TaskState.AUDIT_FAILURE);//任务审核失败
                break;
            case "AUDIT"://待审核
                taskStates.add(TaskState.AWAIT_AUDIT);//等待审核
                taskStates.add(TaskState.AUDIT);//审核中
                taskStates.add(TaskState.AUDIT_SUCCESS);//任务审核成功
                taskStates.add(TaskState.OK_ISSUE);//任务可以发布

                break;
            case "ISSUE"://已发布
                taskStates.add(TaskState.ISSUE);//任务发布中
                taskStates.add(TaskState.FORBID_RECEIVE);//任务禁止被接取
                taskStates.add(TaskState.OUT);//任务被撤回
                break;
            case "FINISH"://已完成
                taskStates.add(TaskState.FINISH);//任务完成
                taskStates.add(TaskState.ABANDON_COMMIT);//用户提交放弃的申请
                taskStates.add(TaskState.ABANDON_OK);//任务被放弃
                taskStates.add(TaskState.USER_HUNTER_NEGOTIATE);//与猎刃协商中
                taskStates.add(TaskState.HUNTER_REJECT);//猎刃拒绝协商
                taskStates.add(TaskState.COMMIT_AUDIT);//提交管理员协商
                taskStates.add(TaskState.ADMIN_NEGOTIATE);//管理员协商中
                taskStates.add(TaskState.HUNTER_COMMIT);//猎刃放弃任务
                break;
        }
        queryTask.setStates(taskStates);

        Page<Task> taskPage = taskService.findByQueryTask(queryTask);
        List<Task> content = taskPage.getContent();
        List<TaskAppDto> taskAppDtos = new ArrayList<>();
        for (Task task : content) {
            taskAppDtos.add(TaskAppDto.toDetail(task));
        }

        return AppPage.init(taskAppDtos, taskPage);
    }

    /**
     * 查询分类下所有已发布任务
     *
     * @param classid 分类id
     * @return
     */
    @GetMapping("/issue/{classid:\\d+}/{page}/{size}")
    @ApiOperation(value = "查询分类下所有已发布任务")
    public AppPage queryByClassid(@PathVariable("classid") long classid,
                                  @PathVariable("page") int page,
                                  @PathVariable("size") int size) {
        QueryTask queryTask = new QueryTask(page, size);
        queryTask.setState(TaskState.ISSUE);
        queryTask.setClassifyId(classid);

        Page<Task> taskPage = taskService.findByQueryTask(queryTask);
        List<Task> content = taskPage.getContent();
        List<TaskAppDto> taskAppDtos = new ArrayList<>();
        for (Task task : content) {
            taskAppDtos.add(TaskAppDto.toDetail(task));
        }

        return AppPage.init(taskAppDtos, taskPage);
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
    @PostMapping("/issue/query")
    @ApiOperation(value = "根据排序条件获取已发布的任务")
    public AppPage taskByAll(@RequestBody QueryTask queryTask) {
        queryTask.setState(TaskState.ISSUE);
        Page<Task> taskPage = taskService.findByQueryTask(queryTask);
        List<Task> content = taskPage.getContent();
        List<TaskAppDto> taskAppDtos = new ArrayList<>();
        for (Task task : content) {
            taskAppDtos.add(TaskAppDto.toDetail(task));
        }

        return AppPage.init(taskAppDtos, taskPage);
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
    public TaskDetailAppDto detail(@PathVariable("id") String id) {
        //根据Id获取任务
        Task task = taskService.findOne(id);
        return TaskDetailAppDto.toDetail(task);
    }

    /**
     * 根据任务编号，获取本人发布的任务的猎刃执行者列表，通过该列表点击获取执行详情
     *
     * @param id
     * @param taskid
     * @return
     */
    @GetMapping("/hunterTask/{taskid:\\d+}/{page:\\d+}/{size:\\d+}/{id:\\d+}")
    @ApiOperation(value = "根据任务编号获取猎刃执行者列表")
    public AppPage hunterTaskByTaskId(@PathVariable("taskid") String taskid,
                                      @PathVariable("page") int page,
                                      @PathVariable("size") int size,
                                      @PathVariable("id") Long id) {
        //任务编号不允许为空
        if (taskid == null) {
            throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_FAILED);
        }
        QueryHunterTask queryHunterTask = new QueryHunterTask(page, size);
        queryHunterTask.setTaskId(taskid);

        //获取当前所有的猎刃信息
        Page<HunterTask> hunterTaskPage = hunterTaskService.findByQueryHunterTask(queryHunterTask);

        List<HunterTask> content = hunterTaskPage.getContent();
        List<HunterTaskAppDto> hunterTaskAppDtos = new ArrayList<>();
        for (HunterTask hunterTask : content) {
            hunterTaskAppDtos.add(HunterTaskAppDto.toDetail(hunterTask));
        }

        return AppPage.init(hunterTaskAppDtos, hunterTaskPage);

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
    public ResultApp modifyTask(@PathVariable("id") Long id, @Valid @RequestBody ModifyTask modifyTask, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Task sourceTask = taskService.findOne(modifyTask.getId());
        Task task = ModifyTask.toTask(sourceTask, modifyTask);
        task.setUserId(id);
        Task taskResult = taskService.modify(task);
        return ResultApp.init("修改成功");
    }

    /**
     * 删除我的任务
     *
     * @param id
     * @param taskId
     */
    @DeleteMapping("/remove/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "删除我的任务")
    public ResultApp delete(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {
        //根据任务编号获取任务
        Task task = taskService.findOne(taskId);

        //判断查询的任务是否存在
        if (task == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //判断任务的发布者与删除任务的用户是否一致
        if (!task.getUser().getId().equals(id)) {
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }

        //判断当前的任务状态是否允许被删除
        if (task.getState() != TaskState.NEW_CREATE) {
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        //删除任务，将任务状态修改为删除状态
        boolean isSuccess = taskService.updateState(taskId, TaskState.DELETE_OK);
        if (!isSuccess) {
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
        return ResultApp.init("删除成功");
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


}
