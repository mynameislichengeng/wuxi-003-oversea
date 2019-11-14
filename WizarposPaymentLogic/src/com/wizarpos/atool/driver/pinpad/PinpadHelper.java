package com.wizarpos.atool.driver.pinpad;

import com.wizarpos.atool.tool.MD5Util;
import com.wizarpos.devices.jniinterface.PinpadInterface;

public class PinpadHelper {

	public static String cal(String cardNo) {
		PinpadInterface.PinpadOpen();
		byte[] blockBuffer = new byte[32];
		int blockLength = PinpadInterface.PinpadCalculatePinBlock(cardNo.getBytes(), cardNo.getBytes().length, blockBuffer, -1, 0);
		if(blockLength < 0) {
			return "-1";
		}
		byte[] rbs = new byte[blockLength];
		System.arraycopy(blockBuffer, 0, rbs, 0, blockLength);
		return MD5Util.bufferToHex(rbs);
	}
}
