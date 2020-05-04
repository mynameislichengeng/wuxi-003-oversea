package com.wizarpos.recode.print.service.impl;

import android.graphics.Typeface;
import android.util.Log;

import com.nexgo.oaf.apiv3.device.printer.AlignEnum;
import com.nexgo.oaf.apiv3.device.printer.BarcodeFormatEnum;
import com.nexgo.oaf.apiv3.device.printer.GrayLevelEnum;
import com.nexgo.oaf.apiv3.device.printer.OnPrintListener;
import com.nexgo.oaf.apiv3.device.printer.Printer;
import com.wizarpos.atool.log.Logger;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.recode.print.constants.PrintConstants;
import com.wizarpos.recode.print.constants.PrintTypeEnum;
import com.wizarpos.recode.print.data.BarcodeDataManager;
import com.wizarpos.recode.print.service.PrintHandleService;

public class PrintDeviceForN3N5HandleImpl extends PrintHandleService {
    private static final int FONT_SIZE_NORMAL = 24;


    private Printer printerN3N5;
    private boolean isBoldFont;
    private AlignEnum align;

    public PrintDeviceForN3N5HandleImpl() {
        printerN3N5 = PaymentApplication.getInstance().deviceEngine.getPrinter();
        printerN3N5.initPrinter();
        printerN3N5.setTypeface(Typeface.DEFAULT);
        printerN3N5.setLetterSpacing(5);
        printerN3N5.setGray(GrayLevelEnum.LEVEL_2);
    }

    public boolean getBoldFont() {
        return isBoldFont;
    }

    public AlignEnum getAlign() {
        return align;
    }


    @Override
    public void contentTrigger(String str) {
        switch (printTypeEnum) {
            case BC://1维码
                str = str.replace(" ", "");
                //第1个参数表示内容，第2个表示宽度
                //第3个参数表示边距离
                //格式1
                printerN3N5.appendBarcode(str, 50, 0, 2, BarcodeFormatEnum.CODE_128, AlignEnum.CENTER);
                Log.d("print", "打印barcode使用的格式:" + BarcodeDataManager.getCurrentFormat().getName());
                break;
            case LEFT_RIGHT:

                String[] indexs = str.split(PrintConstants.LEFT_RIGHT_MARK);
                printerN3N5.appendPrnStr(indexs[0], indexs[1], FONT_SIZE_NORMAL, false);
                break;
            default:
                printerN3N5.appendPrnStr(str, FONT_SIZE_NORMAL, getAlign(), getBoldFont());
                break;
        }
    }

    @Override
    public void keywordTrigger(String keyword) {
        if (keyword.equals("<b>")) {
            isBoldFont = true;
        } else if (keyword.equals("</b>")) {
            isBoldFont = false;
        } else if (keyword.equals("<c>")) {
            align = AlignEnum.CENTER;
        } else if (keyword.equals("</c>")) {
            align = AlignEnum.LEFT;
        } else if (keyword.equals("<w>")) {
        } else if (keyword.equals("</w>")) {
        } else if (keyword.equals("<h>")) {
        } else if (keyword.equals("</h>")) {
        } else if (keyword.equals("<s>")) {
        } else if (keyword.equals("</s>")) {
        } else if (keyword.equals("<i>")) {

        } else if (keyword.equals("</i>")) {

        } else if (keyword.equals("<l>")) {
            align = AlignEnum.LEFT;
        } else if (keyword.equals("</l>")) {

        } else if (keyword.equals("<r>")) {
            align = AlignEnum.RIGHT;
        } else if (keyword.equals("</r>")) {
            align = AlignEnum.LEFT;
        } else if (isLeftAndRightStartKeyWords(keyword)) {
            setPrintTypeEnum(PrintTypeEnum.LEFT_RIGHT);
        } else if (isLeftAndRightEndKeyWords(keyword)) {
            setPrintTypeEnum(PrintTypeEnum.TEXT);
        } else if (keyword.equals("<bc>")) {
            setPrintTypeEnum(PrintTypeEnum.BC);
        } else if (keyword.equals("</bc>")) { // 一维码
            setPrintTypeEnum(PrintTypeEnum.TEXT);
        } else if (keyword.equals("<ul>")) {

        } else if (keyword.equals("</ul>")) { // 下划线

        } else if (keyword.equals("<img>")) {

        } else if (keyword.equals("</img>")) {

        } else if (keyword.equals("<nbr/>")) {
            printerN3N5.appendPrnStr("\n", FONT_SIZE_NORMAL, AlignEnum.LEFT, false);
        } else if (keyword.equals("<t/>")) {

        } else if (keyword.equals("<sls>")) {
        } else if (keyword.equals("</sls>")) {
        } else if (keyword.equals("<end/>")) {
            printerN3N5.startPrint(true, new OnPrintListener() {
                @Override
                public void onPrintResult(final int retCode) {
                    Logger.debug("N3/N5打印完成状态 = " + retCode);
                }
            });

        }
    }

    public Printer getPrinterN3N5() {
        return printerN3N5;
    }
}
