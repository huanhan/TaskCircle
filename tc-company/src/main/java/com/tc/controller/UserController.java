package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.dto.Result;
import com.tc.dto.user.*;
import com.tc.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据查询条件获取用户列表
     * @param queryUser 用户查询条件
     * @return
     */
    @PostMapping
    @ApiOperation(value = "根据查询条件获取用户列表")
    public Result all(@RequestBody QueryUser queryUser){

        Page<User> queryUsers = userService.findByQueryUser(queryUser);
        List<User> result = User.toIndexAsList(queryUsers.getContent());
        return Result.init(result,queryUser);
    }

    /**
     * 获取用户信息详情
     * @param id
     * @return
     */
    @GetMapping("/{id:\\+d}")
    @ApiOperation(value = "获取用户详情信息")
    public User detail(@PathVariable("id") Long id){

        User user = userService.findOne(id);
        return User.toDetail(user);

    }

    /**
     * 根据用户编号获取用户的任务统计信息
     * @param id 用户编号
     * @return
     */
    @GetMapping("/statistics/{id:\\d+}/out/task")
    @ApiOperation(value = "根据用户编号获取用户任务统计信息")
    public UserTaskStatistics getTaskStatisticsByUser(@PathVariable("id") Long id){
        return new UserTaskStatistics();
    }

    /**
     * 根据用户编号获取用户发布的任务列表
     * @param id 用户编号
     * @param queryUserTask 用户任务查询信息
     * @param result 异常结果
     * @return
     */
    @GetMapping("/task/out/{id:\\d+}")
    @ApiOperation(value = "根据用户编号获取用户发布的任务列表")
    public List<Task> getUserOutTask(@PathVariable("id") Long id,
                                     @Valid @RequestBody QueryUserTask queryUserTask, BindingResult result){
        return new ArrayList<>();
    }

    /**
     * 根据用户编号获取用户评论信息
     * @param id 用户编号
     * @param queryUserComment 用户评论查询条件
     * @param result 异常结果
     * @return
     */
    @GetMapping("/comment/{id:\\d+}")
    @ApiOperation(value = "根据用户编号获取用户的评论列表")
    public List<CommentUser> getUserComment(@PathVariable("id") Long id,
                                            @Valid @RequestBody QueryUserComment queryUserComment, BindingResult result){
        return new ArrayList<>();
    }


    /**
     * 根据用户编号获取用户的审核历史记录
     * @param id
     * @param queryUserAuditRecord
     * @param result
     * @return
     */
    @GetMapping("/audit/{id:\\d+}")
    @ApiOperation(value = "根据用户编号获取用户的审核历史记录，包括任务审核，猎刃审核，提现审核等")
    public List<UserAuditRecord> getUserAuditRecords(@PathVariable("id") Long id,
                                                    @Valid @RequestBody QueryUserAuditRecord queryUserAuditRecord, BindingResult result){
        return new ArrayList<>();
    }

    /**
     * 根据用户编号、审核类别、审核编号获取审核详情
     * @param id 用户编号
     * @param category 审核种类
     * @param cid 审核编号
     * @return
     */
    @GetMapping("/audit/{id:\\d+}/{category}/{cid:\\d+}")
    @ApiOperation(value = "根据用户编号、审核类别、审核编号获取审核详情")
    public UserAuditRecord getUserAuditRecord(@PathVariable("id") Long id,
                                              @PathVariable("category") String category,
                                              @PathVariable("cid") Long cid){
        return new UserAuditRecord();
    }


    /**
     * 根据查询条件获取用户操作日志
     * @param id 用户编号
     * @param queryUserAuditRecord 用户操作日志查询条件
     * @param result 异常信息
     * @return
     */
    @GetMapping("/op/{id:\\d+}")
    @ApiOperation(value = "根据查询条件获取用户操作日志")
    public List<UserOperationLog> getUserOperationLogs(@PathVariable("id") Long id,
                                                       @Valid @RequestBody QueryUserAuditRecord queryUserAuditRecord,
                                                       BindingResult result){
        return new ArrayList<>();
    }

    /**
     * 根据用户编号和操作编号获取操作详情
     * @param id 用户编号
     * @param oid 操作编号
     * @return
     */
    @GetMapping("/op/{uid:\\d+}/{oid:\\d+}")
    @ApiOperation(value = "根据用户编号和操作编号获取操作详情")
    public UserOperationLog getUserOperationLog(@PathVariable("uid") Long id,@PathVariable("oid") Long oid){
        return new UserOperationLog();
    }

    /**
     * 根据用户编号和统计条件获取用户收支统计情况
     * @param id 用户编号
     * @param userWithdrawStatisticsCondition 统计条件
     * @return
     */
    @GetMapping("/statistics/{id:\\d+}/ws")
    @ApiOperation(value = "根据用户编号和统计条件获取用户收支统计情况")
    public UserWithdrawStatistics getUserWithdrawStatistics(@PathVariable("id") Long id,
                                                            @RequestBody UserWithdrawStatisticsCondition userWithdrawStatisticsCondition){
        return new UserWithdrawStatistics();
    }

    /**
     * 根据用户编号和收支查询条件获取收支列表
     * @param id 用户编号
     * @param queryUserWithdraw 查询条件
     * @param result 校验异常结果
     * @return
     */
    @GetMapping("/ws/{id:\\d+}")
    public List<UserWithdraw> getUserWithdraw(@PathVariable("id") Long id,
                                              @Valid @RequestBody QueryUserWithdraw queryUserWithdraw,
                                              BindingResult result){
        return new ArrayList<>();
    }


}
