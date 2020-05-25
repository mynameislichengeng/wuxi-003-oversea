package com.wizarpos.pay.recode.hisotory.activitylist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.ui.newui.NewThirdpayScanActivity;
import com.wizarpos.pay2.lite.R;
import com.wizarpos.barcode.scanner.ScannerResult;

public class QueryScanQrCodeActivity extends NewThirdpayScanActivity {

    private final String TAG = QueryScanQrCodeActivity.class.getSimpleName();


    public final static int INTENT_REQUEST_CODE = 1000;
    public final static int INTENT_RESPONSE_SUCCESS = 1010;
    public final static String INTENT_KEY_TRAN_LOG = "tranlog";

    private TextView tvScanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        log("onCreate()");
        tvScanResult = ((TextView) findViewById(R.id.scan_result));
    }

    @Override
    protected void toMicroPay() {
        log("--toMicroPay()--");
    }

    @Override
    protected void checkOrder() {
        log("--checkOrder()--");
    }

    @Override
    protected void onScanSuccess(ScannerResult scannerResult) {
        String result = scannerResult.getResult();
        if (TextUtils.isEmpty(result)) {
            result = "";
        }
        log("--onScanSuccess()--result:" + result);
        Intent intent = new Intent();
        intent.putExtra(INTENT_KEY_TRAN_LOG, result);
        setResult(INTENT_RESPONSE_SUCCESS, intent);
        finish();
    }

    @Override
    public void display(Response response) {
        log("--display()--");
    }

    private void log(String msg) {
        Log.d("tag", TAG + ">>" + msg);
    }
}
