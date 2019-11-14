package com.wizarpos.pay.manage.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay2.lite.R;

/**
 * @Author: yaosong
 * @date 2015-11-3 上午11:31:12
 * @Description: 四舍五入设置 
 */
public class RoundSettingActivity extends BaseViewActivity{

	private RadioButton jiaoRound = null;// 四舍五入到角
	private RadioButton fenRound = null;// 四舍五入到分
	private RadioButton noRound = null;// 无四舍五入
	private String jiao = null;
	private String fen = null;
	private Button savebtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleText("四舍五入");
		showTitleBack();
		setMainView(R.layout.activity_round_setting);
		jiaoRound = (RadioButton) findViewById(R.id.rb_round_jiao);
		fenRound = (RadioButton) findViewById(R.id.rb_round_fen);
		noRound = (RadioButton) findViewById(R.id.rb_round_false);
		savebtn = (Button) findViewById(R.id.btn_setting_save);
		savebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dosave();
				Toast.makeText(RoundSettingActivity.this, "设置保存成功", Toast.LENGTH_SHORT).show();
				RoundSettingActivity.this.finish();
			}
		});
		initConfig();
	}

	protected void dosave() {
		if (jiaoRound.isChecked()) {
			AppConfigHelper.setConfig(AppConfigDef.jiao_round, com.wizarpos.pay.common.Constants.TRUE);
		} else {
			AppConfigHelper.setConfig(AppConfigDef.jiao_round, com.wizarpos.pay.common.Constants.FALSE);
		}
		if (fenRound.isChecked()) {
			AppConfigHelper.setConfig(AppConfigDef.fen_round, com.wizarpos.pay.common.Constants.TRUE);
		} else {
			AppConfigHelper.setConfig(AppConfigDef.fen_round, com.wizarpos.pay.common.Constants.FALSE);
		}
		if (noRound.isChecked()) {
			AppConfigHelper.setConfig(AppConfigDef.no_round, com.wizarpos.pay.common.Constants.TRUE);
		} else {
			AppConfigHelper.setConfig(AppConfigDef.no_round, com.wizarpos.pay.common.Constants.FALSE);
		}
	}
	
	@Override
	protected void onTitleBackClikced(){
		dosave();
		super.onTitleBackClikced();
	}
	
	private void initConfig() {
		jiao = AppConfigHelper.getConfig(AppConfigDef.jiao_round);
		fen = AppConfigHelper.getConfig(AppConfigDef.fen_round);
		if (!TextUtils.isEmpty(jiao) && com.wizarpos.pay.common.Constants.TRUE.equals(jiao)) {
			jiaoRound.setChecked(true);
		} else if (!TextUtils.isEmpty(fen) && com.wizarpos.pay.common.Constants.TRUE.equals(fen)) {
			fenRound.setChecked(true);
		} else {
			noRound.setChecked(true);
		}
	}
	
	
}
