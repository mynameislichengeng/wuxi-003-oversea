package com.wizarpos.recode.print.service.impl;

import android.content.Context;
import android.util.Log;

import com.pax.poslink.peripheries.POSLinkPrinter;
import com.pax.poslink.peripheries.ProcessResult;
import com.wizarpos.atool.log.Logger;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.recode.print.constants.PrintConstants;
import com.wizarpos.recode.print.constants.PrintTypeEnum;
import com.wizarpos.recode.print.service.PrintHandleService;

/**
 * paxa920打印方式
 */
public class PrintDeviceForA920HandleImpl extends PrintHandleService {

    private final String TAG = PrintDeviceForA920HandleImpl.class.getSimpleName();

    //A920设备
    private POSLinkPrinter.PrintDataFormatter printDataFormatter;

    public PrintDeviceForA920HandleImpl() {
        printDataFormatter = new POSLinkPrinter.PrintDataFormatter();
    }

    @Override
    public void contentTrigger(String str) {
        switch (printTypeEnum) {
            case BC://条形码
                String barCodeStr = String.format("\\$BARD,2,1,8,%s", str);
                printDataFormatter.addContent(barCodeStr);
                break;
            case LEFT_RIGHT:
                String[] indexs = str.split(PrintConstants.LEFT_RIGHT_MARK);
                String leftText = indexs[0];
                printDataFormatter.addLeftAlign().addContent(leftText);
                String rightText = indexs[1];
                printDataFormatter.addRightAlign().addContent(rightText);
                break;
            default:
                printDataFormatter.addContent(str);
                break;
        }
    }

    @Override
    public void keywordTrigger(String keyword) {
        if (isLeftStartKeyWords(keyword)) {//左对齐
            printDataFormatter.addLeftAlign();
        } else if (isLeftEndKeyWords(keyword)) {

        } else if (isCenterStartKeyWords(keyword)) {//居中
            printDataFormatter
                    .addCenterAlign();
        } else if (isCenterEndKeyWords(keyword)) {
            printDataFormatter.addLineSeparator();
        } else if (isRightStartKeyWords(keyword)) {//右对齐
            printDataFormatter.addRightAlign();
        } else if (isRightEndKeyWords(keyword)) {

        } else if (isLeftAndRightStartKeyWords(keyword)) {//2端左右对齐
            setPrintTypeEnum(PrintTypeEnum.LEFT_RIGHT);
        } else if (isLeftAndRightEndKeyWords(keyword)) {
            setPrintTypeEnum(PrintTypeEnum.TEXT);
        } else if (isBCStartKeyWords(keyword)) {//条形码
            setPrintTypeEnum(PrintTypeEnum.BC);
        } else if (isBCEndKeyWords(keyword)) {
            setPrintTypeEnum(PrintTypeEnum.TEXT);
        } else if (isLineKeyWords(keyword)) {//换行
            printDataFormatter.addLineSeparator();
        } else if (isNLineKeyWords(keyword)) {//换行
//            printDataFormatter.addLineSeparator();
        } else if (isEndKeyWords(keyword)) {//结束，打印
            Context context = PaymentApplication.getInstance().getApplicationContext();
            String s = printDataFormatter.build();
            int cutmode = POSLinkPrinter.CutMode.DO_NOT_CUT;
            log("A920打印的内容: " + s);
            POSLinkPrinter.getInstance(context).print(s, cutmode, new POSLinkPrinter.PrintListener() {
                @Override
                public void onSuccess() {
                    log("A920打印完成onSuccess()");

                }

                @Override
                public void onError(ProcessResult processResult) {
                    log("A920打印完成onError()");
                }
            });
        }

    }

    private void log(String msg) {
        Log.d("print", TAG + ">>" + msg);
    }
}
