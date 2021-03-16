package com.wizarpos.pay.cashier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.pay.cashier.model.PayTranRsp;
import com.wizarpos.pay.cashier.model.TranLog;
import com.wizarpos.pay.cashier.presenter.transaction.impl.TransactionCancelPresenter;
import com.wizarpos.pay.cashier.thrid_app_controller.ThirdAppTransactionController;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.view.util.Constents;
import com.motionpay.pay2.lite.R;

import java.util.Date;

public class RepealTransactionResultActivity extends TransactionActivity {
    private TextView tvTransType, tvPayAmount, tvTransTime, tvTransSerialNum;
    private TransactionCancelPresenter presenter;
    private String serialNum = null;
    private PayTranRsp bean;
    private boolean isSuccess;
    private String refundAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new TransactionCancelPresenter(this);
        initView();
    }

    private void initView() {
        setMainView(R.layout.repeal_transaction_result_activity);
        setTitleText(getResources().getString(R.string.trans_cancle));
        showTitleBack();
        tvTransType = (TextView) findViewById(R.id.tv_trans_type);
        tvPayAmount = (TextView) findViewById(R.id.tv_pay_amount);
        tvTransTime = (TextView) findViewById(R.id.tv_trans_time);
        tvTransSerialNum = (TextView) findViewById(R.id.tv_trans_serial_num);
        int[] btnIds = {R.id.btn_repeal_trans};
        setOnClickListenerByIds(btnIds, this);
        serialNum = getIntent().getStringExtra("serialNum");
        progresser.showProgress();
        presenter.getTransactionDetial(serialNum, new ResultListener() {
            @Override
            public void onSuccess(Response response) {
                progresser.showContent();
                bean = (PayTranRsp) response.result;
                if (bean.getPayTran() != null && bean.getPayTran().isEmpty() == false) {
                    TranLog tranLog = bean.getPayTran().get(0);
                    String transAmount = Calculater.formotFen(String.valueOf(tranLog.getTran_amount()));
                    String transDes = tranLog.getTran_desc();
                    Date transTime = tranLog.getTran_time();
                    tvTransType.setText(transDes);
                    tvPayAmount.setText(transAmount);
                    tvTransTime.setText(DateUtil.format(transTime, "yyyy-MM-dd HH:mm:ss"));
                    tvTransSerialNum.setText(serialNum);
                }
            }

            @Override
            public void onFaild(Response response) {
                progresser.showContent();
                UIHelper.ToastMessage(RepealTransactionResultActivity.this, response.msg);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_repeal_trans:
                if (bean == null) {
                    return;
                }
                progresser.showProgress();
                presenter.cancel(bean, false, refundAmount, new ResultListener() {
                    @Override
                    public void onSuccess(Response response) {
                        isSuccess = true;
                        String msg = response.msg;
                        Intent intent = new Intent();
                        intent.setAction(Constents.PAYSUCCESSACTION);
                        intent.putExtra("value", "success");
                        if (!ThirdAppTransactionController.getInstance().isInservice()) {
                            UIHelper.ToastMessage(RepealTransactionResultActivity.this, TextUtils.isEmpty(msg) ? msg : getResources().getString(R.string.cancle_success));
                        }
                        LocalBroadcastManager.getInstance(RepealTransactionResultActivity.this).sendBroadcast(intent);
                        serviceSuccess(getIntent());// 撤销成功发送至主界面更新数据
                    }

                    @Override
                    public void onFaild(Response response) {
                        progresser.showContent();
                        if (ThirdAppTransactionController.getInstance().isInservice()) {
                            serviceFaild(response.getMsg());
                        } else {
                            UIHelper.ToastMessage(RepealTransactionResultActivity.this, response.msg);
                        }
                        finish();
                    }
                });
                break;
        }
    }

    @Override
    protected void back() {
        if (isSuccess) {
            serviceSuccess(getIntent());// 撤销成功发送至主界面更新数据
        } else {
            super.back();
        }
    }
}
