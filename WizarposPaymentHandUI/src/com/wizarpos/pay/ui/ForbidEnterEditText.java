package com.wizarpos.pay.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * 
 * @Author: Huangweicai
 * @date 2016-1-18 上午11:36:02
 * @Description:禁止确定键
 */
public class ForbidEnterEditText extends EditText{

	public ForbidEnterEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public ForbidEnterEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ForbidEnterEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ForbidEnterEditText(Context context) {
		super(context);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_ENTER) 
		{
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
