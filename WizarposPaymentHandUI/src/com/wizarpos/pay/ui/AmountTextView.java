package com.wizarpos.pay.ui;

import java.math.BigDecimal;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.wizarpos.pay.common.utils.Calculater;

/**
 * 输入金额edittext</br> 只允许输入数字,保持两位小数
 * 
 * @author wu 20150717
 */
public class AmountTextView extends TextView {

	private static final int MAX_LENGTH = 12;

	private String maxAmount = "999999999.99";

	public AmountTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public AmountTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public AmountTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AmountTextView(Context context) {
		super(context);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		if (TextUtils.isEmpty(text)) {
			text = "0.00";
		}
		if (text.length() > MAX_LENGTH) { return; }
		try {
			if (Calculater.compare(text.toString(), maxAmount) > 0) {
				text = Calculater.divide100(maxAmount);
			}
		} catch (Exception e) {}
		super.setText(text, type);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		setSingleLine();
	}

	public int getAmountByFen() {
		String amount = formotYuan(getText().toString());
		try {
			return Integer.parseInt(amount);
		} catch (Exception e) {}
		return 0;
	}

	public int getAmountByYuan() {
		String amount = getText().toString();
		try {
			return Integer.parseInt(amount);
		} catch (Exception e) {}
		return 0;
	}

	public void addNumber(int number) {
		try {
			setText(formotFen(getText().toString().replaceAll("\\.", "") + number));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delNumber() {
		try {
			String _current = getText().toString().replaceAll("\\.", "");
			if ("0.00".endsWith(_current)) { return; }
			setText(formotFen(_current.substring(0, _current.length() - 1)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String divide100(String a) {
		try {
			return new BigDecimal(a).divide(new BigDecimal(100), 2, BigDecimal.ROUND_UNNECESSARY).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void setTextByFen(String text) {
		setText(formotFen(text));
	}

	public void setTextByYuan(String text) {
		setText(formotYuan(text));
	}

	public void setMaxAmount(String maxAmount) {
		this.maxAmount = maxAmount;
		if (TextUtils.isEmpty(maxAmount)) {
			this.maxAmount = "0.00";
		}
	}

	/**
	 * 格式化 101 --> 1.01
	 * 
	 * @param amount
	 * @return
	 */
	private String formotFen(String amount) {
		try {
			return divide100(amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 乘100
	 * 
	 * @return
	 */
	private String multiply100(String a) {
		return new BigDecimal(a).multiply(new BigDecimal(100)).toString();
	}

	/**
	 * 格式化 1.01 ---> 101
	 * 
	 * @param amount
	 * @return
	 */
	private String formotYuan(String amount) {
		try {
			String _amountStr = multiply100(amount);
			return _amountStr.substring(0, _amountStr.indexOf("."));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 减法
	 * 
	 * @param big
	 * @param small
	 * @return
	 */
	public static String subtract(String big, String small) {
		return new BigDecimal(big).subtract(new BigDecimal(small)).toString();
	}

	/**
	 * 加法
	 * 
	 * @param big
	 * @param small
	 * @return
	 */
	public static String plus(String big, String small) {
		return new BigDecimal(big).add(new BigDecimal(small)).toString();
	}

}
