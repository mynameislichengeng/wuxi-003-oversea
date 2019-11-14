package com.wizarpos.pay.common.ticketdisplay;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.wizarpos.pay.common.DialogHelper;
import com.wizarpos.pay.common.DialogHelper.DialogCallbackAndNo;
import com.wizarpos.wizarpospaymentlogic.R;

/**
 * 显示券列表
 * 
 * @author wu
 * 
 */
public class DisplayTicketActivity extends AppCompatActivity implements OnItemClickListener {

	private ListView lvTickets;
	private DisplayTicketAdapter adapter;
	private List<DisplayTicektBean> displayTicektBeans;
	protected Toolbar toolbar;
	private TextView tvTitle;
	protected TextView tvRight;
	public static final String DISPLAY_TICKET = "displayTicektBeans";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setTitleText("券列表");
//		showTitleBack();
//		setMainView(R.layout.activity_diplay_tickets);
//		setContentView(R.layout.activity_diplay_tickets);
		setContentView(R.layout.activity_diplay_tickets_with_tittle);
		initToolbar();
		setTitleText("券发行");
		showTitleBack();
		lvTickets = (ListView) findViewById(R.id.lvTickets);
		adapter = new DisplayTicketAdapter(this);
		lvTickets.setAdapter(adapter);
		lvTickets.setOnItemClickListener(this);
		displayTicektBeans = (List<DisplayTicektBean>) getIntent().getSerializableExtra(DISPLAY_TICKET);
		if(displayTicektBeans == null || displayTicektBeans.isEmpty()){
			this.finish();
			return;
		}
//		String filePath = Environment.getExternalStorageDirectory().toString();
//		for (int i = 0; i < 10; i++) {
//			DisplayTicektBean bean = new DisplayTicektBean();
//			bean.setTitle("会员券");
//			bean.setSubTitle("价格:1000");
//			bean.setBitmapPath(filePath + File.separator + "ereima.jpeg");
//			bean.setEndString("时间 : 2015-09-09");
//			displayTicektBeans.add(bean);
//		}
		adapter.setDataChanged(displayTicektBeans);
	}
	private void initToolbar(){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		tvTitle = (TextView) toolbar.findViewById(R.id.tvToolbarTitle);
		tvRight = (TextView) toolbar.findViewById(R.id.tvToolbarRight);
		tvRight.setText("确定");
		tvRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DisplayTicketActivity.this.finish();
				
			}
		});
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
		showTips();		
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
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		DisplayTicektBean bean = (DisplayTicektBean) adapter.getItem(position);
		String path = bean.getBitmapPath();
		if (TextUtils.isEmpty(path)) { return; }
		Intent intent = new Intent(this, DisplaySingleImageActivity.class);
		intent.putExtra(DisplaySingleImageActivity.IMAGE_PATH, path);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		showTips();
	}

	private void showTips() {
		DialogHelper.showChoiseDialog(this, "是否放弃领券?", new DialogCallbackAndNo() {
			
			@Override
			public void callbackNo() {
				
			}
			
			@Override
			public void callback() {
				DisplayTicketActivity.this.finish();
			}
		});
	}
	
	
	
}
