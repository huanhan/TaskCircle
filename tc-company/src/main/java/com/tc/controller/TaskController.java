package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.db.entity.pk.TaskStepPK;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.TaskState;
import com.tc.db.enums.TaskType;
import com.tc.dto.Result;
import com.tc.dto.comment.QueryTaskComment;
import com.tc.dto.task.QueryHunterTask;
import com.tc.dto.task.QueryTask;
import com.tc.dto.task.QueryTaskClassify;
import com.tc.dto.task.QueryTaskInterflow;
import com.tc.dto.task.classify.Remove;
import com.tc.dto.task.step.ResultTStep;
import com.tc.dto.task.step.TransHT;
import com.tc.dto.trans.TransData;
import com.tc.dto.trans.TransEnum;
import com.tc.dto.trans.TransTaskQuery;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.*;
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
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 任务控制器
 *
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
    private TaskStepService taskStepService;

    @Autowired
    private TaskClassifyService taskClassifyService;

    @Autowired
    private TaskClassifyRelationService taskClassifyRelationService;

    @Autowired
    private UserHunterInterflowService userHunterInterflowService;

    @Autowired
    private AuditTaskService auditTaskService;

    @Autowired
    private CommentTaskService commentTaskService;

    /**
     * @return
     */
    @GetMapping("/query/setting")
    @ApiOperation(value = "查询任务时用到的查询条件")
    public Result querySetting(@RequestParam(name = "classify", required = false, defaultValue = "0") Integer isClassify) {
        List<TransEnum> transStates = TaskState.toList();
        List<TransEnum> transTypes = TaskType.toList();
        List<TaskClassify> resultTaksClassidy = new ArrayList<>();
        if (isClassify <= 0) {
            List<TaskClassify> parents = taskClassifyService.findByParents();
            resultTaksClassidy = TaskClassify.toListInIndex(parents);
        }
        TransTaskQuery result = new TransTaskQuery(transStates, transTypes, resultTaksClassidy);
        return Result.init(result);
    }

    /**
     * 根据查询条件获取任务列表
     *
     * @param queryTask 查询条件
     * @return
     */
    @PostMapping("/query")
    @ApiOperation(value = "获取任务列表")
    public Result all(@RequestBody QueryTask queryTask) {
        Page<Task> queryTasks = taskService.findByQueryTask(queryTask);
        List<Task> result = Task.toIndexAsList(queryTasks.getContent());
        return Result.init(result, queryTask.append(queryTasks.getTotalElements(),(long)queryTasks.getTotalPages()));
    }

    /**
     * 根据编号获取任务详情
     *
     * @param id 任务编号
     * @return
     */
    @GetMapping("/detail/{id:\\d+}")
    @ApiOperation(value = "获取任务详情")
    public Task detail(@PathVariable("id") String id) {
        Task queryTask = taskService.findOne(id);
        return Task.toDetail(queryTask);
    }

    /**
     * 根据任务来获取该任务所属的分类列表信息
     *
     * @param id 任务编号
     * @return
     */
    @GetMapping("/tcl/{id:\\d+}")
    @ApiOperation(value = "根据任务来获取该任务所属的分类列表信息")
    public Result getTaskClassifyResultByTask(@PathVariable("id") String id) {
        List<TaskClassify> result = taskClassifyService.queryByQueryAndNotPage(QueryTaskClassify.init(id));
        return Result.init(TaskClassify.toListInIndex(result));
    }

    /**
     *
     * @param remove
     * @param bindingResult
     */
    @PostMapping("/add/tcl")
    @ApiOperation(value = "为任务添加分类")
    public Result addClassify(@Valid @RequestBody Remove remove, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }
        List<TaskClassifyRelation> ops = taskClassifyRelationService.findByTask(remove.getId());
        if (ListUtils.isNotEmpty(ops)){
            remove.getIds().removeIf(aLong -> {
               AtomicBoolean isRemove = new AtomicBoolean(false);
               ops.forEach(taskClassifyRelation -> {
                   if (taskClassifyRelation.getTaskClassifyId().equals(aLong)){
                       isRemove.set(true);
                   }
               });
               return isRemove.get();
            });

            if (ListUtils.isEmpty(remove.getIds())){
                throw new DBException("添加的新分类在任务中中存在");
            }
        }
        boolean addIsSuccess = taskClassifyRelationService.addBy(remove);
        if (!addIsSuccess) {
            throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);
        }else {
            List<TaskClassify> result = taskClassifyService.queryByQueryAndNotPage(QueryTaskClassify.init(remove.getId()));
            return Result.init(TaskClassify.toListInIndex(result));
        }
    }


    /**
     * 从任务中移除任务拥有的分类
     *
     * @param remove        分类列表(包括任务编号)
     * @param bindingResult 检查异常结果
     */
    @PostMapping("/delete/tcl")
    @ApiOperation(value = "从任务中移除任务拥有的分类")
    public void removeTaskClassify(@Valid @RequestBody Remove remove, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }
        boolean delIsSuccess = taskClassifyRelationService.deleteBy(remove);
        if (!delIsSuccess) {
            throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);
        }
    }

    /**
     * 获取任务的步骤列表
     *
     * @param id 任务编号
     * @return
     */
    @GetMapping("/step/{id:\\d+}")
    @ApiOperation(value = "获取任务的步骤列表")
    public ResultTStep getTaskSteps(@PathVariable("id") String id) {
        List<TaskStep> queryTss = taskStepService.findByTaskId(id, new Sort(Sort.Direction.ASC, TaskStep.STEP));
        List<HunterTask> query = hunterTaskService.findBy(id,HunterTaskState.EXECUTE);

        List<TaskStep> resultTss = TaskStep.toIndexByList(queryTss);
        List<TransHT> resultHT = HunterTask.toHTStepAsList(query,resultTss.size());

        return new ResultTStep(resultTss,resultHT);
    }

    /**
     * 获取任务中单个步骤的详情
     *
     * @param tid 任务编号
     * @param sid 任务步骤
     * @return
     */
    @GetMapping("/step/detail/{tid:\\d+}/{sid:\\d+}")
    @ApiOperation(value = "获取任务中单个步骤的详细情况")
    public TaskStep getTaskStepDetail(@PathVariable("tid") String tid, @PathVariable("sid") Integer sid) {
        TaskStep taskStep = taskStepService.findOne(new TaskStepPK(tid, sid));
        if (taskStep == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        return TaskStep.toDetail(taskStep);
    }

    /**
     * 根据查询条件，获取接受本任务的猎刃任务列表
     *
     * @param id              任务编号
     * @param queryHunterTask 查询条件
     * @return
     */
    @PostMapping("/hunter/{id:\\d+}")
    @ApiOperation(value = "根据查询条件，获取接受本任务的猎刃任务列表")
    public Result getTaskHunters(@PathVariable("id") String id, @RequestBody QueryHunterTask queryHunterTask) {

        Task task = taskService.findOne(id);
        if (task == null){
            throw new DBException("任务：" + StringResourceCenter.DB_QUERY_FAILED);
        }

        queryHunterTask.setTaskId(id);
        Page<HunterTask> query = hunterTaskService.findByQueryHunterTask(queryHunterTask);
        TransData trans = new TransData(task.getId(),task.getId(),HunterTask.toIndexAsList(query.getContent()));
        return Result.init(trans, queryHunterTask.append(query.getTotalElements(),(long)query.getTotalPages()));
    }


    /**
     * 获取指定任务中用户与猎刃的聊天记录
     *
     * @param id                 任务编号
     * @param queryTaskInterflow 查询条件
     * @return
     */
    @PostMapping("/interflow/{id:\\d+}")
    @ApiOperation(value = "获取指定任务中用户与猎刃的聊天记录")
    public Result getUserHunterInterflows(@PathVariable("id") String id,
                                          @RequestBody QueryTaskInterflow queryTaskInterflow) {
        Task task = taskService.findOne(id);
        if (task == null) {
            throw new DBException("任务：" + StringResourceCenter.DB_QUERY_FAILED);
        }
        queryTaskInterflow.setTaskId(id);
        Page<UserHunterInterflow> query = userHunterInterflowService.findByQueryAndGroup(queryTaskInterflow);
        return Result.init(UserHunterInterflow.toListInIndex(query.getContent()), queryTaskInterflow);
    }

    /**
     * 获取任务评论信息
     * @param queryTaskComment 任务评论查询条件
     * @return
     */
    @PostMapping("/comments/{id:\\d+}")
    @ApiOperation(value = "根据任务编号获取任务的评论列表")
    public Result getTaskComment(@PathVariable("id") String id,@RequestBody QueryTaskComment queryTaskComment){
        Task task = taskService.findOne(id);
        if (task == null) {
            throw new DBException("任务：" + StringResourceCenter.DB_QUERY_FAILED);
        }
        queryTaskComment.setTaskId(id);
        Page<CommentTask> query = commentTaskService.findByQuery(queryTaskComment);
        return Result.init(CommentTask.toListInIndex(query.getContent()),queryTaskComment.append(query.getTotalElements(),(long)query.getTotalPages()));
    }


    @GetMapping("/audit/{id:\\d+}")
    @ApiOperation(value = "根据任务编号，获取任务的审核记录")
    public List<AuditTask> getAuditByHunterTask(@PathVariable("id") String id){
        Task task = taskService.findOne(id);
        if (task == null){
            throw new DBException("任务：" + StringResourceCenter.DB_QUERY_FAILED);
        }
        List<AuditTask> query = auditTaskService.findByTaskId(id);
        return AuditTask.toIndexAsList(query);
    }
}
