package com.wizarpos.recode.print.service.impl;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;

import com.pos.device.printer.PrintCanvas;
import com.pos.device.printer.PrintTask;
import com.pos.device.printer.Printer;
import com.pos.device.printer.PrinterCallback;
import com.wizarpos.log.util.StringUtil;
import com.wizarpos.recode.print.constants.PrintConstants;
import com.wizarpos.recode.print.constants.PrintTypeEnum;
import com.wizarpos.recode.print.service.PrintHandleService;
import com.wizarpos.recode.zxing.ZxingBarcodeManager;

/**
 * AMP 设置打印的类
 */
public class PrintDeviceForAMPHandleImpl extends PrintHandleService {
    private final String TAG = PrintDeviceForAMPHandleImpl.class.getSimpleName();

    private PrintTask printTask;
    private PrintCanvas printCanvas;

    private Paint.Align align = Paint.Align.LEFT;//字体

    private Typeface typeface = Typeface.DEFAULT;//粗细

    int TEXT_SIZE_COMMON = 24;
    int TEXT_SIZE_SAMLL_TIPS = 18;

    private Paint textPaint = new Paint();//用来做文本的
    private Paint bitmapPaint = new Paint();//用来做二维码的
    private Paint samllTipsTextPaint = new Paint();




    public PrintDeviceForAMPHandleImpl() {
        init();
    }

    private void init() {
        printTask = new PrintTask();
        printCanvas = new PrintCanvas();
        //文本
        textPaint.setTextSize(TEXT_SIZE_COMMON);
        samllTipsTextPaint.setTextSize(TEXT_SIZE_SAMLL_TIPS);

    }

    @Override
    public void contentTrigger(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        switch (printTypeEnum) {
            case BC://条形码

                //显示条形码
                Bitmap bitmap = ZxingBarcodeManager.creatBarcode(str, getPrinterWidth() + 36, 60);
//                int scaledHeight = (getPrinterWidth() * bitmap.getHeight()) / bitmap.getWidth();
//                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, getPrinterWidth(), scaledHeight, false);
                printCanvas.drawBitmap(bitmap, bitmapPaint);

                //显示文本
                str = getTextForCenter(samllTipsTextPaint, str);
                printCanvas.drawText(str, samllTipsTextPaint);
                break;
            case LEFT_RIGHT://需要左右的
                String[] indexs = str.split(PrintConstants.LEFT_RIGHT_MARK);
                str = getTextForLeftRight(textPaint, indexs[0], indexs[1]);
                printCanvas.drawText(str, textPaint);
                break;
            default:
                switch (align) {
                    case LEFT:
                        break;
                    case CENTER:
                        str = getTextForCenter(textPaint, str);
                        break;
                    case RIGHT:
                        str = getTextForRight(textPaint, str);
                        break;
                    default:
                        break;
                }
                log(str);
                printCanvas.drawText(str, textPaint);
                break;
        }
    }

    @Override
    public void keywordTrigger(String keyword) {
        if (isLeftStartKeyWords(keyword)) {//左对齐
            align = Paint.Align.LEFT;
        } else if (isLeftEndKeyWords(keyword)) {

        } else if (isCenterStartKeyWords(keyword)) {//居中
            align = Paint.Align.CENTER;
        } else if (isCenterEndKeyWords(keyword)) {
            align = Paint.Align.LEFT;
        } else if (isRightStartKeyWords(keyword)) {//右对齐
            align = Paint.Align.RIGHT;
        } else if (isRightEndKeyWords(keyword)) {
            align = Paint.Align.LEFT;
        } else if (isLeftAndRightStartKeyWords(keyword)) {//
            setPrintTypeEnum(PrintTypeEnum.LEFT_RIGHT);
        } else if (isLeftAndRightEndKeyWords(keyword)) {
            setPrintTypeEnum(PrintTypeEnum.TEXT);
        } else if (isBCStartKeyWords(keyword)) {//条形码
            setPrintTypeEnum(PrintTypeEnum.BC);
        } else if (isBCEndKeyWords(keyword)) {
            setPrintTypeEnum(PrintTypeEnum.TEXT);
        } else if (isNewLineSpaceKeyWords(keyword)) {//换空格行
            paintSpaceLine();
        } else if (isEndKeyWords(keyword)) {//结束，打印
            doPrint();
        }
    }


    private void doPrint() {
        if (printTask != null) {
            printTask.setPrintCanvas(printCanvas);
            printTask.addFeedPaper(100);
            printTask.setGray(230);
            Printer.getInstance().startPrint(printTask, new PrinterCallback() {
                @Override
                public void onResult(int i, PrintTask printTask) {
                    log("onResult()--i:" + i);
                }
            });
        }
    }


    /**
     * 滑一个空格行
     */
    private void paintSpaceLine() {
        printCanvas.drawText(TEXT_SPACE, samllTipsTextPaint);
    }

    /**
     * 居中的文本
     *
     * @param textPaint
     * @param text
     * @return
     */
    private String getTextForCenter(Paint textPaint, String text) {
        float textWidth = textPaint.measureText(text);
        float printerWidth = Float.valueOf(String.valueOf(getPrinterWidth()));
        if (printerWidth <= textWidth) {
            return text;
        } else {
            float remain = (printerWidth - textWidth) / 2;
            float spaceSize = remain / getSpaceWidth(textPaint);
            return getSpaceText((int) spaceSize) + text;
        }
    }

    /**
     * 文本右边对齐
     *
     * @param textPaint
     * @param text
     * @return
     */
    private String getTextForRight(Paint textPaint, String text) {
        float textWidth = textPaint.measureText(text);
        float printerWidth = Float.valueOf(String.valueOf(getPrinterWidth()));
        if (printerWidth <= textWidth) {
            return text;
        } else {
            float remain = printerWidth - textWidth;
            float spaceSize = remain / getSpaceWidth(textPaint);
            return getSpaceText((int) spaceSize) + text;
        }
    }


    /**
     * 左右2边对齐
     *
     * @param textPaint
     * @param leftText
     * @param rightText
     * @return
     */
    private String getTextForLeftRight(Paint textPaint, String leftText, String rightText) {
        //左边文本宽度
        float leftTextWidth = textPaint.measureText(leftText);
        //右边文本宽度
        float rightTextWidth = textPaint.measureText(rightText);
        //打印机宽度
        float printerWidth = Float.valueOf(String.valueOf(getPrinterWidth()));

        if (printerWidth <= leftTextWidth + rightTextWidth) {
            return leftText + rightText;
        } else {
            float remain = printerWidth - leftTextWidth - rightTextWidth;
            float spaceSize = remain / getSpaceWidth(textPaint);
            return leftText + getSpaceText((int) spaceSize) + rightText;
        }
    }


    /**
     * 空格的宽度
     *
     * @return
     */
    private float getSpaceWidth(Paint textPaint) {
        return textPaint.measureText(TEXT_SPACE);
    }

    /**
     * 打印机的宽度
     *
     * @return
     */
    private int getPrinterWidth() {
        return Printer.getInstance().getWidth();
    }




    private void log(String msg) {
        Log.d("print", TAG + ">>" + msg);
    }
}
