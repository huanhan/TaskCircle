package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.db.enums.TaskState;
import com.tc.dto.Result;
import com.tc.dto.StringIds;
import com.tc.dto.comment.QueryTaskComment;
import com.tc.dto.task.*;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.TaskClassifyRelationService;
import com.tc.service.TaskClassifyService;
import com.tc.service.TaskService;
import com.tc.service.TaskStepService;
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
import java.util.List;

/**
 * 任务控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskStepService taskStepService;

    @Autowired
    private TaskClassifyService taskClassifyService;

    @Autowired
    private TaskClassifyRelationService taskClassifyRelationService;

    @PostMapping()
    @ApiOperation("添加任务")
    public Task add(@Valid @RequestBody AddTask addTask,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Task task = taskService.save(AddTask.toTask(addTask));
        return Task.toDetail(task);
    }

    @PostMapping("/step")
    @ApiOperation("添加任务步骤")
    public TaskStep add(@Valid @RequestBody AddTaskStep addTaskStep,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        TaskStep taskStep = taskStepService.save(AddTaskStep.toTaskStep(addTaskStep));
        return TaskStep.toDetail(taskStep);
    }

    /**
     * 根据查询条件获取任务列表
     * @param queryTask 查询条件
     * @return
     */
    @PostMapping("/query")
    @ApiOperation(value = "获取任务列表")
    public Result all(@RequestBody QueryTask queryTask){
        Page<Task> queryTasks = taskService.findByQueryTask(queryTask);
        List<Task> result = Task.toIndexAsList(queryTasks.getContent());
        return Result.init(result,queryTask);
    }

    /**
     * 根据编号获取任务详情
     * @param id 任务编号
     * @return
     */
    @GetMapping("/detail/{id:\\d+}")
    @ApiOperation(value = "获取任务详情")
    public Task detail(@PathVariable("id") String id){
        Task queryTask = taskService.findOne(id);
        return Task.toDetail(queryTask);
    }

    /**
     * 根据任务来获取该任务所属的分类列表信息
     * @param id 任务编号
     * @return
     */
    @GetMapping("/tcl/{id:\\d+}")
    @ApiOperation(value = "根据任务来获取该任务所属的分类列表信息")
    public Result getTaskClassifyResultByTask(@PathVariable("id") String id){
        Page<TaskClassify> result = taskClassifyService.queryByQueryTaskClassify(QueryTaskClassify.init(id));
        return Result.init(TaskClassify.reset(result.getContent()));
    }

    /**
     * 从任务中移除任务拥有的分类
     * @param ids 分类列表(包括任务编号)
     * @param bindingResult 检查异常结果
     */
    @PostMapping("/delete/tcl")
    @ApiOperation(value = "从任务中移除任务拥有的分类")
    public void removeTaskClassify(@Valid @RequestBody StringIds ids, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        boolean delIsSuccess = taskClassifyRelationService.deleteByIds(ids);
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);}
    }

    /**
     * 获取任务的步骤列表
     * @param id 任务编号
     * @return
     */
    @GetMapping("/step/{id:\\d+}")
    @ApiOperation(value = "获取任务的步骤列表")
    public List<TaskStep> getTaskSteps(@PathVariable("id") String id){
        List<TaskStep> queryTss = taskStepService.findByTaskId(id,new Sort(Sort.Direction.ASC,TaskStep.STEP));
        return TaskStep.toIndexByList(queryTss);
    }

    /**
     * 获取任务中单个步骤的执行情况
     * @param tid 任务编号
     * @param sid 任务步骤
     * @return
     */
    @GetMapping("/step/detail/{tid:\\d+}/{sid:\\d+}")
    @ApiOperation(value = "获取任务中单个步骤的执行情况")
    public TaskStepDetail getTaskStepDetail(@PathVariable("tid") Long tid, @PathVariable("sid") Long sid){
        return new TaskStepDetail();
    }

    /**
     * 管理员设置任务状态
     * @param id 任务编号
     * @param state 新状态
     */
    @PutMapping("/state/{id}")
    @ApiOperation(value = "管理员设置任务状态")
    public void updateTaskState(@PathVariable("id") String id,@RequestBody TaskState state){
        if (state == null){
            throw new ValidException(StringResourceCenter.CONTEXT_NOT_NULL);
        }
        Task task = taskService.findOne(id);
        if (task.getState().equals(state)){
            throw new ValidException(StringResourceCenter.VALIDATOR_ADD_TITLE_FAILED);
        }
        boolean has = hasUpdate(task.getState(),state);
        if (has) {
            boolean isUpdateSuccess = taskService.updateState(id,state);
            if (!isUpdateSuccess){
                throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
            }
        }else {
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
    }

    private boolean hasUpdate(TaskState old, TaskState news) {
        switch (old) {
            case NEW_CREATE:
                return false;
            case AWAIT_AUDIT:
                break;
            case AUDIT:
                break;
            case AUDIT_FAILUER:
                break;
            case AUDIT_SUCCESS:
                break;
            case OK_ISSUE:
                break;
            case ISSUE:
                break;
            case RECEIVE:
                break;
            case FORBID_RECEIVE:
                break;
            case FINISH:
                break;
            case DELETE_OK:
                break;
            case DELETE:
                break;
            case ABANDON_OK:
                break;
            case ABANDON:
                break;
            case USER_HUNTER_NEGOTIATE:
                break;
            case HUNTER_REJECT:
                break;
            case ADMIN_NEGOTIATE:
                break;
            case USER_COMPENSATION:
                break;
            case DEPOSIT:
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 获取指定任务与猎刃的聊天记录
     * @param id 猎刃接任务的单号
     * @param queryTaskInterflow 查询条件
     * @param result 检查异常结果
     * @return
     */
    @GetMapping("/interflow/{id:\\d+}")
    @ApiOperation(value = "获取指定任务与猎刃的聊天记录")
    public List<UserHunterInterflow> getUserHunterInterflows(@PathVariable("id") Long id,
                                                             @Valid @RequestBody QueryTaskInterflow queryTaskInterflow,
                                                             BindingResult result){
        return new ArrayList<>();
    }

    /**
     * 根据查询条件，获取接受本任务的猎刃列表
     * @param id 任务编号
     * @param queryTaskHunter 查询条件
     * @param result 检查异常结果
     * @return
     */
    @GetMapping("/hunter/{id:\\d+}")
    @ApiOperation(value = "根据查询条件，获取接受本任务的猎刃列表")
    public List<HunterTask> getTaskHunters(@PathVariable("id") Long id,
                                           @Valid @RequestBody QueryTaskHunter queryTaskHunter,
                                           BindingResult result){
        return new ArrayList<>();
    }

    /**
     * 获取接受了某个任务的某个猎刃的任务具体执行情况步骤
     * @param id 猎刃接受任务的任务单号
     * @return
     */
    @GetMapping("/hts/{id:\\d+}")
    @ApiOperation(value = "获取接受了某个任务的某个猎刃的任务具体执行情况步骤")
    public List<HunterTaskStep> getHunterTaskSteps(@PathVariable("id") Long id){
        return new ArrayList<>();
    }
}
