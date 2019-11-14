package com.wizarpos.pay.ui.newui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.ui.setting.CommonItem;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.ui.newui.NewMainActivity;
import com.wizarpos.pay.view.fragment.common.BaseViewFragment;
import com.wizarpos.pay.view.util.NewCashTextWatcher;
import com.wizarpos.pay2.lite.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReceivablesFragment extends BaseViewFragment {
    private final static String TAG_LOG = "ReceivablesFragment";
    private CommonItem ciSaleAmount, ciInputTip, ciTotal;//支付金额、小费、总金额
    private LinearLayout llP1, llP2, llP3, llInput, llEnterTip;
    private TextView tvShowRMBTotal;//总金额转化为人民币显示
    private TextView tvShowPayTotalRMB;//总金额转化为人民币显示
    private TextView tvP1Amount, tvP1Percentage, tvP2Amount, tvP2Percentage, tvP3Amount, tvP3Percentage, tvInputTip, tvInputTipLeft, tvSaleAmount;
    private String percentP1 = "";//小费第一个百分比
    private String percentP2 = "";//小费第二个百分比
    private String percentP3 = "";//小费第三个百分比
    private String exchangeRate = "";//汇率
    private RelativeLayout rlTip;
    //    private boolean llP1Selected, llP2Selected, llP3Selected, llInputSelected = false;
    private OnSaveListener onSaveListener;

    private String currency = PaymentApplication.getInstance().getResources().getString(R.string.currencySymbol);

    /**
     * 设置按钮点击的回调
     *
     * @author blue_sky
     */
    public interface PayBtnClickListener {
        void onPayBtnClick(String amount, String tipAmount);
    }

    private EditText etInputTip;

    @Override
    public void initView() {
        setMainView(R.layout.fragment_receivables);
        llP1 = ((LinearLayout) mainView.findViewById(R.id.llP1));
        llP2 = ((LinearLayout) mainView.findViewById(R.id.llP2));
        llP3 = ((LinearLayout) mainView.findViewById(R.id.llP3));
        llInput = ((LinearLayout) mainView.findViewById(R.id.llInput));
        llEnterTip = ((LinearLayout) mainView.findViewById(R.id.llEnterTip));
        tvP1Amount = ((TextView) mainView.findViewById(R.id.tvP1Amount));
        tvP1Percentage = ((TextView) mainView.findViewById(R.id.tvP1Percentage));
        tvP2Amount = ((TextView) mainView.findViewById(R.id.tvP2Amount));
        tvP2Percentage = ((TextView) mainView.findViewById(R.id.tvP2Percentage));
        tvP3Amount = ((TextView) mainView.findViewById(R.id.tvP3Amount));
        tvP3Percentage = ((TextView) mainView.findViewById(R.id.tvP3Percentage));
        tvInputTip = ((TextView) mainView.findViewById(R.id.tvInputTip));
        ciSaleAmount = ((CommonItem) mainView.findViewById(R.id.ciSaleAmount));
        tvSaleAmount = ciSaleAmount.getTvRight();
        tvShowRMBTotal = (TextView) mainView.findViewById(R.id.tvShowRMBTotal);
        tvShowPayTotalRMB = (TextView) mainView.findViewById(R.id.tvShowPayTotalRMB);
//        ciSaleAmount.getEtRight().setInputType(InputType.TYPE_NULL);
        ciSaleAmount.setIsShowLine(false);
        ciInputTip = ((CommonItem) mainView.findViewById(R.id.ciInputTip));
        ciInputTip.getTvRight().setPadding(0, 0, 0, 0);
        ciInputTip.setIsShowLine(false);
        tvInputTipLeft = ciInputTip.getTvLeft();
        etInputTip = ciInputTip.getEtRight();
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT <= 10) {
                // 点击EditText，屏蔽默认输入法
                etInputTip.setInputType(InputType.TYPE_NULL);
            } else {
                // 点击EditText，隐藏系统输入法
                getActivity().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                try {
                    Class<EditText> cls = EditText.class;
                    Method method = cls.getMethod("setShowSoftInputOnFocus",
                            boolean.class);// 4.0的是setShowSoftInputOnFocus，4.2的是setSoftInputShownOnFocus
                    method.setAccessible(false);
                    method.invoke(etInputTip, false);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        ciTotal = ((CommonItem) mainView.findViewById(R.id.ciTotal));
        ciTotal.setIsShowLine(false);
        RoundTextView rtPay = (RoundTextView) mainView.findViewById(R.id.rtPay);
        rlTip = (RelativeLayout) mainView.findViewById(R.id.rlTip);
        String tipsCustomAllow = AppConfigHelper.getConfig(AppConfigDef.tipsCustomAllow);
        if (!TextUtils.isEmpty(tipsCustomAllow) && tipsCustomAllow.contains(Constants.ALLOW)) {
            etInputTip.setEnabled(true);
        } else {
            etInputTip.setEnabled(false);
        }
        initDate();
        ((TextView) mainView.findViewById(R.id.tvExchange)).setText(getString(R.string.note) + getString(R.string.exchangeApprox) + exchangeRate + " CNY");
//        setEditClick();
        etInputTip.setText("0.00");
        etInputTip.setEnabled(false);
//        etInputTip.setInputType(InputType.TYPE_CLASS_NUMBER);
//        etInputTip.setSelection(etInputTip.getText().toString().length());
        showTotalAmount("0.00");
        llP1.setOnClickListener(this);
        llP2.setOnClickListener(this);
        llP3.setOnClickListener(this);
        llInput.setOnClickListener(this);
        setEditTipClick();
        rtPay.setOnClickListener(this);
        ciInputTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvInputTipLeft.setText(R.string.enter_a_tip);
                etInputTip.setEnabled(true);
                etInputTip.setFocusable(true);
                etInputTip.setFocusableInTouchMode(true);
                etInputTip.requestFocus();
                etInputTip.setInputType(InputType.TYPE_CLASS_NUMBER);
                etInputTip.setSelection(etInputTip.getText().toString().length());
                llEnterTip.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_select_tip));
                rlTip.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_unselect_tip));
                changeShow();
            }
        });
    }

    private void setEditTipClick() {
        etInputTip.addTextChangedListener(new NewCashTextWatcher(ciInputTip.getEtRight(), new NewCashTextWatcher.EditTextChange() {
            @Override
            public void dataChange(String string) {
                changeShow();
            }
        }));
    }

    private void changeShow() {
        String tipsCustomAllow = AppConfigHelper.getConfig(AppConfigDef.tipsCustomAllow);
        if (!TextUtils.isEmpty(tipsCustomAllow) && tipsCustomAllow.contains(Constants.ALLOW)) {
            showTotalAmount(etInputTip.getText().toString().trim());
            llP1.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tip));
            tvP1Amount.setTextColor(Color.BLACK);
            tvP1Percentage.setTextColor(Color.BLACK);
            llP2.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tip));
            tvP2Amount.setTextColor(Color.BLACK);
            tvP2Percentage.setTextColor(Color.BLACK);
            llP3.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tip));
            tvP3Amount.setTextColor(Color.BLACK);
            tvP3Percentage.setTextColor(Color.BLACK);
            llInput.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tip));
            tvInputTip.setTextColor(Color.BLACK);
        } else {
            tvInputTipLeft.setText("");
            etInputTip.setEnabled(false);
        }
    }

    private void initDate() {
        percentP1 = AppConfigHelper.getConfig(AppConfigDef.percentP1, "0");
        percentP2 = AppConfigHelper.getConfig(AppConfigDef.percentP2, "0");
        percentP3 = AppConfigHelper.getConfig(AppConfigDef.percentP3, "0");
        tvP1Percentage.setText(percentP1 + "%");
        tvP2Percentage.setText(percentP2 + "%");
        tvP3Percentage.setText(percentP3 + "%");
        exchangeRate = AppConfigHelper.getConfig(AppConfigDef.exchangeRate, "1");
        String amount = ((NewMainActivity) getActivity()).getAmount();
        if (amount != null) {
            tvSaleAmount.setText(currency + amount);
            showTip(amount);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnSaveListener) {
            onSaveListener = (OnSaveListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement onSaveListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSaveListener = null;
    }

    private void showTip(String string) {
        String showRMB = getString(R.string.approxCurrency) + String.format("%.2f", Float.parseFloat(Calculater.multiply(string, exchangeRate)));
        String p1Amount = currency + String.format("%.2f", Float.parseFloat(Calculater.multiply(string, Calculater.divide100(percentP1))));
        Log.d("TAG", "dataChange: " + percentP1 + string + Calculater.divide100(percentP1) + p1Amount);
        String p2Amount = currency + String.format("%.2f", Float.parseFloat(Calculater.multiply(string, Calculater.divide100(percentP2))));
        String p3Amount = currency + String.format("%.2f", Float.parseFloat(Calculater.multiply(string, Calculater.divide100(percentP3))));
        tvP1Amount.setText(p1Amount);
        tvP2Amount.setText(p2Amount);
        tvP3Amount.setText(p3Amount);
        tvShowRMBTotal.setText(showRMB);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llP1:
                rlTip.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_select_tip));
                llEnterTip.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_unselect_tip));
                etInputTip.setText(tvP1Amount.getText().toString().trim().replace(currency, ""));
                etInputTip.setEnabled(false);
                showTotalAmount(tvP1Amount.getText().toString().trim().replace(currency, ""));
                llP1.setBackgroundColor(getResources().getColor(R.color.dark_bule));
                tvP1Amount.setTextColor(Color.WHITE);
                tvP1Percentage.setTextColor(Color.WHITE);
                llP2.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tip));
                tvP2Amount.setTextColor(Color.BLACK);
                tvP2Percentage.setTextColor(Color.BLACK);
                llP3.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tip));
                tvP3Amount.setTextColor(Color.BLACK);
                tvP3Percentage.setTextColor(Color.BLACK);
                llInput.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tip));
                tvInputTip.setTextColor(Color.BLACK);
                tvInputTipLeft.setText("");
                break;
            case R.id.llP2:
                rlTip.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_select_tip));
                llEnterTip.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_unselect_tip));
                etInputTip.setText(tvP2Amount.getText().toString().trim().replace(currency, ""));
                etInputTip.setEnabled(false);
                showTotalAmount(tvP2Amount.getText().toString().trim().replace(currency, ""));
                llP2.setBackgroundColor(getResources().getColor(R.color.dark_bule));
                tvP2Amount.setTextColor(Color.WHITE);
                tvP2Percentage.setTextColor(Color.WHITE);
                llP1.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tip));
                tvP1Amount.setTextColor(Color.BLACK);
                tvP1Percentage.setTextColor(Color.BLACK);
                llP3.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tip));
                tvP3Amount.setTextColor(Color.BLACK);
                tvP3Percentage.setTextColor(Color.BLACK);
                llInput.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tip));
                tvInputTip.setTextColor(Color.BLACK);
                tvInputTipLeft.setText("");
                break;
            case R.id.llP3:
                rlTip.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_select_tip));
                llEnterTip.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_unselect_tip));
                etInputTip.setText(tvP3Amount.getText().toString().trim().replace(currency, ""));
                etInputTip.setEnabled(false);
                showTotalAmount(tvP3Amount.getText().toString().trim().replace(currency, ""));
                llP3.setBackgroundColor(getResources().getColor(R.color.dark_bule));
                tvP3Amount.setTextColor(Color.WHITE);
                tvP3Percentage.setTextColor(Color.WHITE);
                llP1.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tip));
                tvP1Amount.setTextColor(Color.BLACK);
                tvP1Percentage.setTextColor(Color.BLACK);
                llP2.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tip));
                tvP2Amount.setTextColor(Color.BLACK);
                tvP2Percentage.setTextColor(Color.BLACK);
                llInput.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tip));
                tvInputTip.setTextColor(Color.BLACK);
                tvInputTipLeft.setText("");
                break;
            case R.id.llInput:
                rlTip.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_select_tip));
                llEnterTip.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_unselect_tip));
                etInputTip.setText("0.00");
                etInputTip.setEnabled(false);
                showTotalAmount("0.00");
                llInput.setBackgroundColor(getResources().getColor(R.color.dark_bule));
                tvInputTip.setTextColor(Color.WHITE);
                llP1.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tip));
                tvP1Amount.setTextColor(Color.BLACK);
                tvP1Percentage.setTextColor(Color.BLACK);
                llP2.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tip));
                tvP2Amount.setTextColor(Color.BLACK);
                tvP2Percentage.setTextColor(Color.BLACK);
                llP3.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_tip));
                tvP3Amount.setTextColor(Color.BLACK);
                tvP3Percentage.setTextColor(Color.BLACK);
                tvInputTipLeft.setText("");
                break;
            case R.id.rtPay:
                String payAmount = ciTotal.getTvRight().getText().toString().replace(currency, "");
                String tipsAmount = ciInputTip.getEtRight().getText().toString();
                if (TextUtils.isEmpty(payAmount)) {
                    payAmount = "0";
                }
                if (getActivity() instanceof PayBtnClickListener) {
                    ((PayBtnClickListener) getActivity()).onPayBtnClick(payAmount, tipsAmount);
                }
                break;
        }
    }

    private void showTotalAmount(String tips) {
        String totalAmount = currency + Calculater.plus(ciSaleAmount.getTvRight().getText().toString().replace(currency, ""), tips);
        ciTotal.getTvRight().setText(totalAmount);
        tvShowPayTotalRMB.setText(getString(R.string.approxCurrency) + String.format("%.2f", Float.parseFloat(Calculater.multiply(totalAmount.replace(currency, ""), exchangeRate))));
        String payAmount = ciTotal.getTvRight().getText().toString().replace(currency, "");
        if (payAmount == null) {
            payAmount = "0.00";
        }
        String tipsAmount = ciInputTip.getEtRight().getText().toString();
        if (tipsAmount == null) {
            tipsAmount = "0.00";
        }
        onSaveListener.onSave(payAmount, tipsAmount);
    }

    public interface OnSaveListener {
        void onSave(String payAmount, String tipsAmount);
    }

}