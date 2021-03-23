package com.wizarpos.pay.recode.zusao.bean.connect;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.recode.util.date.SimpleDateManager;
import com.wizarpos.pay.recode.zusao.bean.resp.ZSQueryOrderStatusResp;
import com.wizarpos.pay.recode.zusao.constants.ZsPayChannelEnum;

import java.util.Date;

public class ZsConnectIntentBeanResp {

    private String KEY_RESPONSE_MERCH_NAME;

    private String KEY_RESPONSE_TIME;

    private String KEY_RESPONSE_DATE;

    private String KEY_RESPONSE_TID;

    private String KEY_RESPONSE_MID;

    private String KEY_RESPONSE_TRANSNAME;

    private String KEY_RESPONSE_CARDLABEL;

    private String KEY_RESPONSE_BASEAMT;

    private String KEY_RESPONSE_TIPAMT;

    private String KEY_RESPONSE_TOTALAMT;

    private String KEY_RESPONSE_TRACENUM;

    private String KEY_RESPONSE_TRANSREF;

    private String KEY_RESPONSE_USER_DEFINED_ECHO_DATA;


    public String getKEY_RESPONSE_MERCH_NAME() {
        return KEY_RESPONSE_MERCH_NAME;
    }

    public void setKEY_RESPONSE_MERCH_NAME(String KEY_RESPONSE_MERCH_NAME) {
        this.KEY_RESPONSE_MERCH_NAME = KEY_RESPONSE_MERCH_NAME;
    }

    public String getKEY_RESPONSE_TIME() {
        return KEY_RESPONSE_TIME;
    }

    public void setKEY_RESPONSE_TIME(String KEY_RESPONSE_TIME) {
        this.KEY_RESPONSE_TIME = KEY_RESPONSE_TIME;
    }

    public String getKEY_RESPONSE_DATE() {
        return KEY_RESPONSE_DATE;
    }

    public void setKEY_RESPONSE_DATE(String KEY_RESPONSE_DATE) {
        this.KEY_RESPONSE_DATE = KEY_RESPONSE_DATE;
    }

    public String getKEY_RESPONSE_TID() {
        return KEY_RESPONSE_TID;
    }

    public void setKEY_RESPONSE_TID(String KEY_RESPONSE_TID) {
        this.KEY_RESPONSE_TID = KEY_RESPONSE_TID;
    }

    public String getKEY_RESPONSE_MID() {
        return KEY_RESPONSE_MID;
    }

    public void setKEY_RESPONSE_MID(String KEY_RESPONSE_MID) {
        this.KEY_RESPONSE_MID = KEY_RESPONSE_MID;
    }

    public String getKEY_RESPONSE_TRANSNAME() {
        return KEY_RESPONSE_TRANSNAME;
    }

    public void setKEY_RESPONSE_TRANSNAME(String KEY_RESPONSE_TRANSNAME) {
        this.KEY_RESPONSE_TRANSNAME = KEY_RESPONSE_TRANSNAME;
    }

    public String getKEY_RESPONSE_CARDLABEL() {
        return KEY_RESPONSE_CARDLABEL;
    }

    public void setKEY_RESPONSE_CARDLABEL(String KEY_RESPONSE_CARDLABEL) {
        this.KEY_RESPONSE_CARDLABEL = KEY_RESPONSE_CARDLABEL;
    }

    public String getKEY_RESPONSE_BASEAMT() {
        return KEY_RESPONSE_BASEAMT;
    }

    public void setKEY_RESPONSE_BASEAMT(String KEY_RESPONSE_BASEAMT) {
        this.KEY_RESPONSE_BASEAMT = KEY_RESPONSE_BASEAMT;
    }

    public String getKEY_RESPONSE_TIPAMT() {
        return KEY_RESPONSE_TIPAMT;
    }

    public void setKEY_RESPONSE_TIPAMT(String KEY_RESPONSE_TIPAMT) {
        this.KEY_RESPONSE_TIPAMT = KEY_RESPONSE_TIPAMT;
    }

    public String getKEY_RESPONSE_TOTALAMT() {
        return KEY_RESPONSE_TOTALAMT;
    }

    public void setKEY_RESPONSE_TOTALAMT(String KEY_RESPONSE_TOTALAMT) {
        this.KEY_RESPONSE_TOTALAMT = KEY_RESPONSE_TOTALAMT;
    }

    public String getKEY_RESPONSE_TRACENUM() {
        return KEY_RESPONSE_TRACENUM;
    }

    public void setKEY_RESPONSE_TRACENUM(String KEY_RESPONSE_TRACENUM) {
        this.KEY_RESPONSE_TRACENUM = KEY_RESPONSE_TRACENUM;
    }

    public String getKEY_RESPONSE_TRANSREF() {
        return KEY_RESPONSE_TRANSREF;
    }

    public void setKEY_RESPONSE_TRANSREF(String KEY_RESPONSE_TRANSREF) {
        this.KEY_RESPONSE_TRANSREF = KEY_RESPONSE_TRANSREF;
    }

    public String getKEY_RESPONSE_USER_DEFINED_ECHO_DATA() {
        return KEY_RESPONSE_USER_DEFINED_ECHO_DATA;
    }

    public void setKEY_RESPONSE_USER_DEFINED_ECHO_DATA(String KEY_RESPONSE_USER_DEFINED_ECHO_DATA) {
        this.KEY_RESPONSE_USER_DEFINED_ECHO_DATA = KEY_RESPONSE_USER_DEFINED_ECHO_DATA;
    }

    public static ZsConnectIntentBeanResp convertFormPay(String json) {
        ZSQueryOrderStatusResp resp = JSON.parseObject(json, ZSQueryOrderStatusResp.class);

        ZsConnectIntentBeanResp connectResult = new ZsConnectIntentBeanResp();
        connectResult.setKEY_RESPONSE_MERCH_NAME(resp.getMerchantDef().getMerchantShortName());
        //
        String payTime = resp.getPayTime();
        if (!TextUtils.isEmpty(payTime)) {
            Date date = SimpleDateManager.fromYYYYMMDDHHMMSS(payTime);
            String time = SimpleDateManager.toStrHHMMSS(date.getTime());
            String fDate = SimpleDateManager.toStrYYYYMMDD(date.getTime());
            connectResult.setKEY_RESPONSE_TIME(time);
            connectResult.setKEY_RESPONSE_DATE(fDate);
        }
        //
        connectResult.setKEY_RESPONSE_TID(resp.getSn());


        connectResult.setKEY_RESPONSE_TRANSREF(resp.getThirdTradeNo());

        connectResult.setKEY_RESPONSE_MID(resp.getMerchantDef().getMid());

        connectResult.setKEY_RESPONSE_TRANSNAME("SALE");

        connectResult.setKEY_RESPONSE_CARDLABEL(ZsPayChannelEnum.of(resp.getPayType()).getDesc());

        connectResult.setKEY_RESPONSE_TIPAMT(String.valueOf(resp.getTipAmount()));

        connectResult.setKEY_RESPONSE_TOTALAMT(String.valueOf(resp.getAmount()));

        connectResult.setKEY_RESPONSE_TRACENUM(resp.getId());

        connectResult.setKEY_RESPONSE_USER_DEFINED_ECHO_DATA(resp.getMerchantTradeCode());

        return connectResult;
    }


}
