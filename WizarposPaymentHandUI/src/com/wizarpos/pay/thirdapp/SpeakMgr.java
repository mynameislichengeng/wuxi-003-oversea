package com.wizarpos.pay.thirdapp;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;

//import com.iflytek.speech.ErrorCode;
//import com.iflytek.speech.ISpeechModule;
//import com.iflytek.speech.InitListener;
//import com.iflytek.speech.SpeechSynthesizer;
//import com.iflytek.speech.SpeechUtility;
//import com.iflytek.speech.SynthesizerListener;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.pay.thirdapp.utils.ApkInstaller;

public class SpeakMgr{
	private static final String LOG_TAG = SpeakMgr.class.getName();

	// 语音合成对象
//	private SpeechSynthesizer mTts;
	
	private boolean isPlaySound = false;
	
	private final String APP_ID = "53c776aa";

	private static SpeakMgr speakMgr;
	
	private SpeakMgr()
	{
		
	}
	
	public static SpeakMgr getInstants()
	{
		if(speakMgr == null)
			speakMgr = new SpeakMgr();
		return speakMgr;
	}
	
	private ResponseListener mListener;
	

	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-2 上午10:08:07 
	 * @param mContext
	 * @return true:已经安装  false:未安装,调用系统安装asset APK
	 * @Description:
	 */
	public void initSpeak(Context mContext,ResponseListener listener)
	{
//		this.mListener = listener;
//		if(mTts != null)
//		{
//			Log.w(LOG_TAG, "mTts is not null.");
////			this.mListener.onSuccess(new Response());
//			onDestroy();
////			return;
//		}
//		// 检测语音引擎是否可用，如果没有安装自动安装
//		if (!checkSpeechServiceInstall(mContext)) {
//			String assetsApk = "SpeechService.apk";
//			if (processInstall(mContext, assetsApk))
//			{
//				Log.d(LOG_TAG, "已安装APK");
////				initSpeak(mContext, listener);
//			}else
//			{
//				Log.w(LOG_TAG, "未安装讯飞APK");
//				this.mListener.onFaild(new Response(-1, "未安装讯飞APK"));
//			}
//			return;
//		}
//		// 引擎初始化
//		SpeechUtility.getUtility(mContext).setAppid(APP_ID);
//		mTts = new SpeechSynthesizer(mContext, mTtsInitListener);
//		return;
	}

	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-2 上午10:05:38 
	 * @param speakText 
	 * @Description: 调用此方法语音播报
	 */
	public void speak(String speakText)
	{
//		if(mTts != null)
//		{
//			mTts.startSpeaking(speakText,
//					mTtsListener);
//		}else
//		{
//			Log.w(LOG_TAG, "mTts is null.");
//		}
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-3 上午10:01:09  
	 * @Description:应用退出的时候需要调用此方法
	 */
	public void onDestroy()
	{
//		if(mTts != null)
//		{
//			if(mTtsListener != null)
//			{
//				mTts.stopSpeaking(mTtsListener);
//			}
//			mTts.destory();
//			mTts =null;
//		}
		
	}
	
	/**
	 * 检测科大讯飞语音+引擎是否安装
	 * 
	 * @return
	 */
	private boolean checkSpeechServiceInstall(Context mContext) {
		String packageName = "com.iflytek.speechcloud";
		List<PackageInfo> packages = mContext.getPackageManager()
				.getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			if (packageInfo.packageName.equals(packageName)) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}
//
//	/**
//	 * 初期化监听。
//	 */
//	private InitListener mTtsInitListener = new InitListener() {
//
//		@Override
//		public void onInit(ISpeechModule arg0, int code) {
//			if (code == ErrorCode.SUCCESS) {
////				mTts.startSpeaking("初始化初始化初始化", mTtsListener);
//				Log.i(LOG_TAG, "语音初始化成功");
//				mListener.onSuccess(new Response());
//			}
//		}
//	};
//
//	/**
//	 * 合成回调监听。
//	 */
//	private SynthesizerListener mTtsListener = new SynthesizerListener.Stub() {
//		@Override
//		public void onBufferProgress(int progress) throws RemoteException {
//
//		}
//
//		@Override
//		public void onCompleted(int code) throws RemoteException {
//			isPlaySound = false;
//			onDestroy();
//		}
//
//		@Override
//		public void onSpeakBegin() throws RemoteException {
//			isPlaySound = true;
//		}
//
//		@Override
//		public void onSpeakPaused() throws RemoteException {
//
//		}
//
//		@Override
//		public void onSpeakProgress(int progress) throws RemoteException {
//
//		}
//
//		@Override
//		public void onSpeakResumed() throws RemoteException {
//
//		}
//	};

	/**
	 * 执行本地安装 语音+
	 * 
	 * @param context
	 * @param assetsApk
	 * @return
	 */
	private boolean processInstall(Context context, String assetsApk) {
		// 本地安装方式
		if (!ApkInstaller.installFromAssets(context,
				assetsApk)) {
//			Toast.makeText(this, "安装语音引擎失败!", Toast.LENGTH_SHORT).showFromDialog();
			return false;
		}
		return true;
	}
}
