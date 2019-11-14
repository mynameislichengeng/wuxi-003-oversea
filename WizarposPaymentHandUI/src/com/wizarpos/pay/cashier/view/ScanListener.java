package com.wizarpos.pay.cashier.view;

/**
 * 扫描监听
 * 
 * @author wu
 */
public interface ScanListener {
	/**
	 * 回调
	 * 
	 * @param result
	 *            卡号
	 * @param isWepayMemberCard
	 *            是否是微信会员卡
	 */
	public void onScan(String result, boolean isWepayMemberCard);
}
