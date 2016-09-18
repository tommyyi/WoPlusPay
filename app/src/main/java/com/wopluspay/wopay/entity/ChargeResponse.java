package com.wopluspay.wopay.entity;

/**
 * Created by Administrator on 2016/9/13.
 */
public class ChargeResponse
{
    /**
     * resultCode : 0
     * resultDescription : success
     * transactionId : d22a2746-fd25-4a4e-9f45-13b789367072
     */

    private int resultCode;
    private String resultDescription;
    private String transactionId;

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

    public String getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }
}
