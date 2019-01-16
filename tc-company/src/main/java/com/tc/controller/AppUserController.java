package com.tc.controller;

import com.tc.db.entity.User;
import com.tc.db.enums.UserCategory;
import com.tc.db.enums.UserState;
import com.tc.dto.app.UserAppDto;
import com.tc.dto.user.ModifyUser;
import com.tc.dto.user.ModifyUserHeader;
import com.tc.exception.ValidException;
import com.tc.service.CommentUserService;
import com.tc.service.UserImgService;
import com.tc.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * APP用户控制器
 *
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/app/user")
public class AppUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentUserService commentUserService;

    @Autowired
    private UserImgService userImgService;

    /**
     * 用来获取用户详情信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "获取用户详情信息")
    public UserAppDto detail(@PathVariable("id") Long id) {
        User user = userService.findOne(id);
        UserAppDto userAppDto = new UserAppDto();
        BeanUtils.copyProperties(user, userAppDto);
        Long count = commentUserService.countByUserId(id);
        userAppDto.setCommentsNum(count);
        return userAppDto;
    }

    /**
     * 修改用户信息
     *
     * @param id
     * @param modifyUser
     * @param bindingResult
     * @return
     */
    @PutMapping("/{id:\\d+}")
    @ApiOperation(value = "修改用户信息")
    public UserAppDto update(@PathVariable("id") Long id, @Valid @RequestBody ModifyUser modifyUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }
        User user = userService.findOne(id);
        BeanUtils.copyProperties(modifyUser, user);
        User update = userService.update(user);
        UserAppDto userAppDto = new UserAppDto();
        BeanUtils.copyProperties(update, userAppDto);
        Long count = commentUserService.countByUserId(id);
        userAppDto.setCommentsNum(count);

        return userAppDto;
    }


    /**
     * 修改用户头像，需要图片的URL地址
     *
     * @param id
     * @param modifyUserHeader
     * @param bindingResult
     */
    @PutMapping("/header/{id:\\d+}")
    @ApiOperation(value = "修改用户头像")
    public void updateHead(@PathVariable("id") Long id, @Valid @RequestBody ModifyUserHeader modifyUserHeader, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }
        User user = userService.findOne(id);
        user.setHeadImg(modifyUserHeader.getHeader());
        userService.update(user);
    }

    /**
     * 用户提交猎刃审核申请
     *
     * @param id
     */
    @GetMapping("/upAudit/{id:\\d+}")
    @ApiOperation(value = "提交成为猎刃的申请")
    public void upAudit(@PathVariable("id") Long id) {
        //根据编号获取详情信息
        User user = userService.findOne(id);
        //验证用户的准确性
        if (user.getCategory() != UserCategory.NORMAL || user.getState() != UserState.NORMAL) {
            throw new ValidException("只有用户未审核状态才能才能提交审核！");
        }
        //验证用户信息完整度
        if (userImgService.countByUserId(user.getId()) == 0) {
            throw new ValidException("未上传证件图片");
        }

        if (StringUtils.isEmpty(user.getIdCard())) {
            throw new ValidException("身份证不能为空");
        }

        if (StringUtils.isEmpty(user.getAddress())) {
            throw new ValidException("家庭住址不能为空");
        }

        if (StringUtils.isEmpty(user.getPhone())) {
            throw new ValidException("用户手机号码还未设置");
        }
       /*
        if (StringUtils.isEmpty(user.getName())) {
            throw new ValidException("昵称不能为空");
        }

        if (StringUtils.isEmpty(user.getSchool())) {
            throw new ValidException("毕业学校不能为空");
        }

        if (StringUtils.isEmpty(user.getMajor())) {
            throw new ValidException("职业不能为空");
        }

        if (StringUtils.isEmpty(user.getInterest())) {
            throw new ValidException("兴趣不能为空");
        }

        if (StringUtils.isEmpty(user.getIntro())) {
            throw new ValidException("简介不能为空");
        }

        if (user.getHeight()!=null&&user.getHeight()!=0) {
            throw new ValidException("身高还未设置");
        }

        if (user.getWeight()!=null&&user.getWeight()!=0) {
            throw new ValidException("体重还未设置");
        }

        if (user.getBirthday()!=null) {
            throw new ValidException("生日还未设置");
        }

        if (StringUtils.isEmpty(user.getHeadImg())) {
            throw new ValidException("用户头像还未设置");
        }*/


        //修改用户状态为申请状态
        userService.updateState(id, UserState.AUDIT_HUNTER, new Date());
    }


}
