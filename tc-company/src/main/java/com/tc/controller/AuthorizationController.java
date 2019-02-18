package com.tc.controller;

import com.tc.db.entity.Admin;
import com.tc.db.entity.AdminAuthority;
import com.tc.db.entity.Authority;
import com.tc.db.entity.UserAuthority;
import com.tc.db.enums.UserCategory;
import com.tc.dto.LongIds;
import com.tc.dto.Result;
import com.tc.dto.Show;
import com.tc.dto.admin.QueryAdmin;
import com.tc.dto.authorization.AdminAutRelation;
import com.tc.dto.authorization.UserAutRelation;
import com.tc.dto.trans.Trans;
import com.tc.dto.trans.TransID;
import com.tc.dto.trans.TransOP;
import com.tc.exception.ValidException;
import com.tc.service.AdminAuthorityService;
import com.tc.service.AdminService;
import com.tc.service.AuthorityService;
import com.tc.service.UserAuthorityService;
import com.tc.until.AAdminOPClassify;
import com.tc.until.AUserOPClassify;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * 授权控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/authorization")
public class AuthorizationController {

    @Autowired
    private AdminAuthorityService adminAuthorityService;

    @Autowired
    private UserAuthorityService userAuthorityService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private AdminService adminService;

    /**
     * 获取所有用户类别，所有权限与两者之间的关联关系
     * @return
     */
    @GetMapping("/all/users")
    @ApiOperation(value = "在用户与权限关系管理界面中，一次性获取所有用户类别，所有权限与两者之间的关联关系")
    public Result allByUser(HttpServletRequest request){
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        //所有用户类别
        List<UserCategory> queryUserCategories = UserCategory.allByUser();
        //所有权限
        List<Authority> queryAuthorities = authorityService.findAll();

        //存放结果内容
        List<Trans> userCategories;
        List<TransOP> authorities;
        List<Trans> userAuthorities = new ArrayList<>();
        if (ListUtils.isNotEmpty(queryUserCategories) && ListUtils.isNotEmpty(queryAuthorities)){
            //两者关系
            List<UserAuthority> queryUserAuthorities = userAuthorityService.findAll();
            userAuthorities = UserAuthority.toTrans(queryUserAuthorities);
        }

        userCategories = UserCategory.toTrans(queryUserCategories);
        authorities = Authority.toTrans(queryAuthorities,id);

        return Result.init(UserAutRelation.init(userCategories,authorities,userAuthorities));
    }

    /**
     * 通过勾选与取消选中的形式来设置用户类别与权限的映射关系
     * @param name 类别名
     * @param ids 权限列表
     */
    @PostMapping("/users/{name}")
    @ApiOperation(value = "设置用户类别对应的权限")
    public Result setUserCategoryAuthority(@PathVariable("name") String name, @RequestBody LongIds ids){
        UserCategory userCategory = UserCategory.findByName(name);
        if (userCategory == null){
            throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_FAILED);
        }
        List<UserAuthority> news = AUserOPClassify.create(userCategory,ids.getIds());
        List<UserAuthority> oldies = userAuthorityService.findByUserCategory(userCategory);

        //将用户选中的内容进行比较
        AUserOPClassify aUserOPClassify = AUserOPClassify.init(news,oldies);

        //保存新的，并且删除旧的
        userAuthorityService.saveNewsAndRemoveOldes(aUserOPClassify.getInAdd(),AUserOPClassify.toAuthorityLong(aUserOPClassify.getInDelete()),userCategory);

        //重新获取两者关系
        List<UserAuthority> result = aUserOPClassify.getBestNews();

        return Result.init(UserAuthority.toTrans(result));
    }

    /**
     * 获取所有管理员，所有权限与两者之间的关联关系
     * @return
     */
    @PostMapping("/admin/query")
    @ApiOperation(value = "在管理员与权限关系管理界面中，一次性获取所有管理员，所有权限与两者之间的关联关系")
    public Result allByAdmin(HttpServletRequest request,@RequestBody QueryAdmin queryAdmin){
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        queryAdmin.setCreation(id);
        //根据查询条件获取的管理员
        List<Admin> queryAdmins = adminService.findByQueryAdminAndAuthorityState(queryAdmin);

        //所有权限
        List<Authority> queryAuthorities = authorityService.findAll();

        //存放结果内容
        List<Trans> admins;
        List<TransOP> authorities;
        List<TransID> adminAuthorities = new ArrayList<>();

        if (!ListUtils.isEmpty(queryAdmins) && !ListUtils.isEmpty(queryAuthorities)) {
            //两者关系
            List<AdminAuthority> queryUserAuthorities = adminAuthorityService.findByKeys(
                    Admin.toKeys(queryAdmins),
                    Authority.toKeys(queryAuthorities)
            );
            adminAuthorities = AdminAuthority.toTransID(queryUserAuthorities);
        }

        admins = Admin.toTrans(queryAdmins);
        authorities = Authority.toTrans(queryAuthorities,id);

        return Result.init(AdminAutRelation.init(admins,authorities,adminAuthorities),queryAdmin);
    }

    /**
     * 通过勾选与取消选中的形式来设置管理员与权限的映射关系
     * @param id 管理员编号
     * @param ids 权限列表
     */
    @PostMapping("/admin/{id:\\d+}")
    @ApiOperation(value = "设置管理员对应的权限")
    public Result setAdminAuthority(@PathVariable("id") Long id, @RequestBody LongIds ids){

        List<AdminAuthority> news = AAdminOPClassify.create(id,ids.getIds());
        List<AdminAuthority> oldies = adminAuthorityService.findByAdminId(id);

        //将用户选中的内容进行比较
        AAdminOPClassify aUserOPClassify = AAdminOPClassify.init(news,oldies);

        //删除旧的，保存新的
        adminAuthorityService.saveNewsAndRemoveOld(aUserOPClassify.getInAdd(),AAdminOPClassify.toAuthorityLong(aUserOPClassify.getInDelete()),id);

        //重新获取两者关系
        List<AdminAuthority> result = aUserOPClassify.getBestNews();

        return Result.init(AdminAuthority.toTransID(result));
    }
}
