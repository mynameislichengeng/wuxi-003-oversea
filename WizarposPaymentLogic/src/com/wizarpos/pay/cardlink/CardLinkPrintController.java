package com.wizarpos.pay.cardlink;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.wizarpos.hspos.api.SettleInfo;
import com.wizarpos.hspos.api.TransInfo;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wu on 16/3/23.
 */
class CardLinkPrintController {

    final byte MANUAL_ENTRY = 0x01; // 手工输入卡号
    final byte SWIPE_ENTRY = 0x02; // 读取磁条卡号
    final byte INSERT_ENTRY = 0x05; // 读取IC卡
    final byte QPBOC_ENTRY = 0x07; // 快速 PBOC借/贷记IC卡读入（非接触式）

    final byte SCAN_ENTRY = 0x10; // POS扫描手机条码
    final byte QRCODE_ENTRY = 0x21; // POS生成二维码， 让手机扫描

    final byte UPCARD_ENTRY = (byte) 0x96; // 采用非接触方式读取CUPMobile移动支付中的集成在手机中的芯片卡（现场支付）
    final byte CONTACTLESS_ENTRY = (byte) 0x98; // 标准PBOC借/贷记IC卡读入（非接触式）

    private Context context;

    public CardLinkPrintController(Context context) {
        this.context = context;
    }

    public void printSale(TransInfo transInfo, String termCap) {
        try {
            printSale(transInfo, "商户存根", "MERCHANT COPY", termCap, 1);
            printSale(transInfo, "持卡人存根", "CARDHOLDER COPY", termCap, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printSale(TransInfo transInfo, String name, String nameTip, String termCap, int index) {
        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
        String printString = BundlePrintData(transInfo, name, nameTip, termCap, index, false);
        if (index == 1) {
            printString += new Q1PrintBuilder().endPrint();
            controller.print(printString);
        } else {
            controller.print(printString, true);
        }
    }

    public void printAny(TransInfo transInfo, String termCap) {
        try {
            printAny(transInfo, "商户存根", "MERCHANT COPY", termCap, 1);
            printAny(transInfo, "持卡人存根", "CARDHOLDER COPY", termCap, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printAny(TransInfo transInfo, String name, String nameTip, String termCap, int index) {
        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
        String printString = BundlePrintData(transInfo, name, nameTip, termCap, index, true);
        if (index == 1) {
            controller.print(printString);
        } else {
            controller.print(printString, true);
        }
    }

    @NonNull
    private String BundlePrintData(TransInfo transInfo, String name, String nameTip, String termCap, int index, boolean isPrintAny) {
        Q1PrintBuilder pb = new Q1PrintBuilder();
        String printString = pb.br() + pb.high(pb.center(pb.normal("银联POS签购单")));
        String line = "--------------------------------";
        printString += "" + pb.center(line);
//        if(type == 1){
//            printStringPayFor += "商户存根           MERCHANT COPY";
//        }else{
//            printStringPayFor += "持卡人存根       CARDHOLDER COPY";
//        }
//        printStringPayFor += pb.br();
//        printStringPayFor += "" + pb.center(line);
        printString += transInfo.getMerchantName() == null ? "" : transInfo.getMerchantName();
        printString += pb.br();
        int paperWidth = 32;
        printString += name + fillString(nameTip, paperWidth - 10, ' ', true) + pb.br();
        printString += line + pb.br();
        printString += "商户号:" + (transInfo.getMid() == null ? "" : transInfo.getMid()) + pb.br();
        printString += "终端号:" + (transInfo.getTid() == null ? "" : transInfo.getTid()) + pb.br();
//        printStringPayFor += "操作员:" + (AppConfigHelper.getConfig(AppConfigDef.operatorNo)) + pb.br();
        printString += "操作员:" + ("01") + pb.br(); //写死为01, 原先使用收款操作员号有问题 wu@[20160811]
        printString += "发卡行:" + (TextUtils.isEmpty(transInfo.getIssuerName()) ? transInfo.getIssuerCode() : transInfo.getIssuerName()) + pb.br();
        printString += "收单行:" + (TextUtils.isEmpty(transInfo.getAcquirerName()) ? transInfo.getAcquirerCode() : transInfo.getAcquirerName()) + pb.br();
        printString += pb.high("卡号(CARD NO):") + pb.br();
        String cardNum = transInfo.getPan();
        String cardFirst = cardNum.substring(0, 6);
        String cardLast = cardNum.substring(cardNum.length() - 4);
        String ps = "******";
        printString += pb.high(cardFirst + ps + cardLast);

        try {
            switch (transInfo.getEntryMode()) {
                case SWIPE_ENTRY:
                    printString += " S";
                    break;
                case INSERT_ENTRY:
                    printString += " I";
                    break;
                case MANUAL_ENTRY:
                    printString += " M";
                    break;
                case UPCARD_ENTRY:
                case CONTACTLESS_ENTRY:
                case QPBOC_ENTRY:
                    printString += " C";
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        printString += pb.br();
        printString += pb.high("交易类型(TRANS TYPE):") + pb.br();
        printString += pb.high("消费 / SALE") + pb.br();

        if (transInfo.getBatchNumber() != -1) {
            printString += "批次号:" + addZero(String.valueOf(transInfo.getBatchNumber()), 6);
        } else {
            printString += "批次号:  ";
        }

        String expiry = null;
        if (transInfo.getExpiryDate() != null) {
            expiry = transInfo.getExpiryDate();
        }
        String year = null;
        String month = null;
        if (!TextUtils.isEmpty(expiry) && expiry.length() >= 4) {
            year = expiry.substring(0, 2);
            month = expiry.substring(2);
        }

        if (year != null) {
            printString += " 有效期：" + year + "/" + month + pb.br();
        } else {
            printString += " 有效期：" + pb.br();
        }

        printString += "凭证号:" + (transInfo.getTrace() == -1 ? "" : addZero(String.valueOf(transInfo.getTrace()), 6));
        printString += " 授权码:" + (transInfo.getAuthCode() == null ? "" : transInfo.getAuthCode()) + pb.br();
        printString += "参考号:" + (transInfo.getRrn() == null ? "" : transInfo.getRrn()) + pb.br();

        String dateStr = transInfo.getTransDate() == null ? "" : transInfo.getTransDate();
        String dateInfo = null;
        if (!TextUtils.isEmpty(dateStr) && dateStr.length() >= 4) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String yearStr = sdf.format(new Date(System.currentTimeMillis()));
            dateInfo = yearStr + "/" + dateStr.substring(0, 2) + "/" + dateStr.substring(2);
        } else {
            dateInfo = "";
        }

        printString += "日期/时间: " + dateInfo;
        String timeInfo = transInfo.getTransTime();
        if (!TextUtils.isEmpty(timeInfo) && timeInfo.length() >= 6) {
            printString += " " + timeInfo.substring(0, 2) + ":" + timeInfo.substring(2, 4) + ":" + timeInfo.substring(4) + pb.br();
        } else {
            printString += " " + pb.br();
        }
        double amountD = 0;
        try {
            amountD = Double.parseDouble(String.valueOf(transInfo.getTransAmount())) / 100;
        } catch (Exception e) {
            e.printStackTrace();
        }

        printString += pb.high("金额:RMB " + String.format("%.2f", amountD)) + pb.br();
        printString += "备注/REFERENCE" + pb.br();
        String smallLineSpanStr = "";
        if (transInfo.getCsn() != -1) {
            smallLineSpanStr += pb.small("卡片序列号:" + addZero(transInfo.getCsn() + "", 3)) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getUnpredictableNumber())) {
            smallLineSpanStr += pb.small("UNPR NUM:" + transInfo.getUnpredictableNumber()) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getArqc())) {
            smallLineSpanStr += pb.small("ARQC:" + transInfo.getArqc()) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getTvr())) {
            smallLineSpanStr += pb.small("TVR:" + transInfo.getTvr()) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getAid())) {
            smallLineSpanStr += pb.small("AID:" + transInfo.getAid()) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getTsi())) {
            smallLineSpanStr += pb.small("TSI:" + transInfo.getTsi()) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getAtc())) {
            smallLineSpanStr += pb.small("ATC:" + transInfo.getAtc()) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getAppLabel())) {
            smallLineSpanStr += pb.small("应用标签:" + transInfo.getAppLabel()) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getAppName())) {
            smallLineSpanStr += pb.small("应用首选名:" + transInfo.getAppName()) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getAip())) {
            smallLineSpanStr += pb.small("AIP:" + transInfo.getAip()) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getIad())) {
            smallLineSpanStr += pb.small("IAD:" + transInfo.getIad()) + pb.br();
        }
        if (transInfo.getEntryMode() == MANUAL_ENTRY
                || transInfo.getEntryMode() == INSERT_ENTRY
                || transInfo.getEntryMode() == QPBOC_ENTRY) {
            if (!TextUtils.isEmpty(termCap)) {
                smallLineSpanStr += pb.small("TermCap:" + termCap) + pb.br();
            }
        }
        printString += pb.smallLineSpan(smallLineSpanStr);
        if (isPrintAny) { //重打印
            printString += "重打印凭证/DUPLICATED" + pb.br();
        }
        printString += line + pb.br();
        if (index == 1) {
            printString += "持卡人签名(CARDHOLDER SIGNATURE)" + pb.br();
            printString += pb.br();
            printString += pb.br();
            printString += pb.br();
            printString += line + pb.br();
        }
        printString += "本人确认以 上交易，同意将其记入本卡账户" + pb.br();
        printString += "I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES" + pb.br();
        if (isPrintAny) {
            printString += pb.endPrint();
        }
        return printString;
    }

    protected void printVoid(TransInfo transInfo, int type) {
        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
        Q1PrintBuilder pb = new Q1PrintBuilder();
        String printString = pb.br() + pb.high(pb.center(pb.normal("银联POS签购单")));
        String line = "--------------------------------";
        printString += "" + pb.center(line);
//        printStringPayFor += transInfo.getMerchantName() == null ? "" : transInfo.getMerchantName();
        if (type == 1) {
            printString += "商户存根           MERCHANT COPY";
        } else {
            printString += "持卡人存根       CARDHOLDER COPY";
        }
        printString += pb.br();
//        printStringPayFor += "" + pb.center(line);
//        printStringPayFor += pb.br();
//        int paperWidth = 32;
//        printStringPayFor += name + fillString(nameTip, paperWidth - 10, ' ', true) + pb.br();
        printString += line + pb.br();
        printString += "商户号:" + (transInfo.getMid() == null ? "" : transInfo.getMid()) + pb.br();
        printString += "终端号:" + (transInfo.getTid() == null ? "" : transInfo.getTid()) + pb.br();
        printString += "操作员:" + (AppConfigHelper.getConfig(AppConfigDef.operatorNo)) + pb.br();
        printString += "发卡行:" + (TextUtils.isEmpty(transInfo.getIssuerName()) ? transInfo.getIssuerCode() : transInfo.getIssuerName()) + pb.br();
        printString += "收单行:" + (TextUtils.isEmpty(transInfo.getAcquirerName()) ? transInfo.getAcquirerCode() : transInfo.getAcquirerName()) + pb.br();
        printString += pb.high("卡号(CARD NO):") + pb.br();
        String cardNum = transInfo.getPan();
        String cardFirst = cardNum.substring(0, 6);
        String cardLast = cardNum.substring(cardNum.length() - 4);
        String ps = "******";
        printString += pb.high(cardFirst + ps + cardLast);

        try {
            switch (transInfo.getEntryMode()) {
                case SWIPE_ENTRY:
                    printString += " S";
                    break;
                case INSERT_ENTRY:
                    printString += " I";
                    break;
                case MANUAL_ENTRY:
                    printString += " M";
                    break;
                case UPCARD_ENTRY:
                case CONTACTLESS_ENTRY:
                case QPBOC_ENTRY:
                    printString += " C";
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        printString += pb.br();
        printString += pb.high("交易类型(TRANS TYPE):") + pb.br();
        printString += pb.high("消费撤销 / VOID") + pb.br();

        if (transInfo.getBatchNumber() != -1) {
            printString += "批次号:" + addZero(String.valueOf(transInfo.getBatchNumber()), 6);
        } else {
            printString += "批次号:  ";
        }

        String expiry = null;
        if (transInfo.getExpiryDate() != null) {
            expiry = transInfo.getExpiryDate();
        }
        String year = null;
        String month = null;
        if (!TextUtils.isEmpty(expiry) && expiry.length() >= 4) {
            year = expiry.substring(0, 2);
            month = expiry.substring(2);
        }

        if (year != null) {
            printString += " 有效期：" + year + "/" + month + pb.br();
        } else {
            printString += " 有效期：" + pb.br();
        }

        printString += "凭证号:" + addZero(String.valueOf((transInfo.getTrace() == -1 ? "" : transInfo.getTrace())), 6);
        printString += " 授权码:" + (transInfo.getAuthCode() == null ? "" : transInfo.getAuthCode()) + pb.br();
        printString += "参考号:" + (transInfo.getRrn() == null ? "" : transInfo.getRrn()) + pb.br();

        String dateStr = transInfo.getTransDate() == null ? "" : transInfo.getTransDate();
        String dateInfo = null;
        if (!TextUtils.isEmpty(dateStr) && dateStr.length() >= 4) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String yearStr = sdf.format(new Date(System.currentTimeMillis()));
            dateInfo = yearStr + "/" + dateStr.substring(0, 2) + "/" + dateStr.substring(2);
        } else {
            dateInfo = "";
        }

        printString += "日期/时间: " + dateInfo;
        String timeInfo = transInfo.getTransTime();
        if (!TextUtils.isEmpty(timeInfo) && timeInfo.length() >= 6) {
            printString += " " + timeInfo.substring(0, 2) + ":" + timeInfo.substring(2, 4) + ":" + timeInfo.substring(4)
                    + pb.br();
        } else {
            printString += " " + pb.br();
        }
        double amountD = 0;
        try {
            amountD = Double.parseDouble(String.valueOf(transInfo.getTransAmount())) / 100;
        } catch (Exception e) {
            e.printStackTrace();
        }

        printString += pb.high("金额:RMB " + String.format("%.2f", amountD) + pb.br());
        printString += "备注/REFERENCE" + pb.br();
        printString += "原凭证号:" + addZero(String.valueOf(transInfo.getOldTrace()), 6);
        printString += pb.br();

        String smallLineSpanStr = "";
        if (transInfo.getCsn() != -1) {
            smallLineSpanStr += pb.small("卡片序列号:" + addZero(transInfo.getCsn() + "", 3)) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getUnpredictableNumber())) {
            smallLineSpanStr += pb.small("UNPR NUM:" + transInfo.getUnpredictableNumber()) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getArqc())) {
            smallLineSpanStr += pb.small("ARQC:" + transInfo.getArqc()) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getTvr())) {
            smallLineSpanStr += pb.small("TVR:" + transInfo.getTvr()) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getAid())) {
            smallLineSpanStr += pb.small("AID:" + transInfo.getAid()) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getTsi())) {
            smallLineSpanStr += pb.small("TSI:" + transInfo.getTsi()) + pb.br();
        }

//        if (!TextUtils.isEmpty(transInfo.getAtc())) {
//            printStringPayFor += pb.small("ATC:" + transInfo.getAtc()) + pb.br();
//        }

        if (!TextUtils.isEmpty(transInfo.getAppLabel())) {
            smallLineSpanStr += pb.small("应用标签:" + transInfo.getAppLabel()) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getAppName())) {
            smallLineSpanStr += pb.small("应用首选名:" + transInfo.getAppName()) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getAip())) {
            smallLineSpanStr += pb.small("AIP:" + transInfo.getAip()) + pb.br();
        }

        if (!TextUtils.isEmpty(transInfo.getIad())) {
            smallLineSpanStr += pb.small("IAD:" + transInfo.getIad()) + pb.br();
        }

//        if (transInfo.getT != null) {
//            printStringPayFor += "TermCap:" + json.get("TermCap").toString() + pb.br();
//        }

//        if (transInfo.get != null) {
//            printStringPayFor += "产品标示信息:" + json.get("ProductIdentity").toString() + pb.br();
//        }
        printString += pb.smallLineSpan(smallLineSpanStr);

        printString += line + pb.br();
        if (type == 1) {
            printString += "持卡人签名(CARDHOLDER SIGNATURE)" + pb.br();
            printString += pb.br();
            printString += pb.br();
            printString += pb.br();
            printString += line + pb.br();
        }
        printString += "本人确认以 上交易，同意将其记入本卡账户" + pb.br();
        printString += "I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES" + pb.br();
        if (type == 1) {
            printString += pb.br() + pb.br() + pb.br() + pb.br() + pb.br();
        } else {
            printString += pb.endPrint();
        }
        if (type == 1) {
            controller.print(printString);
        } else {
            controller.print(printString, true);
        }
    }

    public void printSettle(SettleInfo settleInfo) {
        Log.d("printSettle", "settleFlag is: " + settleInfo.getSettleFlag());
        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
        Q1PrintBuilder pb = new Q1PrintBuilder();
        String printString = pb.br() + pb.center(pb.normal("银联POS签购单"));
        String line = "--------------------------------";
        printString += "" + pb.center(line);
        printString += pb.center("结算总计单");
        printString += pb.br();
        printString += "" + pb.center(line);
        printString += "商户号:" + (settleInfo.getMid() == null ? "" : settleInfo.getMid()) + pb.br();
        printString += "终端号:" + (settleInfo.getTid() == null ? "" : settleInfo.getTid()) + pb.br();
        printString += "操作员:" + (AppConfigHelper.getConfig(AppConfigDef.operatorNo)) + pb.br();
        printString += "批次号:" + settleInfo.getBatchNumber() + pb.br();
        String dateTime = "";
        try {
            dateTime = settleInfo.getBatchDate().substring(0, 4) + settleInfo.getBatchDate().substring(4) + settleInfo.getBatchTime();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("printSettle", "getBatchDate error");
            if (TextUtils.isEmpty(settleInfo.getBatchDate())) {
                Log.d("printSettle", "getBatchDate is empty!");
            } else {
                Log.d("printSettle", "getBatchDate is " + settleInfo.getBatchDate());
            }
        }
        printString += "日期/时间:" + dateTime + pb.br();
        printString += "类型          笔数         金额" + pb.br();
        if ((settleInfo.getSettleFlag() & 0x10) != 0) {
            printString += "内卡对账平" + pb.br();
        } else if ((settleInfo.getSettleFlag() & 0x20) != 0) {
            printString += "内卡对账不平" + pb.br();
        } else if ((settleInfo.getSettleFlag() & 0x40) != 0) {
            printString += "内卡对帐出错" + pb.br();
        }

        printString += println_LabelCountAmount("消费交易", settleInfo.getCupSaleCount(), settleInfo.getCupSaleAmount());
//        printStringPayFor += println_LabelCountAmount("电子现金", settleInfo.getCupCashCount(), settleInfo.getCupCashAmount());
//        printStringPayFor += println_LabelCountAmount("完成请求", settleInfo.getCupAuthCompCount(), settleInfo.getCupAuthCompAmount());
//        printStringPayFor += println_LabelCountAmount("完成通知", settleInfo.getCupAuthSettleCount(), settleInfo.getCupAuthSettleAmount());
//        printStringPayFor += println_LabelCountAmount("离线交易", settleInfo.getCupOfflineCount(), settleInfo.getCupOfflineAmount());
        printString += println_LabelCountAmount("退货交易", settleInfo.getCupRefundCount(), settleInfo.getCupRefundAmount());
//        printStringPayFor += println_LabelCountAmount("圈存交易", settleInfo.getCupLoadCount(), settleInfo.getCupLoadAmount());
        if ((settleInfo.getSettleFlag() & 0x01) != 0) {
            printString += "外卡对账平" + pb.br();
        } else if ((settleInfo.getSettleFlag() & 0x02) != 0) {
            printString += "外卡对账不平" + pb.br();
        } else if ((settleInfo.getSettleFlag() & 0x04) != 0) {
            printString += "外卡对帐出错" + pb.br();
        }

        printString += println_LabelCountAmount("消费交易", settleInfo.getAbrSaleCount(), settleInfo.getAbrSaleAmount());
//        printStringPayFor += println_LabelCountAmount("电子现金 ", settleInfo.getAbrCashCount(), settleInfo.getAbrCashAmount());
//        printStringPayFor += println_LabelCountAmount("完成请求", settleInfo.getAbrAuthCompCount(), settleInfo.getAbrAuthCompAmount());
//        printStringPayFor += println_LabelCountAmount("完成通知", settleInfo.getAbrAuthSettleCount(), settleInfo.getAbrAuthSettleAmount());
//        printStringPayFor += println_LabelCountAmount("离线交易", settleInfo.getAbrOfflineCount(), settleInfo.getAbrOfflineAmount());
        printString += println_LabelCountAmount("退货交易", settleInfo.getAbrRefundCount(), settleInfo.getAbrRefundAmount());
//        printStringPayFor += println_LabelCountAmount("圈存交易", settleInfo.getAbrLoadCount(), settleInfo.getAbrLoadAmount());
        printString += pb.endPrint();
//        printStringPayFor += println_LabelCountAmount("微信消费", settleInfo.getWXSaleCount(),    settleInfo.getWXSaleAmount());
//        printStringPayFor += println_LabelCountAmount("微信退货", settleInfo.getWXRefundCount(),  settleInfo.getWXRefundAmount());
//        printStringPayFor += println_LabelCountAmount("支付宝消费",settleInfo.getAliSaleCount(),   settleInfo.getAliSaleAmount());
//        printStringPayFor += println_LabelCountAmount("支付宝退货",settleInfo.getAliRefundCount(), settleInfo.getAliRefundAmount());

//        if (appState.getTranType() == B_PRINT_SETTLE_REPORT) {
//            println("           重印结算单");
//        }

        controller.print(printString);
    }


    private String addZero(String raw, int maxLen) {
        int len = raw.length();
        for (int i = 0; i < maxLen - len; i++) {
            raw = "0" + raw;
        }
        return raw;
    }

    public static String fillString(String formatString, int length, char fillChar, boolean leftFillFlag) {
        if (formatString == null) {
            formatString = "";
        }

        int strLen = formatString.length();
        if (strLen >= length) {
            return leftFillFlag ? formatString.substring(strLen - length, strLen) : formatString.substring(0, length);
        } else {
            StringBuffer sbuf = new StringBuffer();
            int fillLen = length - formatString.length();

            for (int returnString = 0; returnString < fillLen; ++returnString) {
                sbuf.append(fillChar);
            }

            if (leftFillFlag) {
                sbuf.append(formatString);
            } else {
                sbuf.insert(0, formatString);
            }

            String var8 = sbuf.toString();
            sbuf = null;
            return var8;
        }
    }

    private String println_LabelCountAmount(String name, int cupSaleCount, long cupSaleAmount) {
        return name + "        " + cupSaleCount + "           " + cupSaleAmount + "<br/>";
    }

}
