package com.tc.security.core.properties;

public class SessionProperties {

    /**
     * 用户在一个系统中最大session数
     */
    private int maximumSession = 1;

    /**
     * 达到最大数时，是否阻止新的登陆请求
     */
    private boolean maxSessionsPreventsLogin;


    /**
     * session失效的跳转地址
     */
    private String sessionInvalidUrl = SecurityConstants.DEFAULT_SESSION_INVALID_URL;

    public int getMaximumSession() {
        return maximumSession;
    }

    public void setMaximumSession(int maximumSession) {
        this.maximumSession = maximumSession;
    }

    public boolean isMaxSessionsPreventsLogin() {
        return maxSessionsPreventsLogin;
    }

    public void setMaxSessionsPreventsLogin(boolean maxSessionsPreventsLogin) {
        this.maxSessionsPreventsLogin = maxSessionsPreventsLogin;
    }

    public String getSessionInvalidUrl() {
        return sessionInvalidUrl;
    }

    public void setSessionInvalidUrl(String sessionInvalidUrl) {
        this.sessionInvalidUrl = sessionInvalidUrl;
    }
}
