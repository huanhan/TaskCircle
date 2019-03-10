package com.tc.security.app;

import com.tc.security.app.jwt.OptionsFilter;
import com.tc.security.app.social.openid.OpenIdAuthenticationSecurityConfig;
import com.tc.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.tc.security.core.authorize.AuthorizeConfigManager;
import com.tc.security.core.properties.SecurityConstants;
import com.tc.security.core.properties.SecurityProperties;
import com.tc.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.http.HttpServletRequest;


@Configuration
@EnableResourceServer
public class TcResourceServerConfig extends ResourceServerConfigurerAdapter {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    protected AuthenticationSuccessHandler tcAuthenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler tcAuthenticationFailureHandler;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private SpringSocialConfigurer tcSocialSecurityConfig;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;


    @Bean
    public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(ApplicationContext applicationContext) {

        OAuth2WebSecurityExpressionHandler expressionHandler = new OAuth2WebSecurityExpressionHandler();

        expressionHandler.setApplicationContext(applicationContext);

        return expressionHandler;

    }


    @Autowired
    private OAuth2WebSecurityExpressionHandler expressionHandler;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {


        resources.expressionHandler(expressionHandler);

    }


    @Override
    public void configure(HttpSecurity http) throws Exception {




        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(tcAuthenticationSuccessHandler)
                .failureHandler(tcAuthenticationFailureHandler);


        /**
         * apply：执行传入的配置
         */
        http.apply(validateCodeSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(tcSocialSecurityConfig)
                .and()
                .apply(openIdAuthenticationSecurityConfig)
                .and()
//                .csrf()
//                .disable()
        ;



        http.cors()
                .and()
                .csrf()
                .disable().authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();

        authorizeConfigManager.config(http.authorizeRequests());
    }


    private boolean hasOption(HttpServletRequest request){
        if (antPathMatcher.match("/oauth/**",request.getRequestURI())){
            return true;
        }else {
            return CorsUtils.isPreFlightRequest(request);
        }
    }
}
