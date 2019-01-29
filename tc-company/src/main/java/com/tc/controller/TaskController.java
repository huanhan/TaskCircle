package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.db.entity.pk.HtsRecordPK;
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
import java.sql.Timestamp;
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

    @Autowired
    private HtsRecordService htsRecordService;

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


    @PostMapping("/hts/record/query")
    @ApiOperation(value = "根据查询条件获取猎刃任务步骤变更情况列表")
    public Result getHtsRecords(@RequestBody QueryHtsRecords queryHtsRecords){
        Page<HtsRecord> records = htsRecordService.findByQuery(queryHtsRecords);
        return Result.init(HtsRecord.toListInIndex(records.getContent()),queryHtsRecords);
    }


    /**
     * 获取猎刃任务步骤变更情况
     * @param tid
     * @param sid
     * @return
     */
    @GetMapping("/hts/record/{tid:\\d+}/{sid:\\d+}")
    @ApiOperation(value = "获取猎刃任务步骤变更情况列表")
    public Result getHtsRecords(@PathVariable("tid") String tid, @PathVariable("sid") Integer sid){
        List<HtsRecord> records = htsRecordService.findAll(tid,sid);
        return Result.init(HtsRecord.toListInIndex(records));
    }

    /**
     * 获取猎刃任务步骤变更情况详情
     * @param tid
     * @param sid
     * @param timestamp
     * @return
     */
    @GetMapping("/hts/record/{tid:\\d+}/{sid:\\d+}/{t}")
    @ApiOperation(value = "获取猎刃任务步骤变更情况详情")
    public HtsRecordDto getHtsRecord(@PathVariable("tid") String tid, @PathVariable("sid") Integer sid, @PathVariable("t")Timestamp timestamp){
        HtsRecord record = htsRecordService.findOne(new HtsRecordPK(tid,sid,timestamp));
        return HtsRecordDto.by(record);
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





}
