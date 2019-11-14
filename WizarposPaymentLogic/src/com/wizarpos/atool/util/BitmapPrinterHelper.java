package com.wizarpos.atool.util;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.wizarpos.device.printer.PrinterHelper;
import com.wizarpos.devices.jniinterface.PrinterInterface;

import java.util.ArrayList;
import java.util.List;

public class BitmapPrinterHelper {
	private boolean isPrintFinished = true;
	private Object printing;
	private List<Object> waitings = new ArrayList<Object>();
	private String padOrpos = "";

	private PrintListener listener;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			listener.onFinish();
		}

	};

	public void setListener(PrintListener listener) {
		this.listener = listener;
	}

	public interface PrintListener {
		void onFinish();
	}

	public String getPadOrpos() {
		return padOrpos;
	}

	public void setPadOrpos(String padOrpos) {
		this.padOrpos = padOrpos;
	}

	public void add(Object obj) {
		this.waitings.add(obj);
	}

	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (waitings.isEmpty() == false) {
					if (isPrintFinished) {
						printing = waitings.remove(0);
						isPrintFinished = false;
						print(printing);
					} else {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}

	// 平板打印机切纸方法
	static public byte[] cutPaperByte() {
		return new byte[]{(byte) 0x1B, (byte) 0x69};
	}

	synchronized public void cutPaper() {
		try {
			PrinterInterface.PrinterOpen();
			PrinterInterface.PrinterBegin();
			PrinterInterface.PrinterWrite(cutPaperByte(), cutPaperByte().length);
		} catch (Exception e) {
		} finally {
			PrinterInterface.PrinterEnd();
			PrinterInterface.PrinterClose();
		}
	}

	private void print(final Object obj) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (obj instanceof String) {
						PrinterHelper.print((String) obj);
					} else if (obj instanceof Bitmap) {
						PrinterHelper.printBitmap((Bitmap) obj);
					}
					if (waitings.size() <= 0) {
						if (getPadOrpos().equals("WIZARPAD")) cutPaper();// 如果是平板
																			// 那最后一次打印完成后切纸
						if (listener != null) {
							Looper.prepare();
							handler.sendEmptyMessage(1);
							Looper.loop();
						}
					}
				} catch (Exception e) {
					Log.e("打印错误", "Printer错误");
				} finally {
					isPrintFinished = true;
				}
			}
		}).start();
	}
}
