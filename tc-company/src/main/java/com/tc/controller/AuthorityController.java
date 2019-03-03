package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.db.enums.UserCategory;
import com.tc.dto.Result;
import com.tc.dto.trans.TransEnum;
import com.tc.dto.admin.QueryAdmin;
import com.tc.dto.authority.*;
import com.tc.dto.LongIds;
import com.tc.dto.Show;
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

import javax.servlet.http.HttpServletRequest;
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
        return Authority.toDetail(authority);

    }

    @PostMapping(value = "/all/query")
    @ApiOperation(value = "在权限与资源关系管理界面中，获取一次性获取所有权限，所有资源与两者之间的关联关系")
    public Result all(HttpServletRequest request,@RequestBody QueryAuthority queryAuthority) {
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        queryAuthority.setAdminId(id);
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
        return Result.init(AutResRelation.init(showResources, showAuthorities, autResIds), queryAuthority);
    }

    /**
     * 重新获取权限与资源之间的关系
     * @param request
     * @param queryAuthority
     * @return
     */
    @PostMapping(value = "/all/ar")
    @ApiOperation(value = "重新获取权限与资源之间的关系")
    public Result ar(HttpServletRequest request,@RequestBody QueryAuthority queryAuthority){
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        queryAuthority.setAdminId(id);
        //根据条件查询权限列表
        List<Authority> authorities = authorityService.findByQueryAuthority(queryAuthority);
        if (ListUtils.isNotEmpty(authorities)){
            List<AuthorityResource> result = authorityResourceService.findByAuthorityIds(Authority.toKeys(authorities));
            if (ListUtils.isNotEmpty(result)){
                return Result.init(TranstionHelper.toAuthorityResourceID(result));
            }else {
                throw new DBException("权限与资源关系：" + StringResourceCenter.DB_QUERY_ABNORMAL);
            }
        }else {
            throw new DBException("权限列表：" + StringResourceCenter.DB_QUERY_ABNORMAL);
        }
    }

    /**
     * 添加权限基本信息
     * @param addAuthority
     * @param result
     * @return
     */
    @PostMapping()
    @ApiOperation(value = "添加权限基本信息")
    public Authority add(HttpServletRequest request,@Valid @RequestBody AddAuthority addAuthority,
                         BindingResult result){
        if (result.hasErrors()){
            throw new ValidException(result.getFieldErrors());
        }
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        Authority authority = addAuthority.toAuthority(addAuthority);
        authority.setCreation(id);
        Authority ref = authorityService.save(authority);
        if (ref.getId() == null || ref.getId() <= 0) {
            throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
        }
        return Authority.toDetail(ref);
    }


    /**
     * 修改权限信息
     * @param modifyAuthority
     * @param result
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改权限基本信息")
    public Authority update(HttpServletRequest request,@Valid @RequestBody ModifyAuthority modifyAuthority, BindingResult result){
        if (result.hasErrors()){
            throw new ValidException(result.getFieldErrors());
        }
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        Authority query = authorityService.findOne(modifyAuthority.getId());
        if (query != null){
            if (query.getCreation().equals(id)){
                Authority authority = authorityService.update(modifyAuthority.toAuthority(modifyAuthority));
                return Authority.toDetail(authority);
            }else {
                throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
            }
        }else {
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
    }

    /**
     * 删除单个权限
     * @param id
     */
    @DeleteMapping("{id:\\d+}")
    @ApiOperation(value = "删除单个权限")
    public void delete(HttpServletRequest request,@PathVariable("id") Long id){
        Long creation = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        Authority query = authorityService.findOne(id);
        if (query != null){
            if (query.getCreation().equals(creation)){
                boolean delIsSuccess = authorityService.deleteById(id);
                if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);}
            }else {
                throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
            }
        }else {
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
    }

    /**
     * 删除多个权限
     * @param ids
     */
    @PostMapping("/select/delete")
    @ApiOperation(value = "删除多个权限")
    public void delete(HttpServletRequest request,@Valid @RequestBody LongIds ids, BindingResult result){
        if (result.hasErrors()){
            throw new ValidException(result.getFieldErrors());
        }
        Long creation = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        List<Authority> query = authorityService.findByIds(ids.getIds());
        if (ListUtils.isNotEmpty(query)){
            query.removeIf(authority -> !authority.getCreation().equals(creation));
            if (ListUtils.isNotEmpty(query)){
                boolean delIsSuccess = authorityService.deleteByIds(Authority.toKeys(query));
                if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);}
            }else {
                throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
            }
        }else {
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
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
    public void setAuthorityResource(HttpServletRequest request,@PathVariable("aid") Long aid, @RequestBody LongIds ids){
        Long creation = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        Authority query = authorityService.findOne(aid);
        if (query != null){
            if (query.getCreation().equals(creation)){
                List<AuthorityResource> news = AuthorityOPClassify.create(aid,ids.getIds());
                List<AuthorityResource> oldies = authorityResourceService.findByAuthorityID(aid);
                //将用户选中的内容进行比较
                AuthorityOPClassify authorityOPClassify = AuthorityOPClassify.init(news,oldies);
                if (authorityOPClassify == null){
                    throw new ValidException("无意义的内容");
                }
                //保存新的，并且删除旧的
                authorityResourceService.saveNewsAndRemoveOlds(
                        authorityOPClassify.getInAdd(),
                        AuthorityOPClassify.toResourceLong(authorityOPClassify.getInDelete()),aid);
            }else {
                throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
            }
        }else {
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
    }

    /**
     * 获取权限使用者列表（管理员）
     * @param queryAdmin 权限编号
     * @return
     */
    @PostMapping("/admin/query/{id:\\d+}")
    @ApiOperation(value = "获取权限的使用者列表（管理员）")
    public Result authorityAdmins(HttpServletRequest request,@RequestBody QueryAdmin queryAdmin, @PathVariable("id") Long id){
        Long creation = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        Authority query = authorityService.findOne(id);
        if (query != null) {

            Page<Admin> admins = adminService.findByQueryAdminAndAuthority(queryAdmin,id);
            List<Admin> notAuthAdmins = adminService.findByNotAuthority(id,creation);
            return Result.init(Admin.toTrans(admins.getContent(),notAuthAdmins,query,id),queryAdmin.append(admins.getTotalElements(),(long) admins.getTotalPages()));

        }else {
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }

    }

    /**
     * 移除一个或多个使用者（管理员）
     * @param ids 使用者编号列表
     */
    @PostMapping("/admin/select/delete")
    @ApiOperation(value = "移除一个或多个使用者（管理员）")
    public void removeAdmins(HttpServletRequest request,@Valid @RequestBody LongIds ids,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Long creation = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        Authority query = authorityService.findOne(ids.getId());
        if (query != null){
            if (query.getCreation().equals(creation)){
                boolean delIsSuccess = adminAuthorityService.deleteByIds(ids);
                if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_FAILED);}
            }else {
                throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
            }
        }else {
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }


    }

    /**
     * 添加一个或多个使用者（管理员）
     * @param request
     * @param ids
     * @param bindingResult
     */
    @PostMapping("/admin/select/add")
    @ApiOperation(value = "添加一个或多个使用者（管理员）")
    public Result addAdmins(HttpServletRequest request,@Valid @RequestBody LongIds ids,BindingResult bindingResult){
        Long creation = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        if (validate(request,ids.getId())){

            List<AdminAuthority> queryResult = adminAuthorityService.findBy(ids.getId(),ids.getIds());
            if (ListUtils.isNotEmpty(queryResult)){
                //遍历移除已存在该权限的管理员
                for (AdminAuthority adminAuthority : queryResult) {
                    ids.getIds().removeIf(id -> adminAuthority.getUserId().equals(id));
                }
            }
            if (ListUtils.isNotEmpty(ids.getIds())){
                List<Admin> admins = adminService.findByIds(ids.getIds());
                if (ListUtils.isNotEmpty(admins)){
                    //遍历移除不符合自身权限的管理员
                    admins.forEach(admin -> {
                        if (!admin.getCreateId().equals(creation)){
                            ids.getIds().removeIf(aLong -> aLong.equals(admin.getUserId()));
                        }
                    });
                }else {
                    throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
                }
            }
            if (ListUtils.isNotEmpty(ids.getIds())){
                List<AdminAuthority> result = adminAuthorityService.save(AdminAuthority.by(ids.getId(),ids.getIds()));
                if (ListUtils.isEmpty(result)) {
                    throw new DBException(StringResourceCenter.DB_DELETE_FAILED);
                }else {
                    return Result.init(AdminAuthority.toTrans(result));
                }
            }else {
                throw new ValidException("使用者已配置");
            }

        }else {
            return Result.init(new ArrayList<>());
        }
    }


    /**
     * 获取权限使用者列表（用户）
     * @param id 权限编号
     * @return
     */
    @GetMapping("/user/{id:\\d+}")
    @ApiOperation(value = "获取权限的使用者列表（用户分类）")
    public Result authorityUsers(@PathVariable("id") Long id){

        Authority query = authorityService.findOne(id);
        if (query != null) {

            List<UserAuthority> queryList = userAuthorityService.findByAuthorityId(id);
            List<TransEnum> categories = UserCategory.toList();
            if (ListUtils.isNotEmpty(queryList) && queryList.size() != categories.size()) {
                for (UserAuthority userAuthority : queryList) {
                    categories.removeIf(transEnum ->
                            transEnum.getKey().equals(userAuthority.getCategory().name()));
                }
            } else if (queryList.size() == categories.size()) {
                categories = new ArrayList<>();
            }
            return Result.init(UserAuthority.toShow(queryList, categories,query));

        }else {
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
    }

    /**
     * 从权限详情的使用者列表中，移除一个或多个使用者（用户）
     * @param ids 使用者编号列表
     */
    @PostMapping("/user/select/delete")
    @ApiOperation(value = "移除一个或多个使用者（用户分类）")
    public void removeUsers(HttpServletRequest request,@Valid @RequestBody RemoveUser ids,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        if (validate(request,ids.getId())){
            boolean delIsSuccess = userAuthorityService.deleteByIds(ids);
            if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_FAILED);}
        }
    }

    /**
     * 添加一个或多个使用者（用户分类）
     * @param request
     * @param ids
     * @param bindingResult
     */
    @PostMapping("/user/select/add")
    @ApiOperation(value = "添加一个或多个使用者（用户分类）")
    public Result addUsers(HttpServletRequest request,@Valid @RequestBody RemoveUser ids,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        if (validate(request,ids.getId())){

            List<UserAuthority> queryResult = userAuthorityService.findBy(ids.getId(),ids.getIds());
            if (ListUtils.isNotEmpty(queryResult)){
                for (UserAuthority userAuthority : queryResult) {
                    ids.getIds().removeIf(userCategory -> userAuthority.getCategory().equals(userCategory));
                }
            }
            if (ListUtils.isNotEmpty(ids.getIds())){
                List<UserAuthority> result = userAuthorityService.save(UserAuthority.by(ids.getId(),ids.getIds()));
                if (ListUtils.isEmpty(result)) {
                    throw new DBException(StringResourceCenter.DB_DELETE_FAILED);
                }else {
                    return Result.init(UserAuthority.toShows(result));
                }
            }else {
                throw new ValidException("使用者已配置");
            }

        }else {
            return Result.init(new ArrayList<>());
        }
    }

    private boolean validate(HttpServletRequest request,Long id){
        Long creation = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        Authority query = authorityService.findOne(id);
        if (query != null){
            if (query.getCreation().equals(creation)){
                return true;
            }else {
                throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
            }
        }else {
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
    }
}
