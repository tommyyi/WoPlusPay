package com.wopluspay.wopay;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.wopluspay.Paycode;
import com.wopluspay.wopay.entity.ChargeResponse;

public class TianyiWoCharge
{

    public static final String POST_URL = "https://open.wo.com.cn/openapi/rpc/payment/v1.0";

    public static int CONTENT_EMPTY = 0;


    public static boolean charge(String phoneNumber, Paycode paycode, int verifiedCode) throws Exception
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
        // URLConnection.setFollowRedirects是static 函数，作用于所有的URLConnection对象。
        // connection.setFollowRedirects(true);
        //URLConnection.setInstanceFollowRedirects 是成员函数，仅作用于当前函数
        connection.setInstanceFollowRedirects(true);
        // 时间建议长点
        connection.setConnectTimeout(60000);
        connection.setReadTimeout(60000);
        // 配置连接的Content-type，配置为application/x- www-form-urlencoded的意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode进行编码
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");
        String head = "appKey=" + "\"" + "5bb3543058d042839bc910d2ad109ab0a2d15e88" + "\"" + ", token=" + "\"" + paycode.getToken() + "\"" + "";
        connection.setRequestProperty("Authorization", head);
        // 连接，从postUrl.openConnection()至此的配置必须要在 connect之前完成，
        // 要注意的是connection.getOutputStream()会隐含的进行调用 connect()，所以这里可以省略
        //connection.connect();
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        //正文内容其实跟get的URL中'?'后的参数字符串一致
        HashMap<String, Object> cnts = new HashMap<String, Object>();
        cnts.put("paymentUser", phoneNumber);
        //cnts.put("paymentType", 0);
        cnts.put("outTradeNo", paycode.getOutTradeNo());
        cnts.put("paymentAcount", "001");
        cnts.put("subject", paycode.getProduct());
        //cnts.put("description", paycode.getProduct());
        //cnts.put("price", paycode.getPrice());
        //cnts.put("quantity", 1);
        cnts.put("totalFee", Float.valueOf(paycode.getPrice()));
        //cnts.put("showUrl", "www.baidu.com");
        cnts.put("paymentcodesms", verifiedCode);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time_long = sdf.format(new Date());
        cnts.put("timeStamp", time_long);


        String secretKey = "5bb3543058d042839bc910d2ad109ab0a2d15e88&a5a2d592e019a5c55000f1d422fdba35bc62a19d";
        String signature = Encrypt.encryptSha1(cnts);
        cnts.put("signType", "RSA-SHA1");
        cnts.put("signature", signature);
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
        //Object o = connection.getContent();
        System.out.println("responseCode:" + responseCode);

        String resultData = null;
        if (200 == responseCode || 201 == responseCode)
        {
            InputStream in = connection.getInputStream();
            resultData = getResponseResult(new InputStreamReader(in, "UTF-8"));
            ChargeResponse chargeResponse = JSON.parseObject(resultData, ChargeResponse.class);
            connection.disconnect();
            if(chargeResponse.getResultCode()==0)
                return true;
        }
        else if (CONTENT_EMPTY == connection.getContentLength())
        {
            resultData = "get response error,content is empty";
            connection.disconnect();
            return false;
        }
        else
        {
            BufferedInputStream err = new BufferedInputStream(connection.getErrorStream());
            resultData = getResponseResult(new InputStreamReader(err, "UTF-8"));
            connection.disconnect();
            return false;
        }

        System.out.println(" ============" + resultData + "============= ");
        //connection.disconnect();
        return false;
    }

    public static void main(String[] args) throws Exception
    {
        // TODO Auto-generated method stub 

        try
        {

            // readContentFromGet();

            //readContentFromPost("18680348577", 255);

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

    public static void parseSmsBody(String resultData, String contentType)
    {

        resultData = resultData.replace("\t", "");
        byte[] data = resultData.toString().getBytes();
        try
        {
            JSONObject jso = getJsonModelFromByte(data);
            //            setResultCode(jso.getString("code"));
            //            setDescription(jso.getString("description"));
            //            JSONObject joBody = jso.getJSONObject("result");
            //            if (null != joBody)
            //            {
            //                for (int i = 0; i < joBody.size(); i++)
            //                {
            //                    setCreateTime(joBody.getString("createTime"));
            //                    setProfileId(joBody.getString("profileId"));
            //                    setSmsMsgId(joBody.getString("smsMsgId"));
            //                    setUpdateTime(joBody.getString("update"));
            //                    setSmsStatus(joBody.getString("status"));
            //                    
            //                }
            //                
            //            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public static JSONObject getJsonModelFromByte(byte[] data)
    {
        if (null == data)
        {
            return null;
        }

        JSONObject jsonSrcObj = null;

        try
        {
            Feature[] fArray = new Feature[]{};
            jsonSrcObj = (JSONObject) JSON.parse(data, fArray);
        }
        catch (Exception e)
        {
            return null;
        }

        return jsonSrcObj;
    }
}
