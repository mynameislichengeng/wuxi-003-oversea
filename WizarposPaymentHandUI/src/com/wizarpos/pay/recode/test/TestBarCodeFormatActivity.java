package com.wizarpos.pay.recode.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay2.lite.R;
import com.wizarpos.recode.print.constants.BarFormat;
import com.wizarpos.recode.print.content.AcctContent;
import com.wizarpos.recode.print.data.BarcodeDataManager;
import com.wizarpos.recode.receipt.constants.ReceiptStatusEnum;
import com.wizarpos.recode.receipt.service.ReceiptDataManager;

import java.io.UnsupportedEncodingException;


public class TestBarCodeFormatActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = TestBarCodeFormatActivity.class.getName();


    public static void startActivtyUp(Context context) {
        Intent intent = new Intent(context, TestBarCodeFormatActivity.class);
        context.startActivity(intent);
    }


    private Button btn;

    private EditText ed_zxing_barcode_test, ed_space;
    private Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn = (Button) findViewById(R.id.btn_zxing_test);
        btn.setOnClickListener(this);
        ed_zxing_barcode_test = (EditText) findViewById(R.id.ed_zxing_barcode_test);
        ed_space = findViewById(R.id.ed_space);
        spinner = findViewById(R.id.spinner);
//        BarcodeDataManager.settingCurrentFormat(BarFormat.getEnum(0));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                BarFormat format = BarFormat.getEnum(pos);
                BarcodeDataManager.settingCurrentFormat(format);
                Toast.makeText(TestBarCodeFormatActivity.this, "选择了" + format.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        ReceiptDataManager.settingBarcodeStatus(ReceiptStatusEnum.OPEN);
    }

    @Override
    public void onClick(View v) {
        String str = null;
        try {
            str = getBarcodeText();
//            str = getHtml();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (TextUtils.isEmpty(str)) {
            Toast.makeText(this, "请输入打印内容", Toast.LENGTH_SHORT).show();
            return;
        }


        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(this);
        controller.print(str);
    }

    private String getBarcodeText() throws UnsupportedEncodingException {

        String str = ed_zxing_barcode_test.getText().toString();
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String result = "<c><b>A</b></c>";

        DailyDetailResp dailyDetailResp = new DailyDetailResp();

//        dailyDetailResp.setTransCurrency("CNY");
//        dailyDetailResp.setMerchantTradeCode(str);
        dailyDetailResp.setThirdExtId(str);

        int count = 0;
        String space = ed_space.getText().toString();
        if (!TextUtils.isEmpty(space)) {
            count = Integer.valueOf(space);
        }
//        PrintBase.setCOUNTSPACE(count);
        String totals = AcctContent.printStringActivity(this, dailyDetailResp);

        result += totals;

        result += "<end/>";
        return result;
    }


    private String getHtml() {
        String str = ed_zxing_barcode_test.getText().toString();
        return str;
    }
}
