package com.wizarpos.pay.cardlink;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.hspos.api.EnumCommand;
import com.wizarpos.hspos.api.EnumProgressCode;
import com.wizarpos.hspos.api.SettleInfo;
import com.wizarpos.hspos.api.TransInfo;

/**
 * 日结及签到
 * Created by wu on 16/3/23.
 */
public class SettleAndLoginProxy extends BaseCardLinkProxy {

    private CardLinkListener cardLinkListener;

    public SettleAndLoginProxy(Context context) {
        super(context);
    }

    /**
     * 结算
     */
    public void settle() {
        cardLinkPresenter.onSettle();
    }

    /**
     * 签到
     */
    public void login() {
        cardLinkPresenter.onLogin();
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
        if (cmd == EnumCommand.Settle) {
            printSettle();//打印日结单
            if (cardLinkListener != null) {
                cardLinkListener.onTransSucceed(cmd);
            }
        } else if (cmd == EnumCommand.Login) { //签到完成后结算
            settle();
        } else {
            cardLinkListener.onTransSucceed(cmd);
        }
    }

    @Override
    public void onProgress(EnumCommand cmd, int progressCode, String message) {
        if (progressCode == EnumProgressCode.ProcessOnline.getCode()
                || progressCode == EnumProgressCode.RequestCard.getCode()
                || progressCode == EnumProgressCode.InputPIN.getCode()
                || progressCode == EnumProgressCode.InputAuthCode.getCode()
                ) { //仅给出提示
            cardLinkListener.onProgress(cmd, progressCode, message + "[" + progressCode + "]", false);
            cardLinkPresenter.continueTrans();
        } else if (progressCode == EnumProgressCode.InputOldRRN.getCode()
                || progressCode == EnumProgressCode.InputOldTicket.getCode()
                || progressCode == EnumProgressCode.InputOldTransDate.getCode()
                || progressCode == EnumProgressCode.InputExpiryDate.getCode()
                ) { //给出提示,同时等待下一步动作
            cardLinkListener.onProgress(cmd, progressCode, message + "[" + progressCode + "]", true);
        } else if (progressCode == EnumProgressCode.ConfirmPAN.getCode()) {
            TransInfo transInfo = cardLinkPresenter.getTransInfo();
            String msg = message + "\n卡号: " + transInfo.getPan();
            cardLinkListener.onProgress(cmd, progressCode, msg, true);
        } else if (progressCode == EnumProgressCode.ShowTransTotal.getCode()) { //交易累计
            SettleInfo settleInfo = cardLinkPresenter.getSettleInfo();
            String msg = "交易累计：内卡借记 " + settleInfo.getCupDebitCount() + "/" + settleInfo.getCupDebitAmount()
                    + "\n外卡借记 " + settleInfo.getAbrDebitCount() + "/" + settleInfo.getAbrDebitAmount();
            cardLinkListener.onProgress(cmd, progressCode, msg, false);
            cardLinkPresenter.continueTrans();
        } else if (progressCode == EnumProgressCode.ShowTransInfo.getCode()) {
            cardLinkPresenter.continueTrans();
        } else if (progressCode == EnumProgressCode.InputAdminPass.getCode()) {
            cardLinkPresenter.continueTrans();
        } else {
            cardLinkListener.onProgress(cmd, progressCode, message + "[" + progressCode + "]", true);
        }
    }

    private void printSettle() {
        CardLinkPrintController printService = new CardLinkPrintController(context);
        printService.printSettle(cardLinkPresenter.getSettleInfo());
    }

    public TransInfo getTransInfo() {
        return cardLinkPresenter.getTransInfo();
    }

    public void setEndTransListener(CardLinkPresenter.EndTransListener endTransListener) {
        cardLinkPresenter.setEndTransListener(endTransListener);
    }

    public void setCardLinkListener(CardLinkListener cardLinkListener) {
        this.cardLinkListener = cardLinkListener;
    }

}
