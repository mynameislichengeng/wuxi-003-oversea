package com.wizarpos.pay.ui.newui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wizarpos.base.net.Response;
import com.wizarpos.hspos.api.EnumCommand;
import com.wizarpos.hspos.api.TransInfo;
import com.wizarpos.pay.app.ImageLoadApp;
import com.wizarpos.pay.cardlink.CardLinkListener;
import com.wizarpos.pay.cardlink.CardLinkPresenter;
import com.wizarpos.pay.cardlink.CardLinkTransaction;
import com.wizarpos.pay.cardlink.SignOnProxy;
import com.wizarpos.pay.cardlink.model.EnumProgressCode;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.MerchantLogoHelper;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.TokenGenerater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.login.view.LoginMerchantRebuildActivity;
import com.wizarpos.pay.recode.sale.service.impl.InvoiceServiceImpl;
import com.wizarpos.pay.recode.test.TestActivity;
import com.wizarpos.pay.ui.newui.entity.ItemBean;
import com.wizarpos.pay.ui.newui.fragment.NewGatheringFragment;
import com.wizarpos.pay.ui.newui.fragment.NewQ2GatheringFragment;
import com.wizarpos.pay.ui.newui.fragment.ReceivablesFragment;
import com.wizarpos.pay.ui.newui.util.ItemDataUtils;
import com.wizarpos.pay.ui.widget.CommonToastUtil;
import com.wizarpos.pay.ui.widget.RoundAngleImageView;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.wizarpos.pay.view.util.Tools;
import com.wizarpos.pay2.lite.R;

public class NewMainActivity extends NewBaseMainActivity implements OnClickListener, CardLinkListener, CardLinkPresenter.EndTransListener, ReceivablesFragment.PayBtnClickListener, ReceivablesFragment.OnSaveListener {

    private static final String LOG_TAG = NewMainActivity.class.getName();
    private static final String EXTAR_RETURN = "isReturn";
    private ListView lvType;
    private DrawerLayout dl;
    private ActionBarDrawerToggle drawerToggle;
    private int TICKET_USE = 0;
    private int TRANSACTION_RECORD = 1;
    private int TICKET_USE_RECORD = 2;
    private int TODAY_RECORD = 3;
    private int SETTING = 4;
    private int TIP_SETTING = 5;
    private int EXIT = 6;
    private int TESTWEXIN = 7;
    private int MODIFY_SECURE_PASSWORD = 8;
    private static long holdtime = 0L;
    private static long backholdtime = 0L;
    private final static String TAG_GATHERING_FRAGMENT = "gathering_fragment";
    private final static String TAG_GATHERING_FRAGMENT_Q2 = "gathering_fragment2";
    private NewGatheringFragment newGatheringFragment;
    private NewQ2GatheringFragment newQ2GatheringFragment;
    private final static String TAG_RECEIVABLES_FRAGMENT = "receivables_fragment";
    private ReceivablesFragment receivablesFragment;
    private CardLinkTransaction cardLinkTransaction;
    private int PAY_REQUEST_CODE = 100;
    private int SET_REQUEST_CODE = 101;
    private int TIP_SETTING_CODE = 102;
    private String payAmounts, tipAmount;
    private int amount;

    private boolean bankcardProgressing = false;
    private boolean bankCardComfirmCard = false;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, NewMainActivity.class);
    }

    public static Intent getReturnIntent(Context context) {
        Intent intent = new Intent(context, NewMainActivity.class);
        intent.putExtra(EXTAR_RETURN, true);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate()");
        initView();
        initData();
        requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        clearToast();
        super.onPause();
    }

    @Override
    protected void onStop() {
        clearToast();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "onDestroy()");
        clearToast();
        cardLinkTransaction.endTrans();
        super.onDestroy();
    }

    private void initView() {
        setTitle();
        dl = (DrawerLayout) findViewById(R.id.dl);
        lvType = (ListView) findViewById(R.id.lvType);
        RoundAngleImageView roundAngleImageView = (RoundAngleImageView) findViewById(R.id.ivMerchantLogo);
        TextView tvMerchantName = (TextView) findViewById(R.id.tvMerchantName);
        TextView tvOpreator = (TextView) findViewById(R.id.tvOpreator);
        String url = MerchantLogoHelper.getMerchantLogoUrl();
        showImage(url, roundAngleImageView);
        tvOpreator.setText(getResources().getString(R.string.employee) + ":" + AppConfigHelper.getConfig(AppConfigDef.operatorTrueName, getResources().getString(R.string.employee) + "00"));
        if (TextUtils.isEmpty(url)) {
            roundAngleImageView.setBackgroundResource(R.drawable.logo);
        }
        tvMerchantName.setText(AppConfigHelper.getConfig(AppConfigDef.merchantName));
        drawerToggle = new ActionBarDrawerToggle(this, dl, (Toolbar) findViewById(R.id.toolbarOwner), R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        dl.addDrawerListener(drawerToggle);
        setMainFragment();
    }

    private void setTitle() {
        Toolbar toolbarOwner = (Toolbar) findViewById(R.id.toolbarOwner);
        if (toolbarOwner != null) {
            toolbarOwner.setVisibility(View.VISIBLE);
            setSupportActionBar(toolbarOwner);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        TextView tvTitleOwner = (TextView) findViewById(R.id.tvTitleOwner);
        if (tvTitleOwner != null) {
            tvTitleOwner.setText(getString(R.string.sale));
            tvTitleOwner.setVisibility(View.VISIBLE);
        }
    }

    private void setMainFragment() {
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
            newGatheringFragment = new NewGatheringFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.flContent, newGatheringFragment, TAG_GATHERING_FRAGMENT).commit();
        } else {
            newQ2GatheringFragment = new NewQ2GatheringFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.flContent, newQ2GatheringFragment, TAG_GATHERING_FRAGMENT_Q2).commit();
            newQ2GatheringFragment.setOnConfirmListener(new NewQ2GatheringFragment.OnConfirmListener() {
                @Override
                public void onComfirm() {
                    goQ2Pay();
                }
            });
        }
//        receivablesFragment = new ReceivablesFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.flContent, receivablesFragment, TAG_RECEIVABLES_FRAGMENT).commit();
    }

    private void showImage(String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            ImageLoader.getInstance().displayImage(url, imageView, ImageLoadApp.getOptions());
            imageView.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        initSale();
        lvType.setAdapter(new QuickAdapter<ItemBean>(NewMainActivity.this, R.layout.item_left_layout, ItemDataUtils.getItemBeans(NewMainActivity.this)) {
            @Override
            protected void convert(BaseAdapterHelper helper, final ItemBean item) {
                helper.setImageResource(R.id.item_img, item.getImg())
                        .setText(R.id.item_tv, item.getTitle());
                helper.getView(R.id.llLferItem).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TICKET_USE == item.getRealValue()) {//卡券核销
                            Toast.makeText(NewMainActivity.this, "暂不支持卡券核销", Toast.LENGTH_SHORT).show();
                        } else if (TRANSACTION_RECORD == item.getRealValue()) {//交易记录
                            startNewActivity(NewTranlogActivity.class);
//                            Toast.makeText(NewMainActivity.this, "transaction record", Toast.LENGTH_SHORT).show();
                        } else if (TICKET_USE_RECORD == item.getRealValue()) {//卡券核销记录
                            Toast.makeText(NewMainActivity.this, "暂不支持卡券核销", Toast.LENGTH_SHORT).show();
                        } else if (TODAY_RECORD == item.getRealValue()) {//今日汇总

                            startNewActivity(NewDailySumActivityPlus.class);

                            //test todo
//                            TestActivity.startAc(NewMainActivity.this);
                        } else if (SETTING == item.getRealValue()) {//设置
                            startActivity(new Intent(NewMainActivity.this, NewSettingActivity.class));
                            NewMainActivity.this.finish();
//                        } else if (TIP_SETTING == item.getRealValue()) {//小费设置
//                            startActivityForResult(new Intent(NewMainActivity.this, InputPassWordActivity.class), TIP_SETTING_CODE);
//                            startActivity(new Intent(NewMainActivity.this, TipParameterSettingActivity.class));
//                            finish();
//                        } else if (MODIFY_SECURE_PASSWORD == item.getRealValue()) {//安全密码修改
//                            startActivity(new Intent(NewMainActivity.this, ModifyPasswordActivity.class));
                        } else if (EXIT == item.getRealValue()) {//退出
                            AppStateManager.clearCache();
                            AppConfigHelper.clearCache();
                            NewMainActivity.this.finish();
                            AppStateManager.setState(AppStateDef.isLogin, Constants.FALSE);
                            AppStateManager.setState(AppStateDef.IS_SIGN_OUT_EXIT, Constants.TRUE);
                            LoginMerchantRebuildActivity.startActivity(NewMainActivity.this);
                        } else if (TESTWEXIN == item.getRealValue()) {//测试第三方支付
                        }
                    }
                });
            }
        });
        //清除登出标记
        AppStateManager.setState(AppStateDef.IS_SIGN_OUT_EXIT, Constants.FALSE);
//        //TODO testprinter
//        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
//            for (int i = 0; i < 10; i++) {
//                WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT));
////                PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT)));
//            }
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String collectTips = AppConfigHelper.getConfig(AppConfigDef.collectTips);
        int code = KeyEvent.KEYCODE_0;
        if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            if (bankcardProgressing) {
                return true;
            }
            code = keyCode - code;
            if (null != newGatheringFragment) {
                newGatheringFragment.inputKey(String.valueOf(code));
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (bankcardProgressing) {
                return true;
            }
            if (null != newGatheringFragment) {
                newGatheringFragment.onDel();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_ESCAPE) { //取消按钮
            if (bankcardProgressing) {
//                AppMsg.cancelAll();
                clearToast();
                clear();
                return true;
            }
        } else if (keyCode == KEYCODE_SCAN_LEFT) {
//            if (System.currentTimeMillis() - holdtime > 2000) {
//                bankCardPay();
//                holdtime = System.currentTimeMillis();
//            }
//            goQrPay();
            Log.i("KEY", "KEYCODE_SCAN_LEFT");
            if (!TextUtils.isEmpty(collectTips) && collectTips.equals(Constants.COLLETOFF) && receivablesFragment == null) {
                goUnionPay();
            } else if (receivablesFragment != null) {
                Intent intent = new Intent();
                if (payAmounts.equals("0.00")) {
                    Toast.makeText(this, getResources().getString(R.string.payamount_warn), Toast.LENGTH_SHORT).show();
                    return true;
                }
                intent.putExtra(Constants.realAmount, Calculater.formotYuan(payAmounts));
                intent.putExtra(Constants.tipsAmount, Calculater.formotYuan(tipAmount));
                intent.setClass(NewMainActivity.this, NewMicroActivity.class);
                startActivity(intent);
                finish();
            } else {
                String amount = newGatheringFragment.getAmount();
                if (amount == null) {
                    amount = "0.00";
                }
                if (amount.equals("0.00")) {
                    Toast.makeText(this, getResources().getString(R.string.payamount_warn), Toast.LENGTH_SHORT).show();
                    return true;
                }
                receivablesFragment = new ReceivablesFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, receivablesFragment, TAG_RECEIVABLES_FRAGMENT).commit();
            }
            return true;
        } else if (keyCode == KEYCODE_QR) {
//            if (bankcardProgressing) {
//                return true;
//            }
//            goQrPay();
            Log.d("KEY", "KEYCODE_QR");
            if (!TextUtils.isEmpty(collectTips) && collectTips.equals(Constants.COLLETOFF) && receivablesFragment == null) {
                goUnionPay();
            } else if (receivablesFragment != null) {
                Intent intent = new Intent();
                if (payAmounts.equals("0.00")) {
                    Toast.makeText(this, getResources().getString(R.string.payamount_warn), Toast.LENGTH_SHORT).show();
                    return true;
                }
                intent.putExtra(Constants.realAmount, Calculater.formotYuan(payAmounts));
                intent.putExtra(Constants.tipsAmount, Calculater.formotYuan(tipAmount));
                intent.setClass(NewMainActivity.this, NewMicroActivity.class);
                startActivity(intent);
                finish();
            } else {
                String amount = newGatheringFragment.getAmount();
                if (TextUtils.isEmpty(amount)) {
                    amount = "0.00";
                }
                if (amount.equals("0.00")) {
                    Toast.makeText(this, getResources().getString(R.string.payamount_warn), Toast.LENGTH_SHORT).show();
                    return true;
                }
                receivablesFragment = new ReceivablesFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, receivablesFragment, TAG_RECEIVABLES_FRAGMENT).commit();

            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - backholdtime > 2000 && receivablesFragment == null) {
                backholdtime = System.currentTimeMillis();
                CommonToastUtil.showMsgBelow(this, CommonToastUtil.LEVEL_INFO, getResources().getString(R.string.exit_info));
                return true;
            } else if (null != receivablesFragment) {
                receivablesFragment = null;
                if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent, newGatheringFragment, TAG_GATHERING_FRAGMENT).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent, newQ2GatheringFragment, TAG_GATHERING_FRAGMENT_Q2).commit();
                }
                return false;
            } else {
                AppStateManager.clearCache();
                AppConfigHelper.clearCache();
                AppStateManager.setState(AppStateDef.isLogin, Constants.FALSE);
                NewMainActivity.this.finish();
                System.exit(0);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goUnionPay() {
        String amount = "";
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
            amount = newGatheringFragment.getAmount();
        } else {
            amount = newQ2GatheringFragment.getAmount();
        }
        if (TextUtils.isEmpty(amount)) {
            Toast.makeText(this, getResources().getString(R.string.payamount_warn), Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent intent = new Intent();
            if (amount.equals("0.00")) {
                Toast.makeText(this, getResources().getString(R.string.payamount_warn), Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putExtra(Constants.realAmount, Calculater.formotYuan(amount));
            intent.setClass(NewMainActivity.this, NewMicroActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public String getAmount() {
        String amount = "";
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
            amount = newGatheringFragment.getAmount();
        } else {
            amount = newQ2GatheringFragment.getAmount();
        }
        return amount;
    }

    private void goQ2Pay() {
        String collectTips = AppConfigHelper.getConfig(AppConfigDef.collectTips);
        if (!TextUtils.isEmpty(collectTips) && collectTips.equals(Constants.COLLETOFF) && receivablesFragment == null) {
            goUnionPay();
        } else if (receivablesFragment != null) {
            Intent intent = new Intent();
            intent.putExtra(Constants.realAmount, Calculater.formotYuan(payAmounts));
            intent.putExtra(Constants.tipsAmount, Calculater.formotYuan(tipAmount));
            intent.setClass(NewMainActivity.this, NewMicroActivity.class);
            startActivity(intent);
            finish();
        } else {
            receivablesFragment = new ReceivablesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, receivablesFragment, TAG_RECEIVABLES_FRAGMENT).commit();

        }
    }


    private void initSale() {
        cardLinkTransaction = new CardLinkTransaction(this);
        cardLinkTransaction.setCardLinkListener(this);
        cardLinkTransaction.setEndTransListener(this);
        if (getIntent().getBooleanExtra(EXTAR_RETURN, false)) {  //缺少必要注释, 为什么要调用 endTrans?
            cardLinkTransaction.endTrans();
        } else {
//            startSale(); //进入主页面后不再立刻开启交易
        }
        InvoiceServiceImpl.getInstance().clearInvoiceValue();
    }

    private void startSale() {
        if (SignOnProxy.isTodayFirstLogin() || !Constants.TRUE.equals(AppConfigHelper.getConfig(AppConfigDef.CARDLINK_LOGIN))) {
            showProgressMsg("正在签到...");
            cardLinkTransaction.signOn();
        } else {
            cardLinkTransaction.sale();
        }
    }

    private void showForeverInfo(String msg) {
        CommonToastUtil.showForeverToast(this, msg, CommonToastUtil.LEVEL_INFO, CommonToastUtil.Y_BELOW_CENTER);
    }

    private void showProgressMsg(String msg) {
        CommonToastUtil.showMsgBelow(this, CommonToastUtil.LEVEL_INFO, msg);
    }

    private void showErrorMsg(String msg) {
        CommonToastUtil.showMsgBelow(this, CommonToastUtil.LEVEL_ERROR, msg);
    }

    private void showWarnMsg(String msg) {
        CommonToastUtil.showMsgBelow(this, CommonToastUtil.LEVEL_WARN, msg);
    }

    private void showNotifyMsg(String msg) {
        CommonToastUtil.showMsgBelow(this, CommonToastUtil.LEVEL_INFO, msg);
    }

    @Override
    public void onProgress(EnumCommand cmd, int progressCode, String progress, boolean continueTrans) {
        if (progressCode == EnumProgressCode.InputTransAmount.getCode()) {
            if (amount > 0) {
                cardLinkTransaction.setAmount(amount);
                if (continueTrans) {
                    cardLinkTransaction.continueTrans();
                }
            }
        } else if (progressCode == EnumProgressCode.RequestCard.getCode()) {
            bankcardProgressing = true;
            if (!isFinishing()) {
                showForeverInfo(progress);
            }
//            showProgressMsg(progress);
            if (continueTrans) {
                cardLinkTransaction.continueTrans();
            }
        } else if (progressCode == EnumProgressCode.ConfirmPAN.getCode()) {
//            AppMsg.cancelAll();
            clearToast();
            String cardNo = getCardNo();
            showNotifyMsg("请确认卡号是否正确");
            if (!TextUtils.isEmpty(cardNo)) {
                newGatheringFragment.setCardNo(cardNo);
                bankCardComfirmCard = true;
            }
        } else if (progressCode == EnumProgressCode.InputPIN.getCode()) {
//            AppMsg.cancelAll();
            clearToast();
        } else if (cmd == EnumCommand.Sale && progressCode == EnumProgressCode.ProcessOnline.getCode()) {
            progresser.showProgress();
        }
    }

    private void clearToast() {
        CommonToastUtil.stopAnyway();
    }

    @Override
    public void onTransSucceed(EnumCommand cmd) {
        if (cmd == EnumCommand.Login || cmd == EnumCommand.InitKey) {
            showNotifyMsg("签到成功");
            cardLinkTransaction.sale();
        } else if (cmd == EnumCommand.Sale) {
            progresser.showProgress();
            String cardNo = newGatheringFragment.getCardNo();
            if (TextUtils.isEmpty(cardNo)) {
                cardNo = getCardNo();
            }
            TransInfo transInfo = null;
            if (cardLinkTransaction != null) {
                transInfo = cardLinkTransaction.getTransInfo();
            }
            //上送交易
            cardLinkTransaction.paySuccess(TokenGenerater.newToken(), cardNo, transInfo, new BasePresenter.ResultListener() {
                @Override
                public void onSuccess(Response response) {
                    progresser.showContent();
                    startActivity(NewPaySuccessActivity.getStartIntent(NewMainActivity.this, "BANK", amount + ""));
                    NewMainActivity.this.finish();
                }

                @Override
                public void onFaild(Response response) {
                    progresser.showContent();
                    Log.d(LOG_TAG, "交易上送失败");
                    if (response.code == CardLinkTransaction.CALL_700_FAILED) {
                        DialogHelper2.showDialog(NewMainActivity.this, "网络请求失败，请点击确认重试。", new DialogHelper2.DialogListener() {

                            @Override
                            public void onOK() {
                                progresser.showProgress();
                                cardLinkTransaction.reTryUploadTrans();
                            }

                        });
                        return;
                    }
                    clear();
                    showErrorMsg(response.getMsg());
                }
            });
        }
    }

    @Override
    public void onTransFailed(EnumCommand cmd, String message) {
        if (TextUtils.isEmpty(message)) {

        } else if (cmd == EnumCommand.Login) {
            showErrorMsg("签到失败 " + message);
        } else if (message.contains("系统正忙")) {

        } else if (cmd == EnumCommand.Sale) {
            clear();
            showErrorMsg("交易失败 " + message);
        } else {
            showErrorMsg(message);
        }

    }

    @Override
    public void onEndTransFinish() {
        if (bankcardProgressing || getIntent().getBooleanExtra(EXTAR_RETURN, false)) {
            goonClear();
        }
    }

    private void clear() {
        if (newGatheringFragment != null) {
            newGatheringFragment.reset();
        }
        if (newQ2GatheringFragment != null) {
            newQ2GatheringFragment.reset();
        }
        amount = 0;
        cardLinkTransaction.setAmount(0);
        cardLinkTransaction.endTrans();
        bankCardComfirmCard = false;
    }

    private void goonClear() {
//        cardLinkTransaction.sale();//进入主页面后不再立刻开启交易
        progresser.showContent();
        bankcardProgressing = false;
        Log.i("YS!!", "HApi Restart!");
    }

    @NonNull
    private String getCardNo() {
        try {
            String cardNum = cardLinkTransaction.getTransInfo().getPan();
            String cardFirst = cardNum.substring(0, 6);
            String cardLast = cardNum.substring(cardNum.length() - 4);
            String ps = "******";
            return cardFirst + ps + cardLast;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "无法获取到银行卡号");
            return "";
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (null != newGatheringFragment) {
            newGatheringFragment.reset();
        }
        if (null != newQ2GatheringFragment) {
            newQ2GatheringFragment.reset();
        }
        super.onNewIntent(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.ivLeftIcon:
//                if (dl.getStatus() == DragLayout.Status.Close) {
//                    dl.open();
//                } else {
//                    dl.close();
//                }
//                break;
            case R.id.rlToolbarRightOwner:
                findViewById(R.id.tvSettingParams).performClick();
                break;
//            case R.id.tvSettingParams:
//                startActivity(new Intent(NewMainActivity.this, NewSettingActivity.class));
//                NewMainActivity.this.finish();
//                break;
            default:
                break;
        }
    }

    @Override
    public void onPayBtnClick(String payAmount, String tipsAmount) {
        Intent intent = new Intent();
        if (payAmount.equals("0.00")) {
            Toast.makeText(this, getResources().getString(R.string.payamount_warn), Toast.LENGTH_SHORT).show();
            return;
        }
        intent.putExtra(Constants.realAmount, Calculater.formotYuan(payAmount));
        intent.putExtra(Constants.tipsAmount, Calculater.formotYuan(tipsAmount));
        intent.setClass(NewMainActivity.this, NewMicroActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSave(String payAmount, String tipsAmount) {
        payAmounts = payAmount;
        tipAmount = tipsAmount;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == TIP_SETTING_CODE && resultCode == RESULT_OK) {
//            startActivity(new Intent(NewMainActivity.this, TipParameterSettingActivity.class));
//            finish();
//        }
    }
}
