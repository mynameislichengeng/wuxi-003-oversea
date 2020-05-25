package com.wizarpos.recode.print.base;

import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.model.TransactionInfo;
import com.wizarpos.recode.constants.TransRecordLogicConstants;
import com.wizarpos.recode.print.constants.PrintConstants;
import com.wizarpos.recode.receipt.service.ReceiptDataManager;

public class PrintBase {


    protected final static int PART_LENGTH = 18;//当长度大于14的时候，就分割一下


    protected static Q1PrintBuilder q1PrintBuilder = new Q1PrintBuilder();

    protected static String multipleSpaces(int n) {
        String output = "";
        for (int i = 0; i < n; i++)
            output += " ";
        return output;
    }

    protected static String divide100(String originStr) {
        return Calculater.divide100(originStr);
    }

    /**
     * 移除负号，有的数据前面有一个负号，可以将其移除
     *
     * @param originStr
     * @return
     */
    protected static String removeFuhao(String originStr) {
        if (originStr.startsWith("-")) {
            return originStr.substring(1);
        }
        return originStr;
    }

    protected static int tranZhSpaceNums(int origin, int zhCount, String type) {
        int result = origin;
        if (TransRecordLogicConstants.TRANSCURRENCY.CNY.getType().equals(type)) {
            result = result - 1 * zhCount;

        }
        return result;
    }


    protected static boolean getDeviceTypeForN3N5() {
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_N3_OR_N5) {
            return true;
        } else {
            return false;
        }
    }

    protected static boolean getDeviceTypeForPaxA920() {
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_PAX_A920) {
            return true;
        } else {
            return false;
        }
    }

    protected static boolean getDeviceTypeForAMP8200() {
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_AMP8) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 没有左右对齐语法，需要计算空格
     *
     * @return
     */
    protected static boolean isComputerSpaceForLeftRight() {
//        if (getDeviceTypeForAMP8200()) {
//            return true;
//        }
//        if (getDeviceTypeForPaxA920()) {
//            return true;
//        }
//        if (getDeviceTypeForN3N5()) {
//            return true;
//        }

        return true;
    }

    /**
     * 是否打开条形码
     *
     * @return
     */
    protected static boolean isOpenBarCodeStatus() {
        return ReceiptDataManager.isOpenBarcodeStatus();
    }

    /**
     * 是打开二维码
     *
     * @return
     */
    protected static boolean isOpenQrCodeStatus() {
        return ReceiptDataManager.isOpenQRCodeStatus();
    }

    /**
     * 创建左对齐文本
     *
     * @param leftText
     * @param rightText
     * @return
     */
    protected static String createTextLineForLeft(String leftText, String rightText) {
        StringBuffer sb = new StringBuffer();
        if (rightText.length() > PART_LENGTH) {
            sb.append(formartForLeft(leftText));
            sb.append(formartForRight(rightText));
        } else {
            sb.append(formartForLeft(leftText + rightText));
        }
        return sb.toString();
    }

    /**
     * 创建左右对齐的文本行
     *
     * @param leftText
     * @param rightText
     * @return
     */
    protected static String createTextLineForLeftAndRight(String leftText, String rightText) {
        StringBuffer sb = new StringBuffer();
        if (rightText.length() > PART_LENGTH) {
            sb.append(formartForLeft(leftText));
            sb.append(formartForRight(rightText));
        } else {
            sb.append(formartForLeftAndRight(leftText, rightText));
        }
        return sb.toString();
    }


    protected static String formartForLeft(String txt) {
        return q1PrintBuilder.left(txt);
    }

    protected static String formatForC(String txt) {
        return q1PrintBuilder.center(txt);
    }

    protected static String formartForRight(String txt) {
        return q1PrintBuilder.right(txt);
    }

    protected static String formartForLeftAndRight(String left, String right) {
        String txt = left + PrintConstants.LEFT_RIGHT_MARK + right;
        return q1PrintBuilder.leftAndright(txt);
    }

    protected static String formartForBold(String txt) {
        return q1PrintBuilder.bold(txt);
    }

    protected static String formatForBC(String txt) {
        return q1PrintBuilder.barcode(txt);
    }

    protected static String formatForQC(String txt) {
        return q1PrintBuilder.qrcode(txt);
    }

    protected static String formatForBr() {
        return q1PrintBuilder.br();
    }

    protected static String formatForNBr() {
        return q1PrintBuilder.nBr();
    }


    protected static String formartForLineSpace() {
        return q1PrintBuilder.lineSpace();
    }


}
