package com.wopluspay.wopay;

import com.alibaba.fastjson.JSON;
import com.wopluspay.wopay.entity.TokenResponse;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TianyiWoToken
{
    public static final String POST_URL = "https://open.wo.com.cn/openapi/authenticate/v1.0"+"?appKey=5bb3543058d042839bc910d2ad109ab0a2d15e88&appSecret=a5a2d592e019a5c55000f1d422fdba35bc62a19d";

    public static int CONTENT_EMPTY = 0;

    public static String readToken() throws Exception
    {
        // Post请求的url，与get不同的是不需要带参数
        URL postUrl = new URL(POST_URL);

        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        connection.setRequestMethod("GET");
        //connection.setDoOutput(true);//this sentence will change method to post
        connection.setDoInput(true);
        // 时间建议长点
        connection.setConnectTimeout(60000);
        connection.setReadTimeout(60000);
        //connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Cache-Control", "max-age=0");
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
        connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
        connection.setRequestProperty("Cookie", "_ga=GA1.3.1927612005.1473052286");

        int responseCode = connection.getResponseCode();
        String resultData = null;
        if (200 == responseCode || 201 == responseCode)
        {
            InputStream in = connection.getInputStream();
            resultData = getResponseResult(new InputStreamReader(in, "UTF-8"));
            connection.disconnect();
            TokenResponse tokenResponse = JSON.parseObject(resultData, TokenResponse.class);
            return tokenResponse.getToken();
        }
        else if (CONTENT_EMPTY == connection.getContentLength())
        {
            resultData = "get response error,content is empty";
            connection.disconnect();
            return null;
        }
        else
        {
            BufferedInputStream err = new BufferedInputStream(connection.getErrorStream());
            resultData = getResponseResult(new InputStreamReader(err));
            connection.disconnect();
            return null;
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
