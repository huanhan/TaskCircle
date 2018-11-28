package com.tc.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码处理器，封装不同校验码的处理逻辑
 *
 *
 */
public interface ValidateCodeProcessor {
    /**
     * 创建校验码
     * @param request
     * @throws Exception
     */
    void create(ServletWebRequest request) throws Exception;

    /**
     * 校验
     * @param request
     */
    void validate(ServletWebRequest request);
}
