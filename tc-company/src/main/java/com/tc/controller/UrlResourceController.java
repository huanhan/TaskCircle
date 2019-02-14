package com.tc.controller;

import com.tc.db.entity.AuthorityResource;
import com.tc.db.entity.Resource;
import com.tc.db.entity.User;
import com.tc.dto.LongIds;
import com.tc.dto.Result;
import com.tc.dto.Show;
import com.tc.dto.authority.QueryAR;
import com.tc.dto.enums.QueryEnum;
import com.tc.dto.enums.ResourceState;
import com.tc.dto.resource.ModifyResource;
import com.tc.dto.resource.QueryResource;
import com.tc.dto.resource.SelectAddResource;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.AuthorityResourceService;
import com.tc.service.ResourceService;
import com.tc.until.ControllerHelper;
import com.tc.until.ListUtils;
import com.tc.until.PageRequest;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
    private AuthorityResourceService authorityResourceService;

    @PostMapping("/urls/db")
    @ApiOperation(value = "获取数据库中的url资源")
    public Result allUrl(@RequestBody QueryResource queryResource){
        queryResource.setSort(new Sort(
                new Sort.Order(Sort.Direction.DESC,Resource.SORT_CLASSNAME),
                new Sort.Order(Sort.Direction.DESC,Resource.SORT_CREATETIME)
        ));
        Page<Resource> list = resourceService.findByQuery(queryResource);
        list.forEach(Resource::initResource);
        //获取系统中的资源
        List<Resource> controllerList = ControllerHelper.allUrl(applicationContext);
        //与系统资源对比判断哪些有异常（比如原有资源被移动，被删除等）
        list.forEach(dbr -> controllerList.forEach(clr -> {
            if (dbr.getPath().equals(clr.getPath()) ||
                    dbr.getType().equals(clr.getType())){
                dbr.setResourceState(ResourceState.NORMAL.getState());
                dbr.setNormal(true);
            }
        }));
        return Result.init(list,queryResource.append(list.getTotalElements(),(long) list.getTotalPages()));
    }

    @PostMapping("/urls/controller")
    @ApiOperation(value = "获取控制器中所有的url资源")
    public Result allUrl(@RequestBody PageRequest myPage){
        List<Resource> list = resourceService.findAll();
        //获取系统中的资源
        List<Resource> urls = ControllerHelper.allUrl(applicationContext);
        Page pageList;
        if (list == null || list.size() <= 0) {
            pageList = ListUtils.paging(urls, myPage.getPageNumber(), myPage.getPageSize());
        }else {
            //去除数据库中已添加的资源
            for (Resource has : list) {
                urls.removeIf(resource ->
                        resource.getPath().equals(has.getPath()) &&
                                resource.getType().equals(has.getType())
                );
            }
            pageList = ListUtils.paging(urls, myPage.getPageNumber(), myPage.getPageSize());
            System.out.println("getTotalElements:" + pageList.getTotalElements() + "\n" +
                    "getTotalPages" + pageList.getTotalPages() + "\n" +
                    "getSize" + pageList.getSize() + "\n" +
                    "getNumber" + pageList.getNumber() + "\n" +
                    "getNumberOfElements" + pageList.getNumberOfElements());
        }
        return Result.init(pageList.getContent(),myPage.append(pageList.getTotalElements(),(long)pageList.getTotalPages()));
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
    @GetMapping("/add/all")
    @ApiOperation(value = "一键添加所有未添加的资源")
    public void addAll(HttpServletRequest request){
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
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
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
        List<Resource> queryList = selectAddResource.getList();
        List<Resource> newQueryList = new ArrayList<>();
        //获取系统中的资源
        List<Resource> controllerList = ControllerHelper.allUrl(applicationContext);
        //根据用户传入的资源标识获取系统中的资源
        queryList.forEach(qrl -> controllerList.forEach(
                crl -> {
                    if (qrl.getPath().equals(crl.getPath()) &&
                            qrl.getType().equals(crl.getType())){
                        crl.setCreation(new User(selectAddResource.getCreation()));
                        newQueryList.add(crl);
                    }
                }
            )
        );
        if (newQueryList.size() <= 0){
            throw new DBException(StringResourceCenter.VALIDATOR_INSERT_FAILED);
        }
        //获取数据库中的资源
        List<Resource> dblList = resourceService.findAll();
        //从查询中，移除数据库中已经存在的
        dblList.forEach(
            drl -> newQueryList.removeIf(
                qrl -> qrl.getPath().equals(drl.getPath()) &&
                qrl.getType().equals(drl.getType())
            )
        );
        if (newQueryList.size() <= 0){
            throw new DBException(StringResourceCenter.VALIDATOR_INSERT_ABNORMAL);
        }

        //保存用户选择的资源
        List<Resource> result = resourceService.save(newQueryList);

        //判断保存是否成功
        if (result == null || result.size() <= 0){
            throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
        }else {
            if (result.size() != queryList.size()){
                throw new DBException(StringResourceCenter.DB_INSERT_ABNORMAL);
            }
        }

    }

    /**
     * 修改资源
     * @param modifyResource
     * @param bindingresult
     * @return
     */
    @PutMapping
    @ApiOperation("根据资源标识来修改资源")
    public Resource update(@Valid @RequestBody ModifyResource modifyResource, BindingResult bindingresult){
        if (bindingresult.hasErrors()){
            throw new ValidException(bindingresult.getFieldErrors());
        }
        Resource oldResource = resourceService.findOne(modifyResource.getId());
        if (oldResource == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        Resource updateResource = modifyResource.toResource(oldResource);

        //判断是否发生更新
        if (!modifyResource.isModify()){
            throw new ValidException(StringResourceCenter.VALIDATOR_UPDATE_ABNORMAL);
        }

        Resource result = resourceService.update(updateResource);
        result.initResource();
        return result;
    }


    /**
     * 删除单个资源
     * @param id
     */
    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "根据资源标识删除资源")
    public void delete(@PathVariable("id") Long id){
        boolean delIsSuccess = resourceService.deleteById(id);
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_FAILED);}
    }

    /**
     * 删除多个资源
     * @param ids
     */
    @PostMapping("/delete/select")
    @ApiOperation(value = "根据资源标识数组删除资源")
    public void delete(@Valid @RequestBody LongIds ids,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        boolean delIsSuccess = resourceService.deleteByIds(ids.getIds());
        if (!delIsSuccess){throw new DBException(StringResourceCenter.DB_DELETE_FAILED);}
    }

    /**
     * 获取权限列表
     * @param queryAR 资源查询信息
     * @return
     */
    @PostMapping("/authority/query/{id:\\d+}")
    @ApiOperation(value = "获取使用指定url资源的权限列表")
    public Result urlResourceAuthority(@PathVariable("id") Long id,@RequestBody QueryAR queryAR){
        queryAR.setResourceId(id);
        Page<AuthorityResource> queryResult = authorityResourceService.findByQuery(queryAR);
        if (queryResult.getContent() == null || queryResult.getContent().size() <= 0){
            throw new DBException(StringResourceCenter.DB_QUERY_ABNORMAL);
        }
        List<Show> resultList = AuthorityResource.toShows(queryResult.getContent(),QueryEnum.AUTHORITY);
        return Result.init(resultList,queryAR.append(queryResult.getTotalElements(),(long) queryResult.getTotalPages()));
    }

    /**
     * 从资源详情的使用者列表中，移除一个或多个使用者
     * @param ids 权限编号列表
     */
    @PostMapping("/authority/delete")
    @ApiOperation(value = "从资源详情的使用者列表中移除使用者")
    public void removeAuthority(@Valid @RequestBody LongIds ids, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        boolean success = authorityResourceService.deleteByIds(ids);
        if (!success) {
            throw new DBException(StringResourceCenter.DB_DELETE_FAILED);
        }
    }




}
