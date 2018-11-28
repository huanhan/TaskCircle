package com.tc.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeRepository {

    /**
     * 保存验证码
     * @param request
     * @param code
     * @param validateCodeType
     */
    void save(ServletWebRequest request,ValidateCode code,ValidateCodeType validateCodeType);

    /**
     * 获取验证码
     * @param request
     * @param codeType
     * @return
     */
    ValidateCode get(ServletWebRequest request,ValidateCodeType codeType);

    /**
     * 移出验证码
     * @param request
     * @param codeType
     */
    void remove(ServletWebRequest request,ValidateCodeType codeType);

}
