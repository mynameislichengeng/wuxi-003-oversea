package com.wizarpos.pay.cashier.model;

import java.util.Date;

public class TicketInfo implements java.io.Serializable {
	private String id;
	private String ticketId;// 关联的TicketDef的 id; 这个值可能为空,必须直接取ticketDef 下的 id
	private String ticketNo;// 券号
	private String cardId; // (XXX 不是微信的cardId?)
	private Date startTime;// 启用时间
	private Date expriyTime;// 过期时间
	private String validFlag;// 是否有效(1有效 0无效)
	private Date cancelTime;// 注销时间
	private Date usedTime;
	private String masterTranLogId;// 关联支付交易流水
	private String remark;// 备注
	private String wxAdded;// 卡券是否已经添加到卡包（1：已被添加，0：未添加）
	private String mid;// 慧商户号
	private String hbShared;// 红包是否分享（1：分享，0：未分享）
	private String isFromScan = "false";// 是否是扫码添加

	private String ticketQrcode;// 如果这个字段有值,需要请求图片,然后将结果打印
	private String ticketQRCodeLocalPath;

	private TicketDef ticketDef; // 券定义

	public String endDate;// 券截止日期(微盟券用到) wu@[20150819]

	public String isWeiMengTicket;// 是否是微盟券  bug 后台返回的时 isWeiMengTicket wu@[20150819]

	private String merchantId;
	private Integer payId;
	private Integer cardType;
	private String cardNo;
	private String ticketDefId;
	private Integer ticketCode;
	private Date createTime;
	private String description;

	private String ticketName;
	private Integer balance;
	private Boolean reusedFlag;
	private Boolean usedFlag;
	private Integer validPeriod;
	private Integer activeAmount;
	private Integer usingBalance = 0;
	private String type;//判断券是否属于券包，0 不属于  1 属于

	public boolean usableFlag = true;
	public boolean invalidFlag = false;

	// public String giftCardId; //微盟券送券主键
	// public String giftCardCode;//微盟券核销对应code

	private String ids = "0";

	private int selectedCount; // 待发行数量,客户端私有变量 wu@[20150821]

	private int totalNum;

	// public String giftCardId; //微盟券送券主键
	// public String giftCardCode;//微盟券核销对应code

	private int availableNum=10000;// 可用券的张数 客户端私有变量,除bug[未传可用券的张数]
	private String displayCode;// 打印显示券号[@hong wemeng]

	private int realBalance ;//本地变量，用券实际金额
	
	/**  微盟返回的类型:1-券信息;2-url(需进行转成qrcode) {@link #url}*/
	private String weiMengType;
	private String url;
	
	/** {@link #weiMengType}*/
	public static String WEIMENT_TYPE_TICKET_INFO = "1";
	public static String WEIMENT_TYPE_URL = "2";
	
	public int getSelectedCount() {
		return selectedCount;
	}

	public void setSelectedCount(int selectedCount) {
		this.selectedCount = selectedCount;
	}

	public String getTicketQrcode() {
		return ticketQrcode;
	}

	public void setTicketQrcode(String ticketQrcode) {
		if(getTicketDef()!=null)
		{
			getTicketDef().setTicketQrCode(ticketQrcode);
		}
		this.ticketQrcode = ticketQrcode;
	}

	public TicketDef getTicketDef() {
		return ticketDef;
	}

	public void setTicketDef(TicketDef ticketDef) {
		this.ticketDef = ticketDef;
	}

	public String getTicketQRCodeLocalPath() {
		return ticketQRCodeLocalPath;
	}

	public void setTicketQRCodeLocalPath(String ticketQRCodeLocalPath) {
		this.ticketQRCodeLocalPath = ticketQRCodeLocalPath;
	}

	private String wx_code; // 微信卡的二维码对应的字符串(客户端本地变量,服务端没有该变量)

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getExpriyTime() {
		return expriyTime;
	}

	public void setExpriyTime(Date expriyTime) {
		this.expriyTime = expriyTime;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Date getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(Date usedTime) {
		this.usedTime = usedTime;
	}

	public String getMasterTranLogId() {
		return masterTranLogId;
	}

	public void setMasterTranLogId(String masterTranLogId) {
		this.masterTranLogId = masterTranLogId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getWxAdded() {
		return wxAdded;
	}

	public void setWxAdded(String wxAdded) {
		this.wxAdded = wxAdded;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getHbShared() {
		return hbShared;
	}

	public void setHbShared(String hbShared) {
		this.hbShared = hbShared;
	}

	public String getIsFromScan() {
		return isFromScan;
	}

	public void setIsFromScan(String isFromScan) {
		this.isFromScan = isFromScan;
	}

	public String getWx_code() {
		return wx_code;
	}

	public void setWx_code(String wx_code) {
		this.wx_code = wx_code;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getIsWeiMengTicket() {
		return isWeiMengTicket;
	}

	public void setIsWeiMengTicket(String isWeiMengTicket) {
		this.isWeiMengTicket = isWeiMengTicket;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getTicketDefId() {
		return ticketDefId;
	}

	public void setTicketDefId(String ticketDefId) {
		this.ticketDefId = ticketDefId;
	}

	public Integer getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(Integer ticketCode) {
		this.ticketCode = ticketCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTicketName() {
		return ticketName;
	}

	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Boolean getReusedFlag() {
		return reusedFlag;
	}

	public void setReusedFlag(Boolean reusedFlag) {
		this.reusedFlag = reusedFlag;
	}

	public Boolean getUsedFlag() {
		return usedFlag;
	}

	public void setUsedFlag(Boolean usedFlag) {
		this.usedFlag = usedFlag;
	}

	public Integer getValidPeriod() {
		return validPeriod;
	}

	public void setValidPeriod(Integer validPeriod) {
		this.validPeriod = validPeriod;
	}

	public Integer getActiveAmount() {
		return activeAmount;
	}

	public void setActiveAmount(Integer activeAmount) {
		this.activeAmount = activeAmount;
	}

	public Integer getUsingBalance() {
		return usingBalance;
	}

	public void setUsingBalance(Integer usingBalance) {
		this.usingBalance = usingBalance;
	}

	public boolean isUsableFlag() {
		return usableFlag;
	}

	public void setUsableFlag(boolean usableFlag) {
		this.usableFlag = usableFlag;
	}

	public boolean isInvalidFlag() {
		return invalidFlag;
	}

	public void setInvalidFlag(boolean invalidFlag) {
		this.invalidFlag = invalidFlag;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public String getDisplayCode() {
		return displayCode;
	}

	public void setDisplayCode(String displayCode) {
		this.displayCode = displayCode;
	}

	public int getAvailableNum() {
		return availableNum;
	}

	public void setAvailableNum(int availableNum) {
		this.availableNum = availableNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getRealBalance() {
		return realBalance;
	}

	public void setRealBalance(int realBalance) {
		this.realBalance = realBalance;
	}

	public String getIsWeimMngTicket() {
		return isWeiMengTicket;
	}

	public void setIsWeimMngTicket(String isWeimMngTicket) {
		this.isWeiMengTicket = isWeimMngTicket;
	}

	public String getWeiMengType() {
		return weiMengType;
	}

	public void setWeiMengType(String weiMengType) {
		this.weiMengType = weiMengType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
