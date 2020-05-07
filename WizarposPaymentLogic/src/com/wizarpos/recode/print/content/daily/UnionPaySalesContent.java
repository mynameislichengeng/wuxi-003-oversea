package com.wizarpos.recode.print.content.daily;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.model.TranLogVo;
import com.wizarpos.recode.print.base.PrintBase;

public class UnionPaySalesContent extends PrintBase {
    public static String printContent(TranLogVo tranLogVo) {

        String leftTilte = "Net Sales x " + tranLogVo.getUnionPayNetSalesNumber();
        String rightValue = ("$" + Tools.formatFen(tranLogVo.getUnionPayNetSalesAmount()));
        if (isComputerSpaceForLeftRight()) {
            return createTextLineForLeftAndRight(leftTilte, rightValue);
        } else {
            String printString = leftTilte + multipleSpaces(32 - (("Net Sales x " + tranLogVo.getUnionPayNetSalesNumber()) + "$" + Tools.formatFen(tranLogVo.getUnionPayNetSalesAmount())).length()) + rightValue;
            printString += formatForBr();
            return printString;
        }

    }
}
