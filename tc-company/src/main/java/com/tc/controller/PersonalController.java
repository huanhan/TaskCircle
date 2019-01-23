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
import com.tc.service.*;
import com.tc.until.StringResourceCenter;
import com.tc.until.ValidateUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
     * @param id 当前登陆的用户
     * @return
     */
    @GetMapping("/{id:\\d+}")
    public Admin me(@PathVariable("id") Long id){
        Admin resultAdmin = adminService.findOne(id);
        return Admin.toDetail(resultAdmin);
    }

    /**
     * 修改管理员信息
     * @param modifyAdmin 管理员信息
     * @param bindingResult
     * @return
     */
    @PutMapping("/{id:\\d+}")
    @ApiOperation(value = "修改管理员信息")
    public Admin update(@PathVariable("id") Long id,@Valid @RequestBody ModifyAdmin modifyAdmin,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
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
    @PutMapping("/pw/{id:\\d+}")
    @ApiOperation(value = "修改管理员密码")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody ModifyPassword modifyPassword, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
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
     * @param id
     * @param modifyUserHeader
     * @param bindingResult
     */
    @PutMapping("/header/{id:\\d+}")
    @ApiOperation(value = "修改管理员的个人头像(虚拟)")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody ModifyUserHeader modifyUserHeader,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
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
     * @param id
     * @return
     */
    @GetMapping("/contact/{id:\\d+}")
    @ApiOperation(value = "获取我的所有联系方式")
    public Result contacts(@PathVariable("id") Long id){
        List<UserContact> query = userContactService.findByUser(id);
        return Result.init(UserContact.toListInIndex(query));
    }

    /**
     * 管理员添加联系方式
     * @param id
     * @param addContact
     * @param bindingResult
     * @return
     */
    @PostMapping("/contact/add/{id:\\d+}")
    @ApiOperation(value = "管理员添加本人的联系方式")
    public UserContact add(@PathVariable("id") Long id, @Valid @RequestBody AddContact addContact, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
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
     * @param id
     * @param addContact
     * @param bindingResult
     * @return
     */
    @PostMapping("/contact/modify/{id:\\d+}")
    @ApiOperation(value = "管理员修改本人的联系方式")
    public UserContact update(@PathVariable("id") Long id, @Valid @RequestBody AddContact addContact, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
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
     * @param id
     * @param name
     */
    @DeleteMapping("/contact/{name}/{id:\\d+}")
    @ApiOperation(value = "管理员删除自己的联系方式")
    public void deleteContact(@PathVariable("id") Long id, @PathVariable("name")UserContactName name){
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
     * @param id
     * @return
     */
    @GetMapping("/images")
    @ApiOperation(value = "获取管理员本人的所有图片资料")
    public Result images(@PathVariable("id") Long id){
        List<UserImg> userImgs = userImgService.findByUser(id);
        return Result.init(UserImg.toListInIndex(userImgs));
    }

    /**
     * 添加管理员本人的图片资料
     * @param id
     * @param addImages
     * @param bindingResult
     * @return
     */
    @PostMapping("/images/add/{id:\\d+}")
    @ApiOperation(value = "添加管理员本人的图片资料")
    public UserImg add(@PathVariable("id") Long id, @Valid @RequestBody AddImages addImages, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
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
     * @param id
     * @param addImages
     * @param bindingResult
     * @return
     */
    @PutMapping("/images/modify/{id:\\d+}")
    @ApiOperation(value = "修改管理员本人的图片资料")
    public UserImg update(@PathVariable("id") Long id, @Valid @RequestBody AddImages addImages, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
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
     * @param id
     * @param name
     */
    @DeleteMapping("/images/{name}/{id:\\d+}")
    @ApiOperation(value = "管理员删除自己的图片资料")
    public void deleteImages(@PathVariable("id") Long id, @PathVariable("name")UserIMGName name){
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
    @PostMapping("/audit/query/{id:\\d+}")
    @ApiOperation(value = "获取管理员本人的审核历史列表")
    public Result getAuditByAdmin(@PathVariable("id") Long id,@RequestBody QueryAudit queryAudit){
        if (queryAudit.getAdminId() == null || queryAudit.getAdminId() <= 0){
            throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_ADMIN_FAILED);
        }
        queryAudit.setUserId(id);
        Page<Audit> queryAudits = auditService.findByQueryAudit(queryAudit);
        return Result.init(Audit.toListInIndex(queryAudits.getContent()),queryAudit);
    }

    /**
     * 查看管理员本人的审核详情信息
     * @param id 审核编号
     * @return
     */
    @GetMapping("/audit/detail/{aid:\\d+}/{id:\\d+}")
    @ApiOperation(value = "查看管理员本人的审核详情信息")
    public Audit auditDetail(@PathVariable("id") Long id,@PathVariable("aid") String aid){
        Audit result = auditService.findOne(aid);
        if (!id.equals(result.getAdminId())){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        return Audit.toDetail(result);
    }
}



