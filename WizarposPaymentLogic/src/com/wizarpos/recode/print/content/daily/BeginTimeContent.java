package com.wizarpos.recode.print.content.daily;

import com.wizarpos.pay.model.TranLogVo;
import com.wizarpos.recode.print.base.PrintBase;

public class BeginTimeContent extends PrintBase {

    public static String printContent(TranLogVo tranLogVo) {
        StringBuffer sb = new StringBuffer();
        if (isComputerSpaceForLeftRight()) {
            sb.append(formartForRight(tranLogVo.getBeginTime()));
            return sb.toString();
        } else {
            String beign = "" + multipleSpaces(32 - tranLogVo.getBeginTime().length()) + tranLogVo.getBeginTime();
            return beign + formatForBr();
        }
    }
}
