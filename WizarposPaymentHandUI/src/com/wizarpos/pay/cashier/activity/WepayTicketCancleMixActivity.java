package com.wizarpos.pay.cashier.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.Response;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.ticket.util.TicketManagerUtil;
import com.wizarpos.pay.cashier.presenter.transaction.impl.MixPayTransaction;
import com.wizarpos.pay.cashier.view.input.InputInfoActivity;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.motionpay.pay2.lite.R;

public class WepayTicketCancleMixActivity extends TransactionActivity {
    private static final int REQUEST_CODE = 10001;
    protected static final String LOG_TAG = WepayTicketCancleMixActivity.class.getName();

    public static final String REDUCE_COST_TAG = "reduceCostTag";
    public static final String WEIXIN_CODE_TAG = "weixinCodeTag";
    
    /** 券改造*/
    private ArrayList<TicketInfo> addTicketList = new ArrayList<TicketInfo>();
    /** 应付金额*/
    private String shouldPayAmount;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setMainView(R.layout.ticket_use_activity);
        setData();
    }

    private void setData() {
        setTitleText(getResources().getString(R.string.wepay_ticket_cancle));
        showTitleBack();
        shouldPayAmount = getIntent().getStringExtra(Constants.initAmount);
        toInputView(this, getResources().getString(R.string.wepay_ticket_cancle), false, getIntent(), REQUEST_CODE);
        addTicketList = (ArrayList<TicketInfo>) getIntent().getSerializableExtra("addTicketList");
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
        LogEx.d("微信卡券核销", "微信卡券核销开始,券号:" + result);
        getTicketInfo(result);
    }

    /**
     * @Author: Huangweicai
     * @date 2015-9-6 下午4:38:11
     * @Description: 查询券信息
     */
    private void getTicketInfo(final String authCode) {
        MixPayTransaction trans = new MixPayTransaction(this);
        trans.getWepayTicketInfo(authCode,shouldPayAmount, new ResultListener() {

            @Override
            public void onSuccess(Response response) {
                JSONObject jsonObject = JSONObject.parseObject(response.getResult().toString());
                // reduceCost 券的金额
                String reduceCost = jsonObject.getJSONObject("card").getJSONObject("cash").getString("reduce_cost");
                String cardType = jsonObject.getJSONObject("card").getString("cardType");//券类型
				TicketInfo ticketInfo = TicketManagerUtil.getTicketInfoFromWeTicket(reduceCost, cardType);
				Response mResponse = TicketManagerUtil.verifyAddTicket(ticketInfo, addTicketList);
				if (mResponse.code != 0)
				{
					Toast.makeText(WepayTicketCancleMixActivity.this, mResponse.msg, 0).show();
					WepayTicketCancleMixActivity.this.finish();
					return;
				}
				addTicketList.add(TicketManagerUtil.getTicketInfoFromWeTicket(shouldPayAmount, cardType));
                if (!TextUtils.isEmpty(reduceCost)) {
                    Intent intent = new Intent();
                    intent.putExtra(REDUCE_COST_TAG, Long.valueOf(reduceCost));
                    intent.putExtra(WEIXIN_CODE_TAG, authCode);
                    intent.putExtra("ticketInfo", TicketManagerUtil.getTicketInfoFromWeTicket(shouldPayAmount, cardType));
                    setResult(RESULT_OK, intent);
                    WepayTicketCancleMixActivity.this.finish();
                    return;
                }
                LogEx.e(LOG_TAG, "reduceCost is empty!");
            }

            @Override
            public void onFaild(Response response) {
                LogEx.e(LOG_TAG, "onFaild");
                Toast.makeText(WepayTicketCancleMixActivity.this, response.getMsg(), 0).show();
                WepayTicketCancleMixActivity.this.finish();
            }
        });
    }

}
