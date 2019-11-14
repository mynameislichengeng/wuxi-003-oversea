package com.wizarpos.pay.model;

import java.io.Serializable;

/**
 * 收单返回数据
 * Created by blue_sky on 2016/5/6.
 */
public class SendTransInfo implements Serializable {
    //商户号、终端号、批次号、凭证号、参考号、日期、金额
   private String mid;//商户号
   private String tid;//终端号
   private int batchNumber;//批次号
   private int trace;//凭证号
   private String rrn;//参考号
   private String expiryDate;//日期
   private int transAmount;//金额
   private String cardNo;//卡号

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public int getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(int batchNumber) {
        this.batchNumber = batchNumber;
    }

    public int getTrace() {
        return trace;
    }

    public void setTrace(int trace) {
        this.trace = trace;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(int transAmount) {
        this.transAmount = transAmount;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}
