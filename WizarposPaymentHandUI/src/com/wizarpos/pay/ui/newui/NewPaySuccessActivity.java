package com.wizarpos.pay.ui.newui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ui.dialog.DialogHelper;
import com.ui.dialog.NoticeDialogFragment;
import com.wizarpos.device.printer.html.WebPrintHelper;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.recode.sale.widget.BarcodeView;
import com.wizarpos.pay.ui.widget.CommonToastUtil;
import com.wizarpos.pay.view.util.Tools;
import com.wizarpos.pay2.lite.R;
import com.wizarpos.recode.print.result.payfor.PayForPrintResultManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.List;


/**
 * Created by blue_sky on 2016/3/23.
 */
public class NewPaySuccessActivity extends BaseViewActivity {
    ImageView ivPaySuccess;
    TextView tvAmount;
    private BarcodeView barcodeView;


    private String payDes = "";

    public static final String EXTRA_PAY_TYPE = "payType";
    public static final String EXTRA_PAY_AMOUNT = "realAmount";
    public static final String EXTRA_TRANLOGID = "tranLogId";

    public static Intent getStartIntent(Context context, String payType, String amount) {
        Intent intent = new Intent(context, NewPaySuccessActivity.class);
        intent.putExtra(EXTRA_PAY_TYPE, payType);
        intent.putExtra(EXTRA_PAY_AMOUNT, amount);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        print();
    }

    private void initView() {
        setMainView(R.layout.activity_new_paysuccess);
        showTitleBack();
        setTitleText(getResources().getString(R.string.payment_success));
        findViewById(R.id.btnContiuePay).setOnClickListener(this);
        ivPaySuccess = (ImageView) findViewById(R.id.ivPaySuccess);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        barcodeView = findViewById(R.id.barcodeview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.btnContiuePay).requestFocus();
    }

    private void initData() {
        Intent payIntent = getIntent();
        if (null == payIntent) {
            CommonToastUtil.showMsgBelow(this, CommonToastUtil.LEVEL_WARN, "未获取到支付数据");
//            UIHelper.ToastMessage(this, "未获取到支付数据", Toast.LENGTH_SHORT);
            return;
        }
        String payType = payIntent.getStringExtra(EXTRA_PAY_TYPE);
        if (TextUtils.isEmpty(payType)) {
            //若非移动支付，则为银行卡支付
            payDes = "银行卡支付";
            ivPaySuccess.setImageResource(R.drawable.success_yinghangka);
        } else {
            //移动支付
            String payTypeUpCase = payType.toUpperCase();
            if ("B".equals(payTypeUpCase)) {
                payDes = "百度钱包";
                ivPaySuccess.setImageResource(R.drawable.success_baidu);
            } else if ("A".equals(payTypeUpCase)) {
                payDes = "支付宝";
                ivPaySuccess.setImageResource(R.drawable.success_zhifubao);
            } else if ("W".equals(payTypeUpCase)) {
                payDes = " 微信";
                ivPaySuccess.setImageResource(R.drawable.success_weixin);
            } else if ("T".equals(payTypeUpCase)) {
                payDes = "QQ钱包";
                ivPaySuccess.setImageResource(R.drawable.success_tenpay);
            } else if ("BANK".equals(payTypeUpCase)) {
                payDes = "银行卡支付";
                ivPaySuccess.setImageResource(R.drawable.success_yinghangka);
            } else if ("UNS".equals(payTypeUpCase)) {
                payDes = "银联支付";
                ivPaySuccess.setImageResource(R.drawable.ic_pay_union);
            }

        }
        String realAmount = payIntent.getStringExtra(EXTRA_PAY_AMOUNT);
        if (!TextUtils.isEmpty(realAmount)) {
            try {
                tvAmount.setText("$" + Tools.formatFen(Long.parseLong(realAmount)));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Log.e("error", "Long.parseLong(String) error");
            }
        }
        String transLog = payIntent.getStringExtra(EXTRA_TRANLOGID);
        if (!TextUtils.isEmpty(transLog)) {
            barcodeView.setBarcodePayFor(transLog);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnContiuePay:
                startActivity(NewMainActivity.getStartIntent(this));
                finish();
                break;
        }
    }

    @Override
    protected void onTitleBackClikced() {
        startActivity(NewMainActivity.getStartIntent(this));
        super.onTitleBackClikced();
    }

    @Override
    public void onBackPressed() {
        startActivity(NewMainActivity.getStartIntent(this));
        super.onBackPressed();
    }

    private void print() {
        int printNumber = 1;
        if (!TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.print_number))) {
            printNumber = Integer.parseInt(AppConfigHelper.getConfig(AppConfigDef.print_number));
        }
        final PrintServiceControllerProxy controller = new PrintServiceControllerProxy(this);
        switch (printNumber) {
            case 1:
                if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
//                    PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_CONTEXT)));
                    WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CONTEXT));
                } else {

                    controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CONTEXT));
//                    Constants.HANDUI_IS_BLOCK_UI = true;
//                    PrintServiceControllerProxy controller1 = new PrintServiceControllerProxy(this);
//                    String printCtxString = AppConfigHelper.getConfig(AppConfigDef.PRINT_CONTEXT_ARRAY);
//                    String customprintCxtString = AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_CONTEXT_ARRAY);
//                    List<String> values = JSON.parseArray(printCtxString, String.class);
//                    for (String str : values) {
//                        controller1.print(str, true);
//                    }clea
                }
                break;
            case 2:
                if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
                    WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CONTEXT));
//                    PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_CONTEXT)));
                } else {
                    controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CONTEXT));
                }
                final NoticeDialogFragment dialogFragment = NoticeDialogFragment.newInstance("INFORMATION", "Customer Copy?", "YES", "NO");
                dialogFragment.setListener(new DialogHelper.DialogCallbackAndNo() {
                    @Override
                    public void callback() {
                        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
                            WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_CONTEXT));
//                            PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_CONTEXT)));
                        } else {
                            controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_CONTEXT));
                        }
                    }

                    @Override
                    public void callbackNo() {
                        dialogFragment.dismiss();
                    }
                });
                dialogFragment.show(getSupportFragmentManager(), "SimpleMsgDialogFragment");
                break;
            case 3:
                if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
                    WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CONTEXT));
//                    PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_CONTEXT)));
                } else {
                    controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CONTEXT));
                }
                final NoticeDialogFragment fragmentDialog = NoticeDialogFragment.newInstance("INFORMATION", "Customer Copy?", "YES", "NO");
                fragmentDialog.setListener(new DialogHelper.DialogCallbackAndNo() {
                    @Override
                    public void callback() {
                        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
                            WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_CONTEXT));
//                            PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_CONTEXT)));
                        } else {
                            controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_CONTEXT));
                        }
                        final NoticeDialogFragment dialog = NoticeDialogFragment.newInstance("INFORMATION", "Customer Copy?", "YES", "NO");
                        dialog.setListener(new DialogHelper.DialogCallbackAndNo() {
                            @Override
                            public void callback() {
                                if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
//                                    PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_CONTEXT)));
                                    WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_CONTEXT));
                                } else {
                                    controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_CONTEXT));
                                }
                                ;
                            }

                            @Override
                            public void callbackNo() {
                                dialog.dismiss();
                            }
                        });
                        dialog.show(getSupportFragmentManager(), "SimpleMsgDialogFragment2");
                    }

                    @Override
                    public void callbackNo() {
                        fragmentDialog.dismiss();
                    }
                });
                fragmentDialog.show(getSupportFragmentManager(), "SimpleMsgDialogFragment1");
                break;
        }
    }
}
