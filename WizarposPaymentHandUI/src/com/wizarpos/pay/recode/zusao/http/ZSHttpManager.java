package com.wizarpos.pay.recode.zusao.http;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.pay_tems.bat.entities.BatNewReq;
import com.wizarpos.pay.cashier.pay_tems.wepay.NetWorkUtils;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.recode.http.BaseHttpManager;
import com.wizarpos.pay.recode.http.HttpNewCallback;
import com.wizarpos.pay.recode.zusao.bean.req.ZSCloseOrderNoReq;
import com.wizarpos.pay.recode.zusao.bean.req.ZSGetQrCode953Req;
import com.wizarpos.pay.recode.zusao.bean.req.ZSQueryOrderStatusReq;
import com.wizarpos.pay.recode.zusao.bean.resp.ZSGetQrCode953Resp;
import com.wizarpos.pay.recode.zusao.bean.resp.ZSQueryOrderStatusResp;
import com.wizarpos.recode.constants.HttpConstants;
import com.wizarpos.recode.sale.service.InvoiceLoginServiceImpl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ZSHttpManager extends BaseHttpManager {


    public static void doPost953(final ZSGetQrCode953Req req, final HttpNewCallback httpNewCallback) {
        Map<String, Object> params = new HashMap<>();
        params.put("mixFlag", 0);
        //todo 测试
        params.put("payChannel", req.getPayChannel());
//        params.put("payChannel", Constants.WEPAYFLAG);
        //todo 主扫==null
        params.put("authCode", req.getAuthCode());
//        params.put("authCode", null);

        params.put("captcha", AppConfigHelper.getConfig(AppConfigDef.auth_code));
//        params.put("ids", transactionInfo.getIds());//支付用券//list

      BigDecimal bigDecimal1 =   new BigDecimal(req.getRealAmount()).multiply(new BigDecimal(100));
        ;
        params.put("amount", bigDecimal1.setScale(0, BigDecimal.ROUND_DOWN).toString());

        params.put("inputAmount", req.getRealAmount());
//        params.put("cardNo", transactionInfo.getCardNo());//
//        params.put("rechargeOn", transactionInfo.getRechargeOn());
//        params.put("orderId", transactionInfo.getCommoncashierOrderId());
//        params.put("isPayComingForm", transactionInfo.getIsPayComingForm());
        Map<String, Object> marketList = new HashMap<>();
        params.put("payMarketActivity", marketList);
//        params.put("wmHxInfo", transactionInfo.getBatTicket());
        //todo
        params.put("flag", req.getFlag());
//        params.put("flag", "weixin_native");
        params.put("tipAmount", req.getTipAmount());
        params.put(HttpConstants.API_953_PARAM.INVOICENO.getKey(), InvoiceLoginServiceImpl.getInstatnce().getAppconfigInvoiceValue());

        BatNewReq batNewReq = new BatNewReq();
        batNewReq.setGoods_info(TextUtils.isEmpty(null) ? "第三方支付" : null);
        batNewReq.setSpbill_create_ip(NetWorkUtils.getLocalIpAddress(PaymentApplication.getInstance()));
        batNewReq.setTerminal_no(AppConfigHelper.getConfig(AppConfigDef.sn));
        batNewReq.setStore_id("");
        batNewReq.setPayee_term_id(AppConfigHelper.getConfig(AppConfigDef.alipay_payeeTermId));
        Gson gson = new Gson();
        String commonReqJson = gson.toJson(batNewReq);
        params.put("paramJsonObject", commonReqJson);
        doPost(Constants.SC_953_BAT_COMMON_PAY, params, new HttpNewCallback() {
            @Override
            public <T> void onSuccess(T t) {
                ZSGetQrCode953Resp resp = JSON.parseObject((String) t, ZSGetQrCode953Resp.class);
                httpNewCallback.onSuccess(resp);
            }

            @Override
            public <R> void onError(R r) {
                httpNewCallback.onError(r);
            }

            @Override
            public <M> void onError(int code, M r) {
                httpNewCallback.onError(code,r);
            }
        });


    }

    /**
     * 查询订单状态
     * @param req
     * @param httpNewCallback
     */
    public static void doPost954(final ZSQueryOrderStatusReq req, final HttpNewCallback httpNewCallback) {

        doPost(Constants.SC_954_ORDER_DEF_DETAIL, req, new HttpNewCallback() {
            @Override
            public <T> void onSuccess(T t) {
                ZSQueryOrderStatusResp resp = JSON.parseObject((String) t, ZSQueryOrderStatusResp.class);
                httpNewCallback.onSuccess(resp);
            }

            @Override
            public <R> void onError(R r) {
                httpNewCallback.onError(r);
            }

            @Override
            public <M> void onError(int code, M r) {
                httpNewCallback.onError(code,r);
            }
        });


    }



    /**
     * 关闭订单
     * @param req
     * @param httpNewCallback
     */
    public static void doPost962(final ZSCloseOrderNoReq req, final HttpNewCallback httpNewCallback) {

        doPost(Constants.SC_962_CLOSE_ORDER_NO, req, new HttpNewCallback() {
            @Override
            public <T> void onSuccess(T t) {
                ZSQueryOrderStatusResp resp = JSON.parseObject((String) t, ZSQueryOrderStatusResp.class);
                httpNewCallback.onSuccess(resp);
            }

            @Override
            public <R> void onError(R r) {
                httpNewCallback.onError(r);
            }

            @Override
            public <M> void onError(int code, M r) {
                httpNewCallback.onError(code,r);
            }
        });


    }
}
