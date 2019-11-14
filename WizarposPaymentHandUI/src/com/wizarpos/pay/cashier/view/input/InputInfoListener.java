package com.wizarpos.pay.cashier.view.input;

/**
 * 输入回调
 * @author wu
 *
 */
public interface InputInfoListener {

	public static final int INPUT_TYPE_SWIPE = 1;
	public static final int INPUT_TYPE_CAMERA = 2;
	public static final int INPUT_TYPE_TEXT = 3;
	
	public void onGetInfo(String info, int infoType);
}
