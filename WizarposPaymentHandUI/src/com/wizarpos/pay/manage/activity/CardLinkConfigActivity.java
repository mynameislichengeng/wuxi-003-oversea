package com.wizarpos.pay.manage.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wizarpos.hspos.api.EnumProgressCode;
import com.wizarpos.hspos.api.ParamInfo;
import com.wizarpos.hspos.api.TransInfo;
import com.wizarpos.pay2.lite.R;

public class CardLinkConfigActivity extends CardLinkBaseActivity {

    private static final String LOG_TAG = CardLinkConfigActivity.class.getSimpleName();

    public static Intent getStartActivity(Context context) {
        return new Intent(context, CardLinkConfigActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainView(R.layout.activity_card_link_config);
        setTitleText("收单应用配置");
        showTitleBack();

        Button btnRefund = (Button) findViewById(R.id.btnRefund);                //1
        Button btnVoidSale = (Button) findViewById(R.id.btnVoidSale);            //2
        Button btnSale = (Button) findViewById(R.id.btnSale);                    //3
        Button btnBalance = (Button) findViewById(R.id.btnBalance);              //4
        Button btnSettle = (Button) findViewById(R.id.btnSettle);                //5
        Button btnLogin = (Button) findViewById(R.id.btnLogin);                  //6
        Button btnKey = (Button) findViewById(R.id.btnKey);                      //7
        Button btnSetParam = (Button) findViewById(R.id.btnSetParam);            //8
        Button btnDownloadAID = (Button) findViewById(R.id.btnDownloadAID);      //9
        Button btnDownloadCAPK = (Button) findViewById(R.id.btnDownloadCAPK);    //10

        btnRefund.setOnClickListener(this);                                      //1
        btnVoidSale.setOnClickListener(this);                                    //2
        btnSale.setOnClickListener(this);                                        //3
        btnBalance.setOnClickListener(this);                                     //4
        btnSettle.setOnClickListener(this);                                      //5
        btnLogin.setOnClickListener(this);                                       //6
        btnKey.setOnClickListener(this);                                         //7
        btnSetParam.setOnClickListener(this);                                    //8
        btnDownloadAID.setOnClickListener(this);                                 //9
        btnDownloadCAPK.setOnClickListener(this);                                //10
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnRefund:
                doRefundAction();
                break;
            case R.id.btnVoidSale:
                doVoidSaleAction();
                break;
            case R.id.btnSale:
                doSaleAction();
                break;
            case R.id.btnBalance:
                doBalanceAction();
                break;
            case R.id.btnSettle:
                doSettleAction();
                break;
            case R.id.btnLogin:
                doLoginAction();
                break;
            case R.id.btnKey:
                doInitKeyAction();
                break;
            case R.id.btnSetParam:
                doSetParamAction();
                break;
            case R.id.btnDownloadAID:
                doDownloadAIDAction();
                break;
            case R.id.btnDownloadCAPK:
                doDownloadCAPKAction();
                break;

        }
    }

    private void doSetParamAction() {
        /**
         无锡慧银信息科技有限公司
         商户号:831300048160001
         终端号:30068086
         下载标志：未下载
         授权码:21112805225675
         */

        ParamInfo param = new ParamInfo();
        param.setMid("831300048160001");
        param.setTid("30068086");
        param.setServerIP("58.246.226.173");
        param.setServerPort(8888);
//        param.setTpdu("6000030000");
        posApi.setParam(param);


//        ParamInfo param = new ParamInfo();
//        param.setMid("831290048160033");
//        param.setTid("30017567");
//        param.setServerIP("58.246.226.173");
//        param.setServerPort(8888);
//        param.setTpdu("6000030000");
//        posApi.setParam(param);
    }

    private void doInitKeyAction() {
        TransInfo transInfo = new TransInfo();
        // 华势是联机下载主密钥，主机生成的授权码
        transInfo.setAuthCode("21112805225675");
//        transInfo.setAuthCode("123456");
        posApi.initKey(transInfo);
    }

    private void doDownloadCAPKAction() {
        posApi.downloadCAPK();
    }

    private void doDownloadAIDAction() {
        posApi.downloadAID();
    }

    private void doLoginAction() {

    }

    private void doSettleAction() {

    }

    private void doBalanceAction() {

    }

    private void doSaleAction() {

    }

    private void doVoidSaleAction() {

    }

    private void doRefundAction() {

    }

    @Override
    protected void onProgress() {
        if (curProcessCode == EnumProgressCode.InputTransAmount.getCode()) {
            transInfo.setTransAmount(1);
        } else if (curProcessCode == EnumProgressCode.InputAuthCode.getCode()) {
            transInfo.setAuthCode("123456");
        } else if (curProcessCode == EnumProgressCode.InputOldRRN.getCode()) {
            transInfo.setOldRRN("123456789012");
        } else if (curProcessCode == EnumProgressCode.InputOldTicket.getCode()) {
            transInfo.setOldTrace(123);
        } else if (curProcessCode == EnumProgressCode.InputOldTransDate.getCode()) {
            transInfo.setOldTransDate("0306"); // MMDD
        } else if (curProcessCode == EnumProgressCode.ShowTransTotal.getCode()) {
            Log.d(LOG_TAG, "交易累计：内卡借记 " + settleInfo.getCupDebitCount() + "/" + settleInfo.getCupDebitAmount()
                    + "\n外卡借记 " + settleInfo.getAbrDebitCount() + "/" + settleInfo.getAbrDebitAmount());
        }
    }

    @Override
    protected void onSuccess() {
        switch (curCommand) {
            // 管理类
            // 1.获取参数
            case GetParam:
                Toast.makeText(CardLinkConfigActivity.this, "获取参数 完成", Toast.LENGTH_SHORT).show();
                break;
            // 2.设置参数
            case SetParam:
                Toast.makeText(CardLinkConfigActivity.this, "设置参数 完成", Toast.LENGTH_SHORT).show();
                break;
            // 3.下载主密钥
            case InitKey:
                Toast.makeText(CardLinkConfigActivity.this, "下载主密钥 完成", Toast.LENGTH_SHORT).show();
                break;
            // 4.签到
            case Login:
                Toast.makeText(CardLinkConfigActivity.this, "签到 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]", Toast.LENGTH_SHORT).show();
                break;
            // 5.结算
            case Settle:
                Toast.makeText(CardLinkConfigActivity.this, "结算 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]", Toast.LENGTH_SHORT).show();
                break;
            // 金融交易类
            // 1.查询余额
            case Balance:
                Toast.makeText(CardLinkConfigActivity.this, "查询余额 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]", Toast.LENGTH_SHORT).show();
                break;
            // 2.消费
            case Sale:
                Toast.makeText(CardLinkConfigActivity.this, "消费 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]", Toast.LENGTH_SHORT).show();
                break;
            // 3.消费撤销
            case VoidSale:
                Toast.makeText(CardLinkConfigActivity.this, "消费撤销 完成 , respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]", Toast.LENGTH_SHORT).show();
                break;
            // 4.退货
            case Refund:
                Toast.makeText(CardLinkConfigActivity.this, "退货 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onError() {
        Toast.makeText(CardLinkConfigActivity.this, "错误[" + errorCode + "][" + errorMessage + "]", Toast.LENGTH_SHORT).show();
    }

}
