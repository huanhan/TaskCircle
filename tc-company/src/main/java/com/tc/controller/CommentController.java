package com.tc.controller;

import com.tc.db.entity.CommentHunter;
import com.tc.db.entity.CommentTask;
import com.tc.db.entity.CommentUser;
import com.tc.dto.Result;
import com.tc.dto.comment.QueryHunterComment;
import com.tc.dto.comment.QueryUserComment;
import com.tc.dto.comment.QueryTaskComment;
import com.tc.service.CommentHunterService;
import com.tc.service.CommentTaskService;
import com.tc.service.CommentUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 评论管理
 * @author Cyg
 *
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/comment")
public class CommentController {

    @Autowired
    private CommentUserService commentUserService;

    @Autowired
    private CommentTaskService commentTaskService;

    @Autowired
    private CommentHunterService commentHunterService;


    /**
     * 获取用户评论信息
     * @param queryUserComment 用户评论查询条件
     * @return
     */
    @GetMapping("/user")
    @ApiOperation(value = "根据用户编号获取用户的评论列表")
    public Result getUserComment(@RequestBody QueryUserComment queryUserComment){
        Page<CommentUser> query = commentUserService.findByQuery(queryUserComment);
        return Result.init(CommentUser.toListInIndex(query.getContent()),queryUserComment);
    }

    /**
     * 获取猎刃评论信息
     * @param queryHunterComment 猎刃评论查询条件
     * @return
     */
    @GetMapping("/hunter")
    @ApiOperation(value = "根据猎刃编号获取猎刃的评论列表")
    public Result getUserComment(@RequestBody QueryHunterComment queryHunterComment){
        Page<CommentHunter> query = commentHunterService.findByQuery(queryHunterComment);
        return Result.init(CommentHunter.toListInIndex(query.getContent()),queryHunterComment);
    }

    /**
     * 获取任务评论信息
     * @param queryTaskComment 任务评论查询条件
     * @return
     */
    @GetMapping("/task")
    @ApiOperation(value = "根据猎刃编号获取猎刃的评论列表")
    public Result getTaskComment(@RequestBody QueryTaskComment queryTaskComment){
        Page<CommentTask> query = commentTaskService.findByQuery(queryTaskComment);
        return Result.init(CommentTask.toListInIndex(query.getContent()),queryTaskComment);
    }
}
