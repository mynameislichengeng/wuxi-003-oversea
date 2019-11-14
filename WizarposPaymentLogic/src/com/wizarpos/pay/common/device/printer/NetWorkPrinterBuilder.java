package com.wizarpos.pay.common.device.printer;

import java.util.List;

import android.graphics.Bitmap;

import com.wizarpos.device.printer.Printer;
import com.wizarpos.pay.common.device.printer.network.PrinterNetWorkMgr;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-11-11 下午1:54:54
 * @Description: 网络打印实现
 */
public class NetWorkPrinterBuilder implements Printer {
	
	private PrinterNetWorkMgr printMgr;

	public NetWorkPrinterBuilder()
	{
		printMgr = PrinterNetWorkMgr.getInstants();
	}
	
	@Override
	public void cutPaper() {
		
	}

	@Override
	public void print(String text) {
		text = text.replace("<br/>", "\n").replace("<c><b>", "").replace("</b></c>", "\n")
				.replace("<r>", "").replace("</r>", "").replace("<t/>", " ");
		List<String> ipList = printMgr.getAllIp();
		for(int i=0;i<ipList.size();i++)
		{
			printMgr.openPort(i, ipList.get(i));
			printMgr.print(i,text);
			printMgr.closePort(i);
		}
		
	}

	@Override
	public void print(Bitmap bitmap) {
		// TODO Auto-generated method stub
		
	}

}
