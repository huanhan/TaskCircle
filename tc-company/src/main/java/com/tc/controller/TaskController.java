package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.db.entity.pk.HunterTaskStepPK;
import com.tc.db.entity.pk.TaskStepPK;
import com.tc.db.enums.TaskState;
import com.tc.dto.Result;
import com.tc.dto.StringIds;
import com.tc.dto.task.*;
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
    private HunterTaskService hunterTaskService;

    @Autowired
    private HunterTaskStepService hunterTaskStepService;

    @Autowired
    private TaskStepService taskStepService;

    @Autowired
    private TaskClassifyService taskClassifyService;

    @Autowired
    private TaskClassifyRelationService taskClassifyRelationService;

    @Autowired
    private UserHunterInterflowService userHunterInterflowService;


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
    @PostMapping("/step/{id:\\d+}")
    @ApiOperation(value = "获取任务的步骤列表")
    public List<TaskStep> getTaskSteps(@PathVariable("id") String id){
        List<TaskStep> queryTss = taskStepService.findByTaskId(id,new Sort(Sort.Direction.ASC,TaskStep.STEP));
        return TaskStep.toIndexByList(queryTss);
    }

    /**
     * 获取任务中单个步骤的详情
     * @param tid 任务编号
     * @param sid 任务步骤
     * @return
     */
    @GetMapping("/step/detail/{tid:\\d+}/{sid:\\d+}")
    @ApiOperation(value = "获取任务中单个步骤的详细情况")
    public TaskStep getTaskStepDetail(@PathVariable("tid") String tid, @PathVariable("sid") Integer sid){
        TaskStep taskStep = taskStepService.findOne(new TaskStepPK(tid,sid));
        if (taskStep == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        return TaskStep.toDetail(taskStep);
    }

    /**
     * 根据查询条件，获取接受本任务的猎刃任务列表
     * @param id 任务编号
     * @param queryHunterTask 查询条件
     * @return
     */
    @PostMapping("/hunter/{id:\\d+}")
    @ApiOperation(value = "根据查询条件，获取接受本任务的猎刃任务列表")
    public Result getTaskHunters(@PathVariable("id") String id,@RequestBody QueryHunterTask queryHunterTask){
        queryHunterTask.setTaskId(id);
        Page<HunterTask> query = hunterTaskService.findByQueryHunterTask(queryHunterTask);
        return Result.init(HunterTask.toIndexAsList(query.getContent()),queryHunterTask);
    }

    /**
     * 获取猎刃任务步骤列表
     * @param id
     * @return
     */
    @PostMapping("/hunter/step/{id:\\d+}")
    @ApiOperation(value = "获取猎刃任务的步骤列表")
    public List<HunterTaskStep> getHunterTaskSteps(@PathVariable("id") String id){
        List<HunterTaskStep> query = hunterTaskStepService.findByHunterTaskId(id,new Sort(Sort.Direction.ASC,HunterTaskStep.STEP));
        return HunterTaskStep.toListInIndex(query);
    }

    /**
     * 猎刃任务步骤的详情
     * @param tid
     * @param sid
     * @return
     */
    @GetMapping("/hunter/step/detail/{tid:\\d+}/{sid:\\d+}")
    @ApiOperation(value = "获取任务中单个步骤的详细情况")
    public HunterTaskStep getHunterTaskStep(@PathVariable("tid") String tid, @PathVariable("sid") Integer sid){
        HunterTaskStep result = hunterTaskStepService.findOne(new HunterTaskStepPK(tid,sid));
        if (result == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        return HunterTaskStep.toDetail(result);
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
            boolean isUpdateSuccess = taskService.adminUpdateState(id,state);
            if (!isUpdateSuccess){
                throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
            }
        }else {
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
    }

    /**
     * 获取指定任务中用户与猎刃的聊天记录
     * @param id 猎刃接任务的单号
     * @param queryTaskInterflow 查询条件
     * @return
     */
    @PostMapping("/interflow/{id:\\d+}")
    @ApiOperation(value = "获取指定任务中用户与猎刃的聊天记录")
    public Result getUserHunterInterflows(@PathVariable("id") Long id,
                                          @RequestBody QueryTaskInterflow queryTaskInterflow){
        queryTaskInterflow.setUserId(id);
        Page<UserHunterInterflow> query = userHunterInterflowService.findByQueryAndGroup(queryTaskInterflow);
        return Result.init(UserHunterInterflow.toListInIndex(query.getContent()),queryTaskInterflow);
    }

    /**
     * 获取指定任务，指定用户，指定猎刃的聊天内容
     * @param queryTaskInterflow
     * @param bindingResult
     * @return
     */
    @PostMapping("/interflow/detail")
    @ApiOperation(value = "获取指定任务，指定用户，指定猎刃的聊天内容")
    public Result getInterflow(@RequestBody QueryTaskInterflow queryTaskInterflow,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Page<UserHunterInterflow> query = userHunterInterflowService.findByQuery(queryTaskInterflow);
        return Result.init(UserHunterInterflow.toListInIndex(query.getContent()),queryTaskInterflow);
    }

    private boolean hasUpdate(TaskState old, TaskState news) {
        switch (old) {
            case NEW_CREATE:
                return false;
            case AWAIT_AUDIT:
                break;
            case AUDIT:
                break;
            case AUDIT_FAILURE:
                break;
            case AUDIT_SUCCESS:
                break;
            case OK_ISSUE:
                break;
            case ISSUE:
                break;

            case FORBID_RECEIVE:
                break;

            case ABANDON_OK:
                break;

            case HUNTER_REJECT:
                break;
            case ADMIN_NEGOTIATE:
                break;

            default:
                break;
        }
        return true;
    }




}
