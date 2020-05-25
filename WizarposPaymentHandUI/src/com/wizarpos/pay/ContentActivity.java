package com.wizarpos.pay;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wizarpos.device.printer.PrinterHelper;
import com.wizarpos.pay2.lite.R;

public class ContentActivity extends Activity implements View.OnClickListener {
    private Button btn;
    private EditText input_content, ed_space;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn = findViewById(R.id.btn_zxing_test);
        btn.setOnClickListener(this);
        input_content = (EditText) findViewById(R.id.input_content);
        ed_space = (EditText) findViewById(R.id.ed_space);
    }

    @Override
    public void onClick(View v) {
        String str = input_content.getText().toString();
        String a = ed_space.getText().toString();
        if (TextUtils.isEmpty(a)) {
            a = "0";
        }
        str = multipleSpaces(Integer.valueOf(a)) + str;

        str += "<end/>";

        PrinterHelper.print(str);

    }


    protected static String multipleSpaces(int n) {
        String output = "";
        for (int i = 0; i < n; i++)
            output += " ";
        return output;
    }
}
