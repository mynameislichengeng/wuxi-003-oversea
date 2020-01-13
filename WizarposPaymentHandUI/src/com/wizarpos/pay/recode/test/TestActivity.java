package com.wizarpos.pay.recode.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wizarpos.device.printer.PrinterHelper;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay2.lite.R;


public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = TestActivity.class.getName();


    private Button btn;

    private EditText ed_zxing_barcode_test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn = (Button) findViewById(R.id.btn_zxing_test);
        btn.setOnClickListener(this);

        ed_zxing_barcode_test = (EditText) findViewById(R.id.ed_zxing_barcode_test);
    }


    @Override
    public void onClick(View v) {
        String str = ed_zxing_barcode_test.getText().toString();
        Constants.HANDUI_IS_BLOCK_UI = true;
        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(this);
        controller.print(str);
    }
}
