package com.tc.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.tc.db.entity.*;
import com.tc.db.enums.UserCategory;
import com.tc.dto.user.*;
import com.tc.exception.ValidException;
import com.tc.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
    @GetMapping
    @ApiOperation(value = "根据查询条件获取用户列表")
    public List<User> all(@Valid @RequestBody QueryUser queryUser, BindingResult result){
        return new ArrayList<>();
    }


    /**
     * 用户注册
     * @param user 用户信息
     * @param result 异常结果
     * @return
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册")
    public User register(@Valid @RequestBody RegisterUser user, BindingResult result){
        if (result.hasErrors()){
            throw new ValidException(result.getFieldErrors());
        }
        return userService.save(user.toUser());
    }

    /**
     * 用户基本信息
     * @param authentication 当前登陆的用户
     * @return
     */
    @GetMapping("/me")
    @JsonView(User.UserBasicView.class)
    public User me(Authentication authentication){
        return userService.getUserByUsername(authentication.getPrincipal().toString());
    }


    /**
     * 用户详情信息
     * @param authentication 当前登陆的用户
     * @return
     */
    @GetMapping("/detail")
    @JsonView(User.UserBasicDetailView.class)
    public User detail(Authentication authentication){
        return userService.getUserByUsername(authentication.getPrincipal().toString());
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @param result 异常结果
     * @return
     */
    @PutMapping(value = "/{id:\\d+}")
    public User update(@Valid @RequestBody User user, BindingResult result){
        if (result.hasErrors()){
            throw new ValidException(result.getFieldErrors());
        }
        return userService.update(user);
    }

    /**
     * 根据用户分类获取用户统计信息
     * @param name 用户分类
     * @return
     */
    @GetMapping("/statistics/{name}")
    @ApiOperation(value = "根据用户分类获取用户统计信息")
    public UserCategoryStatistics getUserStatisticsByCategory(@PathVariable("name") String name){
        return new UserCategoryStatistics();
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
     * 根据用户编号获取用户联系方式列表
     * @param id 用户编号
     * @return
     */
    @GetMapping("/contact/{id:\\d+}")
    @ApiOperation(value = "根据用户编号获取用户任务统计信息")
    public List<UserContact> getUserContact(@PathVariable("id") Long id){
        return new ArrayList<>();
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
     * 获取猎刃的基本信息
     * @param id 用户编号
     * @return
     */
    @GetMapping("/hunter/{id:\\d+}")
    @ApiOperation(value = "根据用户编号获取猎刃的基本信息")
    public Hunter hunterDetail(@PathVariable("id") Long id){
        return new Hunter();
    }

    /**
     * 根据用户编号获取猎刃接受的任务的统计信息
     * @param id 用户编号
     * @return
     */
    @GetMapping("/statistics/{id:\\d+}/in/task")
    @ApiOperation(value = "根据用户编号获取猎刃接受的任务的统计信息")
    public HunterTaskStatistics getHunterTaskStatistics(@PathVariable("id") Long id){
        return new HunterTaskStatistics();
    }

    /**
     * 根据用户编号获取猎刃的评论信息
     * @param id 用户编号
     * @param queryUserComment 用户评论查询条件
     * @param result 异常结果
     * @return
     */
    @GetMapping("/comment/{id:\\d+}")
    @ApiOperation(value = "根据用户编号获取用户的评论列表")
    public List<CommentHunter> getHunterComment(@PathVariable("id") Long id,
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

    /**
     * 修改用户信息
     * @param modifyUser
     * @param result
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改用户信息")
    public User update(@Valid @RequestBody ModifyUser modifyUser,BindingResult result){
        return new User();
    }

    /**
     * 删除用户
     * @param id 用户编号
     */
    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "删除用户")
    public void delete(@PathVariable("id") Long id){

    }
}
