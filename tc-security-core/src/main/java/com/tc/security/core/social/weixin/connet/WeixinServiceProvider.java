package com.tc.security.core.social.weixin.connet;

import com.tc.security.core.social.qq.api.QQ;
import com.tc.security.core.social.qq.api.QQImpl;
import com.tc.security.core.social.qq.connet.QQOAuth2Template;
import com.tc.security.core.social.weixin.api.Weixin;
import com.tc.security.core.social.weixin.api.WeixinImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

public class WeixinServiceProvider extends AbstractOAuth2ServiceProvider<Weixin> {

    private String appId;

    /**
     * 将用户导向QQ的认证服务器的URL地址，并获取授权码
     */
    private static final String URL_AUTHORIZE = "https://open.weixin.qq.com/connect/qrconnect";


    /**
     * 应用根据获取的授权码到这个URL中去获取token
     */
    private static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";

    public WeixinServiceProvider(String appId, String appSecret) {
        super(new WeixinOAuth2Template(appId,appSecret,URL_AUTHORIZE,URL_ACCESS_TOKEN));
        this.appId = appId;
    }

    @Override
    public Weixin getApi(String accessToken) {
        return new WeixinImpl(accessToken);
    }
}
