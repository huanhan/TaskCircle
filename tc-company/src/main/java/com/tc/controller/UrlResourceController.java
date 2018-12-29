package com.tc.controller;

import com.tc.db.entity.Authority;
import com.tc.db.entity.Resource;
import com.tc.db.entity.User;
import com.tc.dto.Ids;
import com.tc.dto.Show;
import com.tc.dto.resource.AddResource;
import com.tc.dto.resource.ModifyResource;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.ResourceService;
import com.tc.service.UserService;
import com.tc.validator.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import javax.persistence.Id;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/urls")
    public List<Resource> getAllUrl(
            @RequestParam(name = "hasTrue",required = false,defaultValue = "false") Boolean hasTrue){

        List<Resource> list = resourceService.findAll(
                new Sort(
                        new Sort.Order(Sort.Direction.DESC,Resource.SORT_CLASSNAME),
                        new Sort.Order(Sort.Direction.DESC,Resource.SORT_CREATETIME)
                )
        );
        List<Resource> urls = getAllUrl();
        List<Resource> nonUrls = new ArrayList<>();
        List<Resource> hasUrls = new ArrayList<>();

        if (list == null || list.size() <= 0){
            return urls;
        }

        for (Resource url : urls) {
            for (Resource aList : list) {
                if (url.getPath().equals(aList.getPath())) {
                    hasUrls.add(url);
                } else {
                    nonUrls.add(url);
                }
            }
        }

        if (hasTrue){
            return hasUrls;
        }else {
            return nonUrls;
        }
    }

    @GetMapping(value = "{id:\\d+}")
    public Resource detail(@PathVariable("id") Long id){
        Resource resource = resourceService.findOne(id);
        initResource(resource);
        return resource;
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
        initResource(resource);
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
        boolean delIsSuccess = resourceService.deleteByIds(ids.getIds());
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

    /**
     * 获取controller中所有的url
     * @return
     */
    private List<Resource> getAllUrl(){
        AbstractHandlerMethodMapping<RequestMappingInfo> objHandlerMethodMapping =
                (AbstractHandlerMethodMapping<RequestMappingInfo>)
                        applicationContext.getBean("requestMappingHandlerMapping");

        Map<RequestMappingInfo,HandlerMethod> map = objHandlerMethodMapping.getHandlerMethods();

        List<Resource> list = new ArrayList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m :
                map.entrySet()) {
            Resource resource = new Resource();
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            String className = method.getMethod().getDeclaringClass().getName();
            if (StringUtils.substringMatch(className,0,"com.tc.controller")) {
                PatternsRequestCondition p = info.getPatternsCondition();
                RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
                for (int i = 0; i < p.getPatterns().size(); i++) {
                    resource.setClassName(className);
                    resource.setMethod(method.getMethod().getName());
                    resource.setPath(p.getPatterns().toArray()[i].toString());
                    resource.setType(methodsCondition.getMethods().toArray()[i].toString());
                }
                list.add(resource);
            }
        }

        return list;
    }

    private void initResource(Resource resource) {
        if (resource != null){
            resource.setCreation(new User(resource.getCreation().getId(), resource.getCreation().getName()));

            if (!resource.getAuthorityResources().isEmpty()){
                resource.getAuthorityResources().forEach(ar -> {
                    ar.setAuthority(new Authority(ar.getAuthority().getId(),ar.getAuthority().getName()));
                    ar.setResource(null);
                });
            }
        }
    }
}
