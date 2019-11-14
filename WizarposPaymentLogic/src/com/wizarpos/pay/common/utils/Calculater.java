package com.wizarpos.pay.common.utils;

import java.math.BigDecimal;

public class Calculater {
	/**
	 * 乘法
	 * 
	 * @return
	 */
	public static String multiply(String a, String b) {
		return new BigDecimal(a).multiply(new BigDecimal(b)).toString();
	}

	/**
	 * 乘100
	 * 
	 * @return
	 */
	public static String multiply100(String a) {
		return new BigDecimal(a).multiply(new BigDecimal(100)).toString();
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

	/**
	 * 比较
	 * 
	 * @param big
	 * @param small
	 * @return -1 小于 1 大 0 相等
	 */
	public static int compare(String big, String small) {
		return new BigDecimal(big).compareTo(new BigDecimal(small));
	}

	/**
	 * 除法
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static String divide(String a, String b) {
		try {
			return new BigDecimal(a).divide(new BigDecimal(b), 2,
					BigDecimal.ROUND_UNNECESSARY).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 除法
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static String divide(String a, String b,int digit) {
		try {
			return new BigDecimal(a).divide(new BigDecimal(b), digit,
					BigDecimal.ROUND_UNNECESSARY).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String divide100(String a) {
		try {
			return new BigDecimal(a).divide(new BigDecimal(100), 2,
					BigDecimal.ROUND_UNNECESSARY).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 格式化 101 --> 1.01
	 * 
	 * @param amount
	 * @return
	 */
	public static String formotFen(String amount) {
		try {
			return divide100(amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// /**
	// * 格式化 保留两位小数
	// *
	// * @param amout
	// * @return
	// */
	// public String formatFen(String amout) {
	// if (TextUtils.isEmpty(amout)) {
	// return "0.00";
	// }
	// if ("0".equals(amout) || "0.0".equals(amout) || "0.00".equals(amout)) {
	// return "0.00";
	// }
	// return new BigDecimal(amout).setScale(2,
	// BigDecimal.ROUND_UNNECESSARY).toString();
	// }

	/**
	 * 格式化 1.01 ---> 101
	 * 
	 * @param amount
	 * @return
	 */
	public static String formotYuan(String amount) {
		try {
			String _amountStr = multiply100(amount);
			return _amountStr.substring(0, _amountStr.indexOf("."));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
