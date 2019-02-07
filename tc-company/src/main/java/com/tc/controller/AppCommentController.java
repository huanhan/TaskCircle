package com.tc.controller;


import com.tc.db.entity.*;
import com.tc.db.enums.CommentType;
import com.tc.dto.app.*;
import com.tc.dto.comment.*;
import com.tc.exception.ValidException;
import com.tc.service.*;
import com.tc.until.StringResourceCenter;
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
 * APP评论控制器
 *
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/app/comment")
public class AppCommentController {


    @Autowired
    private CommentUserService commentUserService;

    @Autowired
    private CommentTaskService commentTaskService;

    @Autowired
    private CommentHunterService commentHunterService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private HunterTaskService hunterTaskService;

    @PostMapping("/taskAndTask/{id:\\d+}")
    @ApiOperation(value = "猎刃对任务和用户进行评论")
    public ResultApp taskAndTask(@PathVariable("id") Long id, @Valid @RequestBody HunterCommentDto comment, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }

        //验证猎刃是否有权力评价这个任务和这个用户
        HunterTask hunterTask = hunterTaskService.findOne(comment.getHunterTaskId());
        if (hunterTask == null) {
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        if (hunterTask.getHunterId() != id) {
            throw new ValidException("只能评价自己的任务");
        }

        if (comment.getEvaUserId() == hunterTask.getTask().getUser().getId()) {
            throw new ValidException("当前任务的用户和提交用户不一致");
        }

        if (!hunterTask.getHunterCTask() && !hunterTask.getHunterCUser()) {
            throw new ValidException("任务已经评价过了，请勿重复评价");
        }

        //评价任务
        Comment taskComment = new Comment();
        taskComment.setType(CommentType.COMMENT_TASK);
        //设置任务的评论星级
        taskComment.setNumber(comment.getTaskStart());
        //设置任务的评论内容
        taskComment.setContext(comment.getTaskContext());
        //设置此条评论发起者
        taskComment.setCreationId(id);
        Comment saveTask = commentService.save(taskComment);

        CommentTask commentTask = new CommentTask();
        commentTask.setCommentId(saveTask.getId());
        commentTask.setTaskId(hunterTask.getTaskId());
        commentTaskService.save(commentTask);

        //用户的评论
        Comment userComment = new Comment();
        userComment.setType(CommentType.HUNTER_COMMENT_USER);
        //设置用户的评价内容
        userComment.setContext(comment.getUserContext());
        //设置用户的评价星级
        userComment.setNumber(comment.getUserStart());
        userComment.setCreationId(id);
        Comment saveUser = commentService.save(userComment);

        CommentUser commentUser = new CommentUser();
        commentUser.setCommentId(saveUser.getId());
        commentUser.setTaskId(hunterTask.getTaskId());
        commentUser.setUserId(comment.getEvaUserId());
        commentUserService.save(commentUser);

        hunterTaskService.updateEvaState(hunterTask.getId(), CommentType.HUNTER_COMMENT_USER);
        hunterTaskService.updateEvaState(hunterTask.getId(), CommentType.COMMENT_TASK);

        return ResultApp.init("评价成功");
    }

    /**
     * 猎刃评论任务
     *
     * @param id 用户编号
     * @return
     */
    /*@PostMapping("/task/{id:\\d+}")
    @ApiOperation(value = "猎刃对任务进行评论")
    public Comment toTask(@PathVariable("id") Long id, @Valid @RequestBody TaskComment taskComment, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }

        //验证猎刃评论的任务与猎刃执行的任务是否一致

        //添加评论记录

        return new Comment();
    }*/


    /**
     * 猎刃评论用户
     *
     * @param id
     * @param userComment
     * @param bindingResult
     * @return
     */
   /* @PostMapping("/user/{id:\\d+}")
    @ApiOperation(value = "猎刃对用户进行评论")
    public Comment toUser(@PathVariable("id") Long id, @Valid @RequestBody UserComment userComment, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }

        //验证猎刃是否有资格评论用户，依据猎刃执行的任务

        return new Comment();
    }*/

    /**
     * 用户评论猎刃
     *
     * @param id
     * @param hunterComment
     * @param bindingResult
     * @return
     */
    @PostMapping("/hunter/{id:\\d+}")
    @ApiOperation(value = "用户对猎刃进行评论")
    public ResultApp toHunter(@PathVariable("id") Long id, @Valid @RequestBody HunterComment hunterComment, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }
        //验证用户是否有资格评论猎刃，依据猎刃执行的任务
        HunterTask hunterTask = hunterTaskService.findOne(hunterComment.getHunterTaskId());
        if (hunterTask == null) {
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }

        if (hunterTask.getTask().getUser().getId() != id) {
            throw new ValidException("只能评价自己的任务");
        }

        if (!hunterTask.getUserCHunter()) {
            throw new ValidException("任务已经评价过了，请勿重复评价");
        }

        //用户的评论
        Comment userComment = new Comment();
        userComment.setType(CommentType.USER_COMMENT_HUNTER);
        //设置用户的评价内容
        userComment.setContext(hunterComment.getContent());
        //设置用户的评价星级
        userComment.setNumber(hunterComment.getStart());
        userComment.setCreationId(id);
        Comment saveUser = commentService.save(userComment);

        CommentHunter commentHunter = new CommentHunter();
        commentHunter.setCommentId(saveUser.getId());
        commentHunter.setHunterId(hunterComment.getEvaHunterId());
        commentHunter.setHunterTaskId(hunterComment.getHunterTaskId());
        commentHunterService.save(commentHunter);

        hunterTaskService.updateEvaState(hunterTask.getId(), CommentType.USER_COMMENT_HUNTER);
        return ResultApp.init("评价成功");
    }

    /**
     * 用户获取猎刃对用户的评论
     *
     * @param id
     * @return
     */
    @GetMapping("/evaUserByUser/{page:\\d+}/{size:\\d+}/{id:\\d+}")
    @ApiOperation(value = "用户获取猎刃对用户的评论")
    public AppPage received(@PathVariable("page") int page,
                            @PathVariable("size") int size,
                            @PathVariable("id") Long id) {
        QueryUserComment queryUserComment = new QueryUserComment(page, size);
        queryUserComment.setUserId(id);
        Page<CommentUser> query = commentUserService.findByQuery(queryUserComment);
        List<CommentUser> commentUsers = query.getContent();
        List<CommentUserDto> commentUserDtos = new ArrayList<>();
        for (CommentUser commentUser : commentUsers) {
            commentUserDtos.add(CommentUserDto.init(commentUser));
        }
        return AppPage.init(commentUserDtos, query);
    }

    /**
     * 用户获取用户对猎刃的评论
     *
     * @param id
     * @return
     */
    @GetMapping("/evaHunterByUser/{page:\\d+}/{size:\\d+}/{id:\\d+}")
    @ApiOperation(value = "用户获取用户对猎刃的评论")
    public AppPage issue(@PathVariable("page") int page,
                         @PathVariable("size") int size,
                         @PathVariable("id") Long id) {
        QueryComment queryComment = new QueryComment(page, size);
        queryComment.setType(CommentType.USER_COMMENT_HUNTER);
        queryComment.setCreationId(id);

        Page<Comment> query = commentService.findByQuery(queryComment);
        List<Comment> comments = query.getContent();
        List<CommentHunterDto> commentHunterDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentHunterDtos.add(CommentHunterDto.init(comment));
        }
        return AppPage.init(commentHunterDtos, query);
    }

    /**
     * 猎刃获取猎刃对用户的评论
     *
     * @param id
     * @return
     */
    @GetMapping("/evaUserByHunter/{page:\\d+}/{size:\\d+}/{id:\\d+}")
    @ApiOperation(value = "猎刃获取猎刃对用户的评论")
    public AppPage hunterReceived(@PathVariable("page") int page,
                                  @PathVariable("size") int size,
                                  @PathVariable("id") Long id) {
        QueryComment queryComment = new QueryComment(page, size);
        queryComment.setCreationId(id);
        queryComment.setType(CommentType.HUNTER_COMMENT_USER);
        Page<Comment> query = commentService.findByQuery(queryComment);
        List<Comment> comments = query.getContent();
        List<CommentUserDto> commentUserDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentUserDtos.add(CommentUserDto.init(comment));
        }
        return AppPage.init(commentUserDtos, query);
    }

    /**
     * 猎刃获取用户对猎刃的评论
     *
     * @param id
     * @return
     */
    @GetMapping("/evaHunterByHunter/{page:\\d+}/{size:\\d+}/{id:\\d+}")
    @ApiOperation(value = "猎刃获取用户对猎刃的评论")
    public AppPage hunterIssue(@PathVariable("page") int page,
                               @PathVariable("size") int size,
                               @PathVariable("id") Long id) {
        QueryHunterComment queryHunterComment = new QueryHunterComment(page, size);
        queryHunterComment.setHunterId(id);
        Page<CommentHunter> query = commentHunterService.findByQuery(queryHunterComment);
        List<CommentHunter> commentHunters = query.getContent();
        List<CommentHunterDto> commentHunterDtos = new ArrayList<>();
        for (CommentHunter commentHunter : commentHunters) {
            commentHunterDtos.add(CommentHunterDto.init(commentHunter));
        }
        return AppPage.init(commentHunterDtos, query);

    }

    /**
     * 废弃
     * 获取评论详情
     * @param id 用户编号
     * @param cid 评论编号
     * @return
     */
    /*@GetMapping
    public Comment detail(@PathVariable("id") Long id,@PathVariable("cid") Long cid){
        return new Comment();
    }*/

}
