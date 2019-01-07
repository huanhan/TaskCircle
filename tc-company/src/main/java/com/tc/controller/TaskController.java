package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.db.enums.TaskState;
import com.tc.dto.LongIds;
import com.tc.dto.task.*;
import io.swagger.annotations.ApiOperation;
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

    /**
     * 根据查询条件获取任务列表
     * @param queryTask 查询条件
     * @param bindingResult 校验异常结果
     * @return
     */
    @GetMapping("/all")
    @ApiOperation(value = "获取任务列表")
    public List<Task> all(@Valid @RequestBody QueryTask queryTask, BindingResult bindingResult){
        return new ArrayList<>();
    }

    /**
     * 根据编号获取任务详情
     * @param id 任务编号
     * @return
     */
    @GetMapping("/detail/{id:\\d+}")
    @ApiOperation(value = "获取任务详情")
    public Task detail(@PathVariable("id") Long id){
        return new Task();
    }

    /**
     * 根据任务来获取该任务所属的分类列表信息
     * @param id 任务编号
     * @return
     */
    @GetMapping("/tcl/{id:\\d+}")
    @ApiOperation(value = "根据任务来获取该任务所属的分类列表信息")
    public List<TaskClassifyResult> getTaskClassifyResultByTask(@PathVariable("id") Long id){
        return new ArrayList<>();
    }

    /**
     * 从任务中移除任务拥有的分类
     * @param id 任务编号
     * @param ids 分类列表
     * @param bindingResult 检查异常结果
     */
    @DeleteMapping("/tcl/{id:\\d+}")
    @ApiOperation(value = "从任务中移除任务拥有的分类")
    public void removeTaskClassify(@PathVariable("id") Long id, @Valid @RequestBody LongIds ids, BindingResult bindingResult){

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

    /**
     * 根据查询条件，获取本任务的评论情况
     * @param id 任务编号
     * @param queryTaskComment 评论查询条件
     * @param result 检查异常结果
     * @return
     */
    @GetMapping("/comment/{id:\\d+}")
    @ApiOperation(value = "根据查询条件，获取本任务的评论情况")
    public List<CommentTask> getCommentTasks(@PathVariable("id") Long id,
                                             @Valid @RequestBody QueryTaskComment queryTaskComment,
                                             BindingResult result){
        return new ArrayList<>();
    }

    /**
     * 获取本任务的步骤列表
     * @param id 任务编号
     * @return
     */
    @GetMapping("/step/{id:\\d+}")
    @ApiOperation(value = "获取本任务的步骤列表")
    public List<TaskStep> getTaskSteps(@PathVariable("id") Long id){
        return new ArrayList<>();
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
    @PutMapping("/state/{id:\\d+}")
    @ApiOperation(value = "管理员设置任务状态")
    public void updateTaskState(@PathVariable("id") Long id,@RequestBody TaskState state){

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


}
