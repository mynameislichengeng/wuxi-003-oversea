package com.wizarpos.recode.print.content.daily;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.model.TranLogVo;
import com.wizarpos.recode.print.base.PrintBase;

public class WechatSalesContent extends PrintBase {

    public static String printContent(TranLogVo tranLogVo){
        String leftTitle = "Net Sales x " + tranLogVo.getWechatNetSalesNumber();
        String rightTitle = ("$" + Tools.formatFen(tranLogVo.getWechatNetSalesAmount()));

        if (isComputerSpaceForLeftRight()) {
            return createTextLineForLeftAndRight(leftTitle, rightTitle);
        }else {
            String str = leftTitle+ multipleSpaces(32 - (("Net Sales x " + tranLogVo.getWechatNetSalesNumber()) + "$" + Tools.formatFen(tranLogVo.getWechatNetSalesAmount())).length()) + rightTitle;
            str += formatForBr();
            return str;
        }
    }
}
