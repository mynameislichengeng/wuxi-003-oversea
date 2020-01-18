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
        if (inVoiceValue.length() < PART_LENGTH) {
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
        if (inVoiceValue.length() < PART_LENGTH) {
            aray = new String[1];
            aray[0] = title + multipleSpaces(getInvoiceSpaceCount() - title.getBytes("GBK").length - inVoiceValue.getBytes("GBK").length) + inVoiceValue;
        } else {
            aray = new String[2];
            aray[0] = title;
            aray[1] = multipleSpaces(getInvoiceSpaceCount() - inVoiceValue.getBytes("GBK").length) + inVoiceValue;
        }
        return aray;
    }


    private static String getTitle(Context context) {
        String titleInvoice = context.getString(R.string.print_invoice);
        return titleInvoice;
    }

    private static int getInvoiceSpaceCount() {
        if (getDeviceTypeForN3N5()) {
            return 32 + 10 + 10;
        } else {
            return 32;
        }
    }
}
