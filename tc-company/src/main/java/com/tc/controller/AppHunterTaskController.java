package com.tc.controller;

import com.tc.db.entity.HunterTask;
import com.tc.db.entity.Task;
import com.tc.db.enums.TaskState;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.HunterTaskService;
import com.tc.service.TaskService;
import com.tc.until.StringResourceCenter;
import com.tc.until.TimestampHelper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

}
