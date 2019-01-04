package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.dto.Result;
import com.tc.dto.authority.*;
import com.tc.dto.Ids;
import com.tc.dto.Show;
import com.tc.dto.enums.ResourceState;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.AuthorityResourceService;
import com.tc.service.AuthorityService;
import com.tc.service.ResourceService;
import com.tc.service.UserService;
import com.tc.until.*;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.Valid;
import java.util.ArrayList;
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
    private AuthorityResourceService authorityResourceService;

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
        return Authority.reset(authority);

    }

    @PostMapping(value = "/query")
    @ApiOperation(value = "在权限与资源关系管理界面中，获取一次性获取所有权限，所有资源与两者之间的关联关系")
    public Result all(@RequestBody QueryAuthority queryAuthority){
        //根据条件查询权限列表
        List<Authority> authorities = authorityService.findByQuery(queryAuthority);
        if (ArrayUtils.isEmpty(authorities.toArray())){
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
        //获取所有数据库中的资源(需要全部展示出来)
        List<Resource> resources = resourceService.findAll();
        //获取系统中的资源
        List<Resource> controllerList = ControllerHelper.allUrl(applicationContext);
        //与系统资源对比判断哪些有异常（比如原有资源被移动，被删除等）
        resources.forEach(dbr -> controllerList.forEach(clr -> {
            if (dbr.getPath().equals(clr.getPath()) ||
                    dbr.getType().equals(clr.getType())){
                dbr.setResourceState(ResourceState.NORMAL.getState());
                dbr.setNormal(true);
            }
        }));
        //去除异常的资源
        resources.removeIf(resource -> !resource.isNormal());
        //获取权限与资源之间的关系
        List<AuthorityResource> authorityResources = authorityResourceService.findByKeys(
                Resource.toKeys(resources),
                Authority.toKeys(authorities)
        );
        List<Show> showResources = TranstionHelper.toShowByResources(resources);
        List<Show> showAuthorities = TranstionHelper.toShowByAuthorities(authorities);
        List<AuthorityResource> autResIds = TranstionHelper.toAuthorityResourceID(authorityResources);
        return Result.init(AutResRelation.init(showResources,showAuthorities,autResIds),queryAuthority);
    }

    @GetMapping("/aut")
    @ApiOperation(value = "在权限与资源关系管理界面中，获取所有权限")
    public List<Show> allAuthority(){
        List<Authority> authorities = authorityService.findAll();
        return TranstionHelper.toShowByAuthorities(authorities);
    }

    @GetMapping("/aut/{id:\\d+}")
    @ApiOperation(value = "在权限与资源关系管理界面中，获取权限对应的资源")
    public List<Long> getResourcesByAuthority(@PathVariable("id") Long id){
        List<Long> resourceIds = new ArrayList<>();
        List<AuthorityResource> authorityResources = authorityResourceService.findByAuthorityID(id);
        if (!resourceIds.isEmpty()){
            authorityResources.forEach(authorityResource -> resourceIds.add(authorityResource.getResource().getId()));
        }
        return resourceIds;
    }

    /**
     * 添加权限基本信息
     * @param addAuthority
     * @param authentication
     * @param result
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加权限基本信息")
    public Authority add(@Valid @RequestBody AddAuthority addAuthority,
                        Authentication authentication,
                        BindingResult result){
        if (result.hasErrors()){
            throw new ValidException(result.getFieldErrors());
        }
        User user = userService.getUserByUsername(authentication.getPrincipal().toString());
        Authority authority = addAuthority.toAuthority(addAuthority);
        authority.setAdmin(user.getAdmin());
        Authority ref = authorityService.save(authority);
        ref.setAdmin(new Admin(ref.getAdmin().getUser().getId()));
        if (ref.getId() == null || ref.getId() <= 0) {
            throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
        }


        return ref;
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
        initAuthority(authority);
        return authority;
    }

    /**
     * 删除单个权限
     * @param id
     */
    @DeleteMapping
    @ApiOperation(value = "删除单个权限")
    public void delete(@RequestBody Long id){
        boolean delIsSuccess = authorityService.deleteById(id);
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_FAILED);}
    }

    /**
     * 删除多个权限
     * @param ids
     */
    @DeleteMapping("/all")
    @ApiOperation(value = "删除多个权限")
    public void delete(@Valid @RequestBody Ids ids, BindingResult result){
        if (result.hasErrors()){
            throw new ValidException(result.getFieldErrors());
        }
        boolean delIsSuccess = authorityService.deleteByIds(ids.getlIds());
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_FAILED);}
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
    public void setAuthorityResource(@PathVariable("aid") Long aid, @RequestBody Ids ids){
        List<AuthorityResource> news = AuthorityOPClassify.create(aid,ids.getlIds());
        List<AuthorityResource> oldies = authorityResourceService.findByAuthorityID(aid);

        Authority authority = authorityService.findOne(aid);

        //将用户选中的内容进行比较
        AuthorityOPClassify authorityOPClassify = AuthorityOPClassify.init(news,oldies);

        //保存新的
        if (!authorityOPClassify.getInAdd().isEmpty()){
            List<Resource> resources = resourceService.findByIds(authorityOPClassify.getInAddId());
            authorityResourceService.save(AuthorityOPClassify.toAuthorityResource(authority,resources));
        }

        //删除旧的
        if (!authorityOPClassify.getInDelete().isEmpty()){
            authorityResourceService.deleteByResourceIds(AuthorityOPClassify.toResourceLong(authorityOPClassify.getInDelete()));
        }

    }

    /**
     * 获取权限使用者列表（管理员）
     * @param id 权限编号
     * @return
     */
    @GetMapping("/admin/{id:\\d+}")
    @ApiOperation(value = "获取权限的使用者列表")
    public List<Show> authorityAdmins(@PathVariable("id") Long id){
        return new ArrayList<>();
    }

    /**
     * 从权限详情的使用者列表中，移除一个或多个使用者（管理员）
     * @param id 权限编号
     * @param ids 使用者编号列表
     */
    @DeleteMapping("/admin/{id:\\d+}")
    public void removeAdmins(@PathVariable("id") Long id,@RequestBody Ids ids){

    }

    /**
     * 获取权限使用者列表（用户）
     * @param id 权限编号
     * @return
     */
    @GetMapping("/user/{id:\\d+}")
    @ApiOperation(value = "获取权限的使用者列表")
    public List<Show> authorityUsers(@PathVariable("id") Long id){
        return new ArrayList<>();
    }

    /**
     * 从权限详情的使用者列表中，移除一个或多个使用者（用户）
     * @param id 权限编号
     * @param ids 使用者编号列表
     */
    @DeleteMapping("/user/{id:\\d+}")
    public void removeUsers(@PathVariable("id") Long id,@RequestBody Ids ids){

    }

    /**
     * 初始化要返回的权限信息
     * @param authority
     */
    private void initAuthority(Authority authority) {
        if (authority != null){
            authority.setAdmin(null);
            if (!authority.getAuthorityResources().isEmpty()){
                authority.getAuthorityResources().forEach(ar -> {
                    ar.setAuthority(null);
                    ar.setResource(new Resource(ar.getResource().getId(),ar.getResource().getName()));
                });
            }
        }
    }
}
