package com.wizarpos.recode.print.service.impl;

import android.graphics.Bitmap;
import android.graphics.Color;


import com.wizarpos.device.printer.PrinterCommand;
import com.wizarpos.recode.print.constants.PrintConstants;
import com.wizarpos.recode.print.constants.PrintTypeEnum;
import com.wizarpos.recode.print.service.PrintHandleService;
import com.wizarpos.recode.zxing.ZxingBarcodeManager;
import com.wizarpos.jni.PrinterInterface;

import java.io.UnsupportedEncodingException;

public class PrintDeviceForQ2HandleImpl extends PrintHandleService {

    private static final int GSV_HEAD = 8;
    private static final int WIDTH = 48;
    public static final int BIT_WIDTH = 384;

    private final int MAX_WIDTH = 32;

    public static int QR_WIDTH = 350;
    public static int QR_HEIGHT = 350;

    public PrintDeviceForQ2HandleImpl() {
        startPrint();
    }

    public static void startPrint() {
        PrinterInterface.open();
        PrinterInterface.begin();
        printerWrite(PrinterCommand.init());
        printerWrite(PrinterCommand.setHeatTime(300));
    }

    public static void closePrint() {
        PrinterInterface.end();
        PrinterInterface.close();
    }


    @Override
    public void contentTrigger(String str) {

        switch (printTypeEnum) {
            case BC://一维码
                settingFormatCenter();
                printerBarcode(str);
                //文字
                String bartext = getAlignCenter(str);
                printerText(bartext);
                printerLine();
                break;
            case QC:
                printerQrcode(str);
                printerLine();
                break;
            case LEFT_RIGHT:
                String[] indexs = str.split(PrintConstants.LEFT_RIGHT_MARK);
                //计算空格
                String text = getAlignLeftAndRight(indexs[0], indexs[1]);
                printerText(text);
                //换行
                printerLine();
                break;
            default:
                printerText(str);
                printerLine();
                break;
        }
    }

    @Override
    public void keywordTrigger(String keyword) {
        if (isBoldStartKeyWords(keyword)) {//加粗
            settingTextBlod();

        } else if (isBoldEndKeyWords(keyword)) {
            settingTextUnBlod();

        } else if (isLeftStartKeyWords(keyword)) {//左对齐
            settingFormatLeft();
        } else if (isLeftEndKeyWords(keyword)) {

        } else if (isRightStartKeyWords(keyword)) {//右对齐
            settingFormatRight();
        } else if (isRightEndKeyWords(keyword)) {
            settingFormatLeft();

        } else if (isCenterStartKeyWords(keyword)) {//居中对齐
            settingFormatCenter();
        } else if (isCenterEndKeyWords(keyword)) {
            settingFormatLeft();

        } else if (isLeftAndRightStartKeyWords(keyword)) {//左右对齐
            setPrintTypeEnum(PrintTypeEnum.LEFT_RIGHT);

        } else if (isLeftAndRightEndKeyWords(keyword)) {
            setPrintTypeEnum(PrintTypeEnum.TEXT);

        } else if (isBCStartKeyWords(keyword)) {// 一维码
            setPrintTypeEnum(PrintTypeEnum.BC);

        } else if (isBCEndKeyWords(keyword)) {
            setPrintTypeEnum(PrintTypeEnum.TEXT);

        } else if (isQCStartKeyWords(keyword)) {//二维码
            setPrintTypeEnum(PrintTypeEnum.QC);

        } else if (isQCEndKeyWords(keyword)) {
            setPrintTypeEnum(PrintTypeEnum.TEXT);

        } else if (isNewLineSpaceKeyWords(keyword)) {
            printerLine();
        } else if (isEndKeyWords(keyword)) {
            printerLine();
            printerLine();
            printerLine();
        }

    }


    /**
     * 如果是左右对齐需要如何来排列
     *
     * @param left
     * @param right
     * @return
     */
    private String getAlignLeftAndRight(String left, String right) {
        int num = 0;
        int leftLen = left.length();
        int rightLen = right.length();

        if (leftLen + rightLen > MAX_WIDTH) {
            num = 0;
        } else {
            num = MAX_WIDTH - leftLen - rightLen;
        }
        String space = getSpaceText(num);
        StringBuffer sb = new StringBuffer();
        sb.append(left);
        sb.append(space);
        sb.append(right);
        return sb.toString();
    }

    /**
     * 如果文本要居中，然后又不使用api，直接使用空格来替代，就用这个方法
     *
     * @param text
     * @return
     */
    private String getAlignCenter(String text) {
        int num = 0;
        int textLen = text.length();
        if (textLen > MAX_WIDTH) {
            num = 0;
        } else {
            num = MAX_WIDTH - textLen;
            //因为居中，所以只需要一半
            num = num / 2;
        }
        String space = getSpaceText(num);
        StringBuffer sb = new StringBuffer();
        sb.append(space);
        sb.append(text);
        return sb.toString();
    }

    /**
     * 设置左对齐
     */
    private void settingFormatLeft() {
        printerWrite(PrinterCommand.setAlignMode(0));
    }

    private void settingFormatCenter() {
        printerWrite(PrinterCommand.setAlignMode(1));
    }

    private void settingFormatRight() {
        printerWrite(PrinterCommand.setAlignMode(2));
    }


    /**
     * 设置加粗
     */
    private void settingTextBlod() {
        printerWrite(PrinterCommand.setFontBold(1));
    }

    /**
     * 设置不加粗
     */
    private void settingTextUnBlod() {
        printerWrite(PrinterCommand.setFontBold(0));
    }


    /**
     * 打印换行
     */
    private void printerLine() {
        printerWrite(PrinterCommand.linefeed());
    }

    /**
     * 打印条形码
     *
     * @param barcode
     */
    private static void printerBarcode(String barcode) {
        Bitmap bitmap = ZxingBarcodeManager.creatBarcode(barcode, 350, 60);
        printerBitmap(bitmap);
    }

    /**
     * 打印二维码
     *
     * @param qrcode
     */
    private static void printerQrcode(String qrcode) {
        Bitmap bitmap = ZxingBarcodeManager.creatQrcode(qrcode, QR_WIDTH, QR_HEIGHT);
        printerBitmap(bitmap);
    }


    /**
     * 打印map图片
     *
     * @param bitmap
     */
    private static void printerBitmap(Bitmap bitmap) {
        byte[] result = generateBitmapArrayGSV_MSB(bitmap, 7, 5);
        int lines = (result.length - GSV_HEAD) / WIDTH;
        System.arraycopy(new byte[]{0x1D, 0x76, 0x30, 0x00, 0x30, 0x00, (byte) (lines & 0xff), (byte) ((lines >> 8) & 0xff)}, 0, result, 0, GSV_HEAD);
        printerWrite(result);
    }


    /**
     * 打印文本
     *
     * @param text
     */
    private static void printerText(String text) {
        try {
            byte[] bytes = text.getBytes("GB2312");
            printerWrite(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static void printerWrite(byte[] data) {
        PrinterInterface.write(data, data.length);
    }

    /**
     * 这个是bitmap转换成二进制的方法
     * generate the MSB buffer for bitmap printing GSV command
     *
     * @param bm            the android's Bitmap data
     * @param bitMarginLeft the left white space in bits.
     * @param bitMarginTop  the top white space in bits.
     * @return buffer with DC2V_HEAD + image length
     */
    private static byte[] generateBitmapArrayGSV_MSB(Bitmap bm, int bitMarginLeft, int bitMarginTop) {
        byte[] result = null;
        int n = bm.getHeight() + bitMarginTop;
        int offset = GSV_HEAD;
        result = new byte[n * WIDTH + offset];
        for (int y = 0; y < bm.getHeight(); y++) {
            for (int x = 0; x < bm.getWidth(); x++) {
                if (x + bitMarginLeft < BIT_WIDTH) {
                    int color = bm.getPixel(x, y);
                    int alpha = Color.alpha(color);
                    int red = Color.red(color);
                    int green = Color.green(color);
                    int blue = Color.blue(color);
                    if (alpha > 128 && (red < 128 || green < 128 || blue < 128)) {
                        // set the color black
                        int bitX = bitMarginLeft + x;
                        int byteX = bitX / 8;
                        int byteY = y + bitMarginTop;
                        result[offset + byteY * WIDTH + byteX] |= (0x80 >> (bitX - byteX * 8));
                    }
                } else {
                    // ignore the rest data of this line
                    break;
                }
            }
        }
        return result;
    }

}
