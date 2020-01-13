package com.wizarpos.recode.print.result.payfor;

import android.content.Context;

import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.print.result.payfor.printcontext.PayforHtmlCustomerPrintCxt;
import com.wizarpos.recode.print.result.payfor.printcontext.PayforHtmlPrintContext;
import com.wizarpos.recode.print.result.payfor.printcontext.PayforStringCustomerPrintCxt;
import com.wizarpos.recode.print.result.payfor.printcontext.PayforStringPrintContext;

import java.util.ArrayList;
import java.util.List;

public class PayForPrintResultManager {




    public static List<String> getPrintContext(Context context, TransactionInfo transactionInfo) {
        List<String> mlit = null;
        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
            mlit = PayforHtmlPrintContext.getPrintContext(context, transactionInfo);

        } else {
            mlit = PayforStringPrintContext.getPrintContext(context, transactionInfo);
        }
        return mlit;
    }


    public static List<String> getCustomerPrintContext(Context context, TransactionInfo transactionInfo) {
        List<String> mList = new ArrayList<>();
        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
            mList = PayforHtmlCustomerPrintCxt.getCustomerPrintContext(context, transactionInfo);
        } else {
            mList = PayforStringCustomerPrintCxt.getCustomerPrintContext(context, transactionInfo);
        }
        return mList;
    }
}
