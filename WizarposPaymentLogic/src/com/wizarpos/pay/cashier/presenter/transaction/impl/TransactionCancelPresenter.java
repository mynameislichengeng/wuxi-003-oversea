package com.wizarpos.pay.cashier.presenter.transaction.impl;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.device.printer.html.ToHTMLUtil;
import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.device.printer.html.model.HtmlLine;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.model.PayTranRsp;
import com.wizarpos.pay.cashier.model.SaleOrder;
import com.wizarpos.pay.cashier.model.TranLog;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.recode.constants.HttpConstants;
import com.wizarpos.recode.constants.TransRecordLogicConstants;
import com.wizarpos.recode.print.PrintManager;
import com.wizarpos.recode.print.content.CashierIdContent;
import com.wizarpos.recode.print.content.DeviceContent;
import com.wizarpos.recode.print.content.InvoiceContent;
import com.wizarpos.recode.print.content.SettlementContent;
import com.wizarpos.recode.print.content.TotalContent;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionCancelPresenter {
    private Context context;
    private VoidTransactionProxy voidTransaction;
    //    private StatisticsPresenter statisticsPresenter;
    private RefundDetailResp resp;

    // private JSONArray saleOrder;
    // private List<String[]> recordList_one = new ArrayList<String[]>();
    // private List<String[]> recordList_two = new ArrayList<String[]>();
    //
    // private String tranType;
    // private String offlineTranLogId;
    // private String tranCode;
    // private String reTranLogId;
    // private int tran_amount;
    // private String tranTime;
    // private String ticketName = "";
    // private String ticketBalance = "";
    // private String userName;
    // private String cardNo;
    // private String balance;
    // private String productName = "";
    // private String productCount = "";
    // private String productAmount = "";
    // private String inputAmount;
    // private String extraAmount;
    //
    // private final int CANCEL_ALIPAY = 10021;

    private String tranLogId;

    public TransactionCancelPresenter(Context context) {
        this.context = context;
        voidTransaction = new VoidTransactionProxy(context);
    }

    public void getTransactionDetial(String transNum, final ResultListener listener) {
        if (TextUtils.isEmpty(transNum)) {
            listener.onFaild(new Response(1, "没有输入订单号"));
            return;
        }
        if (transNum.startsWith("P") || transNum.startsWith("p")) {
            transNum = "P" + AppConfigHelper.getConfig(AppConfigDef.mid) + transNum.substring(1);
        }

//		voidTransaction.getTransDetial(transNum, new ResultListener() {
//
//			@Override
//			public void onSuccess(Response response) { // 查询
//				JSONObject _json = (JSONObject) response.getResult();
//				if (!TextUtils.isEmpty(_json.getString("payTran"))) {
//					PayTranRsp transactionCancelResp = new PayTranRsp();
//					transactionCancelResp.setMasterTranLogId(_json.getString("masterTranLogId"));
//					tranLogId = transactionCancelResp.getMasterTranLogId();
//					List<TranLog> tranLogs = JSONArray.parseArray(_json.getString("payTran"), TranLog.class);// 交易
//					transactionCancelResp.setPayTran(tranLogs);
//					if (!TextUtils.isEmpty(_json.getString("saleOrder"))) {
//						List<SaleOrder> saleOrders = JSONArray.parseArray(_json.getString("saleOrder"), SaleOrder.class);// 商品列表
//						transactionCancelResp.setSaleOrder(saleOrders);
//					}
//					if ("1".equals(_json.getString("mixedFlag"))) {
//						transactionCancelResp.setMixedFlag(1);
//						listener.onSuccess(new Response(0, "success", transactionCancelResp));
//					} else {
//						transactionCancelResp.setMixedFlag(0);
//						listener.onSuccess(new Response(0, "success", transactionCancelResp));
//					}
//					return;
//				} else {
//					listener.onFaild(new Response(1, "数据解析异常"));
//				}
//			}
//
//			@Override
//			public void onFaild(Response response) {
//				listener.onFaild(new Response(1, "查询订单失败:" + response.msg));
//			}
//		});
    }

    public void getTransactionDetial(String transNum, boolean bankcardpay, final ResultListener listener) {
        if (TextUtils.isEmpty(transNum)) {
            listener.onFaild(new Response(1, "没有输入订单号"));
            return;
        }
        if (transNum.startsWith("P") || transNum.startsWith("p")) {
            transNum = "P" + AppConfigHelper.getConfig(AppConfigDef.mid) + transNum.substring(1);
        }
        voidTransaction.getTransDetial(transNum, bankcardpay, new ResultListener() {

            @Override
            public void onSuccess(Response response) { // 查询
                JSONObject _json = (JSONObject) response.getResult();
                if (!TextUtils.isEmpty(_json.getString("payTran"))) {
                    PayTranRsp transactionCancelResp = new PayTranRsp();
                    transactionCancelResp.setMasterTranLogId(_json.getString("masterTranLogId"));
                    tranLogId = transactionCancelResp.getMasterTranLogId();
                    List<TranLog> tranLogs = JSONArray.parseArray(_json.getString("payTran"), TranLog.class);// 交易
                    transactionCancelResp.setPayTran(tranLogs);
                    JSONObject getTransInfo = (JSONObject) _json.getJSONArray("payTran").get(0);
                    transactionCancelResp.setRefundAmount(getTransInfo.getString("tranAmount"));
                    if (!TextUtils.isEmpty(_json.getString("saleOrder"))) {
                        List<SaleOrder> saleOrders = JSONArray.parseArray(_json.getString("saleOrder"), SaleOrder.class);// 商品列表
                        transactionCancelResp.setSaleOrder(saleOrders);
                    }
                    if ("1".equals(_json.getString("mixedFlag"))) {
                        transactionCancelResp.setMixedFlag(1);
                        listener.onSuccess(new Response(0, "撤销成功！", transactionCancelResp));
                    } else {
                        transactionCancelResp.setMixedFlag(0);
                        listener.onSuccess(new Response(0, "撤销成功！", transactionCancelResp));
                    }
                    return;
                } else {
                    listener.onFaild(new Response(1, "数据解析异常"));
                }
            }

            @Override
            public void onFaild(Response response) {
//                listener.onFaild(new Response(1, "查询订单失败:" + response.msg));
                listener.onFaild(new Response(1, response.msg));
            }
        });
    }

    public void cancel(PayTranRsp bean, boolean bankcardpay, String refundAmount, ResultListener listener) {
        if (bean.getMixedFlag() == 1) {
            cancelMixTrans(bean, listener);
        } else {
            List<TranLog> payTrans = bean.getPayTran();
            if (payTrans != null && !payTrans.isEmpty()) {// 交易列表是否为空
//                if (payTrans.size() == 1) {// 1笔交易
//                    if (Constants.SC_813_ALIPAY.equals(payTrans.get(0).getTran_code())) {// 支付宝订单号
//						cancelAlipay(bean, listener);
                revokeOnlinePayTransaction(bean, refundAmount, listener);
//                    } else if (Constants.SC_814_TENPAY.equals(payTrans.get(0).getTran_code())) {// 微信
//                        revokeOnlinePayTransaction(bean, refundAmount, listener);
////						cancelWepay(bean, listener);
//                    } else if (Constants.SC_841_TENPAY_QQ.equals(payTrans.get(0).getTran_code())) {// 手Q支付
////						cancelTenpay(bean, listener);
//                        revokeOnlinePayTransaction(bean, refundAmount, listener);
//                    } else if (Constants.SC_850_BAIDU.equals(payTrans.get(0).getTran_code())) {//百度支付
//                        revokeOnlinePayTransaction(bean, refundAmount, listener);
//                    } else if ("815".equals(payTrans.get(0).getTran_code())) {//威富通微信支付
//                        revokeOnlinePayTransaction(bean, refundAmount, listener);
//                    } else if ("817".equals(payTrans.get(0).getTran_code())) {//威富通支付宝支付
//                        revokeOnlinePayTransaction(bean, refundAmount, listener);
//                    } else if ("821".equals(payTrans.get(0).getTran_code())) {//威富通QQ钱包支付
//                        revokeOnlinePayTransaction(bean, refundAmount, listener);
//                    } else if ("825".equals(payTrans.get(0).getTran_code())) {//鑫蓝支付宝支付
//                        revokeOnlinePayTransaction(bean, refundAmount, listener);
//                    } else if ("827".equals(payTrans.get(0).getTran_code())) {//鑫蓝微信宝支付
//                        revokeOnlinePayTransaction(bean, refundAmount, listener);
//                    } else {
//                        cancelCommonTrans(bean, bankcardpay, refundAmount, listener);// 撤销其他种类订单
//                    }
//                } else {
//                    cancelCommonTrans(bean, bankcardpay, refundAmount, listener);// 撤销其他种类订单
//                }
            }
        }
    }

    private void revokeOnlinePayTransaction(PayTranRsp bean, String refundAmount, ResultListener listener) {
        setOrderSateVoid(bean, refundAmount, listener);
    }

    private void cancelMixTrans(PayTranRsp bean, final ResultListener listener) {
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            String containSale = "0";
            if (bean.getSaleOrder() != null && !bean.getSaleOrder().isEmpty()) {// 商品列表不为空
                containSale = "1";
            }
            params.put("tranLogId", tranLogId);
            params.put("tranCode", "");
            params.put("containSale", containSale);
            NetRequest.getInstance().addRequest(Constants.SC_906_GET_MIX_PAY_REVERSE, params, new ResponseListener() {

                @Override
                public void onSuccess(Response response) {
                    listener.onSuccess(response);
                }

                @Override
                public void onFaild(Response response) {
                    listener.onFaild(response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 撤销普通交易
//     */
//    private void cancelCommonTrans(PayTranRsp bean, boolean bankcardpay, String refundAmount, final ResultListener listener) {
//        if (bean == null || bean.getPayTran() == null || bean.getPayTran().isEmpty()) {
//            listener.onFaild(new Response(1, "撤销失败!"));
//            return;
//        }
//        TranLog tran = bean.getPayTran().get(0);
//        String containSale = "0";
//        if (bean.getSaleOrder() != null && !bean.getSaleOrder().isEmpty()) {// 商品列表不为空
//            containSale = "1";
//        }
//        String tranLogId = "";
//        String tranCode = tran.getTran_code();
//        if ("400".equals(tranCode)) {
//            tranLogId = tran.getMaster_tran_log_id();
//        } else if ("402".equals(tranCode)) {
//            tranLogId = tran.getOffline_tran_log_id();
//        } else if ("302".equals(tranCode)) {
//            tranLogId = tran.getMaster_tran_log_id();
//        } else if ("304".equals(tranCode)) {
//            tranLogId = tran.getMaster_tran_log_id();
//        } else if ("401".equals(tranCode)) {
//            tranLogId = tran.getMaster_tran_log_id();
//        } else {
//            tranLogId = tran.getMaster_tran_log_id();
//        }
//        Map<String, Object> params = new HashMap<String, Object>();
//        try {
//            if (!TextUtils.isEmpty(tran.getOrder_no())) {
//                params.put("orderNo", tran.getOrder_no());
//            }
//            params.put("tranLogId", tranLogId);
//            params.put("tranCode", tranCode);
//            params.put("containSale", containSale);
//            params.put("refundAmount", refundAmount);
//            params.put("batFlag", Constants.BAT_FLAG);
//            if (bankcardpay) {
//                params.put("isBankCard", Constants.BANKCARDPAY);
//            }
//            NetRequest.getInstance().addRequest(Constants.SC_955_TRANLOG_CANCEL, params, new ResponseListener() {
//
//                @Override
//                public void onSuccess(Response response) {
//                    listener.onSuccess(response);
//                }
//
//                @Override
//                public void onFaild(Response response) {
//                    listener.onFaild(response);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    /**
//     * 撤销
//     */
//    public void cancelTrans(DailyDetailResp bean, boolean bankcardpay, String refundAmount, final ResultListener listener) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        try {
//            params.put("tranLogId", bean.getTranLogId());
//            params.put("tranCode", bean.getTransType());
//            params.put("containSale", "0");
//            params.put("refundAmount", refundAmount);
//            params.put("batFlag", Constants.BAT_FLAG);
//            if (bankcardpay) {
//                params.put("isBankCard", Constants.BANKCARDPAY);
//            }
//            NetRequest.getInstance().addRequest(Constants.SC_955_TRANLOG_CANCEL, params, new ResponseListener() {
//
//                @Override
//                public void onSuccess(Response response) {
//                    listener.onSuccess(response);
//                }
//
//                @Override
//                public void onFaild(Response response) {
//                    listener.onFaild(response);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 获取交易类型
     *
     * @param tranLog
     * @return
     */
    public String getTransType(TranLog tranLog) {
        String transCode = tranLog.getTran_code();
        if (TextUtils.isEmpty(transCode)) {
            return "";
        }
        if ("700".equals(transCode)) {
            return "银行卡支付";
        } else if ("302".equals(transCode)) {
            return "会员卡充值";
        } else if ("304".equals(transCode)) {
            return "会员卡消费";
        } else if ("300".equals(transCode)) {
            return "会员卡发卡";
        } else if ("831".equals(transCode)) {
            return "会员卡冻结/会员卡解冻";
        } else if ("830".equals(transCode)) {
            return "会员卡换卡";
        } else if ("310".equals(transCode)) {
            return "会员卡消费撤销/会员卡充值撤销";
        } else if ("400".equals(transCode)) {
            return "现金支付";
        } else if ("814".equals(transCode)) {
            return "微信支付(含微店订单微信支付)";
        } else if ("813".equals(transCode)) {
            return "支付宝支付";
        } else if ("820".equals(transCode)) {
            return "第三方支付撤销(支付宝/微信)";
        } else if ("306".equals(transCode)) {
            return "会员卡注销";
        } else if ("315".equals(transCode)) {
            return "积分赠送";
        } else if ("316".equals(transCode)) {
            return "积分消费";
        } else if ("402".equals(transCode)) {
            if (TextUtils.isEmpty(tranLog.getOffline_tran_log_id())) {
                return "离线现金交易";
            } else {
                return "现金支付";
            }
        } else if ("401".equals(transCode)) {
            return " 其它支付";
        } else if ("405".equals(transCode)) {
            return "现金交易撤销/离线现金交易撤销";
        } else return "";
    }

    /**
     * 调用后台接口,将订单状态改为已撤销
     *
     * @param bean
     * @param listener
     */
    private void setOrderSateVoid(final PayTranRsp bean, String refundAmount, final ResultListener listener) {
        if (bean.getMixedFlag() == 1) {
            // TODO混合支付

        } else {
            int containSale = (bean.getSaleOrder() != null && bean.getSaleOrder().isEmpty() == false) ? 1 : 0;
            TranLog payTran = bean.getPayTran().get(0);
            doSetOrderVoidAction(payTran.getOrder_no(), tranLogId, payTran.getTran_code(), containSale, refundAmount, listener);
        }
    }

    /**
     * 设置订单状态-撤销支付调用
     *
     * @param orderNo     微信或支付宝 的 商户订单号
     * @param tranLogId   支付交易流水号
     * @param tranCode    交易码 900接口返回
     * @param containSale 900接口返回中是否包含saleOrder.如果saleOrder不为空传1,否则传0
     */
    protected void doSetOrderVoidAction(String orderNo, String tranLogId, String tranCode, int containSale, String refundAmount,
                                        final ResultListener listener) {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("orderNo", orderNo);
        m.put("tranLogId", tranLogId);// tranLogId
        m.put("tranCode", tranCode);
        m.put("containSale", containSale);
        m.put("refundAmount", refundAmount);
        m.put("batFlag", Constants.BAT_FLAG);
        m.put("refundOperId", AppConfigHelper.getConfig(AppConfigDef.refundOperId));
        m.put("refundOperName", AppConfigHelper.getConfig(AppConfigDef.refundOperName));
        NetRequest.getInstance().addRequest(Constants.SC_955_TRANLOG_CANCEL, m, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
//                statisticsPresenter = new StatisticsPresenter(PaymentApplication.getInstance());
                resp = new RefundDetailResp();
                if (response != null) {
                    JSONObject result = (JSONObject) response.getResult();
                    resp.setRefundAmount(result.getString("refundAmount").replace("-", "").trim());
                    resp.setTransKind(result.getString("tranDesc").replace("Refund", "").trim());
                    resp.setMasterTranLogId(result.getString("masterTranLogId"));
                    resp.setTranLogId(result.getString("id"));
                    resp.setThirdTradeNo(result.getString("thirdTradeNo"));
                    resp.setExchangeRate(result.getString("exchangeRate"));
                    resp.setCnyAmount(result.getString("cnyAmount"));
                    resp.setPayTime(result.getString("payTime"));
                    //交易货币
                    resp.setTransCurrency(result.getString(HttpConstants.API_955_RESPONSE.TRANSCURRENCY.getKey()).replace("-", "").trim());
                    resp.setTranAmount(result.getString(HttpConstants.API_955_RESPONSE.TRANAMOUNT.getKey()));
                    //
                    resp.setSn(result.getString(HttpConstants.API_955_RESPONSE.SN.getKey()));
                    //
                    if (result.getString(HttpConstants.API_955_RESPONSE.OPTNAME.getKey()) != null) {
                        resp.setOptName(result.getString(HttpConstants.API_955_RESPONSE.OPTNAME.getKey()));
                    } else {
                        resp.setOptName("");
                    }
                    resp.setSettlementCurrency(result.getString(HttpConstants.API_955_RESPONSE.SETTLEMENTCURRENCY.getKey()));
                    resp.setSettlementAmount(result.getString(HttpConstants.API_955_RESPONSE.SETTLEMENTAMOUNT.getKey()));
                    //发票号
                    resp.setMerchantTradeCode(result.getString(HttpConstants.API_955_RESPONSE.MERCHANTTRADECODE.getKey()));

                    if (result.getString("thirdExtName") != null) {
                        resp.setThirdExtName(result.getString("thirdExtName"));
                    }
                    if (result.getString("thirdExtId") != null) {
                        resp.setThirdExtId(result.getString("thirdExtId"));
                    }
                }
                AppConfigHelper.setConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT, getRefundPrintContext());
                AppConfigHelper.setConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT, getCustomerRefundPrintContext());
                AppConfigHelper.setConfig(AppConfigDef.refundOperId, "");
                AppConfigHelper.setConfig(AppConfigDef.refundOperName, "");
//                statisticsPresenter.printRefund(resp);
                listener.onSuccess(new Response(0, PaymentApplication.getInstance().getString(R.string.cancel_success)));
            }

            @Override
            public void onFaild(Response response) {
                AppConfigHelper.setConfig(AppConfigDef.refundOperId, "");
                AppConfigHelper.setConfig(AppConfigDef.refundOperName, "");
                listener.onFaild(response);
            }
        });
    }

    public String getCustomerRefundPrintContext() {
        String printString = null;
        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
            HTMLPrintModel model = new HTMLPrintModel();
            List<HtmlLine> lines = new ArrayList<>();
            lines.add(new HTMLPrintModel.SimpleLine(AppConfigHelper.getConfig(AppConfigDef.merchantName), true, true));
            try {
                String address = AppConfigHelper.getConfig(AppConfigDef.merchantAddr);
                if (!TextUtils.isEmpty(address)) {
                    if (address.getBytes("GBK").length <= 32) {
                        lines.add(new HTMLPrintModel.SimpleLine(address, false, true));
                    } else if (address.getBytes("GBK").length <= 64) {
                        lines.add(new HTMLPrintModel.SimpleLine(address.substring(0, 32), false, true));
                        lines.add(new HTMLPrintModel.SimpleLine(address.substring(32), false, true));
                    } else if (address.getBytes("GBK").length <= 96) {
                        lines.add(new HTMLPrintModel.SimpleLine(address.substring(0, 32), false, true));
                        lines.add(new HTMLPrintModel.SimpleLine(address.substring(32, 64), false, true));
                        lines.add(new HTMLPrintModel.SimpleLine(address.substring(64), false, true));
                    }
                }
                lines.add(new HTMLPrintModel.SimpleLine(AppConfigHelper.getConfig(AppConfigDef.merchantTel), false, true));
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_merchant_id) + AppConfigHelper.getConfig(AppConfigDef.mid)));

//                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_terminal_id) + AppConfigHelper.getConfig(AppConfigDef.sn)));
//                HTMLPrintModel.SimpleLine snPrinHtml = PrintManager.printHtmlSn(context, resp.getSn());
//                lines.add(snPrinHtml);
                lines.add(DeviceContent.printHtmlDevice(context));

//                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_cashier_id) + AppConfigHelper.getConfig(AppConfigDef.operatorTrueName)));
//                HTMLPrintModel.SimpleLine optHtml = PrintManager.printHtmlOptName(context, resp.getOptName());
//                lines.add(optHtml);
                lines.add(CashierIdContent.printHtml(context));

                lines.add(new HTMLPrintModel.EmptyLine());
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.refund_uppercase), true, true));

//                String printTotal = context.getString(R.string.print_total);
//                String transCurrency = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(resp.getTransCurrency());
//                lines.add(new HTMLPrintModel.LeftAndRightLine(printTotal, transCurrency + Calculater.formotFen(resp.getRefundAmount())));

                lines.add(TotalContent.printHtmlRefund(context, resp));

//                String exchangeRate = resp.getExchangeRate();
//                if (TextUtils.isEmpty(exchangeRate)) {
//                    exchangeRate = "1";
//                }

//                String cnyAmount = Calculater.formotFen(resp.getCnyAmount()).replace("-", "").trim();
//                if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
//                    cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(Calculater.formotFen(resp.getRefundAmount()), exchangeRate)));
//                }
//                lines.add(new HTMLPrintModel.LeftAndRightLine("", "CNY " + cnyAmount));
//                HTMLPrintModel.LeftAndRightLine setlePrint = PrintManager.printHtmlSettlement(exchangeRate, resp);
//                lines.add(setlePrint);
                lines.add(SettlementContent.printHtmlSettlementRefund(resp));
                lines.add(new HTMLPrintModel.EmptyLine());



                String tranlogId = Tools.deleteMidTranLog(resp.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
                String printRecepit = context.getString(R.string.print_receipt);
                lines.add(new HTMLPrintModel.LeftAndRightLine(printRecepit + "#", tranlogId));
                lines.add(new HTMLPrintModel.LeftAndRightLine(resp.getPayTime().substring(0, 10), resp.getPayTime().substring(10)));
                String payType = resp.getTransKind();
                if ("WechatPay".equals(payType)) {
                    payType = "Wechat Pay";
                }
                lines.add(new HTMLPrintModel.LeftAndRightLine(context.getString(R.string.print_type), payType));
                String thirdTransOrder = resp.getThirdTradeNo();
                if (!TextUtils.isEmpty(thirdTransOrder)) {
                    lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_trans)));
                    lines.add(new HTMLPrintModel.LeftAndRightLine("", thirdTransOrder));
                }

                //invoice打印
                InvoiceContent.printHtmlRefund(context, lines, resp);

                String acctName = resp.getThirdExtName();
                if (!TextUtils.isEmpty(acctName)) {
                    String printAcctName = context.getString(R.string.print_acctName);
                    lines.add(new HTMLPrintModel.LeftAndRightLine(printAcctName, acctName));
                }
                String acct = resp.getThirdExtId();
                if (!TextUtils.isEmpty(acct)) {
                    String printAcct = context.getString(R.string.print_acct);
                    lines.add(new HTMLPrintModel.LeftAndRightLine(printAcct, acct));
                }
                lines.add(new HTMLPrintModel.EmptyLine());
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_approved), true, true));
                lines.add(new HTMLPrintModel.EmptyLine());
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_customer_copy), true, true));
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_important), true, true));
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_hint1), true, true));
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_hint2), true, true));
                lines.add(new HTMLPrintModel.BranchLine());
                model.setLineList(lines);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            printString = ToHTMLUtil.toHtml(model);
            return printString;
        }
        try {
            Q1PrintBuilder builder = new Q1PrintBuilder();
            printString = "";
            String merchant = AppConfigHelper.getConfig(AppConfigDef.merchantName);
            printString += builder.center(builder.bold(merchant));
            String address = AppConfigHelper.getConfig(AppConfigDef.merchantAddr);
            if (!TextUtils.isEmpty(address)) {
                if (address.getBytes("GBK").length <= 32) {
                    printString += builder.center(address);
                } else if (address.getBytes("GBK").length <= 64) {
                    printString += builder.center(address.substring(0, 32));
                    printString += builder.center(address.substring(32));
                } else if (address.getBytes("GBK").length <= 96) {
                    printString += builder.center(address.substring(0, 32));
                    printString += builder.center(address.substring(32, 64));
                    printString += builder.center(address.substring(64));
                }
            }
            String tel = AppConfigHelper.getConfig(AppConfigDef.merchantTel);
            if (!TextUtils.isEmpty(tel)) {
                printString += builder.center(tel);
            }
            String merchantId = AppConfigHelper.getConfig(AppConfigDef.mid);
            printString += context.getString(R.string.print_merchant_id) + merchantId + builder.br();

//            String terminalId = AppConfigHelper.getConfig(AppConfigDef.sn);
//            printStringPayFor += context.getString(R.string.print_terminal_id) + terminalId + builder.br();
//            String snStr = resp.getSn();
//            printStringPayFor += PrintManager.printStringSn(context, snStr) + builder.br();
            printString += DeviceContent.printStringDevice(context) + builder.br();

//            String cahierId = AppConfigHelper.getConfig(AppConfigDef.operatorTrueName);
//            printStringPayFor += context.getString(R.string.print_cashier_id) + cahierId + builder.br();
//            String optNameStr = resp.getOptName();
//            printStringPayFor += PrintManager.printStringOptName(context, optNameStr) + builder.br();
            printString += CashierIdContent.printString(context) + builder.br();

            printString += builder.br();
            printString += builder.center(builder.bold(context.getString(R.string.refund_uppercase))) + builder.br();
            //
//            String transCurrency = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(resp.getTransCurrency());
//            int numTotalSpace = PrintManager.tranZhSpaceNums(25, 1, resp.getTransCurrency());
//            String printTotal = context.getString(R.string.print_total);
//            printStringPayFor += printTotal + multipleSpaces(numTotalSpace - Calculater.formotFen(resp.getRefundAmount()).length()) + transCurrency + Calculater.formotFen(resp.getRefundAmount()) + builder.br();

            printString += TotalContent.printStringRefund(context, resp) + builder.br();

//            String exchangeRate = resp.getExchangeRate();
//            if (TextUtils.isEmpty(exchangeRate)) {
//                exchangeRate = "1";
//            }

//            String cnyAmount = Calculater.formotFen(resp.getCnyAmount()).replace("-", "").trim();
//            if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
//                cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(Calculater.formotFen(resp.getRefundAmount()), exchangeRate)));
//            }
//            printStringPayFor += multipleSpaces(28 - cnyAmount.length()) + "CNY " + cnyAmount + builder.br();

//            String selPrint = PrintManager.printStringSettlement(exchangeRate, resp);
//            printStringPayFor += selPrint + builder.br();
            printString += SettlementContent.printStringSettlementRefund(resp) + builder.br();

            printString += builder.br()+builder.nBr();



            String tranlogId = Tools.deleteMidTranLog(resp.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
            String printRecepit = context.getString(R.string.print_receipt);
            printString += printRecepit + "#" + multipleSpaces(31 - printRecepit.getBytes("GBK").length - tranlogId.length()) + tranlogId + builder.br();
            printString += resp.getPayTime().substring(0, 10) + multipleSpaces(22 - resp.getPayTime().substring(10).length()) + resp.getPayTime().substring(10) + builder.br();
            String payType = resp.getTransKind();
            if ("WechatPay".equals(payType)) {
                payType = "Wechat Pay";
            } else if (payType.contains("Union")) {
                payType = "Union Pay QR";
            }
            String printType = context.getString(R.string.print_type);
            printString += printType + multipleSpaces(32 - printType.getBytes("GBK").length - payType.getBytes("GBK").length) + payType + builder.br();
            String thirdTransOrder = resp.getThirdTradeNo();
            if (!TextUtils.isEmpty(thirdTransOrder)) {
                printString += context.getString(R.string.print_trans) + builder.br();
                printString += multipleSpaces(32 - thirdTransOrder.getBytes("GBK").length) + thirdTransOrder + builder.br();
            }
            String[] invoicePrint = InvoiceContent.printStringRefund(context, resp);
            if (invoicePrint != null) {
                for (String str : invoicePrint) {
                    printString += str + builder.br();
                }
            }

            String acctName = resp.getThirdExtName();
            if (!TextUtils.isEmpty(acctName)) {
                String printAcctName = context.getString(R.string.print_acctName);
                printString += printAcctName + multipleSpaces(32 - printAcctName.getBytes("GBK").length - acctName.getBytes("GBK").length) + acctName + builder.br();
            }
            String acct = resp.getThirdExtId();
            if (!TextUtils.isEmpty(acct)) {
                String printAcct = context.getString(R.string.print_acct);
                printString += printAcct + multipleSpaces(32 - printAcct.getBytes("GBK").length - acct.getBytes("GBK").length) + acct + builder.br();
            }

            printString += builder.br();
            printString += builder.center(builder.bold(context.getString(R.string.print_approved)));
            printString += builder.br();
            printString += builder.center(builder.bold(context.getString(R.string.print_customer_copy)));
            printString += builder.center(builder.bold(context.getString(R.string.print_important)));
            printString += builder.center(builder.bold(context.getString(R.string.print_hint1)));
            printString += builder.center(builder.bold(context.getString(R.string.print_hint2)));
            printString += builder.branch() + builder.endPrint();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return printString;
    }

    public String getRefundPrintContext() {
        String printString = null;
        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
            HTMLPrintModel model = new HTMLPrintModel();
            List<HtmlLine> lines = new ArrayList<>();
            lines.add(new HTMLPrintModel.SimpleLine(AppConfigHelper.getConfig(AppConfigDef.merchantName), true, true));
            try {
                String address = AppConfigHelper.getConfig(AppConfigDef.merchantAddr);
                if (!TextUtils.isEmpty(address)) {
                    if (address.getBytes("GBK").length <= 32) {
                        lines.add(new HTMLPrintModel.SimpleLine(address, false, true));
                    } else if (address.getBytes("GBK").length <= 64) {
                        lines.add(new HTMLPrintModel.SimpleLine(address.substring(0, 32), false, true));
                        lines.add(new HTMLPrintModel.SimpleLine(address.substring(32), false, true));
                    } else if (address.getBytes("GBK").length <= 96) {
                        lines.add(new HTMLPrintModel.SimpleLine(address.substring(0, 32), false, true));
                        lines.add(new HTMLPrintModel.SimpleLine(address.substring(32, 64), false, true));
                        lines.add(new HTMLPrintModel.SimpleLine(address.substring(64), false, true));
                    }
                }
                lines.add(new HTMLPrintModel.SimpleLine(AppConfigHelper.getConfig(AppConfigDef.merchantTel), false, true));
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_merchant_id) + AppConfigHelper.getConfig(AppConfigDef.mid)));

//                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_terminal_id) + AppConfigHelper.getConfig(AppConfigDef.sn)));
//                HTMLPrintModel.SimpleLine snHtml = PrintManager.printHtmlSn(context, resp.getSn());
//                lines.add(snHtml);
                lines.add(DeviceContent.printHtmlDevice(context));

//                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_cashier_id) + AppConfigHelper.getConfig(AppConfigDef.operatorTrueName)));
//                HTMLPrintModel.SimpleLine optHtml = PrintManager.printHtmlOptName(context, resp.getOptName());
//                lines.add(optHtml);
                lines.add(CashierIdContent.printHtml(context));

                lines.add(new HTMLPrintModel.EmptyLine());
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.refund_uppercase), true, true));

//                String transCurrency = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(resp.getTransCurrency());
//                String printTotal = context.getString(R.string.print_total);
//                lines.add(new HTMLPrintModel.LeftAndRightLine(printTotal, transCurrency + Calculater.formotFen(resp.getRefundAmount())));
                lines.add(TotalContent.printHtmlRefund(context, resp));


                String exchangeRate = resp.getExchangeRate();
                if (TextUtils.isEmpty(exchangeRate)) {
                    exchangeRate = "1";
                }
//                String cnyAmount = Calculater.formotFen(resp.getCnyAmount()).replace("-", "").trim();
//                if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
//                    cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(Calculater.formotFen(resp.getRefundAmount()), exchangeRate)));
//                }
//                lines.add(new HTMLPrintModel.LeftAndRightLine("", "CNY " + cnyAmount));

//                HTMLPrintModel.LeftAndRightLine setlementHtml = PrintManager.printHtmlSettlement(exchangeRate, resp);
//                lines.add(setlementHtml);
                lines.add(SettlementContent.printHtmlSettlementRefund(resp));


                lines.add(new HTMLPrintModel.EmptyLine());



                String tranlogId = Tools.deleteMidTranLog(resp.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
                String printRecepit = context.getString(R.string.print_receipt);
                lines.add(new HTMLPrintModel.LeftAndRightLine(printRecepit + "#", tranlogId));
                lines.add(new HTMLPrintModel.LeftAndRightLine(resp.getPayTime().substring(0, 10), resp.getPayTime().substring(10)));
                String payType = resp.getTransKind();
                if ("WechatPay".equals(payType)) {
                    payType = "Wechat Pay";
                }
                lines.add(new HTMLPrintModel.LeftAndRightLine(context.getString(R.string.print_type), payType));
                String thirdTransOrder = resp.getThirdTradeNo();
                if (!TextUtils.isEmpty(thirdTransOrder)) {
                    lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_trans)));
                    lines.add(new HTMLPrintModel.LeftAndRightLine("", thirdTransOrder));
                }

                //invoice打印
                InvoiceContent.printHtmlRefund(context, lines, resp);

                String acctName = resp.getThirdExtName();
                if (!TextUtils.isEmpty(acctName)) {
                    String printAcctName = context.getString(R.string.print_acctName);
                    lines.add(new HTMLPrintModel.LeftAndRightLine(printAcctName, acctName));
                }
                String acct = resp.getThirdExtId();
                if (!TextUtils.isEmpty(acct)) {
                    String printAcct = context.getString(R.string.print_acct);
                    lines.add(new HTMLPrintModel.LeftAndRightLine(printAcct, acct));
                }
                lines.add(new HTMLPrintModel.EmptyLine());
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_approved), true, true));
                lines.add(new HTMLPrintModel.EmptyLine());
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_merchant_copy), true, true));
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_important), true, true));
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_hint1), true, true));
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_hint2), true, true));
                lines.add(new HTMLPrintModel.BranchLine());
                model.setLineList(lines);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            printString = ToHTMLUtil.toHtml(model);
            return printString;
        }
        try {
            Q1PrintBuilder builder = new Q1PrintBuilder();
            printString = "";
            String merchant = AppConfigHelper.getConfig(AppConfigDef.merchantName);
            printString += builder.center(builder.bold(merchant));
            String address = AppConfigHelper.getConfig(AppConfigDef.merchantAddr);
            if (!TextUtils.isEmpty(address)) {
                if (address.getBytes("GBK").length <= 32) {
                    printString += builder.center(address);
                } else if (address.getBytes("GBK").length <= 64) {
                    printString += builder.center(address.substring(0, 32));
                    printString += builder.center(address.substring(32));
                } else if (address.getBytes("GBK").length <= 96) {
                    printString += builder.center(address.substring(0, 32));
                    printString += builder.center(address.substring(32, 64));
                    printString += builder.center(address.substring(64));
                }
            }
            String tel = AppConfigHelper.getConfig(AppConfigDef.merchantTel);
            if (!TextUtils.isEmpty(tel)) {
                printString += builder.center(tel);
            }
            String merchantId = AppConfigHelper.getConfig(AppConfigDef.mid);
            printString += context.getString(R.string.print_merchant_id) + merchantId + builder.br();

//            String terminalId = AppConfigHelper.getConfig(AppConfigDef.sn);
//            printStringPayFor += context.getString(R.string.print_terminal_id) + terminalId + builder.br();
//            String snStr = resp.getSn();
//            printStringPayFor += PrintManager.printStringSn(context, snStr) + builder.br();
            printString += DeviceContent.printStringDevice(context) + builder.br();


//            String cahierId = AppConfigHelper.getConfig(AppConfigDef.operatorTrueName);
//            printStringPayFor += context.getString(R.string.print_cashier_id) + cahierId + builder.br();
//            String optName = resp.getOptName();
//            printStringPayFor += PrintManager.printStringOptName(context, optName) + builder.br();
            printString += CashierIdContent.printString(context) + builder.br();


            printString += builder.br();
            printString += builder.center(builder.bold(context.getString(R.string.refund_uppercase))) + builder.br();

//            String transCurrency = TransRecordLogicConstants.TRANSCURRENCY.getPrintStr(resp.getTransCurrency());
//            int numTotalSpace = PrintManager.tranZhSpaceNums(25, 1, resp.getTransCurrency());
//            String printTotal = context.getString(R.string.print_total);
//            printStringPayFor += printTotal + multipleSpaces(numTotalSpace - Calculater.formotFen(resp.getRefundAmount()).length()) + transCurrency + Calculater.formotFen(resp.getRefundAmount()) + builder.br();
            printString += TotalContent.printStringRefund(context, resp) + builder.br();

//            String exchangeRate = resp.getExchangeRate();
//            if (TextUtils.isEmpty(exchangeRate)) {
//                exchangeRate = "1";
//            }

//            String cnyAmount = Calculater.formotFen(resp.getCnyAmount()).replace("-", "").trim();
//            if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
//                cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(Calculater.formotFen(resp.getRefundAmount()), exchangeRate)));
//            }
//            printStringPayFor += multipleSpaces(28 - cnyAmount.length()) + "CNY " + cnyAmount + builder.br();

//            String printSetlement = PrintManager.printStringSettlement(exchangeRate, resp);
//            printStringPayFor += printSetlement + builder.br();
            printString += SettlementContent.printStringSettlementRefund(resp) + builder.br();


            printString += builder.br() + builder.nBr();



            String tranlogId = Tools.deleteMidTranLog(resp.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
            String printRecepit = context.getString(R.string.print_receipt);
            printString += printRecepit + "#" + multipleSpaces(31 - printRecepit.getBytes("GBK").length - tranlogId.length()) + tranlogId + builder.br();
            printString += resp.getPayTime().substring(0, 10) + multipleSpaces(22 - resp.getPayTime().substring(10).length()) + resp.getPayTime().substring(10) + builder.br();
            String payType = resp.getTransKind();
            if ("WechatPay".equals(payType)) {
                payType = "Wechat Pay";
            } else if (payType.contains("Union")) {
                payType = "Union Pay QR";
            }
            String printType = context.getString(R.string.print_type);
            printString += printType + multipleSpaces(32 - printType.getBytes("GBK").length - payType.getBytes("GBK").length) + payType + builder.br();
            String thirdTransOrder = resp.getThirdTradeNo();
            if (!TextUtils.isEmpty(thirdTransOrder)) {
                printString += context.getString(R.string.print_trans) + builder.br();
                printString += multipleSpaces(32 - thirdTransOrder.getBytes("GBK").length) + thirdTransOrder + builder.br();
            }
            String[] invoicePrint = InvoiceContent.printStringRefund(context, resp);
            if (invoicePrint != null) {
                for (String str : invoicePrint) {
                    printString += str + builder.br();
                }
            }

            String acctName = resp.getThirdExtName();
            if (!TextUtils.isEmpty(acctName)) {
                String printAcctName = context.getString(R.string.print_acctName);
                printString += printAcctName + multipleSpaces(32 - printAcctName.getBytes("GBK").length - acctName.getBytes("GBK").length) + acctName + builder.br();
            }
            String acct = resp.getThirdExtId();
            if (!TextUtils.isEmpty(acct)) {
                String printAcct = context.getString(R.string.print_acct);
                printString += printAcct + multipleSpaces(32 - printAcct.getBytes("GBK").length - acct.getBytes("GBK").length) + acct + builder.br();
            }

            printString += builder.br();
            printString += builder.center(builder.bold(context.getString(R.string.print_approved)));
            printString += builder.br();
            printString += builder.center(builder.bold(context.getString(R.string.print_merchant_copy)));
            printString += builder.center(builder.bold(context.getString(R.string.print_important)));
            printString += builder.center(builder.bold(context.getString(R.string.print_hint1)));
            printString += builder.center(builder.bold(context.getString(R.string.print_hint2)));
            printString += builder.branch() + builder.endPrint();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return printString;
    }

    /**
     * 打印空格
     *
     * @param n
     * @return
     */
    public String multipleSpaces(int n) {
        String output = "";
        for (int i = 0; i < n; i++)
            output += " ";
        return output;
    }

}
