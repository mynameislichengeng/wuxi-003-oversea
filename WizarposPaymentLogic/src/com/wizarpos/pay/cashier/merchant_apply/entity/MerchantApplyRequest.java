package com.wizarpos.pay.cashier.merchant_apply.entity;

import java.io.Serializable;

/**
 *
 * @Author: Huangweicai
 * @date 2015-11-26 下午3:51:55
 * @Description:商户进件注册bean
 */
public class MerchantApplyRequest implements Serializable{
	// 商户经营名称(商户名称)
	private String merchantManagementName;
	// 商户简称
	private String merchantShortName;
	// 业务联系人
	private String bizLinker;
	// 业务联系人邮箱
	private String bizEmail;
	// 业务联系人电话
	private String bizTel;
	//第三方支付范围(A支付宝,W 微信,Q  qq钱包,B 百度钱包),多个则以JSONARRAY对象存储
	private String thirdPayRange;
	//第三方支付商户类型(1 自有参数 2正常申请第三方支付 3 平台代收)
	private String thirdPayType;
	// 商户注册名称(账户名称)
	private String merchantRegisterName;
	// 开户银行
	private String openBankAccount;
	// 开户行行号
	private String openBankNo;
	// 营业执照 (OSS路径)
	private String bizLicenseImgUrl;
	// 法人身份证(正) (OSS路径)
	private String corpIdentAbvImgUrl;
	// 法人身份证(反) (OSS路径)
	private String corpIdentObvImgUrl;
	private String merchantProportion;// 商户营业地址
	// 商户进件标示 1:用于商户进件 
	private String MerchantBankApplyFlag = "1";

	private String imei;
	//申请来源(1代理商,2手机APP)
	private String applySource = "2";


	public String getMerchantShortName() {
		return merchantShortName;
	}
	public void setMerchantShortName(String merchantShortName) {
		this.merchantShortName = merchantShortName;
	}
	public String getMerchantManagementName() {
		return merchantManagementName;
	}
	public void setMerchantManagementName(String merchantManagementName) {
		this.merchantManagementName = merchantManagementName;
	}
	public String getBizLinker() {
		return bizLinker;
	}
	public void setBizLinker(String bizLinker) {
		this.bizLinker = bizLinker;
	}
	public String getBizEmail() {
		return bizEmail;
	}
	public void setBizEmail(String bizEmail) {
		this.bizEmail = bizEmail;
	}
	public String getBizTel() {
		return bizTel;
	}
	public void setBizTel(String bizTel) {
		this.bizTel = bizTel;
	}
	public String getThirdPayType() {
		return thirdPayType;
	}
	public void setThirdPayType(String thirdPayType) {
		this.thirdPayType = thirdPayType;
	}
	public String getMerchantRegisterName() {
		return merchantRegisterName;
	}
	public void setMerchantRegisterName(String merchantRegisterName) {
		this.merchantRegisterName = merchantRegisterName;
	}
	public String getOpenBankAccount() {
		return openBankAccount;
	}
	public void setOpenBankAccount(String openBankAccount) {
		this.openBankAccount = openBankAccount;
	}
	public String getOpenBankNo() {
		return openBankNo;
	}
	public void setOpenBankNo(String openBankNo) {
		this.openBankNo = openBankNo;
	}
	public String getBizLicenseImgUrl() {
		return bizLicenseImgUrl;
	}
	public void setBizLicenseImgUrl(String bizLicenseImgUrl) {
		this.bizLicenseImgUrl = bizLicenseImgUrl;
	}
	public String getCorpIdentAbvImgUrl() {
		return corpIdentAbvImgUrl;
	}
	public void setCorpIdentAbvImgUrl(String corpIdentAbvImgUrl) {
		this.corpIdentAbvImgUrl = corpIdentAbvImgUrl;
	}
	public String getCorpIdentObvImgUrl() {
		return corpIdentObvImgUrl;
	}
	public void setCorpIdentObvImgUrl(String corpIdentObvImgUrl) {
		this.corpIdentObvImgUrl = corpIdentObvImgUrl;
	}
	public String getThirdPayRange() {
		return thirdPayRange;
	}
	public void setThirdPayRange(String thirdPayRange) {
		this.thirdPayRange = thirdPayRange;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getApplySource() {
		return applySource;
	}
	public String getMerchantBankApplyFlag() {
		return MerchantBankApplyFlag;
	}
	public String getMerchantProportion() {
		return merchantProportion;
	}
	public void setMerchantProportion(String merchantProportion) {
		this.merchantProportion = merchantProportion;
	}

}
