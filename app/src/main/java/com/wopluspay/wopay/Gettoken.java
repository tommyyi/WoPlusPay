package com.wopluspay.wopay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Gettoken
{
    
    public static final String GET_URL = "https://open.wo.com.cn/openapi/authenticate/v1.0";
    
    public static int CONTENT_EMPTY = 0;
    
    public static void readContentFromGet()
        throws Exception
    {
        // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码 
        String getURL =
            GET_URL + "?appKey=" + URLEncoder.encode("b46a23749eecb6f429022a641091418e0e9d921b", "utf-8") + "&"
                + "appSecret=" + URLEncoder.encode("00917dccdd81138c1abc0283297776602b359133", "utf-8");
        URL getUrl = new URL(getURL);
        // 根据拼凑的URL，打开连接，URL.openConnection()函数会根据 URL的类型，返回不同的URLConnection子类的对象，在这里我们的URL是一个http，因此它实际上返回的是HttpURLConnection 
        HttpURLConnection connection = (HttpURLConnection)getUrl.openConnection();
        //connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setConnectTimeout(60000);
        connection.setReadTimeout(60000);
        // 建立与服务器的连接，并未发送数据 
        connection.connect();
        // 发送数据到服务器并使用Reader读取返回的数据 
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        
        System.out.println(" ============================= ");
        
        System.out.println(" Contents of get request ");
        
        System.out.println(" ============================= ");
        
        String dataTemp = "";
        if (CONTENT_EMPTY == connection.getContentLength())
        {
            dataTemp = "get response error,content is empty";
            connection.disconnect();
            
        }
        else
        {
            //获取消息体的内容
            String inputLine;
            while ((inputLine = reader.readLine()) != null)
            {
                dataTemp += inputLine + "\n";
                
            }
        }
        System.out.println(dataTemp);
        reader.close();
        
        // 断开连接 
        
        connection.disconnect();
        
        System.out.println(" ============================= ");
        
        System.out.println(" Contents of get request ends ");
        
        System.out.println(" ============================= ");
        
    }
    
    public static void main(String[] args)
        throws Exception
    {
        
        try
        {
            
            readContentFromGet();
            
        }
        catch (IOException e)
        {
            
            e.printStackTrace();
            
        }
        
    }
    
    public static String getResponseResult(InputStreamReader inputReader)
        throws Exception
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
