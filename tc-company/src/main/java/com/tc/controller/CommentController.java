package com.tc.controller;

import com.tc.db.entity.CommentUser;
import com.tc.dto.user.QueryUserComment;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论管理
 * @author Cyg
 *
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/comment")
public class CommentController {

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

}
