package com.tc.security;

import com.tc.security.core.authorize.AuthorizeConfigProvider;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

@Component
@Order(Integer.MAX_VALUE)
public class DemoAuthorizeConfigProvider implements AuthorizeConfigProvider {
    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {

        config.antMatchers(
                "/user/register"
        ).permitAll()
                .antMatchers("/swagger-ui.html","/doc.html")
                .permitAll()
                .antMatchers("/swagger-resources/**")
                .permitAll()
                .antMatchers("/images/**")
                .permitAll()
                .antMatchers("/webjars/**")
                .permitAll()
                .antMatchers("/v2/api-docs")
                .permitAll()
                .antMatchers("/configuration/ui")
                .permitAll()
                .antMatchers("/configuration/security")
                .permitAll();

    }
}
