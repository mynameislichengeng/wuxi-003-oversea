package com.wizarpos.pay.cashier.model;

import java.util.Date;
import java.util.List;
import android.text.TextUtils;

public class TicketDef implements java.io.Serializable {
	private static final long serialVersionUID = -5430740859208509767L;
	private String id;
	private String mid;// 商户号
	private String merchantId;
	private Integer payId;
	private String ticketCode;
	private String ticketName;
	private Integer balance;
	private Boolean reusedFlag;
	private Boolean usedFlag;
	private Integer validPeriod;
	private Integer activeAmount;
	private Date createTime;
	private String description;
	private String wxFlag;
	/** 是否是卡包 */
	protected boolean isPacket = false;
	/** 卡包子元素 */
	private List<TicketPacketInfo> packInfo;
	/** 与{@link #balance}类似,在第三方券支付的时候服务端返回 
	 *  与{@link #balance}区别于(仅第三方支付时): 比如 券为10.00元 应付5.00元 balance返回为 499 reduceCodt返回为1000 @author hwc
	 * */
	private Long reduceCost;

	private String endDate;

	/**
	 * 券类型: 0 代金券 1折扣券 2礼品券 3团购券 4优惠券5.通用券6.商户红包7微信会员卡 (3~7为微信)
	 */
	private String ticketType;
	/**
	 * 券类型名称
	 */
	private String ticketTypeName;

	private String discount; // 折扣金额
	private long endTimestamp;//有效时间
	
	private String endTimestampStr;//有效时间str
	
	/** 券类型 折扣券 与 {@link #ticketType} 相对应*/
	public static final String TICKET_TYPE_DISCOUNT = "1";
	public static final String TICKET_TYPE_GIFT = "2";//礼品券
	public static final String TICKET_TYPE_WEPAY = "7";//微信会员卡
	
	/**
	 * 券唯一性ID
	 */
	private String ticketId;
	
	/**	礼品 礼品券专用	**/
	private String gift;

	/**	折扣 折扣券专用	**/
	private Integer hyDiscount; 
	
	
	/** 券描述 不是服务端返回 */
	private String ticketDescription;
	
	/** 微信会员卡 返回 需要打印成二维码*/
	private String ticketQrCode;

	
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMerchantId() {
		return this.merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getTicketCode() {
		return this.ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public String getTicketName() {
		return this.ticketName;
	}

	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}

	public Integer getBalance() {
		return this.balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Boolean getReusedFlag() {
		return this.reusedFlag;
	}

	public void setReusedFlag(Boolean reusedFlag) {
		this.reusedFlag = reusedFlag;
	}

	public Boolean getUsedFlag() {
		return this.usedFlag;
	}

	public void setUsedFlag(Boolean usedFlag) {
		this.usedFlag = usedFlag;
	}

	public Integer getValidPeriod() {
		return this.validPeriod;
	}

	public void setValidPeriod(Integer validPeriod) {
		this.validPeriod = validPeriod;
	}

	public Integer getActiveAmount() {
		return this.activeAmount;
	}

	public void setActiveAmount(Integer activeAmount) {
		this.activeAmount = activeAmount;
	}

	public Date getCreateTime() {
		return this.createTime;
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

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public String getWxFlag() {
		return wxFlag;
	}

	public void setWxFlag(String wxFlag) {
		this.wxFlag = wxFlag;
	}

	public boolean isPacket() {
		return isPacket;
	}

	public void setPacket(boolean isPacket) {
		this.isPacket = isPacket;
	}

	public List<TicketPacketInfo> getPackInfo() {
		return packInfo;
	}

	public void setPackInfo(List<TicketPacketInfo> packInfo) {
		this.packInfo = packInfo;
	}

	public String getTicketType() {
		return TextUtils.isEmpty(ticketType)?"0":ticketType;//若无类型默认为代金券
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public String getTicketTypeName() {
		return ticketTypeName;
	}

	public void setTicketTypeName(String ticketTypeName) {
		this.ticketTypeName = ticketTypeName;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public long getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
		if(endTimestamp > 0)
		{
			this.endTimestampStr = com.wizarpos.log.util.DateUtil.longToString(endTimestamp) + "";
		}else if(endTimestamp == -1){
			//若服务端endTimestamp的值为-1 则为永久有效
			this.endTimestampStr = "永久有效";
		}
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getEndTimestampStr() {
		return endTimestampStr;
	}

	public void setEndTimestampStr(String endTimestampStr) {
		this.endTimestampStr = endTimestampStr;
	}
	
	public String getGift() {
		return gift;
	}

	public void setGift(String gift) {
		this.gift = gift;
	}

	public Integer getHyDiscount() {
		return hyDiscount;
	}

	public void setHyDiscount(Integer hyDiscount) {
		if(hyDiscount != null && hyDiscount > 0)
		{//若有折扣
			String des = (100 - hyDiscount) + "%";
			setTicketDescription(des);
		}
		this.hyDiscount = hyDiscount;
	}

	public Long getReduceCost() {
		return reduceCost;
	}

	public void setReduceCost(Long reduceCost) {
		this.reduceCost = reduceCost;
	}

	public String getTicketDescription() {
		return ticketDescription;
	}

	public void setTicketDescription(String ticketDescription) {
		this.ticketDescription = ticketDescription;
	}

	public String getTicketQrCode() {
		return ticketQrCode;
	}

	public void setTicketQrCode(String ticketQrCode) {
		this.ticketQrCode = ticketQrCode;
	}
	

	public static String getTicketTypeNameByTicketType(String ticketType){
		//0 代金券 1折扣券 2礼品券 3团购券 4优惠券5.通用券6.商户红包7微信会员卡 (3~7为微信)
		if(TextUtils.isEmpty(ticketType)){
			return "";
		}else if("0".equals(ticketType)){
			return "代金券";
		}else if("1".equals(ticketType)){
			return "折扣券";
		}else if("2".equals(ticketType)){
			return "礼品券";
		}else if("3".equals(ticketType)){
			return "团购券";
		}else if("4".equals(ticketType)){
			return "优惠券";
		}else if("5".equals(ticketType)){
			return "通用券";
		}else if("6".equals(ticketType)){
			return "商户红包券";
		}else if("7".equals(ticketType)){
			return "微信会员卡券";
		}else{
			return "";
		}
	}
	
}
