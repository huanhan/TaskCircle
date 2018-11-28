package com.tc.security.core.properties;

import org.springframework.boot.autoconfigure.social.SocialProperties;

public class WeixinProperties extends SocialProperties {

    /**
     * 第三方id，用来决定发起第三方登陆的Url，默认是weixin
     */
    private String providerId = "weixin";

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
