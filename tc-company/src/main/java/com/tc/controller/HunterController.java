package com.tc.controller;

import com.tc.db.entity.CommentHunter;
import com.tc.db.entity.Hunter;
import com.tc.dto.user.HunterTaskStatistics;
import com.tc.dto.comment.QueryUserComment;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 猎刃控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping("/hunter")
public class HunterController {

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
     * 根据用户编号获取猎刃的评论信息
     * @param id 用户编号
     * @param queryUserComment 用户评论查询条件
     * @param result 异常结果
     * @return
     */
    @GetMapping("/comment//{id:\\d+}")
    @ApiOperation(value = "根据用户编号获取用户的评论列表")
    public List<CommentHunter> getHunterComment(@PathVariable("id") Long id,
                                                @Valid @RequestBody QueryUserComment queryUserComment, BindingResult result){
        return new ArrayList<>();
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
}
