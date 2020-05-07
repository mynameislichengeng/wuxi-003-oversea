package com.wizarpos.recode.print.content.daily;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.model.TranLogVo;
import com.wizarpos.recode.print.base.PrintBase;

public class AliSalesContent extends PrintBase {
    public static String printContent(TranLogVo tranLogVo) {

        String leftTitle = "Net Sales x " + tranLogVo.getAlipayNetSalesNumber();
        String rightTitle = ("$" + Tools.formatFen(tranLogVo.getAlipayNetSalesAmount()));
        if (isComputerSpaceForLeftRight()) {
            return createTextLineForLeftAndRight(leftTitle, rightTitle);
        } else {
            String str = leftTitle + multipleSpaces(32 - (("Net Sales x " + tranLogVo.getAlipayNetSalesNumber()) + "$" + Tools.formatFen(tranLogVo.getAlipayNetSalesAmount())).length()) + rightTitle;
            str += formatForBr();
            return str;
        }
    }
}
