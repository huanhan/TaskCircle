package com.tc.filter;

import com.tc.exception.ValidException;
import com.tc.security.MyJwtTokenEnhacer;
import com.tc.security.core.properties.SecurityProperties;
import com.tc.until.StringResourceCenter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
public class TokenFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String bearerToken = ((HttpServletRequest)servletRequest).getHeader("Authorization");
        logger.info("Authorization：" + bearerToken);
        if (!StringUtils.isEmpty(bearerToken)){
            String token = StringUtils.substringAfter(bearerToken,"bearer ");
            logger.info("token：" + token);
            if (!StringUtils.isEmpty(token)) {
                Jws<Claims> Jwt = null;
                try {
                    Jwt = Jwts.parser().setSigningKey(securityProperties.getOauth2().getJwtSigningKey().getBytes("UTF-8"))
                            .parseClaimsJws(token);
                } catch (Exception ignored) {
                    logger.info("错误的JWT：" + ignored.getMessage());
                    throw new ValidException("错误的JWT：" + ignored.getMessage());
                }

                Claims claims = Jwt.getBody();
                servletRequest.setAttribute(StringResourceCenter.USER_ID, claims.get(StringResourceCenter.USER_ID));

            }
            //redisTemplate.
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
