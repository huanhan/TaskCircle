package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.dto.audit.QueryAudit;
import com.tc.dto.audit.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 审核控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/audit")
public class AuditController {

    /**
     * 所有用户需要放弃的任务列表
     * @param queryUserAbandonTask 查询用户放弃的任务
     * @param result 检查校验结果
     * @return
     */
    @GetMapping("/user/abandon")
    @ApiOperation(value = "所有用户需要放弃的任务列表")
    public List<Task> allByUserAbandonTask(@Valid @RequestBody QueryUserAbandonTask queryUserAbandonTask,
                                           BindingResult result){
        return new ArrayList<>();
    }

    /**
     * 所有猎刃需要放弃的任务列表
     * @param queryHunterAbandonTask 查询猎刃放弃的任务
     * @param result 检查校验结果
     * @return
     */
    @GetMapping("/hunter/abandon")
    @ApiOperation(value = "所有猎刃需要放弃的任务列表")
    public List<HunterTask> allByHunterAbandonTask(@Valid @RequestBody QueryHunterAbandonTask queryHunterAbandonTask,
                                                   BindingResult result) {
        return new ArrayList<>();
    }

    /**
     * 所有用户需要发布的任务列表
     * @param queryUserIssueTask 查询用户需要发布的任务
     * @param result 检查校验结果
     * @return
     */
    @GetMapping("/user/issue")
    @ApiOperation(value = "所有用户需要发布的任务列表")
    public List<Task> allByUserIssueTask(@Valid @RequestBody QueryUserIssueTask queryUserIssueTask,
                                         BindingResult result){
        return new ArrayList<>();
    }

    /**
     * 用户申请成为猎刃列表
     * @param queryUserToHunter 查询用户申请猎刃条件
     * @param result 检查校验结果
     * @return
     */
    @GetMapping("/user/hunter")
    @ApiOperation(value = "用户申请成为猎刃列表")
    public List<User> allByUserToHunter(@Valid @RequestBody QueryUserToHunter queryUserToHunter,
                                        BindingResult result){
        return new ArrayList<>();
    }


    /**
     * 获取用户提现审核列表
     * @param queryUserWithdrawAudit 查询条件
     * @param result 检查校验结果
     * @return
     */
    @GetMapping("/user/withdraw")
    @ApiOperation(value = "用户提现审核列表")
    public List<UserWithdraw> allByUserWithdraw(@Valid @RequestBody QueryUserWithdrawAudit queryUserWithdrawAudit,
                                                BindingResult result){
        return new ArrayList<>();
    }

    /**
     * 获取审核任务的参照信息
     * @param id 任务编号
     * @return
     */
    @GetMapping("/task/refer/{id:\\d+}")
    @ApiOperation(value = "获取审核任务的参照信息")
    public TaskAuditRefer getTaskAuditRefer(@PathVariable("id") Long id){
        return new TaskAuditRefer();
    }

    /**
     * 添加任务审核结果
     * @param addTaskAudit 要添加的任务审核信息
     * @param bindingResult 检查校验结果
     * @return
     */
    @PostMapping("/task")
    @ApiOperation(value = "添加任务审核结果")
    public AuditTask addTaskAudit(@Valid @RequestBody AddTaskAudit addTaskAudit,BindingResult bindingResult){
        return new AuditTask();
    }

    /**
     * 添加猎刃申请审核结果
     * @param addHunterAudit 要添加的用户审核信息
     * @param bindingResult 检查校验结果
     * @return
     */
    @PostMapping("/user")
    @ApiOperation(value = "添加猎刃申请审核结果")
    public AuditHunter addHunterAudit(@Valid @RequestBody AddHunterAudit addHunterAudit,BindingResult bindingResult){
        return new AuditHunter();
    }

    /**
     * 添加提现审核结果
     * @param addWithdrawAudit 提现审核信息
     * @param bindingResult 检查校验结果
     * @return
     */
    @PostMapping("/withdraw")
    @ApiOperation(value = "添加提现审核结果")
    public AuditWithdraw addHunterAudit(@Valid @RequestBody AddWithdrawAudit addWithdrawAudit,BindingResult bindingResult){
        return new AuditWithdraw();
    }

    /**
     * 我的审核列表
     * @param authentication 当前登陆的用户
     * @param queryAudit 查询条件
     * @return
     */
    @GetMapping("/me")
    @ApiOperation(value = "我的审核列表")
    public List<Audit> auditByMe(Authentication authentication,
                                 @Valid @RequestBody QueryAudit queryAudit,
                                 BindingResult bindingResult){
        return new ArrayList<>();
    }

    /**
     * 查看审核详情信息
     * @param id 审核编号
     * @return
     */
    @GetMapping("/detail/{id:\\d+}")
    @ApiOperation(value = "查看审核详情信息")
    public Audit detail(@PathVariable("id") Long id){
        return new Audit();
    }
}
