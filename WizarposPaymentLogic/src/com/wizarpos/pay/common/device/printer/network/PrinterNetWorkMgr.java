package com.wizarpos.pay.common.device.printer.network;

import java.util.List;
import java.util.Vector;

import android.graphics.Bitmap;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Base64;

import com.alibaba.fastjson.JSONObject;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.EscCommand.JUSTIFICATION;
import com.gprinter.command.GpCom;
import com.wizarpos.pay.common.device.printer.network.common.PrintConstants;
import com.wizarpos.pay.db.AppConfigHelper;


/**
 * 
 * @Author: Huangweicai
 * @date 2015-11-11 上午11:20:11
 * @Description: 网络打印管理类
 */
public class PrinterNetWorkMgr {
	
	private final String LOG_TAG = PrinterNetWorkMgr.class.getName();
	
	private static PrinterNetWorkMgr printerMgr;
	private GpService mGpService;
	
	private EscCommand esc;
	
	private PrinterNetWorkMgr()
	{
		if(esc == null)
			esc = new EscCommand();
	}
	
	public static PrinterNetWorkMgr getInstants()
	{
		if(printerMgr == null)
			printerMgr = new PrinterNetWorkMgr();
		return printerMgr;
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-11 上午11:26:22 
	 * @param mGpService 
	 * @Description:设置Gpservice 在{@link Pay2Application}中设置
	 */
	public void setGpService(GpService mGpService)
	{
		this.mGpService = mGpService;
	}
	
	private int printerId;
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-11 上午11:28:49  
	 * @Description:打开端口
	 */
	public void openPort(int printerId,String ip)
	{
		this.printerId = printerId;
		if (mGpService != null) {
			try {
				int resultCode = mGpService.openPort(printerId, PrintConstants.CONNECT_TYPE_INTERNET, ip, PrintConstants.DEFAULT_PORT);
				
				
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private final String PRINT_IP_TAG = "printip";
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-11 下午2:30:36 
	 * @param ip 
	 * @Description:将IP保存在本地
	 */
	public void addipToDisk(String ip)
	{
		String ipJson = AppConfigHelper.getConfig(PRINT_IP_TAG);
		String json;
		if(TextUtils.isEmpty(ipJson))
		{
			json = JSONObject.toJSONString(new String[]{ip});
		}else
		{
			List<String> ipList = JSONObject.parseArray(ipJson, String.class);
			if(ipList.contains(ip))
			{
				return;
			}
			ipList.add(ip);
			json = JSONObject.toJSONString(ipList);
		}
		AppConfigHelper.setConfig(PRINT_IP_TAG, json);
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-11 下午2:35:03 
	 * @param ip 
	 * @Description:将IP从本地删除
	 */
	public void removeipFromDisk(String ip)
	{
		String ipJson = AppConfigHelper.getConfig(PRINT_IP_TAG);
		String json;
		if(TextUtils.isEmpty(ipJson))
		{
			return;
		}else
		{
			List<String> ipList = JSONObject.parseArray(ipJson, String.class);
			ipList.remove(ip);
			json = JSONObject.toJSONString(ipList);
		}
		AppConfigHelper.setConfig(PRINT_IP_TAG, json);
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-11 下午2:36:56 
	 * @return 
	 * @Description:获得IP集合
	 */
	public List<String> getAllIp()
	{
		String ipJson = AppConfigHelper.getConfig(PRINT_IP_TAG, "");
		if(TextUtils.isEmpty(ipJson))
			return null;
		List<String> ipList = JSONObject.parseArray(ipJson, String.class);
		return ipList;
	}
	
	public void removeAllIp()
	{
		AppConfigHelper.setConfig(PRINT_IP_TAG, "");
	}
	
	public void closePort(int printerId)
	{
		if(mGpService != null)
			try {
				mGpService.closePort(printerId);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	public void print(int printerId,Bitmap bitmap)
	{
		if (bitmap != null) {
			esc = new EscCommand();
			esc.addSelectJustification(JUSTIFICATION.CENTER);
			esc.addRastBitImage(bitmap, bitmap.getWidth(), 0); // 打印图片
			Vector<Byte> datas = esc.getCommand(); // 发送数据
			Byte[ ] Bytes = datas.toArray(new Byte[datas.size()]);
			byte[ ] bytes = toPrimitive(Bytes);
			String str = Base64.encodeToString(bytes, Base64.DEFAULT);
			int rel;
			try {
				rel = mGpService.sendEscCommand(printerId, str);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-11 下午5:24:07 
	 * @param printerId 链接端口的时候设置的id {@link #openPort(int, String)}
	 * @param text 
	 * @Description:打印文字
	 */
	public void print(int printerId,String text)
	{
		esc = new EscCommand();
		esc.addText(text); // 打印文字
		Vector<Byte> datas = esc.getCommand(); // 发送数据
		Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
		byte[] bytes = toPrimitive(Bytes);
		String str = Base64.encodeToString(bytes, Base64.DEFAULT);
		int rel = 0;
		try {
			if (mGpService != null) {
				rel = mGpService.sendEscCommand(printerId, str);
				GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
				if (r != GpCom.ERROR_CODE.SUCCESS) {
					//失败处理
				}
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} finally {
			try {
				mGpService.closePort(printerId);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}


	public static byte[] toPrimitive(Byte[] array) {
		if(array == null) {
			return null;
		} else if(array.length == 0) {
			return  new byte[0];
		} else {
			byte[] result = new byte[array.length];

			for(int i = 0; i < array.length; ++i) {
				result[i] = array[i].byteValue();
			}

			return result;
		}
	}


}
