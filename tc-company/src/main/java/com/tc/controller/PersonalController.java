package com.tc.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.tc.db.entity.User;
import com.tc.dto.user.RegisterUser;
import com.tc.exception.ValidException;
import com.tc.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 个人信息中心
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/me")
public class PersonalController {

    @Autowired
    private UserService userService;

    /**
     * 用户基本信息
     * @param authentication 当前登陆的用户
     * @return
     */
    @GetMapping("/info")
    @JsonView(User.UserBasicView.class)
    public User me(Authentication authentication){
        return userService.getUserByUsername(authentication.getPrincipal().toString());
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

}



