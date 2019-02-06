package com.tc.controller;

import com.tc.db.entity.User;
import com.tc.db.enums.UserCategory;
import com.tc.db.enums.UserState;
import com.tc.dto.app.RegistAppDto;
import com.tc.dto.app.ResultApp;
import com.tc.dto.app.UserAppDto;
import com.tc.dto.user.ModifyUser;
import com.tc.dto.user.ModifyUserHeader;
import com.tc.exception.ValidException;
import com.tc.security.app.validate.code.impl.RedisValidateCodeRepository;
import com.tc.security.core.validate.code.ValidateCode;
import com.tc.security.core.validate.code.ValidateCodeException;
import com.tc.security.core.validate.code.ValidateCodeProcessorHolder;
import com.tc.security.core.validate.code.ValidateCodeType;
import com.tc.service.CommentUserService;
import com.tc.service.UserImgService;
import com.tc.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private RedisValidateCodeRepository redisValidateCodeRepository;

    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

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
    public ResultApp update(@PathVariable("id") Long id, @Valid @RequestBody ModifyUser modifyUser, BindingResult bindingResult) {
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

        return ResultApp.init("更新成功");
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
    public ResultApp updateHead(@PathVariable("id") Long id, @Valid @RequestBody ModifyUserHeader modifyUserHeader, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }
        User user = userService.findOne(id);
        user.setHeadImg(modifyUserHeader.getHeader());
        userService.update(user);
        return ResultApp.init("更新成功");
    }

    /**
     * 用户提交猎刃审核申请
     *
     * @param id
     */
    @PostMapping("/upAudit/{id:\\d+}")
    @ApiOperation(value = "提交成为猎刃的申请")
    public ResultApp upAudit(@PathVariable("id") Long id) {
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
        return ResultApp.init("更新成功");
    }

    /**
     * 提交猎刃审核申请(废弃)
     *
     * @param commitHunter  提交申请的必要信息
     * @param bindingResult
     */
    /*@PostMapping("/commit/hunter")
    @ApiOperation(value = "提交猎刃审核申请")
    public ResultApp commitByHunter(@Valid @RequestBody CommitHunter commitHunter, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }

        User user = userService.findOne(commitHunter.getId());
        if (user == null) {
            throw new ValidException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
        if (user.getCategory() != UserCategory.NORMAL) {
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        if (user.getState() != UserState.NORMAL) {
            throw new ValidException(StringResourceCenter.VALIDATOR_STATE_FAILED);
        }

        User result = userService.save(CommitHunter.toUser(user, commitHunter));
        if (result == null) {
            throw new ValidException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
        return ResultApp.init("已提交");
    }*/

    /**
     * 用户注册
     *
     * @param user   用户信息
     * @param result 异常结果
     * @return
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册")
    public ResultApp register(@Valid @RequestBody RegistAppDto user, HttpServletRequest req, HttpServletResponse resp, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidException(result.getFieldErrors());
        }
        //验证验证码是否正确
        ServletWebRequest servletWebRequest = new ServletWebRequest(req, resp);
        ValidateCode codeInSession = redisValidateCodeRepository.get(servletWebRequest, ValidateCodeType.IMAGE);
        String codeInRequest = user.getImageCode();

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidException("验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidException("验证码不存在");
        }

        if (codeInSession.isExpried()) {
            redisValidateCodeRepository.remove(servletWebRequest, ValidateCodeType.IMAGE);
            throw new ValidException("验证码已过期");
        }

        if (!org.apache.commons.lang.StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidException("验证码不匹配");
        }

        redisValidateCodeRepository.remove(servletWebRequest, ValidateCodeType.IMAGE);

        User save = userService.save(user.toUser());
        return ResultApp.init("注册成功");
    }


    @GetMapping("/{time:\\d+}/code/Image")
    public void validateCode(HttpServletRequest request, HttpServletResponse response, @PathVariable("time") Long time) throws Exception {
        validateCodeProcessorHolder.findValidateCodeProcessor("Image").create(new ServletWebRequest(request, response));
    }

}
