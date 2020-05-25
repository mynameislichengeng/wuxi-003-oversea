package com.wizarpos.recode.data.info;

import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.pay.model.TransactionInfo;

public class TranTypeManager {

    public static boolean isPayAliPay(TransactionInfo transactionInfo) {
        if (Constants.ALIPAYFLAG.equals(transactionInfo.getPayType())) {
            return true;
        }
        return false;
    }

    public static boolean isPayWechatPay(TransactionInfo transactionInfo) {
        if (Constants.WEPAYFLAG.equals(transactionInfo.getPayType())) {
            return true;
        }
        return false;
    }

    public static boolean isPayUnionPay(TransactionInfo transactionInfo) {
        if (Constants.UNION.equals(transactionInfo.getPayType())) {
            return true;
        }
        return false;
    }


    public static boolean isActivityAlipay(DailyDetailResp resp) {
        String payType = resp.getTransName();
        return isbaseAlipay(payType);
    }

    public static boolean isActivityWechat(DailyDetailResp resp) {
        String payType = resp.getTransName();
        return isbaseWechat(payType);
    }

    public static boolean isActivityUnion(DailyDetailResp resp) {
        String payType = resp.getTransName();
        return isbaseUnion(payType);
    }


    public static boolean isRefundAlipay( RefundDetailResp resp) {
        String payType = resp.getTransKind();
        return isbaseAlipay(payType);
    }

    public static boolean isRefundWechat( RefundDetailResp resp) {
        String payType = resp.getTransKind();
        return isbaseWechat(payType);
    }

    public static boolean isRefundUnion( RefundDetailResp resp) {
        String payType = resp.getTransKind();
        return isbaseUnion(payType);
    }


    private static boolean isbaseAlipay(String payType) {
        if (payType.contains("Ali")) {
            return true;
        }
        return false;
    }






    private static boolean isbaseWechat(String payType) {
        if (payType.contains("Wechat")) {
            return true;
        }
        return false;
    }

    private static boolean isbaseUnion(String payType) {
        if (payType.contains("Union")) {
            return true;
        }
        return false;
    }
}
