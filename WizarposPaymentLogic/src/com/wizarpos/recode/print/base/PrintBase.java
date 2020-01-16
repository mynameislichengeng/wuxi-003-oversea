package com.wizarpos.recode.print.base;

import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.recode.constants.TransRecordLogicConstants;

public class PrintBase {

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
            result = result - 1 * zhCount;
        }
        return result;
    }

    protected static String formatForC(String txt) {
        return q1PrintBuilder.center(txt);
    }

    protected static String formatForBC(String txt){
        return q1PrintBuilder.barcode(txt);
    }

    protected static String formatForBr(){
        return q1PrintBuilder.br();
    }
}
