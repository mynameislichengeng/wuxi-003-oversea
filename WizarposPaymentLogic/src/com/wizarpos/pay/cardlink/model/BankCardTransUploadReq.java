package com.wizarpos.pay.cardlink.model;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
import com.wizarpos.pay.common.utils.BeanToMapUtil;
import com.wizarpos.pay.model.SendTransInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Song
 *         银行卡交易成功后上送请求
 */
@Table(name = "bank_card_tran_upload")
public class BankCardTransUploadReq implements Serializable {

    @Id
    @NoAutoIncrement
    private String token;
    private String payAmount;
    private String extraAmount;
    private String inputAmount;
    private String issueTicketMode;
    private String cardName = "银行卡";
    private String cardType = "1";
    private String cardNo = "";
    private String bankCardNo;
    private String rechargeOn;
    private String masterPayAmount;
    private String amount;
    private String ticketNo;
    private List<String> ids;
    private String masterTranLogId;
    private int mixFlag;
    private String isPayComingForm;
    private String transInfo;
    private String bankOrderNo;
    private String referCode;
    private String bankTerminalNo;
    private String bankMid;

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getExtraAmount() {
        return extraAmount;
    }

    public void setExtraAmount(String extraAmount) {
        this.extraAmount = extraAmount;
    }

    public String getInputAmount() {
        return inputAmount;
    }

    public void setInputAmount(String inputAmount) {
        this.inputAmount = inputAmount;
    }

    public String getIssueTicketMode() {
        return issueTicketMode;
    }

    public void setIssueTicketMode(String issueTicketMode) {
        this.issueTicketMode = issueTicketMode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMasterPayAmount() {
        return masterPayAmount;
    }

    public void setMasterPayAmount(String masterPayAmount) {
        this.masterPayAmount = masterPayAmount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getMasterTranLogId() {
        return masterTranLogId;
    }

    public void setMasterTranLogId(String masterTranLogId) {
        this.masterTranLogId = masterTranLogId;
    }

    public int getMixFlag() {
        return mixFlag;
    }

    public void setMixFlag(int mixFlag) {
        this.mixFlag = mixFlag;
    }

    public String getIsPayComingForm() {
        return isPayComingForm;
    }

    public void setIsPayComingForm(String isPayComingForm) {
        this.isPayComingForm = isPayComingForm;
    }

    public String getTransInfo() {
        return transInfo;
    }

    public void setTransInfo(String transInfo) {
        this.transInfo = transInfo;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public void setBankOrderNo(String bankOrderNo) {
        this.bankOrderNo = bankOrderNo;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public String getBankTerminalNo() {
        return bankTerminalNo;
    }

    public void setBankTerminalNo(String bankTerminalNo) {
        this.bankTerminalNo = bankTerminalNo;
    }

    public String getBankMid() {
        return bankMid;
    }

    public void setBankMid(String bankMid) {
        this.bankMid = bankMid;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public Map<String, Object> getUploadMap() {
        try {
            Map<String, Object> params = BeanToMapUtil.objectToMap(this);
            params.put("transInfo", JSONObject.parseObject(this.transInfo, SendTransInfo.class));
            return params;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
