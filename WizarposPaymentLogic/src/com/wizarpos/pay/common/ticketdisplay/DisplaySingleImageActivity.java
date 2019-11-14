package com.wizarpos.pay.common.ticketdisplay;

import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wizarpos.wizarpospaymentlogic.R;

/**
 * 显示单张图片.支持缩放
 * 
 * @author wu
 * 
 */
public class DisplaySingleImageActivity extends AppCompatActivity{

	private PhotoView ivPhotoView;
	protected Toolbar toolbar;
	private TextView tvTitle;

	public static final String IMAGE_PATH = "DisplaySingleImageActivity_imagePath";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setTitleText("详情");
//		showTitleBack();
//		setMainView(R.layout.activity_display_single_image);
		setContentView(R.layout.activity_display_single_image_qrcode);
		initToolbar();
		setTitleText("券二维码");
		showTitleBack();
		ivPhotoView = (PhotoView) findViewById(R.id.ivPhotoView);

		String imgPath = "file://" + getIntent().getStringExtra(IMAGE_PATH);

		ImageLoader.getInstance().displayImage(imgPath, ivPhotoView);
		
//		ivPhotoView.setOnClickListener(this);

	}
	private void initToolbar(){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		tvTitle = (TextView) toolbar.findViewById(R.id.tvToolbarTitle);
		setSupportActionBar(toolbar);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onTitleBackClikced();
		}
		return super.onOptionsItemSelected(item);
	}
	private void onTitleBackClikced() {
		this.finish();
		
	}
	/**
	 * 设置标题栏文字
	 * 
	 * @param title
	 */
	protected void setTitleText(String title) {
		tvTitle.setText(title);
		showToolbar();
	}
	private void showToolbar() {
		if (toolbar.getVisibility() != View.VISIBLE) {
			toolbar.setVisibility(View.VISIBLE);
		}
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}
	/**
	 * 显示标题栏左侧返回按钮
	 */
	protected void showTitleBack() {
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
//	@Override
//	public void onClick(View v) {
//		super.onClick(v);
//		if(v.getId() == R.id.ivPhotoView){
//			this.finish();
//		}
//	}
	
	

}
