package com.wizarpos.pay.cashier.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.cashier.activity.TransactionActivity;
import com.wizarpos.pay.cashier.model.TicketDef;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.ticket.util.TicketManagerUtil;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionFactory;
import com.wizarpos.pay.cashier.presenter.transaction.impl.CashTransactionImpl;
import com.wizarpos.pay.cashier.presenter.transaction.inf.CashTransaction;
import com.wizarpos.pay.cashier.thrid_app_controller.ThirdAppTransactionController;
import com.wizarpos.pay.cashier.view.input.InputInfoActivity;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.view.fragment.InputPadFragment;
import com.wizarpos.pay.view.fragment.InputPadFragment.OnKeyChangedListener;
import com.wizarpos.pay.view.fragment.OnMumberClickListener;
import com.wizarpos.pay2.lite.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CashPayActivity extends TransactionActivity implements OnMumberClickListener, OnKeyChangedListener {
	private CashTransaction transaction;
	private InputPadFragment inputPadFragment;
	TextView tvInitAmount, tvReduceAmount, tvChargeAmount, tvShouldAmount;
	EditText etRealAmount, etTicketNum;
	ArrayList<String> cashList = new ArrayList<String>();
	String ticketNum;
	private int TICKET_INFO = 10001;
	private static String initAmount = "", realAmount = "", changeAmount = "", shouldAmount = "";
	
	//四舍五入相关
	private TextView roundTv;
	private LinearLayout llRound;
	private double round = 0.00;
	
	private boolean isScan = false;// 是否是扫描
	
	private List<TicketInfo> addTicketList = new ArrayList<TicketInfo>();
	
	// private RelativeLayout relativeLayout;
	// private LinearLayout llLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		transaction = TransactionFactory.newCashTransaction(this);
		transaction.handleIntent(getIntent());
		initView();
		int[] btnIds = { R.id.btn_confirm, R.id.btn_scan_tickets};
		setOnClickListenerByIds(btnIds, this);
		initConfig();
	}

	private void initConfig() {
		
		//四舍五入 @yaosong [20151103]
		double initAmount = Double.parseDouble(this.initAmount)/100;
		if (com.wizarpos.pay.common.Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.jiao_round))) {
			long inputMoneyRoundJiao = (long) Math.round(initAmount) * 100;
			round = sub(initAmount, inputMoneyRoundJiao / 100);
			llRound.setVisibility(View.VISIBLE);
			roundTv.setText(String.valueOf(round));
			transaction.setRound(round);
			tvShouldAmount.setText(Tools.formatFen(inputMoneyRoundJiao));
		} else if (com.wizarpos.pay.common.Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.fen_round))) {
			double inputMoneyRoundfen = new BigDecimal(initAmount).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
			round = sub(initAmount, inputMoneyRoundfen);
			llRound.setVisibility(View.VISIBLE);
			roundTv.setText(String.valueOf(round));
			((CashTransactionImpl)transaction).setRound(round);
			tvShouldAmount.setText(Tools.formatFen((long) (inputMoneyRoundfen * 100)));
		} else {
			llRound.setVisibility(View.GONE);
		}
	}

	private void initView() {
		setMainView(R.layout.cash_pay);
		showTitleBack();
		setTitleText(transaction.isMixTransaction() ? getResources().getString(R.string.mix_pay) + getResources().getString(R.string.cashier_pay_title)
				: getResources().getString(R.string.cashier_pay_title));
		String recharge = getIntent().getStringExtra(ThirdAppTransactionController.RECHARGE);
		if (transaction.isMixTransaction() == false){
			setTitleRight("使用优惠券");
		}
		if(!TextUtils.isEmpty(recharge)&&ThirdAppTransactionController.RECHARGE.equals(recharge)){
			hideTitleRightText();
		}
		inputPadFragment = InputPadFragment.newInstance(InputPadFragment.KEYBOARDTYPE_SIMPLE);
		getSupportFragmentManager().beginTransaction().replace(R.id.flInputPad, inputPadFragment).commit();
		inputPadFragment.setOnMumberClickListener(this);
		inputPadFragment.setOnTextChangedListener(this);
		tvInitAmount = (TextView) findViewById(R.id.tv_cashier);
		tvReduceAmount = (TextView) findViewById(R.id.tv_rebate);
		tvChargeAmount = (TextView) findViewById(R.id.tv_charge);
		tvShouldAmount = (TextView) findViewById(R.id.tv_accounts);
		etRealAmount = (EditText) findViewById(R.id.et_received);
		// etRealAmount = (EditText) findViewById(R.id.et_received);
		// etTicketNum = (EditText) findViewById(R.id.et_ticket_num);
		initAmount = transaction.getInitAmount();
		realAmount = transaction.getRealAmount();
		changeAmount = transaction.getChangeAmount();
		shouldAmount = transaction.getShouldAmount();

		tvInitAmount.setText(Calculater.formotFen(initAmount));
		tvReduceAmount.setText(Calculater.formotFen(transaction.getReduceAmount()));
		tvChargeAmount.setText(Calculater.formotFen(changeAmount));
		tvShouldAmount.setText(Calculater.formotFen(shouldAmount));
		etRealAmount.setText(Calculater.formotFen(realAmount));
		
		//四舍五入相关
		llRound = (LinearLayout) findViewById(R.id.ll_round_amount);
		roundTv = (TextView) findViewById(R.id.tv_round);
		
		// etTicketNum.setText("");
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		inputPadFragment.setEditView(etRealAmount, com.wizarpos.pay.view.fragment.InputPadFragment.InputType.TYPE_INPUT_MONEY);
	}

//	@Override
//	public void onClick(View view) {
//		super.onClick(view);
//		switch (view.getId()) {
//		case R.id.key_ok:
//			new Handler().post(new Runnable() {
//
//				@Override
//				public void run() {
//					trans();
//				}
//			});
//			break;
//		}
//	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0 == TICKET_INFO && arg1 == RESULT_OK) { //调用摄像头逻辑调整 wu@[20150827]
			// 券号
			try { 
				ticketNum = arg2.getStringExtra(InputInfoActivity.content);
				if(TextUtils.isEmpty(ticketNum)){
					return;
				}
				isScan = (arg2.getIntExtra(InputInfoActivity.type, 0) == InputInfoActivity.INPUT_TYPE_CAMERA);
//				ticketNum = arg2.getStringExtra("ticketNo");
//				scanTicketNo = arg2.getStringExtra("scanTicketNo");
				getTicketInfo(ticketNum);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 支付
	 */
	private void trans() {
		String realAmount = etRealAmount.getText().toString();
		if (realAmount.equals("0.00")) {
			realAmount = transaction.getShouldAmount(); //Bugfix 应该是取应收金额 wu@[20150929]
		} else {
			realAmount = Calculater.formotYuan(realAmount);
		}
		transaction.setRealAmount(realAmount);
		progresser.showProgress();
		transaction.trans(new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				Intent intent = (Intent) response.result;
				if (transaction.isMixTransaction()) {
					setResult(RESULT_OK, intent);
					CashPayActivity.this.finish();
				} else {
					toTransactionSuccess(CashPayActivity.this, intent);
					CashPayActivity.this.finish();
				}
			}

			public void onFaild(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(CashPayActivity.this, response.msg);
			}
		});
	}

	/**
	 * 获取券信息
	 * 
	 * @param ticketNum
	 */
	private void getTicketInfo(String ticketNum) {
		progresser.showProgress();
		transaction.getTicketInfo(ticketNum, shouldAmount, new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				showTicketInfo((TicketInfo) response.result);
			}

			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(CashPayActivity.this, response.msg);
			}
		});
	}

	/**
	 * 显示券的信息
	 * 
	 * @param ticket
		 */
		private void showTicketInfo(final TicketInfo ticket) {
		/** 显示微信券信息 */
		AlertDialog.Builder builder = new Builder(CashPayActivity.this);
		if(TicketDef.TICKET_TYPE_GIFT.equals(ticket.getTicketDef().getTicketType())){
			builder.setMessage(ticket.getTicketDef().getTicketName());
		}else if (TicketDef.TICKET_TYPE_DISCOUNT.equals(ticket.getTicketDef().getTicketType())) {//折扣券和礼品券不显示金额@hong[20151230]
			builder.setMessage(ticket.getTicketDef().getTicketName() + "\n" + Calculater.formotFen(ticket.getTicketDef().getBalance() + "") + "元");
		}else {
			builder.setMessage(ticket.getTicketDef().getTicketName() + "\n" + Calculater.formotFen(ticket.getTicketDef().getBalance() + "") + "元");
		}
		builder.setTitle(getResources().getString(R.string.ticket_info));
		builder.setPositiveButton(getResources().getString(R.string.ok), new OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				Response mResponse = TicketManagerUtil.verifyAddTicket(ticket, addTicketList);
				if(mResponse.code != 0) {
					Toast.makeText(CashPayActivity.this, mResponse.msg, Toast.LENGTH_SHORT).show();
					return;
				}
				Response response = transaction.addTicket(ticket, isScan);
				addTicketList.add(ticket);
				Toast.makeText(CashPayActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();//? wu
				if (response.getCode() == 0) {
					updateView();
				}
			}
		});
		builder.setNegativeButton(getResources().getString(R.string.ticket_cancle), new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				finish();

			}
		});
		builder.create().show();

	}

	@Override
	protected void onTitleRightClicked() {
//		Intent intent = new Intent(CashPayActivity.this, InputCardNumActivity.class);
//		intent.putExtra(InputCardNumActivity.TITLE, "券使用");
//		intent.putExtra(InputCardNumActivity.IS_USE_SWIPE, false);
		
		if(Constants.TRUE.equals(AppStateManager.getState(AppStateDef.isOffline, Constants.FALSE)))
		{
			Toast.makeText(this, "离线状态下不能使用券", Toast.LENGTH_SHORT).show();
		}else
		{
			if (DeviceManager.DEVICE_TYPE_OTHER==DeviceManager.getInstance().getDeviceType()) {
				toInputTicketView(this, "券使用", false, true, true, new Intent(), TICKET_INFO); //用券界面修改 hong@[20150924]
			}else {
				toInputView(this, "券使用", false, true, true, new Intent(), TICKET_INFO); //调用摄像头逻辑调整 wu@[20150827]
			}
		
		}
//		startActivityForResult(intent, TICKET_INFO);

	}

	/**
	 * 
	 * @Author:Huangweicai
	 * @Date:2015-7-20 下午5:28:07
	 * @Reason:添加券成功后更新UI
	 */
	private void updateView() {
		 initAmount = transaction.getInitAmount();
		 realAmount = transaction.getRealAmount();
		 changeAmount = transaction.getChangeAmount();
		 shouldAmount = transaction.getShouldAmount();
		 String reduceAmount = transaction.getReduceAmount();
		 tvInitAmount.setText(Calculater.formotFen(initAmount));
		 tvReduceAmount.setText(Calculater.formotFen(reduceAmount));
		 tvChargeAmount.setText(Calculater.formotFen(changeAmount));
		 tvShouldAmount.setText(Calculater.formotFen(shouldAmount));
		 etRealAmount.setText(Calculater.formotFen(realAmount));
	}

	@Override
	public void onSubmit() {
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				String realAmount = etRealAmount.getText().toString();
				if (realAmount.equals("0.00")) {
					realAmount = transaction.getShouldAmount(); //Bugfix 应该是取应收金额 wu@[20150929]
				} else {
					realAmount = Calculater.formotYuan(realAmount);
				}
				progresser.showProgress();
				transaction.setRealAmount(realAmount);
				transaction.derate(new ResultListener() {
					
					@Override
					public void onSuccess(Response response) {
						String amount=response.msg.toString();
						if (amount.contains("-")) {
							showDerate(amount);
						}else {
							trans();
						}
					}
					
					@Override
					public void onFaild(Response response) {
						
					}
				});
			}
		});

	}

	@Override
	public void onTextChanged(String newStr) {
		calChange(TextUtils.isEmpty(newStr)? "0.00":newStr);
	}
	private void showDerate(String amount){//显示减免金额
		AlertDialog.Builder builder = new Builder(CashPayActivity.this);
		builder.setTitle("减免金额")
		.setMessage("是否减免金额：" + Calculater.formotFen(amount.replace("-", "")) + "元?")
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				trans();
			}
		})
		.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.create().show();
	}
	/**
	 * 计算找零
	 */
	private void calChange(String payAmount) {
		// 应收金额 <= 收银金额
		// 找零 = 实收金额 - 应收金额
		String shouldPayAmount = tvShouldAmount.getText().toString();
		BigDecimal changeAmount = new BigDecimal(payAmount).subtract(new BigDecimal(shouldPayAmount)).setScale(2, BigDecimal.ROUND_UNNECESSARY);
		// 组合支付时,找零金额不能小于0
		if (transaction.isMixTransaction() && changeAmount.compareTo(new BigDecimal(0)) < 0) {
			tvChargeAmount.setText("0.00");
		} else {
			tvChargeAmount.setText(changeAmount.toString());
		}

	}

}
