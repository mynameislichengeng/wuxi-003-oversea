package com.wizarpos.pay.cashier.presenter.payment.impl;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cardlink.SaleProxyOld;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;

import java.util.ArrayList;

/**
 * 银行卡消费
 *
 * @author wu
 */
public class CardPayment extends UDIDPaymentImpl implements SaleProxyOld.SaleListener {

    private ArrayList<String> cardInfo;
    private int hasICCard;

    private SaleProxyOld saleProxy;

    private ResultListener listener;

    private CardPaymetProgressListener cardPaymetProgressListener;

    public CardPayment(Context context) {
        saleProxy = new SaleProxyOld(context);
        saleProxy.setSaleListener(this);
    }

    @Override
    public boolean pay(String amount, ResultListener listener) {
        if (TextUtils.isEmpty(amount) || "0".equals(amount)) {
            listener.onFaild(new Response(1, "交易金额不能为0"));
            return false;
        }
        this.listener = listener;

        //TODO 怎么传入卡号 包括非接卡卡号
        final String cardInfo2;
        final String cardInfo3;
        if (cardInfo != null) {
            cardInfo2 = cardInfo.get(1);
            cardInfo3 = cardInfo.get(2);
        } else {
            cardInfo2 = "";
            cardInfo3 = "";
        }
        setAmount(amount);
        payCash(Integer.parseInt(amount));

        return true;
    }

    protected void payCash(int amount) {
        saleProxy.sale(amount);
    }

    @Override
    public boolean revoke(String amount, ResultListener listener) {
        return false;
    }

    public void setCardInfo(ArrayList<String> cardInfo) {
        this.cardInfo = cardInfo;
    }

    public void setHasICCard(boolean HasICCard) {
        this.hasICCard = HasICCard ? 1 : 0;
    }

    @Override
    public void onProgress(String progress, boolean continueTrans) {
        cardPaymetProgressListener.onProgress(progress, continueTrans);//不做非空判断 wu
    }

    @Override
    public void onSaleSuccess(String cardNo) {
        listener.onSuccess(new Response(0, "success", cardNo));
    }

    @Override
    public void onSaleFailed(String message) {
        listener.onFaild(new Response(-1, message));
    }

    public void continueTrans(){
        saleProxy.continueTrans();
    }

    public void endTrans(){
        saleProxy.endTrans();
    }

    public void onDestory() {

    }

    public void setCardPaymetProgressListener(CardPaymetProgressListener cardPaymetProgressListener) {
        this.cardPaymetProgressListener = cardPaymetProgressListener;
    }

    public interface CardPaymetProgressListener {
        void onProgress(String progress, boolean continueTrans);
    }
}
