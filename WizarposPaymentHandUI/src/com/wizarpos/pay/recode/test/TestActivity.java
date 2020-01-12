package com.wizarpos.pay.recode.test;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wizarpos.pay2.lite.R;
import com.wizarpos.recode.zxing.ZxingBarcodeManager;


public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = TestActivity.class.getName();


    private Button btn;
    private ImageView ivBarcode;
    private EditText ed_zxing_barcode_test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn = (Button) findViewById(R.id.btn_zxing_test);
        btn.setOnClickListener(this);
        ivBarcode = (ImageView) findViewById(R.id.iv_zxing_test);
        ed_zxing_barcode_test = (EditText) findViewById(R.id.ed_zxing_barcode_test);
    }


    @Override
    public void onClick(View v) {
        String contentString = ed_zxing_barcode_test.getText().toString();
        if (!contentString.equals("")) {
            // 根据字符串生成条形码图片并显示在界面上，第二个参数为图片的大小（350*350）
            Bitmap qrCodeBitmap = null;
            qrCodeBitmap = ZxingBarcodeManager.creatBarcode(contentString, 200, 40);
            ivBarcode.setImageBitmap(qrCodeBitmap);
        } else {
            Toast.makeText(this, "Text can not be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
