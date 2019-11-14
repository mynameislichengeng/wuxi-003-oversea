package com.wizarpos.atool.tool;

public class ByteUtil {

	public static String byteToHex(byte[] bs) {
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < bs.length; n++) {
			stmp = Integer.toHexString(bs[n] & 0xFF);
			sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
		}
		return sb.toString().toUpperCase().trim();
	}

	public static byte[] hexToByte(String hex) {
		int m = 0, n = 0;
		int l = hex.length() / 2;
		System.out.println(l);
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = Byte.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
		}
		return ret;
	}
}
