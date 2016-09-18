package com.wopluspay.wopay;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

//import sun.misc.BASE64Encoder;

public class Encrypt
{
    /**
     * The Constant CRYPTO_NAME_HMAC_SHA1.
     */
    private static final String CRYPTO_NAME_HMAC_SHA1 = "HmacSHA1";

    /**
     * Encrypt.
     *
     * @param map the map
     * @return the string
     */
    public static String encryptSha1(Map<String, Object> map)
    {
        String sourceStr = buildQueryParam(map);
        System.out.println(sourceStr);
        String encryptSource = null;
        try
        {
            encryptSource = encryptS1B(sourceStr);
        }
        catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encryptSource;
    }

    /**
     * Encrypt hmac sha1.
     *
     * @param map       the map
     * @param secretKey the secret key
     * @return the string
     */
    public static String encryptHmacSha1(Map<String, Object> map, String secretKey)
    {
        String sourceStr = buildQueryParam(map);
        System.out.println(sourceStr);

        byte[] result = hmacSha1(sourceStr, secretKey);

        //BASE64Encoder base64 = new BASE64Encoder();
        return Base64Encoder.encode(result);
    }

    /**
     * Hmac sha1.
     *
     * @param source    the source
     * @param secretKey the secret key
     * @return the byte[]
     */
    public static byte[] hmacSha1(String source, String secretKey)
    {
        try
        {
            SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes(), CRYPTO_NAME_HMAC_SHA1);
            Mac mac = Mac.getInstance(CRYPTO_NAME_HMAC_SHA1);
            mac.init(secret);
            byte[] result = mac.doFinal(source.getBytes("UTF-8"));
            return result;
        }
        catch (InvalidKeyException e)
        {
            return null;
        }
        catch (NoSuchAlgorithmException e)
        {
            return null;
        }
        catch (UnsupportedEncodingException e)
        {
            return null;
        }
    }

    /**
     * Bytes2 hex.
     *
     * @param bts the bts
     * @return the string
     */
    private static String bytes2Hex(byte[] bts)
    {
        StringBuilder hexSb = new StringBuilder("");
        String tmp = null;
        for (int i = 0; i < bts.length; i++)
        {
            tmp = Integer.toHexString(bts[i] & 0xFF);
            if (tmp.length() == 1)
            {
                hexSb.append("0");
            }
            hexSb.append(tmp);
        }
        return hexSb.toString();
    }

    /**
     * Encrypt s1 b.
     *
     * @param passwd the passwd
     * @return the string
     * @throws NoSuchAlgorithmException     the no such algorithm exception
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static String encryptS1B(String passwd) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest md5;
        md5 = MessageDigest.getInstance("SHA-1");
        md5.update(passwd.getBytes("UTF-8"));

        byte[] digesta = md5.digest();

        String hexString = bytes2Hex(digesta);
        return Base64Encoder.encode(hexString.getBytes("UTF-8"));
        //return Base64.encodeToString(hexString.getBytes("utf-8"), Base64.DEFAULT);
    }

    /**
     * Builds the query param.
     *
     * @param queryParamMap the query param map
     * @return the string
     */
    public static String buildQueryParam(Map<String, Object> queryParamMap)
    {
        List<String> keyList = getFinalKeySet(queryParamMap);
        StringBuilder sb = new StringBuilder();
        for (String key : keyList)
        {
            sb.append(key).append("=").append(queryParamMap.get(key)).append("&");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * Gets the final key set.
     *
     * @param queryParamMap the query param map
     * @return the final key set
     */
    public static List<String> getFinalKeySet(Map<String, Object> queryParamMap)
    {
        List<String> keyList = getKeyList(queryParamMap);
        Collections.sort(keyList, new QueryKeyNoCaseComparator());

        return keyList;
    }

    /**
     * Gets the key list.
     *
     * @param map the map
     * @return the key list
     */
    public static List<String> getKeyList(Map<String, Object> map)
    {
        List<String> list = new ArrayList<String>();

        Set<Entry<String, Object>> entrySet = map.entrySet();
        for (Entry<String, Object> entry : entrySet)
        {
            list.add(entry.getKey());
        }

        return list;
    }
}
