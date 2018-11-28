package com.tc.security.core.authentication.mobile;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //以下是身份认证的逻辑

        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken)authentication;

        UserDetails user = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());

        if (user == null){
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user,user.getAuthorities());

        authenticationResult.setDetails(authentication.getDetails());

        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authenticate) {
        //以下是用来提供给Manager判断是不是我们自己的类
        //判断传进来的类是不是我们自己定义的Token
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authenticate);
    }
}
