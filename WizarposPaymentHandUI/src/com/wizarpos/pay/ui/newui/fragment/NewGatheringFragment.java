package com.wizarpos.pay.ui.newui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wizarpos.log.util.StringUtil;
import com.wizarpos.pay.broadcastreceiver.Alarmreceiver;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.view.fragment.InputPadFragment;
import com.wizarpos.pay.view.fragment.OnMumberClickListener;
import com.wizarpos.pay.view.fragment.common.BaseViewFragment;
import com.wizarpos.pay.view.util.NewCashTextWatcher;
import com.wizarpos.pay2.lite.R;

public class NewGatheringFragment extends BaseViewFragment implements OnMumberClickListener, InputPadFragment.OnKeyChangedListener {
    private final static String TAG_LOG = "NewGatheringFragment";
    private EditText etAmount;
    private LinearLayout llBankCard;
    private TextView tvBankCard;
    private InputPadFragment inputPadFragment;
    private TextView tvShowRMB;
    private TextView tvExchange;
    private String exchangeRate;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != progresser) {
                progresser.showContent();
            }
            if (null != exchangeRate) {
                exchangeRate = AppConfigHelper.getConfig(AppConfigDef.exchangeRate);
                tvExchange.setText(getString(R.string.note) + getString(R.string.exchangeApprox) + exchangeRate + " CNY");
            }
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        }
    };

    @Override
    public void initView() {
        setMainView(R.layout.fragment_gathering);
        etAmount = (EditText) mainView.findViewById(R.id.etAmount);
        tvShowRMB = ((TextView) mainView.findViewById(R.id.tvShowRMB));
        tvExchange = ((TextView) mainView.findViewById(R.id.tvExchange));
        llBankCard = (LinearLayout) mainView.findViewById(R.id.llBankCard);
        tvBankCard = (TextView) mainView.findViewById(R.id.tvBankCard);
        exchangeRate = AppConfigHelper.getConfig(AppConfigDef.exchangeRate);
        if (TextUtils.isEmpty(exchangeRate)){
            progresser.showProgress();
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(Alarmreceiver.ACTION_GET_EXCHANGE));
        }else {
            tvExchange.setText(getString(R.string.note) + getString(R.string.exchangeApprox) + exchangeRate + " CNY");
        }
        initInputPad();
        etAmount.addTextChangedListener(new NewCashTextWatcher(etAmount, new NewCashTextWatcher.EditTextChange() {
            @Override
            public void dataChange(String string) {
                if (etAmount.getText().toString().trim() != null) {
                    String showRMB = String.format("%.2f", Float.parseFloat(Calculater.multiply(etAmount.getText().toString().trim(), exchangeRate)));
                    tvShowRMB.setText(showRMB);
                }
            }
        }));
    }

    private void initInputPad() {
        inputPadFragment = new InputPadFragment();
        inputPadFragment.setOnMumberClickListener(this);
        inputPadFragment.setOnTextChangedListener(this);
        inputPadFragment.setEditView(
                etAmount,
                com.wizarpos.pay.view.fragment.InputPadFragment.InputType.TYPE_INPUT_MONEY);
        etAmount.setSelection(etAmount.getText().length());
        etAmount.setInputType(InputType.TYPE_NULL);
    }


    public void inputKey(String num) {
        inputPadFragment.onNumber(num);
    }

    public void onDel() {
        inputPadFragment.onDel();
    }

    public String getAmount() {
        if (null == etAmount) {
            Log.d(TAG_LOG, "etAmount is null");
            return null;
        }
        String amount = etAmount.getText().toString().trim();
        if (!StringUtil.isSameString(amount, "0.00") && !TextUtils.isEmpty(amount)) {
            Log.d(TAG_LOG, amount + "");
            return amount;
        } else {
            return null;
        }
    }

    public void setCardNo(String cardNo) {
        llBankCard.setVisibility(View.VISIBLE);
        tvBankCard.setText(cardNo);
    }

    public String getCardNo() {
        return tvBankCard.getText().toString();
    }

    public void reset() {
        tvBankCard.setText("");
        etAmount.setText("0.00");
        llBankCard.setVisibility(View.GONE);
    }

    @Override
    public void onTextChanged(String newStr) {
        inputPadFragment.setNumberEditable(true);
    }

    @Override
    public void onSubmit() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
            receiver = null;
        }
    }
}