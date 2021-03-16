package com.wizarpos.pay.ui.newui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.wizarpos.pay.common.base.BaseActivity;
import com.wizarpos.pay.ui.ProgressLayout;
import com.motionpay.pay2.lite.R;

/**
 * Created by wu on 16/3/27.
 */
public class NewBaseTranlogActivity extends BaseActivity {

    protected ProgressLayout progresser;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_tranlog_detail_new);
        progresser = (ProgressLayout) findViewById(R.id.progress);
        progresser.showContent();
    }
//
//    protected void requestFocus(){
//        RelativeLayout focuslayout = (RelativeLayout) findViewById(R.id.rlRequestFocusLayout);
//        focuslayout.requestFocus();
//    }

    public void startNewActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
        finish();
    }
}
