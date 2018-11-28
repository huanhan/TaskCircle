package com.tc.security.core.social.weixin.connet;
import org.springframework.social.oauth2.AccessGrant;

public class WeixinAccessGrant extends AccessGrant {

    private String openId;

    public WeixinAccessGrant() {
        super("");
    }

    public WeixinAccessGrant(String access_token, String scope, String refresh_token, Long expires_in) {
        super(access_token, scope, refresh_token, expires_in);
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
