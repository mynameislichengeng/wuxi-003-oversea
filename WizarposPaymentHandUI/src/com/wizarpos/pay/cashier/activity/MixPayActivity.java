package com.wizarpos.pay.cashier.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.model.TransactionInfo2;
import com.wizarpos.pay.cashier.presenter.MixTransactionFinishReceiver;
import com.wizarpos.pay.cashier.presenter.MixTransactionFinishReceiver.MixTransactionFinishListener;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.ticket.TicketManagerFactory;
import com.wizarpos.pay.cashier.presenter.ticket.inf.CommonTicketManager;
import com.wizarpos.pay.cashier.presenter.ticket.util.TicketManagerUtil;
import com.wizarpos.pay.cashier.presenter.transaction.TransactionFactory;
import com.wizarpos.pay.cashier.presenter.transaction.impl.MixPayTransaction;
import com.wizarpos.pay.cashier.view.MemberCardDetailActivity;
import com.wizarpos.pay.cashier.view.input.InputInfoActivity;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.RegexpUtils;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.GetCommonTicketInfoResp;
import com.wizarpos.pay.ui.AmountTextView;
import com.wizarpos.pay.ui.PopupWindowEx;
import com.wizarpos.pay.view.TransTypeItem;
import com.wizarpos.pay.view.adapter.MixPayAdapter;
import com.wizarpos.pay.view.adapter.MixPayAdapter.OnItemClickLitener;
import com.wizarpos.pay.view.exrecyclerview.decoration.DividerGridItemDecoration;
import com.wizarpos.pay.view.fragment.InputAmountFragment;
import com.wizarpos.pay.view.fragment.MixDiscountFragment;
import com.wizarpos.pay.view.fragment.MixDiscountFragment.DiscountListener;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.wizarpos.pay.view.util.DialogHelper2.DialogChoiseListener;
import com.wizarpos.pay2.lite.R;

/**
 * @Author:Huangweicai
 * @Date:2015-7-28 上午10:50:45
 * @Reason:组合支付
 */
public class MixPayActivity extends TransactionActivity implements OnClickListener, MixTransactionFinishListener, OnItemClickLitener, DiscountListener,
		InputAmountFragment.InputAmountListener{
	private final String LOG_TAG = "MixPayActivity";
	private AmountTextView tvInitAmount, tvDiscountAmount, tvReceivedAmount, tvNotPayAmount, tvReduceAmount;
	private PopupWindowEx mixPopEx;
//	private MixPayListFragment mixPayListFragment;
	private RecyclerView rvMixPay;
	private MixPayAdapter payAdapter;
	private MixDiscountFragment mixDiscountFragment;
	private InputAmountFragment inputAmountFragment;
	/**
	 * logic
	 */
	private MixPayTransaction mixPayTransaction;

	private CommonTicketManager commonTicketManager;

	private final int BACK_CLICKED = -100;
	private List<TransTypeItem> transTypeItems = new ArrayList<>();

	private static final int MIX_PAY_CASH = 820;
	private static final int MIX_PAY_BANK_CARD = 821;
	private static final int MIX_PAY_MEMBER_SCAN = 822;
	private static final int MIX_PAY_ALIPAY = 823;
	private static final int MIX_PAY_WEPAY = 824;
	// private static final int MIX_PAY_ALIPAY_TICKET = 825;
	private static final int MIX_PAY_WEPAY_TICKET = 826;
	// private static final int MIX_PAY_NOT_MEMBER_TICKET = 827;
	// private static final int MIX_PAY_MEMBER_TICKET_SCAN = 828;
	// private static final int MIX_PAY_TICKET = 829;
	// private static final int MIX_PAY_CHANGE_DEL = 830;
	private static final int MIX_PAY_BAIDU = 831;
	private static final int MIX_PAY_TENCENT = 832;
	private static final int MIX_PAY_THIRD_TICKET = 833;
	private static final int MIX_PAY_MEMBER = 834;
	private static final int MIX_PAY_MEMBER_TICKET_SCAN = 836;
	private static final int MIX_PAY_MEMBER_TICKET = 835;
	private static final int MIX_PAY_NO_MEMBER_TICKET_SCAN = 837;

	private MixTransactionFinishReceiver mixTransactionFinishReceiver;

	private int currentTransType = -1;

	private boolean isScan = false;
	private String ticketNum;
	/** 券改造 已经添加的券*/
	private ArrayList<TicketInfo> addTicketList = new ArrayList<>();
	/** intent传输TAG*/
	public static String TICKET_LIST_TAG = "addTicketList";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setMainView(R.layout.activity_mix_pay);
		setTitleText("组合支付");
		mixPayTransaction = TransactionFactory.newMixPayTransaction(this);
		mixPayTransaction.handleIntent(getIntent());
		commonTicketManager = TicketManagerFactory.createCommonTicketManager(this);
		registerMixReceiver();
		initTransTypes();
		initView();
		initGridView();
	}

	private void initTransTypes() {
		TransTypeItem cashTransaction = new TransTypeItem(TransactionTemsController.TRANSACTION_TYPE_CASH, "现金", R.drawable.icon_mix_cash);
		if(Constants.APP_VERSION_NAME != Constants.APP_VERSION_LAWSON)
		{
			transTypeItems.add(cashTransaction);
		}
		if(DeviceManager.getInstance().isSupportBankCard()){
			TransTypeItem bankTransaction = new TransTypeItem(TransactionTemsController.TRANSACTION_TYPE_BANK_CARD, "银行卡", R.drawable.icon_mix_card);
			if(Constants.APP_VERSION_NAME != Constants.APP_VERSION_LAWSON)
			{
				transTypeItems.add(bankTransaction);
			}
		}
		TransTypeItem memberTransaction = new TransTypeItem(TransactionTemsController.TRANSACTION_TYPE_MEMBER_CARD, "会员卡", R.drawable.icon_mix_bank);
		if(Constants.APP_VERSION_NAME != Constants.APP_VERSION_LAWSON)
		{
			transTypeItems.add(memberTransaction);
		}
		if (Constants.BAT_FLAG) {
			//bat
			if (Constants.FLAG_ON.equals(AppConfigHelper.getConfig(AppConfigDef.isAlipay))) {
				TransTypeItem alipayTransaction = new TransTypeItem(TransactionTemsController.TRANSACTION_TYPE_ALIPAY, "支付宝", R.drawable.icon_mix_alipay);
				transTypeItems.add(alipayTransaction);
			}
			if (Constants.FLAG_ON.equals(AppConfigHelper.getConfig(AppConfigDef.isWepay))) {
				TransTypeItem wepayTransaction = new TransTypeItem(TransactionTemsController.TRANSACTION_TYPE_WEPAY_PAY, "微信", R.drawable.icon_mix_wepay);
				transTypeItems.add(wepayTransaction);
			}
		}else {
			String appId = AppConfigHelper.getConfig(AppConfigDef.alipay_pattern), key = AppConfigHelper.getConfig(AppConfigDef.alipay_key);
			if ((Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.use_alipay))) && (!TextUtils.isEmpty(appId) && !TextUtils.isEmpty(key))) {
				TransTypeItem alipayTransaction = new TransTypeItem(TransactionTemsController.TRANSACTION_TYPE_ALIPAY, "支付宝", R.drawable.icon_mix_alipay);
				transTypeItems.add(alipayTransaction);
			}
			String wxAppId = AppConfigHelper.getConfig(AppConfigDef.weixin_app_id), appKey = AppConfigHelper.getConfig(AppConfigDef.weixin_app_key),
					mchid = AppConfigHelper.getConfig(AppConfigDef.weixin_mchid_id);
			if ((Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.use_weixin_pay)))
					&& (!TextUtils.isEmpty(wxAppId) && !TextUtils.isEmpty(appKey) && !TextUtils.isEmpty(mchid))) {
				TransTypeItem wepayTransaction = new TransTypeItem(TransactionTemsController.TRANSACTION_TYPE_WEPAY_PAY, "微信", R.drawable.icon_mix_wepay);
				transTypeItems.add(wepayTransaction);
			}
			
			
		}
//		String tenPayAppId = AppConfigHelper.getConfig(AppConfigDef.tenpay_bargainor_id), tenAppKey = AppConfigHelper.getConfig(AppConfigDef.tenpay_key), optId = AppConfigHelper
//				.getConfig(AppConfigDef.tenpay_op_user_id), optPasswd = AppConfigHelper.getConfig(AppConfigDef.tenpay_op_user_passwd);
//		if ((Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.use_tenpay)))
//				&& (!TextUtils.isEmpty(tenPayAppId) && !TextUtils.isEmpty(tenAppKey) && !TextUtils.isEmpty(optId) && !TextUtils.isEmpty(optPasswd))) {
//			TransTypeItem tenpayTransaction = new TransTypeItem(TransactionTemsController.TRANSACTION_TYPE_TEN_PAY, "QQ钱包", R.drawable.qq);
//			transTypeItems.add(tenpayTransaction);
//		}
		// String baiduAppId = AppConfigHelper.getConfig(AppConfigDef.baidupay_id), baiduAppKey = AppConfigHelper.getConfig(AppConfigDef.baidupay_key);
		// if ((Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.use_baidupay)))
		// && (!TextUtils.isEmpty(baiduAppId) && !TextUtils.isEmpty(baiduAppKey))) {
		// TransTypeItem baiduTransaction = new TransTypeItem(TransactionTemsController.TRANSACTION_TYPE_BAIDU_PAY, "百度支付", R.drawable.ic_baidu);
		// transTypeItems.add(baiduTransaction);
		// }
		// TransTypeItem otherpayTransaction = new TransTypeItem(TransactionTemsController.TRANSACTION_TYPE_OTHER, "其它支付", R.drawable.payother);
		// transTypeItems.add(otherpayTransaction);
		TransTypeItem wepayTicketTransaction = new TransTypeItem(TransactionTemsController.TRANSACTION_TYPE_WEPAY_TICKET_CANCEL, "微信卡券", R.drawable.bq_weixincard);
		transTypeItems.add(wepayTicketTransaction);
		// TransTypeItem thirdTicketTransaction = new TransTypeItem(TransactionTemsController.TRANSACTION_TYPE_THIRD_TICKET_CANCEL, "第三方卡券",
		// R.drawable.payother);
		// transTypeItems.add(thirdTicketTransaction);
		TransTypeItem memberTicketTransaction = new TransTypeItem(TransactionTemsController.TRANSACTION_TYPE_MEMBER_TICKET_CANCEL, "会员券", R.drawable.icon_mix_member);
		if(Constants.APP_VERSION_NAME != Constants.APP_VERSION_LAWSON)
		{
			transTypeItems.add(memberTicketTransaction);
		}
		TransTypeItem NOMemberTicketTransaction = new TransTypeItem(TransactionTemsController.TRANSACTION_TYPE_NO_MOMBER_TICKET_CANCEL, "非会员券", R.drawable.icon_mix_member_ticket);
		if(Constants.APP_VERSION_NAME != Constants.APP_VERSION_LAWSON)
		{
			transTypeItems.add(NOMemberTicketTransaction);
		}
		TransTypeItem changeDelTransaction = new TransTypeItem(TransactionTemsController.TRANSACTION_TYPE_CHANGE_DEL, "零头处理", R.drawable.icon_pay_change_del);
		transTypeItems.add(changeDelTransaction);
		TransTypeItem backClicked = new TransTypeItem(BACK_CLICKED, "返回", R.drawable.icon_mix_back);
		transTypeItems.add(backClicked);
	}

	private void initView() {
		tvInitAmount = (AmountTextView) this.findViewById(R.id.tvInitAmount);
		tvDiscountAmount = (AmountTextView) this.findViewById(R.id.tvDiscountAmount);
		tvReduceAmount = (AmountTextView) this.findViewById(R.id.tvReduceAmount);
		tvReceivedAmount = (AmountTextView) this.findViewById(R.id.tvReceivedAmount);
		tvNotPayAmount = (AmountTextView) this.findViewById(R.id.tvNotPayAmount);
		rvMixPay = (RecyclerView) this.findViewById(R.id.rvMixPay);
		rvMixPay.setLayoutManager(new GridLayoutManager(this, 4));
		findViewById(R.id.rlMixPayReceivedAmount).setOnClickListener(this);
		findViewById(R.id.rlMixDiscount).setOnClickListener(this);
		updateView();

	}

	private void updateView() {
		tvInitAmount.setTextByFen(mixPayTransaction.getMixTransactionInfo().getInitAmount() + "");
		tvDiscountAmount.setTextByFen(mixPayTransaction.getMixTransactionInfo().getDiscountAmount() + "");
		tvReduceAmount.setTextByFen(mixPayTransaction.getMixTransactionInfo().getReduceAmount() + "");
		tvReceivedAmount.setTextByFen(mixPayTransaction.getMixTransactionInfo().getRealAmount() + "");
		tvNotPayAmount.setTextByFen(Tools.formatYuan(getNotPayAmount()));
	}

	private void initGridView() {
		payAdapter = new MixPayAdapter(this);
		rvMixPay.addItemDecoration(new DividerGridItemDecoration(this));
		rvMixPay.setAdapter(payAdapter);
		payAdapter.setDataChanged(transTypeItems);
		payAdapter.setItemClickListener(this);
		// gvMixPay.setOnItemClickListener(this);
	}

	private void registerMixReceiver() {
		mixTransactionFinishReceiver = new MixTransactionFinishReceiver();
		mixTransactionFinishReceiver.setListener(this);
		IntentFilter filter = new IntentFilter(MixTransactionFinishReceiver.ACTION);
		this.registerReceiver(mixTransactionFinishReceiver, filter);
	}

	private void unRegisterMixReceiver() {
		try {
			this.unregisterReceiver(mixTransactionFinishReceiver);
		} catch (Exception e) {}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlMixPayReceivedAmount:// 已收
			showReceivedPop();
			break;
		case R.id.rlMixDiscount:// 折扣
			if (mixPayTransaction.isReduced()) {
				UIHelper.ToastMessage(this, "不能重复折扣");
				return;
			}
			mixDiscount();
			break;

		default:
			break;
		}
	}

	private void mixDiscount() {
		if(inputAmountFragment!=null && inputAmountFragment.isVisible()){return;}
		if(mixPopEx != null && mixPopEx.isShowing()){return;}
		mixDiscountFragment = new MixDiscountFragment();
		mixDiscountFragment.setDiscountListener(this);
		Bundle args = new Bundle();
		args.putInt("shouldPayAmount", mixPayTransaction.getMixTransactionInfo().getShouldAmount());
		mixDiscountFragment.setArguments(args);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.popup_enter, R.anim.popup_exit);
		if (mixDiscountFragment.isAdded()) {
			transaction.show(mixDiscountFragment).commit();
		} else {
			transaction.add(R.id.flMixDiscount, mixDiscountFragment).commit();
		}
	}

	private void inputAmout() {
		if(mixDiscountFragment!=null && mixDiscountFragment.isVisible()){return;}
		if(mixPopEx != null && mixPopEx.isShowing()){return;}
		inputAmountFragment = new InputAmountFragment();
		inputAmountFragment.setInputAmountListener(this);
		Bundle args = new Bundle();
		args.putString("maxAmount", mixPayTransaction.getMixTransactionInfo().getShouldAmount() + "");
		args.putBoolean("isInputAmount", true);
		inputAmountFragment.setArguments(args);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.popup_enter, R.anim.popup_exit);
		if (inputAmountFragment.isAdded()) {
			transaction.show(inputAmountFragment).commit();
		} else {
			transaction.add(R.id.flMixDiscount, inputAmountFragment).commit();
		}
	}

	/**
	 * @Author:Huangweicai
	 * @Date:2015-7-28 下午4:11:45
	 * @Reason:弹出已收
	 */
	private void showReceivedPop() {
		if(mixDiscountFragment!=null && mixDiscountFragment.isVisible()){return;}
		if(inputAmountFragment!=null && inputAmountFragment.isVisible()){return;}
		TransactionInfo2 mixTransactionInfo = mixPayTransaction.getMixTransactionInfo();
		if (mixTransactionInfo.getPayedTransactionInfo() == null || mixTransactionInfo.getPayedTransactionInfo().isEmpty()) { return; }
		if (mixPopEx == null) {
			mixPopEx = new PopupWindowEx(this);
		}
		mixPopEx.setDataChanged(mixTransactionInfo, findViewById(R.id.main));
//		if(mixPayListFragment == null){
//			mixPayListFragment = new MixPayListFragment();
//		}
//		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//		transaction.setCustomAnimations(R.anim.popup_enter, R.anim.popup_exit);
//		if (mixPayListFragment.isAdded()) {
//			transaction.showFromDialog(mixPayListFragment).commit();
//		} else {
//			transaction.add(R.id.flMixDiscount, mixPayListFragment).commit();
//		}
	}

	private Intent intent = null;

	private TransTypeItem selectedTransType;

	@Override
	public void onItemClick(View view, int position, TransTypeItem transTypeItem) {
		selectedTransType = transTypeItem;
		if (transTypeItem.getRealValue() == BACK_CLICKED) {
			onBack();
		} else if (transTypeItem.getRealValue() == TransactionTemsController.TRANSACTION_TYPE_MEMBER_TICKET_CANCEL) {// 会员券核销
			deliverTrans(mixPayTransaction.getTransactionInfo().getShouldAmount() + "", selectedTransType);
		} else if (transTypeItem.getRealValue() == TransactionTemsController.TRANSACTION_TYPE_NO_MOMBER_TICKET_CANCEL) {// 非会员券核销
			deliverTrans(mixPayTransaction.getTransactionInfo().getShouldAmount() + "", selectedTransType);
		} else if (transTypeItem.getRealValue() == TransactionTemsController.TRANSACTION_TYPE_NO_MOMBER_TICKET_DISCOUNT) {
			deliverTrans(null, selectedTransType);
		} else if (transTypeItem.getRealValue() == TransactionTemsController.TRANSACTION_TYPE_THIRD_TICKET_CANCEL) {// 第三方券核销
			deliverTrans(mixPayTransaction.getTransactionInfo().getShouldAmount() + "", selectedTransType);
		} else if (transTypeItem.getRealValue() == TransactionTemsController.TRANSACTION_TYPE_WEPAY_TICKET_CANCEL) {// 微信券核销
			deliverTrans(mixPayTransaction.getTransactionInfo().getShouldAmount() + "", selectedTransType);
		} else if (transTypeItem.getRealValue() == TransactionTemsController.TRANSACTION_TYPE_CHANGE_DEL) {// 抹0
			deliverTrans(null, selectedTransType);
		} else if (transTypeItem.getRealValue() == TransactionTemsController.TRANSACTION_TYPE_CASH) {// 现金消费免去输入金额
			deliverTrans(mixPayTransaction.getTransactionInfo().getShouldAmount() + "", selectedTransType);
		} else if (transTypeItem.getRealValue() == TransactionTemsController.TRANSACTION_TYPE_BANK_CARD) {// 银行卡消费免去输入金额
			deliverTrans(mixPayTransaction.getTransactionInfo().getShouldAmount() + "", selectedTransType);
		} else {
			inputAmout();
		}
	}

	private void deliverTrans(String initAmount, TransTypeItem transTypeItem) {
		currentTransType = transTypeItem.getRealValue();
		intent = new Intent();
		intent.putExtra(Constants.mixFlag, "1");
		intent.putExtra(Constants.TRANSACTION_TYPE, TransactionTemsController.TRANSACTION_TYPE_MIXPAY);// 交易类型
		intent.putExtra(Constants.initAmount, initAmount);// 用户输入的子消费应收金额
		intent.putExtra(Constants.mixTranLogId, mixPayTransaction.getMixTransactionInfo().getMixTranLogId());// 组合支付主流水号
		intent.putExtra(Constants.mixInitAmount, mixPayTransaction.getMixTransactionInfo().getInitAmount() + "");// 组合支付总金额
		intent.putExtra(TICKET_LIST_TAG, addTicketList);//券改造
		if(transTypeItems.isEmpty()){ //标示是第一笔交易
			intent.putExtra(Constants.isFirstTrans, true);
		}
		LogEx.d("mixpay", "deliverTrans initAmount --> " + initAmount + "  mixInitAmount --> " + mixPayTransaction.getMixTransactionInfo().getInitAmount());
		switch (transTypeItem.getRealValue()) {
		case TransactionTemsController.TRANSACTION_TYPE_CASH:// 现金
			toCashMixTransactionView(this, intent, MIX_PAY_CASH);
			break;
		case TransactionTemsController.TRANSACTION_TYPE_BANK_CARD:// 银行卡
			toCardMixTransactionView(this, intent, MIX_PAY_BANK_CARD);
			break;
		case TransactionTemsController.TRANSACTION_TYPE_MEMBER_CARD:// 会员卡
			toMemberMixTransactionVew(this, intent, MIX_PAY_MEMBER_SCAN);
			break;
		case TransactionTemsController.TRANSACTION_TYPE_ALIPAY:// 支付宝
//			intent.putExtra(Constants.realAmount, initAmount); //传入realAmount wu
			toAlipayMixMicroTransaction(this, intent, MIX_PAY_ALIPAY);
			break;
		case TransactionTemsController.TRANSACTION_TYPE_WEPAY_PAY:// 微信
//			intent.putExtra(Constants.realAmount, initAmount); //传入realAmount wu
			toWepayMixMicroTransaction(this, intent, MIX_PAY_WEPAY);
			break;
		case TransactionTemsController.TRANSACTION_TYPE_TEN_PAY:// QQ
//			intent.putExtra(Constants.realAmount, initAmount); //传入realAmount wu
			toTenpayMixMicroTransaction(this, intent, MIX_PAY_TENCENT);
			break;
		case TransactionTemsController.TRANSACTION_TYPE_BAIDU_PAY:// 百度
//			intent.putExtra(Constants.realAmount, initAmount); //传入realAmount wu
			toBaiduMixMicroTransaction(this, intent, MIX_PAY_BAIDU);
			break;
		case TransactionTemsController.TRANSACTION_TYPE_THIRD_TICKET_CANCEL:// 第三方券核销
			toThridTicketCancelMix(this, intent, MIX_PAY_THIRD_TICKET);
			break;
		case TransactionTemsController.TRANSACTION_TYPE_WEPAY_TICKET_CANCEL:// 微信卡券核销
			toWepayTicketCancelMix(this, intent, MIX_PAY_WEPAY_TICKET);
			break;
		case TransactionTemsController.TRANSACTION_TYPE_MEMBER_TICKET_CANCEL:// 会员券核销
			intent.putExtra("title", "组合支付|会员券");
			toMixMemberTicketPass(this, intent, MIX_PAY_MEMBER_TICKET_SCAN);
			break;
		case TransactionTemsController.TRANSACTION_TYPE_NO_MOMBER_TICKET_CANCEL:// 普通券核销
			toMixNormalTicketPass(this, intent, MIX_PAY_NO_MEMBER_TICKET_SCAN);
			break;
		case TransactionTemsController.TRANSACTION_TYPE_CHANGE_DEL:// 抹0
			handleChangeDel();
			break;

		default:
			break;
		}
	}

	/**
	 * 零头处理
	 */
	private void handleChangeDel() {
		DialogHelper2.showChoiseDialog(this, "去掉角，分?", new DialogChoiseListener() {

			@Override
			public void onNo() {

			}

			@Override
			public void onOK() {
				progresser.showProgress();
				mixPayTransaction.handleChange(new ResultListener() {

					@Override
					public void onSuccess(Response response) {
						progresser.showContent();
						updateView();
						isTransactonFinsih();
					}

					@Override
					public void onFaild(Response response) {
						progresser.showContent();
						UIHelper.ToastMessage(MixPayActivity.this, response.getMsg());
					}
				});
			}
		});
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent data) {
		super.onActivityResult(arg0, arg1, data);
		if (arg1 == RESULT_OK) {
			switch (arg0) {
			case MIX_PAY_CASH:
			case MIX_PAY_BANK_CARD:
			case MIX_PAY_ALIPAY:
			case MIX_PAY_WEPAY:
			case MIX_PAY_BAIDU:
			case MIX_PAY_TENCENT:
			case MIX_PAY_MEMBER:
				onMixTransacitonFinish(data);
				break;
			case MIX_PAY_THIRD_TICKET:
				addTicketList = (ArrayList<TicketInfo>) intent.getSerializableExtra("addTicketList");
				onMixTransacitonFinish(data);
				break;
			case MIX_PAY_MEMBER_TICKET:
				addTicketList = (ArrayList<TicketInfo>) intent.getSerializableExtra("addTicketList");
				onMixTransacitonFinish(data);
				break;
			case MIX_PAY_MEMBER_SCAN: //会员卡
				try {
					Intent intent = data;
					ticketNum = data.getStringExtra(InputInfoActivity.content);
					if(TextUtils.isEmpty(ticketNum)){
						return;
					}
					boolean isScanMember = (data.getIntExtra(InputInfoActivity.type, 0) == InputInfoActivity.INPUT_TYPE_CAMERA);
					if (isScanMember) { // 是否是微信会员卡
						if(RegexpUtils.isAllNumberCard(ticketNum)) {
							data.putExtra("cardNo", ticketNum);
						}else {
							data.putExtra("tokenId", ticketNum);
						}
					} else {
						data.putExtra("cardNo", ticketNum);
					}
					intent.setClass(this, MemberCardDetailActivity.class);
					startActivityForResult(intent, MIX_PAY_MEMBER);					
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case MIX_PAY_MEMBER_TICKET_SCAN: //会员券
				try { 
					ticketNum = data.getStringExtra(InputInfoActivity.content);
					addTicketList = (ArrayList<TicketInfo>) data.getSerializableExtra(TICKET_LIST_TAG);
					if(TextUtils.isEmpty(ticketNum)){
						return;
					}
					boolean isScanMemberTicket = (data.getIntExtra(InputInfoActivity.type, 0) == InputInfoActivity.INPUT_TYPE_CAMERA);
//					ticketNum = arg2.getStringExtra("ticketNo");
//					scanTicketNo = arg2.getStringExtra("scanTicketNo");
//					getTicketInfo(ticketNum);
//					data.putExtra(Constants.mixFlag, "1");
					if (isScanMemberTicket) { // 是否是微信会员卡
						data.putExtra("tokenId", ticketNum);
					} else {
						data.putExtra("cardNo", ticketNum);
					}
					data.setClass(this, MixMembertTicketActivity.class);
					data.putExtra(TICKET_LIST_TAG, addTicketList);
					startActivityForResult(data, MIX_PAY_MEMBER_TICKET);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				break;
			case MIX_PAY_NO_MEMBER_TICKET_SCAN:
				getCommonTicketInfo(data);
				break;
			case MIX_PAY_WEPAY_TICKET:
				uploadWepayTicketCancel(data);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 上送微信卡券核销结果
	 */
	protected void uploadWepayTicketCancel(final Intent arg2) {
		progresser.showProgress();
		int realAmount = (int) arg2.getLongExtra(WepayTicketCancleMixActivity.REDUCE_COST_TAG, 0);
		String cardNo = arg2.getStringExtra(WepayTicketCancleMixActivity.WEIXIN_CODE_TAG);
		mixPayTransaction.uploadWepayTicketMixTransaction(realAmount, cardNo, new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				TicketInfo ticketInfo = (TicketInfo) arg2.getSerializableExtra("ticketInfo");
				if(ticketInfo!=null) {
					addTicketList.add(ticketInfo);
				}
				UIHelper.ToastMessage(MixPayActivity.this, response.msg);
				mixPayTransaction.addTransactionInfo((TransactionInfo2) response.getResult());
				updateView();
				isTransactonFinsih();
			}

			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(MixPayActivity.this, response.msg);
			}
		});
	}

	protected void getCommonTicketInfo(Intent arg2) {
			// 券号
			try { 
				ticketNum = arg2.getStringExtra(InputInfoActivity.content); //调用摄像头逻辑跳转 wu@[20150902] 
				if(TextUtils.isEmpty(ticketNum)){
					return;
				}
				isScan = (arg2.getIntExtra(InputInfoActivity.type, 0) == InputInfoActivity.INPUT_TYPE_CAMERA);
				progresser.showProgress();
				String shouldPayAmount =  mixPayTransaction.getMixTransactionInfo().getShouldAmount() + "";
				commonTicketManager.getTicketInfo(ticketNum, shouldPayAmount ,mixPayTransaction.getTransactionInfo().getInitAmount() + "" ,new ResultListener() {

					@Override
					public void onSuccess(Response response) {
						progresser.showContent();
						GetCommonTicketInfoResp resp = (GetCommonTicketInfoResp) response.result;
						showTicketInfo((TicketInfo) resp.getTicketInfo());
					}

					@Override
					public void onFaild(Response response) {
						progresser.showContent();
						UIHelper.ToastMessage(MixPayActivity.this, response.msg);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * 解析某种支付方式返回的参数
	 */
	private TransactionInfo2 praseActivityResponse(Intent respIntent) {
		TransactionInfo2 cashInfo = new TransactionInfo2();
		cashInfo.setInitAmount(getIntValue(respIntent.getStringExtra(Constants.initAmount)));
		cashInfo.setShouldAmount(getIntValue(respIntent.getStringExtra(Constants.shouldAmount)));
		cashInfo.setRealAmount(getIntValue(respIntent.getStringExtra(Constants.realAmount)));
		cashInfo.setReduceAmount(getIntValue(respIntent.getStringExtra(Constants.reduceAmount)));
		cashInfo.setChangeAmount(getIntValue(respIntent.getStringExtra(Constants.changeAmount)));
		cashInfo.setDiscountAmount(getIntValue(respIntent.getStringExtra(Constants.discountAmount)));
		cashInfo.setTransactionType(currentTransType);
		cashInfo.setMixFlag("1");
		cashInfo.setCardNo(respIntent.getStringExtra(Constants.cardNo));
		cashInfo.setMixTranLogId(respIntent.getStringExtra(Constants.mixTranLogId));
		return cashInfo;
	}

	private int getIntValue(String sVal) {
		try {
			return Integer.parseInt(sVal);
		} catch (Exception e) {
			return 0;
		}
	}

	private int getNotPayAmount() {
		return mixPayTransaction.getMixTransactionInfo().getShouldAmount();
	}

	@Override
	public void onMixTransacitonFinish(Intent intent) {
		TransactionInfo2 trInfo = praseActivityResponse(intent);
		mixPayTransaction.addTransactionInfo(trInfo);
		updateView();
		// showReceivedPop(mixPayTransaction.getMixTransactionInfo());
		isTransactonFinsih();
	}

	private void isTransactonFinsih() {
		// 判断交易是否结束
		if (mixPayTransaction.isTransactionFinsh()) {
			Intent resultIntent = mixPayTransaction.bundleResult();
			toTransactionSuccess(MixPayActivity.this, resultIntent);
			this.finish();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		LogEx.d("TAG", "onTouch");
		if (mixDiscountFragment == null) return super.dispatchTouchEvent(ev);
		int height = findViewById(R.id.flMixDiscount).getTop();
		int y = (int) ev.getY();
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			if (y < height) {
				if (mixDiscountFragment.isAdded() && !mixDiscountFragment.isHidden()) {
					getSupportFragmentManager().beginTransaction().remove(mixDiscountFragment).commit();
					return true;
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onDestroy() {
		unRegisterMixReceiver();
		super.onDestroy();
	}

	@Override
	public void onReduceAmount(int reduce) {
		LogEx.d("mixpay", "开始扣减 扣减金额 --> " + reduce);
		Response response = mixPayTransaction.handleReduceByAmount(reduce);
		if (response.code == 0) {
			updateView();
			if (mixDiscountFragment != null && mixDiscountFragment.isAdded()) {
				getSupportFragmentManager().beginTransaction().remove(mixDiscountFragment).commit();
			}
			uploadReduce(null, reduce + "");
		} else {
			UIHelper.ToastMessage(MixPayActivity.this, response.msg);
		}
	}

	@Override
	public void onReducePrecent(int precent) {
		Response response = mixPayTransaction.handleReduceByPrecent(precent);
		if (response.code == 0) {
			updateView();
			if (mixDiscountFragment != null && mixDiscountFragment.isAdded()) {
				getSupportFragmentManager().beginTransaction().remove(mixDiscountFragment).commit();
			}
			uploadReduce(precent + "", (int)response.getResult() +"");
		} else {
			UIHelper.ToastMessage(MixPayActivity.this, response.msg);
		}
	}

	private void uploadReduce(final String precent, final String amount) {
		progresser.showProgress();
		mixPayTransaction.uploadDiscount(precent , amount, new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(MixPayActivity.this, "折扣设置成功");
				isTransactonFinsih();
			}

			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(MixPayActivity.this, response.getMsg());
			}
		});
	}

	@Override
	public void onBackPressed() {
		//组合支付不允许点击返回键退出
		
		if (mixDiscountFragment != null && mixDiscountFragment.isVisible()) {
			getSupportFragmentManager().beginTransaction().remove(mixDiscountFragment).commit();
		} else if (inputAmountFragment != null && inputAmountFragment.isVisible()) {
			getSupportFragmentManager().beginTransaction().remove(inputAmountFragment).commit();
		} else if (mixPopEx != null && mixPopEx.isShowing()) {
			mixPopEx.hide();
		} 
//		else if (mixPayListFragment != null && mixPayListFragment.isVisible()) {
//			getSupportFragmentManager().beginTransaction().remove(mixPayListFragment).commit();
//		}
	}

	@Override
	public void onAmount(String amount) {
		getSupportFragmentManager().beginTransaction().remove(inputAmountFragment).commit();
		if (TextUtils.isEmpty(amount) || "0".equals(amount)) { return; }
		deliverTrans(amount, selectedTransType);
	}

	/**
	 * 显示券的信息
	 * 
	 * @param ticket
	 */
	private void showTicketInfo(final TicketInfo ticket) {
		/** 显示微信券信息 */
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(ticket.getTicketDef().getTicketName() + "\n" + Calculater.formotFen(ticket.getTicketDef().getBalance()+"") + "元");
		builder.setTitle(getResources().getString(R.string.ticket_info));
		builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
//				if (scanTicketNo != null) {
//					ticket.setIsFromScan("true");
//				} else {
//					ticket.setIsFromScan("false");
//				}
				Response mResponse = TicketManagerUtil.verifyAddTicket(ticket, addTicketList);
				if(mResponse.code != 0) {
					Toast.makeText(MixPayActivity.this, mResponse.msg, Toast.LENGTH_SHORT).show();
					return;
				}
				ticket.setIsFromScan(isScan ? Constants.TRUE : Constants.FALSE); //修复摄像头调用逻辑 wu@[20150902]
				progresser.showProgress();
				mixPayTransaction.passMixCommonTicket(ticket, new ResultListener() {

					@Override
					public void onSuccess(Response response) {
						addTicketList.add(ticket);//券改造 @hwc 2015年12月30日13:35:57
						progresser.showContent();
						mixPayTransaction.addTransactionInfo((TransactionInfo2) response.result);
						updateView();
						// showReceivedPop(mixPayTransaction.getMixTransactionInfo());
						isTransactonFinsih();
					}

					@Override
					public void onFaild(Response response) {
						progresser.showContent();
						UIHelper.ToastMessage(MixPayActivity.this, response.msg);
					}
				});
			}
		});
		builder.setNegativeButton(getResources().getString(R.string.ticket_cancle), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				finish();
			}
		});
		builder.create().show();

	}

	/**
	 * 卡券核销
	 */
	protected void onBack() {
		// thirdTicketCancel();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setTitle("卡支付未完成,选择退出方式");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "折扣退出");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "直接退出");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "取消");
		list.add(map);
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.layout_single_choose_simple, new String[] { "title" }, new int[] { R.id.tvTitle });
		builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {//折扣退出
					onReduceAmount(getNotPayAmount());
				} else if (which == 1) {//直接退出
					back();
				}
				dialog.dismiss();
			}
		});
		builder.setCancelable(true);
		AlertDialog dialog = builder.create();
		try {
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
