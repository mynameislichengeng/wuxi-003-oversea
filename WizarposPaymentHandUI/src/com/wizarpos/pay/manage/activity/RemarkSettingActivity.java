package com.wizarpos.pay.manage.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.motionpay.pay2.lite.R;

/**
 * @Author: yaosong
 * @date 2015-11-3 上午11:29:49
 * @Description:其他支付-备注设置 
 */
public class RemarkSettingActivity extends BaseViewActivity{

	private RadioButton remarkRadioButton;
	private RadioButton noneRadioButton;
	private Button saveBtn;
	private String SHOWREMARK = "1";
	private String NONEREMARK = "0";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleText("其他支付");
		showTitleBack();
		setMainView(R.layout.activity_remark_setting);
		remarkRadioButton = (RadioButton) findViewById(R.id.rb_remark_true);
		noneRadioButton = (RadioButton) findViewById(R.id.rb_remark_false);
		saveBtn = (Button) findViewById(R.id.btn_save_setting);
		saveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dosave();
				Toast.makeText(RemarkSettingActivity.this, "设置保存成功", Toast.LENGTH_SHORT).show();
				RemarkSettingActivity.this.finish();
			}
		});
		initConfig();
	}
	
	private void initConfig() {
		if (SHOWREMARK.equals(AppConfigHelper.getConfig(AppConfigDef.isRemark))) {
			remarkRadioButton.setChecked(true);
		} else {
			remarkRadioButton.setChecked(false);
		}
	}
	
	@Override
	protected void onTitleBackClikced(){
		dosave();
		super.onTitleBackClikced();
	}
	
	private void dosave() {
		if (remarkRadioButton.isChecked()) {
			AppConfigHelper.setConfig(AppConfigDef.isRemark, SHOWREMARK);
		} else {
			AppConfigHelper.setConfig(AppConfigDef.isRemark, NONEREMARK);
		}
	}

}
