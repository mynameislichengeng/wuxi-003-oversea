package com.wizarpos.pay.cashier.merchant_apply.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.common.Constants;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-12-1 下午4:22:37
 * @Description:文件上传
 */
public class UploadFileController {
	
	private static final String LOG_TAG = UploadFileController.class.getName();
	/** 线程池*/
	private ExecutorService executorService = null;
	/** 线程数量*/
	private static final int THEAD_POOL_NUM = 5;
	
	private ThreadLocal<Map<ImageType, String>> mapLocal;
	
	private ResponseListener lisetener ;
	
//	String bizLicenseImgUrl = MerchantApplyNetMgr.getInstants().getApplyBean().getBizLicenseImgUrl();
//	String corpIdentAbvImgUrl = MerchantApplyNetMgr.getInstants().getApplyBean().getCorpIdentAbvImgUrl();
//	String corpIdentObvImgUrl = MerchantApplyNetMgr.getInstants().getApplyBean().getCorpIdentObvImgUrl();
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-17 下午6:10:59
	 * @Description: 商户进件
	 */
	public enum ImageType
    {
		// 营业执照 (OSS路径)
		TYPE_IMAGE_BIZ_LICENSE(1),
		// 法人身份证(正) (OSS路径)
		TYPE_IMAGE_CORP_IDENT(2),
		// 法人身份证(反) (OSS路径)
		TYPE_IMAGE_CORP_IDENT_OBV(3);
		
        /**
         * 构造函数,初始化枚举元素值
         * 
         * @param iValue
         *            输入整数值
         */
        ImageType(int iValue)
        {
        		this.m_iEnumValue = iValue;
        }
        /**
         * 获取枚举型对应的整数值
         * Description: 获取枚举型对应的整数值
         * @return int型，该枚举元素对应的值
         */
        public int getValue()
        {
            return m_iEnumValue;
        }

        private final int m_iEnumValue;

    }
	
	private Handler mHandler = new Handler()
	{
		@Override
		public void dispatchMessage(Message msg) {
			int msgWhat = msg.what;
			if(msgWhat < 0)
			{
				lisetener.onFaild(new Response(-1, "上传图片失败"));
			}else
			{
				Object oj = msg.obj;
				lisetener.onSuccess(new Response(0, "上传图片成功",oj));
			}
		}
	};	
	
	public UploadFileController()
	{
		executorService = Executors.newFixedThreadPool(THEAD_POOL_NUM);
		mapLocal = new ThreadLocal<Map<ImageType, String>>() 
        {
			@Override
			protected Map<ImageType, String> initialValue() {
				return new HashMap<>();
			}
		};
	}
	
	public void uploadFile(final Map<ImageType, String> imageMap,final ResponseListener listener)
	{
		if(imageMap == null || imageMap.size() == 0)
		{
			listener.onSuccess(new Response(1, "map为空"));
			return;
		}
		Map<ImageType, File> fileMap = new HashMap<UploadFileController.ImageType, File>();
		for (Map.Entry<ImageType, String> entry : imageMap.entrySet()) 
		{
			String filePath = entry.getValue();
			File file = new File(filePath);
			if(file.exists())
			{
				fileMap.put(entry.getKey(), file);
			}else
			{
				listener.onFaild(new Response(-1, "文件不存在"));
				return;
			}
		
		}
//		for(int i=0;i<filePathList.size();i++)
//		{
//			String filePath = filePathList.get(i);
//			File file = new File(filePath);
//			if(file.exists())
//			{
//				files[i] = file;
//			}else
//			{
//				listener.onFaild(new Response(-1, "文件不存在"));
//				return;
//			}
//		}
		uploadFileMap(fileMap, listener);
	}
	

	
	public void uploadFileMap(final Map<ImageType, File> imageMap,final ResponseListener lisetener)
	{
		this.lisetener = lisetener;
		executorService.submit(new Runnable() {
			
			@Override
			public void run() {
				for (Map.Entry<ImageType, File> entry : imageMap.entrySet()) 
				{  
					if(!uploadFileOnThread(entry.getValue(),entry.getKey()))
					{
						mHandler.sendEmptyMessage(-1);
//						lisetener.onFaild(new Response(-1, "上传图片失败"));
						return;
					}  
				}  
				Message msg = mHandler.obtainMessage();
				msg.what = 0;
				msg.obj = mapLocal.get();
				mHandler.sendMessage(msg);
			}
		});
	}

	private synchronized boolean uploadFileOnThread(File file,ImageType type)
	{
		try {
			/* 拼凑请求地址 */
			String serverUrl = "http://" + Constants.DEFAULT_IP +":" + Constants.DEFAULT_PORT + Constants.SUFFIX_URL;
			Log.i(LOG_TAG, "请求地,图片上传:" + serverUrl);
//			String serverUrl = "http://train.wizarpos.com:80/member-server/service";
			URL url = new URL(serverUrl);
			HttpURLConnection con = (HttpURLConnection) url
					.openConnection();
			// 发送POST请求必须设置如下两行
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			String sn = android.os.Build.SERIAL;
			con.setRequestProperty("sn", sn);
			con.setRequestProperty("Content-Type",
					"multipart/form-data;");
			con.setRequestProperty("type", "png");
			con.setRequestProperty("te_method", "uploadFile");
			con.setRequestProperty("version", "V1_5");
			DataOutputStream ds = new DataOutputStream(con
					.getOutputStream());

			InputStream is = new FileInputStream(file);
			byte[] data = InputStreamTOByte(is);
			if (data != null) {
				ds.write(data);
			}
			// 定义BufferedReader输入流来读取URL的响应
			InputStream is1 = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is1.read()) != -1) {
				b.append((char) ch);
			}
			String s = b.toString();
			Log.i("上传结果", "====上传结果：" + s);

			//{"code":0,"msg":"success","result":{"photoUrl":"http://image.wizarpos.com/MerchantApply/20151201171056.png"}}
			JSONObject json = JSON.parseObject(s);
			if(json.getIntValue("code") == 0)
			{
				Log.i(UploadFileController.class.getSimpleName(),
						"=====上传成功＝＝＝＝");
				String urlStr = json.getJSONObject("result").getString("photoUrl");
				Map<ImageType, String> mapStr = mapLocal.get();
				mapStr.put(type, urlStr);
				mapLocal.set(mapStr);
			}else
			{
				Log.i(UploadFileController.class.getSimpleName(),
						"=====上传失败＝＝＝＝");
				return false;
			}
			ds.close();
			return true;
		} catch (Exception e) {
			Log.e("图片上传", "图片上传:" + e);
			return false;
		}finally
		{
			
		}
	}
	
	final static int BUFFER_SIZE = 4096;  
	/**  
     * 将InputStream转换成byte数组  
     * @param in InputStream  
     * @return byte[]  
     * @throws IOException  
     */  
    public static byte[] InputStreamTOByte(InputStream in) throws IOException{  
          
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] data = new byte[BUFFER_SIZE];  
        int count = -1;  
        while((count = in.read(data,0,BUFFER_SIZE)) != -1)  
            outStream.write(data, 0, count);  
          
        data = null;  
        return outStream.toByteArray();  
    }  

}
