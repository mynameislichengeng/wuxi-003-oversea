package com.wizarpos.pay.model;

import java.io.Serializable;
import java.util.List;

public class LoginResp implements Serializable {

    private long currentTime;
    private MerchantInfo merchant;
    private Agent agentMerchant;//代理商 wu
    private boolean merchant_has_weixin;
    private long symTime;
    private List<SysParam> sysParams;
    private long sysPosTimeStamp;
    private TerminalInfo terminal;
    private String merchantType; //商户类型 wu@[20150824]
    private String alipay;//支付宝是否开启@hong
    private String baidu;//百付宝是否开启@hong
    private String qq;//QQ钱包是否开启@hong
    private String weixin;//微信是否开启@hong
    private String mechantId;
    private String terminalId;
    private String serverIP;
    private String serverPort;
    private String authCode;
    private String tpddu;
    private MerchantDefSuffix merchantDefSuffix;


	private List<MerchantLabelEntity> merchantLabel ;
	private UserEntity muser;
	private List<UserEntity> userList ;
	private String sessionId;

    private List<String> refundRelationMids;
	
    public List<MerchantLabelEntity> getMerchantLabel() {
		return merchantLabel;
	}

	public void setMerchantLabel(List<MerchantLabelEntity> merchantLabel) {
		this.merchantLabel = merchantLabel;
	}

	public UserEntity getMuser() {
		return muser;
	}

	public void setMuser(UserEntity muser) {
		this.muser = muser;
	}

	public List<UserEntity> getUserList() {
		return userList;
	}

	public void setUserList(List<UserEntity> userList) {
		this.userList = userList;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Agent getAgentMerchant() {
        return agentMerchant;
    }

    public void setAgentMerchant(Agent agentMerchant) {
        this.agentMerchant = agentMerchant;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public MerchantInfo getMerchant() {
        return merchant;
    }

    public void setMerchant(MerchantInfo merchant) {
        this.merchant = merchant;
    }

    public boolean isMerchant_has_weixin() {
        return merchant_has_weixin;
    }

    public void setMerchant_has_weixin(boolean merchant_has_weixin) {
        this.merchant_has_weixin = merchant_has_weixin;
    }

    public long getSymTime() {
        return symTime;
    }

    public void setSymTime(long symTime) {
        this.symTime = symTime;
    }

    public List<SysParam> getSysParams() {
        return sysParams;
    }

    public void setSysParams(List<SysParam> sysParams) {
        this.sysParams = sysParams;
    }

    public long getSysPosTimeStamp() {
        return sysPosTimeStamp;
    }

    public void setSysPosTimeStamp(long sysPosTimeStamp) {
        this.sysPosTimeStamp = sysPosTimeStamp;
    }

    public TerminalInfo getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalInfo terminal) {
        this.terminal = terminal;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getBaidu() {
        return baidu;
    }

    public void setBaidu(String baidu) {
        this.baidu = baidu;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getTpddu() {
        return tpddu;
    }

    public void setTpddu(String tpddu) {
        this.tpddu = tpddu;
    }

    public String getMechantId() {
        return mechantId;
    }

    public void setMechantId(String mechantId) {
        this.mechantId = mechantId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public MerchantDefSuffix getMerchantDefSuffix() {
        return merchantDefSuffix;
    }

    public void setMerchantDefSuffix(MerchantDefSuffix merchantDefSuffix) {
        this.merchantDefSuffix = merchantDefSuffix;
    }

    public List<String> getRefundRelationMids() {
        return refundRelationMids;
    }

    public void setRefundRelationMids(List<String> refundRelationMids) {
        this.refundRelationMids = refundRelationMids;
    }
}
