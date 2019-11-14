package com.wizarpos.atool.tool;

import android.widget.EditText;

import java.math.BigDecimal;

/**
 * EditText输入框工具类
 * @author lizhou
 *
 */
public class EditTextUtil {

	public static Integer emptyToNum(String v) {
		if (v == null || "".equals(v.trim())) {
			return 0;
		}
		return Integer.parseInt(v.trim());
	}
	
	public static Integer emptyToNum(EditText et) {
		return emptyToNum(et.getText().toString());
	}
	
	/**
	 * 字符串转换成０的BigDecimal
	 * @param v
	 * @return
	 */
	public static BigDecimal emptyToBigDecimal(String v) {
		if (v == null || "".equals(v.trim())) {
			return new BigDecimal("0");
		}
		return new BigDecimal(v.trim());
	}
	
	public static BigDecimal emptyToBigDecimal(EditText et) {
		return emptyToBigDecimal(et.getText().toString());
	}
	
}

