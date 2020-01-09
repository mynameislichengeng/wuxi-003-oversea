package com.wizarpos.pay.model;

import android.text.TextUtils;

import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.pay_tems.bat.entities.BatWeimobTicket;

import java.io.Serializable;
import java.util.List;

/**
 * 交易描述 Created by wu on 2015/6/26 0026.
 */
public class TransactionInfo implements Serializable {

    protected int transactionType;// 交易类型
    protected String tranId;// 订单号 (线上支付订单号)
    protected String transTime;// 付款时间
    protected String giftPoints;// 赠送积分
    protected boolean isOffline;// 是否是离线模式
    protected String tranLogId = "";// 流水号
    protected String body;// 商品信息
    protected boolean isNeedPrint;// 是否需要打印

    protected boolean isNeedTicket;// 是否需要发券

    // 金额精确到分
    protected String initAmount = "0";// 初始金额 (传入金额)
    protected String shouldAmount = "0";// 应付金额
    protected String realAmount = "0";// 实付金额
    protected String reduceAmount = "0";// 扣减金额
    protected String changeAmount = "0";// 找零金额
    protected String discountAmount = "0";// 折扣扣减金额
    protected String mixInitAmount = "0";// 混合支付总金额
    protected String ticketTotalAomount = "0";// 券总计金额

    protected String authCode;// 交易特征码(线上支付,被扫支付)
    protected String thirdTradeNo;// 第三方交易号

    protected String thirdExtId;
    protected String thirdExtName;
    private String cnyAmount;//服务端返回交易的人民币金额

    protected String cardNo;
    protected String rechargeOn;
    protected String isPayComingForm;
    protected String mixFlag;
    protected String mixTranLogId;
    protected String payTypeFlag;//线上支付类型
    private String payType;//移动支付的支付类型 wu
    private String transCurrency;
    private String sn;
    private String optName;
    private String settlementAmount;
    private String settlementCurrency;

    protected List<TicketInfo> tList;//线上支付发行的卡券
    protected String commoncashierOrderId;//销售单Id wu
    protected BatWeimobTicket batTicket;//第三方微盟券使用对象

    protected boolean isFirstTransaction;//是否是第一笔交易

    private List<String> ids;//第三方支付支持用券
    private String saleInputAmount;//销售调用收款传值
//	public void setMasterTranLogId(String masterTranLogId) {
//		this.masterTranLogId = masterTranLogId;
//	}
    /**
     * 营销活动一期 返回的券限制
     **/
    protected String ticketIds = "";
    /**
     * 营销活动一期 营销原价
     **/
    protected String marketOriginalPrice = "";
    private String tips;//小费
    private String exchangeRate;//获取实时费率
    private String payTime;//获取交易时间

    /**
     * 获取移动支付的支付类型
     *
     * @return 支付类型
     * @auther wu
     */
    public String getPayTypeDes() {
        if (TextUtils.isEmpty(this.payType)) {
            return "";
        }
        final String payTypeUpCase = this.payType.toUpperCase();
        if ("B".equals(payTypeUpCase)) {
            return "百度钱包";
        } else if ("A".equals(payTypeUpCase)) {
            return "支付宝";
        } else if ("W".equals(payTypeUpCase)) {
            return " 微信";
        } else if ("T".equals(payTypeUpCase)) {
            return "QQ钱包";
        } else {
            return "";
        }
    }

    public boolean isFirstTransaction() {
        return isFirstTransaction;
    }

    public void setFirstTransaction(boolean isFirstTransaction) {
        this.isFirstTransaction = isFirstTransaction;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getMixTranLogId() {
        return mixTranLogId;
    }

    public void setMixTranLogId(String mixTranLogId) {
        this.mixTranLogId = mixTranLogId;
    }

    public String getThirdTradeNo() {
        return thirdTradeNo;
    }

    public void setThirdTradeNo(String thirdTradeNo) {
        this.thirdTradeNo = thirdTradeNo;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getGiftPoints() {
        return giftPoints;
    }

    public void setGiftPoints(String giftPoints) {
        this.giftPoints = giftPoints;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public void setIsOffline(boolean isOffline) {
        this.isOffline = isOffline;
    }

    public String getTranLogId() {
        return tranLogId;
    }

    public void setTranLogId(String tranLogId) {
        this.tranLogId = tranLogId;
    }

    public String getInitAmount() {
        return initAmount;
    }

    public void setInitAmount(String initAmount) {
        this.initAmount = initAmount;
    }

    public String getShouldAmount() {
        return shouldAmount;
    }

    public void setShouldAmount(String shouldAmount) {
        this.shouldAmount = shouldAmount;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

    public String getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(String reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public String getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(String changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isNeedPrint() {
        return isNeedPrint;
    }

    public void setOffline(boolean isOffline) {
        this.isOffline = isOffline;
    }

    public void setIsNeedPrint(boolean isNeedPrint) {
        this.isNeedPrint = isNeedPrint;
    }

    public boolean isNeedTicket() {
        return isNeedTicket;
    }

    public void setNeedTicket(boolean isNeedTicket) {
        this.isNeedTicket = isNeedTicket;
    }

    public void setNeedPrint(boolean isNeedPrint) {
        this.isNeedPrint = isNeedPrint;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getRechargeOn() {
        return rechargeOn;
    }

    public void setRechargeOn(String rechargeOn) {
        this.rechargeOn = rechargeOn;
    }

    public String getIsPayComingForm() {
        return isPayComingForm;
    }

    public void setIsPayComingForm(String isPayComingForm) {
        this.isPayComingForm = isPayComingForm;
    }

    public String getMixFlag() {
        return mixFlag;
    }

    public void setMixFlag(String mixFlag) {
        this.mixFlag = mixFlag;
    }

    public String getMixInitAmount() {
        return mixInitAmount;
    }

    public void setMixInitAmount(String mixInitAmount) {
        this.mixInitAmount = mixInitAmount;
    }

    public String getPayTypeFlag() {
        return payTypeFlag;
    }

    public void setPayTypeFlag(String payTypeFlag) {
        this.payTypeFlag = payTypeFlag;
    }

    public List<TicketInfo> gettList() {
        return tList;
    }

    public void settList(List<TicketInfo> tList) {
        this.tList = tList;
    }

    public void setCommoncashierOrderId(String commoncashierOrderId) {
        this.commoncashierOrderId = commoncashierOrderId;
    }

    public String getCommoncashierOrderId() {
        return commoncashierOrderId;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }


    public String getTicketIds() {
        return ticketIds;
    }

    public void setTicketIds(String ticketIds) {
        this.ticketIds = ticketIds;
    }

    public String getMarketOriginalPrice() {
        return marketOriginalPrice;
    }

    public void setMarketOriginalPrice(String marketOriginalPrice) {
        this.marketOriginalPrice = marketOriginalPrice;
    }

    public String getTicketTotalAomount() {
        if (ticketTotalAomount == null) {
            ticketTotalAomount = "0";
        }
        return ticketTotalAomount;
    }

    public void setTicketTotalAomount(String ticketTotalAomount) {
        this.ticketTotalAomount = ticketTotalAomount;
    }

    public String getSaleInputAmount() {
        return saleInputAmount;
    }

    public void setSaleInputAmount(String saleInputAmount) {
        this.saleInputAmount = saleInputAmount;
    }

    public BatWeimobTicket getBatTicket() {
        return batTicket;
    }

    public void setBatTicket(BatWeimobTicket batTicket) {
        this.batTicket = batTicket;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getThirdExtId() {
        return thirdExtId;
    }

    public void setThirdExtId(String thirdExtId) {
        this.thirdExtId = thirdExtId;
    }

    public String getThirdExtName() {
        return thirdExtName;
    }

    public void setThirdExtName(String thirdExtName) {
        this.thirdExtName = thirdExtName;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getCnyAmount() {
        return cnyAmount;
    }

    public void setCnyAmount(String cnyAmount) {
        this.cnyAmount = cnyAmount;
    }

    public String getTransCurrency() {
        return transCurrency;
    }

    public void setTransCurrency(String transCurrency) {
        this.transCurrency = transCurrency;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(String settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public String getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(String settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }
}

