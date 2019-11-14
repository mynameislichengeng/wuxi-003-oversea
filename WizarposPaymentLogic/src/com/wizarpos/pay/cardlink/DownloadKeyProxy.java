package com.wizarpos.pay.cardlink;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.hspos.api.EnumCommand;
import com.wizarpos.hspos.api.EnumProgressCode;
import com.wizarpos.hspos.api.SettleInfo;
import com.wizarpos.hspos.api.TransInfo;

/**
 * 下载主秘钥
 * Created by wu on 16/3/23.
 */
public class DownloadKeyProxy extends BaseCardLinkProxy {

    private static final String MSG_ERR_STATE = "交易状态错乱";

    private static final String LOG_TAG = DownloadKeyProxy.class.getSimpleName();

    private CardLinkListener cardLinkListener;

    public DownloadKeyProxy(Context context) {
        super(context);
    }

    /**
     * 下载 key
     * setParam --> InitKey --> Login (成功后) --> DownloadAID/DownloadCAPK
     */
    public void downloadKey() {
        cardLinkPresenter.onSetParam();
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
        if (cmd == EnumCommand.SetParam) {
            cardLinkPresenter.onKey();
        } else if (cmd == EnumCommand.DownloadAID) {
            cardLinkPresenter.onDownloadCAPK();
        } else if (cmd == EnumCommand.DownloadCAPK) {
            transSuccess(cmd);
        } else if (cmd == EnumCommand.InitKey) {
            cardLinkPresenter.onLogin(); //下载主秘钥完成后签到
        } else if (cmd == EnumCommand.Login) {
            cardLinkPresenter.onDownloadAID();
        } else {
            transFaild(cmd, MSG_ERR_STATE);
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

    private void transSuccess(EnumCommand cmd) {
        cardLinkListener.onTransSucceed(cmd);
    }

    private void transFaild(EnumCommand cmd, String message) {
        cardLinkListener.onTransFailed(cmd, message);
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
