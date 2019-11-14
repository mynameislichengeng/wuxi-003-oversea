package com.wizarpos.pay.cashier.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.cashier.activity.TicketUseActivity;
import com.wizarpos.pay.cashier.activity.TransactionActivity;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionFactory;
import com.wizarpos.pay.cashier.presenter.transaction.inf.MemberTransaction;
import com.wizarpos.pay.cashier.view.input.InputInfoActivity;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay2.lite.R;

public class MemberCardDetailActivity extends TransactionActivity implements MemberTransaction.MemberTransactionListener {
	private TextView tvInitAmount, tvReduceAmount, tvScanCardAmount;
	static String initAmount = "", ReduceAmount = "", scanCardAmount = "", cardNum = "", tokenId = "";
	private List<TicketInfo> tickets = new ArrayList<TicketInfo>();
//	private List<TicketInfo> usedTickets = new ArrayList<TicketInfo>();
	private MemberTransaction transcation;

	private final int ACTIVITY_RESUL＿MEMBER_TICKET = 1;// 添加会员券
	private final int ACTIVITY_RESUL＿ADD_WEPAY_TICKET = 1002;// 添加微信券
	private final int ACTIVITY_RESUL＿ADD_COMMON_TICKET = 1003;// 添加普通券
	private String MEMBERPAY = "1002";
	private TextView tvCardBlacne;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		transcation = TransactionFactory.newMemberTransaction(this);
		setMainView(R.layout.member_card_detail_activity);
		setTitleText(transcation.isMixTransaction() ? getResources().getString(R.string.mix_pay) + getResources().getString(R.string.member_card_title)
				: getResources().getString(R.string.member_card_title));
		progresser.showProgress();
		transcation.handleIntent(getIntent());
		initView();
		initData();
	}

	private void initView() {
		showTitleBack();
		tvInitAmount = (TextView) findViewById(R.id.tv_init_amount);
		tvReduceAmount = (TextView) findViewById(R.id.tv_reduce_amount);
		tvScanCardAmount = (TextView) findViewById(R.id.tv_scan_card);
		tvCardBlacne = (TextView) findViewById(R.id.tvBalance);
		// findViewById(R.id.btn_add_ticket).setVisibility(View.VISIBLE);
		int[] btnIds = { R.id.btn_pay, R.id.btn_add_member_ticket, R.id.btn_add_common_ticket, R.id.btn_add_wepay_ticket };
		setOnClickListenerByIds(btnIds, this);
	}

	private void initData() {
		initAmount = getIntent().getStringExtra("initAmount");
		cardNum = getIntent().getStringExtra("cardNo");
		tokenId = getIntent().getStringExtra("tokenId");
		tvInitAmount.setText(Calculater.formotFen(transcation.getInitAmount()));	//bugfix yaosong [20160114]
		tvReduceAmount.setText(Calculater.formotFen(transcation.getReduceAmount()));
		tvScanCardAmount.setText(Calculater.formotFen(transcation.getShouldAmount()));
		// doPayDetail(cardNum, initAmount);
		if (transcation.isMixTransaction()) {
			findViewById(R.id.btn_add_member_ticket).setVisibility(View.GONE);
			findViewById(R.id.btn_add_common_ticket).setVisibility(View.GONE);
			findViewById(R.id.btn_add_wepay_ticket).setVisibility(View.GONE);
		}
	}

	/**
	 * 更新会员卡余额
	 */
	private void updateMemberCardBalance() {
		try {
			String balance = transcation.getMemeberCardInfo().getBalance();
			tvCardBlacne.setText(Calculater.formotFen(balance));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.btn_pay:
			trans();
			break;
		case R.id.btn_add_member_ticket:// 添加会员券
			addMemberTicket();
			break;
		case R.id.btn_add_common_ticket:// 添加普通券
			addCommonTicket();
			break;
		case R.id.btn_add_wepay_ticket:// 添加微信券
			addWepayTicket();
			break;
		}
	}

	private void addWepayTicket() {
		toInputView(MemberCardDetailActivity.this, "券扫描", false, false, true, new Intent(), ACTIVITY_RESUL＿ADD_WEPAY_TICKET);
	}

	private void addCommonTicket() {
//		Intent intent = new Intent(MemberCardDetailActivity.this, ScanTicketUseActivity.class);
//		intent.putExtra("paystype", MEMBERPAY);
//		startActivityForResult(intent, ACTIVITY_RESUL＿ADD_COMMON_TICKET);
		
		toInputView(MemberCardDetailActivity.this, "券扫描", false, true, true, new Intent(), ACTIVITY_RESUL＿ADD_COMMON_TICKET);
	}

	private void addMemberTicket() {
		if (tickets.isEmpty()) {
			UIHelper.ToastMessage(MemberCardDetailActivity.this, getResources().getString(R.string.no_use_ticket));
			return;
		}
		Intent intent = new Intent(MemberCardDetailActivity.this, TicketUseActivity.class);
		intent.putExtra("allTickets", (Serializable) tickets); // 所有券
		intent.putExtra("initAmount", transcation.getInitAmount());
		startActivityForResult(intent, ACTIVITY_RESUL＿MEMBER_TICKET);
	}

	private void trans() {
		progresser.showProgress();
		transcation.trans(new ResultListener() {
			@Override
			public void onSuccess(Response response) {
//				progresser.showContent();//会员卡支付页面确定支付后先刷新到会员卡支付界面再跳到支付成功页面 @yaosong[20151107]
				Intent intent = (Intent) response.result;
				if (transcation.isMixTransaction()) {
					setResult(RESULT_OK, intent);
					MemberCardDetailActivity.this.finish();
				} else {
					toTransactionSuccess(MemberCardDetailActivity.this, intent);
					MemberCardDetailActivity.this.finish();
				}
			}

			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(MemberCardDetailActivity.this, response.msg);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) { return; }
		if (requestCode == ACTIVITY_RESUL＿MEMBER_TICKET) {// 添加会员券
			transcation.removeAllMemberTciket();// 清空所有已添加的券
			List<TicketInfo> ticketInfo = (List<TicketInfo>) data.getSerializableExtra("addCommonTicket");
			for (int i = 0; i < ticketInfo.size(); i++) {
				Response response = transcation.addMemberTicket(ticketInfo.get(i), false);
				if(response.code != 0){
					UIHelper.ToastMessage(MemberCardDetailActivity.this, response.msg);
					transcation.removeAllMemberTciket();// 清空所有已添加的券
					return;
				}
			}
			tvInitAmount.setText(Tools.formatFen(Long.parseLong(transcation.getInitAmount())));
			tvReduceAmount.setText(Tools.formatFen(Long.parseLong(transcation.getReduceAmount())));
			tvScanCardAmount.setText(Tools.formatFen(Long.parseLong(transcation.getShouldAmount())));

		} else if (requestCode == ACTIVITY_RESUL＿ADD_WEPAY_TICKET) {// 微信券
//			Response response = (Response) data.getSerializableExtra("result");
//			String result = response.getResult().toString();
//			getWepayTicketInfo(data.getStringExtra(InputInfoActivity.content));
			String ticketNum = data.getStringExtra(InputInfoActivity.content);
			if(TextUtils.isEmpty(ticketNum)){
				return;
			}
			boolean isScan = true ;
			getWepayTicketInfo(ticketNum, isScan);
		} else if (requestCode == ACTIVITY_RESUL＿ADD_COMMON_TICKET) {// 普通券
			// String result = data.getStringExtra("scanResult");
//			String scanTicketNo = data.getStringExtra("scanTicketNo");
//			String ticketNo = data.getStringExtra("ticketNo");
//			if (scanTicketNo != null) {
//				getCommonTicketInfo(scanTicketNo, true);
//			} else if (ticketNo != null) {
//				getCommonTicketInfo(ticketNo, false);
//			}
			String ticketNum = data.getStringExtra(InputInfoActivity.content);
			if(TextUtils.isEmpty(ticketNum)){
				return;
			}
			boolean isScan = (data.getIntExtra(InputInfoActivity.type, 0) == InputInfoActivity.INPUT_TYPE_CAMERA);
			getCommonTicketInfo(ticketNum, isScan);
		}
	}

	/**
	 * 现采用503(与非会员相同)接口去查询微信券
	 * @param ticketNum
	 * @param isScan
	 */
	private void getWepayTicketInfo(String code,final boolean isScan) {
		transcation.getWepayTicketInfo(code,  new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				final TicketInfo ticket = (TicketInfo) response.getResult();
				/** 显示普通券信息 */
				AlertDialog.Builder builder = new Builder(MemberCardDetailActivity.this);
				builder.setTitle("券的信息");
				builder.setMessage(ticket.getTicketDef().getTicketName() + "\n" + Calculater.formotFen(ticket.getTicketDef().getBalance()+"") + "元");
				builder.setPositiveButton("确认", new OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// 添加微信券
						addWepayTicket(ticket);
					}
				});
				builder.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						finish();
					}
				});
				builder.create().show();
			}

			@Override
			public void onFaild(Response response) {
				UIHelper.ToastMessage(MemberCardDetailActivity.this, response.msg);
			}
		});
	}

	/**
	 * @deprecated 此方法调用519接口已过时，现采用503(与非会员相同)接口去查询微信券
	 * @param code
	 */
	private void getWepayTicketInfo(String code) {
		transcation.getWepayTicketInfo(code, new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				final TicketInfo ticket = (TicketInfo) response.result;
				/** 显示微信券信息 */
				AlertDialog.Builder builder = new Builder(MemberCardDetailActivity.this);
				builder.setMessage(Tools.formatFen(ticket.getTicketDef().getBalance()) + "元");
				builder.setTitle("券的信息");
				builder.setPositiveButton("确认", new OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// 添加微信券
						addWepayTicket(ticket);
					}
				});
				builder.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						finish();

					}
				});
				builder.create().show();
			}

			@Override
			public void onFaild(Response response) {
				UIHelper.ToastMessage(MemberCardDetailActivity.this, response.msg);
			}
		});

	}

	protected void addWepayTicket(TicketInfo ticket) {
		transcation.addWepayTicket(ticket, true, new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				UIHelper.ToastMessage(MemberCardDetailActivity.this, "微信券添加成功");
				tvReduceAmount.setText(Tools.formatFen(Long.parseLong(transcation.getReduceAmount())));
				tvScanCardAmount.setText(Tools.formatFen(Long.parseLong(transcation.getShouldAmount())));
			}

			@Override
			public void onFaild(Response response) {
				UIHelper.ToastMessage(MemberCardDetailActivity.this, response.msg);
			}
		});
	}

	private void getCommonTicketInfo(String cardNo, final boolean isFromScan) {
		transcation.getCommonTicketInfo(cardNo, new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				final TicketInfo ticket = (TicketInfo) response.getResult();
				/** 显示普通券信息 */
				AlertDialog.Builder builder = new Builder(MemberCardDetailActivity.this);
				builder.setTitle("券的信息");
				builder.setMessage(ticket.getTicketDef().getTicketName() + "\n" + Calculater.formotFen(ticket.getTicketDef().getBalance()+"") + "元");
				builder.setPositiveButton("确认", new OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// 添加普通券
						Response addCommonTicketResponse = transcation.addCommonTicket(ticket, isFromScan);
						if (addCommonTicketResponse.code!=0) {
							UIHelper.ToastMessage(MemberCardDetailActivity.this, addCommonTicketResponse.msg);
						} else{
							tvReduceAmount.setText(Calculater.formotFen(transcation.getReduceAmount()));
							tvScanCardAmount.setText(Calculater.formotFen(transcation.getShouldAmount()));
						}
					}
				});
				builder.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						finish();
					}
				});
				builder.create().show();
			}

			@Override
			public void onFaild(Response response) {
				UIHelper.ToastMessage(MemberCardDetailActivity.this, response.msg);
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onInit(Response response) {
		progresser.showContent();
		if (response.code == 0) {
			if (response.result != null) {
				tickets.addAll((List<TicketInfo>) response.getResult());
			}
			updateMemberCardBalance();
		} else {
			UIHelper.ToastMessage(this, response.msg);
			MemberCardDetailActivity.this.finish();
		}

	}
}
