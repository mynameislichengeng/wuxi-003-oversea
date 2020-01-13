package com.wizarpos.recode.print.result.payfor.printcontext;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.print.base.PrintBase;
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

public class PayforStringPrintContext extends PrintBase {

    public static List<String> getPrintContext(Context context, TransactionInfo transactionInfo) {
        List<String> mList = new ArrayList<>();
        String printString = null;
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


            printString += DeviceContent.printStringDevice(context) + builder.br();

            printString += CashierIdContent.printString(context) + builder.br();


            printString += builder.br() + builder.nBr();
            printString += builder.center(builder.bold(context.getString(R.string.print_sale))) + builder.br() + builder.nBr();


            String totalAmount = Calculater.formotFen(transactionInfo.getRealAmount());
            String tipsAmount = transactionInfo.getTips();

            printString += PurchaseContent.printStrPurchasePayFor(context, tipsAmount, totalAmount, transactionInfo) + builder.br();


            if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
                printString += TipsContent.printStringPayFor(context, tipsAmount, transactionInfo) + builder.br();
            }

            printString += TotalContent.printStringPayFor(context, totalAmount) + builder.br();

            String exchangeRate = AppConfigHelper.getConfig(AppConfigDef.exchangeRate);
            if (TextUtils.isEmpty(exchangeRate)) {
                exchangeRate = "1";
            }

            printString += SettlementContent.printStringSettlementPayFor(transactionInfo) + builder.br();

            String showCNY = "CAD 1.00=CNY " + Calculater.multiply("1", exchangeRate);
            String printFx = context.getString(R.string.print_fx_rate);
            printString += printFx + multipleSpaces(32 - printFx.getBytes("GBK").length - showCNY.length()) + showCNY + builder.br();
            printString += builder.br() + builder.nBr();


            //
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

            //
            String[] invoiceString = InvoiceContent.printStringPayfor(context, transactionInfo);
            if (invoiceString != null) {
                for (String str : invoiceString) {
                    printString += str + builder.br();
                }
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
            if (ReceiptDataManager.isOpenStatus()) {
                String barCodeText = BarcodeTextContent.printStringPayfor(transactionInfo);
                if (!TextUtils.isEmpty(barCodeText)) {
                    printString += builder.center(barCodeText) + builder.br();
                }
            }
            mList.add(printString);

            //第2部分
            printString = "";
            printString += builder.center(builder.bold(context.getString(R.string.print_approved)));
            printString += builder.br() + builder.nBr();
            printString += builder.center(builder.bold(context.getString(R.string.print_merchant_copy)));
            printString += builder.center(builder.bold(context.getString(R.string.print_important)));
            printString += builder.center(builder.bold(context.getString(R.string.print_hint1)));
            printString += builder.center(builder.bold(context.getString(R.string.print_hint2)));
            printString += builder.branch() + builder.endPrint();
            mList.add(printString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return mList;
    }
}
