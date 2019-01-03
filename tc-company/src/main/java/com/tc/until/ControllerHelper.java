package com.tc.until;

import com.tc.db.entity.Resource;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControllerHelper {


    /**
     * 获取controller中所有的url
     * @return
     */
    public static List<Resource> allUrl(WebApplicationContext applicationContext){
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
                    Annotation annotation = method.getMethod().getAnnotation(ApiOperation.class);
                    if (annotation != null) {
                        String value = ((ApiOperation) annotation).value();
                        resource.setName(value);
                    }
                    resource.setPath(p.getPatterns().toArray()[i].toString());
                    resource.setType(methodsCondition.getMethods().toArray()[i].toString());
                }
                list.add(resource);
            }
        }

        return list;
    }

}
