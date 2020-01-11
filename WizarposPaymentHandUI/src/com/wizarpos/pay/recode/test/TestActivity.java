package com.wizarpos.pay.recode.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lc.librefreshlistview.linear.SimpleLinearRecycleView;
import com.lc.librefreshlistview.listener.RefreshEventListener;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.recode.hisotory.activitylist.widget.TransRecordDialog;

import com.wizarpos.pay2.lite.R;


public class TestActivity extends AppCompatActivity {


    public static void startAc(Context context) {
        Intent intent = new Intent(context, TestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);
        Button btnModelSetting = findViewById(R.id.btnModelSetting);
        btnModelSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClick(v);
            }
        });
    }


    public void btnClick(View view) {
//        DailyDetailResp resp = new DailyDetailResp();
//        resp.setSingleAmount("你好");
//        resp.setPayTime("2019-01-03");
//        resp.setTransType("alipay");
//        resp.setTranLogId("p123123");
//        resp.setRefundAmount("123");
//        TransRecordDialog.refundDialog(this, resp);
    }

}
