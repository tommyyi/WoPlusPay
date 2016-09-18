package com.wopluspay;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.wopluspay.wopay.TianyiWoCharge;
import com.wopluspay.wopay.TianyiWoCode;
import com.wopluspay.wopay.TianyiWoToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cn.cmgame.billing.api.GameInterface;

public class PayCenter
{
    public static final int SUCCESS = 1;
    public static final int FAIL = 2;

    public static boolean getCodeSms(Paycode paycode, String phoneNumber)
    {
        try
        {
            String token = TianyiWoToken.readToken();
            if(token==null)
                return false;

            paycode.setToken(token);
            int isOk = TianyiWoCode.requestCodeSms(phoneNumber, paycode, token);
            if(isOk!=0)
                return false;
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean woPay(Paycode paycode, String phoneNumber, int codesms)
    {
        try
        {
            return TianyiWoCharge.charge(phoneNumber,paycode, codesms);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static Paycode getPayCode(Activity activity, String billingIndex)
    {
        List<Paycode> parseArray=
        JSON.parseArray(readPayList(activity),Paycode.class);
        int size=parseArray.size();
        for(int index=0;index<size;index++)
            if(parseArray.get(index).getNumber().equals(billingIndex))
                return parseArray.get(index);
        return null;
    }

    public static void pay(Activity activity, String billingIndex, GameInterface.IPayCallback iPayCallback)
    {
        Dialog dialog=new PayDialog(activity, activity.getResources().getIdentifier("dialog","style",activity.getPackageName()),billingIndex,iPayCallback);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        Point size=new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        window .setGravity(Gravity.CENTER);
        params.width=size.x-activity.getResources().getDimensionPixelSize(activity.getResources().getIdentifier("activity_horizontal_margin","dimen",activity.getPackageName()));
        params.height=WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        dialog.show();
    }

    public static String readPayList(Activity activity)
    {
        try
        {
            InputStream inputStream = activity.getAssets().open("paycode.json");
            int available = inputStream.available();
            byte[] buffer=new byte[available];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
