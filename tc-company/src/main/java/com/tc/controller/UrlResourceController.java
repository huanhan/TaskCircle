package com.tc.controller;

import com.tc.db.entity.Resource;
import com.tc.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resource")
public class UrlResourceController {

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/urls")
    public List<Map<String,String>> getAllUrl(
            @RequestParam(name = "hasTrue",required = false,defaultValue = "false") Boolean hasTrue){

        List<Resource> list = resourceService.findAll(new Sort(new Sort.Order(Sort.Direction.DESC,Resource.SORT_CREATETIME)));
        List<Map<String,String>> urls = getAllUrl();
        List<Map<String,String>> nonUrls = new ArrayList<>();
        List<Map<String,String>> hasUrls = new ArrayList<>();

        if (list == null || list.size() <= 0){
            return urls;
        }

        for (Map<String, String> url : urls) {
            for (Resource aList : list) {
                if (url.get("className").equals(aList.getPath())) {
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
     * 获取controller中所有的url
     * @return
     */
    private List<Map<String,String>> getAllUrl(){
        AbstractHandlerMethodMapping<RequestMappingInfo> objHandlerMethodMapping = (AbstractHandlerMethodMapping<RequestMappingInfo>)applicationContext.getBean("requestMappingHandlerMapping");

        Map<RequestMappingInfo,HandlerMethod> map = objHandlerMethodMapping.getHandlerMethods();

        List<Map<String,String>> list = new ArrayList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m :
                map.entrySet()) {
            Map<String,String> map1 = new HashMap<>();
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            String className = method.getMethod().getDeclaringClass().getName();
            if (StringUtils.substringMatch(className,0,"com.tc.controller")) {
                PatternsRequestCondition p = info.getPatternsCondition();
                RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
                for (int i = 0; i < p.getPatterns().size(); i++) {
                    map1.put("className", className);
                    map1.put("method", method.getMethod().getName());
                    map1.put("url",p.getPatterns().toArray()[i].toString());
                    map1.put("type", methodsCondition.getMethods().toArray()[i].toString());
                }
                list.add(map1);
            }
        }

        return list;
    }
}
