package com.wizarpos.recode.print.service.impl;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.pos.device.printer.PrintCanvas;
import com.pos.device.printer.PrintTask;
import com.pos.device.printer.Printer;
import com.pos.device.printer.PrinterCallback;
import com.wizarpos.recode.print.constants.PrintConstants;
import com.wizarpos.recode.print.constants.PrintTypeEnum;
import com.wizarpos.recode.print.service.PrintHandleService;

/**
 * AMP 设置打印的类
 */
public class PrintDeviceForAMPHandleImpl extends PrintHandleService {
    private final String TAG = PrintDeviceForAMPHandleImpl.class.getSimpleName();

    private PrintTask printTask;
    private PrintCanvas printCanvas;

    private Paint.Align align = Paint.Align.LEFT;//字体

    private Typeface typeface = Typeface.DEFAULT;//粗细

    int TEXT_SIZE_COMMON = 20;

    private Paint textPaint = new Paint();


    private final String TEXT_SPACE = " ";

    public PrintDeviceForAMPHandleImpl() {
        init();
    }

    private void init() {
        printTask = new PrintTask();
        printCanvas = new PrintCanvas();
        //文本
        textPaint.setTextSize(TEXT_SIZE_COMMON);

    }

    @Override
    public void contentTrigger(String str) {
        switch (printTypeEnum) {
            case BC://条形码

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
        } else if (isNewLineKeyWords(keyword)) {//换行

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
        printCanvas.drawText(TEXT_SPACE, textPaint);
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
            float spaceSize = remain / getSpaceWidth();
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
            float spaceSize = remain / getSpaceWidth();
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
            float spaceSize = remain / getSpaceWidth();
            return leftText + getSpaceText((int) spaceSize) + rightText;
        }
    }


    /**
     * 空格的宽度
     *
     * @return
     */
    private float getSpaceWidth() {
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

    /**
     * 根据指定的空格数，获得空格文本
     *
     * @param count
     * @return
     */
    private String getSpaceText(int count) {
        if (count <= 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            sb.append(TEXT_SPACE);
        }
        return sb.toString();
    }

    private void log(String msg) {
        Log.d("print", TAG + ">>" + msg);
    }
}
