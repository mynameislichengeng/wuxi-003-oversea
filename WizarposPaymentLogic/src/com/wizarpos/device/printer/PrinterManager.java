package com.wizarpos.device.printer;

/**
 * 打印驱动管理
 * @author wu
 *
 */
public class PrinterManager {
	
	private static PrinterManager manager;
	
	private PrinterManager(){	}
	
	private Printer printer;
	
	public static PrinterManager getInstance(){
		if(manager == null){
			manager = new PrinterManager();
		}
		return manager;
	}
	
	public void setPrinter(Printer printer){
		this.printer = printer;
	}
	
	public Printer getPrinter() {
		return printer;
	}
}
