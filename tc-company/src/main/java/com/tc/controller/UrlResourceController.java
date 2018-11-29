package com.tc.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/urls")
    public List<Map<String,String>> getAllUrl(@RequestParam(name = "hasTrue",required = false,value = "false") Boolean hasTrue){

        return getAllUrl();
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
