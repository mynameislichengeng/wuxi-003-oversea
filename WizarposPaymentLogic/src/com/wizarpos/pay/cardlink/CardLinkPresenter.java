package com.wizarpos.pay.cardlink;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.wizarpos.hspos.api.EnumCommand;
import com.wizarpos.hspos.api.HuashiApi;
import com.wizarpos.hspos.api.ParamInfo;
import com.wizarpos.hspos.api.SettleInfo;
import com.wizarpos.hspos.api.TradeListener;
import com.wizarpos.hspos.api.TransInfo;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;


/**
 * Created by wu on 16/3/10.
 */
public class CardLinkPresenter {

    private CardLinkPresenterListener cardLinkPresenterListener;
    private EndTransListener endTransListener;

    private static final String LOG_TAG = CardLinkPresenter.class.getSimpleName();

    private final int PROGRESS_NOTIFIER = 1;
    private final int SUCCESS_NOTIFIER = 2;
    private final int FAIL_NOTIFIER = 3;

    private HuashiApi posApi;
    private TransInfo transInfo;
    private ParamInfo paramInfo;
    private SettleInfo settleInfo;

    private int errorCode;
    private String errorMessage;

    private Handler handler;

    private EnumCommand curCommand;
    //    private EnumCommand purposeCommand;
    private int curProcessCode;
    private String curProcessMessage;

    public void init() {
        posApi = PaymentApplication.getInstance().getPosApi();
        posApi.setTradeListener(tradeListener);
        handler = new Handler() {
            public void handleMessage(Message msg) {
                /*这里是处理信息的方法*/
                switch (msg.what) {
                    case PROGRESS_NOTIFIER:
                        Log.d(LOG_TAG, "onProgress[" + curCommand.getCmdCode() + "][" + curProcessCode + "][" + curProcessMessage + "]");
                        cardLinkPresenterListener.onProgress(curCommand, curProcessCode, curProcessMessage);
                        break;
                    case FAIL_NOTIFIER:
                        Log.d(LOG_TAG, "错误[" + errorCode + "][" + errorMessage + "]");
                        if (transInfo == null || "00".equals(transInfo.getRespCode()) || TextUtils.isEmpty(transInfo.getRespDesc())) {
                            cardLinkPresenterListener.onTransFailed(curCommand, errorCode + "", errorMessage);
                        } else {
                            cardLinkPresenterListener.onTransFailed(curCommand, transInfo.getRespCode(), transInfo.getRespDesc());
                        }
                        break;
                    case SUCCESS_NOTIFIER:
                        switch (curCommand) {
                            // 管理类
                            // 1.获取参数
                            case GetParam:
                                Log.d(LOG_TAG, "获取参数 完成");
                                cardLinkPresenterListener.onTransSucceed(curCommand, paramInfo);
                                break;
                            // 2.设置参数
                            case SetParam:
                                Log.d(LOG_TAG, "设置参数 完成");
                                cardLinkPresenterListener.onTransSucceed(curCommand, transInfo);
                                break;
                            // 3.下载主密钥
                            case InitKey:
                                Log.d(LOG_TAG, "下载主密钥 完成");
                                cardLinkPresenterListener.onTransSucceed(curCommand, transInfo);
                                break;
                            // 4.签到
                            case Login:
                                Log.d(LOG_TAG, "签到 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
                                if ("00".equals(transInfo.getRespCode())) {
                                    AppConfigHelper.setConfig(AppConfigDef.CARDLINK_LOGIN, Constants.TRUE);
                                    AppConfigHelper.setConfig(AppConfigDef.lastPOSLoginTime, System.currentTimeMillis() + "");
                                    cardLinkPresenterListener.onTransSucceed(curCommand, transInfo);
                                } else {
                                    AppConfigHelper.setConfig(AppConfigDef.CARDLINK_LOGIN, Constants.FALSE);
                                    cardLinkPresenterListener.onTransFailed(curCommand, errorCode + "", errorMessage);
                                }
                                break;
                            // 5.结算
                            case Settle:
                                Log.d(LOG_TAG, "结算 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
                                if (settleInfo.getSettleFlag() != 0) {
                                    cardLinkPresenterListener.onTransSucceed(curCommand, settleInfo);
                                } else {
                                    cardLinkPresenterListener.onTransFailed(curCommand, transInfo.getRespCode(), transInfo.getRespDesc());
                                }
//                                if ("00".equals(transInfo.getRespCode())) {

//                                } else {
//                                    cardLinkPresenterListener.onTransFailed(curCommand, transInfo.getRespCode(), transInfo.getRespDesc());
//                                }
                                break;
                            // 金融交易类
                            // 1.查询余额
                            case Balance:
                                Log.d(LOG_TAG, "查询余额 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
                                if ("00".equals(transInfo.getRespCode())) {
                                    cardLinkPresenterListener.onTransSucceed(curCommand, transInfo);
                                } else {
                                    cardLinkPresenterListener.onTransFailed(curCommand, transInfo.getRespCode(), transInfo.getRespDesc());
                                }
                                break;
                            // 2.消费
                            case Sale:
                                Log.d(LOG_TAG, "消费 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
                                if ("00".equals(transInfo.getRespCode())) {
                                    cardLinkPresenterListener.onTransSucceed(curCommand, transInfo);
                                } else {
                                    cardLinkPresenterListener.onTransFailed(curCommand, transInfo.getRespCode(), transInfo.getRespDesc());
                                }
                                break;
                            // 3.消费撤销
                            case VoidSale:
                                Log.d(LOG_TAG, "消费撤销 完成 , respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
                                if ("00".equals(transInfo.getRespCode())) {
                                    cardLinkPresenterListener.onTransSucceed(curCommand, transInfo);
                                } else {
                                    cardLinkPresenterListener.onTransFailed(curCommand, transInfo.getRespCode(), transInfo.getRespDesc());
                                }
                                break;
                            case QueryAnyTrans:
                                Log.d(LOG_TAG, "查询 完成 , respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
                                if ("00".equals(transInfo.getRespCode())) {
                                    cardLinkPresenterListener.onTransSucceed(curCommand, transInfo);
                                } else {
                                    cardLinkPresenterListener.onTransFailed(curCommand, transInfo.getRespCode(), transInfo.getRespDesc());
                                }
                                break;
                            // 4.退货
                            case Refund:
                                Log.d(LOG_TAG, "退货 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
                                if ("00".equals(transInfo.getRespCode())) {
                                    cardLinkPresenterListener.onTransSucceed(curCommand, transInfo);
                                } else {
                                    cardLinkPresenterListener.onTransFailed(curCommand, transInfo.getRespCode(), transInfo.getRespDesc());
                                }
                                break;
                            case DownloadAID:
                                Log.d(LOG_TAG, "下载AID参数 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
                                if ("00".equals(transInfo.getRespCode())) {
                                    cardLinkPresenterListener.onTransSucceed(curCommand, transInfo);
                                } else {
                                    cardLinkPresenterListener.onTransFailed(curCommand, transInfo.getRespCode(), transInfo.getRespDesc());
                                }
                                break;
                            case DownloadCAPK:
                                Log.d(LOG_TAG, "下载公钥参数 完成, respCode[" + transInfo.getRespCode() + "]respDesc[" + transInfo.getRespDesc() + "]");
                                if ("00".equals(transInfo.getRespCode())) {
                                    cardLinkPresenterListener.onTransSucceed(curCommand, transInfo);
                                } else {
                                    cardLinkPresenterListener.onTransFailed(curCommand, transInfo.getRespCode(), transInfo.getRespDesc());
                                }
                                break;
                            case EndTrans:
                                if (endTransListener != null) {
                                    endTransListener.onEndTransFinish();
                                }
                                break;
                            default:
                                break;
                        }
                        break;
                }
            }
        };
    }

    // 管理类
    // 1.获取参数
    public void onGetParam() {
        onCommand(EnumCommand.GetParam);
    }

    // 2.设置参数
    public void onSetParam() {
        onCommand(EnumCommand.SetParam);
    }

    // 3.下载主密钥
    public void onKey() {
        onCommand(EnumCommand.InitKey);
    }

    // 4.签到
    public void onLogin() {
        onCommand(EnumCommand.Login);
    }

    // 5.结算
    public void onSettle() {
        onCommand(EnumCommand.Settle);
    }

    // 6.下载AID参数
    public void onDownloadAID() {
        onCommand(EnumCommand.DownloadAID);
    }

    // 7.下载AID参数
    public void onDownloadCAPK() {
        onCommand(EnumCommand.DownloadCAPK);
    }

    // 金融交易类
    // 1.查询余额
    public void onBalance() {
        onCommand(EnumCommand.Balance);
    }

    // 2.消费
    public void onSale() {
        onCommand(EnumCommand.Sale);
    }

    // 3.消费撤销
    public void onVoidSale() {
        onCommand(EnumCommand.VoidSale);
    }

    // 4.退货
    public void onRefund() {
        onCommand(EnumCommand.Refund);
    }

    //4.查询任意一笔交易
    public void queryAnyTrans(TransInfo transInfo){
        this.transInfo = transInfo;
        onCommand(EnumCommand.QueryAnyTrans);
    }

    //4.查询任意一笔交易
    public void queryLastTrans(){
        onCommand(EnumCommand.QueryLastTrans);
    }

    public void onCommand(EnumCommand cmd) {
//        purposeCommand = cmd;
//        if (cmd == EnumCommand.Sale && saleTransInfo != null) {//逻辑调整. sale允许直接传入交易金额
//            transInfo = saleTransInfo;
//        } else {
        if (cmd != EnumCommand.QueryAnyTrans) {
            transInfo = new TransInfo();
        }
//        }
        switch (cmd) {
            // 管理类
            // 1.获取参数
            case GetParam:
                Log.d(LOG_TAG, "获取参数");
                posApi.getParam();
                break;
            // 2.设置参数
            case SetParam:
                Log.d(LOG_TAG, "设置参数");
                ParamInfo param = new ParamInfo();
                String MID = AppConfigHelper.getConfig(AppConfigDef.CARDLINK_MECHANTID);
                param.setMid(MID);
                Log.d(LOG_TAG, "MID: " + param.getMid());
                param.setTid(AppConfigHelper.getConfig(AppConfigDef.CARDLINK_TERMINALID));
                Log.d(LOG_TAG, "TID: " + param.getTid());
                param.setServerIP(AppConfigHelper.getConfig(AppConfigDef.CARDLINK_SERVERIP));
                Log.d(LOG_TAG, "IP: " + param.getServerIP());
                String protStr = AppConfigHelper.getConfig(AppConfigDef.CARDLINK_SERVERPORT);
                if (!TextUtils.isEmpty(protStr)) {
                    try {
                        int port = Integer.parseInt(protStr);
                        param.setServerPort(port);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.d(LOG_TAG, "PORT: " + param.getServerPort());
                param.setTpdu(AppConfigHelper.getConfig(AppConfigDef.CARDLINK_TPDU));
                Log.d(LOG_TAG, "TPDU: " + param.getTpdu());
                posApi.setParam(param);
                break;
            // 3.下载主密钥
            case InitKey:
                Log.d(LOG_TAG, "下载主密钥");
                // 华势是联机下载主密钥，主机生成的授权码
                String authCode = AppConfigHelper.getConfig(AppConfigDef.CARDLINK_AUTHCODE);
                transInfo.setAuthCode(authCode);
                Log.d(LOG_TAG, "AuthCode: " + authCode);
                posApi.initKey(transInfo);
                break;
            // 4.签到
            case Login:
                Log.d(LOG_TAG, "签到");
                posApi.login();
                break;
            // 5.结算
            case Settle:
                Log.d(LOG_TAG, "结算");
                posApi.settle();
                break;
            // 金融交易类
            // 1.查询余额
            case Balance:
                Log.d(LOG_TAG, "查询余额");
                posApi.balance(transInfo);
                break;
            // 2.消费
            case Sale:
                Log.d(LOG_TAG, "消费");
                posApi.sale(transInfo);
                break;
            // 3.消费撤销
            case VoidSale:
                Log.d(LOG_TAG, "撤销");
                posApi.voidSale(transInfo);
                break;
            // 4.退货
            case Refund:
                Log.d(LOG_TAG, "退货");
                posApi.refund(transInfo);
                break;
            case DownloadAID:
                Log.d(LOG_TAG, "下载AID参数");
                posApi.downloadAID();
                break;
            case DownloadCAPK:
                Log.d(LOG_TAG, "下载公钥参数");
                posApi.downloadCAPK();
                break;
            case EndTrans:
                if (endTransListener != null) {
                    endTransListener.onEndTransFinish();
                }
                break;
            case QueryAnyTrans://查询任意一笔交易
                Log.d(LOG_TAG,"查询任意一笔交易");
                posApi.queryAnyTrans(transInfo);
                break;
            case QueryLastTrans://查询最后一笔交易
                Log.d(LOG_TAG,"查询最后一笔交易");
                posApi.queryLastTrans(transInfo);
                break;
            default:
                break;
        }
    }

    private TradeListener tradeListener = new TradeListener() {

        @Override
        public void onProgress(EnumCommand cmd, int progressCode, String message, Object param) {
            if (cmd == EnumCommand.Settle) {
                settleInfo = (SettleInfo) param;
            } else {
                transInfo = (TransInfo) param;
            }

            Message msg = new Message();
            msg.what = PROGRESS_NOTIFIER;
            curCommand = cmd;
            curProcessCode = progressCode;
            curProcessMessage = message;
            handler.sendMessage(msg);
        }

        @Override
        public void onTransSucceed(EnumCommand cmd, Object params) {
            curCommand = cmd;
            if (cmd == EnumCommand.GetParam) {
                paramInfo = (ParamInfo) params;
            } else if (cmd == EnumCommand.SetParam) {

            } else if (cmd == EnumCommand.Settle) {
                settleInfo = (SettleInfo) params;
            } else {
                transInfo = (TransInfo) params;
            }
            Message msg = new Message();
            msg.what = SUCCESS_NOTIFIER;
            handler.sendMessage(msg);
        }

        @Override
        public void onTransFailed(EnumCommand cmd, final int error, final String message) {
            curCommand = cmd;
            errorCode = error;
            errorMessage = message;
            Message msg = new Message();
            msg.what = FAIL_NOTIFIER;
            handler.sendMessage(msg);
        }

    };

    public void continueTrans() {
        Log.d(LOG_TAG, "continueTrans");
//        if (curProcessCode == EnumProgressCode.InputTransAmount.getCode()) {
//            transInfo.setTransAmount(1);
//        } else if (curProcessCode == EnumProgressCode.InputAuthCode.getCode()) {
//            transInfo.setAuthCode("123456");
//        } else if (curProcessCode == EnumProgressCode.InputOldRRN.getCode()) {
//            transInfo.setOldRRN("123456789012");
//        } else if (curProcessCode == EnumProgressCode.InputOldTicket.getCode()) {
//            transInfo.setOldTrace(123);
//        } else if (curProcessCode == EnumProgressCode.InputOldTransDate.getCode()) {
//            transInfo.setOldTransDate("0306"); // MMDD
//        }

        switch (curCommand) {
            case Balance:
                posApi.balance(transInfo);
                break;
            case Sale:
                posApi.sale(transInfo);
                break;
            case VoidSale:
                posApi.voidSale(transInfo);
                break;
            case Refund:
                posApi.refund(transInfo);
                break;
            case Login:
                posApi.login();
                break;
            case Settle:
                posApi.settle();
                break;
            case InitKey:
                posApi.initKey(transInfo);
                break;
            case DownloadAID:
                posApi.downloadAID();
                break;
            case DownloadCAPK:
                posApi.downloadCAPK();
                break;
            case EndTrans:
                if (endTransListener != null) {
                    endTransListener.onEndTransFinish();
                }
                break;
        }
    }


    public HuashiApi getPosApi() {
        return posApi;
    }

    public TransInfo getTransInfo() {
        return transInfo;
    }

    public ParamInfo getParamInfo() {
        return paramInfo;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public EnumCommand getCurCommand() {
        return curCommand;
    }

    public int getCurProcessCode() {
        return curProcessCode;
    }

    public String getCurProcessMessage() {
        return curProcessMessage;
    }

    public SettleInfo getSettleInfo() {
        return settleInfo;
    }

    public void endTrans() {
        Log.d(LOG_TAG, "结束交易");
        posApi.endTrans();
    }

    public void setCardLinkPresenterListener(CardLinkPresenterListener cardLinkPresenterListener) {
        this.cardLinkPresenterListener = cardLinkPresenterListener;
    }

    public interface CardLinkPresenterListener {
        void onTransFailed(EnumCommand cmd, final String error, final String message);

        void onProgress(EnumCommand cmd, int progressCode, String message);

        void onTransSucceed(EnumCommand cmd, Object params);
    }

    public void setEndTransListener(EndTransListener endTransListener) {
        this.endTransListener = endTransListener;
    }

    public interface EndTransListener {
        void onEndTransFinish();
    }

    //    public EnumCommand getPurposeCommand() {
//        return purposeCommand;
//    }
}
