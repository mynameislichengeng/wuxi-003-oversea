package com.wizarpos.pay.cashier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.model.ThirdTicketInfo;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.ticket.TicketManagerFactory;
import com.wizarpos.pay.cashier.presenter.ticket.inf.TicketManager;
import com.wizarpos.pay.cashier.view.TransactionFlowController;
import com.wizarpos.pay.cashier.view.input.InputInfoActivity;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.wizarpos.pay.view.util.DialogHelper2.DialogChoiseListener;
import com.motionpay.pay2.lite.R;

/**
 * 第三方卡券核销
 */
public class ThirdTicketCancleActivity extends TransactionFlowController {

    private static final int REQUEST_CODE = 1001;
    private TicketManager ticketManager;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setMainView(R.layout.ticket_use_activity);
        ticketManager = TicketManagerFactory.createCommonTicketManager(ThirdTicketCancleActivity.this);
        setData();
    }

    private void setData() {
        setTitleText(getResources().getString(R.string.third_ticket_cancle));
        showTitleBack();
//		toInputView(this, getResources().getString(R.string.third_ticket_cancle), getIntent(), REQUEST_CODE);
        toInputView(this, getResources().getString(R.string.third_ticket_cancle), false, getIntent(), REQUEST_CODE);
        progresser.showProgress();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == REQUEST_CODE && arg1 == RESULT_OK) { //调用摄像头逻辑调整 wu@[20150827]
            // 券号
            try {
                String ticketNum = arg2.getStringExtra(InputInfoActivity.content);
                if (TextUtils.isEmpty(ticketNum)) {
                    return;
                }
                boolean isScan = (arg2.getIntExtra(InputInfoActivity.type, 0) == InputInfoActivity.INPUT_TYPE_CAMERA);
                onScan(ticketNum, isScan);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.finish();
        }
    }

    public void onScan(String result, boolean isScan) {
        ticketManager.getThirdTicketInfo(result, new BasePresenter.ResultListener() {
            @Override
            public void onSuccess(Response response) {

                if (PaymentApplication.getInstance().isWemengMerchant()) { //区分微盟券
                    passWemengTicket(response);
                } else {
                    passThirdTicket(response);
                }
            }

            @Override
            public void onFaild(Response response) {
                UIHelper.ToastMessage(ThirdTicketCancleActivity.this, response.msg);
                finish();
            }
        });

    }

    private void passWemengTicket(Response response) {
        final TicketInfo ticketInfo = (TicketInfo) response.result;
        String ticketShow = "券名称:" + ticketInfo.getTicketDef().getTicketName() + "\n" + "券类型:" + ticketInfo.getTicketDef().getTicketTypeName() + "\n截止日期:" + ticketInfo.getEndDate();
        DialogHelper2.showChoiseDialog(ThirdTicketCancleActivity.this, ticketShow, new DialogChoiseListener() {

            @Override
            public void onNo() {
                finish();
            }

            @Override
            public void onOK() {
                // 核销
                ticketManager.passWeimengTicket(ticketInfo, new BasePresenter.ResultListener() {

                    @Override
                    public void onSuccess(Response response) {
                        UIHelper.ToastMessage(ThirdTicketCancleActivity.this, getResources().getString(R.string.ticket_cancle_success));
                        finish();
                    }

                    @Override
                    public void onFaild(Response response) {
                        UIHelper.ToastMessage(ThirdTicketCancleActivity.this, response.msg);
                        finish();
                    }
                });

            }
        });
    }

    private void passThirdTicket(Response response) {
        String ticketShow;
        final ThirdTicketInfo ticketInfo = (ThirdTicketInfo) response.result;
        ticketShow = ticketInfo.getName() + "金额：" + ticketInfo.getAmount() + "元";
        DialogHelper2.showChoiseDialog(ThirdTicketCancleActivity.this, ticketShow, new DialogChoiseListener() {

            @Override
            public void onNo() {
                finish();
            }

            @Override
            public void onOK() {
                // 核销
                ticketManager.passThirdTicket(ticketInfo, new BasePresenter.ResultListener() {

                    @Override
                    public void onSuccess(Response response) {
                        UIHelper.ToastMessage(ThirdTicketCancleActivity.this, getResources().getString(R.string.ticket_cancle_success));
                        finish();
                    }

                    @Override
                    public void onFaild(Response response) {
                        UIHelper.ToastMessage(ThirdTicketCancleActivity.this, response.msg);
                        finish();
                    }
                });

            }
        });
    }
}
