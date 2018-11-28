package com.tc.security.core.social.qq.connet;

import com.tc.security.core.social.qq.api.QQ;
import com.tc.security.core.social.qq.api.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    private String appId;

    /**
     * 将用户导向QQ的认证服务器的URL地址，并获取授权码
     */
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";


    /**
     * 应用根据获取的授权码到这个URL中去获取token
     */
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    public QQServiceProvider(String appId,String appSecret) {
        super(new QQOAuth2Template(appId,appSecret,URL_AUTHORIZE,URL_ACCESS_TOKEN));
        this.appId = appId;
    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken,appId);
    }
}
