package com.wizarpos.pay.cashier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.pay.cashier.thrid_app_controller.ThirdAppTransactionController;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppLoginResponse;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.thirdapp.ThirdAppBroadcastReceiver;
import com.wizarpos.pay.view.fragment.InputPadFragment;
import com.wizarpos.pay.view.fragment.InputPadFragment.OnKeyChangedListener;
import com.wizarpos.pay.view.fragment.OnMumberClickListener;
import com.wizarpos.pay2.lite.R;


/**
 * 交易撤销
 */
public class CancelTransactionActivity extends TransactionActivity implements OnMumberClickListener, OnKeyChangedListener, OnCheckedChangeListener, ThirdAppBroadcastReceiver.ThirdAppListener {
    private EditText etTranLogId = null;
    private CheckBox cbF;

    public static final String ORDER_NO = "order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(this, ThirdAppBroadcastReceiver.FILTER_TRANSACITON);
        initView();
        initThirdAppController();
        if (!ThirdAppTransactionController.getInstance().isInservice()) {
            cbF.setOnCheckedChangeListener(this);

            InputPadFragment inputPadFragment = InputPadFragment.newInstance(InputPadFragment.KEYBOARDTYPE_SIMPLE);
            inputPadFragment.setEditView(etTranLogId, com.wizarpos.pay.view.fragment.InputPadFragment.InputType.TYPE_INPUT_NORMAL);
            inputPadFragment.setOnMumberClickListener(this);
            inputPadFragment.setOnTextChangedListener(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.flInputPad, inputPadFragment).commit();
        }
        String orderNo = null;
        if (thirdAppController.getThirdAppVoidTransRequest() != null) {
            orderNo = thirdAppController.getThirdAppVoidTransRequest().getOrderNo();
        }
        if (TextUtils.isEmpty(orderNo)) {
            orderNo = getIntent().getStringExtra(ORDER_NO);
        }
        if (!TextUtils.isEmpty(orderNo) && Constants.TRUE.equals(AppStateManager.getState(AppStateDef.isLogin))) {
            setTranLogIdET(orderNo);
            query();
        }

    }

    private void initView() {
        setMainView(R.layout.repeal_transaction_activity);
        setTitleText(getResources().getString(R.string.trans_cancle));
        showTitleBack();
//		setTitleRight("查询");//[查询和确定是的功能相同，去掉查询@hong]
        etTranLogId = (EditText) findViewById(R.id.et_serial_num);
        cbF = (CheckBox) findViewById(R.id.cb_f);
        int[] btnIds = {R.id.btn_query};
        setOnClickListenerByIds(btnIds, this);

    }

    protected void query() {
        String tranLogId = etTranLogId.getText().toString();
        if (TextUtils.isEmpty(tranLogId)) {
            return;
        }
        if (cbF.isChecked()) {
            tranLogId = "F" + tranLogId;
        }
        Intent intent = new Intent(CancelTransactionActivity.this, RepealTransactionResultActivity.class);
        intent.putExtra("serialNum", tranLogId);
        startActivity(intent);
    }

    private void initThirdAppController() {
        thirdAppController = ThirdAppTransactionController.getInstance();
        thirdAppController.setThridAppFinisher(this);
        thirdAppController.parseRequest(getIntent());
    }

    @Override
    protected void onTitleRightClicked() {
        super.onTitleRightClicked();
        query();
    }

    @Override
    public void onSubmit() {
        query();
    }

    @Override
    public void onTextChanged(String newStr) {
        if (TextUtils.isEmpty(newStr)) {
            return;
        }
        if (cbF.isChecked()) {
            setTranLogIdET(removeP(newStr));
        } else {
            setTranLogIdET(insertP(newStr));
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.cb_f) {
            String tranLogId = etTranLogId.getText().toString();
            if (TextUtils.isEmpty(tranLogId)) {
                return;
            }
            if (isChecked) {
                setTranLogIdET(removeP(tranLogId));
            } else {
                setTranLogIdET(insertP(tranLogId));
            }
        }
    }

    private void setTranLogIdET(String tranLogId) {
        etTranLogId.setText(tranLogId);
    }

    protected String removeP(String newStr) {
        if (newStr.startsWith("P")) {
            newStr = newStr.replace("P", "");
        }
        return newStr;
    }

    protected String insertF(String newStr) {
        if (!newStr.startsWith("F")) {
            newStr = "F" + newStr;
        }
        return newStr;
    }

    protected String insertP(String newStr) {
        if (!newStr.startsWith("P")) {
            newStr = "P" + newStr;
        }
        return newStr;
    }

    @Override
    public void onResult(Intent intent) {
        Logger2.debug("撤销流程完成");
        try {
            if (thirdAppController.isInservice()) {
                bundleThridTransactionResponse(intent);
            } else {
                this.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void back() {
        try {
            AppStateManager.setState(AppStateDef.isInService, Constants.FALSE);
            ThirdAppLoginResponse response = new ThirdAppLoginResponse();
            response.setCode(1);
            response.setMessage("用户取消");
            Intent intent = new Intent();
            intent.putExtra("response", JSONObject.toJSONString(response));
            setResult(RESULT_CANCELED, intent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            thirdAppController.reset();
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        
    }
}
