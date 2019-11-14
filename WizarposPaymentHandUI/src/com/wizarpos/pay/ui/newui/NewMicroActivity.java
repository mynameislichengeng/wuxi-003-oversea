package com.wizarpos.pay.ui.newui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wizarpos.barcode.scanner.ScannerResult;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.pay_tems.bat.inf.BatCommonTransaction;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionFactory;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.OrderDef;
import com.wizarpos.pay.ui.widget.CommonToastUtil;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.wizarpos.pay2.lite.R;

/**
 * copy from{@link com.wizarpos.pay.cashier.activity.UnionPayMicroActivity}
 */

public class NewMicroActivity extends NewThirdpayScanActivity implements OnClickListener {
    private BatCommonTransaction batTransaction;
    private TextView tvScanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String realAmount = intent.getStringExtra(Constants.realAmount); //增加realAmount验证,如果没有realAmount,则将initAmount设为realAmount wu
        if (TextUtils.isEmpty(realAmount)) {
            realAmount = getIntent().getStringExtra(Constants.initAmount);
            intent.putExtra(Constants.realAmount, realAmount);
        }
        intent.putExtra("payTypeFlag", Constants.UNION_BAT);
        batTransaction = TransactionFactory.newCommonTransaction(this);
        batTransaction.handleIntent(intent);
        initView();
    }

    private void initView() {
//        setTitleText("移动支付");
//        if (DeviceManager.getInstance().isWizarDevice() == false) { //隐藏扫一扫按钮
//            findViewById(R.id.rlMicroPay).setVisibility(View.GONE);
//            findViewById(R.id.llBtm).requestLayout();
//        } else {
//            findViewById(R.id.btnMicroPay).setVisibility(View.GONE);
//        }
        tvScanResult = ((TextView) findViewById(R.id.scan_result));
    }

    @Override
    protected void toMicroPay() {
//		toTenpayNativeTransaction(this, getIntent());
//		this.finish();
    }

    protected void checkOrder() {
        progresser.showProgress();
        batTransaction.checkOrder(batTransaction.getTransactionInfo().getTranId(), new BasePresenter.ResultListener() {
            @Override
            public void onSuccess(Response response) {
                progresser.showContent();
                OrderDef def = (OrderDef) response.result;
                if (OrderDef.STATE_PAYED == def.getState()) {
                    toSuccess();
                } else {
                    CommonToastUtil.showMsgBelow(NewMicroActivity.this, CommonToastUtil.LEVEL_SUCCESS, response.msg);
//                    UIHelper.ToastMessage(NewMicroActivity.this, response.msg);
                }

            }

            @Override
            public void onFaild(Response response) {
                progresser.showContent();
                CommonToastUtil.showMsgBelow(NewMicroActivity.this, CommonToastUtil.LEVEL_ERROR, response.msg);
//                UIHelper.ToastMessage(NewMicroActivity.this, response.msg);
//				showPayFaildDialog(response.msg);
            }
        });
    }

    @Override
    protected void onScanSuccess(ScannerResult scannerResult) {
        String authCode = scannerResult.getResult();
        trans(authCode);
    }

    /**
     * 获取授权码后，进行交易
     *
     * @param authCode
     */
    private void trans(final String authCode) {
        progresser.showProgress();
        batTransaction.commonPay("", Constants.UNIONFLAG, authCode, new BasePresenter.ResultListener() {

            @Override
            public void onSuccess(Response response) {
//                UIHelper.ToastMessage(NewMicroActivity.this, "");
                toSuccess();
            }

            @Override
            public void onFaild(final Response response) {
                progresser.showContent();
                if (response.code == 1) {//需要输入密码
//                        showRemindInfo(response.msg);
                    CommonToastUtil.showMsgBelow(NewMicroActivity.this, CommonToastUtil.LEVEL_WARN, "" + response.msg);
                    tvScanResult.setText("please wait for customer input pin");
                } else if (response.code == 2) {//验证码错误@hong [20160602]
                    DialogHelper2.showChooseDialog(NewMicroActivity.this, response.msg, new DialogHelper2.DialogChoiseListener() {
                        @Override
                        public void onOK() {
                            progresser.showProgress();
                            //替换本地保存的验证码
                            AppConfigHelper.setConfig(AppConfigDef.auth_code, response.getResult().toString());
                            //继续支付
                            batTransaction.commonPay("", Constants.UNIONFLAG, authCode, new BasePresenter.ResultListener() {
                                @Override
                                public void onSuccess(Response response) {
                                    progresser.showContent();
//                                    UIHelper.ToastMessage(NewMicroActivity.this, "Success");
                                    toSuccess();
                                }

                                @Override
                                public void onFaild(Response response) {
                                    progresser.showContent();
                                    if (response.code == 1) {//需要输入密码
//                                            showRemindInfo(response.msg);
                                        CommonToastUtil.showMsgBelow(NewMicroActivity.this, CommonToastUtil.LEVEL_WARN, "" + response.msg);
                                    } else {
                                        CommonToastUtil.showMsgAbove(NewMicroActivity.this
                                                , CommonToastUtil.LEVEL_ERROR, response.msg);
                                        resumeScan(2000);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onNo() {
//                            finish();
                        }
                    });
                } else {
                    progresser.showContent();
                    CommonToastUtil.showMsgAbove(NewMicroActivity.this
                            , CommonToastUtil.LEVEL_ERROR, "" + response.msg);
                    resumeScan(2000);
                }
            }
        });
    }

    private void toSuccess() {
        deliverResult(batTransaction.isMixTransaction(), batTransaction.bundleResult());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (batTransaction != null) {
            batTransaction.onDestory();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(NewMainActivity.getReturnIntent(this));
        super.onBackPressed();
    }

    @Override
    public void display(Response response) {
        String authCode = response.getResult().toString();
        trans(authCode);
    }


}
