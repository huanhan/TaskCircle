package com.tc.controller;

import com.tc.db.entity.Admin;
import com.tc.db.entity.Audit;
import com.tc.db.entity.UserContact;
import com.tc.db.entity.UserImg;
import com.tc.db.entity.pk.UserContactPK;
import com.tc.db.entity.pk.UserImgPK;
import com.tc.db.enums.UserContactName;
import com.tc.db.enums.UserIMGName;
import com.tc.dto.Result;
import com.tc.dto.admin.ModifyAdmin;
import com.tc.dto.audit.QueryAudit;
import com.tc.dto.user.ModifyPassword;
import com.tc.dto.user.ModifyUserHeader;
import com.tc.dto.user.contact.AddContact;
import com.tc.dto.user.images.AddImages;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.security.MyJwtTokenEnhacer;
import com.tc.service.*;
import com.tc.until.StringResourceCenter;
import com.tc.until.ValidateUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 管理员个人信息中心
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/admin/me")
public class PersonalController {

    private Logger logger = LoggerFactory.getLogger(PersonalController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserContactService userContactService;

    @Autowired
    private UserImgService userImgService;

    @Autowired
    private AuditService auditService;

    /**
     * 用户基本信息
     * @param request 当前登陆的用户
     * @return
     */
    @GetMapping()
    public Admin me(HttpServletRequest request){
        logger.info("userId:" + request.getAttribute(StringResourceCenter.USER_ID));
        Admin resultAdmin = adminService.findOne(Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString()));
        return Admin.toDetail(resultAdmin);
    }

    /**
     * 修改管理员信息
     * @param modifyAdmin 管理员信息
     * @param bindingResult
     * @return
     */
    @PutMapping()
    @ApiOperation(value = "修改管理员信息")
    public Admin update(HttpServletRequest request,@Valid @RequestBody ModifyAdmin modifyAdmin,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (!id.equals(modifyAdmin.getId())){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        Admin admin = adminService.findOne(id);
        if (admin == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
        Admin result = adminService.save(ModifyAdmin.toAdmin(admin,modifyAdmin));
        return Admin.toDetail(result);
    }

    /**
     * 修改管理员密码
     * @param modifyPassword 管理员密码信息
     * @param bindingResult
     * @return
     */
    @PutMapping("/pw")
    @ApiOperation(value = "修改管理员密码")
    public void update(HttpServletRequest request, @Valid @RequestBody ModifyPassword modifyPassword, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (!id.equals(modifyPassword.getId())){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        Admin admin = adminService.findOne(id);
        if (admin == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
        boolean isEquals = userService.hasPasswordEquals(admin.getUser().getPassword(),modifyPassword.getOldPassword());
        if (!isEquals){
            throw new ValidException("输入的密码不正确");
        }
        isEquals = StringUtils.equals(modifyPassword.getNewPassword(),modifyPassword.getQueryPassword());
        if (!isEquals){
            throw new ValidException("两次密码不一样");
        }
        boolean isSuccess = userService.updatePassword(id,modifyPassword);
        if (!isSuccess){
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
    }

    /**
     * 修改管理员的个人头像(虚拟)
     * @param request
     * @param modifyUserHeader
     * @param bindingResult
     */
    @PutMapping("/header")
    @ApiOperation(value = "修改管理员的个人头像(虚拟)")
    public void update(HttpServletRequest request, @Valid @RequestBody ModifyUserHeader modifyUserHeader,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (!id.equals(modifyUserHeader.getId())){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        boolean isSuccess = userService.updateHeader(id,modifyUserHeader.getHeader());
        if (!isSuccess){
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
    }

    /**
     * 获取本人的所有联系方式
     * @param request
     * @return
     */
    @GetMapping("/contact")
    @ApiOperation(value = "获取我的所有联系方式")
    public Result contacts(HttpServletRequest request){
        List<UserContact> query = userContactService.findByUser(Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString()));
        return Result.init(UserContact.toListInIndex(query));
    }

    @GetMapping("/contact/{name}")
    @ApiOperation(value = "获取联系方式详情信息")
    public UserContact detail(HttpServletRequest request,@PathVariable("name")UserContactName name){
        UserContact detail = userContactService.findOne(
                new UserContactPK(Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString()),
                        name));
        if (detail == null){
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
        return UserContact.toDetail(detail);
    }

    /**
     * 管理员添加联系方式
     * @param request
     * @param addContact
     * @param bindingResult
     * @return
     */
    @PostMapping("/contact/add")
    @ApiOperation(value = "管理员添加本人的联系方式")
    public UserContact add(HttpServletRequest request, @Valid @RequestBody AddContact addContact, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (!id.equals(addContact.getUserId())){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        if (!ValidateUtil.isAdd(addContact)){
            throw new ValidException(addContact.getContactName().getContactName() + "：格式不正确");
        }
        UserContact result = userContactService.save(AddContact.toContact(addContact));
        if (result == null){
            throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
        }
        return UserContact.toDetail(result);
    }

    /**
     * 管理员修改本人的联系方式
     * @param request
     * @param addContact
     * @param bindingResult
     * @return
     */
    @PutMapping("/contact/modify")
    @ApiOperation(value = "管理员修改本人的联系方式")
    public UserContact update(HttpServletRequest request, @Valid @RequestBody AddContact addContact, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (!id.equals(addContact.getUserId())){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        if (!ValidateUtil.isAdd(addContact)){
            throw new ValidException(addContact.getContactName().getContactName() + "：格式不正确");
        }
        UserContact query = userContactService.findOne(new UserContactPK(addContact.getUserId(),addContact.getContactName()));
        if (query == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        if (!AddContact.toContact(query,addContact)){
            throw new ValidException(StringResourceCenter.VALIDATOR_UPDATE_ABNORMAL);
        }
        UserContact result = userContactService.update(query);
        if (result == null){
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
        return UserContact.toDetail(result);
    }

    /**
     * 管理员删除自己的联系方式
     * @param request
     * @param name
     */
    @DeleteMapping("/contact/{name}")
    @ApiOperation(value = "管理员删除自己的联系方式")
    public void deleteContact(HttpServletRequest request, @PathVariable("name")UserContactName name){
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        UserContact query = userContactService.findOne(new UserContactPK(id,name));
        if (query == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        boolean isSuccess = userContactService.deleteById(new UserContactPK(id,name));
        if (!isSuccess){
            throw new DBException(StringResourceCenter.DB_DELETE_FAILED);
        }
    }

    /**
     * 获取管理员本人的所有图片资料
     * @param request
     * @return
     */
    @GetMapping("/images")
    @ApiOperation(value = "获取管理员本人的所有图片资料")
    public Result images(HttpServletRequest request){
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        List<UserImg> userImgs = userImgService.findByUser(id);
        return Result.init(UserImg.toListInIndex(userImgs));
    }

    /**
     * 添加管理员本人的图片资料
     * @param request
     * @param addImages
     * @param bindingResult
     * @return
     */
    @PostMapping("/images/add")
    @ApiOperation(value = "添加管理员本人的图片资料")
    public UserImg add(HttpServletRequest request, @Valid @RequestBody AddImages addImages, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (!id.equals(addImages.getUserId())){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        UserImg result = userImgService.save(AddImages.toImages(addImages));
        if (result == null){
            throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
        }
        return UserImg.toDetail(result);
    }

    /**
     * 修改管理员本人的图片资料
     * @param request
     * @param addImages
     * @param bindingResult
     * @return
     */
    @PutMapping("/images/modify")
    @ApiOperation(value = "修改管理员本人的图片资料")
    public UserImg update(HttpServletRequest request, @Valid @RequestBody AddImages addImages, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (!id.equals(addImages.getUserId())){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        UserImg query = userImgService.findOne(new UserImgPK(addImages.getUserId(),addImages.getImgName()));
        if (query == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        if (!AddImages.toContact(query,addImages)){
            throw new ValidException(StringResourceCenter.VALIDATOR_UPDATE_ABNORMAL);
        }
        UserImg result = userImgService.update(query);
        if (result == null){
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
        return UserImg.toDetail(result);
    }

    /**
     * 管理员删除自己的图片资料
     * @param request
     * @param name
     */
    @DeleteMapping("/images/{name}")
    @ApiOperation(value = "管理员删除自己的图片资料")
    public void deleteImages(HttpServletRequest request, @PathVariable("name")UserIMGName name){
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        UserImg query = userImgService.findOne(new UserImgPK(id,name));
        if (query == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        boolean isSuccess = userImgService.deleteById(new UserImgPK(id,name));
        if (!isSuccess){
            throw new DBException(StringResourceCenter.DB_DELETE_FAILED);
        }
    }


    /**
     * 获取管理员本人的审核列表
     * @param queryAudit 审核查询条件
     * @return
     */
    @PostMapping("/audit/query")
    @ApiOperation(value = "获取管理员本人的审核历史列表")
    public Result getAuditByAdmin(HttpServletRequest request,@RequestBody QueryAudit queryAudit){
        if (queryAudit.getAdminId() == null || queryAudit.getAdminId() <= 0){
            throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_ADMIN_FAILED);
        }
        queryAudit.setUserId(Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString()));
        Page<Audit> queryAudits = auditService.findByQueryAudit(queryAudit);
        return Result.init(Audit.toListInIndex(queryAudits.getContent()),queryAudit);
    }

    /**
     * 查看管理员本人的审核详情信息
     * @param id 审核编号
     * @return
     */
    @GetMapping("/audit/detail/{id:\\d+}")
    @ApiOperation(value = "查看管理员本人的审核详情信息")
    public Audit auditDetail(@PathVariable("id") Long id,HttpServletRequest request){
        Audit result = auditService.findOne(id);
        Long aid = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (!aid.equals(result.getAdminId())){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        return Audit.toDetail(result);
    }
}



