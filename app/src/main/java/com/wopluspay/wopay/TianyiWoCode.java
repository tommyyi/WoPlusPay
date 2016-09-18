package com.wopluspay.wopay;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.wopluspay.Paycode;
import com.wopluspay.wopay.entity.RequestCodeSmsResponse;

public class TianyiWoCode
{

    public static final String POST_URL = "http://open.wo.com.cn/openapi/rpc/paymentcodesms/v1.0";

    public static int CONTENT_EMPTY = 0;


    public static int requestCodeSms(String phoneNumber, Paycode paycode, String token) throws Exception
    {
        // Post请求的url，与get不同的是不需要带参数
        URL postUrl = new URL(POST_URL);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        //打开读写属性，默认均为false
        connection.setDoOutput(true);
        connection.setDoInput(true);
        // 设置请求方式，默认为GET
        connection.setRequestMethod("POST");
        // Post 请求不能使用缓存
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        // 时间建议长点
        connection.setConnectTimeout(60000);
        connection.setReadTimeout(60000);
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");
        String head = "appKey=" + "\"" + "5bb3543058d042839bc910d2ad109ab0a2d15e88" + "\"" + ",token=" + "\"" + token + "\"" + "";
        connection.setRequestProperty("Authorization", head);
        // 要注意的是connection.getOutputStream()会隐含的进行调用 connect()，所以这里可以省略
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        //正文内容其实跟get的URL中'?'后的参数字符串一致
        HashMap<String, Object> cnts = new HashMap<String, Object>();
        //cnts.put("paymentType", 0);
        String outTradeNo = System.currentTimeMillis() + "";
        Log.e("outTradeNo",outTradeNo);
        paycode.setOutTradeNo(outTradeNo);
        cnts.put("outTradeNo", outTradeNo);
        cnts.put("subject", paycode.getProduct());
        cnts.put("paymentUser", phoneNumber);
        cnts.put("paymentAcount", "001");
        //cnts.put("description", paycode.getProduct());
        //cnts.put("price", paycode.getPrice());//0.01
        //cnts.put("quantity", 1);
        cnts.put("totalFee", Float.valueOf(paycode.getPrice()));
        //cnts.put("showUrl", "www.baidu.com");
        //cnts.put("timeStamp", "18600000001");
        String content = JSON.toJSONString(cnts);

        // DataOutputStream.writeBytes将字符串中的16位的 unicode字符以8位的字符形式写道流里面
        out.write(content.getBytes());
        out.flush();
        out.close(); // flush and close 
        int responseCode = connection.getResponseCode();

        String contentEncoding;
        contentEncoding = connection.getContentEncoding();
        System.out.println(" contentEncoding= " + contentEncoding);

        String contentType;
        contentType = connection.getContentType();
        System.out.println(" contentType= " + contentType);

        String responseMessage;
        responseMessage = connection.getResponseMessage();
        System.out.println(" responseMessage= " + responseMessage);
        System.out.println("responseCode:" + responseCode);

        String resultData = null;
        if (200 == responseCode || 201 == responseCode)
        {
            InputStream in = connection.getInputStream();
            resultData = getResponseResult(new InputStreamReader(in, "UTF-8"));
            Log.e("TAG",resultData);
            RequestCodeSmsResponse requestCodeSmsResponse = JSON.parseObject(resultData, RequestCodeSmsResponse.class);

            connection.disconnect();
            return requestCodeSmsResponse.getResultCode();
        }
        else if (CONTENT_EMPTY == connection.getContentLength())
        {
            resultData = "get response error,content is empty";
            connection.disconnect();
            return -1;
        }
        else
        {
            BufferedInputStream err = new BufferedInputStream(connection.getErrorStream());
            resultData = getResponseResult(new InputStreamReader(err, "UTF-8"));
            connection.disconnect();
            return -1;
        }
        //System.out.println(" ============================= ");
        //System.out.println(" Contents of post request ");
        //System.out.println(" ============================= ");

        //System.out.println(" ============================= ");
        //System.out.println(" Contents of post request ends ");

        //System.out.println(" ============================= ");
        //System.out.println(" ============" + resultData + "============= ");
        //connection.disconnect();
        //return resultData;
    }

    public static void main(String[] args) throws Exception
    {
        // TODO Auto-generated method stub 

        try
        {

            // readContentFromGet();

            //readContentFromPost("18680348577", "xmxx1Y", 0.01);

        }
        catch (Exception e)
        {

            // TODO Auto-generated catch block 

            e.printStackTrace();

        }

    }

    public static String getResponseResult(InputStreamReader inputReader) throws Exception
    {

        BufferedReader buffer = new BufferedReader(inputReader);
        String inputLine = null;
        String dataTemp = "";
        while ((inputLine = buffer.readLine()) != null)
        {
            dataTemp += inputLine + "";
        }
        return dataTemp;
    }
}
