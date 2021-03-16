package com.wizarpos.pay.setting.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.motionpay.pay2.lite.R;

public class SuggestionFeedbackActivity extends BaseViewActivity {
	EditText etSuggestion;
	Button btnCommit;
	ImageButton back;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setContentView(R.layout.suggestion_feedback_activity);
		setTitleText(getResources().getString(R.string.feedback));
		etSuggestion = (EditText) findViewById(R.id.et_suggestion);
		int[] btnIds = { R.id.btn_commit };
		setOnClickListenerByIds(btnIds, this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_commit:
			// 将获取的信息提交给服务器
			finish();
			break;
		}
	}
}
