package com.tc.controller;

import com.tc.db.entity.Admin;
import com.tc.db.entity.Audit;
import com.tc.db.entity.Authority;
import com.tc.db.entity.User;
import com.tc.db.enums.AdminState;
import com.tc.db.enums.AuditState;
import com.tc.db.enums.AuditType;
import com.tc.dto.LongIds;
import com.tc.dto.Result;
import com.tc.dto.admin.*;
import com.tc.dto.audit.AuditResult;
import com.tc.dto.audit.QueryAudit;
import com.tc.dto.authority.QueryAuthority;
import com.tc.dto.trans.Trans;
import com.tc.dto.trans.TransStates;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.*;
import com.tc.until.FloatHelper;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public Result all(HttpServletRequest request, @Valid @RequestBody QueryAdmin queryAdmin){
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        Page<Admin> queryAdmins = adminService.findByQueryAdmin(queryAdmin);
        List<Admin> resultAdmins = Admin.toListInIndex(queryAdmins.getContent(),id);
        return Result.init(new TransStates(AdminState.toList(),resultAdmins),queryAdmin.append(queryAdmins.getTotalElements(),(long)queryAdmins.getTotalPages()));
    }

    /**
     * 添加管理员
     * @param registerAdmin 管理员信息
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加管理员")
    public Admin add(HttpServletRequest request, @Valid @RequestBody RegisterAdmin registerAdmin, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
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
        if (FloatHelper.isNull(queryAuthority.getAdminId())){
            throw new ValidException("需要指定管理员");
        }
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
    public Admin removeAdmin(HttpServletRequest request,@PathVariable("id") Long id){
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        Admin leave = adminService.findOne(id);
        if (leave == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
        if (!leave.getCreateId().equals(me)){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        if (leave.getAdminState().equals(AdminState.LEAVE_FOOICE)){
            throw new ValidException("该管理员已经离职");
        }
        boolean isLeave = adminService.leaveOffice(id);
        if (!isLeave){
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }else {
            leave.setAdminState(AdminState.LEAVE_FOOICE);
            return Admin.toDetail(leave);
        }
    }

    /**
     * 管理员复职
     * @param id 管理员编号
     */
    @GetMapping("/reAdd/{id:\\d+}")
    @ApiOperation(value = "管理员复职")
    public Admin reAddAdmin(HttpServletRequest request,@PathVariable("id") Long id){
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        Admin admin = adminService.findOne(id);
        if (admin == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
        if (!admin.getCreateId().equals(me)){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        if (admin.getAdminState().equals(AdminState.ON_GUARD)){
            throw new ValidException("该管理员已经在岗");
        }
        boolean isLeave = adminService.reAddOffice(id);
        if (!isLeave){
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }else {
            admin.setAdminState(AdminState.ON_GUARD);
            return Admin.toDetail(admin);
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
    public Admin update(HttpServletRequest request,@Valid @RequestBody ModifyAdmin modifyAdmin,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        Admin admin = adminService.findOne(modifyAdmin.getId());
        if (admin == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
        if (!admin.getCreateId().equals(me)){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        Admin result = adminService.save(ModifyAdmin.toAdmin(admin,modifyAdmin));
        return Admin.toDetail(result);
    }

    /**
     * 查看管理员审核列表
     * @param queryAudit 审核查询条件
     * @return
     */
    @PostMapping("/audit/query/{id:\\d+}")
    @ApiOperation(value = "获取管理员审核列表")
    public Result getAuditByAdmin(@RequestBody QueryAudit queryAudit, @PathVariable("id") Long id){
        Admin admin = adminService.findOne(id);
        if (admin == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
        queryAudit.setAdminId(id);
        Page<Audit> queryAudits = auditService.findByQueryAudit(queryAudit);
        AuditResult result = new AuditResult(
                AuditState.toList(),
                AuditType.toList(),
                Audit.toListInIndex(queryAudits.getContent()),
                new Trans(admin.getUserId(),admin.getUser().toTitle()));
        return Result.init(result,queryAudit.append(queryAudits.getTotalElements(),(long)queryAudits.getTotalPages()));
    }

    /**
     * 查看管理员的审核详情信息
     * @param aid 管理员编号
     * @param id 审核编号
     * @return
     */
    @GetMapping("/audit/detail/{id}/{aid:\\d+}")
    @ApiOperation(value = "查看我的审核详情信息")
    public Audit auditDetail(@PathVariable("id") String id,@PathVariable("aid") Long aid){
        Audit result = auditService.findOne(id);
        if (!aid.equals(result.getAdminId())){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        return Audit.toDetail(result);
    }

}
