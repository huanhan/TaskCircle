package com.tc.security;

import com.tc.db.entity.User;
import com.tc.dto.user.LoginUser;
import com.tc.exception.ValidException;
import com.tc.security.app.authentication.TcAuthenticationSuccessHandler;
import com.tc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("jwtTokenEnhancer")
public class MyJwtTokenEnhacer implements TokenEnhancer {

    private Logger logger = LoggerFactory.getLogger(MyJwtTokenEnhacer.class);

    @Autowired
    private UserService userService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication authentication) {
        Map<String,Object> info = new HashMap<>();


        LoginUser account = (LoginUser)authentication.getPrincipal();
        info.put("userId",account.getUserId());

        ((DefaultOAuth2AccessToken)oAuth2AccessToken).setAdditionalInformation(info);
        return oAuth2AccessToken;
    }
}
