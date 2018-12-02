package com.tc.filter;

import com.tc.security.core.properties.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
public class TokenFilter implements Filter {

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
        if (!StringUtils.isEmpty(bearerToken)){
            String token = StringUtils.substringAfter(bearerToken,"bearer ");
//            Claims claims = Jwts.parser().setSigningKey(securityProperties.getOauth2().getJwtSigningKey().getBytes("UTF-8"))
//                    .parseClaimsJws(token).getBody();
//            claims.get("user_name");
            //redisTemplate.
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
