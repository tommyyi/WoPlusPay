package com.wopluspay;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.cmgame.billing.api.GameInterface;

public class PayDialog extends Dialog
{
    Activity activity;
    Button ok;
    Button cancel;
    Button btCodeSms;
    EditText phoneEdit, codeSmsEdit;
    private String billingIndex;
    private GameInterface.IPayCallback iPayCallback;
    private ProgressDialog mProgressDialog;
    private Paycode mPayCode;
    private Handler mHandler;

    public PayDialog(Activity activity)
    {
        super(activity);
    }

    public PayDialog(Activity activity, int theme,String billingIndex,GameInterface.IPayCallback iPayCallback)
    {
        super(activity, theme);

        this.activity=activity;
        this.billingIndex=billingIndex;
        this.iPayCallback=iPayCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(/*R.layout.dialog*/getContext().getResources().getIdentifier("dialog","layout",getContext().getPackageName()));

        mProgressDialog = new ProgressDialog(getContext());
        mPayCode = PayCenter.getPayCode(activity, billingIndex);
        mHandler = new Handler();

        ok=(Button)findViewById(/*R.id.ok*/getContext().getResources().getIdentifier("ok","id",getContext().getPackageName()));
        btCodeSms=(Button)findViewById(/*R.id.btcodesms*/getContext().getResources().getIdentifier("btcodesms","id",getContext().getPackageName()));
        cancel=(Button)findViewById(/*R.id.cancel*/getContext().getResources().getIdentifier("cancel","id",getContext().getPackageName()));
        phoneEdit=(EditText)findViewById(/*R.id.phoneNumber*/getContext().getResources().getIdentifier("phoneNumber","id",getContext().getPackageName()));
        codeSmsEdit=(EditText)findViewById(/*R.id.verifiedNumber*/getContext().getResources().getIdentifier("verifiedNumber","id",getContext().getPackageName()));

        btCodeSms.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mProgressDialog.setMessage("正在请求联通下发验证码...");
                Runnable runnable=new Runnable()
                {
                    @Override
                    public void run()
                    {
                        final boolean isOk = PayCenter.getCodeSms(mPayCode, phoneEdit.getText().toString());
                        Runnable runnable1=new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if(isOk)
                                {
                                    Toast.makeText(getContext(),"验证码已下发，请输入该验证码",Toast.LENGTH_LONG).show();
                                }
                                else
                                    Toast.makeText(getContext(),"验证码未下发",Toast.LENGTH_LONG).show();
                            }
                        };
                        mHandler.post(runnable1);
                    }
                };
                new Thread(runnable).start();
            }
        });

        ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PayDialog.this.dismiss();
                mProgressDialog.setMessage("正在处理中...");
                mProgressDialog.show();

                Runnable runnable=new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(PayCenter.woPay(mPayCode, phoneEdit.getText().toString(),Integer.valueOf(codeSmsEdit.getText().toString())))
                            iPayCallback.onResult(PayCenter.SUCCESS,billingIndex,new Object());
                        else
                            iPayCallback.onResult(PayCenter.FAIL,billingIndex,new Object());
                        mProgressDialog.dismiss();
                        PayDialog.this.dismiss();
                    }
                };
                new Thread(runnable).start();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PayDialog.this.dismiss();
            }
        });
    }
}
