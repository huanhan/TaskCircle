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

/**
 * 控制器辅助工具
 * @author Cyg
 */
public class ControllerHelper {


    public static final String REQUEST_HANDLER_MAPPGIN = "requestMappingHandlerMapping";

    /**
     * 获取controller中所有的url
     * @return
     */
    public static List<Resource> allUrl(WebApplicationContext applicationContext){
        //获取由Spring托管的指定Bean
        AbstractHandlerMethodMapping<RequestMappingInfo> objHandlerMethodMapping =
                (AbstractHandlerMethodMapping<RequestMappingInfo>)
                        applicationContext.getBean(REQUEST_HANDLER_MAPPGIN);
        //从Bean中获取所有的URL与对应方法的信息
        Map<RequestMappingInfo,HandlerMethod> map = objHandlerMethodMapping.getHandlerMethods();
        List<Resource> list = new ArrayList<>();
        //遍历map，获取仅获取自身需要的数据信息，并添加到list中
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m :
                map.entrySet()) {
            Resource resource = new Resource();
            //Request中的信息，包含路径
            RequestMappingInfo info = m.getKey();
            //Request对应的方法信息
            HandlerMethod method = m.getValue();
            //方法所属的类的名称
            String className = method.getMethod().getDeclaringClass().getName();
            //仅获取com.tc.controller下的所有类
            if (StringUtils.substringMatch(className,0,"com.tc.controller")) {
                //获取path信息
                PatternsRequestCondition p = info.getPatternsCondition();
                //获取type信息
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
