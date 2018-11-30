package com.tc.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.tc.db.entity.User;
import com.tc.dto.RegisterUser;
import com.tc.exception.ValidException;
import com.tc.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    /**
     * 用户注册
     * @param user
     * @param result
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
     * @param authentication
     * @return
     */
    @GetMapping("/me")
    @JsonView(User.UserBasicView.class)
    public User me(Authentication authentication){
        return userService.getUserByUsername(authentication.getPrincipal().toString());
    }


    /**
     * 用户详情信息
     * @param authentication
     * @return
     */
    @GetMapping("/detail")
    @JsonView(User.UserBasicDetailView.class)
    public User detail(Authentication authentication){
        return userService.getUserByUsername(authentication.getPrincipal().toString());
    }

    /**
     * 更新用户信息
     * @param user
     * @param result
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
