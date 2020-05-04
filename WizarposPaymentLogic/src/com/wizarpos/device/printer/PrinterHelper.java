package com.wizarpos.device.printer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.wizarpos.jni.PrinterInterface;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.recode.print.constants.HtmlRemarkConstans;
import com.wizarpos.recode.print.constants.KeyWords;
import com.wizarpos.recode.print.constants.PrintTypeEnum;
import com.wizarpos.recode.print.data.SpixContent;
import com.wizarpos.recode.print.service.PrintHandleService;
import com.wizarpos.recode.print.service.impl.PrintDeviceForA920HandleImpl;
import com.wizarpos.recode.print.service.impl.PrintDeviceForAMPHandleImpl;
import com.wizarpos.recode.print.service.impl.PrintDeviceForN3N5HandleImpl;
import com.wizarpos.recode.zxing.ZxingBarcodeManager;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class PrinterHelper {

    public static final int BIT_WIDTH = 384;
    private static final int WIDTH = 48;
    private static final int GSV_HEAD = 8;


    public static void print(String text) {
        Log.d("print", "打印的内容:" + text);
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_N3_OR_N5) {
            KeywordTrigger trigger = new KeywordTrigger(KeyWords.keywords);
            trigger.setHandle(new PrintDeviceForN3N5HandleImpl());
            trigger.setSource(text);
            trigger.parse();
        } else if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_PAX_A920) {
            KeywordTrigger trigger = new KeywordTrigger(KeyWords.keywords);
            trigger.setHandle(new PrintDeviceForA920HandleImpl());
            trigger.setSource(text);
            trigger.parse();
        } else if(DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_AMP8){
            KeywordTrigger trigger = new KeywordTrigger(KeyWords.keywords);
            trigger.setHandle(new PrintDeviceForAMPHandleImpl());
            trigger.setSource(text);
            trigger.parse();
        }else {
            try {
                List<SpixContent.PrintType> printTypes = SpixContent.splitFromBC(text);
                for (SpixContent.PrintType printContent : printTypes) {
                    try {
                        PrinterInterface.open();
                        PrinterInterface.begin();
                        printerWrite(PrinterCommand.init());
                        printerWrite(PrinterCommand.setHeatTime(300));
                        KeywordTrigger trigger = new KeywordTrigger(KeyWords.keywords);
                        trigger.setHandle(new PrinterHelper.PrinterKeywordTriggerHandle());
                        trigger.setSource(printContent.getTxt());
                        trigger.parse();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        PrinterInterface.end();
                        PrinterInterface.close();
                    }
                }
            } catch (Exception e) {

            }

        }
    }


    public static void printBitmap(Bitmap bitmap) {
        try {
            if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_N3_OR_N5) {


            } else {
                PrinterInterface.open();
                PrinterInterface.begin();
                printerWrite(PrinterCommand.init());
                printerWrite(PrinterCommand.setHeatTime(180));

                byte[] result = generateBitmapArrayGSV_MSB(bitmap, 0, 0);
                int lines = (result.length - GSV_HEAD) / WIDTH;
                System.arraycopy(new byte[]{0x1D, 0x76, 0x30, 0x00, 0x30, 0x00, (byte) (lines & 0xff), (byte) ((lines >> 8) & 0xff)}, 0, result, 0, GSV_HEAD);

                printerWrite(result);
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            PrinterInterface.end();
            PrinterInterface.close();
        }
    }

    /**
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


    private static void printText(String text) {
        try {
            byte[] bytes = text.getBytes("GB2312");
            PrinterInterface.write(bytes, bytes.length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static void printerBarcode(String barcode) {
        Bitmap bitmap = ZxingBarcodeManager.creatBarcode(barcode, 400, 60);
        byte[] result = generateBitmapArrayGSV_MSB(bitmap, 7, 5);
        int lines = (result.length - GSV_HEAD) / WIDTH;
        System.arraycopy(new byte[]{0x1D, 0x76, 0x30, 0x00, 0x30, 0x00, (byte) (lines & 0xff), (byte) ((lines >> 8) & 0xff)}, 0, result, 0, GSV_HEAD);
        printerWrite(result);
    }

    public static void printerWrite(byte[] data) {
        PrinterInterface.write(data, data.length);
    }

    private static void write(byte[] data) {
        PrinterInterface.write(data, data.length);
    }


    static class PrinterKeywordTriggerHandle extends PrintHandleService {

        public PrinterKeywordTriggerHandle() {
        }

        @Override
        public void contentTrigger(String str) {

            switch (printTypeEnum) {
                case BC://一维码
                    String txt = str.replace(" ", "");
                    printerBarcode(txt);
                    //文字
                    printText(str);
                    break;
                default:
                    printText(str);
                    break;
            }
        }

        @Override
        public void keywordTrigger(String keyword) {

            if (keyword.equals("<b>")) {
                write(PrinterCommand.setFontBold(1));
            } else if (keyword.equals("</b>")) {
                write(PrinterCommand.setFontBold(0));
            } else if (keyword.equals("<c>")) {
                write(PrinterCommand.setAlignMode(1));
            } else if (keyword.equals("</c>")) {
                write(PrinterCommand.linefeed());
                write(PrinterCommand.setAlignMode(0));
            } else if (keyword.equals("<w>")) {
                write(PrinterCommand.getCmdEscSo());

            } else if (keyword.equals("</w>")) {
                write(PrinterCommand.getCmdEscDc4());
            } else if (keyword.equals("<h>")) {
                write(PrinterCommand.setFontEnlarge(0x01));
            } else if (keyword.equals("</h>")) {
                write(PrinterCommand.setFontEnlarge(0x00));
            } else if (keyword.equals("<s>")) {
                write(PrinterCommand.getCmdSmallFontCN(1));
                write(PrinterCommand.getCmdSmallFontEN(1));
            } else if (keyword.equals("</s>")) {
                write(PrinterCommand.getCmdSmallFontCN(0));
                write(PrinterCommand.getCmdSmallFontEN(0));
            } else if (keyword.equals("<i>")) {

            } else if (keyword.equals("</i>")) {

            } else if (keyword.equals("<l>")) {
                write(PrinterCommand.setAlignMode(0));
            } else if (keyword.equals("</l>")) {

            } else if (keyword.equals("<r>")) {
                write(PrinterCommand.setAlignMode(50));
            } else if (keyword.equals("</r>")) {
                write(PrinterCommand.linefeed());
                write(PrinterCommand.setAlignMode(2));
            } else if (keyword.equals("<bc>")) {
                setPrintTypeEnum(PrintTypeEnum.BC);
            } else if (keyword.equals("</bc>")) { // 一维码
                setPrintTypeEnum(PrintTypeEnum.TEXT);
            } else if (keyword.equals("<ul>")) {

            } else if (keyword.equals("</ul>")) { // 下划线

            } else if (keyword.equals("<img>")) {

            } else if (keyword.equals("</img>")) {

            } else if (keyword.equals("<br/>")) {
                write(PrinterCommand.linefeed());
            } else if (keyword.equals("<t/>")) {

            } else if (keyword.equals("<sls>")) {
                write(PrinterCommand.getCmdEsc3N(20));
            } else if (keyword.equals("</sls>")) {
                write(PrinterCommand.getCmdEsc2());
            }

        }


    }


}
