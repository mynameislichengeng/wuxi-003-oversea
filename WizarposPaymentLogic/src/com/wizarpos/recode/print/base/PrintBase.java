package com.wizarpos.recode.print.base;

import com.wizarpos.pay.common.utils.Calculater;

public class PrintBase {

    protected static String multipleSpaces(int n) {
        String output = "";
        for (int i = 0; i < n; i++)
            output += " ";
        return output;
    }

    protected static String divide100(String originStr){
       return Calculater.divide100(originStr);
    }
}
