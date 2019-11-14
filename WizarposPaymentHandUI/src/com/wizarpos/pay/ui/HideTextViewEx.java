package com.wizarpos.pay.ui;

import java.util.Arrays;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 
 * @Author: Huangweicai
 * @date 2016-3-21 下午6:15:32
 * @Description:隐藏部分字符 目前用于隐藏银行卡号 例: 卡号 8877878783 ---> 8877***783
 */
public class HideTextViewEx extends TextView{
	
	private int start;
	private int end;


	public HideTextViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public HideTextViewEx(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HideTextViewEx(Context context) {
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		String text = getText().toString();
		if(!TextUtils.isEmpty(text))
		{
			if (start == 0 && end == text.length()) {}
	        if (start >= 0 && start <= end && end <= text.length()) {
	        		String newText = replace(text, '*', start, end-start);
	            setText(newText);
	        }
		}
		
		
		super.onDraw(canvas);
		
		
	}
	
	/** 替换指定位置的字符 */
	public static String replace(String src, char c, int startIndex, int length) 
	{
		if (src == null) {
			return null;
		}
		if (src.length() < startIndex + length) {
			return src;
		}
		if (length < 0) {
			throw new IllegalArgumentException("length should be >= 0");
		}
		char[] cs = new char[length];
		Arrays.fill(cs, c);
		return src.substring(0, startIndex) + String.valueOf(cs) + src.substring(startIndex + length);
	}
	
	public void setSubIndex(int start,int end)
	{
		this.start = start;
		this.end = end;
	}
	
}
