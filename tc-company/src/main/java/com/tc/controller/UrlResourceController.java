package com.tc.controller;

import com.tc.db.entity.Resource;
import com.tc.db.entity.User;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.ResourceService;
import com.tc.service.UserService;
import com.tc.validator.until.StringResourceCenter;
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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        List<Resource> list = resourceService.findAll(new Sort(new Sort.Order(Sort.Direction.DESC,Resource.SORT_CREATETIME)));
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


    /**
     * 添加路由到数据库
     * @param resource
     * @param authentication
     * @param result
     * @return
     */
    @PostMapping
    public Resource add(@Valid @RequestBody Resource resource,
                    Authentication authentication,
                    BindingResult result){
        if (result.hasErrors()){
            throw new ValidException(result.getFieldErrors());
        }
        User user = userService.getUserByUsername((String)authentication.getPrincipal());
        resource.setCreation(user);
        Resource ref = resourceService.save(resource);
        if (ref == null || ref.getId() == null || ref.getId() <= 0) {
            throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
        }
        return ref;
    }


    @DeleteMapping(value = "/{id:\\d+}")
    public void delete(@PathVariable("id") Long id){

    }

    /**
     * 获取controller中所有的url
     * @return
     */
    private List<Resource> getAllUrl(){
        AbstractHandlerMethodMapping<RequestMappingInfo> objHandlerMethodMapping = (AbstractHandlerMethodMapping<RequestMappingInfo>)applicationContext.getBean("requestMappingHandlerMapping");

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
}
