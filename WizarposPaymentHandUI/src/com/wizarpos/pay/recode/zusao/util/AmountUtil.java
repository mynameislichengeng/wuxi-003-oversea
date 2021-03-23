package com.wizarpos.pay.recode.zusao.util;

import java.math.BigDecimal;

public class AmountUtil {

    /**
     * 按保留2位小数来显示
     *
     * @param amount
     * @return
     */
    public static String showUi(String amount) {
        BigDecimal big = new BigDecimal(amount);
        String showAmount = big.setScale(2, BigDecimal.ROUND_UP).toString();
        String foramt = "$%s";
        return String.format(foramt, showAmount);

    }
}
