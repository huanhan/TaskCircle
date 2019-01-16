package com.tc.controller;

import com.tc.db.entity.User;
import com.tc.dto.user.ModifyUser;
import com.tc.dto.user.ModifyUserHeader;
import com.tc.exception.ValidException;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * APP用户控制器
 *
 * @author Cyg
 *
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/app/user")
public class AppUserController {


    /**
     * 用来获取用户详情信息
     * @param id
     * @return
     */
    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "获取用户详情信息")
    public User detail(@PathVariable("id") Long id){
        return new User();
    }

    /**
     * 修改用户信息
     * @param id
     * @param modifyUser
     * @param bindingResult
     * @return
     */
    @PutMapping("/{id:\\d+}")
    @ApiOperation(value = "修改用户信息")
    public User update(@PathVariable("id") Long id,@Valid @RequestBody ModifyUser modifyUser, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        return new User();
    }


    /**
     * 修改用户头像，需要图片的URL地址
     * @param id
     * @param modifyUserHeader
     * @param bindingResult
     */
    @PutMapping("/header/{id:\\d+}")
    @ApiOperation(value = "修改用户头像")
    public void updateHead(@PathVariable("id") Long id, @Valid @RequestBody ModifyUserHeader modifyUserHeader, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
    }

    /**
     * 用户提交猎刃审核申请
     * @param id
     */
    @GetMapping("/upAudit/{id:\\d+}")
    @ApiOperation(value = "提交成为猎刃的申请")
    public void upAudit(@PathVariable("id") Long id){
        //根据编号获取详情信息

        //验证用户的准确性

        //验证用户信息完整度

        //修改用户状态为申请状态
    }


}
