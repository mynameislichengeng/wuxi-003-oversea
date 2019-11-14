package com.wizarpos.pay.cashier.view;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wizarpos.pay.cashier.adapter.MainFragmentAdapter;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay2.lite.R;

public class ScanTicketUseActivity extends BaseViewActivity implements OnClickListener, ScanListener {

	private TextView tvTitle;
	private ViewPager viewPager;
	private MainFragmentAdapter memberFragmentAdapter;
	private Button[] btnArray = new Button[2];
	private LinearLayout[] llArray = new LinearLayout[2];
	private int currentFragmentIndex = 0;
	private ScanTwoDimensionCode scandimensioncodeFragment;
	private TicketNumberHumanWorkFragment humanworkFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setMainView(R.layout.ticket_use_activity);
		setupView();
		setData();
		addListener();
		updateButtonColor();
	}

	private void setupView() {
		setTitleText("券使用");
		showTitleBack();
		viewPager = (ViewPager) findViewById(R.id.viewpager_ticketuse_fragment);
		btnArray[0] = (Button) findViewById(R.id.btn_two_dimensioncode_ticket);
		btnArray[1] = (Button) findViewById(R.id.btn_humanwork_ticket);
		llArray[0] = (LinearLayout) findViewById(R.id.ll_two_dimensioncode_ticket);
		llArray[1] = (LinearLayout) findViewById(R.id.ll_humanwork_ticket);
	}

	private void setData() {
		scandimensioncodeFragment = new ScanTwoDimensionCode();
		humanworkFragment = new TicketNumberHumanWorkFragment();

		scandimensioncodeFragment.setListener(this);
		humanworkFragment.setListener(this);

		ArrayList<Fragment> list = new ArrayList<Fragment>();
		list.add(scandimensioncodeFragment);
		list.add(humanworkFragment);
		memberFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(),list);
//		memberFragmentAdapter.setFragments(list);
		viewPager.setAdapter(memberFragmentAdapter);

	}

	private void addListener() {
		for (Button btn : btnArray) {
			btn.setOnClickListener(this);
		}
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				currentFragmentIndex = index;
				updateButtonColor();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	private void updateButtonColor() {
		for (int i = 0; i < btnArray.length; i++) {
			if (i == this.currentFragmentIndex) {
				btnArray[i].setTextColor(getResources().getColor(R.color.blue_color));

			} else {
				btnArray[i].setTextColor(0xFF000000);
			}
		}
		for (int i = 0; i < llArray.length; i++) {
			if (i == this.currentFragmentIndex) {
				llArray[i].setBackgroundColor(getResources().getColor(R.color.blue_color));
			} else {
				llArray[i].setBackgroundColor(getResources().getColor(R.color.gray_color));
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_two_dimensioncode_ticket:
			this.currentFragmentIndex = 0;
			break;
		case R.id.btn_humanwork_ticket:
			this.currentFragmentIndex = 1;
			break;
		}
		viewPager.setCurrentItem(currentFragmentIndex);
		updateButtonColor();
	}

	@Override
	public void onScan(String result, boolean isScan) {
		Intent intent = new Intent();
		if (isScan) { // 是否是扫描
			intent.putExtra("scanTicketNo", result);
		} else {
			intent.putExtra("ticketNo", result);
		}
		setResult(RESULT_OK, intent);
		ScanTicketUseActivity.this.finish();
		// String fromCode = getIntent().getStringExtra("paystype");
		// if (fromCode.equals("1001")) {
		// intent.setClass(this, CashPayActivity.class);
		// } else if (fromCode.equals("1002")) {
		// intent.setClass(this, MemberCardDetailActivity.class);
		// setResult(RESULT_OK, intent);
		// ScanTicketUseActivity.this.finish();
		// }else if (fromCode.equals("1003")) {
		// intent.setClass(this, BankCardPayActivity.class);
		// setResult(RESULT_OK, intent);
		// ScanTicketUseActivity.this.finish();
		// }
	}

}
