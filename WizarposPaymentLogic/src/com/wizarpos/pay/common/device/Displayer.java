package com.wizarpos.pay.common.device;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * 客显接口
 * 
 * @author wu
 */
public interface Displayer {

	void display(Bitmap bitmap);

	void display(HashMap<String, String> dismap);
}
