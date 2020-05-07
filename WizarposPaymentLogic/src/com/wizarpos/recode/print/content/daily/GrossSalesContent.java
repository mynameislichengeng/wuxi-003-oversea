package com.wizarpos.recode.print.content.daily;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.model.TranLogVo;
import com.wizarpos.recode.print.base.PrintBase;

public class GrossSalesContent extends PrintBase {

    public static String printContent(TranLogVo tranLogVo) {

        String leftTitle = "Gross Sales x " + tranLogVo.getGrossSalesNumber();
        String rightValue = ("$" + Tools.formatFen(tranLogVo.getGrossSalesAmount()));
        if (isComputerSpaceForLeftRight()) {
            String s = createTextLineForLeftAndRight(leftTitle, rightValue);
            return s;
        } else {
            String str = leftTitle + multipleSpaces(31 - (("Gross Sales x " + tranLogVo.getGrossSalesNumber()) + Tools.formatFen(tranLogVo.getGrossSalesAmount())).length()) + rightValue;
            str += formatForBr();
            return str;
        }
    }
}
