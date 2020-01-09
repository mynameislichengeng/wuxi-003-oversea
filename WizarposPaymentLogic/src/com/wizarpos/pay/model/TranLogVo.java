package com.wizarpos.pay.model;

/**
 * 今日汇总接口返回
 *
 * @author wu at 2019-06-08
 */
public class TranLogVo {


    private String mid;

    private String beginTime;
    private String endTime;

    //净销售总笔数
    private int netSalesNumber;
    //净销售总金额
    private int netSalesAmount;
    //总收款金额
    private int totalCollected;
    //销售总笔数
    private int grossSalesNumber;
    //销售总金额，不包括小费
    private int grossSalesAmount;
    //退款总笔数
    private int refundsNumber;
    //退款总金额
    private int refundsAmount;
    //小费总笔费
    private int tipsNumber;
    //小费总金额
    private int tipsAmount;

    //支付宝销售总笔数
    private int alipaySalesNumber;
    //支付宝销售总金额
    private int alipaySalesAmount;
    //支付宝退款总笔数
    private int alipayRefundsNumber;
    //支付宝退款总金额
    private int alipayRefundsAmount;
    //支付宝净销售总笔数
    private int alipayNetSalesNumber;
    //支付宝净销售总金额
    private int alipayNetSalesAmount;
    //支付宝-新接口用的-饼图使用
    private int alipayGrossAmount;

    //微信销售总笔数
    private int wechatSalesNumber;
    //微信销售总金额
    private int wechatSalesAmount;
    //微信退款总笔数
    private int wechatRefundsNumber;
    //微信退款总金额
    private int wechatRefundsAmount;
    //微信净销售总笔数
    private int wechatNetSalesNumber;
    //微信净销售总金额
    private int wechatNetSalesAmount;
    //微信-新接口用的-饼图使用
    private int wechatGrossAmount;


    //银联支付宝销售总笔数
    private int unionPaySalesNumber;
    //银联支付销售总金额
    private int unionPaySalesAmount;
    //银联支付退款总笔数
    private int unionPayRefundsNumber;
    //银联支付退款总金额
    private int unionPayRefundsAmount;
    //银联支付净销售总笔数
    private int unionPayNetSalesNumber;
    //银联支付净销售总金额
    private int unionPayNetSalesAmount;
    //银联支付-新接口用的-饼图使用
    private int unionPayGrossAmount;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public int getNetSalesNumber() {
        return netSalesNumber;
    }

    public void setNetSalesNumber(int netSalesNumber) {
        this.netSalesNumber = netSalesNumber;
    }

    public int getNetSalesAmount() {
        return netSalesAmount;
    }

    public void setNetSalesAmount(int netSalesAmount) {
        this.netSalesAmount = netSalesAmount;
    }

    public int getTotalCollected() {
        return totalCollected;
    }

    public void setTotalCollected(int totalCollected) {
        this.totalCollected = totalCollected;
    }

    public int getGrossSalesNumber() {
        return grossSalesNumber;
    }

    public void setGrossSalesNumber(int grossSalesNumber) {
        this.grossSalesNumber = grossSalesNumber;
    }

    public int getGrossSalesAmount() {
        return grossSalesAmount;
    }

    public void setGrossSalesAmount(int grossSalesAmount) {
        this.grossSalesAmount = grossSalesAmount;
    }

    public int getRefundsNumber() {
        return refundsNumber;
    }

    public void setRefundsNumber(int refundsNumber) {
        this.refundsNumber = refundsNumber;
    }

    public int getRefundsAmount() {
        return refundsAmount;
    }

    public void setRefundsAmount(int refundsAmount) {
        this.refundsAmount = refundsAmount;
    }

    public int getTipsNumber() {
        return tipsNumber;
    }

    public void setTipsNumber(int tipsNumber) {
        this.tipsNumber = tipsNumber;
    }

    public int getTipsAmount() {
        return tipsAmount;
    }

    public void setTipsAmount(int tipsAmount) {
        this.tipsAmount = tipsAmount;
    }

    public int getAlipaySalesNumber() {
        return alipaySalesNumber;
    }

    public void setAlipaySalesNumber(int alipaySalesNumber) {
        this.alipaySalesNumber = alipaySalesNumber;
    }

    public int getAlipaySalesAmount() {
        return alipaySalesAmount;
    }

    public void setAlipaySalesAmount(int alipaySalesAmount) {
        this.alipaySalesAmount = alipaySalesAmount;
    }

    public int getAlipayRefundsNumber() {
        return alipayRefundsNumber;
    }

    public void setAlipayRefundsNumber(int alipayRefundsNumber) {
        this.alipayRefundsNumber = alipayRefundsNumber;
    }

    public int getAlipayRefundsAmount() {
        return alipayRefundsAmount;
    }

    public void setAlipayRefundsAmount(int alipayRefundsAmount) {
        this.alipayRefundsAmount = alipayRefundsAmount;
    }

    public int getAlipayNetSalesNumber() {
        return alipayNetSalesNumber;
    }

    public void setAlipayNetSalesNumber(int alipayNetSalesNumber) {
        this.alipayNetSalesNumber = alipayNetSalesNumber;
    }

    public int getAlipayNetSalesAmount() {
        return alipayNetSalesAmount;
    }

    public void setAlipayNetSalesAmount(int alipayNetSalesAmount) {
        this.alipayNetSalesAmount = alipayNetSalesAmount;
    }

    public int getWechatSalesNumber() {
        return wechatSalesNumber;
    }

    public void setWechatSalesNumber(int wechatSalesNumber) {
        this.wechatSalesNumber = wechatSalesNumber;
    }

    public int getWechatSalesAmount() {
        return wechatSalesAmount;
    }

    public void setWechatSalesAmount(int wechatSalesAmount) {
        this.wechatSalesAmount = wechatSalesAmount;
    }

    public int getWechatRefundsNumber() {
        return wechatRefundsNumber;
    }

    public void setWechatRefundsNumber(int wechatRefundsNumber) {
        this.wechatRefundsNumber = wechatRefundsNumber;
    }

    public int getWechatRefundsAmount() {
        return wechatRefundsAmount;
    }

    public void setWechatRefundsAmount(int wechatRefundsAmount) {
        this.wechatRefundsAmount = wechatRefundsAmount;
    }

    public int getWechatNetSalesNumber() {
        return wechatNetSalesNumber;
    }

    public void setWechatNetSalesNumber(int wechatNetSalesNumber) {
        this.wechatNetSalesNumber = wechatNetSalesNumber;
    }

    public int getWechatNetSalesAmount() {
        return wechatNetSalesAmount;
    }

    public void setWechatNetSalesAmount(int wechatNetSalesAmount) {
        this.wechatNetSalesAmount = wechatNetSalesAmount;
    }

    public int getUnionPaySalesNumber() {
        return unionPaySalesNumber;
    }

    public void setUnionPaySalesNumber(int unionPaySalesNumber) {
        this.unionPaySalesNumber = unionPaySalesNumber;
    }

    public int getUnionPaySalesAmount() {
        return unionPaySalesAmount;
    }

    public void setUnionPaySalesAmount(int unionPaySalesAmount) {
        this.unionPaySalesAmount = unionPaySalesAmount;
    }

    public int getUnionPayRefundsNumber() {
        return unionPayRefundsNumber;
    }

    public void setUnionPayRefundsNumber(int unionPayRefundsNumber) {
        this.unionPayRefundsNumber = unionPayRefundsNumber;
    }

    public int getUnionPayRefundsAmount() {
        return unionPayRefundsAmount;
    }

    public void setUnionPayRefundsAmount(int unionPayRefundsAmount) {
        this.unionPayRefundsAmount = unionPayRefundsAmount;
    }

    public int getUnionPayNetSalesNumber() {
        return unionPayNetSalesNumber;
    }

    public void setUnionPayNetSalesNumber(int unionPayNetSalesNumber) {
        this.unionPayNetSalesNumber = unionPayNetSalesNumber;
    }

    public int getUnionPayNetSalesAmount() {
        return unionPayNetSalesAmount;
    }

    public void setUnionPayNetSalesAmount(int unionPayNetSalesAmount) {
        this.unionPayNetSalesAmount = unionPayNetSalesAmount;
    }

    public int getAlipayGrossAmount() {
        return alipayGrossAmount;
    }

    public void setAlipayGrossAmount(int alipayGrossAmount) {
        this.alipayGrossAmount = alipayGrossAmount;
    }

    public int getWechatGrossAmount() {
        return wechatGrossAmount;
    }

    public void setWechatGrossAmount(int wechatGrossAmount) {
        this.wechatGrossAmount = wechatGrossAmount;
    }

    public int getUnionPayGrossAmount() {
        return unionPayGrossAmount;
    }

    public void setUnionPayGrossAmount(int unionPayGrossAmount) {
        this.unionPayGrossAmount = unionPayGrossAmount;
    }
}