package com.wizarpos.recode.print.result.payfor.printcontext;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.device.printer.html.ToHTMLUtil;
import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.device.printer.html.model.HtmlLine;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.print.content.CashierIdContent;
import com.wizarpos.recode.print.content.DeviceContent;
import com.wizarpos.recode.print.content.InvoiceContent;
import com.wizarpos.recode.print.content.PurchaseContent;
import com.wizarpos.recode.print.content.SettlementContent;
import com.wizarpos.recode.print.content.TipsContent;
import com.wizarpos.recode.print.content.TotalContent;
import com.wizarpos.recode.print.content.barcode.BarcodeTextContent;
import com.wizarpos.recode.receipt.service.ReceiptDataManager;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PayforHtmlPrintContext {

    public static List<String> getPrintContext(Context context, TransactionInfo transactionInfo) {
        List<String> mList = new ArrayList<>();

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

            lines.add(DeviceContent.printHtmlDevice(context));


            lines.add(CashierIdContent.printHtml(context));


            lines.add(new HTMLPrintModel.EmptyLine());
            lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_sale), true, true));
            String totalAmount = Calculater.formotFen(transactionInfo.getRealAmount());
            String tipsAmount = transactionInfo.getTips();


            HTMLPrintModel.LeftAndRightLine purchasePrint = PurchaseContent.printHtmlPurchasePayFor(context, tipsAmount, totalAmount);
            lines.add(purchasePrint);


            if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
                lines.add(TipsContent.printHtmlPayFor(context, tipsAmount));
            }

            lines.add(TotalContent.printHtmlPayFor(context, totalAmount));
            String exchangeRate = AppConfigHelper.getConfig(AppConfigDef.exchangeRate);
            if (TextUtils.isEmpty(exchangeRate)) {
                exchangeRate = "1";
            }

            // 打印

            lines.add(SettlementContent.printHtmlSettlementPayFor(transactionInfo));

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
            //invoice
            InvoiceContent.printHtmlPayfor(context, lines, transactionInfo);

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

            if (ReceiptDataManager.isOpenStatus()) {
                BarcodeTextContent.printHtmlPayfor(lines, transactionInfo);
            }
            model.setLineList(lines);
            mList.add(ToHTMLUtil.toHtml(model));
            //第2部分
            if (ReceiptDataManager.isOpenStatus()) {

            }
            //第3部分
            model = new HTMLPrintModel();
            lines = new ArrayList<>();
            lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_approved), true, true));
            lines.add(new HTMLPrintModel.EmptyLine());
            lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_merchant_copy), true, true));
            lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_important), true, true));
            lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_hint1), true, true));
            lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_hint2), true, true));
            lines.add(new HTMLPrintModel.BranchLine());
            model.setLineList(lines);
            mList.add(ToHTMLUtil.toHtml(model));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return mList;

    }
}
