package com.wizarpos.recode.print.content.daily;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.model.TranLogVo;
import com.wizarpos.recode.print.base.PrintBase;

public class NetSalesContent extends PrintBase {

    public static String printContent(TranLogVo tranLogVo) {

        String leftTilte = "Net Sales x " + tranLogVo.getNetSalesNumber();
        String rightTitle = ("$" + Tools.formatFen(tranLogVo.getNetSalesAmount()));
        if (isComputerSpaceForLeftRight()) {
            return createTextLineForLeftAndRight(leftTilte, rightTitle);
        } else {
            String printString = leftTilte + multipleSpaces(31 - (("Net Sales x " + tranLogVo.getNetSalesNumber()) + Tools.formatFen(tranLogVo.getNetSalesAmount())).length()) + rightTitle;
            printString += formatForBr();
            return printString;
        }
    }
}
