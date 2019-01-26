package com.tc.controller;

import com.tc.db.entity.Admin;
import com.tc.db.entity.Audit;
import com.tc.db.entity.Authority;
import com.tc.db.entity.User;
import com.tc.dto.LongIds;
import com.tc.dto.Result;
import com.tc.dto.admin.*;
import com.tc.dto.audit.QueryAudit;
import com.tc.dto.authority.QueryAuthority;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.*;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理员控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private AdminAuthorityService adminAuthorityService;

    @Autowired
    private AuditService auditService;
    /**
     * 查询管理员详情
     * @param id 管理员
     * @return
     */
    @GetMapping(value = "{id:\\d+}")
    @ApiOperation(value = "查看管理员的详情")
    public Admin detail(@PathVariable("id") Long id){
        Admin resultAdmin = adminService.findOne(id);
        return Admin.toDetail(resultAdmin);
    }

    /**
     * 获取管理员列表，根据查询条件
     * @param queryAdmin 查询条件
     * @return
     */
    @PostMapping("/query")
    @ApiOperation(value = "获取管理员列表，根据查询条件")
    public Result all(@Valid @RequestBody QueryAdmin queryAdmin){
        Page<Admin> queryAdmins = adminService.findByQueryAdmin(queryAdmin);
        List<Admin> resultAdmins = Admin.toListInIndex(queryAdmins.getContent());
        return Result.init(resultAdmins,queryAdmin);
    }

    /**
     * 添加管理员
     * @param registerAdmin 管理员信息
     */
    @PostMapping("/add/{id:\\d+}")
    @ApiOperation(value = "添加管理员")
    public Admin add(@PathVariable("id") Long id,@Valid @RequestBody RegisterAdmin registerAdmin, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Admin saveAdmin = RegisterAdmin.toAdmin(registerAdmin,id);
        Admin resultAdmin = adminService.save(saveAdmin);
        return Admin.toDetail(resultAdmin);
    }

    /**
     * 查看管理员拥有的所有权限
     * @return
     */
    @PostMapping("/authority/query")
    @ApiOperation(value = "管理员权限列表")
    public Result adminAuthority(@RequestBody QueryAuthority queryAuthority){
        Page<Authority> queryAuthorities = authorityService.findByAdmin(queryAuthority.getAdminId(),queryAuthority);
        return Result.init(Authority.toDetail(queryAuthorities.getContent()),queryAuthority);
    }

    /**
     * 移除管理员权限
     * @param ids 管理员权限列表
     */
    @PostMapping("/authority/delete")
    @ApiOperation(value = "移除管理员权限")
    public void removeAdminAuthority(@Valid @RequestBody LongIds ids, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        boolean delIsSuccess = adminAuthorityService.deleteByAuthorityIds(ids.getIds(),ids.getId());
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);}
    }

    /**
     * 管理员离职
     * @param id 管理员编号
     */
    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "管理员离职")
    public void removeAdmin(@PathVariable("id") Long id){
        boolean isLeave = adminService.leaveOffice(id);
        if (!isLeave){
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
    }

    /**
     * 修改管理员信息
     * @param modifyAdmin 管理员信息
     * @param bindingResult
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改管理员信息")
    public Admin update(@Valid @RequestBody ModifyAdmin modifyAdmin,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Admin admin = adminService.findOne(modifyAdmin.getId());
        if (admin == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
        Admin result = adminService.save(ModifyAdmin.toAdmin(admin,modifyAdmin));
        return Admin.toDetail(result);
    }

    /**
     * 查看管理员审核列表
     * @param queryAudit 审核查询条件
     * @return
     */
    @PostMapping("/audit/query")
    @ApiOperation(value = "获取管理员审核列表")
    public Result getAuditByAdmin(@RequestBody QueryAudit queryAudit){
        if (queryAudit.getAdminId() == null || queryAudit.getAdminId() <= 0){
            throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_ADMIN_FAILED);
        }
        Page<Audit> queryAudits = auditService.findByQueryAudit(queryAudit);
        return Result.init(Audit.toListInIndex(queryAudits.getContent()),queryAudit);
    }

    /**
     * 查看管理员的审核详情信息
     * @param id 审核编号
     * @return
     */
    @GetMapping("/audit/detail/{aid:\\d+}/{id:\\d+}")
    @ApiOperation(value = "查看我的审核详情信息")
    public Audit auditDetail(@PathVariable("id") Long id,@PathVariable("aid") String aid){
        Audit result = auditService.findOne(aid);
        if (!id.equals(result.getAdminId())){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        return Audit.toDetail(result);
    }

}
