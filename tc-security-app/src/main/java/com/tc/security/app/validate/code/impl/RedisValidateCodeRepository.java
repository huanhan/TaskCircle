package com.tc.security.app.validate.code.impl;

import com.tc.security.core.validate.code.ValidateCode;
import com.tc.security.core.validate.code.ValidateCodeException;
import com.tc.security.core.validate.code.ValidateCodeRepository;
import com.tc.security.core.validate.code.ValidateCodeType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.transaction.Transactional;
import java.util.concurrent.TimeUnit;

@Component
public class RedisValidateCodeRepository implements ValidateCodeRepository {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;



    @Override
    public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType) {
        redisTemplate.opsForValue().set(buildKey(request,validateCodeType),code,30,TimeUnit.MINUTES);
    }

    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeType codeType) {
        Object validateCode = redisTemplate.opsForValue().get(buildKey(request,codeType));
        if (validateCode == null){
            return null;
        }
        return (ValidateCode)validateCode;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void remove(ServletWebRequest request, ValidateCodeType codeType) {
        redisTemplate.delete(buildKey(request,codeType));
    }

    private String buildKey(ServletWebRequest request,ValidateCodeType type){
        String deviceId = request.getHeader("deviceId");
        if (StringUtils.isBlank(deviceId)){
            throw new ValidateCodeException("请在请求头中携带deviceId参数");
        }
        return "code:" + type.toString().toUpperCase() + ":" + deviceId;
    }
}
