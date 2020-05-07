package com.wizarpos.recode.print.content.daily;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.model.TranLogVo;
import com.wizarpos.recode.print.base.PrintBase;

public class TipDialyContent  extends PrintBase {

    public static String printContent(TranLogVo tranLogVo) {

        String leftTile = "Tips x " + tranLogVo.getTipsNumber() ;
        String rightValue =  ("$" + Tools.formatFen(tranLogVo.getTipsAmount())) ;

        if (isComputerSpaceForLeftRight()) {
            return createTextLineForLeftAndRight(leftTile, rightValue);
        } else {


            String printString = leftTile + multipleSpaces(31 - (("Tips x " + tranLogVo.getTipsNumber()) + Tools.formatFen(tranLogVo.getTipsAmount())).length()) + rightValue;

            printString += formatForBr();
            return printString;
        }
    }
}
