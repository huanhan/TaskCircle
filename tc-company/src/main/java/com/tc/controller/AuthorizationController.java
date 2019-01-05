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
    public Result allByUser(){
        //所有用户类别
        List<UserCategory> queryUserCategories = UserCategory.all();
        //所有权限
        List<Authority> queryAuthorities = authorityService.findAll();
        //两者关系
        List<UserAuthority> queryUserAuthorities = userAuthorityService.findAll();

        //存放结果内容
        List<Show> userCategories = new ArrayList<>();
        List<Show> authorities = new ArrayList<>();
        List<UserAuthority> userAuthorities = new ArrayList<>();

        if (!ListUtils.isEmpty(queryUserCategories)){
            userCategories = UserCategory.toShows(queryUserCategories);
        }

        if (!ListUtils.isEmpty(queryAuthorities)){
            authorities = Authority.toShows(queryAuthorities);
        }

        if (!ListUtils.isEmpty(queryUserAuthorities)){
            userAuthorities = UserAuthority.reset(queryUserAuthorities);
        }

        return Result.init(UserAutRelation.init(userCategories,authorities,userAuthorities));
    }

    /**
     * 通过勾选与取消选中的形式来设置用户类别与权限的映射关系
     * @param name 类别名
     * @param ids 权限列表
     */
    @PostMapping("/users/{name}")
    @ApiOperation(value = "设置用户类别对应的权限")
    public void setUserCategoryAuthority(@PathVariable("name") String name, @RequestBody LongIds ids){
        UserCategory userCategory = UserCategory.findByName(name);
        if (userCategory == null){
            throw new ValidException(StringResourceCenter.VALIDATOR_QUERY_FAILED);
        }
        List<UserAuthority> news = AUserOPClassify.create(userCategory,ids.getIds());
        List<UserAuthority> oldies = userAuthorityService.findByUserCategory(userCategory);

        //将用户选中的内容进行比较
        AUserOPClassify aUserOPClassify = AUserOPClassify.init(news,oldies);

        //保存新的
        if (!aUserOPClassify.getInAdd().isEmpty()){
            userAuthorityService.save(aUserOPClassify.getInAdd());
        }

        //删除旧的
        if (!aUserOPClassify.getInDelete().isEmpty()){
            userAuthorityService.deleteByAuthorityIds(AUserOPClassify.toAuthorityLong(aUserOPClassify.getInDelete()),userCategory);
        }
    }

    /**
     * 获取所有管理员，所有权限与两者之间的关联关系
     * @return
     */
    @PostMapping("/admin/query")
    @ApiOperation(value = "在管理员与权限关系管理界面中，一次性获取所有管理员，所有权限与两者之间的关联关系")
    public Result allByAdmin(@RequestBody QueryAdmin queryAdmin){

        //根据查询条件获取的管理员
        List<Admin> queryAdmins = adminService.findByQueryAdminAndAuthorityState(queryAdmin);

        //所有权限
        List<Authority> queryAuthorities = authorityService.findAll();

        //存放结果内容
        List<Show> admins;
        List<Show> authorities;
        List<AdminAuthority> adminAuthorities = new ArrayList<>();

        if (!ListUtils.isEmpty(queryAdmins) && !ListUtils.isEmpty(queryAuthorities)) {
            //两者关系
            List<AdminAuthority> queryUserAuthorities = adminAuthorityService.findByKeys(
                    Admin.toKeys(queryAdmins),
                    Authority.toKeys(queryAuthorities)
            );
            adminAuthorities = AdminAuthority.reset(queryUserAuthorities);

        }

        admins = Admin.toShows(queryAdmins);
        authorities = Authority.toShows(queryAuthorities);

        return Result.init(AdminAutRelation.init(admins,authorities,adminAuthorities),queryAdmin);
    }

    /**
     * 通过勾选与取消选中的形式来设置管理员与权限的映射关系
     * @param id 管理员编号
     * @param ids 权限列表
     */
    @PostMapping("/admin/{id:\\d+}")
    @ApiOperation(value = "设置管理员对应的权限")
    public void setAdminAuthority(@PathVariable("id") Long id, @RequestBody LongIds ids){

        List<AdminAuthority> news = AAdminOPClassify.create(id,ids.getIds());
        List<AdminAuthority> oldies = adminAuthorityService.findByAdminId(id);

        //将用户选中的内容进行比较
        AAdminOPClassify aUserOPClassify = AAdminOPClassify.init(news,oldies);

        //保存新的
        if (!aUserOPClassify.getInAdd().isEmpty()){
            adminAuthorityService.save(aUserOPClassify.getInAdd());
        }

        //删除旧的
        if (!aUserOPClassify.getInDelete().isEmpty()){
            adminAuthorityService.deleteByAuthorityIds(AAdminOPClassify.toAuthorityLong(aUserOPClassify.getInDelete()),id);
        }
    }
}
