package com.tc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 评论管理
 * @author Cyg
 *
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/comment")
public class CommentController {
}
