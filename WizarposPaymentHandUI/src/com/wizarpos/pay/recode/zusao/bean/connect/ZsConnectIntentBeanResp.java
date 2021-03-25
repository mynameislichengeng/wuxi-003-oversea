package com.wizarpos.pay.recode.zusao.bean.connect;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.wizarpos.pay.recode.util.date.SimpleDateManager;
import com.wizarpos.pay.recode.zusao.bean.resp.ZSQueryOrderStatusResp;
import com.wizarpos.pay.recode.zusao.constants.ZsPayChannelEnum;

import java.util.Date;

public class ZsConnectIntentBeanResp {

    private String response_resultcode_key;

    private String response_code_key;

    private String response_merch_name_key;

    private String response_time_key;

    private String response_date_key;

    private String response_tid_key;

    private String response_mid_key;

    private String response_transname_key;

    private String response_cardlabel_key;

    private String response_baseamt_key;

    private String response_tipamt_key;

    private String response_totalamt_key;

    private String response_trace_key;

    private String response_transref_key;

    private String response_user_defined_echo_data;


    public String getResponse_merch_name_key() {
        return response_merch_name_key;
    }

    public void setResponse_merch_name_key(String response_merch_name_key) {
        this.response_merch_name_key = response_merch_name_key;
    }

    public String getResponse_time_key() {
        return response_time_key;
    }

    public void setResponse_time_key(String response_time_key) {
        this.response_time_key = response_time_key;
    }

    public String getResponse_date_key() {
        return response_date_key;
    }

    public void setResponse_date_key(String response_date_key) {
        this.response_date_key = response_date_key;
    }

    public String getResponse_tid_key() {
        return response_tid_key;
    }

    public void setResponse_tid_key(String response_tid_key) {
        this.response_tid_key = response_tid_key;
    }

    public String getResponse_mid_key() {
        return response_mid_key;
    }

    public void setResponse_mid_key(String response_mid_key) {
        this.response_mid_key = response_mid_key;
    }

    public String getResponse_transname_key() {
        return response_transname_key;
    }

    public void setResponse_transname_key(String response_transname_key) {
        this.response_transname_key = response_transname_key;
    }

    public String getResponse_cardlabel_key() {
        return response_cardlabel_key;
    }

    public void setResponse_cardlabel_key(String response_cardlabel_key) {
        this.response_cardlabel_key = response_cardlabel_key;
    }

    public String getResponse_baseamt_key() {
        return response_baseamt_key;
    }

    public void setResponse_baseamt_key(String response_baseamt_key) {
        this.response_baseamt_key = response_baseamt_key;
    }

    public String getResponse_tipamt_key() {
        return response_tipamt_key;
    }

    public void setResponse_tipamt_key(String response_tipamt_key) {
        this.response_tipamt_key = response_tipamt_key;
    }

    public String getResponse_totalamt_key() {
        return response_totalamt_key;
    }

    public void setResponse_totalamt_key(String response_totalamt_key) {
        this.response_totalamt_key = response_totalamt_key;
    }

    public String getResponse_trace_key() {
        return response_trace_key;
    }

    public void setResponse_trace_key(String response_trace_key) {
        this.response_trace_key = response_trace_key;
    }

    public String getResponse_transref_key() {
        return response_transref_key;
    }

    public void setResponse_transref_key(String response_transref_key) {
        this.response_transref_key = response_transref_key;
    }

    public String getResponse_user_defined_echo_data() {
        return response_user_defined_echo_data;
    }

    public void setResponse_user_defined_echo_data(String response_user_defined_echo_data) {
        this.response_user_defined_echo_data = response_user_defined_echo_data;
    }

    public String getResponse_resultcode_key() {
        return response_resultcode_key;
    }

    public void setResponse_resultcode_key(String response_resultcode_key) {
        this.response_resultcode_key = response_resultcode_key;
    }

    public String getResponse_code_key() {
        return response_code_key;
    }

    public void setResponse_code_key(String response_code_key) {
        this.response_code_key = response_code_key;
    }

    public static ZsConnectIntentBeanResp convertFormPay(String json) {
        ZSQueryOrderStatusResp resp = JSON.parseObject(json, ZSQueryOrderStatusResp.class);
        ZsConnectIntentBeanResp connectResult = new ZsConnectIntentBeanResp();
        connectResult.setResponse_resultcode_key("0000");
        connectResult.setResponse_merch_name_key(resp.getMerchantDef().getMerchantShortName());
        //
        String payTime = resp.getPayTime();
        if (!TextUtils.isEmpty(payTime)) {
            Date date = SimpleDateManager.fromYYYYMMDDHHMMSS(payTime);
            String time = SimpleDateManager.toStrHHMMSS(date.getTime());
            String fDate = SimpleDateManager.toStrYYYYMMDD(date.getTime());
            connectResult.setResponse_time_key(time);
            connectResult.setResponse_date_key(fDate);
        }
        //
        connectResult.setResponse_tid_key(resp.getSn());


        connectResult.setResponse_transref_key(resp.getThirdTradeNo());

        connectResult.setResponse_mid_key(resp.getMerchantDef().getMid());

        connectResult.setResponse_transname_key("SALE");

        connectResult.setResponse_cardlabel_key(ZsPayChannelEnum.of(resp.getPayType()).getDesc());

        connectResult.setResponse_tipamt_key(String.valueOf(resp.getTipAmount()));

        connectResult.setResponse_totalamt_key(String.valueOf(resp.getAmount()));

        connectResult.setResponse_trace_key(resp.getId());

        connectResult.setResponse_user_defined_echo_data(resp.getMerchantTradeCode());

        return connectResult;
    }


}
