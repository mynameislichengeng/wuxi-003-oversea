package com.wizarpos.pay.login.merchant.input;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.merchant_apply.MerchantApplyNetMgr;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.login.util.FileUtil;
import com.wizarpos.pay.view.BasePopupWindow;
import com.wizarpos.pay2.lite.R;

/**
 * @Author: yaosong
 * @date 2015-11-27 上午9:40:59
 * @Description:资料上传
 * 废弃
 */
public class UploadInformationActivity extends BaseViewActivity{

	private BasePopupWindow popupWindow;
	private ImageView iv1,iv2,iv3;
	private RelativeLayout rl_pop_parent;
	private int x_offset = 0;
	private int y_offset = 0;
	private String name;
	private int photo_index;
	private View view;		//popupwindow的view
	// 存储路径  
    private static final String PATH = Environment  
            .getExternalStorageDirectory() + "/DCIM"; 
    // 请求  
    private static final int CAMERA_TAKE = 1;  
    private static final int CAMERA_SELECT = 2; 
    private static final int IV_1 = 11;  
    private static final int IV_2 = 22; 
    private static final int IV_3 = 33; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		photo_index = -1;
		initView();
		checkNotify();
	}

	private void initView() {
		setTitleText("资料上传");
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		setMainView(R.layout.activity_information_upload_merchant);
		view = inflater.inflate(R.layout.popup_photo, null);
		popupWindow = new BasePopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,true);
		iv1 = (ImageView) findViewById(R.id.iv_photo1);
		iv2 = (ImageView) findViewById(R.id.iv_photo2);
		iv3 = (ImageView) findViewById(R.id.iv_photo3);
		setOnClickListenerById(R.id.iv_photo1, this);
		setOnClickListenerById(R.id.iv_photo2, this);
		setOnClickListenerById(R.id.iv_photo3, this);
		setOnClickListenerById(R.id.btn_next, this);
		setOnClickListenerById(R.id.btn_cancle, this);
		view.findViewById(R.id.tv_take_photo).setOnClickListener(this);
		view.findViewById(R.id.tv_choose_photo).setOnClickListener(this);
	}
	
	private void checkNotify() 
	{
		if(MerchantApplyNetMgr.getInstants().isApplyBeanNull())
		{
			return;
		}
		String bizLicenseImgUrl = MerchantApplyNetMgr.getInstants().getApplyBean().getBizLicenseImgUrl();
		String corpIdentAbvImgUrl = MerchantApplyNetMgr.getInstants().getApplyBean().getCorpIdentAbvImgUrl();
		String corpIdentObvImgUrl = MerchantApplyNetMgr.getInstants().getApplyBean().getCorpIdentObvImgUrl();
		ImageLoader.getInstance().displayImage(bizLicenseImgUrl, iv1);
		ImageLoader.getInstance().displayImage(corpIdentAbvImgUrl, iv2);
		ImageLoader.getInstance().displayImage(corpIdentObvImgUrl, iv3);
	}

	@Override
	public void onClick(View view) {
		int[] location = new int[2];  
		super.onClick(view);
		switch (view.getId()) {
		case R.id.iv_photo1:
			rl_pop_parent = (RelativeLayout) findViewById(R.id.rl_pop_parent1);
			location = new int[2];  
			rl_pop_parent.getLocationInWindow (location);
			x_offset = location[0];
			y_offset = location[1];
			popupWindow.showAtLocation(rl_pop_parent,Gravity.NO_GRAVITY,x_offset+rl_pop_parent.getWidth(),y_offset+rl_pop_parent.getHeight()/2-view.getHeight()/2+2);
			photo_index = IV_1;
			break;
		case R.id.iv_photo2:
			rl_pop_parent = (RelativeLayout) findViewById(R.id.rl_pop_parent2);
			location = new int[2];  
			rl_pop_parent.getLocationInWindow (location);
			x_offset = location[0];
			y_offset = location[1];
			popupWindow.showAtLocation(rl_pop_parent,Gravity.NO_GRAVITY,x_offset+rl_pop_parent.getWidth(),y_offset+rl_pop_parent.getHeight()/2-view.getHeight()/2+2);
			photo_index = IV_2;
			break;
		case R.id.iv_photo3:
			rl_pop_parent = (RelativeLayout) findViewById(R.id.rl_pop_parent3);
			location = new int[2];  
			rl_pop_parent.getLocationInWindow (location);
			x_offset = location[0];
			y_offset = location[1];
			popupWindow.showAtLocation(rl_pop_parent,Gravity.NO_GRAVITY,x_offset+rl_pop_parent.getWidth(),y_offset+rl_pop_parent.getHeight()/2-view.getHeight()/2+2);
			photo_index = IV_3;
			break;
		case R.id.btn_next://下一步
			if(matchInfo())
			{
				startNewActivity(SettingsActivity.class);
				finish();
			}
			break;
		case R.id.btn_cancle:
			this.finish();
			break;
		case R.id.tv_take_photo:
			takePhonos();
			break;
		case R.id.tv_choose_photo:
			photos();
			break;
		}
	}
	
	private boolean matchInfo()
	{
		if(TextUtils.isEmpty(MerchantApplyNetMgr.getInstants().getApplyBean().getBizLicenseImgUrl())
				|| TextUtils.isEmpty(MerchantApplyNetMgr.getInstants().getApplyBean().getCorpIdentAbvImgUrl())
				|| TextUtils.isEmpty(MerchantApplyNetMgr.getInstants().getApplyBean().getCorpIdentObvImgUrl()))
		{
			Toast.makeText(this, "内容不能为空", 0).show();
			return false;
		}
		return true;
	}
	
	private void showPhoto(Bitmap bitmap,String filepath) {
		int width = iv1.getWidth();
        LogEx.d("filepath",filepath);
		int height = (int)(bitmap.getHeight() * ((double)iv1.getWidth() / (double)bitmap.getWidth()));
		Bitmap smallBitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height);
		switch (photo_index) {
		case IV_1:
			iv1.setImageBitmap(smallBitmap);
			//保存营业执照filepath
			MerchantApplyNetMgr.getInstants().getApplyBean().setBizLicenseImgUrl(filepath);
			break;
		case IV_2:
			iv2.setImageBitmap(smallBitmap);
			//保存法人身份证正面filepath
			MerchantApplyNetMgr.getInstants().getApplyBean().setCorpIdentAbvImgUrl(filepath);
			break;
		case IV_3:
			iv3.setImageBitmap(smallBitmap);
			//保存法人身份证反面filepath
			MerchantApplyNetMgr.getInstants().getApplyBean().setCorpIdentObvImgUrl(filepath);
			break;
		default:
			break;
		}
	}
	//拍照
	private void takePhonos() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用系统相机  
		new DateFormat();  
		name = DateFormat.format("yyyyMMdd_hhmmss",  
		                Calendar.getInstance(Locale.CHINA))  
		                + ".jpg";  
		Uri imageUri = Uri.fromFile(new File(PATH, name));  
		  
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//把照片保存在sd卡中指定位置。  
		startActivityForResult(intent, CAMERA_TAKE);
		
	}
	//相册
	private void photos() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
		intent.setType("image/*");  
		startActivityForResult(intent, CAMERA_SELECT); 
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		popupWindow.dismiss();
		 if (resultCode == RESULT_OK) {  
	            switch (requestCode) {  
	            case CAMERA_TAKE:  
	                Bitmap bitmap = BitmapFactory.decodeFile(PATH + "/" + name);  
	                Toast.makeText(this, name, Toast.LENGTH_LONG).show();  
	                System.out.println(bitmap.getHeight() + "======"  
	                        + bitmap.getWidth());
                    showPhoto(bitmap,PATH + "/" + name);
	                break;  
	  
	            case CAMERA_SELECT:  
	                ContentResolver resolver = getContentResolver();  
	                // 照片的原始资源地址  
	                Uri imgUri = data.getData();  
	                try {  
	                    // 使用ContentProvider通过Uri获取原始图片  
	                    Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,  
	                            imgUri);  
	                    showPhoto(photo,FileUtil.getImageAbsolutePath(this, imgUri));
	                } catch (Exception e) {  
	                	e.printStackTrace(); 
	                }  
	  
	                break;  
	            }  
	        }  
	}
}
