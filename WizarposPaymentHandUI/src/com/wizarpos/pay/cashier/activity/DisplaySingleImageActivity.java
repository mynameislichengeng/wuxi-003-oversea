package com.wizarpos.pay.cashier.activity;

import uk.co.senab.photoview.PhotoView;
import android.os.Bundle;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.motionpay.pay2.lite.R;

/**
 * 显示单张图片.支持缩放
 * 
 * @author wu
 * 
 */
public class DisplaySingleImageActivity extends BaseViewActivity {

	private PhotoView ivPhotoView;

	public static final String IMAGE_PATH = "DisplaySingleImageActivity_imagePath";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleText("详情");
		showTitleBack();
		setMainView(R.layout.activity_display_single_image);
		ivPhotoView = (PhotoView) findViewById(R.id.ivPhotoView);

		String imgPath = "file://" + getIntent().getStringExtra(IMAGE_PATH);

		ImageLoader.getInstance().displayImage(imgPath, ivPhotoView);
		
		ivPhotoView.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if(v.getId() == R.id.ivPhotoView){
			this.finish();
		}
	}

}
