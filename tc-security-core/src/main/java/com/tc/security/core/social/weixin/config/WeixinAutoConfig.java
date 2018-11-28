package com.tc.security.core.social.weixin.config;

import com.tc.security.core.properties.SecurityProperties;
import com.tc.security.core.properties.WeixinProperties;
import com.tc.security.core.social.weixin.connet.WeixinConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

/**
 * ConditionalOnProperty:保证用户添加了该配置项后才启用该配置
 * @author Cyg
 */
@Configuration
@ConditionalOnProperty(prefix = "tc.security.social.weixin",name = "app-id")
public class WeixinAutoConfig extends SocialAutoConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;


    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        WeixinProperties weixinConfig = securityProperties.getSocial().getWeixin();

        return new WeixinConnectionFactory(weixinConfig.getProviderId(),weixinConfig.getAppId(),
                weixinConfig.getAppSecret());
    }
}
