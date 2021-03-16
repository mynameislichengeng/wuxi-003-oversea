package com.wizarpos.pay.cashier.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.cashier.activity.TransactionActivity;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionFactory;
import com.wizarpos.pay.cashier.presenter.transaction.impl.OtherpayTransactionImpl;
import com.wizarpos.pay.cashier.presenter.transaction.inf.OtherPayTransaction;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.view.adapter.OtherPayAdapter;
import com.motionpay.pay2.lite.R;

public class OtherPayActivity extends TransactionActivity {
	
	private TextView shouldPayAmountTv = null;
	private TextView roundTv = null;
	private LinearLayout llRound;
	
	private OtherPayTransaction transaction;
	private String TAG = "OtherPayActivity";
	private ArrayList<String> recordList;
	private ArrayList<Integer> drawList;
	private String serviceId;
	private String serviceName;
	private String SHOWREMARK = "1";
	private String NONEREMARK = "0";
	private EditText reMarkEt;
	private double round = 0.00;
	private boolean isRound = false;//非四舍五入

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		transaction = TransactionFactory.newOtherPayTransaction(this);
		transaction.handleIntent(getIntent());
		initView();
		initConfig();
		getData();
	}

	private void initView() {
		setMainView(R.layout.activity_other_pay);
		setTitleText(transaction.isMixTransaction() ? getResources().getString(R.string.mix_pay) + getResources().getString(R.string.other_pay)
				: getResources().getString(R.string.other_pay));
		
		roundTv = (TextView) findViewById(R.id.tv_round_amount);
		shouldPayAmountTv = (TextView) findViewById(R.id.tv_shouldpay_amount);
		llRound = (LinearLayout) findViewById(R.id.layout_otherpay_round_head);
		
		showTitleBack();
	}
	
	private void initConfig() {

		//四舍五入 @yaosong [20151103]
		double initAmount = Double.parseDouble(transaction.getInitAmount())/100;
		if (com.wizarpos.pay.common.Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.jiao_round))) {
			isRound = true;
			long inputMoneyRoundJiao = (long) Math.round(initAmount) * 100;
			round = sub(initAmount, inputMoneyRoundJiao / 100);
			llRound.setVisibility(View.VISIBLE);
			roundTv.setText(String.valueOf(round));
			// shouldPayAmountEt.setText(inputMoney);
			shouldPayAmountTv.setText(Tools.formatFen(inputMoneyRoundJiao));
		} else if (com.wizarpos.pay.common.Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.fen_round))) {
			isRound = true;
			double inputMoneyRoundfen = new BigDecimal(initAmount).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
			round = sub(initAmount, inputMoneyRoundfen);
			llRound.setVisibility(View.VISIBLE);
			roundTv.setText(String.valueOf(round));
			// shouldPayAmountEt.setText(inputMoney);
			shouldPayAmountTv.setText(Tools.formatFen((long) (inputMoneyRoundfen * 100)));
		} else {
			isRound = false;
			llRound.setVisibility(View.GONE);
			shouldPayAmountTv.setText(transaction.getInitAmount());
		}
	}

	private void getData() {
		progresser.showProgress();
		((OtherpayTransactionImpl) transaction).setInitListener(new ResultListener() {
			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				Log.e(TAG, String.valueOf(response.result));
				JSONArray jsonArray = (JSONArray) response.getResult();
				final List<String[]> serviceList = new ArrayList<String[]>();
				for (int i = 0; i < jsonArray.size(); i++) {
					String serviceType[] = new String[3];
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					boolean usedFlag = jsonObject.getBooleanValue("usedFlag");
					serviceId = jsonObject.getString("serviceId");
					serviceName = jsonObject.getString("serviceName");
					serviceType[0] = String.valueOf(usedFlag);
					serviceType[1] = serviceName;
					serviceType[2] = serviceId;
					serviceList.add(serviceType);
				}
				GridView gridview = (GridView) findViewById(R.id.gridview);
				recordList = new ArrayList<String>();
				for (int i = 0; i < serviceList.size(); i++) {
					if (serviceList.get(i)[0] != null && "true".equals(serviceList.get(i)[0])) {
						recordList.add(serviceList.get(i)[1]);
					}
				}
				drawList = new ArrayList<Integer>();
				getColorBackgroud();
				gridview.setAdapter(new OtherPayAdapter(recordList, drawList, OtherPayActivity.this));
				gridview.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
						try {
							serviceId = serviceList.get(position)[2];
							serviceName = serviceList.get(position)[1];
							if (SHOWREMARK.equals(AppConfigHelper.getConfig(AppConfigDef.isRemark))) {
								showRemark(serviceList, position);
							} else {
								progresser.showProgress();
								doPay();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				});
			}

			private void getColorBackgroud() {
				drawList.add(R.drawable.btn_selector_color_red);
				drawList.add(R.drawable.btn_selector_color_blue);
				drawList.add(R.drawable.btn_selector_color_green);
				drawList.add(R.drawable.btn_selector_color_lake_blue);
				drawList.add(R.drawable.btn_selector_color_red);
				drawList.add(R.drawable.btn_selector_color_blue);
				drawList.add(R.drawable.btn_selector_color_green);
				drawList.add(R.drawable.btn_selector_color_lake_blue);
				drawList.add(R.drawable.btn_selector_color_lake_blue);
			}

			@Override
			public void onFaild(Response response) {
				Log.e(TAG, String.valueOf(response.result));
			}
		});
	}


	private void showRemark(final List<String[]> serviceList, final int position) {
		new AlertDialog.Builder(OtherPayActivity.this).setTitle("备注").setIcon(android.R.drawable.ic_dialog_info)
				.setView(reMarkEt = new EditText(OtherPayActivity.this)).setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						serviceId = serviceList.get(position)[2];
						progresser.showProgress();
						doPay();
					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
	}
	
	private void doPay() {
		//非四舍五入金额问题fix @yaosong[20151111]
		if (isRound){
			transaction.setRealAmount(Tools.toIntMoney(shouldPayAmountTv.getText().toString())+"");
		}else{
			transaction.setRealAmount(transaction.getInitAmount());
		}
		transaction.setRound(Tools.toIntMoney(roundTv.getText().toString())+"");
		transaction.setServiceId(serviceId);
		transaction.setServiceName(serviceName);
		if (SHOWREMARK.equals(AppConfigHelper.getConfig(AppConfigDef.isRemark)) && null != reMarkEt) {
			transaction.setMark(reMarkEt.getText().toString());
		}
		transaction.trans(new ResultListener() {
			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				Log.e(TAG, String.valueOf(response.result));
				Intent intent = (Intent) response.result;
				toTransactionSuccess(OtherPayActivity.this, intent);
				OtherPayActivity.this.finish();
			}

			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				Log.e(TAG, String.valueOf(response.result));
				UIHelper.ToastMessage(OtherPayActivity.this, response.msg);
			}
		});
	}
}
