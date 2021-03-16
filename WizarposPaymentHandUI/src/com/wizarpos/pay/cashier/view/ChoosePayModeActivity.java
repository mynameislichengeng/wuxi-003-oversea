package com.wizarpos.pay.cashier.view;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.wizarpos.pay.view.TransTypeItem;
import com.wizarpos.pay.view.adapter.CashierPayItemAdapter;
import com.motionpay.pay2.lite.R;

/**
 * 选择支付方式
 * 
 * @author wu
 * 
 */
public class ChoosePayModeActivity extends Activity implements OnClickListener, OnItemClickListener {

	public static final String TRANS_TYPES = "transTypes";

	private List<TransTypeItem> transTypes = null;

	private CashierPayItemAdapter payItemsAdapter;
	private ListView lvChoosePayMode;
	/** 选中下标 */
	private int curPosition = -1;
	
	private View mView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFinishOnTouchOutside(true);
		setContentView(R.layout.activity_choosepaymode);
		initView();
		transTypes = (List<TransTypeItem>) getIntent().getSerializableExtra(TRANS_TYPES);
		lvChoosePayMode = (ListView) findViewById(R.id.lvChoosePayMode);
		findViewById(R.id.tvChoosePayModeCancel).setOnClickListener(this);
		findViewById(R.id.tvChoosePayModeSubmit).setOnClickListener(this);
		payItemsAdapter = new CashierPayItemAdapter(this);
		lvChoosePayMode.setAdapter(payItemsAdapter);
		if (transTypes != null && !transTypes.isEmpty()) {
			payItemsAdapter.setDataChanged(transTypes);
		}
		lvChoosePayMode.setOnItemClickListener(this);
	}
	
	private void initView()
	{
		mView = this.findViewById(R.id.mView);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvChoosePayModeCancel:
			this.finish();
			break;
		case R.id.tvChoosePayModeSubmit:
			doSubmit();
			break;
		default:
			break;
		}
	}

	private void doSubmit() {
		if (curPosition == -1) {
			Toast.makeText(this, "未选中条目", 0).show();
			return;
		}
		Intent intent = new Intent();
		intent.putExtra("payMode", (Serializable) transTypes.get(curPosition));
		intent.putExtra("position", curPosition);
		setResult(RESULT_OK, intent);
		this.finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		curPosition = position;
		Intent intent = new Intent();
		intent.putExtra("payMode", (Serializable) transTypes.get(position));
		intent.putExtra("position", curPosition);
		setResult(RESULT_OK, intent);
		this.finish();
	}

	@Override
	public void finish() {
		super.finish();
		this.overridePendingTransition(R.anim.activity_close_up_btm, R.anim.activity_close_up_btm);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int top = mView.getTop();  
		int bottom = mView.getBottom();
		
		int y=(int) event.getY();  
		if(event.getAction()==MotionEvent.ACTION_UP){
			if(y> top || y < bottom)
			{
				this.finish();
			}
		}                 
		return true;  
	}
	

}
