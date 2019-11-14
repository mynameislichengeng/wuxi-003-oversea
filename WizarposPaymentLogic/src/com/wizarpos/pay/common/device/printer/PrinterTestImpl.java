package com.wizarpos.pay.common.device.printer;

import android.graphics.Bitmap;

import com.wizarpos.device.printer.Printer;

/**
 * 测试模式,所有方法为空
 * 
 * @author wu
 */
public class PrinterTestImpl implements Printer {

	public PrinterTestImpl() {
	}

	@Override
	public void print(String str) {
	}

	@Override
	public void print(Bitmap bitmap) {
	}

	@Override
	public void cutPaper() {
	}

}
