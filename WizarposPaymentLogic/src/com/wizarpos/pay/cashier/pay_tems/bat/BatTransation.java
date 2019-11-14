package com.wizarpos.pay.cashier.pay_tems.bat;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wizarpos.atool.log.Logger;
import com.wizarpos.atool.net.socket.SocketRequest;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.base.net.Response;
import com.wizarpos.device.printer.html.ToHTMLUtil;
import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.device.printer.html.model.HtmlLine;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.model.TicketDef;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.pay_tems.LooperQueryerTransactionState;
import com.wizarpos.pay.cashier.pay_tems.LooperTask;
import com.wizarpos.pay.cashier.pay_tems.bat.entities.BatWeimobTicket;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.ticket.TicketManagerFactory;
import com.wizarpos.pay.cashier.presenter.ticket.inf.CommonTicketManager;
import com.wizarpos.pay.cashier.presenter.transaction.impl.OnlinePaymentTransactionImpl;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.GetCommonTicketInfoResp;
import com.wizarpos.pay.model.OrderDef;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class BatTransation extends OnlinePaymentTransactionImpl {
    private LooperQueryerTransactionState queryer;
    private String transType = "";

    protected BatWeimobTicket batTicket;//第三方微盟券使用对象

    protected List<TicketInfo> usedTicketlist = new ArrayList<TicketInfo>();    //用掉的券 yaosong

    private CommonTicketManager commonTicketManager;

    private static final int PRINT = 101;

    private int currentSize;
    private PrintListener printListener;
    private ResultListener resultListener;
    private Response response;
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == PRINT) {
//                if (printListener != null) {
//                    int printNumber = 1;
//                    if (!TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.print_number))) {
//                        printNumber = Integer.parseInt(AppConfigHelper.getConfig(AppConfigDef.print_number));
//                    }
//                    printListener.onPrint(printNumber, currentSize);
//                }
//            }
//        }
//    };

    public BatTransation(Context context) {
        super(context);
    }

    public BatWeimobTicket getBatTicket() {
        return batTicket;
    }

    public void setPrintListener(PrintListener printListener) {
        this.printListener = printListener;
    }

    public void setBatTicket(BatWeimobTicket batTicket) {
        transactionInfo.setBatTicket(batTicket);
        this.batTicket = batTicket;
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        transType = transactionInfo.getPayTypeFlag();
        commonTicketManager = TicketManagerFactory.createCommonTicketManager(context);
        if (Constants.ALIPAY_BAT.equals(transType)) {
            defTransactionType(TransactionTemsController.TRANSACTION_TYPE_ALIPAY);
        } else if (Constants.WEPAY__BAT.equals(transType)) {
            defTransactionType(TransactionTemsController.TRANSACTION_TYPE_WEPAY_PAY);
        } else if (Constants.TENPAY_BAT.equals(transType)) {
            defTransactionType(TransactionTemsController.TRANSACTION_TYPE_TEN_PAY);
        } else if (Constants.BAIDUPAY_BAT.equals(transType)) {
            defTransactionType(TransactionTemsController.TRANSACTION_TYPE_BAIDU_PAY);
        } else if (Constants.UNION_BAT.equals(transType)) {
            defTransactionType(TransactionTemsController.TRANSACTION_TYPE_UNION_PAY);
        }
    }

    @Override
    public void printTransInfo() {
//        if (transactionInfo.isNeedPrint()) {
//            return;
//        }
//        int printNumber = 1;
//        if (!TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.print_number))) {
//            printNumber = Integer.parseInt(AppConfigHelper.getConfig(AppConfigDef.print_number));
//        }
//        if (printNumber > 0) {
//            PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
//            controller.print(getPrintContext());
//            currentSize++;
//            if (currentSize >= printNumber) {
//                this.resultListener.onSuccess(response);
//                return;
//            }
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    handler.sendEmptyMessage(PRINT);
//                }
//            }, 2000L);
//        } else {
//            this.resultListener.onSuccess(response);
//        }
    }

    public void printTransInfoWithListener(Response response, ResultListener resultListener) {
        this.resultListener = resultListener;
        this.response = response;
        printTransInfo();
    }

    public String getPrintContext() {
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
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_terminal_id) + AppConfigHelper.getConfig(AppConfigDef.sn)));
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_cashier_id) + AppConfigHelper.getConfig(AppConfigDef.operatorTrueName)));
                lines.add(new HTMLPrintModel.EmptyLine());
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_sale), true, true));
                String totalAmount = Calculater.formotFen(transactionInfo.getRealAmount());
                String tipsAmount = transactionInfo.getTips();
                String printPurchase = context.getString(R.string.print_purchase);
                if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
                    String purchaseAmount = Calculater.formotFen(Calculater.subtract(totalAmount, tipsAmount));
                    lines.add(new HTMLPrintModel.LeftAndRightLine(printPurchase, "$" + purchaseAmount));
                } else {
                    lines.add(new HTMLPrintModel.LeftAndRightLine(printPurchase, "$" + totalAmount));
                }
                if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
                    String printTip = context.getString(R.string.print_tip);
                    lines.add(new HTMLPrintModel.LeftAndRightLine(printTip, "$" + Calculater.formotFen(tipsAmount)));
                }
                lines.add(new HTMLPrintModel.LeftAndRightLine("Total:", "$" + totalAmount));
                String exchangeRate = AppConfigHelper.getConfig(AppConfigDef.exchangeRate);
                if (TextUtils.isEmpty(exchangeRate)) {
                    exchangeRate = "1";
                }
                String cnyAmount = Calculater.formotFen(AppConfigHelper.getConfig(AppConfigDef.CNY_AMOUNT)).trim();
                if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
                    cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(totalAmount, exchangeRate)));
                }
                lines.add(new HTMLPrintModel.LeftAndRightLine("", "CNY " + cnyAmount));
                String showCNY = "CAD 1.00=CNY " + Calculater.multiply("1", exchangeRate);
                String printFx = context.getString(R.string.print_fx_rate);
                lines.add(new HTMLPrintModel.LeftAndRightLine(printFx, showCNY));
                lines.add(new HTMLPrintModel.EmptyLine());
                String tranlogId = Tools.deleteMidTranLog(transactionInfo.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
                String printRecepit = context.getString(R.string.print_receipt);
                lines.add(new HTMLPrintModel.LeftAndRightLine(printRecepit + "#", tranlogId));
                lines.add(new HTMLPrintModel.LeftAndRightLine(transactionInfo.getPayTime().substring(0, 10), transactionInfo.getPayTime().substring(10)));
                if (Constants.ALIPAYFLAG.equals(transactionInfo.getPayType())) {
                    lines.add(new HTMLPrintModel.LeftAndRightLine(context.getString(R.string.print_type), "Alipay"));
                } else if (Constants.WEPAYFLAG.equals(transactionInfo.getPayType())) {
                    lines.add(new HTMLPrintModel.LeftAndRightLine(context.getString(R.string.print_type), "Wechat Pay"));
                }

                String thirdTransOrder = transactionInfo.getThirdTradeNo();
                if (!TextUtils.isEmpty(thirdTransOrder)) {
                    lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_trans)));
                    lines.add(new HTMLPrintModel.LeftAndRightLine("", thirdTransOrder));
                }
                String acctName = transactionInfo.getThirdExtName();
                if (!TextUtils.isEmpty(acctName)) {
                    String printAcctName = context.getString(R.string.print_acctName);
                    lines.add(new HTMLPrintModel.LeftAndRightLine(printAcctName, acctName));
                }
                String acct = transactionInfo.getThirdExtId();
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
            String terminalId = AppConfigHelper.getConfig(AppConfigDef.sn);
            printString += context.getString(R.string.print_terminal_id) + terminalId + builder.br();
            String cahierId = AppConfigHelper.getConfig(AppConfigDef.operatorTrueName);
            printString += context.getString(R.string.print_cashier_id) + cahierId + builder.br();
            printString += builder.br() + builder.nBr();
            printString += builder.center(builder.bold(context.getString(R.string.print_sale))) + builder.br() + builder.nBr();
            String totalAmount = Calculater.formotFen(transactionInfo.getRealAmount());
            String tipsAmount = transactionInfo.getTips();
            String printPurchase = context.getString(R.string.print_purchase);
            if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
                String purchaseAmount = Calculater.formotFen(Calculater.subtract(transactionInfo.getRealAmount(), tipsAmount));
                printString += printPurchase + multipleSpaces(31 - printPurchase.getBytes("GBK").length - purchaseAmount.length()) + "$" + purchaseAmount + builder.br();
            } else {
                printString += printPurchase + multipleSpaces(31 - printPurchase.getBytes("GBK").length - totalAmount.length()) + "$" + totalAmount + builder.br();
            }
            String printTip = context.getString(R.string.print_tip);
            if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
                printString += printTip + multipleSpaces(31 - printTip.getBytes("GBK").length - Calculater.formotFen(transactionInfo.getRealAmount()).length()) + "$" + Calculater.formotFen(tipsAmount) + builder.br();
            }
            printString += "Total:" + multipleSpaces(25 - totalAmount.length()) + "$" + totalAmount + builder.br();
            String exchangeRate = AppConfigHelper.getConfig(AppConfigDef.exchangeRate);
            if (TextUtils.isEmpty(exchangeRate)) {
                exchangeRate = "1";
            }
            String cnyAmount = Calculater.formotFen(AppConfigHelper.getConfig(AppConfigDef.CNY_AMOUNT)).trim();
            if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
                cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(totalAmount, exchangeRate)));
            }
            printString += multipleSpaces(28 - cnyAmount.length()) + "CNY " + cnyAmount + builder.br();
            String showCNY = "CAD 1.00=CNY " + Calculater.multiply("1", exchangeRate);
            String printFx = context.getString(R.string.print_fx_rate);
            printString += printFx + multipleSpaces(32 - printFx.getBytes("GBK").length - showCNY.length()) + showCNY + builder.br();
            printString += builder.br() + builder.nBr();
            String tranlogId = Tools.deleteMidTranLog(transactionInfo.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
            String printRecepit = context.getString(R.string.print_receipt);
            printString += printRecepit + "#" + multipleSpaces(31 - printRecepit.getBytes("GBK").length - tranlogId.getBytes("GBK").length) + tranlogId + builder.br();
            printString += transactionInfo.getPayTime().substring(0, 10) + multipleSpaces(22 - transactionInfo.getPayTime().substring(10).length()) + transactionInfo.getPayTime().substring(10) + builder.br();
            String printType = context.getString(R.string.print_type);
            if (Constants.ALIPAYFLAG.equals(transactionInfo.getPayType())) {
                printString += printType + multipleSpaces(26 - printType.getBytes("GBK").length) + "Alipay" + builder.br();
            } else if (Constants.WEPAYFLAG.equals(transactionInfo.getPayType())) {
                printString += printType + multipleSpaces(22 - printType.getBytes("GBK").length) + "Wechat Pay" + builder.br();
            } else if (Constants.UNION.equals(transactionInfo.getPayType())) {
                printString += printType + multipleSpaces(20 - printType.getBytes("GBK").length) + "Union Pay QR" + builder.br();
            }


            String thirdTransOrder = transactionInfo.getThirdTradeNo();
            if (!TextUtils.isEmpty(thirdTransOrder)) {
                printString += context.getString(R.string.print_trans) + builder.br();
                printString += multipleSpaces(32 - thirdTransOrder.getBytes("GBK").length) + thirdTransOrder + builder.br();
            }
            String acctName = transactionInfo.getThirdExtName();
            if (!TextUtils.isEmpty(acctName)) {
                String printAcctName = context.getString(R.string.print_acctName);
                printString += printAcctName + multipleSpaces(32 - printAcctName.getBytes("GBK").length - acctName.getBytes("GBK").length) + acctName + builder.br();
            }
            String acct = transactionInfo.getThirdExtId();
            if (!TextUtils.isEmpty(acct)) {
                String printAcct = context.getString(R.string.print_acct);
                printString += printAcct + multipleSpaces(32 - printAcct.getBytes("GBK").length - acct.getBytes("GBK").length) + acct + builder.br();
            }
            printString += builder.br();
            printString += builder.center(builder.bold(context.getString(R.string.print_approved)));
            printString += builder.br()+ builder.nBr();
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

    public String getCustomerPrintContext() {
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
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_terminal_id) + AppConfigHelper.getConfig(AppConfigDef.sn)));
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_cashier_id) + AppConfigHelper.getConfig(AppConfigDef.operatorTrueName)));
                lines.add(new HTMLPrintModel.EmptyLine());
                lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_sale), true, true));
                String totalAmount = Calculater.formotFen(transactionInfo.getRealAmount());
                String tipsAmount = transactionInfo.getTips();
                String printPurchase = context.getString(R.string.print_purchase);
                if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
                    String purchaseAmount = Calculater.formotFen(Calculater.subtract(totalAmount, tipsAmount));
                    lines.add(new HTMLPrintModel.LeftAndRightLine(printPurchase, "$" + purchaseAmount));
                } else {
                    lines.add(new HTMLPrintModel.LeftAndRightLine(printPurchase, "$" + totalAmount));
                }
                if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
                    String printTip = context.getString(R.string.print_tip);
                    lines.add(new HTMLPrintModel.LeftAndRightLine(printTip, "$" + Calculater.formotFen(tipsAmount)));
                }
                lines.add(new HTMLPrintModel.LeftAndRightLine("Total:", "$" + totalAmount));
                String exchangeRate = AppConfigHelper.getConfig(AppConfigDef.exchangeRate);
                if (TextUtils.isEmpty(exchangeRate)) {
                    exchangeRate = "1";
                }
                String cnyAmount = Calculater.formotFen(AppConfigHelper.getConfig(AppConfigDef.CNY_AMOUNT)).trim();
                if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
                    cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(totalAmount, exchangeRate)));
                }
                lines.add(new HTMLPrintModel.LeftAndRightLine("", "CNY " + cnyAmount));
                String showCNY = "CAD 1.00=CNY " + Calculater.multiply("1", exchangeRate);
                String printFx = context.getString(R.string.print_fx_rate);
                lines.add(new HTMLPrintModel.LeftAndRightLine(printFx, showCNY));
                lines.add(new HTMLPrintModel.EmptyLine());
                String tranlogId = Tools.deleteMidTranLog(transactionInfo.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
                String printRecepit = context.getString(R.string.print_receipt);
                lines.add(new HTMLPrintModel.LeftAndRightLine(printRecepit + "#", tranlogId));
                lines.add(new HTMLPrintModel.LeftAndRightLine(transactionInfo.getPayTime().substring(0, 10), transactionInfo.getPayTime().substring(10)));
                if (Constants.ALIPAYFLAG.equals(transactionInfo.getPayType())) {
                    lines.add(new HTMLPrintModel.LeftAndRightLine(context.getString(R.string.print_type), "Alipay"));
                } else if (Constants.WEPAYFLAG.equals(transactionInfo.getPayType())) {
                    lines.add(new HTMLPrintModel.LeftAndRightLine(context.getString(R.string.print_type), "Wechat Pay"));
                }

                String thirdTransOrder = transactionInfo.getThirdTradeNo();
                if (!TextUtils.isEmpty(thirdTransOrder)) {
                    lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_trans)));
                    lines.add(new HTMLPrintModel.LeftAndRightLine("", thirdTransOrder));
                }
                String acctName = transactionInfo.getThirdExtName();
                if (!TextUtils.isEmpty(acctName)) {
                    String printAcctName = context.getString(R.string.print_acctName);
                    lines.add(new HTMLPrintModel.LeftAndRightLine(printAcctName, acctName));
                }
                String acct = transactionInfo.getThirdExtId();
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
            String terminalId = AppConfigHelper.getConfig(AppConfigDef.sn);
            printString += context.getString(R.string.print_terminal_id) + terminalId + builder.br();
            String cahierId = AppConfigHelper.getConfig(AppConfigDef.operatorTrueName);
            printString += context.getString(R.string.print_cashier_id) + cahierId + builder.br();
            printString += builder.br();
            printString += builder.center(builder.bold(context.getString(R.string.print_sale))) + builder.br();
            String totalAmount = Calculater.formotFen(transactionInfo.getRealAmount());
            String tipsAmount = transactionInfo.getTips();
            String printPurchase = context.getString(R.string.print_purchase);
            if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
                String purchaseAmount = Calculater.formotFen(Calculater.subtract(transactionInfo.getRealAmount(), tipsAmount));
                printString += printPurchase + multipleSpaces(31 - printPurchase.getBytes("GBK").length - purchaseAmount.length()) + "$" + purchaseAmount + builder.br();
            } else {
                printString += printPurchase + multipleSpaces(31 - printPurchase.getBytes("GBK").length - totalAmount.length()) + "$" + totalAmount + builder.br();
            }
            String printTip = context.getString(R.string.print_tip);
            if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
                printString += printTip + multipleSpaces(31 - printTip.getBytes("GBK").length - Calculater.formotFen(transactionInfo.getRealAmount()).length()) + "$" + Calculater.formotFen(tipsAmount) + builder.br();
            }
            printString += "Total:" + multipleSpaces(25 - totalAmount.length()) + "$" + totalAmount + builder.br();
            String exchangeRate = AppConfigHelper.getConfig(AppConfigDef.exchangeRate);
            if (TextUtils.isEmpty(exchangeRate)) {
                exchangeRate = "1";
            }
            String cnyAmount = Calculater.formotFen(AppConfigHelper.getConfig(AppConfigDef.CNY_AMOUNT)).trim();
            if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
                cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(totalAmount, exchangeRate)));
            }
            printString += multipleSpaces(28 - cnyAmount.length()) + "CNY " + cnyAmount + builder.br();
            String showCNY = "CAD 1.00=CNY " + Calculater.multiply("1", exchangeRate);
            String printFx = context.getString(R.string.print_fx_rate);
            printString += printFx + multipleSpaces(32 - printFx.getBytes("GBK").length - showCNY.length()) + showCNY + builder.br();
            printString += builder.br();
            String tranlogId = Tools.deleteMidTranLog(transactionInfo.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
            String printRecepit = context.getString(R.string.print_receipt);
            printString += printRecepit + "#" + multipleSpaces(31 - printRecepit.getBytes("GBK").length - tranlogId.getBytes("GBK").length) + tranlogId + builder.br();
            printString += transactionInfo.getPayTime().substring(0, 10) + multipleSpaces(22 - transactionInfo.getPayTime().substring(10).length()) + transactionInfo.getPayTime().substring(10) + builder.br();
            String printType = context.getString(R.string.print_type);
            if (Constants.ALIPAYFLAG.equals(transactionInfo.getPayType())) {
                printString += printType + multipleSpaces(26 - printType.getBytes("GBK").length) + "Alipay" + builder.br();
            } else if (Constants.WEPAYFLAG.equals(transactionInfo.getPayType())) {
                printString += printType + multipleSpaces(22 - printType.getBytes("GBK").length) + "Wechat Pay" + builder.br();
            }else if(Constants.UNION.equals(transactionInfo.getPayType())){
                printString += printType + multipleSpaces(20 - printType.getBytes("GBK").length) + "Union Pay QR" + builder.br();
            }
            String thirdTransOrder = transactionInfo.getThirdTradeNo();
            if (!TextUtils.isEmpty(thirdTransOrder)) {
                printString += context.getString(R.string.print_trans) + builder.br();
                printString += multipleSpaces(32 - thirdTransOrder.getBytes("GBK").length) + thirdTransOrder + builder.br();
            }
            String acctName = transactionInfo.getThirdExtName();
            if (!TextUtils.isEmpty(acctName)) {
                String printAcctName = context.getString(R.string.print_acctName);
                printString += printAcctName + multipleSpaces(32 - printAcctName.getBytes("GBK").length - acctName.getBytes("GBK").length) + acctName + builder.br();
            }
            String acct = transactionInfo.getThirdExtId();
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

    @Override
    public void onDestory() {
        if (queryer != null) {
            queryer.onDestory();
        }
        this.printListener = null;
        this.resultListener = null;
        this.response = null;
    }

    /**
     * 轮询
     *
     * @param resultListener 轮询回调
     */
    protected void looperQuery(final ResultListener resultListener) {
        queryer = new LooperQueryerTransactionState(transactionInfo.getTranId());
        queryer.setListener(new LooperTask.LooperFinishListener() {
            @Override
            public void onFinish(Object object) {
                Logger.debug("轮询到结果");
                try {
                    if (object == null) {
                        resultListener.onFaild(new Response(1, context.getResources().getString(R.string.data_parse_error)));
                        return;
                    }
                    Response response = (Response) object;
                    if (response.code == 0) {
                        OrderDef orderDef = (OrderDef) response.getResult();
                        transactionInfo.setThirdTradeNo(orderDef.getThirdTradeNo());
                        String ticketInfo = orderDef.getTicketInfo();
                        if (ticketInfo != null) {
                            JSONArray ticketArray = JSONArray
                                    .parseArray(ticketInfo);
                            List<TicketInfo> list = new ArrayList<TicketInfo>();
                            for (int i = 0; i < ticketArray.size(); i++) {
                                JSONObject jsonObject = (JSONObject) ticketArray
                                        .get(i);
                                TicketInfo info = JSONObject.parseObject(
                                        jsonObject.toString(), TicketInfo.class);
                                TicketDef def = JSONObject.parseObject(
                                        jsonObject.toString(), TicketDef.class);
                                info.setTicketDef(def);
                                list.add(info);
                            }
                            if (OrderDef.STATE_PAYED == (orderDef.getState())) {
                                transactionInfo.settList(list);
                            }
                        }
                        if (OrderDef.STATE_PAYED == (orderDef.getState())) {
                            transactionInfo.setPayType(orderDef.getPayType());//移动支付对应的支付类型 wu
                            transactionInfo.setPayTime(orderDef.getPayTime());
                            if (!TextUtils.isEmpty(orderDef.getThirdExtId())) {
                                transactionInfo.setThirdExtId(orderDef.getThirdExtId());
                            }
                            if (!TextUtils.isEmpty(orderDef.getThirdExtName())) {
                                transactionInfo.setThirdExtName(orderDef.getThirdExtName());
                            }
                            AppConfigHelper.setConfig(AppConfigDef.PRINT_CONTEXT, getPrintContext());
                            AppConfigHelper.setConfig(AppConfigDef.PRINT_CUSTOMER_CONTEXT, getCustomerPrintContext());
                            resultListener.onSuccess(new Response(0, "支付成功", bundleResult()));
//                            printTransInfoWithListener(result, resultListener);
                        } else {
                            Response result = new Response(1, OrderDef.getOrderStateDes(orderDef.getState()), orderDef);
                            resultListener.onFaild(result);
                        }
                    } else {
                        resultListener.onFaild(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resultListener.onFaild(new Response(1, context.getResources().getString(R.string.data_parse_error)));
                }
            }
        });
        queryer.start();
    }

    public void getTicketInfo(String ticketNum, final ResultListener listener) {
        commonTicketManager.getTicketInfo(ticketNum, transactionInfo.getInitAmount(), new ResultListener() {

            @Override
            public void onSuccess(Response response) {
                GetCommonTicketInfoResp commonTicketInfoResp = (GetCommonTicketInfoResp) response.result;
                if (commonTicketInfoResp.getWemengTicket() != null) {
                    //TODO 微盟券
                }
                listener.onSuccess(new Response(0, "获取券信息成功", commonTicketInfoResp.getTicketInfo()));
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);
            }
        }, "Y", "1", transactionInfo.getRealAmount());
    }

    /**
     * 添加券
     */
    public Response addTicket(TicketInfo ticket, boolean isFromScan) {
        Response response = new Response();
        try {
            int blance = ticket.getTicketDef().getBalance();
            LogEx.d("TICKET", blance + "");
            if (commonTicketManager.isAddedTicket(ticket.getId())) {
                return new Response(1, "该券已添加,不能重复添加");
            }
            Response addResult = commonTicketManager.addTicket(ticket, isFromScan);
            if (addResult.code == 0) {
                /* 更新实收金额和扣减金额 */
                if (Calculater.compare(blance + "", transactionInfo.getRealAmount()) > 0) {//更新扣减金额 wu
                    //TODO 第三方支付至少支付1分
                    transactionInfo.setReduceAmount(Calculater.subtract(transactionInfo.getRealAmount(), "1"));
                    transactionInfo.setRealAmount("1");
                } else {
                    transactionInfo.setReduceAmount(Calculater.plus(transactionInfo.getReduceAmount(), blance + ""));
                    transactionInfo.setRealAmount(Calculater.subtract(transactionInfo.getRealAmount(), blance + ""));
                    transactionInfo.setTicketTotalAomount(Calculater.plus(transactionInfo.getTicketTotalAomount(), blance + ""));
                }
                BatWeimobTicket weiMobTicket = new BatWeimobTicket();
                weiMobTicket.setAmount(Integer.valueOf(transactionInfo.getInitAmount()));
                weiMobTicket.setMid(AppConfigHelper.getConfig(AppConfigDef.mid));
                weiMobTicket.setSn(AppConfigHelper.getConfig(AppConfigDef.sn));
                weiMobTicket.setConfirmCardCode(ticket.getTicketNo());
                transactionInfo.setBatTicket(weiMobTicket);

                usedTicketlist.add(ticket);
            }
            return addResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setCode(-1);
        response.setMsg("添加券失败");
        return response;
    }

    public List<TicketInfo> getUsedTicketlist() {
        return usedTicketlist;
    }

    public void setUsedTicketlist(List<TicketInfo> usedTicketlist) {
        this.usedTicketlist = usedTicketlist;
    }

    public void continuePrint() {
//        if (transactionInfo.isNeedPrint()) {
//            return;
//        }
//        int printNumber = 1;
//        if (!TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.print_number))) {
//            printNumber = Integer.parseInt(AppConfigHelper.getConfig(AppConfigDef.print_number));
//        }
//        if (printNumber > 0) {
//            PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
//            controller.print(getCustomerPrintContext());
//            currentSize++;
//            if (currentSize >= printNumber) {
//                this.resultListener.onSuccess(response);
//                return;
//            }
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    handler.sendEmptyMessage(PRINT);
//                }
//            }, 2000L);
//        } else {
//            this.resultListener.onSuccess(response);
//        }
    }

    public void cancelPrint() {
        this.resultListener.onSuccess(response);
    }

    public interface PrintListener {

        void onPrint(int total, int current);
    }

}
