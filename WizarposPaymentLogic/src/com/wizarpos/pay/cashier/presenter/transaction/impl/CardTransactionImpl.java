package com.wizarpos.pay.cashier.presenter.transaction.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.payment.impl.CardPayment;
import com.wizarpos.pay.cashier.presenter.ticket.TicketManagerFactory;
import com.wizarpos.pay.cashier.presenter.ticket.inf.CommonTicketManager;
import com.wizarpos.pay.cashier.presenter.transaction.inf.CardTransaction;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.LastPrintHelper;
import com.wizarpos.pay.common.device.DisplayHelper;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.common.ticketdisplay.DisplayTicektBean;
import com.wizarpos.pay.common.ticketdisplay.DisplayTicketActivity;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.FileUtil;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.common.utils.TokenGenerater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.GetCommonTicketInfoResp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行卡交易
 *
 * @author wu
 */
public class CardTransactionImpl extends TransactionImpl implements CardTransaction {

    private CommonTicketManager commonTicketManager;// 银行卡交易只能使用普通券
    private CardPayment payment;

    private String scorePrint; // 积分
    private String transTime;// 付款时间

    private boolean isNeedPrint = true;
    private String token;
    private boolean isHandled_submit;
    private TicketInfo wemengTicketInfo;

    /**
     * 700接口调用失败@yaosong [20151030]
     **/
    public final static int CALL_700_FAILED = -700;

    private ResultListener tranUploadListener = null;

    public static final String SWIPE_BANK_JSONARRAY = "SWIPE_BANK_JSONARRAY";
    public static final String SWIPE_BANK_NO = "SWIPE_BANK_NO";
    public static final String HAS_ICCARD = "HAS_ICCARD";

    private ArrayList<String> swipeBankJSONArray;//银行卡磁道信息
    private String swipeBankCardNo;//银行卡号
    private boolean HasICCard;//是否插入IC

    public CardTransactionImpl(Context context) {
        super(context);
        token = TokenGenerater.newToken(); //生成 token wu@[20151125]

        payment = new CardPayment(context);
    }

    @Override
    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
        changeAmount = Calculater.subtract(realAmount, shouldAmount);// 计算找零金额
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        defTransactionType(TransactionTemsController.TRANSACTION_TYPE_BANK_CARD);

        commonTicketManager = TicketManagerFactory.createCommonTicketManager(context);

        swipeBankJSONArray = (ArrayList<String>) intent.getSerializableExtra(SWIPE_BANK_JSONARRAY);
        swipeBankCardNo = (String) intent.getSerializableExtra(SWIPE_BANK_NO);
        HasICCard = intent.getBooleanExtra(HAS_ICCARD, false);
        payment.setCardInfo((ArrayList<String>) intent.getSerializableExtra(SWIPE_BANK_JSONARRAY));
        payment.setHasICCard(HasICCard);
    }

    /**
     * 交易
     */
    public void trans(final ResultListener listener) {
        if (TextUtils.isEmpty(realAmount) || Calculater.compare(realAmount, "1") < 0) {
            listener.onFaild(new Response(1, "交易金额不能为0"));
            return;
        }
        if (isMixTransaction() == false && Integer.parseInt(shouldAmount) > Integer.parseInt(realAmount)) {
            listener.onFaild(new Response(1, "实收金额需要大于或等于应收金额！"));
            return;
        }
        transTime = DateUtil.format(new Date(), DateUtil.P2);
        // payment.setAmount(realAmount);// 为payment设置交易金额
        tranUploadListener = new ResultListener() {

            @Override
            public void onSuccess(Response response) {
                if (response.getResult() == null) {
//					Toast.makeText(context, "银行卡号为空", Toast.LENGTH_SHORT).show();
                    listener.onSuccess(new Response(0, "success", bundleResult()));
                } else {
                    cardNo = String.valueOf(response.getResult());
                    paySuccessResult(response, new ResultListener() {

                        @Override
                        public void onSuccess(Response response) {
                            listener.onSuccess(new Response(0, "success", bundleResult()));
                        }

                        @Override
                        public void onFaild(Response response) {
                            listener.onFaild(response);
                        }
                    });
                }
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);
                onDestory();//解除绑定
            }
        };
        payment.pay(realAmount, new ResultListener() {// 银行卡支付

            @Override
            public void onSuccess(Response response) {
                uploadTrans(tranUploadListener);
            }

            @Override
            public void onFaild(Response response) {
                // 银行卡支付失败
                listener.onFaild(response);
                onDestory();//解除绑定
            }
        });

    }

    @Override
    public boolean revokeTrans(ResultListener listener) {
        return false;
    }

    /**
     * 支付成功或失败
     */
    private void paySuccessResult(Response response, final ResultListener listener) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("merchantId", AppConfigHelper.getConfig(AppConfigDef.merchantId));
        params.put("tranLogId", tranLogId);

        params.put("bankCardNo", cardNo);
        params.put("state", true);
        params.put("payAmount",
                new BigDecimal(initAmount).multiply(new BigDecimal(100))
                        .intValue() + "");
        params.put("extraAmount", reduceAmount);
//		params.put("inputAmount", inputAmountNeed);
        params.put("issueTicketMode", "3");
        params.put("rechargeOn", rechargeOn);
        params.put("amount",
                new BigDecimal(reduceAmount)
                        .multiply(new BigDecimal(100)).intValue() + "");
        NetRequest.getInstance().addRequest(Constants.SC_833_BANK_CARD_PAY_RESULT, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                JSONObject jsonObject = (JSONObject) response.getResult();
                // ---------- 微盟券 wu----------------------
                try {
                    if (wemengTicketInfo == null
                            && jsonObject.containsKey("giftTicket")) {
                        if (!((JSONObject) jsonObject.get("giftTicket")).isEmpty()) {
                            wemengTicketInfo = Tools.jsonObjectToJavaBean(
                                    (JSONObject) jsonObject.get("giftTicket"),
                                    TicketInfo.class);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // ---------- end 微盟券 wu ----------------

                if (jsonObject.getString("ticket") == null) {
                    listener.onSuccess(response);
                } else {
                    String ticketId = jsonObject.getString("ticket");
//                    if (!TextUtils.isEmpty(ticketId)) {
//                        requestQRCode(ticketId, listener);
//                    } else {
                        listener.onSuccess(response);
//                    }
                }
            }

            @Override
            public void onFaild(Response arg0) {
                listener.onFaild(arg0);
            }
        });


    }

    /**
     * 重调700接口上送交易信息
     *
     * @param listener
     */
    public void reTryUploadTrans(final ResultListener listener) {
        uploadTrans(tranUploadListener);
    }

    /**
     * 上送交易
     */
    private void uploadTrans(final ResultListener listener) {
        try {
            List<String> ids = new ArrayList<String>();
            String reduceStr = initAmount;
            List<TicketInfo> tickets = commonTicketManager.getAddedTickets();
            if (tickets != null && !tickets.isEmpty()) {
                for (TicketInfo ticket : tickets) {
                    if ("true".equals(ticket.getIsFromScan())) {
                        ids.add(ticket.getId() + "|1");// 如果是扫码的券,值未1,否则为0
                    } else {
                        ids.add(ticket.getId() + "|0");
                    }
                    // ids.add(ticket.getId() + "|1");// 银行卡消费过程中使用的券 都是
                    // 扫描的券,所以恒为1
                    Integer balance = ticket.getTicketDef().getBalance();
                    ticketReduceAomount += balance;
                }
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("payAmount", realAmount);
            params.put("extraAmount", reduceAmount);
//			params.put("inputAmount", initAmount);
            if (!TextUtils.isEmpty(saleInputAmount) && !saleInputAmount.equals("0")) {
                params.put("inputAmount", saleInputAmount);        //当有销售金额传入时,使用 saleInputAmout wu@[20151120]
            } else {
                params.put("inputAmount", initAmount);
            }
            params.put("issueTicketMode", isAutoPublishTicket);
            params.put("cardName", "银行卡");
            params.put("cardType", TextUtils.isEmpty(cardType) ? "1" : cardType);
            params.put("cardNo", cardNo == null ? "" : cardNo);
            params.put("rechargeOn", rechargeOn);
            params.put("token", token); //增加 token 字段 hong20151217
            if (isMixTransaction()) { //增加传入混合支付的总金额 wu
                params.put("masterPayAmount", mixInitAmount);//组合支付总金额
            }
            if (PaymentApplication.getInstance().isWemengMerchant()) {
                params.put("amount", initAmount);//为微盟券增加 wu
            }
            if (commonTicketManager.isContainsWemengTicket()) {
                params.put("ticketNo", commonTicketManager.getAddedWemengTicketNo());
            } else {
                params.put("ids", ids);
            }
            if (isMixTransaction()) {// 混合支付
                LogEx.d("混合支付主流水号", "现金上送:" + mixTranLogId);
                params.put("masterTranLogId", mixTranLogId);
                params.put("mixFlag", 1);
                params.put("isPayComingForm", isPayComingForm);
            }
            NetRequest.getInstance().addRequest(Constants.SC_700_BANK_CARD_PAY, params, new ResponseListener() {

                @Override
                public void onSuccess(Response response) {
                    if (isMixTransaction()) {
                        String jsonString = JSON.toJSONString(response.getResult(), SerializerFeature.DisableCircularReferenceDetect);
                        JSONObject jsonObject = JSON.parseObject(jsonString);
                        JSONObject jTranLog = jsonObject.getJSONObject("slaveTranLog");// 子流水 截取流水号的方式和现金不一样 wu@[20150910]
                        mixTranLogId = jTranLog.getString("masterTranLogId");// 混合支付主流水号
                        LogEx.d("混合支付主流水号", "现金返回:" + mixTranLogId);
                        tranLogId = jTranLog.getString("id");// 混合支付子流水号
                        //防止订单重复提交@hong 20151217
                        LogEx.d("jTranLog", jTranLog.toString());
                        String tokenReturn = jTranLog.getString("token");
                        boolean doubleClickFlag = jsonObject.getBoolean("doubleClickFlag");
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
                        print();
                        listener.onSuccess(response);
//						response.code = 0;
//						response.msg = "success";
//						response.result = bundleResult();
//						listener.onSuccess(response);
                    } else {
                        JSONObject _jsonObject = JSONObject.parseObject(response.getResult().toString());
                        scorePrint = _jsonObject.getString("tranPoints");
                        JSONObject _jTranLog = _jsonObject.getJSONObject("tranLog");
                        String _publishTickets = _jsonObject.getString("publishTickets");
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
                        // ---------------微盟券 wu----------------------
                        try {
                            if (wemengTicketInfo == null && _jsonObject.containsKey("giftTicket")) {
                                if (((JSONObject) _jsonObject.get("giftTicket")).isEmpty() == false) {
                                    wemengTicketInfo = Tools.jsonObjectToJavaBean(
                                            (JSONObject) _jsonObject.get("giftTicket"), TicketInfo.class);
                                    wemengTicketInfo.setIsWeiMengTicket("1");//标示是微盟券
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // ---------------微盟券 wu----------------------

                        print();

                        if (!TextUtils.isEmpty(_publishTickets)) {// 打印券
                            List<TicketInfo> ticketInfos = JSONArray.parseArray(_publishTickets, TicketInfo.class);
                            commonTicketManager.printTickets(ticketInfos);
                        }

                        DisplayHelper.getInstance().startKxBankCard(payment.getUDID(), realAmount);
                        listener.onSuccess(response);
//						listener.onSuccess(new Response(0, "success", bundleResult()));
                    }
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

    @Override
    public void getCommonTicketInfo(String ticketNum, String shouldPayAmount,
                                    final ResultListener listener) {
        commonTicketManager.getTicketInfo(ticketNum, initAmount, shouldPayAmount, new ResultListener() {

            @Override
            public void onSuccess(Response response) {
                GetCommonTicketInfoResp commonTicketInfoResp = (GetCommonTicketInfoResp) response.result;
                if (commonTicketInfoResp.getWemengTicket() != null) {
                    wemengTicketInfo = commonTicketInfoResp.getWemengTicket();
                }
                listener.onSuccess(new Response(0, "获取券信息成功", commonTicketInfoResp.getTicketInfo()));
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);

            }
        });
    }

    /**
     * 获取普通券的信息
     *
     * @param ticketNum 券唯一标识
     * @param listener
     */
    public void getCommonTicketInfo(String ticketNum, final ResultListener listener) {
        getCommonTicketInfo(ticketNum, "", listener);
    }

    /**
     * 添加普通券
     */
    public Response addCommonTicket(TicketInfo ticket, boolean isFromScan) {
        try {
//			if ("1".equals(ticket.getTicketDef().getWxFlag()) || "2".equals(ticket.getTicketDef().getWxFlag())) { return new Response(1, "添加券失败:无法添加微信券"); }// 不允许添加微信券
            if (commonTicketManager.isAddedTicket(ticket.getId())) {
                return new Response(1, "该券已添加,不能重复添加");
            }

			/* 更新应付金额和扣减金额 */
            int blance = ticket.getTicketDef().getBalance();

            if (Calculater.compare(blance + "", shouldAmount) > 0) {
                return new Response(1, "添加失败,券金额大于或等于消费金额");
            }

            if (Calculater.compare(shouldAmount, blance + "") > 0) {
                Response addTicketResponse = commonTicketManager.addTicket(ticket, isFromScan);
                if (addTicketResponse.code == 0) {
                    calculateAddTicket(blance);
                }
                return addTicketResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(1, "添加券失败");
    }

    /**
     * 请求二维码
     */
    private void requestQRCode(final String ticketId, final ResultListener listener) {
//        final Handler mHandler = new Handler();
//        new Thread() {
//            public void run() {
//                try {
//                    HttpClient httpClient = getHttpClient();
//                    HttpGet httpGet = new HttpGet(
//                            "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="
//                                    + ticketId);
//                    HttpResponse response = httpClient.execute(httpGet);
//                    if (response.getStatusLine().getStatusCode() != 200) {
//                        listener.onFaild(new Response(-1, "请求微信二维码发生异常"));
//                        return;
//                    }
//                    Bitmap bm = BitmapFactory.decodeStream(response.getEntity()
//                            .getContent());
//                    printFocusWx(bm);
//                    mHandler.post(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            listener.onSuccess(new Response(0, "success", bundleResult()));
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            ;
//        }.start();
    }

    /**
     * 打印二维码
     */
    private synchronized void printFocusWx(Bitmap bm) {
        if (deviceManager.isSupprotPrint()) {
            PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
            Q1PrintBuilder builder = new Q1PrintBuilder();
            String printString = "";
            printString += builder.center(builder.bold("扫一扫开通微信会员卡"));
            printString += builder.normal("--------------------------------") + builder.br();
            controller.print(printString);
            controller.print(Tools.resizeBitmap(bm, Constants.QRCODE_LENGTH, Constants.QRCODE_LENGTH));
            controller.print(builder.endPrint());
            controller.cutPaper();
        } else {
            List<DisplayTicektBean> displayTicektBeans = new ArrayList<DisplayTicektBean>();

            DisplayTicektBean bean = new DisplayTicektBean();
            bean.setTitle("扫一扫开通微信会员卡");

            String filePath = FileUtil.saveBitmap(bm);
            bean.setBitmapPath(filePath);

            displayTicektBeans.add(bean);
            if (displayTicektBeans.isEmpty() == false) {
                Intent intent = new Intent(context, DisplayTicketActivity.class);
                intent.putExtra(DisplayTicketActivity.DISPLAY_TICKET, (Serializable) displayTicektBeans);
                context.startActivity(intent);
            }
        }
    }

    private void print() {
        if (isNeedPrint == false) {
            return;
        }
        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
        Q1PrintBuilder builder = new Q1PrintBuilder();
        String printString = "";
        if (isMixTransaction()) {
            printString += builder.center(builder.bold("组合支付|银行卡消费"));
        } else {
            printString += builder.center(builder.bold("银行卡消费"));
        }
        printString += builder.branch();
        printString += "商户号：" + AppConfigHelper.getConfig(AppConfigDef.merchantId) + builder.br();

        printString += "商户名称：" + AppConfigHelper.getConfig(AppConfigDef.merchantName) + builder.br();
        printString += "终端号：" + AppConfigHelper.getConfig(AppConfigDef.terminalId) + builder.br();
        printString += "流水号：" + tranLogId + builder.br();
        String bankCardNum = payment.getUDID();
        if (!TextUtils.isEmpty(bankCardNum)) {
            printString += "卡号："
                    + (bankCardNum.length() > 10 ? Tools.replace(bankCardNum, '*', 6, bankCardNum.length() - 10)
                    : bankCardNum) + builder.br();
        }
        if (isMixTransaction()) {
            printString += "总金额:" + Calculater.divide100(mixInitAmount) + builder.br();
        }
        printString += "收银：" + Calculater.divide100(initAmount) + "元" + builder.br();
        // printStringPayFor += "折扣：" + disCountNeed + "折" + pb.br();
        if (!TextUtils.isEmpty(discountAmount) && !"0".equals(discountAmount)) {
            printString += "折扣减价：" + Calculater.divide100(discountAmount) + "元" + builder.br();
        }
        String printReduceAmount = Calculater.subtract(reduceAmount + "", ticketReduceAomount + "");
        printString += "扣减：" + Calculater.divide100(printReduceAmount) + "元" + builder.br();
        printString += "券总计：" + Calculater.divide100(ticketReduceAomount + "") + "元" + builder.br();
        printString += "刷卡：" + Calculater.divide100(realAmount) + "元" + builder.br();
        if (scorePrint != null) {
            printString += "赠送积分：" + scorePrint + builder.br();
        }
        printString += "时间：" + transTime + builder.br();
        printString += builder.normal("--------------------------------") + builder.br();
        Logger2.debug(printString);
        controller.print(printString);

        printString = commonTicketManager.getCommonTicketPrintInfo(); // 打印券
        if (TextUtils.isEmpty(printString) == false) {
            controller.print(printString);
        }
        controller.print(builder.endPrint());
        controller.cutPaper();
        LastPrintHelper.beginTransaction().addString(printString).commit();
    }

    @Override
    public void onDestory() {
        super.onDestory();
        payment.onDestory();
    }

    @Override
    protected Intent bundleResult() {
        Intent intent = super.bundleResult();
        if (PaymentApplication.getInstance().isWemengMerchant()) {//为微盟券增加
//			intent.putExtra(Constants.isUsedWemngTicket, commonTicketManager.isContainsWemengTicket()); 
            intent.putExtra(Constants.wemengTicketInfo, wemengTicketInfo);
        }
        return intent;
    }

    @Override
    public void setCardPaymetProgressListener(CardPayment.CardPaymetProgressListener cardPaymetProgressListener) {
        payment.setCardPaymetProgressListener(cardPaymetProgressListener);
    }

    @Override
    public void continueTrans() {
        payment.continueTrans();
    }

    @Override
    public void endTrans() {
        payment.endTrans();
    }


}
