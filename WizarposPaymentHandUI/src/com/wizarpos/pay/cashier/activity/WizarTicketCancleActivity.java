package com.wizarpos.pay.cashier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.model.TicketDef;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.ticket.TicketManagerFactory;
import com.wizarpos.pay.cashier.presenter.ticket.inf.TicketManager;
import com.wizarpos.pay.cashier.view.input.InputInfoActivity;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.GetCommonTicketInfoResp;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.motionpay.pay2.lite.R;

public class WizarTicketCancleActivity extends TransactionActivity {
    private static final int REQUEST_CODE = 10001;
    private TicketManager ticketManager;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        ticketManager = TicketManagerFactory.createCommonTicketManager(WizarTicketCancleActivity.this);
        setData();
    }

    private void setData() {
        setTitleText(getResources().getString(R.string.wizar_ticket_cancle));
        showTitleBack();
        toInputView(this, getResources().getString(R.string.wizar_ticket_cancle), false, getIntent(), REQUEST_CODE);
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

    public void onScan(final String result, boolean isScan) {
        LogEx.d("慧商卡券核销", "慧商卡券核销开始,券号:" + result);
        getTicketInfo(result);
    }

    private void getTicketInfo(String result) {
        if(TextUtils.isEmpty(result)){
            return;
        }else if(!result.startsWith("T")){
            result = "T" + AppConfigHelper.getConfig(AppConfigDef.mid) + result;
        }
        progresser.showProgress();
        ticketManager.getTicketInfo(result, "0", new ResultListener() {
            @Override
            public void onSuccess(Response response) {
                progresser.showContent();
                LogEx.d("result", String.valueOf(response.getResult()));
                GetCommonTicketInfoResp commonTicketInfoResp = (GetCommonTicketInfoResp) response.result;
                showTicketInfoDialog(commonTicketInfoResp);
            }

            @Override
            public void onFaild(Response response) {
                progresser.showContent();
                progresser.showError(response.getMsg(), false);
            }
        },"N","0","0");
    }

    private void showTicketInfoDialog(GetCommonTicketInfoResp commonTicketInfoResp) {
        final TicketInfo ticketInfo = commonTicketInfoResp.getTicketInfo();
        String ticketShow = "券名称:" + ticketInfo.getTicketDef().getTicketName() + "\n" + "券类型:" + TicketDef.getTicketTypeNameByTicketType(ticketInfo.getTicketDef().getTicketType()) + "\n截止日期:" + DateUtil.format(ticketInfo.getExpriyTime(), DateUtil.P1);
        DialogHelper2.showChoiseDialog(WizarTicketCancleActivity.this, ticketShow, new DialogHelper2.DialogChoiseListener() {

            @Override
            public void onNo() {
                finish();
            }

            @Override
            public void onOK() {
                // 核销
                cancelWizarTicket(ticketInfo);
            }
        });
    }

    private void cancelWizarTicket(final TicketInfo ticketInfo) {
        ticketManager.passWizarGiftTicket(ticketInfo.getId(), new ResultListener() {

            @Override
            public void onSuccess(Response response) {
                progresser.showContent();
                ticketManager.printWizarGiftTicket(ticketInfo);
                LogEx.d("慧商卡券核销", "慧商卡券核销成功");
                UIHelper.ToastMessage(WizarTicketCancleActivity.this, "核销成功");
                WizarTicketCancleActivity.this.finish();
            }

            @Override
            public void onFaild(Response response) {
                progresser.showContent();
                LogEx.d("慧商卡券核销", "慧商卡券核销失败");
                UIHelper.ToastMessage(WizarTicketCancleActivity.this, response.msg);
                WizarTicketCancleActivity.this.finish();
            }
        });
    }

}
