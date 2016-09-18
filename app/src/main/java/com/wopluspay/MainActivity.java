package com.wopluspay;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import cn.cmgame.billing.api.GameInterface;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pay(View view)
    {
        PayCenter.pay(this, "013", new GameInterface.IPayCallback()
        {
            public static final String TAG = "TAG";

            @Override
            public void onResult(int resultCode, String s, Object o)
            {
                if (resultCode == PayCenter.SUCCESS)
                {
                    Log.e(TAG,"SUCCESS");
                }

                if(resultCode==PayCenter.FAIL)
                    Log.e(TAG,"FAIL");
            }
        });
    }
}
