package com.wizarpos.recode.print.content;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.device.printer.html.model.HtmlLine;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.print.base.PrintBase;
import com.wizarpos.recode.sale.service.InvoiceLoginServiceImpl;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class InvoiceContent extends PrintBase {


    public static void printHtmlPayfor(Context context, List<HtmlLine> lines, TransactionInfo transactionInfo) {
        String inVoiceValue = transactionInfo.getMerchantTradeCode();
        printHtmlBase(context, lines, inVoiceValue);
    }

    public static String[] printStringPayfor(Context context, TransactionInfo transactionInfo) throws UnsupportedEncodingException {
        return printStringBase(context, transactionInfo.getMerchantTradeCode());
    }

    public static void printHtmlRefund(Context context, List<HtmlLine> lines, RefundDetailResp refundDetailResp) {
        String inVoiceValue = refundDetailResp.getMerchantTradeCode();
        printHtmlBase(context, lines, inVoiceValue);
    }

    public static String[] printStringRefund(Context context, RefundDetailResp refundDetailResp) throws UnsupportedEncodingException {
        return printStringBase(context, refundDetailResp.getMerchantTradeCode());
    }

    public static void printHtmlActivity(Context context, List<HtmlLine> lines, DailyDetailResp dailyDetailResp) {
        String inVoiceValue = dailyDetailResp.getMerchantTradeCode();
        printHtmlBase(context, lines, inVoiceValue);
    }

    public static String[] printStringActivity(Context context, DailyDetailResp dailyDetailResp) throws UnsupportedEncodingException {
        return printStringBase(context, dailyDetailResp.getMerchantTradeCode());
    }

    private static void printHtmlBase(Context context, List<HtmlLine> lines, String inVoiceValue) {
        if (TextUtils.isEmpty(inVoiceValue)) {
            return;
        }
        String title = getTitle(context);
        if (inVoiceValue.length() < PART_NUM_13) {
            lines.add(new HTMLPrintModel.LeftAndRightLine(title, inVoiceValue));
        } else {
            lines.add(new HTMLPrintModel.SimpleLine(title));
            lines.add(new HTMLPrintModel.LeftAndRightLine("", inVoiceValue));
        }
    }


    private static String[] printStringBase(Context context, String inVoiceValue) throws UnsupportedEncodingException {
        if (TextUtils.isEmpty(inVoiceValue)) {
            return null;
        }
        String[] aray;
        String title = getTitle(context);
        if (isComputerSpaceForLeftRight()) {
            aray = new String[1];
            aray[0] = createTextLineForLeftAndRight(title, inVoiceValue);
            return aray;
        } else {
            if (inVoiceValue.length() < PART_NUM_13) {
                aray = new String[1];
                aray[0] = title + multipleSpaces(getInvoiceSpaceCount(inVoiceValue) - title.getBytes("GBK").length - inVoiceValue.getBytes("GBK").length) + inVoiceValue;
            } else {
                aray = new String[2];
                aray[0] = title;
                aray[1] = multipleSpaces(getInvoiceSpaceCountArray(inVoiceValue) - inVoiceValue.getBytes("GBK").length) + inVoiceValue;
            }
        }


        return aray;
    }


    private static String getTitle(Context context) {
        String titleInvoice = context.getString(R.string.print_invoice);
        return titleInvoice;
    }


    private static int getInvoiceSpaceCount(String content) {
        if (getDeviceTypeForN3N5()) {

            int length = content.length();
            int countTr = 0;
            switch (length) {
                case 1:
                    countTr = 0;
                    break;
                case 2:
                    countTr = -2;
                    break;
                case 3:
                    countTr = -6;
                    break;
                case 4:
                    countTr = -9;
                    break;
                case 5:
                    countTr = -11;
                    break;
                case 6:
                    countTr = -14;
                    break;
                case 7:
                    countTr = -17;
                    break;
                case 8:
                    countTr = -19;
                    break;
                case 9:
                    countTr = -22;
                    break;
                case 10:
                    countTr = -25;
                    break;
                case 11:
                    countTr = -27;
                    break;
                case 12:
                    countTr = -30;
                    break;
            }

            return 54 + countTr;
        } else {
            return 32;
        }
    }

    private static int getInvoiceSpaceCountArray(String content) {
        if (getDeviceTypeForN3N5()) {

            int length = content.length();
            int countTr = 0;
            switch (length) {
                case 13:
                    countTr = 0;
                    break;
                case 14:
                    countTr = -2;
                    break;
                case 15:
                    countTr = -4;
                    break;
                case 16:
                    countTr = -5;
                    break;
                case 17:
                    countTr = -7;
                    break;
                case 18:
                    countTr = -10;
                    break;
                case 19:
                    countTr = -12;
                    break;
                case 20:
                    countTr = -14;
                    break;
                case 21:
                    countTr = -16;
                    break;
                case 22:
                    countTr = -19;
                    break;
                case 23:
                    countTr = -22;
                    break;
                case 24:
                    countTr = -25;
                    break;
                case 25:
                    countTr = -25;
                    break;
            }

            return 28 + countTr;
        } else {
            return 32;
        }
    }

}
