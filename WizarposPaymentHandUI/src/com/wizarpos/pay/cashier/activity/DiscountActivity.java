package com.wizarpos.pay.cashier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.view.fragment.InputPadFragment;
import com.wizarpos.pay.view.fragment.OnMumberClickListener;
import com.motionpay.pay2.lite.R;

import java.math.BigDecimal;

public class DiscountActivity extends BaseViewActivity implements OnMumberClickListener, View.OnFocusChangeListener {

    private String initAmount;

    public static final String DISCUNT_INIT = "discount_init";

    private EditText etCustomDiscount;
    private EditText etPrecentDiscount;
    private EditText etDirectDiscount;
    private Button btnCustomDiscount;
    private Button btnPrecentDiscount;
    private Button btnDirectDiscount;
    private TextView etInitAmount;

    private InputPadFragment inputPadFragment;

    private int currentFocus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("折扣");
        setMainView(R.layout.activity_discount);
        showTitleBack();
        initAmount = getIntent().getStringExtra(DISCUNT_INIT);
        etInitAmount = (TextView) findViewById(R.id.etInitAmount);
        etCustomDiscount = (EditText) findViewById(R.id.etCustomDiscount);
        etPrecentDiscount = (EditText) findViewById(R.id.etPrecentDiscount);
        etDirectDiscount = (EditText) findViewById(R.id.etDirectDiscount);

        btnCustomDiscount = (Button) findViewById(R.id.btnCustomDiscount);
        btnPrecentDiscount = (Button) findViewById(R.id.btnPrecentDiscount);
        btnDirectDiscount = (Button) findViewById(R.id.btnDirectDiscount);
//        setTitleRight("确定");
        inputPadFragment = new InputPadFragment();
        inputPadFragment.setOnMumberClickListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.flInputPad, inputPadFragment).commit();
        etCustomDiscount.setOnFocusChangeListener(this);
        etDirectDiscount.setOnFocusChangeListener(this);
        etPrecentDiscount.setOnFocusChangeListener(this);

        etCustomDiscount.setInputType(InputType.TYPE_NULL);
        etPrecentDiscount.setInputType(InputType.TYPE_NULL);
        etDirectDiscount.setInputType(InputType.TYPE_NULL);

        etInitAmount.setText("待收金额: " + Calculater.formotFen(initAmount));
    }


    @Override
    public void onSubmit() {
        String discountAmount = null;
        try {
            switch (currentFocus) {
                case 0:
                    discountAmount = Calculater.formotYuan(etCustomDiscount.getText().toString());
                    if (TextUtils.isEmpty(discountAmount)) {
                        discountAmount = initAmount;
                    }
                    if (Calculater.compare(discountAmount, initAmount) > 0) {
                        UIHelper.ToastMessage(this, "请输入正确的折扣金额");
                        return;
                    }
                    discountAmount = Calculater.subtract(initAmount, discountAmount);
                    break;
                case 1:
                    String precent = etPrecentDiscount.getText().toString();
                    if (TextUtils.isEmpty(precent)) {
                        precent = "100";
                        etPrecentDiscount.setText("100");
                    }
                    if (Calculater.compare("0", precent) > 0 || Calculater.compare(precent, "100") > 0) {
                        UIHelper.ToastMessage(this, "请输入正确的折扣金额");
                        return;
                    }
                    BigDecimal initAmountDec = new BigDecimal(initAmount);
                    BigDecimal precentDec = new BigDecimal(Calculater.subtract("100",precent));
                    discountAmount = initAmountDec.multiply(precentDec).divide(new BigDecimal("100"),0).toString();
                    break;
                case 2:
                    discountAmount = Calculater.formotYuan(etCustomDiscount.getText().toString());
                    if (TextUtils.isEmpty(discountAmount)) {
                        discountAmount = "100";
                    }
                    if (Calculater.compare(discountAmount, initAmount) > 0) {
                        UIHelper.ToastMessage(this, "请输入正确的折扣金额");
                        return;
                    }
                    break;
            }

        } catch (Exception e) {
            discountAmount = "0";
        }
        LogEx.d("DiscountAmount:", discountAmount);
        Intent intent = getIntent();
        intent.putExtra(Constants.discountAmount, discountAmount);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            return;
        }
        switch (v.getId()) {
            case R.id.etCustomDiscount:
                currentFocus = 0;
                etPrecentDiscount.setText("");
                etDirectDiscount.setText("0.00");
                inputPadFragment.setEditView(etCustomDiscount, InputPadFragment.InputType.TYPE_INPUT_MONEY);
                break;
            case R.id.etPrecentDiscount:
                currentFocus = 1;
                etCustomDiscount.setText("0.00");
                etDirectDiscount.setText("0.00");
                inputPadFragment.setEditView(etPrecentDiscount, InputPadFragment.InputType.TYPE_INPUT_NORMAL);
                break;
            case R.id.etDirectDiscount:
                currentFocus = 2;
                etPrecentDiscount.setText("");
                etCustomDiscount.setText("0.00");
                inputPadFragment.setEditView(etDirectDiscount, InputPadFragment.InputType.TYPE_INPUT_MONEY);
                break;
        }
    }
}
