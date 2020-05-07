package com.wizarpos.recode.print.content.daily;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.model.TranLogVo;
import com.wizarpos.recode.print.base.PrintBase;

public class RefundDailyContent extends PrintBase {

    public static String printContent(TranLogVo tranLogVo) {

        String leftTile = "Refunds x "+tranLogVo.getRefundsNumber();
        String rightValue = "$" + Tools.formatFen(tranLogVo.getRefundsAmount());

        if (isComputerSpaceForLeftRight()) {
            return createTextLineForLeftAndRight(leftTile, rightValue);
        } else {
            String printString = leftTile  + multipleSpaces(31 - (("Refunds x " + tranLogVo.getRefundsNumber()) + Tools.formatFen(tranLogVo.getRefundsAmount())).length()) + rightValue;
            printString += formatForBr();
            return printString;
        }
    }
}
