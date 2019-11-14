package com.wizarpos.pay.db;

import com.lidroid.xutils.db.annotation.Table;

@Table(name = "merchant_card_def")
public class MerchantCardDef {
	private String id;
	private int mid;
	private String merchantId;
	private int payId;
	private int cardType;
	private String cardName;
	private String acceptedMedia;
	private int publishElecCard;
	private int valid;
	private int validPeriod;
	private int publishTicket;
	private long createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public int getPayId() {
		return payId;
	}

	public void setPayId(int payId) {
		this.payId = payId;
	}

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getAcceptedMedia() {
		return acceptedMedia;
	}

	public void setAcceptedMedia(String acceptedMedia) {
		this.acceptedMedia = acceptedMedia;
	}

	public int getPublishElecCard() {
		return publishElecCard;
	}

	public void setPublishElecCard(int publishElecCard) {
		this.publishElecCard = publishElecCard;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public int getValidPeriod() {
		return validPeriod;
	}

	public void setValidPeriod(int validPeriod) {
		this.validPeriod = validPeriod;
	}

	public int getPublishTicket() {
		return publishTicket;
	}

	public void setPublishTicket(int publishTicket) {
		this.publishTicket = publishTicket;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
