package com.wopluspay.wopay.entity;

/**
 * Created by Administrator on 2016/9/13.
 */
public class RequestCodeSmsResponse
{
    /**
     * resultCode : 0
     * resultDescription : success
     */

    private int resultCode;
    private String resultDescription;

    public int getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(int resultCode)
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
}
