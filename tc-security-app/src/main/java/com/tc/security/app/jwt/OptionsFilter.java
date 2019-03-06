package com.tc.security.app.jwt;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OptionsFilter implements Filter {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        if (request.getMethod().equals("OPTIONS") && antPathMatcher.match("/oauth/**",request.getRequestURI())) {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
