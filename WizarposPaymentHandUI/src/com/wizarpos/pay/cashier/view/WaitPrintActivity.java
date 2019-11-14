package com.wizarpos.pay.cashier.view;

import android.os.Bundle;
import android.os.Handler;

import com.wizarpos.device.printer.PrintServiceController;
import com.wizarpos.pay.common.base.BaseViewActivity;

/**
 * 等待打印结束
 * Created by wu on 16/1/15.
 */
public class WaitPrintActivity extends BaseViewActivity {

    private Handler handler = new Handler();

    private Runnable checkRunnable = new Runnable() {
        @Override
        public void run() {
            boolean isPrintting = PrintServiceController.isPrintting(WaitPrintActivity.this);
            if (isPrintting) {
                handler.postDelayed(this, 1_000L);
            } else {
                setResult(RESULT_OK, getIntent());
                WaitPrintActivity.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("正在打印");
        progresser.showProgress();
        handler.postDelayed(checkRunnable, 1_000L);
    }

    @Override
    public void onBackPressed() {

    }

}
