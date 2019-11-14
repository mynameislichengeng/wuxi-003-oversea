package com.wizarpos.pay.login.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @Author: yaosong
 * @date 2016-1-5 下午3:05:12
 * @Description:特殊字符输入限制
 */
public class DigitsTextWatcher implements TextWatcher {
	public final static String digits = "`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\t\n";// 要过滤掉的字符
	
	private String tmp = "";
	private EditText mEditText;

	public DigitsTextWatcher(EditText et) {
		mEditText = et;
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		mEditText.setSelection(s.length());
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		tmp = s.toString();
	}

	@Override
	public void afterTextChanged(Editable s) {
		String str = s.toString();
		if (str.equals(tmp)) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			if (digits.indexOf(str.charAt(i)) < 0) {
				sb.append(str.charAt(i));
			}
		}
		tmp = sb.toString();
		mEditText.setText(tmp);
	}
}
