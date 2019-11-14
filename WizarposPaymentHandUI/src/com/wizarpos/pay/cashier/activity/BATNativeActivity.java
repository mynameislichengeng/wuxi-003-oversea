package com.wizarpos.pay.cashier.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.common.view.MaterialImageLoading;
import com.wizarpos.pay2.lite.R;

public abstract class BATNativeActivity extends BATActivity {

	private ImageView ivBigBarcode;
	private ImageView ivBarcode;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setMainView(R.layout.activity_native);
		showTitleBack();
		ivBarcode = (ImageView) findViewById(R.id.ivBarcode);
		ivBarcode.setOnClickListener(this);
		ivBigBarcode = (ImageView) findViewById(R.id.ivBigBarcode);
		ivBigBarcode.setOnClickListener(this);
		
		int[] btnIds = { R.id.btnMciropay, R.id.btnCheckOrder, R.id.flBig };
		setOnClickListenerByIds(btnIds, this);
	}

	/**
	 * 显示图片 String --> Bitmap
	 * 
	 * @param ivBarcode
	 * @param url
	 */
	protected void showImgFromString(String url) {
		ivBarcode.setImageBitmap(Tools.genQRCode(url));
		MaterialImageLoading.animate(ivBarcode).setDuration(700).start();
		ivBigBarcode.setImageBitmap(Tools.genQRCode(url));
	}

	/**
	 * 网络加载图片并显示
	 * 
	 * @param ivBarcode
	 * @param url
	 */
	protected void showImgFromNet(String url) {
		ImageLoader.getInstance().displayImage(url, ivBarcode, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String arg0, View arg1) {}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				MaterialImageLoading.animate(ivBarcode).setDuration(700).start();
			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {

			}
		});
		ImageLoader.getInstance().displayImage(url, ivBigBarcode);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.flBig:
		case R.id.ivBigBarcode:
			findViewById(R.id.flBig).setVisibility(View.GONE);
			findViewById(R.id.llFront).setVisibility(View.VISIBLE);
			break;
		case R.id.ivBarcode:
			findViewById(R.id.flBig).setVisibility(View.VISIBLE);
			findViewById(R.id.llFront).setVisibility(View.GONE);
			break;
		case R.id.btnMciropay:
			toMicropay();
			break;
		case R.id.btnCheckOrder:
			checkOrder();
			break;
		default:
			break;
		}
	}

	/**
	 * 被扫
	 */
	protected abstract void toMicropay() ;

	/**
	 * 检查订单
	 */
	protected abstract void checkOrder() ;
}
