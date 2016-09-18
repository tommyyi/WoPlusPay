package com.wopluspay;

/**
 * Created by Administrator on 2016/9/12.
 */
public class Paycode
{
    /**
     * price : 600.0
     * product : 金币包
     * number : 001
     * outTradeNo : 193892832
     * token : 47d449031a13616e95be52d6f27f8ed8d93587c7
     */

    private String price;
    private String product;
    private String number;

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    private String token;

    public String getOutTradeNo()
    {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo)
    {
        this.outTradeNo = outTradeNo;
    }

    private String outTradeNo;

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getProduct()
    {
        return product;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }
}
