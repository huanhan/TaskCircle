package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.dto.Result;
import com.tc.dto.admin.QueryAdmin;
import com.tc.dto.authority.*;
import com.tc.dto.LongIds;
import com.tc.dto.Show;
import com.tc.dto.enums.QueryEnum;
import com.tc.dto.enums.ResourceState;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.*;
import com.tc.until.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Cyg
 *
 * 权限控制器，用来控制权限与资源之间的映射关系
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/authority")
public class AuthorityController {

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthorityResourceService authorityResourceService;

    @Autowired
    private AdminAuthorityService adminAuthorityService;

    @Autowired
    private UserAuthorityService userAuthorityService;

    @Autowired
    private UserService userService;

    /**
     * 查询权限详情
     * @param id 权限编号
     * @return
     */
    @GetMapping(value = "{id:\\d+}")
    @ApiOperation(value = "查看权限的详情")
    public Authority detail(@PathVariable("id") Long id){

        Authority authority = authorityService.findOne(id);
        return Authority.toShows(authority);

    }

    @PostMapping(value = "/all/query")
    @ApiOperation(value = "在权限与资源关系管理界面中，获取一次性获取所有权限，所有资源与两者之间的关联关系")
    public Result all(@RequestBody QueryAuthority queryAuthority){
        //根据条件查询权限列表
        List<Authority> authorities = authorityService.findByQueryAuthority(queryAuthority);
        //获取所有数据库中的资源(需要全部展示出来)
        List<Resource> resources = resourceService.findAll();
        if (!resources.isEmpty()) {
            //获取系统中的资源
            List<Resource> controllerList = ControllerHelper.allUrl(applicationContext);
            //与系统资源对比判断哪些有异常（比如原有资源被移动，被删除等）
            resources.forEach(dbr -> controllerList.forEach(clr -> {
                if (dbr.getPath().equals(clr.getPath()) ||
                        dbr.getType().equals(clr.getType())) {
                    dbr.setResourceState(ResourceState.NORMAL.getState());
                    dbr.setNormal(true);
                }
            }));
            //去除异常的资源
            resources.removeIf(resource -> !resource.isNormal());
        }
        //获取权限与资源之间的关系
        List<AuthorityResource> authorityResources = authorityResourceService.findByKeys(
                Resource.toKeys(resources),
                Authority.toKeys(authorities)
        );
        //将获取的数据结果转成界面需要的内容
        List<Show> showResources = TranstionHelper.toShowByResources(resources);
        List<Show> showAuthorities = TranstionHelper.toShowByAuthorities(authorities);
        List<AuthorityResource> autResIds = TranstionHelper.toAuthorityResourceID(authorityResources);
        return Result.init(AutResRelation.init(showResources,showAuthorities,autResIds),queryAuthority);
    }

    @PostMapping("/aut/query")
    @ApiOperation(value = "在权限与资源关系管理界面中，获取所有权限")
    public Result allAuthority(@RequestBody QueryAuthority queryAuthority){
        //根据条件查询权限列表
        List<Authority> authorities = authorityService.findByQueryAuthority(queryAuthority);
        return Result.init(TranstionHelper.toShowByAuthorities(authorities),queryAuthority);
    }

    @GetMapping("/aut/{id:\\d+}")
    @ApiOperation(value = "在权限与资源关系管理界面中，获取权限对应的资源")
    public List<Long> getResourcesByAuthority(@PathVariable("id") Long id){
        List<AuthorityResource> authorityResources = authorityResourceService.findByAuthorityID(id);
        return AuthorityResource.toKey(authorityResources,QueryEnum.RESOURCE);
    }

    /**
     * 添加权限基本信息
     * @param addAuthority
     * @param result
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加权限基本信息")
    public Authority add(@Valid @RequestBody AddAuthority addAuthority,
                        BindingResult result){
        if (result.hasErrors()){
            throw new ValidException(result.getFieldErrors());
        }
        User user = userService.findOne(addAuthority.getCreation());
        Authority authority = addAuthority.toAuthority(addAuthority);
        authority.setAdmin(user.getAdmin());
        Authority ref = authorityService.save(authority);
        if (ref.getId() == null || ref.getId() <= 0) {
            throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
        }
        return Authority.toShows(ref);
    }


    /**
     * 修改权限信息
     * @param modifyAuthority
     * @param result
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改权限基本信息")
    public Authority update(@Valid @RequestBody ModifyAuthority modifyAuthority, BindingResult result){
        if (result.hasErrors()){
            throw new ValidException(result.getFieldErrors());
        }
        Authority authority = authorityService.update(modifyAuthority.toAuthority(modifyAuthority));
        return Authority.toShows(authority);
    }

    /**
     * 删除单个权限
     * @param id
     */
    @DeleteMapping("{id:\\d+}")
    @ApiOperation(value = "删除单个权限")
    public void delete(@PathVariable("id") Long id){
        boolean delIsSuccess = authorityService.deleteById(id);
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);}
    }

    /**
     * 删除多个权限
     * @param ids
     */
    @PostMapping("/select/delete")
    @ApiOperation(value = "删除多个权限")
    public void delete(@Valid @RequestBody LongIds ids, BindingResult result){
        if (result.hasErrors()){
            throw new ValidException(result.getFieldErrors());
        }
        boolean delIsSuccess = authorityService.deleteByIds(ids.getIds());
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);}
    }

    /**
     * 设置权限对应资源
     * 该功能模块，通过url中的权限编号获取数据库中所有对应的资源
     * 根据输入的资源编号列表与权限编号对应的资源来判断哪些需要移除，哪些需要新增
     * @param aid 权限编号
     * @param ids 资源编号列表
     */
    @PostMapping("/{aid:\\d+}")
    @ApiOperation(value = "设置权限对应的资源")
    public void setAuthorityResource(@PathVariable("aid") Long aid, @RequestBody LongIds ids){

        List<AuthorityResource> news = AuthorityOPClassify.create(aid,ids.getIds());
        List<AuthorityResource> oldies = authorityResourceService.findByAuthorityID(aid);

        //将用户选中的内容进行比较
        AuthorityOPClassify authorityOPClassify = AuthorityOPClassify.init(news,oldies);

        //保存新的
        if (!authorityOPClassify.getInAdd().isEmpty()){
            authorityResourceService.save(authorityOPClassify.getInAdd());
        }

        //删除旧的
        if (!authorityOPClassify.getInDelete().isEmpty()){
            authorityResourceService.deleteByResourceIds(AuthorityOPClassify.toResourceLong(authorityOPClassify.getInDelete()),aid);
        }

    }

    /**
     * 获取权限使用者列表（管理员）
     * @param queryAdmin 权限编号
     * @return
     */
    @PostMapping("/admin/query/{id:\\d+}")
    @ApiOperation(value = "获取权限的使用者列表（管理员）")
    public Result authorityAdmins(@RequestBody QueryAdmin queryAdmin, @PathVariable("id") Long id){
        Page<Admin> admins = adminService.findByQueryAdminAndAuthority(queryAdmin,id);
        return Result.init(Admin.toShows(admins.getContent()),queryAdmin);
    }

    /**
     * 移除一个或多个使用者（管理员）
     * @param ids 使用者编号列表
     */
    @PostMapping("/admin/select/delete")
    @ApiOperation(value = "移除一个或多个使用者（管理员）")
    public void removeAdmins(@Valid @RequestBody LongIds ids,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        boolean delIsSuccess = adminAuthorityService.deleteByIds(ids);
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_FAILED);}
    }

    /**
     * 获取权限使用者列表（用户）
     * @param id 权限编号
     * @return
     */
    @GetMapping("/user/{id:\\d+}")
    @ApiOperation(value = "获取权限的使用者列表（用户分类）")
    public Result authorityUsers(@PathVariable("id") Long id){
        List<UserAuthority> queryList = userAuthorityService.findByAuthorityId(id);
        return Result.init(UserAuthority.toShows(queryList));
    }

    /**
     * 从权限详情的使用者列表中，移除一个或多个使用者（用户）
     * @param ids 使用者编号列表
     */
    @PostMapping("/user/select/delete")
    @ApiOperation(value = "移除一个或多个使用者（用户分类）")
    public void removeUsers(@Valid @RequestBody RemoveUser ids,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        boolean delIsSuccess = userAuthorityService.deleteByIds(ids);
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_FAILED);}
    }
}
