package com.wizarpos.pay.cashier.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.CommonUtil;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.activity.BankCardPayActivity;
import com.wizarpos.pay.cashier.activity.CommonThirdPayActivity;
import com.wizarpos.pay.cashier.activity.DiscountActivity;
import com.wizarpos.pay.cashier.activity.TransactionActivity;
import com.wizarpos.pay.cashier.adapter.TransTypeItemsAdapter;
import com.wizarpos.pay.cashier.model.Card;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.transaction.impl.TransactionMarketingActivitiesPresenter;
import com.wizarpos.pay.cashier.thrid_app_controller.ThirdAppTransactionController;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppTransactionRequest;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.model.MarketPayReq;
import com.wizarpos.pay.model.MarketPayRes;
import com.wizarpos.pay.thirdapp.ThirdAppBroadcastReceiver;
import com.wizarpos.pay.thirdapp.ThirdAppBroadcastReceiver.ThirdAppListener;
import com.wizarpos.pay.view.TransTypeItem;
import com.wizarpos.pay.view.fragment.InputPadFragment;
import com.wizarpos.pay.view.fragment.OnMumberClickListener;
import com.wizarpos.pay.view.util.Constents;
import com.motionpay.pay2.lite.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GatheringActivity extends TransactionActivity implements
        OnMumberClickListener, TransactionTemsController, ThirdAppListener,
        InputPadFragment.OnKeyChangedListener {
    private static final String LOG_TAG = GatheringActivity.class.getName();
    private int transCode = 0;
    private TextView tvDiscout = null;
    private EditText etInitAmount = null;
    protected String initAmount = null;
    protected String discountAmount = null;
    private String swipeBankCardNo = null;// 如果在此界面扫描到了卡(银行卡) @yaosong
    private JSONArray swipeBankJSONArray = null;// 收单应用需要完整的二三磁道信息,不能只传入卡号
    // wu@[20151211]
    private String swipeMemberCardNo = null;// 如果在此界面扫描到了卡(会员卡) @yaosong
    private String thirdMemberCardNo = null;// 第三方应用传入的会员卡卡号@hong[20151221]
    private boolean hasICCard = false;// 如果在此界面扫描到了IC卡 @yaosong
    private InputPadFragment inputPadFragment;
    private GridView payModeGridView = null;
    private TransTypeItemsAdapter typeAdapter = null;
    /**
     * 会员包名
     */
    private final String MEMBER_PACKAGE_NAME = "com.wizarpos.wizarposmemberui";
    private NetBroadcastReceiver mNetBroadcastReceiver;

    private int INTENT_CHOOSE_PAYMODE = 10001;
    private ImageView ivPayMode;
    private TextView tvPayMode;
    private long holdTime=0;

    private static final int GO_INTPUT_MEMEBR_NO = 10003;

    private static final int discountRequestCode = 10015;

    private List<TransTypeItem> transTypes = new ArrayList<TransTypeItem>();
    ;
    private TransTypeItem bankTransaction = new TransTypeItem(
            TransactionTemsController.TRANSACTION_TYPE_BANK_CARD, "银行卡",
            R.drawable.pay_bankcard_selector);
    private TransTypeItem memberTransaction = new TransTypeItem(
            TransactionTemsController.TRANSACTION_TYPE_MEMBER_CARD, "会员卡",
            R.drawable.pay_membercard_selector);
    private TransTypeItem alipayTransaction = new TransTypeItem(
            TransactionTemsController.TRANSACTION_TYPE_ALIPAY, "支付宝",
            R.drawable.pay_ali_selector);
    private TransTypeItem wepayTransaction = new TransTypeItem(
            TransactionTemsController.TRANSACTION_TYPE_WEPAY_PAY, "微信",
            R.drawable.pay_weixin_selector);
    private TransTypeItem tenpayTransaction = new TransTypeItem(
            TransactionTemsController.TRANSACTION_TYPE_TEN_PAY, "QQ钱包",
            R.drawable.pay_qq_selector);
    private TransTypeItem unionpayTransaction = new TransTypeItem(
            TransactionTemsController.TRANSACTION_TYPE_UNION_PAY, "移动支付",
            R.drawable.union_pay);
    private TransTypeItem cashTransaction = new TransTypeItem(
            TransactionTemsController.TRANSACTION_TYPE_CASH, "现金",
            R.drawable.pay_cash_selector);
    private TransTypeItem baiduTransaction = new TransTypeItem(
            TransactionTemsController.TRANSACTION_TYPE_BAIDU_PAY, "百度钱包",
            R.drawable.pay_baidu_selector);
    private TransTypeItem otherpayTransaction = new TransTypeItem(
            TransactionTemsController.TRANSACTION_TYPE_OTHER, "其它支付",
            R.drawable.pay_other_selector);
    private TransTypeItem mixpayTransaction = new TransTypeItem(
            TransactionTemsController.TRANSACTION_TYPE_MIXPAY, "组合支付",
            R.drawable.pay_combine_selector);

    final ResultListener swipeListener = new ResultListener() {
        @Override
        public void onSuccess(Response response) {
            DeviceManager.getInstance().closeCardListener();

            JSONArray cardArr = (JSONArray) response.getResult();
            String secondChannel = cardArr.getString(1);
            if (TextUtils.isDigitsOnly(secondChannel)) {// 纯数字,判定为会员卡号
                if (transTypes.contains(memberTransaction)) {
                    swipeMemberCardNo = secondChannel;
                    LogEx.d("会员卡号:", swipeMemberCardNo);
                    if (!TextUtils.isEmpty(thirdMemberCardNo)) {// 第三方调用以第三方传入的卡号为主@hong[20151221]
                        swipeMemberCardNo = thirdMemberCardNo;
                        UIHelper.ToastMessage(GatheringActivity.this,
                                "第三方已传入会员卡卡号");
                        setPayMode(TransactionTemsController.TRANSACTION_TYPE_MEMBER_CARD);
                        initScanner();// 重启监听
                    } else {
                        initAmount = etInitAmount.getText().toString();
                        if (TextUtils.isEmpty(initAmount)
                                || "0".equals(initAmount)
                                || "0.00".equals(initAmount)) {
                            UIHelper.ToastMessage(GatheringActivity.this,
                                    "请输入收款金额");
                            setPayMode(TransactionTemsController.TRANSACTION_TYPE_MEMBER_CARD);
                            initScanner();// 重启监听
                        } else {
                            initAmount = Calculater.formotYuan(initAmount);
                            doPay(TransactionTemsController.TRANSACTION_TYPE_MEMBER_CARD);
                        }
                    }
                } else {
                    UIHelper.ToastMessage(GatheringActivity.this,
                            "您的设备未开启使用会员卡");
                }
            } else if (secondChannel.contains("=")) {// 银行卡的磁道二磁道信息有"="
                if (transTypes.contains(bankTransaction)) {
                    try {
                        String service_code = secondChannel.substring(
                                secondChannel.indexOf("=") + 5,
                                secondChannel.indexOf("=") + 8);// 获取卡片服务代码
                        if (service_code.startsWith("2")
                                || service_code.startsWith("6")) { // 卡片有集成电路芯片
                            UIHelper.ToastMessage(GatheringActivity.this,
                                    "该银行卡带有芯片，请插入IC卡检测口");
                            setPayMode(TransactionTemsController.TRANSACTION_TYPE_BANK_CARD);
                            initScanner();// 重启监听
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    swipeBankCardNo = secondChannel.substring(0,
                            secondChannel.indexOf("="));
                    if (!TextUtils.isEmpty(swipeBankCardNo)) {
                        LogEx.d("银行卡号:", swipeBankCardNo);
                        swipeBankJSONArray = cardArr;
                        initAmount = etInitAmount.getText().toString();
                        if (TextUtils.isEmpty(initAmount)
                                || "0".equals(initAmount)
                                || "0.00".equals(initAmount)) {
                            UIHelper.ToastMessage(GatheringActivity.this,
                                    "请输入收款金额");
                            setPayMode(TransactionTemsController.TRANSACTION_TYPE_BANK_CARD);
                            initScanner();// 重启监听
                        } else {
                            initAmount = Calculater.formotYuan(initAmount);
                            doPay(TransactionTemsController.TRANSACTION_TYPE_BANK_CARD);
                        }
                    } else {
                        UIHelper.ToastMessage(GatheringActivity.this,
                                "读卡失败，请重新刷卡");
                        LogEx.d("获取磁条卡解析失败");
                        initScanner();// 重启监听
                    }
                } else {
                    UIHelper.ToastMessage(GatheringActivity.this,
                            "您的设备未开启银行卡交易");
                }
            }

        }

        @Override
        public void onFaild(Response response) {
            DeviceManager.getInstance().closeCardListener();
            LogEx.d(response.getMsg());
            UIHelper.ToastMessage(GatheringActivity.this, "读卡失败，请重新刷卡");
            initScanner();// 重启监听
        }
    };
    /**
     * 请输入收款金额
     **/
    private final int HANDLER_CODE_1 = 1;
    /**
     * IC卡读取失败，请重新插入
     **/
    private final int HANDLER_CODE_2 = 2;
    private final int HANDLER_CODE_3 = 3;
    /**
     * IC卡读取成功，跳转消费
     **/
    private final int HANDLER_CODE_4 = 4;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_CODE_1:
                    UIHelper.ToastMessage(GatheringActivity.this, "请输入收款金额");
                    setPayMode(TransactionTemsController.TRANSACTION_TYPE_BANK_CARD);
                    break;
                case HANDLER_CODE_2:
                    UIHelper.ToastMessage(GatheringActivity.this, "IC卡读取失败，请重新插入");
                    break;
                case HANDLER_CODE_3:
                    UIHelper.ToastMessage(GatheringActivity.this, "您的设备未开启银行卡支付");
                    break;
                case HANDLER_CODE_4:
                    doPay(TransactionTemsController.TRANSACTION_TYPE_BANK_CARD);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(this, ThirdAppBroadcastReceiver.FILTER_TRANSACITON);
        initThirdAppController();
        initView();
        initData();
        showTransType();
        setViewListener();

        if (transTypes.isEmpty()) {
            UIHelper.ToastMessage(this, "当前没有可用的支付方式,请先配置");
            onBackPressed();
        }
    }

    private void initThirdAppController() {
        thirdAppController = ThirdAppTransactionController.getInstance();
        thirdAppController.setTransactionTemsController(this);
        thirdAppController.setThridAppFinisher(this);
        thirdAppController.parseRequest(getIntent());
    }

    private void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        setMainView(R.layout.gathering_activity);
        showTitleBack();
        setTitleText(getResources().getString(R.string.pay_cashier));
        // spTransType = (Spinner) findViewById(R.id.sp_trans_type);
        findViewById(R.id.rlPayMode).setOnClickListener(this);
        ivPayMode = (ImageView) findViewById(R.id.ivPayMode);
        tvPayMode = (TextView) findViewById(R.id.tvPayMode);
        tvDiscout = (TextView) findViewById(R.id.tvDiscount);
        etInitAmount = (EditText) findViewById(R.id.etInputAmount);
        if (thirdAppController.isInservice()) {// 第三方应用调用时平铺支付方式，点击支付方式直接进入支付环节
            findViewById(R.id.flInputPad).setVisibility(View.GONE); // 隐藏键盘
            findViewById(R.id.rlPayMode).setVisibility(View.GONE); // 隐藏支付方式选择
            findViewById(R.id.fl_paymode_choose).setVisibility(View.VISIBLE);
            payModeGridView = (GridView) findViewById(R.id.gv_paymode_choose);
            typeAdapter = new TransTypeItemsAdapter(this);
            payModeGridView.setAdapter(typeAdapter);
            thirdMemberCardNo = thirdAppController.getRequestBean()
                    .getMemberCard();
            if (!TextUtils.isEmpty(thirdMemberCardNo)) {// 第三方传入会员卡卡号@hong[20151221]
                swipeMemberCardNo = thirdMemberCardNo;
            }
        } else {
            inputPadFragment = new InputPadFragment();
            inputPadFragment.setOnTextChangedListener(this);
            inputPadFragment.setOnMumberClickListener(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flInputPad, inputPadFragment).commit();
        }
        if (Constants.DISCOUNTABLE) {
            setTitleRight("折扣");
        }
        tvDiscout.setVisibility(View.GONE);
    }

    private void initData() {
        mNetBroadcastReceiver = new NetBroadcastReceiver();
        setNetBroadcastRegist();// 设置网络监听
    }

    private void setViewListener() {
        if (thirdAppController.isInservice()) {// 第三方应用调用
            etInitAmount.setText(Calculater.formotFen(thirdAppController
                    .getRequestBean().getAmount()));
        } else {
            inputPadFragment
                    .setEditView(
                            etInitAmount,
                            com.wizarpos.pay.view.fragment.InputPadFragment.InputType.TYPE_INPUT_MONEY);
            if (getIntent().getStringExtra("initAmount") != null) {
                etInitAmount.setText(getIntent().getStringExtra("initAmount"));
            }
        }
        etInitAmount.setSelection(etInitAmount.getText().length());

        setPayMode();

        if (null != typeAdapter && thirdAppController.isInservice()) {
            payModeGridView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    ThirdAppTransactionRequest requestBean = thirdAppController
                            .getRequestBean();
                    if(transTypes == null && transTypes.size() < position) {
                    		onBackPressed();
                    		return;
                    }
                    requestBean.setChoosePayMode(transTypes.get(position)
                            .getRealValue());
                    skipToTransaction(requestBean);
                }
            });
        }

        int[] btnIds = {R.id.btn_qing_chu, R.id.btn_shoukuan};
        setOnClickListenerByIds(btnIds, this);
    }

    private void setPayMode() {
        if (transTypes != null && !transTypes.isEmpty()) {
            int lastPayMode = Integer.parseInt(AppStateManager.getState(
                    AppStateDef.LAST_PAY_MODE, "0"));
            if (lastPayMode >= transTypes.size()) {// 设为上次选中的支付类型 wu
                lastPayMode = 0;
            }
            ivPayMode.setImageResource(transTypes.get(lastPayMode).getIcon());
            tvPayMode.setText(transTypes.get(lastPayMode).getShowValue());
            tvPayMode.setTag(transTypes.get(lastPayMode));
            transCode = transTypes.get(lastPayMode).getRealValue();
            if (null != typeAdapter && thirdAppController.isInservice()) {
                typeAdapter.setDataChanged(transTypes);
            }
        }
    }

    private void setPayMode(int payMode) {
        if (transTypes != null && !transTypes.isEmpty()) {
            if (payMode >= transTypes.size()) {// 设为上次选中的支付类型 wu
                return;
            }
            for (int i = 0; i < transTypes.size(); i++) {
                if (payMode == transTypes.get(i).getRealValue()) {
                    ivPayMode.setImageResource(transTypes.get(i).getIcon());
                    tvPayMode.setText(transTypes.get(i).getShowValue());
                    tvPayMode.setTag(transTypes.get(i));
                    transCode = transTypes.get(i).getRealValue();
                }
            }
            if (null != typeAdapter && thirdAppController.isInservice()) {
                typeAdapter.setDataChanged(transTypes);
            }
        }
    }

    @Override
    protected void onTitleRightClicked() {
        super.onTitleRightClicked();
        initAmount = etInitAmount.getText().toString();
        if (TextUtils.isEmpty(initAmount) || "0".equals(initAmount)
                || "0.00".equals(initAmount)) {
            UIHelper.ToastMessage(this, "请输入收款金额");
            return;
        }
        initAmount = etInitAmount.getText().toString();
        initAmount = Calculater.formotYuan(initAmount);
        Intent intent = new Intent(this, DiscountActivity.class);
        intent.putExtra(DiscountActivity.DISCUNT_INIT, initAmount);
        startActivityForResult(intent, discountRequestCode);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_shoukuan:
            /*
			 * initAmount = etInitAmount.getText().toString(); //
			 * amount=initAmount; if (!initAmount.equals("0.00")) { initAmount =
			 * etInitAmount.getText().toString(); initAmount =
			 * Calculater.formotYuan(initAmount); doPay(transCode); } break;
			 */
                break;
            case R.id.btn_qing_chu:
                if (thirdAppController.isInservice() == false) {
                    etInitAmount.setText("0.00");
                }
                break;
            case R.id.rlPayMode:
                Intent intent = new Intent(this, ChoosePayModeActivity.class);
                intent.putExtra(ChoosePayModeActivity.TRANS_TYPES,
                        (Serializable) transTypes);
                startActivityForResult(intent, INTENT_CHOOSE_PAYMODE);
                this.overridePendingTransition(R.anim.activity_open_btm_up, 0);
                break;

        }
    }

    /**
     * 重写父类的back方法
     */
    protected void back() {
        if (thirdAppController.isInservice()) {
            serviceFaild();
        } else {
            this.finish();
        }
    }

    // 交易类型
    protected void showTransType() {
        bundleTransTypes(transTypes);
    }

    private boolean isSupportPayItem(String item) {
        return Constants.TRUE.equals(AppConfigHelper.getConfig(item));
    }

    /**
     * 拼装交易类型
     *
     * @param transTypes
     */
    protected void bundleTransTypes(final List<TransTypeItem> transTypes) {
        transTypes.clear();
        if (isSupportPayItem(AppConfigDef.isSupportCash)) {
            if (Constants.APP_VERSION_NAME != Constants.APP_VERSION_LAWSON) {
                transTypes.add(cashTransaction);
            }
        }
        if (Constants.FALSE.equals(AppStateManager.getState(
                AppStateDef.isOffline, Constants.FALSE))) {
            if (DeviceManager.getInstance().isSupportBankCard()
                    && isSupportPayItem(AppConfigDef.isSupportBankCard)) {// 慧银机器支持银行卡消费@hong
                // //M0也不能刷银行卡
                // wu@[20150914]
//				if (Constants.APP_VERSION_NAME != Constants.APP_VERSION_LAWSON) {
                transTypes.add(bankTransaction);// 银行卡
//				}
            }
            if (isSupportPayItem(AppConfigDef.isSupportMemberCard)) {
                if (Constants.APP_VERSION_NAME != Constants.APP_VERSION_LAWSON) {
                    if (!thirdAppController.isInservice()
                            || (thirdAppController.isInservice() && !thirdAppController
                            .getRequestBean().getTransType()
                            .equals("recharge"))) {// 会员充值调用不显示会员卡、其他支付、组合支付
                        // @yaosong
                        // [20160105]
                        transTypes.add(memberTransaction);// 会员卡
                    }
                }
            }
            // bat替换@hong
            if (Constants.BAT_FLAG) {
                if (Constants.FLAG_ON.equals(AppConfigHelper
                        .getConfig(AppConfigDef.isAlipay))
                        && isSupportPayItem(AppConfigDef.isSupportAlipay)) {
                    transTypes.add(alipayTransaction);// 支付宝
                }
                if (Constants.FLAG_ON.equals(AppConfigHelper
                        .getConfig(AppConfigDef.isWepay))
                        && isSupportPayItem(AppConfigDef.isSupportWepay)) {
                    transTypes.add(wepayTransaction);// 微信
                }
                if (Constants.FLAG_ON.equals(AppConfigHelper
                        .getConfig(AppConfigDef.isTenpay))
                        && isSupportPayItem(AppConfigDef.isSupportTenpay)) {
                    transTypes.add(tenpayTransaction);// 手Q支付
                }
                if (Constants.FLAG_ON.equals(AppConfigHelper
                        .getConfig(AppConfigDef.isBaidupay))
                        && isSupportPayItem(AppConfigDef.isSupportBaiduPay)) {
                    transTypes.add(baiduTransaction);// 百度支付
                }
                if (isSupportPayItem(AppConfigDef.isSupportUnionPay)) {
                    transTypes.add(unionpayTransaction);// 移动支付被扫
                }
            } else {
                String appId = AppConfigHelper
                        .getConfig(AppConfigDef.alipay_pattern), key = AppConfigHelper
                        .getConfig(AppConfigDef.alipay_key), agnetId = AppConfigHelper
                        .getConfig(AppConfigDef.alipay_agent_id), used = AppConfigHelper
                        .getConfig(AppConfigDef.use_alipay);
                if ((!TextUtils.isEmpty(appId) && !TextUtils.isEmpty(key) && used
                        .equals("true"))
                        && isSupportPayItem(AppConfigDef.isSupportAlipay)) {
                    transTypes.add(alipayTransaction);// 支付宝
                }
                String wxappId = AppConfigHelper
                        .getConfig(AppConfigDef.weixin_app_id), wxappKey = AppConfigHelper
                        .getConfig(AppConfigDef.weixin_app_key), mchid = AppConfigHelper
                        .getConfig(AppConfigDef.weixin_mchid_id), wxused = AppConfigHelper
                        .getConfig(AppConfigDef.use_weixin_pay), wxagentUsed = AppConfigHelper
                        .getConfig(AppConfigDef.xinpay_agent_pay);
                if ((!TextUtils.isEmpty(wxappId)
                        && !TextUtils.isEmpty(wxappKey)
                        && !TextUtils.isEmpty(mchid) && (wxused
                        .equals(Constants.TRUE) || wxagentUsed
                        .equals(Constants.TRUE)))
                        && isSupportPayItem(AppConfigDef.isSupportWepay)) {
                    transTypes.add(wepayTransaction);// 微信
                }
                String qappId = AppConfigHelper
                        .getConfig(AppConfigDef.tenpay_bargainor_id), qappKey = AppConfigHelper
                        .getConfig(AppConfigDef.tenpay_key), optId = AppConfigHelper
                        .getConfig(AppConfigDef.tenpay_op_user_id), optPasswd = AppConfigHelper
                        .getConfig(AppConfigDef.tenpay_op_user_passwd), qused = AppConfigHelper
                        .getConfig(AppConfigDef.use_tenpay);
                if ((!TextUtils.isEmpty(qappId) && !TextUtils.isEmpty(qappKey)
                        && !TextUtils.isEmpty(optId)
                        && !TextUtils.isEmpty(optPasswd) && qused
                        .equals(Constants.TRUE))
                        && isSupportPayItem(AppConfigDef.isSupportTenpay)) {
                    transTypes.add(tenpayTransaction);// 手Q支付
                }
                String bappId = AppConfigHelper
                        .getConfig(AppConfigDef.baidupay_id), bappKey = AppConfigHelper
                        .getConfig(AppConfigDef.baidupay_key);
                if (((Constants.TRUE.equals(AppConfigHelper
                        .getConfig(AppConfigDef.use_baidupay))) && (!TextUtils
                        .isEmpty(bappId) && !TextUtils.isEmpty(bappKey)))
                        && isSupportPayItem(AppConfigDef.isSupportBaiduPay)) {
                    transTypes.add(baiduTransaction);// 百度支付
                }

            }
            if (isSupportPayItem(AppConfigDef.isSupportOhterPay)) {
                if (Constants.APP_VERSION_NAME != Constants.APP_VERSION_LAWSON) {
                    if (!thirdAppController.isInservice()
                            || (thirdAppController.isInservice() && !thirdAppController
                            .getRequestBean().getTransType()
                            .equals("recharge"))) {// 会员充值调用不显示会员卡、其他支付、组合支付
                        // @yaosong [20160105]
                        transTypes.add(otherpayTransaction);
                    }
                }
            }
            if (isSupportPayItem(AppConfigDef.isSupportMixPay)) {
                if (Constants.APP_VERSION_NAME != Constants.APP_VERSION_LAWSON) {
                    if (!thirdAppController.isInservice()
                            || (thirdAppController.isInservice() && !thirdAppController
                            .getRequestBean().getTransType()
                            .equals("recharge"))) {// 会员充值调用不显示会员卡、其他支付、组合支付
                        // @yaosong [20160105]
                        transTypes.add(mixpayTransaction);
                    }
                }
            }
        }
    }

    protected void doPay(int tranCode) {
        Intent intent = new Intent();
        intent.putExtra(Constants.initAmount, initAmount);
        intent.putExtra("tranCode", tranCode);
        intent.putExtra("discount", discountAmount); // TODO 微信扣减 修改这个字段的名称
        // @yaosong [20151116]
        if (thirdAppController.isInservice()) {// 传入第三方支付请求参数
            intent.putExtra(Constants.cardNo, thirdAppController
                    .getRequestBean().getMemberCard());// 增加获取 cardNo 字段
            intent.putExtra(Constants.thirdRequest,
                    thirdAppController.getRequestBean());
            if (thirdAppController.getRequestBean().getTransType()
                    .equals("recharge")) {
                intent.putExtra("rechargeOn", "1");
            }
        }
        if (tranCode == TransactionTemsController.TRANSACTION_TYPE_CASH) {
            if (Constants.TRUE.equals(AppStateManager.getState(
                    AppStateDef.isOffline, Constants.FALSE))) {
                // 离线时直接走现金支付，跳过营销活动 @yaosong
                toCashTransactionView(this, intent);
            } else {
                intent.putExtra("marketOriginalPrice", initAmount); // 营销原价
                // @yaosong
                // [20151116]
                goMarketReq(tranCode, intent);
            }
        } else if (tranCode == TransactionTemsController.TRANSACTION_TYPE_BANK_CARD) {
            if (swipeBankJSONArray != null) {
                intent.putExtra(BankCardPayActivity.SWIPE_BANK_JSONARRAY,
                        swipeBankJSONArray);
                intent.putExtra(BankCardPayActivity.SWIPE_BANK_NO,
                        swipeBankCardNo);
            }
            if (hasICCard) { // 如果有IC卡，传入一个标识
                intent.putExtra(BankCardPayActivity.HAS_ICCARD, hasICCard);
                hasICCard = false;
            }
            intent.putExtra("marketOriginalPrice", initAmount); // 营销原价 @yaosong
            // [20151116]
            // 营销活动
            goMarketReq(tranCode, intent);
        } else if (tranCode == TransactionTemsController.TRANSACTION_TYPE_MEMBER_CARD) {
            if (!CommonUtil.checkApkExist(this, MEMBER_PACKAGE_NAME)) {
                LogEx.i(LOG_TAG, "会员应用不存在,不能进行会员卡支付");
                Toast.makeText(this, "会员应用不存在,不能进行会员卡支付", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            // toMemberTransactionVew(this, intent);
            intent.putExtra("marketOriginalPrice", initAmount); // 营销原价 @yaosong
            // [20151116]
            goMarketReq(tranCode, intent);
        } else if (tranCode == TransactionTemsController.TRANSACTION_TYPE_ALIPAY) {
            // intent.putExtra("payTypeFlag", Constants.ALIPAY_BAT);//@hong
            // bat多传一个支付标志
            // toAlipayMicroTransaction(this, intent);
            intent.putExtra("marketOriginalPrice", initAmount); // 营销原价 @yaosong
            // [20151116]
            goMarketReq(tranCode, intent);
        } else if (tranCode == TransactionTemsController.TRANSACTION_TYPE_WEPAY_PAY) {
            // intent.putExtra("payTypeFlag", Constants.WEPAY__BAT);//@hong
            // bat多传一个支付标志
            // toWepayMicroTransaction(this, intent);
            intent.putExtra("marketOriginalPrice", initAmount); // 营销原价 @yaosong
            // [20151116]
            goMarketReq(tranCode, intent);
        } else if (tranCode == TransactionTemsController.TRANSACTION_TYPE_TEN_PAY) {
            // intent.putExtra("payTypeFlag", Constants.TENPAY_BAT);//@hong
            // bat多传一个支付标志
            // toTenpayMicroTransaction(this, intent);
            intent.putExtra("marketOriginalPrice", initAmount); // 营销原价 @yaosong
            // [20151116]
            goMarketReq(tranCode, intent);
        } else if (tranCode == TransactionTemsController.TRANSACTION_TYPE_BAIDU_PAY) {
            // intent.putExtra("payTypeFlag", Constants.BAIDUPAY_BAT);//@hong
            // bat多传一个支付标志
            // toBaiduMicroTransaction(this, intent);
            intent.putExtra("marketOriginalPrice", initAmount); // 营销原价 @yaosong
            // [20151116]
            goMarketReq(tranCode, intent);
        } else if (tranCode == TransactionTemsController.TRANSACTION_TYPE_OTHER) {
            toOtherTransactionView(this, intent);
        } else if (tranCode == TransactionTemsController.TRANSACTION_TYPE_MIXPAY) {
            if (thirdAppController.isInservice()) {
                intent.putExtra(Constants.saleInputAmount, initAmount);
            }
            toMixTransaction(this, intent);
        } else if (tranCode == TransactionTemsController.TRANSACTION_TYPE_UNION_PAY) {
            intent.putExtra("marketOriginalPrice", initAmount); // 营销原价 @yaosong
            // [20151116]
            goMarketReq(tranCode, intent);
        }
        if (tvDiscout != null) {
            tvDiscout.setVisibility(View.GONE); // 跳转支付后隐藏扣减提示@yaosong[20151116]
        }
    }

    /**
     * @Author: Huangweicai
     * @date 2015-9-10 上午10:04:45
     * @Description: 网络状态监听
     */
    private void setNetBroadcastRegist() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constents.PAYACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mNetBroadcastReceiver, filter);
    }

    /**
     * @author Huangweicai
     * @ClassName: NetBroadcastReceiver
     * @date 2015-9-10 上午10:07:02
     * @Description: 网络监听
     */
    public class NetBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            transTypes = new ArrayList<TransTypeItem>();
            bundleTransTypes(transTypes);
            if (transTypes.size() < 1) {
                return;
            }
            transCode = transTypes.get(0).getRealValue();
            tvPayMode.setText(transTypes.get(0).getShowValue());
            ivPayMode.setImageResource(transTypes.get(0).getIcon());
        }
    }

    @Override
    public void hideCardTransaction() {
        removeTransactionTem(bankTransaction);
    }

    @Override
    public void hideMemberTransaction() {
        removeTransactionTem(memberTransaction);
    }

    @Override
    public void hideCashTransaction() {
        removeTransactionTem(cashTransaction);
    }

    @Override
    public void hideMixTransaction() {
        removeTransactionTem(mixpayTransaction);
    }

    @Override
    public void hideAlipayTransaction() {
        removeTransactionTem(alipayTransaction);
    }

    @Override
    public void hideAlipayMicroTransaction() {

    }

    @Override
    public void hideAlipayNativeTransaction() {

    }

    @Override
    public void hideWxpayTransacton() {
        removeTransactionTem(wepayTransaction);
    }

    @Override
    public void hideWxpayMicroTransaction() {

    }

    @Override
    public void hideWxpayNativeTransaction() {

    }

    @Override
    public void hideBaiduTransaction() {
        removeTransactionTem(baiduTransaction);
    }

    @Override
    public void hideBaiduNativeTransaction() {

    }

    @Override
    public void hideBaiduMicroTransaction() {

    }

    @Override
    public void hideTenpayTransaction() {
        removeTransactionTem(tenpayTransaction);
    }

    @Override
    public void hideTenpayNativeTransaction() {

    }

    @Override
    public void hideTenpayMicroTransaction() {

    }

    @Override
    public void hideTicketPassTransaction() {

    }

    @Override
    public void hideOtherTransaction() {
        removeTransactionTem(otherpayTransaction);
    }

    @Override
    public void skipToTransaction(ThirdAppTransactionRequest requestBean) {
        int payModeCode = requestBean.getChoosePayMode();
        // 重新组装一次transTypes,解决第三方调用直接跳转到指定支付方式时transTypes为empty的问题 @yaosong
        bundleTransTypes(transTypes);
        if (transTypes.isEmpty()) {
            UIHelper.ToastMessage(this, "当前没有可用的支付方式,请先配置");
            onBackPressed();
            return;
        }
        for (TransTypeItem item : transTypes) {
            if (payModeCode == item.getRealValue()) {
                transCode = payModeCode;
                initAmount = thirdAppController.getRequestBean().getAmount();
                doPay(transCode);
                return;
            }
        }
        onBackPressed();
    }

    private void removeTransactionTem(TransTypeItem item) {
        if (transTypes != null) {
            transTypes.remove(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPayMode();
        initScanner();
        startIC();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DeviceManager.getInstance().closeCardListener();
        endIC();
    }

    /**
     * @author yaosong
     */
    protected void initScanner() {
        if (DeviceManager.getInstance().isSupportSwipe()) {// 增加设备类型判断,否则在x86的机器会崩溃
            // wu
            DeviceManager.getInstance().startSwipeListener(new Handler(),
                    swipeListener);
        }
    }

    @Override
    public void onResult(Intent intent) {// 消费流程结束
        Logger2.debug("消费流程完成");
        try {
            if (thirdAppController.isInservice()) {
                bundleThridTransactionResponse(intent);
            } else {
                etInitAmount.setText("0.00");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();// 取消交易流程监听
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mNetBroadcastReceiver);
    }

    @Override
    public void onSubmit() {
        initAmount = etInitAmount.getText().toString();
        if (TextUtils.isEmpty(initAmount) || "0".equals(initAmount)
                || "0.00".equals(initAmount)) {
            UIHelper.ToastMessage(this, "请输入收款金额");
            return;
        }
        initAmount = etInitAmount.getText().toString();
        initAmount = Calculater.formotYuan(initAmount);
        doPay(transCode);
    }

    @Override
    public void onTextChanged(String newStr) {
        discountAmount = "0";
        initAmount = "0";
        tvDiscout.setText("已扣减: 0.00");
        inputPadFragment.setNumberEditable(true);
    }

    // 营销活动请求参数拼接 @yaosong [20151216]
    private void goMarketReq(int tranCode, Intent intent) {
        String mid = AppConfigHelper.getConfig(AppConfigDef.mid);
        MarketPayReq req = new MarketPayReq();
        String memberCard = "";
        req.setReChangeOn("0");
        req.setCardNo("");
        if (thirdAppController.isInservice()) {
            memberCard = thirdAppController.getRequestBean().getMemberCard();
            if (!TextUtils.isEmpty(memberCard)) {// 是否是会员卡调用
                req.setCardNo(memberCard);
            }
            if (thirdAppController.getRequestBean().getTransType()	//bugfix 会员充值没有卡号，不可用卡号是否为空来判断是否充值 yaosong [20160301] 
            		.equals(thirdAppController.RECHARGE)) {// 是否是充值
            	req.setReChangeOn("1");
            } else {
            	req.setReChangeOn("0");
            }
        }
        req.setMid(mid);
        switch (tranCode) {
            case TransactionTemsController.TRANSACTION_TYPE_CASH:
                req.setPayMethod(MarketPayReq.PAY_MODE_CASH);
                break;
            case TransactionTemsController.TRANSACTION_TYPE_MEMBER_CARD:
                req.setPayMethod(MarketPayReq.PAY_MODE_MEMBER_CARD);
                break;
            case TransactionTemsController.TRANSACTION_TYPE_WEPAY_PAY:
                req.setPayMethod(MarketPayReq.PAY_MODE_WEIXIN);
                break;
            case TransactionTemsController.TRANSACTION_TYPE_ALIPAY:
                req.setPayMethod(MarketPayReq.PAY_MODE_ALIPAY);
                break;
            case TransactionTemsController.TRANSACTION_TYPE_BANK_CARD:
                req.setPayMethod(MarketPayReq.PAY_MODE_BANKCARD);
                break;
            case TransactionTemsController.TRANSACTION_TYPE_BAIDU_PAY:
                req.setPayMethod(MarketPayReq.PAY_MODE_BAIDU);
                break;
            case TransactionTemsController.TRANSACTION_TYPE_TEN_PAY:
                req.setPayMethod(MarketPayReq.PAY_MODE_TENPAY);
                break;
            case TransactionTemsController.TRANSACTION_TYPE_UNION_PAY:
                req.setPayMethod("");
                break;
            default:
                break;
        }
        req.setPayAmount(initAmount);
        goMarketing(tranCode, req, intent);
    }

    // 营销活动一期
    private void goMarketing(final int tranCode, MarketPayReq req,
                             final Intent intent) {
        progresser.showProgress();
        TransactionMarketingActivitiesPresenter presenter = new TransactionMarketingActivitiesPresenter(
                this, req);
        presenter.goMarketPay(req, new ResultListener() {

            @Override
            public void onSuccess(Response response) {
                MarketPayRes res = JSONObject.parseObject(
                        response.result.toString(), MarketPayRes.class);
                LogEx.e("GatheringActivity", String.valueOf(response.result));
                if (thirdAppController.isInservice()) {
                    intent.putExtra(ThirdAppTransactionController.RECHARGE,thirdAppController.getRequestBean().getTransType());
                }
                intent.putExtra(Constants.initAmount, (res.getReducAmount()));
                intent.putExtra("ticketIds", res.getTicketIds().toJSONString());
                if (tranCode == TransactionTemsController.TRANSACTION_TYPE_CASH) {
                    toCashTransactionView(GatheringActivity.this, intent);
                } else if (tranCode == TransactionTemsController.TRANSACTION_TYPE_ALIPAY) {
                    // toAlipayMicroTransaction(GatheringActivity.this, intent);
                    intent.setClass(GatheringActivity.this,
                            CommonThirdPayActivity.class);
                    startActivity(intent);
                } else if (tranCode == TransactionTemsController.TRANSACTION_TYPE_WEPAY_PAY) {
                    // toAlipayMicroTransaction(GatheringActivity.this, intent);
                    intent.setClass(GatheringActivity.this,
                            CommonThirdPayActivity.class);
                    startActivity(intent);
                } else if (tranCode == TransactionTemsController.TRANSACTION_TYPE_MEMBER_CARD) {
                    if (!TextUtils.isEmpty(swipeMemberCardNo)) {
                        intent.putExtra("cardNo", swipeMemberCardNo);
                        intent.setClass(GatheringActivity.this,
                                MemberCardDetailActivity.class);
                        startActivity(intent);
                    } else {
                        // 改成调用会员应用的识别刷卡
                        toMemberInputView(GatheringActivity.this, "会员卡",
                                intent, GO_INTPUT_MEMEBR_NO);
//						toInputView(GatheringActivity.this, "会员卡", intent,
//								GO_INTPUT_MEMEBR_NO);
                    }
                } else if (tranCode == TransactionTemsController.TRANSACTION_TYPE_BANK_CARD) {
                    toCardTransactionView(GatheringActivity.this, intent);
                } else if (tranCode == TransactionTemsController.TRANSACTION_TYPE_TEN_PAY) {
                    // toAlipayMicroTransaction(GatheringActivity.this, intent);
                    intent.setClass(GatheringActivity.this,
                            CommonThirdPayActivity.class);
                    startActivity(intent);
                } else if (tranCode == TransactionTemsController.TRANSACTION_TYPE_BAIDU_PAY) {
                    // toAlipayMicroTransaction(GatheringActivity.this, intent);
                    intent.setClass(GatheringActivity.this,
                            CommonThirdPayActivity.class);
                    startActivity(intent);
                } else if (tranCode == TransactionTemsController.TRANSACTION_TYPE_UNION_PAY) {
                     toUnionMicroTransaction(GatheringActivity.this, intent);
                    intent.setClass(GatheringActivity.this,
                            CommonThirdPayActivity.class);
                    startActivity(intent);
                }
                progresser.showContent();
            }

            @Override
            public void onFaild(Response response) {
                progresser.showContent();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int code = KeyEvent.KEYCODE_0;
        if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            code = keyCode - code;
            inputPadFragment.onNumber(String.valueOf(code));
        } else if (keyCode == KeyEvent.KEYCODE_DEL) {
            inputPadFragment.onDel();
        }
        if (keyCode == KEYCODE_SCAN_LEFT || keyCode == KEYCODE_SCAN_RIGHT
                || keyCode == KEYCODE_QR ) {
            if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
                if (transTypes.contains(unionpayTransaction)) {
                    if (System.currentTimeMillis() -holdTime > 5000) {
                        initAmount = etInitAmount.getText().toString();
                        if (TextUtils.isEmpty(initAmount) || "0".equals(initAmount)
                                || "0.00".equals(initAmount)) {
                            UIHelper.ToastMessage(GatheringActivity.this, "请输入收款金额");
                            setPayMode(TransactionTemsController.TRANSACTION_TYPE_UNION_PAY);
                        }else{
                            initAmount = Calculater.formotYuan(initAmount);
                            doPay(TransactionTemsController.TRANSACTION_TYPE_UNION_PAY);
                        }
                    }
                    holdTime = System.currentTimeMillis();
                } else {
                    UIHelper.ToastMessage(GatheringActivity.this, "您的设备未开启移动支付");
                    holdTime = System.currentTimeMillis();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);

        if (arg0 == GO_INTPUT_MEMEBR_NO && arg1 == RESULT_CANCELED) {
            serviceFaild("用户取消");
        }
        if (arg0 == GO_INTPUT_MEMEBR_NO && arg1 == RESULT_OK) {
            if (arg2 == null) { //bugfix wu
                return;
            }
            String msg = arg2.getSerializableExtra("msg").toString();
            JSONObject jsonObject = JSONObject.parseObject(msg);
            int resultCode = jsonObject.getIntValue("code");
            if(resultCode != 0) {//修复bug 
            		Toast.makeText(this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
            		return;
            }
            
            Card mCard = jsonObject.getObject("data", Card.class);
            String cardNo = mCard.getCardNo();
            // String cardNo = arg2.getStringExtra(InputInfoActivity.content);
            // int inputType = arg2.getIntExtra(InputInfoActivity.type, 0);
            if (TextUtils.isEmpty(cardNo)) {
                return;
            }
            toMemberIntent.putExtra("cardNo", cardNo);
            if ("1".equals(getIntent().getStringExtra(Constants.mixFlag))) {
                setResult(RESULT_OK, toMemberIntent);
                this.finish();
            } else {
                toMemberIntent.setClass(this, MemberCardDetailActivity.class);
                startActivity(toMemberIntent);
                // this.finish();//除 bug ,第三方应用调用无法设置返回参数 wu@[20151209]
            }
        } else if (arg0 == INTENT_CHOOSE_PAYMODE && arg1 == RESULT_OK) {
            TransTypeItem payMode = (TransTypeItem) arg2
                    .getSerializableExtra("payMode");
            ivPayMode.setImageResource(payMode.getIcon());
            tvPayMode.setText(payMode.getShowValue());
            tvPayMode.setTag(payMode);
            transCode = payMode.getRealValue();
            AppStateManager.setState(AppStateDef.LAST_PAY_MODE,
                    arg2.getIntExtra("position", 0) + "");
        } else if (arg0 == discountRequestCode && arg1 == RESULT_OK) {
            discountAmount = arg2.getStringExtra(Constants.discountAmount);
            String showAmout = Calculater.formotFen(discountAmount);
            tvDiscout.setText("微信扣减: "
                    + (TextUtils.isEmpty(showAmout) ? "0.00" : showAmout));
            tvDiscout.setVisibility(View.VISIBLE);
            etInitAmount.setText(Calculater.formotFen(Calculater.subtract(
                    initAmount, discountAmount)));
            inputPadFragment.setNumberEditable(false);// 设置扣减金额后不能再次编辑金额
        }
    }

    private void startIC() {
        if (DeviceManager.getInstance().isSupportBankCard()) {
            DeviceManager.getInstance().startSmartCardListener(
                    new BasePresenter.ResultListener() {
                        @Override
                        public void onSuccess(Response response) {// Tips
                            // 此处回调在子线程中
                            if (transTypes.contains(bankTransaction)) {
                                LogEx.d("IC", response.msg);
                                hasICCard = true;
                                initAmount = etInitAmount.getText().toString();
                                if (TextUtils.isEmpty(initAmount)
                                        || "0".equals(initAmount)
                                        || "0.00".equals(initAmount)) {
                                    mHandler.sendEmptyMessage(HANDLER_CODE_1);
                                } else {
                                    initAmount = Calculater
                                            .formotYuan(initAmount);
                                    mHandler.sendEmptyMessage(HANDLER_CODE_4);
                                }
                            } else {
                                mHandler.sendEmptyMessage(HANDLER_CODE_3);
                            }
                        }

                        @Override
                        public void onFaild(Response response) {
                            LogEx.d("IC", response.msg);
                            mHandler.sendEmptyMessage(HANDLER_CODE_2);
                        }
                    });
        }
    }

    private void endIC() {
        if (DeviceManager.getInstance().isSupportBankCard()) {
            DeviceManager.getInstance().closeSmartCard();
        }
    }

}
