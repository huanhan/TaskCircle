package com.tc.security.core.authorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TcAuthorizeConfigManager implements AuthorizeConfigManager {
    
    
    @Autowired
    private List<AuthorizeConfigProvider> authorizeConfigProviders;
    
    
    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        authorizeConfigProviders.forEach(authorizeConfigProvider -> authorizeConfigProvider.config(config));
        //config.anyRequest().authenticated();
    }
    
    
    
}
