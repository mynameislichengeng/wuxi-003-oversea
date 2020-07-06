package com.wizarpos.recode.print.service.impl;

import android.text.TextUtils;
import android.util.Log;

import com.pax.poslink.peripheries.POSLinkPrinter;
import com.wizarpos.recode.print.constants.PrintConstants;
import com.wizarpos.recode.print.constants.PrintTypeEnum;
import com.wizarpos.recode.print.data.PrintPartiesDataManager;
import com.wizarpos.recode.print.service.help.MyA20PrinterListenerImpl;
import com.wizarpos.recode.print.service.PrintHandleService;

import java.util.List;

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
        if (TextUtils.isEmpty(str)) {
            return;
        }
        switch (printTypeEnum) {
            case BC://条形码
                String s = PrintConstants.PrintContentPartity.BC.getStart() + str + PrintConstants.PrintContentPartity.BC.getEnd();
                printDataFormatter.addContent(s);
//                printDataFormatter.addLineSeparator();
                break;
            case QC:
                String s2 = PrintConstants.PrintContentPartity.QC.getStart() + str + PrintConstants.PrintContentPartity.QC.getEnd();
                printDataFormatter.addContent(s2);
                break;
            case LEFT_RIGHT:
                String[] indexs = str.split(PrintConstants.LEFT_RIGHT_MARK);
                String leftText = indexs[0];
                printDataFormatter.addLeftAlign().addContent(leftText);
                String rightText = indexs[1];
                printDataFormatter.addRightAlign().addContent(rightText);
                printDataFormatter.addLineSeparator();
                break;
            default:
                printDataFormatter.addContent(str);
                printDataFormatter.addLineSeparator();
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

        } else if (isQCStartKeyWords(keyword)) {//二维码
            setPrintTypeEnum(PrintTypeEnum.QC);
        } else if (isQCEndKeyWords(keyword)) {
            setPrintTypeEnum(PrintTypeEnum.TEXT);

        } else if (isNewLineSpaceKeyWords(keyword)) {//换空格行
            printDataFormatter.addLineSeparator();
        } else if (isEndKeyWords(keyword)) {//结束，打印
            printer();
        }

    }


    private void printer() {
        printDataFormatter.addLineSeparator();
        printDataFormatter.addLineSeparator();
        printDataFormatter.addLineSeparator();
        String s = printDataFormatter.build();
        log("A920打印的内容: " + s);
        List<PrintConstants.PartityItem> mReList = PrintPartiesDataManager.getParList(s);
        MyA20PrinterListenerImpl printer = new MyA20PrinterListenerImpl(mReList);
        printer.print();
    }


    private void log(String msg) {
        Log.d("print", TAG + ">>" + msg);
    }
}
