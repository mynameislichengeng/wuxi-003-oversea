package com.wizarpos.pay.cardlink;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.wizarpos.hspos.api.EnumCommand;
import com.wizarpos.hspos.api.EnumProgressCode;
import com.wizarpos.hspos.api.ParamInfo;
import com.wizarpos.hspos.api.SettleInfo;
import com.wizarpos.hspos.api.TransInfo;

/**
 * 撤销
 * Created by wu on 16/3/23.
 */
public class VoidProxy extends BaseCardLinkProxy {

    private static final String LOG_TAG = VoidProxy.class.getSimpleName();

    private CardLinkListener cardLinkListener;

    public VoidProxy(Context context) {
        super(context);
    }

    public void voidSale() {
        cardLinkPresenter.onGetParam();
    }

    public void continueTrans() {
        cardLinkPresenter.continueTrans();
    }

    @Override
    public void onTransFailed(EnumCommand cmd, String error, String message) {
        if (cardLinkListener != null) {
            String failedMsg = (TextUtils.isEmpty(message) ? "" : message) + "[" + error + "]";
            cardLinkListener.onTransFailed(cmd, failedMsg);
        }
    }

    @Override
    public void onTransSucceed(EnumCommand cmd, Object params) {
        if (cmd == EnumCommand.GetParam) {
            ParamInfo paramInfo = cardLinkPresenter.getParamInfo();
            if (paramInfo != null) {
                String termCap = paramInfo.getTermCap();
                Log.d(LOG_TAG, "termCap:" + termCap);
            }
            cardLinkPresenter.onVoidSale();
        } else if (cmd == EnumCommand.VoidSale) {
            printVoidSale();//打印撤销
            if (cardLinkListener != null) {
                cardLinkListener.onTransSucceed(cmd);
            }
        } else {
            if (cardLinkListener != null) {
                cardLinkListener.onTransSucceed(cmd);
            }
        }
    }

    @Override
    public void onProgress(EnumCommand cmd, int progressCode, String message) {
        if (progressCode == EnumProgressCode.ProcessOnline.getCode()
                || progressCode == EnumProgressCode.RequestCard.getCode()
                || progressCode == EnumProgressCode.InputPIN.getCode()
                || progressCode == EnumProgressCode.InputAuthCode.getCode()
                ) { //仅给出提示
            if (cardLinkListener != null) {
                cardLinkListener.onProgress(cmd, progressCode, message + "[" + progressCode + "]", false);
            }
            cardLinkPresenter.continueTrans();
        } else if (progressCode == EnumProgressCode.InputOldRRN.getCode()
                || progressCode == EnumProgressCode.InputOldTicket.getCode()
                || progressCode == EnumProgressCode.InputOldTransDate.getCode()
                || progressCode == EnumProgressCode.InputExpiryDate.getCode()
                ) { //给出提示,同时等待下一步动作
            if (cardLinkListener != null) {
                cardLinkListener.onProgress(cmd, progressCode, message + "[" + progressCode + "]", true);
            }
        } else if (progressCode == EnumProgressCode.ConfirmPAN.getCode()) {
            if (cardLinkListener != null) {
                TransInfo transInfo = cardLinkPresenter.getTransInfo();
                String msg = message + "\n卡号: " + transInfo.getPan();
                cardLinkListener.onProgress(cmd, progressCode, msg, true);
            }
        } else if (progressCode == EnumProgressCode.ShowTransTotal.getCode()) { //交易累计
            SettleInfo settleInfo = cardLinkPresenter.getSettleInfo();
            String msg = "交易累计：内卡借记 " + settleInfo.getCupDebitCount() + "/" + settleInfo.getCupDebitAmount()
                    + "\n外卡借记 " + settleInfo.getAbrDebitCount() + "/" + settleInfo.getAbrDebitAmount();
            if (cardLinkListener != null) {
                cardLinkListener.onProgress(cmd, progressCode, msg, false);
            }
            cardLinkPresenter.continueTrans();
        } else if (progressCode == EnumProgressCode.ShowTransInfo.getCode()) {
            cardLinkPresenter.continueTrans();
        } else if (progressCode == EnumProgressCode.InputAdminPass.getCode()) {
            cardLinkPresenter.continueTrans();
        } else {
            if (cardLinkListener != null) {
                cardLinkListener.onProgress(cmd, progressCode, message + "[" + progressCode + "]", true);
            }
        }
    }

    public TransInfo getTransInfo() {
        return cardLinkPresenter.getTransInfo();
    }

    private void printVoidSale() {
        CardLinkPrintController printService = new CardLinkPrintController(context);
        printService.printVoid(cardLinkPresenter.getTransInfo(), 1);
        printService.printVoid(cardLinkPresenter.getTransInfo(), 2);
    }

    public void setEndTransListener(CardLinkPresenter.EndTransListener endTransListener) {
        cardLinkPresenter.setEndTransListener(endTransListener);
    }

    public void setCardLinkListener(CardLinkListener cardLinkListener) {
        this.cardLinkListener = cardLinkListener;
    }

    public void removeCardListener() {
        cardLinkListener = null;
    }
}
