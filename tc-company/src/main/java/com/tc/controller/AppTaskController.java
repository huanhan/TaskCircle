package com.tc.controller;

import com.tc.db.entity.HunterTask;
import com.tc.db.entity.Task;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.TaskState;
import com.tc.dto.Result;
import com.tc.dto.task.*;
import com.tc.exception.ValidException;
import com.tc.service.TaskService;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * APP用户控制器
 * 所有的用户编号都是用来校验用户，
 * 有用户编号的地方，说明该接口需要用户授权访问
 * @author Cyg
 *
 *
 *
 *
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/app/task")
public class AppTaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 根据状态获取指定用户的任务列表
     * @param id 用户编号
     * @param queryTask 状态不能为空
     * @return
     */
    @PostMapping("/user/{id:\\d+}")
    @ApiOperation(value = "根据状态获取指定用户的任务列表")
    public Result taskByUser(@PathVariable("id") Long id, @RequestBody QueryTask queryTask){
        if (!hasState(queryTask.getState())){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }

        return Result.init(new ArrayList<Task>(),queryTask);
    }

    /**
     * 根据状态获取指定猎刃的任务列表
     * @param id 用户编号
     * @param queryHunterTask 状态不能为空
     * @return
     */
    @PostMapping("/hunter/{id:\\d+}")
    @ApiOperation(value = "根据状态获取指定猎刃的任务列表")
    public Result taskByHunter(@PathVariable("id") Long id, @RequestBody QueryHunterTask queryHunterTask){
        if (!hasState(queryHunterTask.getState())){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        return Result.init(new ArrayList<HunterTask>(),queryHunterTask);
    }

    /**
     * 获取所有以发布的任务，排序条件由外部传入，QueryTask支持
     * 格式
     * sort：[{
     *      "direction": "ASC",
     *      "property": "auditTime",
     *      "ignoreCase": false,
     *      "nullHandling": "NATIVE",
     *      "ascending": true,
     *      "descending": false
     * }
     * ]
     * @param queryTask
     * @return
     */
    @PostMapping("/query")
    @ApiOperation(value = "根据排序条件获取已发布的任务")
    public Result taskByAll(@RequestBody QueryTask queryTask){
        queryTask.setState(TaskState.ISSUE);
        return Result.init(new ArrayList<Task>(),queryTask);
    }

    /**
     * 获取任务详情信息,该任务详情是任何人都可以看的任务详情
     *
     * 如果需要在任务详情信息中显示任务步骤等，
     * 请在Service中进行查询拼装
     *
     * @param id
     * @return
     */
    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "获取任务详情信息")
    public Task look(@PathVariable("id") String id){
        //根据Id获取任务

        //判断状态是否为已发布

        //不为以发布
        if (1 == 1){
            throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_FAILED);
        }

        return new Task();
    }

    /**
     * 根据任务编号，获取本人发布的任务的猎刃执行者列表，通过该列表点击获取执行详情
     * @param id
     * @param queryHunterTask
     * @return
     */
    @PostMapping("/hunterTask/{id:\\d+}")
    @ApiOperation(value = "获取我发布的任务的猎刃执行情况列表")
    public Result hunterTaskByMe(@PathVariable("id") Long id,@RequestBody QueryHunterTask queryHunterTask){
        //任务编号不允许为空
        if (queryHunterTask.getTaskId() == null){
            throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_FAILED);
        }
        return Result.init(new ArrayList<HunterTask>(),queryHunterTask);
    }

    /**
     * 普通用户将任务提交审核
     *
     * 需要审核的任务有（用户新建任务，用户放弃的任务）
     *
     * @param id
     * @param taskId
     */
    @GetMapping("/user/upAudit/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "将用户的任务提交审核")
    public void upAuditByUser(@PathVariable("id") Long id,@PathVariable("taskId") String taskId){
        //根据任务编号获取任务

        //判断任务的发布者与提交任务审核的用户是否一致

        //判断任务的状态是新建任务还是，用户放弃任务

        //修改任务状态为新建任务 -> AWAIT_AUDIT,用户放弃任务 -> COMMIT_AUDIT
    }

    /**
     * 猎刃将任务提交审核
     *
     * 需要审核的任务有（猎刃放弃任务，猎刃完成的任务被用户拒绝）
     *
     * @param id
     * @param taskId
     */
    @GetMapping("/hunter/upAudit/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "将用户的任务提交审核")
    public void upAuditByHunter(@PathVariable("id") Long id,@PathVariable("taskId") String taskId){
        //根据猎刃任务编号获取任务

        //判断任务的接收者与提交任务审核的用户是否一致

        //判断任务的状态是猎刃放弃任务，还是猎刃完成任务需要审核

        //修改任务状态为猎刃放弃任务COMMIT_TO_ADMIN，完成任务需要审核COMMIT_ADMIN_ADUIT
    }

    /**
     * 猎刃将完成的任务提交用户
     * @param id
     * @param taskId
     */
    @GetMapping("/hunter/toUser/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "将完成的任务交给用户查看")
    public void upAuditByHunterToUser(@PathVariable("id") Long id,@PathVariable("taskId") String taskId){
        //根据编号获取猎刃任务详情（包括执行步骤）
        //进行简要的系统判断（比如步骤是否都有完成）
        //修改猎刃任务状态为任务完成，后面用户需要查看猎刃完成的任务情况，根据情况点击确认完成
    }

    /**
     * 添加任务已实现
     * @param id 用户编号
     * @param addTask
     * @param bindingResult
     * @return
     */
    @PostMapping("/add/{id:\\d+}")
    @ApiOperation("添加我的新任务")
    public Task add(@PathVariable("id") Long id, @Valid @RequestBody AddTask addTask, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Task task = taskService.save(AddTask.toTask(addTask));
        return Task.toDetail(task);
    }


    /**
     * 修改任务信息
     * @param id 用户编号
     * @param modifyTask
     * @param bindingResult
     * @return
     */
    @PostMapping("/modify/{id:\\d+}")
    @ApiOperation("修改我的任务")
    public Task update(@PathVariable("id") Long id, @Valid @RequestBody ModifyTask modifyTask, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        return new Task();
    }

    /**
     * 删除我的任务
     * @param id
     * @param taskId
     */
    @DeleteMapping("/remove/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "删除我的任务")
    public void delete(@PathVariable("id") Long id,@PathVariable("taskId") String taskId){
        //根据任务编号获取任务详情

        //判断任务发布者与用户编号是否相同

        //判断当前的任务状态是否允许被删除

        //删除任务，将任务状态修改为删除状态
    }

    /**
     * 猎刃接任务
     * @param id
     * @param taskId
     */
    @GetMapping("/accept/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "猎刃接任务")
    public void acceptTask(@PathVariable("id") Long id, @PathVariable("taskId") String taskId){
        //获取用户详情信息

        //判断该用户是否可以接任务

        //在service中添加猎刃任务信息，需要做如下判断：任务是否允许被接，接完后是否需要修改任务状态，接完任务猎刃需要缴纳的押金
    }

    /**
     * 任务执行步骤情况
     * @param id
     * @param addHTS
     * @param bindingResult
     */
    @PostMapping("/hts/{id:\\d+}")
    @ApiOperation(value = "添加猎刃任务执行步骤情况")
    public void addHunterTaskStep(@PathVariable("id") Long id, @Valid @RequestBody AddHTS addHTS, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
    }


    /**
     * 用户发布任务
     *
     * task需要替换成DTO
     *
     * @param id 用户编号
     * @param task 任务信息
     */
    @GetMapping("/issue/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "发布我的任务")
    public void issueTask(@PathVariable("id") Long id,@Valid @RequestBody Task task,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }


        //获取任务信息

        //判断任务状态是否允许发布

        //修改任务状态为发布状态，并且在修改完后，从用户账户中扣除发布需要的押金
    }

    /**
     * 用户撤回
     * @param id 用户编号
     * @param taskId 任务编号
     */
    @GetMapping("/out/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "撤回我的任务")
    public void outTask(@PathVariable("id") Long id,@PathVariable("taskId") String taskId){
        //获取任务信息

        //判断任务状态是否可被测回

        //如果不可撤回，发送异常报告，通知用户当前任务已被接取，并询问用户是否走猎刃协商流程

        //如果可以撤回，则修改任务状态，并将押金退回用户账户
    }

    /**
     * 用户或者猎刃点击放弃任务
     * @param id
     * @param taskId
     */
    @GetMapping("/abandon/{taskId:\\d+}/{id:\\d+}")
    @ApiOperation(value = "用户或者猎刃点击放弃任务")
    public void abandonTask(@PathVariable("id") Long id, @PathVariable("taskId") String taskId){
        //根据Id判断放弃任务的是用户还是猎刃

        //根据taskId获取任务详情

        //判断该任务的放弃条件

        //如果满足直接放弃的条件，则在这里直接放弃，并退回用户押金或者猎刃押金

        //如果不满足，则抛除异常并交由用户自己选择
    }



    /**
     * 判断传入的猎刃任务状态是否允许访问
     * 允许访问返回True，不允许访问返回False
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
