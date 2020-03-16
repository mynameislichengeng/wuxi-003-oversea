package com.wizarpos.recode.print.base;

import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.recode.constants.TransRecordLogicConstants;

public class PrintBase {

    private static int COUNTSPACE = 0;

    protected final static int PART_LENGTH = 20;//当长度大于14的时候，就分割一下
    protected static final int PART_NUM_13 = 13;

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

    protected static String removeFuhao(String originStr) {
        if (originStr.startsWith("-")) {
            return originStr.substring(1);
        }
        return originStr;
    }

    protected static int tranZhSpaceNums(int origin, int zhCount, String type) {
        int result = origin;
        if (TransRecordLogicConstants.TRANSCURRENCY.CNY.getType().equals(type)) {
            if (getDeviceTypeForN3N5()) {
                result = result - 2 * zhCount;
            } else {
                result = result - 1 * zhCount;
            }

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


    protected static String formatForC(String txt) {
        return q1PrintBuilder.center(txt);
    }

    protected static String formatForBC(String txt) {
        return q1PrintBuilder.barcode(txt);
    }

    protected static String formatForBr() {
        return q1PrintBuilder.br();
    }

    protected static String formatForNBr() {
        return q1PrintBuilder.nBr();
    }

    public static int getCOUNTSPACE() {
        return COUNTSPACE;
    }

    public static void setCOUNTSPACE(int COUNTSPACE) {
        PrintBase.COUNTSPACE = COUNTSPACE;
    }
}
