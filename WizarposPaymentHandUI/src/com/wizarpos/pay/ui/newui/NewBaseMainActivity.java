package com.wizarpos.pay.ui.newui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.wizarpos.pay.common.base.BaseActivity;
import com.wizarpos.pay.ui.ProgressLayout;
import com.wizarpos.pay2.lite.R;

/**
 * Created by wu on 16/3/27.
 */
public class NewBaseMainActivity extends BaseActivity {

    protected ProgressLayout progresser;

    /** Key code constant: left scan key. */
    public static final int KEYCODE_SCAN_LEFT = 229;

    /** Key code constant: right scan key. */
    public static final int KEYCODE_SCAN_RIGHT = 230;

    /** Key code constant: talk key. */
    public static final int KEYCODE_TALK = 231;

    /** Key code constant: qr key. */
    public static final int KEYCODE_QR = 232;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_new_test_main);
        progresser = (ProgressLayout) findViewById(R.id.progress);
        progresser.showContent();
    }

    protected void requestFocus(){
        RelativeLayout focuslayout = (RelativeLayout) findViewById(R.id.rlRequestFocusLayout);
        focuslayout.requestFocus();
    }

    public void startNewActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
        finish();
    }
}
