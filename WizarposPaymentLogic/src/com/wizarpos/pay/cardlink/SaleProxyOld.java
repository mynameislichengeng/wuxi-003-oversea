package com.wizarpos.pay.cardlink;

import android.content.Context;
import android.util.Log;

import com.wizarpos.hspos.api.EnumCommand;
import com.wizarpos.hspos.api.EnumProgressCode;
import com.wizarpos.hspos.api.SettleInfo;
import com.wizarpos.hspos.api.TransInfo;
import com.wizarpos.pay.common.utils.Calculater;

/**
 * Created by wu on 16/3/10.
 */
public class SaleProxyOld extends SignOnProxy {

    private static final String LOG_TAG = SaleProxyOld.class.getSimpleName();

    private SaleListener saleListener;
    private int amount = -1;
    private CardLinkPrintController printService;

    public SaleProxyOld(Context context) {
        super(context);
        printService = new CardLinkPrintController(context);
    }

    public void sale(int amount) {
        this.amount = amount;
        signOn();
    }

    @Override
    public void onTransFailed(EnumCommand cmd, String error, String message) {
        super.onTransFailed(cmd, error, message);
        if (cmd == EnumCommand.Sale) {
            saleFailed(error, message);
        }
    }

    @Override
    public void onProgress(EnumCommand cmd, int progressCode, String message) {
        super.onProgress(cmd, progressCode, message);
        if (cmd == EnumCommand.Sale) {
            if (saleListener != null) {
                if (progressCode == EnumProgressCode.ProcessOnline.getCode()
                        || progressCode == EnumProgressCode.RequestCard.getCode()
                        || progressCode == EnumProgressCode.InputAdminPass.getCode()
                        || progressCode == EnumProgressCode.InputExpiryDate.getCode()
                        || progressCode == EnumProgressCode.InputPIN.getCode()
                        ) { //仅给出提示
                    saleListener.onProgress(message + "[" + progressCode + "]", false);
                    cardLinkPresenter.continueTrans();
                } else if (progressCode == EnumProgressCode.InputAuthCode.getCode()
                        || progressCode == EnumProgressCode.InputOldRRN.getCode()
                        || progressCode == EnumProgressCode.InputOldTicket.getCode()
                        || progressCode == EnumProgressCode.InputOldTransDate.getCode()
                        ) { //给出提示,同时等待下一步动作
                    saleListener.onProgress(message + "[" + progressCode + "]", true);
                } else if (progressCode == EnumProgressCode.InputTransAmount.getCode()) { // 已经设置了获取交易金额,不再提示
                    Log.d(LOG_TAG, "已经设置了获取交易金额,直接继续交易");
                    cardLinkPresenter.getTransInfo().setTransAmount(amount);//设置交易金额
                    cardLinkPresenter.continueTrans();
                } else if (progressCode == EnumProgressCode.ConfirmAmount.getCode()) {
                    TransInfo transInfo = cardLinkPresenter.getTransInfo();
                    String msg = message + "\n交易金额: " + Calculater.formotFen(transInfo.getTransAmount() + "");
                    saleListener.onProgress(msg, true);
                } else if (progressCode == EnumProgressCode.ConfirmPAN.getCode()) {
                    TransInfo transInfo = cardLinkPresenter.getTransInfo();
                    String msg = message + "\n卡号: " + transInfo.getPan();
                    saleListener.onProgress(msg, true);
                } else if (progressCode == EnumProgressCode.ShowTransTotal.getCode()) { //交易累计
                    SettleInfo settleInfo = cardLinkPresenter.getSettleInfo();
                    String msg = "交易累计：内卡借记 " + settleInfo.getCupDebitCount() + "/" + settleInfo.getCupDebitAmount()
                            + "\n外卡借记 " + settleInfo.getAbrDebitCount() + "/" + settleInfo.getAbrDebitAmount();
                    saleListener.onProgress(msg, false);
                    cardLinkPresenter.continueTrans();
                } else {
                    saleListener.onProgress(message + "[" + progressCode + "]", true);
                }
            }
        }
    }

    @Override
    public void onTransSucceed(EnumCommand cmd, Object params) {
        super.onTransSucceed(cmd, params);
        if (cmd == EnumCommand.Login) { //签到成功后调用消费
            doSaleAciton();
        } else if (cmd == EnumCommand.Sale) {
            saleSuccess(params);
        }
    }

    private void doSaleAciton() {
        cardLinkPresenter.onSale();
    }


    /**
     * 支付成功
     *
     * @param params
     */
    private void saleSuccess(Object params) {
        if (saleListener != null) {
            TransInfo transInfo = (TransInfo) params;
            if ("00".equals(transInfo.getRespCode())) {
                printService.printSale(transInfo, termCap);
                String cardNo = transInfo.getPan();
                saleListener.onSaleSuccess(cardNo);
            } else {
                saleListener.onSaleFailed(transInfo.getRespDesc() + "[" + transInfo.getRespCode() + "]");
            }
        }
    }

    /**
     * 支付失败
     *  @param error
     * @param message
     */
    private void saleFailed(String error, String message) {
        if (saleListener != null) {
            saleListener.onSaleFailed(message + "[" + error + "]");
        }
    }

    @Override
    protected void signOnFailed(String msg) {
//        super.signOnFailed();
        if (saleListener != null) {
            saleListener.onSaleFailed(msg);
        }
    }

    public void continueTrans() {
        cardLinkPresenter.continueTrans();
    }

    public void endTrans() {
        cardLinkPresenter.endTrans();
    }

    public void setSaleListener(SaleListener saleListener) {
        this.saleListener = saleListener;
    }

    public interface SaleListener {

        void onProgress(String progress, boolean continueTrans);

        void onSaleSuccess(String cardNo);

        void onSaleFailed(String message);
    }


}
