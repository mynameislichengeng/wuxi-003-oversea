package com.wizarpos.pay.cashier.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.app.Pay2Application;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.activity.TransactionActivity;
import com.wizarpos.pay.cashier.model.TicketDef;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.ticket.impl.PublishTicketPresenter;
import com.wizarpos.pay.cashier.presenter.ticket.impl.TicketManagerImpl;
import com.wizarpos.pay.cashier.presenter.ticket.impl.WemengTicketPublisher;
import com.wizarpos.pay.cashier.presenter.transaction.impl.TransactionImpl;
import com.wizarpos.pay.cashier.presenter.transaction.impl.TransactionMarketingActivitiesPresenter;
import com.wizarpos.pay.cashier.thrid_app_controller.ThirdAppTransactionController;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppTransactionRequest;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.LastPrintHelper;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.model.ThirdGiveTicketsReq;
import com.wizarpos.pay.view.util.Constents;
import com.wizarpos.pay2.lite.R;

import java.util.ArrayList;
import java.util.List;

public class PaySuccessActivity extends TransactionActivity {
    private TextView tvCashState, tvInitAmount, tvReduceAmount, tvRealAmount, tvChargeAmount, tvCardNum, tvDiscount;
    private TransactionImpl transactionImpl;
    private String cardNum = null;
    private int transType = 0;
    private Button rePrintBt;
    private LinearLayout cardNumLL, chargeLL;
    private Intent intent;

    private PublishTicketPresenter presenter;

    private WemengTicketPublisher wemengTicketPublisher;

    private Handler mHandler = new Handler();
    private Runnable delayRunnable = null;
    /**
     * 第三方支付不需要券发行
     */
    private boolean isNeedTicket = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();
        intent.setAction(Constents.PAYSUCCESSACTION);
        intent.putExtra("value", "success");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        initView();
        initData();

        if (isInService()) { //wu
            if (isPrintting()) {
                toPrintingView();
            } else {
                waitAndBack();
            }
        } else {
            waitAndBack();
        }
    }

    /**
     * 第三方调用时 如果不需发券自动关闭
     */
    private void waitAndBack() {
        if (ThirdAppTransactionController.getInstance().isInservice()
                && !isNeedTicket() || !isNeedTicket) {
            delayRunnable = new Runnable() {
                @Override
                public void run() {
                    onTitleBackClikced();
                }
            };
            mHandler.postDelayed(delayRunnable, 3000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (fromPrintingViewSuccess(requestCode, resultCode)) { //wu
            waitAndBack();
        }
    }

    private void initData() {
        intent = getIntent();
    }

    private void initView() {
        setMainView(R.layout.cash_pay_success);
        setTitleText("支付成功");
        showTitleBack();
//		setTitleRight("券发行");
        tvCardNum = (TextView) findViewById(R.id.card_num);
        cardNumLL = (LinearLayout) findViewById(R.id.card_num_ll);
        chargeLL = (LinearLayout) findViewById(R.id.change_ll);
        tvInitAmount = (TextView) findViewById(R.id.tv_gathering);
        tvReduceAmount = (TextView) findViewById(R.id.tv_rebate);
        tvRealAmount = (TextView) findViewById(R.id.tv_recieved);
        tvChargeAmount = (TextView) findViewById(R.id.tv_charge);
        tvDiscount = (TextView) findViewById(R.id.tvDiscount);
        // tvRight = (Button) findViewById(R.id.tvRight);
        rePrintBt = (Button) findViewById(R.id.btn_cash_re_print);
        // R.id.tvRight,
        int[] btnIds = {R.id.btn_cash_back_pay, R.id.btn_cash_re_print};
        setOnClickListenerByIds(btnIds, this);
        Intent intent = getIntent();
        transType = intent.getIntExtra(Constants.TRANSACTION_TYPE, -1);
        cardNum = intent.getStringExtra(Constants.cardNo);
        switch (transType) {
            case TransactionTemsController.TRANSACTION_TYPE_BANK_CARD:// 银行卡
                setTitleText(getResources().getString(R.string.card_bank_pay_title));
                cardNumLL.setVisibility(View.GONE);
                chargeLL.setVisibility(View.GONE);
                tvInitAmount.setText(Calculater.formotFen(intent.getStringExtra("initAmount")));
                tvReduceAmount.setText(Calculater.formotFen(intent.getStringExtra("reduceAmount")));
                tvRealAmount.setText(Calculater.formotFen(intent.getStringExtra("realAmount")));
                // tvCardNum.setText(cardNum);
                break;
            case TransactionTemsController.TRANSACTION_TYPE_MEMBER_CARD:// 会员卡
                setTitleText(getResources().getString(R.string.member_card_title));
                cardNumLL.setVisibility(View.VISIBLE);
                chargeLL.setVisibility(View.GONE);
                tvInitAmount.setText(Calculater.formotFen(intent.getStringExtra("initAmount")));
                tvReduceAmount.setText(Calculater.formotFen(intent.getStringExtra("reduceAmount")));
                tvRealAmount.setText(Calculater.formotFen(intent.getStringExtra("realAmount")));
                tvCardNum.setText(cardNum);
                break;
            case TransactionTemsController.TRANSACTION_TYPE_CASH:// 现金
                setTitleText(getResources().getString(R.string.cashier_pay_title));
                cardNumLL.setVisibility(View.GONE);
                chargeLL.setVisibility(View.VISIBLE);
                tvInitAmount.setText(Calculater.formotFen(intent.getStringExtra("initAmount")));
                tvReduceAmount.setText(Calculater.formotFen(intent.getStringExtra("reduceAmount")));
                tvRealAmount.setText(Calculater.formotFen(intent.getStringExtra("realAmount")));
                tvChargeAmount.setText(Calculater.formotFen(intent.getStringExtra("changeAmount")));
                break;
            case TransactionTemsController.TRANSACTION_TYPE_WEPAY_MEMBER_CARD:// 微信会员卡
                setTitleText(getResources().getString(R.string.wepay_card_title));
                cardNumLL.setVisibility(View.VISIBLE);
                chargeLL.setVisibility(View.GONE);
                tvInitAmount.setText(Calculater.formotFen(intent.getStringExtra("initAmount")));
                tvReduceAmount.setText(Calculater.formotFen(intent.getStringExtra("reduceAmount")));
                tvRealAmount.setText(Calculater.formotFen(intent.getStringExtra("realAmount")));
                tvCardNum.setText(cardNum);
                break;
            case TransactionTemsController.TRANSACTION_TYPE_ALIPAY:// 支付宝支付
                setTitleText(getResources().getString(R.string.alipay));
                cardNumLL.setVisibility(View.GONE);
                chargeLL.setVisibility(View.GONE);
                tvInitAmount.setText(Calculater.formotFen(intent.getStringExtra("initAmount")));
                tvReduceAmount.setText(Calculater.formotFen(intent.getStringExtra("reduceAmount")));
                tvRealAmount.setText(Calculater.formotFen(intent.getStringExtra("realAmount")));
                tvRight.setVisibility(View.GONE);
                isNeedTicket = false;
                break;
            case TransactionTemsController.TRANSACTION_TYPE_WEPAY_PAY:// 微信支付
                setTitleText(getResources().getString(R.string.wepay));
                cardNumLL.setVisibility(View.GONE);
                chargeLL.setVerticalScrollbarPosition(View.GONE);
                tvInitAmount.setText(Calculater.formotFen(intent.getStringExtra("initAmount")));
                tvReduceAmount.setText(Calculater.formotFen(intent.getStringExtra("reduceAmount")));
                tvRealAmount.setText(Calculater.formotFen(intent.getStringExtra("realAmount")));
                tvRight.setVisibility(View.GONE);
                isNeedTicket = false;
                break;
            case TransactionTemsController.TRANSACTION_TYPE_TEN_PAY:// 手Q支付
                setTitleText(getResources().getString(R.string.q_wallet_title));
                cardNumLL.setVisibility(View.GONE);
                chargeLL.setVerticalScrollbarPosition(View.GONE);
                tvInitAmount.setText(Calculater.formotFen(intent.getStringExtra("initAmount")));
                tvReduceAmount.setText(Calculater.formotFen(intent.getStringExtra("reduceAmount")));
                tvRealAmount.setText(Calculater.formotFen(intent.getStringExtra("realAmount")));
                tvRight.setVisibility(View.GONE);
                isNeedTicket = false;
                break;
            case TransactionTemsController.TRANSACTION_TYPE_BAIDU_PAY:// 百度支付
                setTitleText(getResources().getString(R.string.baidu_pay));
                cardNumLL.setVisibility(View.GONE);
                chargeLL.setVerticalScrollbarPosition(View.GONE);
                tvInitAmount.setText(Calculater.formotFen(intent.getStringExtra("initAmount")));
                tvReduceAmount.setText(Calculater.formotFen(intent.getStringExtra("reduceAmount")));
                tvRealAmount.setText(Calculater.formotFen(intent.getStringExtra("realAmount")));
                tvRight.setVisibility(View.GONE);
                isNeedTicket = false;
                break;
            case TransactionTemsController.TRANSACTION_TYPE_OTHER:// 其它支付
                setTitleText(getResources().getString(R.string.other_pay));
                tvRight.setVisibility(View.GONE);
                cardNumLL.setVisibility(View.GONE);
                chargeLL.setVisibility(View.GONE);
                tvInitAmount.setText(Calculater.formotFen(intent.getStringExtra("initAmount")));
                tvReduceAmount.setText(Calculater.formotFen(intent.getStringExtra("reduceAmount")));
                tvRealAmount.setText(Calculater.formotFen(intent.getStringExtra("realAmount")));
                tvChargeAmount.setText(Calculater.formotFen(intent.getStringExtra("changeAmount")));
                isNeedTicket = false;
                break;
            case TransactionTemsController.TRANSACTION_TYPE_MIXPAY:// 組合支付
                setTitleText("组合支付");
                tvRight.setVisibility(View.GONE);
                isNeedTicket = false;
                cardNumLL.setVisibility(View.GONE);
                chargeLL.setVisibility(View.GONE);
                findViewById(R.id.llDiscount).setVisibility(View.VISIBLE);
                findViewById(R.id.llDicountLine).setVisibility(View.VISIBLE);
                tvInitAmount.setText(Calculater.formotFen(intent.getStringExtra(Constants.initAmount)));
                tvReduceAmount.setText(Calculater.formotFen(intent.getStringExtra(Constants.reduceAmount)));
                tvDiscount.setText(Calculater.formotFen(intent.getStringExtra(Constants.discountAmount)));
                tvRealAmount.setText(Calculater.formotFen(intent.getStringExtra(Constants.realAmount)));
                break;
            case TransactionTemsController.TRANSACTION_TYPE_UNION_PAY://移动支付
                setTitleText("移动支付");
                cardNumLL.setVisibility(View.GONE);
                chargeLL.setVerticalScrollbarPosition(View.GONE);
                tvInitAmount.setText(Calculater.formotFen(intent.getStringExtra("initAmount")));
                tvReduceAmount.setText(Calculater.formotFen(intent.getStringExtra("reduceAmount")));
                tvRealAmount.setText(Calculater.formotFen(intent.getStringExtra("realAmount")));
                tvRight.setVisibility(View.GONE);
                isNeedTicket = false;
                break;
        }
        if (transType == TransactionTemsController.TRANSACTION_TYPE_WEPAY_PAY
                || transType == TransactionTemsController.TRANSACTION_TYPE_BAIDU_PAY
                || transType == TransactionTemsController.TRANSACTION_TYPE_TEN_PAY
                || transType == TransactionTemsController.TRANSACTION_TYPE_ALIPAY
                || transType == TransactionTemsController.TRANSACTION_TYPE_UNION_PAY) {
            ThirdGiveTicketsReq();
        }
        if (Pay2Application.getInstance().isWemengMerchant()) {
            isNeedTicket = true;//若是微盟应用 则支持发券 Hwc 2015年12月16日09:28:26
            TicketInfo wemengTicketInfo = (TicketInfo) getIntent().getSerializableExtra(Constants.wemengTicketInfo);
            if (wemengTicketInfo != null && wemengTicketInfo.getWeiMengType().equals(TicketInfo.WEIMENT_TYPE_URL)) {
                if (wemengTicketPublisher == null) {
                    wemengTicketPublisher = new WemengTicketPublisher(this, null);
                    wemengTicketPublisher.printWemobUrl(wemengTicketInfo.getUrl());
                }
            }
        }
        if (isNeedTicket() == false) {
            tvRight.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            /*
             * case R.id.tvRight: Intent intent = getIntent(); intent.setClass(this, tvRightActivity.class); startActivity(intent); finish(); break;
             */
            case R.id.btn_cash_back_pay:
                // setResult(RESULT_CANCELED);
//			serviceSuccess(getIntent());
//			this.finish();
                if (!isNeedTicket() || !isNeedTicket) {//若不需要券发行则直接返回
                    checkBack();
                    break;
                }
                if (Constants.TRUE.equals(AppStateManager.getState(AppStateDef.isOffline))) {
                    checkBack();
                    break;
                }

                if (PaymentApplication.getInstance().isWemengMerchant()) {
                    if (transType == TransactionTemsController.TRANSACTION_TYPE_ALIPAY
                            || transType == TransactionTemsController.TRANSACTION_TYPE_WEPAY_PAY
                            || transType == TransactionTemsController.TRANSACTION_TYPE_TEN_PAY
                            || transType == TransactionTemsController.TRANSACTION_TYPE_BAIDU_PAY) {// 若是微盟第三方支付则请求券接口
                        getWeiMobTicket();
                        break;
                    }
                }

                if (PaymentApplication.getInstance().isWemengMerchant()) {// 若是威萌切不为空则跳到券发行
                    TicketInfo wemengTicketInfo = (TicketInfo) getIntent()
                            .getSerializableExtra(Constants.wemengTicketInfo);
                    if (wemengTicketInfo != null) {
                        skitToTicketPublishactivity();
                    } else {
                        checkBack();
                    }
                    break;
                }

                //发送请求查询券
                presenter = new PublishTicketPresenter(this);
                presenter.handleIntent(getIntent());
                getPublishableTicket();
                break;
            case R.id.btn_cash_re_print:// 小票补打
                rePrint();
                break;
        }
    }


    /**
     * 原有的点击“返回收银”的逻辑代码现移到该方法里面
     * 若请求券失败或者没有券的话执行该方法 Hwc
     */
    private void checkBack() {
        if (PaymentApplication.getInstance().isWemengMerchant()) {
            noteNoPublishTicket();
        } else {
            back();
        }
    }

    /**
     * @Author: Huangweicai
     * @date 2015-12-16 上午11:30:10
     * @Description:
     */
    private void getWeiMobTicket() {
        progresser.showProgress();
        wemengTicketPublisher = new WemengTicketPublisher(this, null);
        String amount = Calculater.formotFen(getIntent().getStringExtra("realAmount"));
        wemengTicketPublisher.getTicketForThirdPay(Tools.toIntMoney(amount) + "", new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                progresser.showContent();
                TicketInfo wemengTicketInfo = (TicketInfo) response.getResult();
                getIntent().putExtra(Constants.wemengTicketInfo, wemengTicketInfo);
                if (wemengTicketInfo != null) {
                    skitToTicketPublishactivity();
                } else {
                    checkBack();
                }
            }

            @Override
            public void onFaild(Response response) {
                progresser.showContent();
                UIHelper.ToastMessage(PaySuccessActivity.this, response.msg);
                checkBack();
            }
        });
    }


    /**
     * 获取可发行的券定义列表
     */
    private void getPublishableTicket() {
        progresser.showProgress();
        presenter.getPublishableTicket(new ResultListener() {

            @Override
            public void onSuccess(Response response) {
                if (response.code == -1) {
                    Toast.makeText(PaySuccessActivity.this, "没有可发行的卡券", 0).show();
                    back();
                    return;
                }
                ArrayList<TicketDef> publishableTicketDefs = (ArrayList<TicketDef>) response.result;
                Intent intent = getIntent();
                intent.putExtra(TicketPublishActivity.TAG_TICKET_INFO, publishableTicketDefs);
                intent.setClass(PaySuccessActivity.this, TicketPublishActivity.class);
                startActivity(intent);
                finish();
//				progresser.showContent();
            }

            @Override
            public void onFaild(Response response) {
                progresser.showContent();
                UIHelper.ToastMessage(PaySuccessActivity.this, response.msg);
                checkBack();
            }
        });
    }


    @Override
    protected void onTitleBackClikced() {
//		super.onTitleBackClikced();//除BUG,会先执行serviceFaild wu@[20150905]
//		serviceSuccess(getIntent());
//		this.finish();
        if (PaymentApplication.getInstance().isWemengMerchant()) {
            noteNoPublishTicket();
        } else {
            back();
        }
    }

    @Override
    public void onBackPressed() {
        if (PaymentApplication.getInstance().isWemengMerchant()) {
            noteNoPublishTicket();
        } else {
            super.onBackPressed();
        }
    }

    private void noteNoPublishTicket() {
        TicketInfo wemengTicketInfo = (TicketInfo) getIntent().getSerializableExtra(Constants.wemengTicketInfo);
        wemengTicketPublisher = new WemengTicketPublisher(this, wemengTicketInfo);
        wemengTicketPublisher.handleIntent(getIntent());
        progresser.showProgress();
        wemengTicketPublisher.noteCloseTicket(new ResultListener() {

            @Override
            public void onSuccess(Response response) {
                progresser.showContent();
                back();
            }

            @Override
            public void onFaild(Response response) {
                progresser.showContent();
//				UIHelper.ToastMessage(TicketPublishActivity.this, response.msg);
                back();
            }
        });
    }

    @Override
    protected void back() {
        serviceSuccess(getIntent());
    }

    /**
     * @Author: Admin
     * @date 2015-10-12 下午5:09:09
     * @Description: 是否需要券发行（第三方应用调用的时候做判断,本应用调用返回true）
     */
    private boolean isNeedTicket() {
        boolean isNeedTicket = true;
        if (ThirdAppTransactionController.getInstance().isInservice()) {
            ThirdAppTransactionRequest thirdRequest = ThirdAppTransactionController.getInstance().getRequestBean();
            isNeedTicket = TextUtils.isEmpty(thirdRequest.getNoTicket());
        }
        return isNeedTicket;
    }

    /**
     * @Author:Huangweicai
     * @Date:2015-7-20 上午11:38:14
     * @Reason:小票补打
     */
    private void rePrint() {
        LastPrintHelper.printLast(this);
    }

    @Override
    protected void onTitleRightClicked() {
        skitToTicketPublishactivity();
    }

    /**
     * 跳转到券发行界面并结束该界面 Hwc
     */
    private void skitToTicketPublishactivity() {
        Intent intent = getIntent();
        intent.setClass(this, TicketPublishActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (delayRunnable != null) {
            mHandler.removeCallbacks(delayRunnable);
            delayRunnable = null;
        }
    }

    /**
     * 营销二期 @yaosong [20151218]
     */
    private void ThirdGiveTicketsReq() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra(Constants.tranLogId))) {
            TransactionMarketingActivitiesPresenter presenter = new TransactionMarketingActivitiesPresenter();
            ThirdGiveTicketsReq req = new ThirdGiveTicketsReq();

            req.setTranLogId(getIntent().getStringExtra(Constants.tranLogId));
            presenter.thirdGiveTickets(req, new ResultListener() {

                @Override
                public void onSuccess(Response response) {
                    List<com.wizarpos.pay.cashier.model.TicketInfo> publishTicketsBean = (List<TicketInfo>) response.getResult();
//					Log.e("", "===============size is :" + publishTicketsBean.size() + "========.");
                    // printThirdGiveTickets(publishTicketsBean,false);//打印普通券
                    printThirdWxTickets(publishTicketsBean);// 打印微信券
                }

                @Override
                public void onFaild(Response response) {

                }
            });
        }
    }

    /**
     * @param publishTicketsBean
     * @author yaosong [21051218]
     */
    private void printThirdWxTickets(List<TicketInfo> publishTicketsBean) {
        final TicketManagerImpl manager = new TicketManagerImpl(this);
        manager.filterWepayQRTicket(publishTicketsBean, new ResultListener() {// 过滤微信ticketQrcode,根据ticketQrcode请求图片
            // wu@[20150818]
            @Override
            public void onSuccess(Response response) {
                final List<com.wizarpos.pay.cashier.model.TicketInfo> publishTicketsBean = (List<com.wizarpos.pay.cashier.model.TicketInfo>) response.result;
                if (publishTicketsBean.size() > 0) {
                    manager.printThirdGiveTickets(publishTicketsBean, true);// [打印券]
                }
            }

            @Override
            public void onFaild(Response response) {
                if (null != response && !TextUtils.isEmpty(response.getMsg())) {
                    UIHelper.ToastMessage(PaySuccessActivity.this, response.getMsg());
                }
            }
        });
    }

}
