package com.wizarpos.pay.cashier.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.payment.impl.CardPayment;
import com.wizarpos.pay.cashier.presenter.ticket.util.TicketManagerUtil;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionFactory;
import com.wizarpos.pay.cashier.presenter.transaction.impl.CardTransactionImpl;
import com.wizarpos.pay.cashier.presenter.transaction.inf.CardTransaction;
import com.wizarpos.pay.cashier.view.input.InputInfoActivity;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.view.fragment.InputPadFragment;
import com.wizarpos.pay.view.fragment.OnMumberClickListener;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.wizarpos.pay.view.util.DialogHelper2.DialogChoiseListener;
import com.wizarpos.pay.view.util.DialogHelper2.DialogListener;
import com.motionpay.pay2.lite.R;

import java.util.ArrayList;
import java.util.List;

public class BankCardPayActivity extends TransactionActivity implements OnMumberClickListener, ResultListener, CardPayment.CardPaymetProgressListener {
	public static final String SWIPE_BANK_JSONARRAY = "SWIPE_BANK_JSONARRAY";
	public static final String SWIPE_BANK_NO = "SWIPE_BANK_NO";
	public static final String HAS_ICCARD = "HAS_ICCARD";

	private ArrayList<String> swipeBankJSONArray;//银行卡磁道信息
	private String swipeBankCardNo;//银行卡号
	private boolean HasICCard;//是否插入IC

	private CardTransaction cardTransaction;
	private InputPadFragment inputPadFragment;
	private TextView tvInitAmount, tvReduceAmount, tvShouldAmount;
	private EditText etRealAmount;
	private String CASHPAY = "1003";
	private TicketInfo ticket;
	private int TICKET_INFO = 10002;
	private boolean isScan = false;// 是否是扫描

	private Dialog cardlinkDialog;

	/**
	 * 券改造
	 */
	private List<TicketInfo> addTicketList = new ArrayList<TicketInfo>();

	final String CARD_LINK_PAYMENT = "CARD_LINK_PAYMENT";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		swipeBankJSONArray = (ArrayList<String>) getIntent().getSerializableExtra(SWIPE_BANK_JSONARRAY);
		swipeBankCardNo = (String) getIntent().getSerializableExtra(SWIPE_BANK_NO);
		HasICCard = getIntent().getBooleanExtra(HAS_ICCARD, false);
		cardTransaction = TransactionFactory.newCardTransaction(this);
		cardTransaction.setCardPaymetProgressListener(this);
		cardTransaction.handleIntent(getIntent());

		initView();
	}

	private void initView() {
		setMainView(R.layout.bank_pay);
		setTitleText(cardTransaction.getTransactionType() == TransactionTemsController.TRANSACTION_TYPE_MIXPAY ? getResources().getString(R.string.mix_pay)
				+ getResources().getString(R.string.card_bank_pay_title) : getResources().getString(R.string.card_bank_pay_title));
		showTitleBack();
		setTitleRight("使用优惠券");
		inputPadFragment = InputPadFragment.newInstance(InputPadFragment.KEYBOARDTYPE_SIMPLE);
		getSupportFragmentManager().beginTransaction().replace(R.id.flInputPad, inputPadFragment).commit();
		inputPadFragment.setOnMumberClickListener(this);
		if (cardTransaction.getTransactionType() == TransactionTemsController.TRANSACTION_TYPE_MIXPAY) {
			setTitleRightVisible(false);
		}

		tvInitAmount = (TextView) findViewById(R.id.tv_cardpay_init_amount);
		tvReduceAmount = (TextView) findViewById(R.id.tv_cardpay_rebate);
		etRealAmount = (EditText) findViewById(R.id.et_cardpay_scan_card);
		tvInitAmount.setText(Calculater.formotFen(cardTransaction.getInitAmount()));
		tvReduceAmount.setText(Calculater.formotFen(cardTransaction.getReduceAmount()));
		etRealAmount.setText(Calculater.formotFen(cardTransaction.getShouldAmount()));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		inputPadFragment.setEditView(etRealAmount, com.wizarpos.pay.view.fragment.InputPadFragment.InputType.TYPE_INPUT_MONEY);

	}

	@Override
	protected void onTitleRightClicked() {
		toInputView(this, "券使用", false, true, true, new Intent(), TICKET_INFO); //用券界面修改 wu
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0 == TICKET_INFO && arg1 == RESULT_OK) { //调用摄像头逻辑调整 wu@[20150827]
			// 券号
			try {
				String ticketNum = arg2.getStringExtra(InputInfoActivity.content);
				if (TextUtils.isEmpty(ticketNum)) {
					return;
				}
				isScan = (arg2.getIntExtra(InputInfoActivity.type, 0) == InputInfoActivity.INPUT_TYPE_CAMERA);
				getTicketInfo(ticketNum);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
	}

	private void getTicketInfo(String ticketNum) {
		progresser.showProgress();
		cardTransaction.getCommonTicketInfo(ticketNum, cardTransaction.getShouldAmount(), new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				ticket = (TicketInfo) response.result;
				String msg = ticket.getTicketDef().getTicketName() + "\n" + Calculater.formotFen(ticket.getTicketDef().getBalance() + "");
				DialogHelper2.showChoiseDialog(BankCardPayActivity.this, msg, new DialogChoiseListener() {

					public void onNo() {

					}

					@Override
					public void onOK() {
						Response mResponse = TicketManagerUtil.verifyAddTicket(ticket, addTicketList);
						if (mResponse.code != 0) {
							Toast.makeText(BankCardPayActivity.this, mResponse.msg, Toast.LENGTH_SHORT).show();
							return;
						}
						Response addCommonTicketResponse = cardTransaction.addCommonTicket(ticket, isScan);
						addTicketList.add(ticket);
						if (addCommonTicketResponse.code == 0) {
							tvInitAmount.setText(Calculater.formotFen(cardTransaction.getInitAmount()));
							tvReduceAmount.setText(Calculater.formotFen(cardTransaction.getReduceAmount()));
							etRealAmount.setText(Calculater.formotFen(cardTransaction.getShouldAmount()));
						} else {
							UIHelper.ToastMessage(BankCardPayActivity.this, addCommonTicketResponse.msg);
						}

					}

				});
			}

			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(BankCardPayActivity.this, response.msg);
			}
		});

	}

	private void trans() {
		String realAmount = etRealAmount.getText().toString();
		if (realAmount.equals("0.00")) {
//			realAmount = cardTransaction.getInitAmount();
			realAmount = cardTransaction.getShouldAmount(); //Bugfix 应该是取应收金额 wu@[20150929]
		} else {
			realAmount = Calculater.formotYuan(realAmount);
		}
		progresser.showProgress();
		cardTransaction.setRealAmount(realAmount);
		cardTransaction.trans(this);

	}

	@Override
	public void onSubmit() {
		trans();
	}

	@Override
	protected void onDestroy() {
		cardTransaction.onDestory();
		super.onDestroy();
	}


	@Override
	public void onSuccess(Response response) {
		progresser.showContent();
		hideCardlinkDialog();
		Intent intent = (Intent) response.result;
		if (cardTransaction.getTransactionType() == TransactionTemsController.TRANSACTION_TYPE_MIXPAY) {
			setResult(RESULT_OK, intent);
			BankCardPayActivity.this.finish();
		} else {
			toTransactionSuccess(BankCardPayActivity.this, intent);
			BankCardPayActivity.this.finish();
		}

	}

	@Override
	public void onFaild(Response response) {
		progresser.showContent();
		hideCardlinkDialog();
		int errorCode = response.getCode();
		if (errorCode == CardTransactionImpl.CALL_700_FAILED) {
			//700接口调用失败@yaosong [20151030]
			DialogHelper2.showDialog(BankCardPayActivity.this, "网络请求失败，请点击确认重试。", new DialogListener() {

				@Override
				public void onOK() {
					((CardTransactionImpl) cardTransaction).reTryUploadTrans(BankCardPayActivity.this);
				}

			});
		}
		UIHelper.ToastMessage(BankCardPayActivity.this, response.msg);
	}

	@Override
	public void onProgress(String progress, boolean continueTrans) {
		if(this.isFinishing()){
			return;
		}
		hideCardlinkDialog();
		MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
		builder.content(progress);
		builder.cancelable(false);
		builder.positiveText("取消");
		builder.onPositive(new MaterialDialog.SingleButtonCallback() {
			@Override
			public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
				materialDialog.dismiss();
				cardTransaction.endTrans();
				back();
			}
		});
		if(continueTrans){
			builder.negativeText("确定");
			builder.onNegative(new MaterialDialog.SingleButtonCallback() {
				@Override
				public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
					materialDialog.dismiss();
					cardTransaction.continueTrans();
				}
			});
		}
		cardlinkDialog = builder.show();
	}

	private void hideCardlinkDialog() {
		if(cardlinkDialog != null && cardlinkDialog.isShowing()){
			cardlinkDialog.dismiss();
		}
	}
}
