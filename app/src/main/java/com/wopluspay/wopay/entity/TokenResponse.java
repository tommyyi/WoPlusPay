package com.wopluspay.wopay.entity;

/**
 * Created by Administrator on 2016/9/13.
 */
public class TokenResponse
{
    /**
     * resultCode : 0
     * resultDescription : success
     * token : cc44a3a69786d38cefcc900d48be56262ddab487
     * tokenExpireIn : 2592000
     */

    private String resultCode;
    private String resultDescription;
    private String token;
    private String tokenExpireIn;

    public String getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(String resultCode)
    {
        this.resultCode = resultCode;
    }

    public String getResultDescription()
    {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription)
    {
        this.resultDescription = resultDescription;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getTokenExpireIn()
    {
        return tokenExpireIn;
    }

    public void setTokenExpireIn(String tokenExpireIn)
    {
        this.tokenExpireIn = tokenExpireIn;
    }
}
