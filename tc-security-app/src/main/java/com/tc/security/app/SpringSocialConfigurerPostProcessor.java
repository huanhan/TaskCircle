package com.tc.security.app;

import com.tc.security.core.social.TcSpringSocialConfigurer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class SpringSocialConfigurerPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (StringUtils.equals(beanName,"tcSocialSecurityConfig")){
            TcSpringSocialConfigurer configurer = (TcSpringSocialConfigurer)bean;
            configurer.signupUrl("/social/signUp");
            return configurer;
        }

        return bean;
    }
}
