package com.wizarpos.recode.print.base;

public class PrintBase {

    protected static String multipleSpaces(int n) {
        String output = "";
        for (int i = 0; i < n; i++)
            output += " ";
        return output;
    }
}
