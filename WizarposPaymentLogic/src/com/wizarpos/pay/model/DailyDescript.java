package com.wizarpos.pay.model;

import java.io.Serializable;

/**
 * 日结单返回实体描述
 */
public class DailyDescript implements Serializable {
    private long tranAmountCount;
    private long tranAmountSum;
    private String tranCode;
    private String tranCodeDesc;

    public long getTranAmountCount() {
        return tranAmountCount;
    }

    public void setTranAmountCount(long tranAmountCount) {
        this.tranAmountCount = tranAmountCount;
    }

    public long getTranAmountSum() {
        return tranAmountSum;
    }

    public void setTranAmountSum(long tranAmountSum) {
        this.tranAmountSum = tranAmountSum;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public String getTranCodeDesc() {
        return tranCodeDesc;
    }

    public void setTranCodeDesc(String tranCodeDesc) {
        this.tranCodeDesc = tranCodeDesc;
    }
}
