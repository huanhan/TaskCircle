package com.tc.controller;


import com.tc.db.entity.Comment;
import com.tc.dto.Result;
import com.tc.dto.comment.HunterComment;
import com.tc.dto.comment.QueryComment;
import com.tc.dto.comment.TaskComment;
import com.tc.dto.comment.UserComment;
import com.tc.exception.ValidException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * APP评论控制器
 *
 * @author Cyg
 *
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/app/comment")
public class AppCommentController {

    /**
     * 猎刃评论任务
     * @param id 用户编号
     * @return
     */
    @PostMapping("/task/{id:\\d+}")
    @ApiOperation(value = "猎刃对执行的任务进行评论")
    public Comment toTask(@PathVariable("id") Long id, @Valid @RequestBody TaskComment taskComment, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        //验证猎刃评论的任务与猎刃执行的任务是否一致

        //添加评论记录

        return new Comment();
    }


    /**
     * 猎刃评论用户
     * @param id
     * @param userComment
     * @param bindingResult
     * @return
     */
    @PostMapping("/user/{id:\\d+}")
    @ApiOperation(value = "猎刃对用户进行评论")
    public Comment toUser(@PathVariable("id") Long id, @Valid @RequestBody UserComment userComment, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        //验证猎刃是否有资格评论用户，依据猎刃执行的任务

        return new Comment();
    }

    /**
     * 用户评论猎刃
     * @param id
     * @param hunterComment
     * @param bindingResult
     * @return
     */
    @PostMapping("/user/{id:\\d+}")
    @ApiOperation(value = "猎刃对用户进行评论")
    public Comment toHunter(@PathVariable("id") Long id, @Valid @RequestBody HunterComment hunterComment, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        //验证用户是否有资格评论猎刃，依据猎刃执行的任务

        return new Comment();
    }

    /**
     * 获取评论我的信息
     * 依据的是评论的类别：包括（评论用户，评论猎刃，评论任务）
     * @param id
     * @param queryComment
     * @return
     */
    @PostMapping("/{id:\\d+}")
    @ApiOperation(value = "获取我的相关评论信息")
    public Result all(@PathVariable("id") Long id,@RequestBody QueryComment queryComment){
        return Result.init(new ArrayList<Comment>(),queryComment);
    }

    /**
     * 获取评论详情
     * @param id 用户编号
     * @param cid 评论编号
     * @return
     */
    @GetMapping
    public Comment detail(@PathVariable("id") Long id,@PathVariable("cid") Long cid){
        return new Comment();
    }

}
