package com.wizarpos.recode.print;

import com.wizarpos.recode.constants.TransRecordLogicConstants;

public class PrintManager {

    public static int tranZhSpaceNums(int origin, int zhCount, String type) {
        int result = origin;
        if (TransRecordLogicConstants.TRANSCURRENCY.CNY.getType().equals(type)) {
            result = result - 1 * zhCount;
        }
        return result;
    }

}
