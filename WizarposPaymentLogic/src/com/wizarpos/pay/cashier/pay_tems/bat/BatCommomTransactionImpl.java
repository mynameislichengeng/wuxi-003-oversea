package com.wizarpos.pay.cashier.pay_tems.bat;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.pay_tems.bat.entities.BatMicroRsp;
import com.wizarpos.pay.cashier.pay_tems.bat.entities.BatNewReq;
import com.wizarpos.pay.cashier.pay_tems.bat.inf.BatCommonTransaction;
import com.wizarpos.pay.cashier.pay_tems.wepay.NetWorkUtils;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.wizarpospaymentlogic.R;

import java.util.HashMap;
import java.util.Map;

import static com.wizarpos.base.net.NetRequest.NETWORK_ERR;


/**
 * Created by blue_sky on 2016/6/27.
 */
public class BatCommomTransactionImpl extends BatTransation implements BatCommonTransaction {
    private String ERROR_CODE = "126";

    public BatCommomTransactionImpl(Context context) {
        super(context);
    }

    /**
     * @param flag       第三方支付主扫标志 被扫时可以传空
     * @param payChannel 支付标识 W 微信 B 百度 A 支付宝 T QQ Y 翼支付 U 统一被扫
     * @param authCode
     * @param listener
     */
    public void commonPay(final String flag, String payChannel, String authCode, final BasePresenter.ResultListener listener) {
        Map<String, Object> params = new HashMap<>();
        params.put("mixFlag", 0);
        params.put("payChannel", payChannel);
        params.put("authCode", authCode);
        params.put("captcha", AppConfigHelper.getConfig(AppConfigDef.auth_code));
        params.put("ids", transactionInfo.getIds());//支付用券//list
        params.put("amount", transactionInfo.getRealAmount());
//        if (TextUtils.isEmpty(transactionInfo.getSaleInputAmount())) {
//            if (isMixTransaction()) {
//                params.put("inputAmount", transactionInfo.getMixInitAmount());
//            } else {
//                params.put("inputAmount", transactionInfo.getInitAmount());
//            }
//        } else {
//            params.put("inputAmount", transactionInfo.getSaleInputAmount());
//        }
        params.put("inputAmount", transactionInfo.getRealAmount());
        params.put("cardNo", transactionInfo.getCardNo());//
        params.put("rechargeOn", transactionInfo.getRechargeOn());
        params.put("orderId", transactionInfo.getCommoncashierOrderId());
        params.put("isPayComingForm", transactionInfo.getIsPayComingForm());
        Map<String, Object> marketList = new HashMap<>();
        params.put("payMarketActivity", marketList);
        params.put("wmHxInfo", transactionInfo.getBatTicket());
        params.put("flag", flag);
        params.put("tipAmount", transactionInfo.getTips());
        BatNewReq req = new BatNewReq();
        req.setGoods_info(TextUtils.isEmpty(transactionInfo.getBody()) ? "第三方支付" : transactionInfo.getBody());
        req.setSpbill_create_ip(NetWorkUtils.getLocalIpAddress(PaymentApplication.getInstance()));
        req.setTerminal_no(AppConfigHelper.getConfig(AppConfigDef.sn));
        req.setStore_id("");
        req.setPayee_term_id(AppConfigHelper.getConfig(AppConfigDef.alipay_payeeTermId));
        Gson gson = new Gson();
        String commonReqJson = gson.toJson(req);
        params.put("paramJsonObject", commonReqJson);
        NetRequest.getInstance().addRequest(Constants.SC_953_BAT_COMMON_PAY, params, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                JSONObject resultObj = (JSONObject) response.result;
                String errorCode = resultObj.getString("code");
                if (!TextUtils.isEmpty(errorCode) && ERROR_CODE.equals(errorCode)) {
                    listener.onFaild(new Response(2, resultObj.getString("message"), resultObj.getString("captcha")));
                } else {
                    JSONObject orderObj = (JSONObject) resultObj.get("orderDef");
                    String flag = orderObj.getString("mnFlag");
                    if (flag.contains("micro")) {
                        parseMicro(response, listener);
                    } else if (flag.contains("native")) {
                        parseNative(response, listener);
                    }
                }
            }

            @Override
            public void onFaild(final Response response) {
                if (isMixTransaction() && transactionInfo.isFirstTransaction()) {// 第一步交易失败撤销主单
                    revokeMixTranLog(transactionInfo.getMixTranLogId(), new BasePresenter.ResultListener() {

                        @Override
                        public void onSuccess(Response response1) {
                            listener.onFaild(response);
                        }

                        @Override
                        public void onFaild(Response response1) {
                            listener.onFaild(response);
                        }
                    });
                } else {
                    if (response.code == NETWORK_ERR) {//当交易超时后,依然轮询 wu@[20160620]
                        looperQuery(listener);
                    }
                    listener.onFaild(response);
                }
            }
        });

    }

    protected void parseMicro(Response response, BasePresenter.ResultListener resultListener) {
        if (response.result != null && !TextUtils.isEmpty(response.getResult().toString())) {
            JSONObject object = JSONObject.parseObject(response.result.toString());
            JSONObject orderObj = (JSONObject) object.get("orderDef");
            Integer state = null;
            if (orderObj != null&&orderObj.containsKey("state")) {
                state = orderObj.getIntValue("state");
            }
            //
            if (object.containsKey("err_code") && object.getIntValue("err_code") == BatMicroRsp.ERROR_CODE) {//处理需要密码
                BatMicroRsp rsp = new BatMicroRsp();
                rsp.setMsg(context.getResources().getString(R.string.enter_password));
                resultListener.onFaild(new Response(1, rsp.getMsg()));
//                JSONObject resultObj = (JSONObject) response.result;
//                orderObj = (JSONObject) resultObj.get("orderDef");
                String orderNum = orderObj.getString("orderNo");
                String id = orderObj.getString("id");
                transactionInfo.setCnyAmount(orderObj.getString("cnyAmount"));
                transactionInfo.setTranLogId(id);
                transactionInfo.setTranId(orderNum);
                looperQuery(resultListener);
            } else if (state != null && state == 1) {
                String orderNum = orderObj.getString("orderNo");
                String id = orderObj.getString("id");
                transactionInfo.setCnyAmount(orderObj.getString("cnyAmount"));
                transactionInfo.setTranLogId(id);
                transactionInfo.setTranId(orderNum);
                looperQuery(resultListener);
            } else if (state != null && state == 2) {
                BatMicroRsp rsp = JSONObject.parseObject(response.result.toString(), BatMicroRsp.class);
                JSONObject json = JSONObject.parseObject(response.getResult()
                        .toString());
                JSONObject tranIdObj = (JSONObject) json.get("orderDef");
                String id = tranIdObj.getString("id");
                String exchangeRate = tranIdObj.getString("exchangeRate");
                if (!TextUtils.isEmpty(exchangeRate)) {
                    AppConfigHelper.setConfig(AppConfigDef.exchangeRate, exchangeRate);
                }
                JSONArray payMarket = json.getJSONArray("payMarket");
                transactionInfo.setThirdTradeNo(rsp.getThirdTradeNo());
                transactionInfo.setTranLogId(id);
                transactionInfo.setRealAmount(tranIdObj.getString("amount"));
                transactionInfo.setPayType(tranIdObj.getString("payType"));//移动支付对应的支付类型 wu
                transactionInfo.setPayTime(tranIdObj.getString("payTime"));
                transactionInfo.setCnyAmount(tranIdObj.getString("cnyAmount"));
                String cnyAmount = tranIdObj.getString("cnyAmount");
                if (!TextUtils.isEmpty(cnyAmount)) {
                    AppConfigHelper.setConfig(AppConfigDef.CNY_AMOUNT, cnyAmount);
                }
                if (!TextUtils.isEmpty(tranIdObj.getString("thirdExtId"))) {
                    transactionInfo.setThirdExtId(tranIdObj.getString("thirdExtId"));
                }
                if (!TextUtils.isEmpty(tranIdObj.getString("thirdExtName"))) {
                    transactionInfo.setThirdExtName(tranIdObj.getString("thirdExtName"));
                }
                AppConfigHelper.setConfig(AppConfigDef.PRINT_CONTEXT, getPrintContext());
                AppConfigHelper.setConfig(AppConfigDef.PRINT_CUSTOMER_CONTEXT, getCustomerPrintContext());
                resultListener.onSuccess(new Response(0, "支付成功", bundleResult()));
//                printTransInfoWithListener(result, resultListener);
//                resultListener.onSuccess(result);
            }else {
                Response ps = new Response();
                ps.setMsg(response.getMsg());
                resultListener.onFaild(ps);
            }
        }
    }

    protected void parseNative(Response response, BasePresenter.ResultListener resultListener) {
        JSONObject json = JSONObject.parseObject(response.getResult()
                .toString());
        String realPath = json.getString("realPath");
        JSONObject tranIdObj = (JSONObject) json.get("orderDef");
        String tranId = tranIdObj.getString("orderNo");
        String id = tranIdObj.getString("id");
        transactionInfo.setTranLogId(id);
        transactionInfo.setTranId(tranId);
        if (TextUtils.isEmpty(realPath)) {
            resultListener.onFaild(new Response(1, "数据解析异常"));
            return;
        }
        resultListener.onSuccess(new Response(0, "获取二维码成功", realPath));
    }

    @Override
    public void getBarCode(BasePresenter.ResultListener listener) {

    }

    @Override
    public void listenResult(BasePresenter.ResultListener listener) {
        looperQuery(listener);
    }


//    public void printOrderInfo(OrderDef orderDef) {
//        PrintServiceController controller = new PrintServiceController(context);
//        Q1PrintBuilder builder = new Q1PrintBuilder();
//        String printString = "";
//        String transType = orderDef.getPayType();
//        String payTypeDes = null;
//        if (Constants.WEPAYFLAG.equals(transType)) {
//            payTypeDes = "微信支付";
//        } else if (Constants.ALIPAYFLAG.equals(transType)) {
//            payTypeDes = "支付宝支付";
//        } else if (Constants.BAIDUPAYFLAG.equals(transType)) {
//            payTypeDes = "百度支付";
//        } else if (Constants.TENPAYFLAG.equals(transType)) {
//            payTypeDes = "QQ钱包支付";
//        }
//        if (!TextUtils.isEmpty(payTypeDes)) {
//            printString += builder.center(builder.bold(AppConfigHelper.getConfig(AppConfigDef.merchantName)));
//        }
//        printString += builder.branch();
////        printString += "慧商户号：" + AppConfigHelper.getConfig(AppConfigDef.mid) + builder.br();
////        printString += "商户名称：" + AppConfigHelper.getConfig(AppConfigDef.merchantName) + builder.br();
////        printString += "终端号：" + AppConfigHelper.getConfig(AppConfigDef.terminalId) + builder.br();
//        // Logger.debug("订单号：" + transactionInfo.getTranId());
//        printString += "流水号："
//                + (!TextUtils.isEmpty(orderDef.getId()) ? Tools.deleteMidTranLog(orderDef.getId(), AppConfigHelper.getConfig(AppConfigDef.mid)) : "")
//                + builder.br();
//        printString += "时间：" + DateUtil.format(new Date(), DateUtil.P4) + builder.br();
//        printString += "收银员：" + AppConfigHelper.getConfig(AppConfigDef.operatorTrueName) + builder.br();
//        printString += builder.branch();
////        if (!TextUtils.isEmpty(orderDef.getThirdTradeNo())) {
////            printString += "凭证号：" + builder.br() + orderDef.getThirdTradeNo() + builder.br();
////            if (!TextUtils.isEmpty(orderDef.getPayType())) {
////                if (!TextUtils.isEmpty(payTypeDes)) {
////                    printString += "支付方式：" + payTypeDes + builder.br();
////                }
////            }
////        }
//        printString += "应收金额：" + Calculater.divide100(String.valueOf(orderDef.getInputAmount())) + "元" + builder.br();
//        printString += "支付方式：" + payTypeDes + builder.br();
//        printString += "实收金额：" + Calculater.divide100(String.valueOf(orderDef.getAmount())) + "元" + builder.br();
//        printString += builder.branch();
//        printString += builder.endPrint();
//        controller.print(printString);
////        controller.cutPaper();
//        LastPrintHelper.beginTransaction().addString(printString).commit();
//    }


//    public void checkOrderState(String tranId, final boolean print, final BasePresenter.ResultListener listener) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("orderNo", tranId);
//        params.put("mid", AppConfigHelper.getConfig(AppConfigDef.mid));
//        NetRequest.getInstance().addRequest(Constants.SC_820_ORDER_DEF_DETAIL, params,
//                new ResponseListener() {
//
//                    @Override
//                    public void onSuccess(Response response) {
//                        if (response.result == null) {
//                            response.code = 1;
//                            response.msg = "数据解析失败";
//                            listener.onFaild(response);
//                            return;
//                        }
//                        OrderDef order = JSONObject.parseObject(response.result.toString(), OrderDef.class);
//                        if (StringUtil.isSameString(order.getTimeOutFlag(), OrderDef.TIME_OUT)) {
//                            order.setState(OrderDef.STATE_TIME_OUT);
//                        }
//                        if (print) {
//                            printOrderInfo(order);
//                        }
//                        listener.onSuccess(new Response(0, OrderDef.getOrderStateDes(order.getState()), order));
//                    }
//
//                    @Override
//                    public void onFaild(Response response) {
//                        listener.onFaild(new Response(1, response.msg));
//                    }
//                });
//    }

}
