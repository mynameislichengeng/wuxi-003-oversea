package com.wizarpos.recode.print.content.daily;

import com.wizarpos.pay.model.TranLogVo;
import com.wizarpos.recode.print.base.PrintBase;

public class EndTimeContent extends PrintBase {

    public static String printContent(TranLogVo tranLogVo) {
        StringBuffer sb = new StringBuffer();
        if (isComputerSpaceForLeftRight()) {
            sb.append(formartForRight(tranLogVo.getEndTime()));
//            sb.append(formartForLineSpace());
            return sb.toString();
        } else {
            String beign = "" + multipleSpaces(32 - tranLogVo.getEndTime().length()) + tranLogVo.getEndTime();
            return beign + formatForBr();
        }
    }
}
