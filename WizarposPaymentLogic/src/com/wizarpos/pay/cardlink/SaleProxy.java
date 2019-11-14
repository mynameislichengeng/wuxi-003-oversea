package com.wizarpos.pay.cardlink;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.wizarpos.hspos.api.EnumCommand;
import com.wizarpos.hspos.api.EnumProgressCode;
import com.wizarpos.hspos.api.ParamInfo;
import com.wizarpos.hspos.api.SettleInfo;
import com.wizarpos.hspos.api.TransInfo;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

/**
 * 消费
 * Created by wu on 16/3/23.
 */
public class SaleProxy extends BaseCardLinkProxy {

    private static final String LOG_TAG = SaleProxy.class.getSimpleName();

    private CardLinkListener cardLinkListener;

    private int amount;

    public SaleProxy(Context context) {
        super(context);
    }

    public void signOn() {
        cardLinkPresenter.onLogin();
    }

    public void sale() {
        cardLinkPresenter.onGetParam();
    }

    public void setAmount(int amount) {
        this.amount = amount;
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
                String termCap = paramInfo.getTermCap(); //获取termCap
                AppConfigHelper.setConfig(AppConfigDef.TERMCAP, termCap);
                Log.d(LOG_TAG, "termCap:" + termCap);
            }
            cardLinkPresenter.onSale();
        } else if (cmd == EnumCommand.SetParam) {
            cardLinkPresenter.onKey();
        } else if (cmd == EnumCommand.DownloadAID) {
            cardLinkPresenter.onDownloadCAPK();
        } else if (cmd == EnumCommand.DownloadCAPK) {
            cardLinkPresenter.onKey();
        } else if (cmd == EnumCommand.InitKey) {
            cardLinkPresenter.onLogin(); //下载主秘钥完成后签到
        } else if (cmd == EnumCommand.Settle) {
            printSettle();//打印日结单
            if (cardLinkListener != null) {
                cardLinkListener.onTransSucceed(cmd);
            }
        } else if (cmd == EnumCommand.VoidSale) {
            printVoidSale();//打印撤销
            if (cardLinkListener != null) {
                cardLinkListener.onTransSucceed(cmd);
            }
        } else if (cmd == EnumCommand.Sale) {
            TransInfo transInfo = (TransInfo) params;
            if ("00".equals(transInfo.getRespCode())) {
                CardLinkPrintController printService = new CardLinkPrintController(context);
                printService.printSale(transInfo, AppConfigHelper.getConfig(AppConfigDef.TERMCAP));
                cardLinkListener.onTransSucceed(cmd);
            } else {
                cardLinkListener.onTransFailed(cmd, transInfo.getRespDesc());
            }
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
        } else if (progressCode == EnumProgressCode.InputTransAmount.getCode()) { // 已经设置了获取交易金额,不再提示
            if (amount > 0) {
                Log.d(LOG_TAG, "已经设置了获取交易金额,直接继续交易");
                cardLinkPresenter.getTransInfo().setTransAmount(amount);//设置交易金额
                cardLinkPresenter.continueTrans();
            } else {
                cardLinkListener.onProgress(cmd, progressCode, message, true);
            }
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
}
