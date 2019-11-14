package com.wizarpos.pay.cashier.thrid_app_controller.model;

import com.wizarpos.pay.cashier.thrid_app_controller.model.base.ThirdResponse;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

public class ThirdAppLoginEXJsonResponse extends ThirdResponse {
	private String operatorNo;
	private String mid;
	private String fid;
	private String merchantId;
	private String terminalId;
	private String operatorId;
	private String merchantName;
	private String permission;
	private String operatorName;
	private String ip;
	private String port;
	private String saleDeductType;
	private String isOpenMemberDeduct;
	private String serverVersion;
	private String appPushId;
	private String sessionId;
	private String authFlag;
	private String unifiedLogin;

	public void setSaleDeductType(String saleDeductType) {
		this.saleDeductType = saleDeductType;
	}
	
	public String getSaleDeductType() {
		return saleDeductType;
	}
	
	public String getOperatorNo() {
		return operatorNo;
	}

	public void setOperatorNo(String operatorNo) {
		this.operatorNo = operatorNo;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getServerVersion() {
		return serverVersion;
	}

	public void setServerVersion(String serverVersion) {
		this.serverVersion = serverVersion;
	}

	public String getIsOpenMemberDeduct() {
		return isOpenMemberDeduct;
	}

	public void setIsOpenMemberDeduct(String isOpenMemberDeduct) {
		this.isOpenMemberDeduct = isOpenMemberDeduct;
	}

	public String getAppPushId() {
		return appPushId;
	}

	public void setAppPushId(String appPushId) {
		this.appPushId = appPushId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getAuthFlag() {
		return authFlag;
	}

	public void setAuthFlag(String authFlag) {
		this.authFlag = authFlag;
	}

	public String getUnifiedLogin() {
		return unifiedLogin;
	}

	public void setUnifiedLogin(String unifiedLogin) {
		this.unifiedLogin = unifiedLogin;
	}
}
