package com.tc.security;

import com.tc.security.core.authorize.AuthorizeConfigProvider;
import com.tc.security.core.properties.SecurityConstants;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;

@Component
@Order(Integer.MAX_VALUE)
public class DemoAuthorizeConfigProvider implements AuthorizeConfigProvider {
    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {

            config.antMatchers(
                    "/user/register",
                    "/login",
                    "/app/user/register",
                    "/app/user/*/code/Image"
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
                    .permitAll()
            .anyRequest().access("@rbacService.hasPermission(request,authentication)");

    }
}
