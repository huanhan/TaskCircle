package com.tc.controller;

import com.tc.db.entity.Resource;
import com.tc.db.entity.User;
import com.tc.dto.Ids;
import com.tc.dto.MyPage;
import com.tc.dto.Result;
import com.tc.dto.Show;
import com.tc.dto.enums.ResourceState;
import com.tc.dto.resource.*;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.ResourceService;
import com.tc.service.UserService;
import com.tc.until.ControllerHelper;
import com.tc.until.ListHelper;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Cyg
 * 路径资源控制器
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/resource")
public class UrlResourceController {

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private UserService userService;

    @PostMapping("/urls/db")
    @ApiOperation(value = "获取数据库中的url资源")
    public Result allUrl(@RequestBody QueryResource queryResource){
        queryResource.setSort(new Sort(
                new Sort.Order(Sort.Direction.DESC,Resource.SORT_CLASSNAME),
                new Sort.Order(Sort.Direction.DESC,Resource.SORT_CREATETIME)
        ));
        List<Resource> list = resourceService.findByQuery(queryResource);
        List<ResourceDetail> detailList = new ArrayList<>();
        list.forEach(resource -> detailList.add(ResourceDetail.byResource(resource)));
        //获取系统中的资源
        List<Resource> controllerList = ControllerHelper.allUrl(applicationContext);
        //与系统资源对比判断哪些有异常（比如原有资源被移动，被删除等）
        detailList.forEach(dbr -> controllerList.forEach(clr -> {
            if (dbr.getPath().equals(clr.getPath()) ||
                    dbr.getType().equals(clr.getType())){
                dbr.setResourceState(ResourceState.NORMAL.getState());
                dbr.setNormal(true);
            }
        }));
        return Result.init(detailList,queryResource);
    }

    @PostMapping("/urls/controller")
    @ApiOperation(value = "获取控制器中所有的url资源")
    public Result allUrl(@RequestBody MyPage myPage){
        List<Resource> list = resourceService.findAll();
        //获取系统中的资源
        List<Resource> urls = ControllerHelper.allUrl(applicationContext);
        if (list == null || list.size() <= 0) {
            return Result.init(urls);
        }
        //去除数据库中已添加的资源
        for (Resource has : list) {
            urls.removeIf(resource ->
                    resource.getPath().equals(has.getPath()) &&
                            resource.getType().equals(has.getType())
            );
        }
        Page pageList = new ListHelper<Resource>().paging(urls, myPage.getPageIndex(), myPage.getPageSize());
        return Result.init(pageList);
    }

    @GetMapping(value = "{id:\\d+}")
    @ApiOperation(value = "获取数据库中资源数据详情")
    public Resource detail(@PathVariable("id") Long id){

        boolean exist = false;
        Resource resource = resourceService.findOne(id);

        if (resource == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        //获取系统中的资源
        List<Resource> controllerList = ControllerHelper.allUrl(applicationContext);
        for (Resource res :
                controllerList) {
            exist = res.getPath().equals(resource.getPath()) &&
                    res.getType().equals(resource.getType());
            if (exist) {
                break;
            }
        }

        if (exist){
            resource.initResource();
            return resource;
        } else {
            throw new DBException(StringResourceCenter.DB_RESOURCE_ABNORMAL);
        }

    }

    /**
     * 一键添加所有未添加的资源
     */
    @PostMapping("/add/all")
    @ApiOperation(value = "一键添加所有未添加的资源")
    public void addAll(@RequestBody Long id){
        List<Resource> list = resourceService.findAll();
        //获取系统中的资源
        List<Resource> urls = ControllerHelper.allUrl(applicationContext);
        if (list != null && list.size() > 0) {
            //去除数据库中已添加的资源
            for (Resource has : list) {
                urls.removeIf(resource ->
                        resource.getPath().equals(has.getPath()) &&
                                resource.getType().equals(has.getType())
                );
            }
            //去除不允许添加的内容
            urls.removeIf(resource -> (resource.getName() == null || resource.getName().length() <= 0));
        }
        //遍历设置需要添加的内容
        urls.forEach(resource -> {
            resource.setCreation(new User(id));
        });

        List<Resource> result = resourceService.save(urls);
        if (result == null || result.size() <= 0){
            throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
        }else {
            if (result.size() != urls.size()){
                throw new DBException(StringResourceCenter.DB_INSERT_ABNORMAL);
            }
        }
    }

    /**
     * 选择添加所有未添加的资源
     */
    @PostMapping("/add/select")
    @ApiOperation(value = "选择添加所有未添加的资源")
    public void addSelect(@Valid @RequestBody SelectAddResource selectAddResource,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Iterator<Resource> list = selectAddResource.getList().iterator();
        List<Resource> controllerList = ControllerHelper.allUrl(applicationContext);




    }


    /**
     * 添加路由到数据库
     * @param addResource
     * @param authentication
     * @param result
     * @return
     */
    @PostMapping
    public Resource add(@Valid @RequestBody AddResource addResource,
                        Authentication authentication,
                    BindingResult result){
        if (result.hasErrors()){
            throw new ValidException(result.getFieldErrors());
        }
        User user = userService.getUserByUsername(authentication.getPrincipal().toString());
        Resource resource = addResource.toResource(addResource);
        resource.setCreation(user);
        Resource ref = resourceService.save(resource);
        ref.setCreation(new User(ref.getCreation().getId()));
        if (ref.getId() == null || ref.getId() <= 0) {
            throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
        }
        return ref;
    }


    /**
     * 修改资源
     * @param modifyResource
     * @param result
     * @return
     */
    @PutMapping
    public Resource update(@Valid @RequestBody ModifyResource modifyResource, BindingResult result){
        if (result.hasErrors()){
            throw new ValidException(result.getFieldErrors());
        }
        Resource resource = resourceService.update(modifyResource.toResource(modifyResource));
        resource.initResource();
        return resource;
    }


    /**
     * 删除单个资源
     * @param id
     */
    @DeleteMapping
    public void delete(@RequestBody Long id){
        boolean delIsSuccess = resourceService.deleteById(id);
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_FAILED);}
    }

    /**
     * 删除多个资源
     * @param ids
     */
    @DeleteMapping("/all")
    public void delete(@RequestBody Ids ids){
        boolean delIsSuccess = resourceService.deleteByIds(ids.getlIds());
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_FAILED);}
    }

    /**
     * 获取权限列表
     * @param id url资源编号
     * @return
     */
    @GetMapping("/authority/{id:\\d+}")
    @ApiOperation(value = "获取使用指定url资源的权限列表")
    public List<Show> urlResourceAuthority(@PathVariable("id") Long id){
        return new ArrayList<>();
    }

    /**
     * 从权限详情的使用者列表中，移除一个或多个使用者
     * @param id url资源编号
     * @param ids 权限编号列表
     */
    @DeleteMapping("/authority/{id:\\d+}")
    public void removeAuthority(@PathVariable("id") Long id,@RequestBody Ids ids){

    }




}
