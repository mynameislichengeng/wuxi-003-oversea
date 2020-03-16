package com.wizarpos.pay.ui.newui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.log.util.StringUtil;
import com.wizarpos.pay.app.Pay2Application;
import com.wizarpos.pay.broadcastreceiver.Alarmreceiver;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.recode.sale.callback.InvoiceUIClickListener;
import com.wizarpos.pay.recode.sale.service.impl.InvoiceServiceImpl;
import com.wizarpos.pay.setting.presenter.RateUpdate;
import com.wizarpos.pay.view.InputPad;
import com.wizarpos.pay.view.fragment.common.BaseViewFragment;
import com.wizarpos.pay.view.util.NewCashTextWatcher;
import com.wizarpos.pay2.lite.R;
import com.alibaba.fastjson.JSONObject;


public class NewQ2GatheringFragment extends BaseViewFragment {
    private final static String TAG_LOG = NewQ2GatheringFragment.class.getName();
    private EditText etAmount;
    private TextView tvShowRMB, tv_invoice;
    private String exchangeRate;
    private InputPad inputPad;
    private LinearLayout lin_invoice_content, lin_invoice_icon;

    private OnConfirmListener onConfirmListener;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (null != progresser) {
                    progresser.showContent();
                }
                if (null != exchangeRate) {
                    exchangeRate = AppConfigHelper.getConfig(AppConfigDef.exchangeRate);
                }
                LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    @Override
    public void initView() {
        Log.d(TAG_LOG, "initView()");
        setMainView(R.layout.fragment_gathering_for_q2);
        etAmount = (EditText) mainView.findViewById(R.id.etAmount);
        tvShowRMB = ((TextView) mainView.findViewById(R.id.tvShowRMB));
        lin_invoice_icon = mainView.findViewById(R.id.lin_invoice_icon);
        lin_invoice_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateEditIconOnclick();
            }
        });
        //invoice
        lin_invoice_content = mainView.findViewById(R.id.line_invoice_content);
        tv_invoice = mainView.findViewById(R.id.tv_invoice);
        setLayoutInvoiceTv();
        exchangeRate = AppConfigHelper.getConfig(AppConfigDef.exchangeRate);
        if (TextUtils.isEmpty(exchangeRate)) {
            progresser.showProgress();
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(Alarmreceiver.ACTION_GET_EXCHANGE));
            broadcast();
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

    private void setLayoutInvoiceTv() {
        String invoiceStr = InvoiceServiceImpl.getInstance().gettingInvoice(getContext());
        tv_invoice.setText(invoiceStr);
        if (TextUtils.isEmpty(invoiceStr)) {
            lin_invoice_content.setVisibility(View.GONE);
        } else {
            lin_invoice_content.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 点击编辑icon的相应事件回调
     */
    private void operateEditIconOnclick() {
        InvoiceServiceImpl.getInstance().settingInvoiceCallback(getContext(), new InvoiceUIClickListener() {
            @Override
            public void onClick(String invoice) {
                setLayoutInvoiceTv();
            }
        });
    }

    private void initInputPad() {
        inputPad = (InputPad) mainView.findViewById(R.id.inputPad);
        etAmount.setSelection(etAmount.getText().length());
        etAmount.setInputType(InputType.TYPE_NULL);
        inputPad.addEditView(etAmount, InputPad.InputType.TYPE_INPUT_MONEY);
        inputPad.setOnConfirmListener(new InputPad.OnComfirmListener() {
            @Override
            public void onConfirm(EditText editText) {
                onSubmit();
            }
        });
    }

    private void onSubmit() {
        if (TextUtils.isEmpty(getAmount())) {
            Toast.makeText(Pay2Application.getInstance(), Pay2Application.getInstance().getResources().getString(R.string.payamount_warn), Toast.LENGTH_SHORT).show();
            return;
        }
        //验证invoice是否输入合法
        if (!InvoiceServiceImpl.getInstance().validateInvoice(getContext())) {
            return;
        }

        if (onConfirmListener != null) {
            onConfirmListener.onComfirm();
        }
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


    public void reset() {
        etAmount.setText("0.00");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG_LOG, "onDestroy()");
        if (receiver != null) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
            receiver = null;
        }
    }

    public interface OnConfirmListener {
        void onComfirm();
    }

    private void broadcast() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RateUpdate rateUpdate = new RateUpdate();
                rateUpdate.rateUpdate(new ResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        JSONObject jsonObject = (JSONObject) response.getResult();
                        if (null != jsonObject) {
                            String exchangeRate = jsonObject.getString("rate");
                            if (!TextUtils.isEmpty(exchangeRate)) {
                                AppConfigHelper.setConfig(AppConfigDef.exchangeRate, exchangeRate);
                                Intent intent1 = new Intent();
                                intent1.setAction(Alarmreceiver.ACTION_GET_EXCHANGE);
                                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent1);
                            }
                        }
                    }

                    @Override
                    public void onFaild(Response response) {
                        Toast.makeText(getContext(), response.msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 200);

    }
}