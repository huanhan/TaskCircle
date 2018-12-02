package com.tc.dto;

import java.io.Serializable;

public class TokenResult implements Serializable {
    /**
     * access_token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0ZXN0MTIzNCIsInNjb3BlIjpbImFsbCIsInJlYWQiLCJ3cml0ZSJdLCJjb21wYW55IjoidGMiLCJleHAiOjE1NDM2ODQzMDYsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4sUk9MRV9VU0VSIl0sImp0aSI6IjA0YmRhYTYxLTExZjUtNGVmMy1hYjIzLTJkNmJjM2ZiOTViZiIsImNsaWVudF9pZCI6InRjIn0.I2ngL_EnlMVflFzWK0DlNdvSnbZHz09cqYnMR8kg2g0
     * token_type : bearer
     * refresh_token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0ZXN0MTIzNCIsInNjb3BlIjpbImFsbCIsInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiIwNGJkYWE2MS0xMWY1LTRlZjMtYWIyMy0yZDZiYzNmYjk1YmYiLCJjb21wYW55IjoidGMiLCJleHAiOjE1NDYyNjkxMDYsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4sUk9MRV9VU0VSIl0sImp0aSI6IjViM2NmOTFiLWI3NjUtNDZkMC1iYmI1LTRhOTU1ZWU4N2Y4OCIsImNsaWVudF9pZCI6InRjIn0.Rw-0od3f5uFRiq1UQf6QdrXwoO91frQYHftl_WaZLY0
     * expires_in : 7199
     * scope : all read write
     * company : tc
     * jti : 04bdaa61-11f5-4ef3-ab23-2d6bc3fb95bf
     */

    public String accessToken;
    public String tokenType;
    public String refreshToken;
    public int expiresIn;
    public String scope;
    public String company;
    public String jti;




    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }
}
