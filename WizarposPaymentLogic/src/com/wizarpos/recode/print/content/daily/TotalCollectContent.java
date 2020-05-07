package com.wizarpos.recode.print.content.daily;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.model.TranLogVo;
import com.wizarpos.recode.print.base.PrintBase;

public class TotalCollectContent extends PrintBase {

    public static String printContent(TranLogVo tranLogVo) {

        String leftTitle = "Total Collected";
        String rightValue = ("$" + Tools.formatFen(tranLogVo.getTotalCollected()));
        if(isComputerSpaceForLeftRight()){
            return createTextLineForLeftAndRight(leftTitle, rightValue);
        }else {
            String str = "Total Collected" + multipleSpaces(16 - Tools.formatFen(tranLogVo.getTotalCollected()).length()) + ("$" + Tools.formatFen(tranLogVo.getTotalCollected()));
            str += formatForBr();
            return str;
        }
    }
}
