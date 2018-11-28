package com.tc.security.browser.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tc.security.core.properties.SecurityProperties;
import com.tc.security.core.support.SimpleResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TcLogoutSuccessHandler implements LogoutSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(TcLogoutSuccessHandler.class);




    private SecurityProperties securityProperties;

    private ObjectMapper objectMapper = new ObjectMapper();


    public TcLogoutSuccessHandler(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        logger.info("退出成功");

        String signOutUrl = securityProperties.getBrowser().getSignOutUrl();

        if (StringUtils.isBlank(signOutUrl)){
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse("退出成功")));
        } else {
            httpServletResponse.sendRedirect(signOutUrl);
        }
    }
}
