package com.wizarpos.pay.cardlink;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.hspos.api.TransInfo;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cardlink.model.BankCardTransUploadReq;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.BankTransUploadDao;
import com.wizarpos.pay.model.SendTransInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wu on 16/3/27.
 */
public class CardLinkTransaction {

    private static final String LOG_TAG = CardLinkTransaction.class.getSimpleName();

    private Context context;

    private int amount;

    private boolean isHandled_submit;

    private boolean endTransEnable; //如果没有开始交易就直接调用 endTrans, jar 包会空指针异常. 所有引入该变量控制. wu@[20160513]

    private SaleProxy cardLinkProxy;

    private Map<String, Object> retryParams = null;
    private BasePresenter.ResultListener retryUploadListener = null;

    /**
     * 700接口调用失败@yaosong [20151030]
     **/
    public final static int CALL_700_FAILED = -700;
    private String tranLogId = "";

    public CardLinkTransaction(Context context) {
        this.context = context;
        cardLinkProxy = new SaleProxy(context);
    }

    public void signOn() {
        endTransEnable = true;
        cardLinkProxy.signOn();
    }

    public void sale() {
        endTransEnable = true;
        cardLinkProxy.sale();
    }

    public void setAmount(int amount) {
        this.amount = amount;
        cardLinkProxy.setAmount(amount);
    }

    public void continueTrans() {
        cardLinkProxy.continueTrans();
    }

    public void setCardLinkListener(CardLinkListener cardLinkListener) {
        cardLinkProxy.setCardLinkListener(cardLinkListener);
    }

    public void setEndTransListener(CardLinkPresenter.EndTransListener endTransListener) {
        cardLinkProxy.setEndTransListener(endTransListener);
    }

    public TransInfo getTransInfo() {
        return cardLinkProxy.getTransInfo();
    }

    public void endTrans() {
        if (endTransEnable) {
            cardLinkProxy.endTrans();
        } else {
            Log.d(LOG_TAG, "endTrans:  没有开始交易, 不需要调用 endTrans");
        }
    }

    public void setEndTransEnable(boolean endTransEnable) {
        this.endTransEnable = endTransEnable;
    }

    public void paySuccess(String token, String bankcardNo, TransInfo transInfo, BasePresenter.ResultListener listener) {
        uploadTrans(token, bankcardNo, transInfo, listener);
    }

    /**
     * 上送交易
     */
    private void uploadTrans(final String token, final String bankcardNo, TransInfo transInfo, final BasePresenter.ResultListener listener) {
        try {
            final String transTime = DateUtil.format(new Date(), DateUtil.P2);

            Map<String, Object> params = new HashMap<>();
            params.put("payAmount", amount);
            params.put("inputAmount", amount);
            params.put("cardName", "银行卡");
            params.put("bankCardNo", bankcardNo);
            params.put("bankOrderNo", String.valueOf(transInfo.getTrace()));
            params.put("referCode", transInfo.getRrn());
            SendTransInfo sendTransInfo = new SendTransInfo();
            sendTransInfo.setBatchNumber(transInfo.getBatchNumber());
            sendTransInfo.setExpiryDate(transInfo.getExpiryDate());
            sendTransInfo.setMid(transInfo.getMid());
            sendTransInfo.setRrn(transInfo.getRrn());
            sendTransInfo.setTrace(transInfo.getTrace());
            sendTransInfo.setTid(transInfo.getTid());
            sendTransInfo.setTransAmount(transInfo.getTransAmount());
            sendTransInfo.setCardNo(bankcardNo);

            params.put("transInfo", sendTransInfo);//银行卡消费上传数据
            params.put("token", token); //增加 token 字段 hong20151217
            addTrans2Db(params);
            retryParams = params;
            retryUploadListener = listener;
            NetRequest.getInstance().addRequest(Constants.SC_700_BANK_CARD_PAY, params, new ResponseListener() {
                //
                @Override
                public void onSuccess(Response response) {
                    BankTransUploadDao.getInstance().deleteTransUpload(token);
                    JSONObject _jsonObject = JSONObject.parseObject(response.getResult().toString());
                    JSONObject _jTranLog = _jsonObject.getJSONObject("tranLog");
                    tranLogId = _jTranLog.getString("masterTranLogId");
                    //防止订单重复提交
                    LogEx.d("jTranLog", _jTranLog.toString());
                    String tokenReturn = _jTranLog.getString("token");
                    boolean doubleClickFlag = _jTranLog.getBoolean("doubleClickFlag");
                    LogEx.d("token", "current token: " + token);
                    LogEx.d("token", "return token: " + tokenReturn + " - doubleClickFlag: " + doubleClickFlag);
                    if (!token.equals(tokenReturn)) { //增加 toke 验证逻辑
                        return;
                    }
                    if (doubleClickFlag && isHandled_submit) {
                        return;
                    } else {
                        isHandled_submit = true;
                        LogEx.d("token", "isHandled_submit: " + isHandled_submit);
                    }
                    print(transTime, bankcardNo);
                    listener.onSuccess(response);
                    retryParams = null;
                    retryUploadListener = null;
                }

                @Override
                public void onFaild(Response response) {
                    response.setCode(CALL_700_FAILED);
                    listener.onFaild(response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reTryUploadTrans() {
        try {
            if (null != retryParams && retryParams.containsKey("token")) {
                final String transTime = DateUtil.format(new Date(), DateUtil.P2);
                final String token = (String) retryParams.get("token");
                final String bankcardNo = (String) retryParams.get("bankcardNo");
                NetRequest.getInstance().addRequest(Constants.SC_700_BANK_CARD_PAY, retryParams, new ResponseListener() {
                    //
                    @Override
                    public void onSuccess(Response response) {
                        BankTransUploadDao.getInstance().deleteTransUpload(token);
                        JSONObject _jsonObject = JSONObject.parseObject(response.getResult().toString());
                        JSONObject _jTranLog = _jsonObject.getJSONObject("tranLog");
                        tranLogId = _jTranLog.getString("masterTranLogId");
                        //防止订单重复提交
                        LogEx.d("jTranLog", _jTranLog.toString());
                        String tokenReturn = _jTranLog.getString("token");
                        boolean doubleClickFlag = _jTranLog.getBoolean("doubleClickFlag");
                        LogEx.d("token", "current token: " + token);
                        LogEx.d("token", "return token: " + tokenReturn + " - doubleClickFlag: " + doubleClickFlag);
                        if (!token.equals(tokenReturn)) { //增加 toke 验证逻辑
                            return;
                        }
                        if (doubleClickFlag && isHandled_submit) {
                            return;
                        } else {
                            isHandled_submit = true;
                            LogEx.d("token", "isHandled_submit: " + isHandled_submit);
                        }
                        print(transTime, bankcardNo);
                        retryUploadListener.onSuccess(response);
                        retryParams = null;
                        retryUploadListener = null;
                    }

                    @Override
                    public void onFaild(Response response) {
                        response.setCode(CALL_700_FAILED);
                        retryUploadListener.onFaild(response);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加银行卡交易到数据库
     *
     * @param params
     */
    private void addTrans2Db(Map<String, Object> params) {
        BankCardTransUploadReq uploadReq = new BankCardTransUploadReq();
        uploadReq.setPayAmount((Integer) params.get("payAmount") + "");
        uploadReq.setInputAmount((Integer) params.get("inputAmount") + "");
        uploadReq.setCardName((String) params.get("cardName"));
        uploadReq.setToken((String) params.get("token"));
        if (params.containsKey("transInfo")) {
            uploadReq.setTransInfo(JSONObject.toJSONString((SendTransInfo) params.get("transInfo")));
        }
        if (params.containsKey("bankOrderNo")) {
            uploadReq.setBankOrderNo((String) params.get("bankOrderNo"));
        }
        if (params.containsKey("referCode")) {
            uploadReq.setReferCode((String) params.get("referCode"));
        }
        if (params.containsKey("extraAmount")) {
            uploadReq.setExtraAmount((String) params.get("extraAmount"));
        }
        if (params.containsKey("issueTicketMode")) {
            uploadReq.setIssueTicketMode((String) params.get("issueTicketMode"));
        }
        if (params.containsKey("cardType")) {
            uploadReq.setCardType((String) params.get("cardType"));
        }
        if (params.containsKey("cardNo")) {
            uploadReq.setCardNo((String) params.get("cardNo"));
        }
        if (params.containsKey("rechargeOn")) {
            uploadReq.setRechargeOn((String) params.get("rechargeOn"));
        }
        if (params.containsKey("masterPayAmount")) {
            uploadReq.setMasterPayAmount((String) params.get("masterPayAmount"));
        }
        if (params.containsKey("amount")) {
            uploadReq.setAmount((String) params.get("amount"));
        }
        if (params.containsKey("bankCardNo")) {
            uploadReq.setBankCardNo((String) params.get("bankCardNo"));
        }
        if (params.containsKey("ticketNo")) {
            uploadReq.setTicketNo((String) params.get("ticketNo"));
        }
        if (params.containsKey("ids")) {
            uploadReq.setIds((List<String>) params.get("ids"));
        }
        if (params.containsKey("masterTranLogId")) {
            uploadReq.setMasterTranLogId((String) params.get("masterTranLogId"));
        }
        if (params.containsKey("mixFlag")) {
            uploadReq.setMixFlag((int) params.get("mixFlag"));
        }
        if (params.containsKey("isPayComingForm")) {
            uploadReq.setIsPayComingForm((String) params.get("isPayComingForm"));
        }
        if (params.containsKey("bankTerminalNo")) {
            uploadReq.setBankTerminalNo((String) params.get("bankTerminalNo"));
        }
        if (params.containsKey("bankMid")) {
            uploadReq.setBankMid((String) params.get("bankMid"));
        }
        BankTransUploadDao.getInstance().addTransUpload(uploadReq);
    }

    private void print(String transTime, String bankcardNo) {
        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
        Q1PrintBuilder builder = new Q1PrintBuilder();
        String printString = "";
//        printStringPayFor += builder.center(builder.bold("银行卡消费"));
//        printStringPayFor += builder.branch();
//        printStringPayFor += "商户号：" + AppConfigHelper.getConfig(AppConfigDef.merchantId) + builder.br();
        printString += "慧商户号：" + AppConfigHelper.getConfig(AppConfigDef.mid) + builder.br();
//        printStringPayFor += "商户名称：" + AppConfigHelper.getConfig(AppConfigDef.merchantName) + builder.br();
//        printStringPayFor += "终端号：" + AppConfigHelper.getConfig(AppConfigDef.terminalId) + builder.br();
        printString += "终端设备号：" + AppConfigHelper.getConfig(AppConfigDef.sn) + builder.br();
        printString += "流水号：" + tranLogId + builder.br();
//        String bankCardNum = bankcardNo;
//        if (!TextUtils.isEmpty(bankCardNum)) {
//            printStringPayFor += "卡号：" + bankcardNo + builder.br();
//        }
//        if (isMixTransaction()) {
//            printStringPayFor += "总金额:" + Calculater.divide100(mixInitAmount) + builder.br();
//        }
//        printStringPayFor += "收银：" + Calculater.divide100(amount + "") + "元" + builder.br();
        // printStringPayFor += "折扣：" + disCountNeed + "折" + pb.br();
//        if (!TextUtils.isEmpty(discountAmount) && !"0".equals(discountAmount)) {
//            printStringPayFor += "折扣减价：" + Calculater.divide100(discountAmount) + "元" + builder.br();
//        }
//        String printReduceAmount = Calculater.subtract(reduceAmount + "", ticketReduceAomount + "");
//        printStringPayFor += "扣减：" + Calculater.divide100(printReduceAmount) + "元" + builder.br();
//        printStringPayFor += "券总计：" + Calculater.divide100(ticketReduceAomount + "") + "元" + builder.br();
//        printStringPayFor += "刷卡：" + Calculater.divide100(amount + "") + "元" + builder.br();
//        if (scorePrint != null) {
//            printStringPayFor += "赠送积分：" + scorePrint + builder.br();
//        }
//        printStringPayFor += "时间：" + transTime + builder.br();
        printString += builder.branch();
        printString += builder.endPrint();
//        Logger2.debug(printStringPayFor);
//        int printNumber = 1;
//        if (!TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.print_number))) {
//            printNumber = Integer.parseInt(AppConfigHelper.getConfig(AppConfigDef.print_number));
//        }
//        for (int i = 0; i < printNumber; i++) {
        controller.print(printString, true);
//        }
//        printStringPayFor = commonTicketManager.getCommonTicketPrintInfo(); // 打印券
//        if (TextUtils.isEmpty(printStringPayFor) == false) {
//            controller.print(printStringPayFor);
//        }
    }

}
