//package com.wizarpos.pay.ui.newui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ExpandableListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.ui.dialog.DialogHelper;
//import com.ui.dialog.NoticeDialogFragment;
//import com.wizarpos.atool.tool.Tools;
//import com.wizarpos.base.net.NetRequest;
//import com.wizarpos.base.net.Response;
//import com.wizarpos.base.net.ResponseListener;
//import com.wizarpos.device.printer.html.WebPrintHelper;
//import com.wizarpos.hspos.api.TransInfo;
//import com.wizarpos.pay.cardlink.QueryAnyProxy;
//import com.wizarpos.pay.common.Constants;
//import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
//import com.wizarpos.pay.common.utils.Calculater;
//import com.wizarpos.pay.db.AppConfigDef;
//import com.wizarpos.pay.db.AppConfigHelper;
//import com.wizarpos.pay.fragment.RefundDialogFragment;
//import com.wizarpos.pay.manage.activity.InputPassWordActivity;
//import com.wizarpos.pay.model.DailyDetailResp;
//import com.wizarpos.pay.model.SendTransInfo;
//import com.wizarpos.pay.model.TransDetailResp;
//import com.wizarpos.pay.recode.hisotory.activitylist.bean.http.ResponseTranRecoderListBean;
//import com.wizarpos.pay.statistics.presenter.StatisticsPresenter;
//import com.wizarpos.pay.ui.newui.adapter.TranlogDetailAdapter;
//import com.wizarpos.pay.ui.newui.fragment.QueryFragment;
//import com.wizarpos.pay2.lite.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class NewTranlogActivity extends NewBaseTranlogActivity implements QueryFragment.QueryFragmentListener, View.OnClickListener, RefundDialogFragment.OnSaveListener {
//    private ExpandableListView expandableListView;
//    private TranlogDetailAdapter adapter;
//    private DrawerLayout dlMain;
//    private String alreadyAmount;
//
//    //右侧抽屉相关数据
//    private QueryFragment queryFragment;
//
//    private StatisticsPresenter statisticsPresenter;
//    private String TODAY = "0";
//    private String YESTODAY = "1";
//    private String THISWEEK = "2";
//    private String RECHARGEON = "1";
//    private String UNRECHARGEON = "0";
//    private String DEFAULTNUM = "0";
//    private QueryAnyProxy cardLinkProxy;
////    private static String TAG = NewTranlogActivityBackup.class.getSimpleName();
//    private static final int REQUEST_PAY_CANCEL = 2001;
//    private static final int REQUEST_INPUT_PASSWORD = 2002;
//    private DailyDetailResp dailyDetailResp;
//    private List<DailyDetailResp> respList = new ArrayList<>();
//    private List<DailyDetailResp> relist = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initView();
//        getData(THISWEEK, UNRECHARGEON, null, "", DEFAULTNUM, "", "");
////        getDataNew(THISWEEK, UNRECHARGEON, null, "", DEFAULTNUM, "", "");
//        initDrawerLayout();
//    }
//
//    private void initView() {
////        setMainView(R.layout.activity_tranlog_detail_new);
//        statisticsPresenter = new StatisticsPresenter(this);
//        cardLinkProxy = new QueryAnyProxy(this);
//        setTitleTxt(getResources().getString(R.string.trans_detail));
////        setTitleRightImage(R.drawable.ic_nav_search);
//        adapter = new TranlogDetailAdapter(NewTranlogActivity.this);
//
//        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
//        expandableListView.setVisibility(View.VISIBLE);
//        expandableListView.setAdapter(adapter);
//        expandableListView.setGroupIndicator(null); // 去掉默认带的箭头
//        expandableListView.setSelection(0);// 设置默认选中项
//        expandableListView.setOnGroupClickListener(onGroupClickListener);
//        adapter.setOnTranLogDetialListener(new TranlogDetailAdapter.OnTranLogDetialListener() {
//            @Override
//            public void onPrint(final DailyDetailResp resp) {
//                if (Constants.SC_700_BANK_CARD_PAY.equals(resp.getTransType())) {
//                    // TODO: 2016/4/27 调用收单查询数据
////                    Log.d("BANKCARD", "onPrint: "+resp.getBank_info().getMid());
////                    startActivity(NewQueryCardLinkActivity.getStartIntent(NewTranlogActivity.this,resp));
//                    SendTransInfo sendTransInfo = JSON.parseObject(resp.getBank_info(), SendTransInfo.class);
//                    TransInfo transInfo = new TransInfo();
//                    if (sendTransInfo != null) {
//                        transInfo.setOldTrace(sendTransInfo.getTrace());
//                    }
//                    cardLinkProxy.queryAnyTrans(transInfo);
////                    statisticsPresenter.printDetial(resp);
//                } else {
//                    String masterTranlogId = Tools.deleteMidTranLog(resp.getMasterTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
//                    getDetailData("", "", "", "", DEFAULTNUM, "", masterTranlogId);
//                }
//            }
//
//            @Override
//            public void onRevoke(DailyDetailResp resp) {
//                dailyDetailResp = resp;
//                alreadyAmount = String.valueOf(Integer.parseInt(resp.getSingleAmount()) - Integer.parseInt(resp.getRefundAmount()));
//                toInputPasswordActivity(REQUEST_INPUT_PASSWORD);
//            }
//        });
//    }
//
//    private void rePrintCustomer() {
//        for (final DailyDetailResp detailResp : respList) {
//            if (getString(R.string.pay_tag).equals(detailResp.getTransKind())) {
//                detailResp.setSingleAmount(detailResp.getTrasnAmount());
//                detailResp.setTransName(Constants.TRAN_TYPE.get(detailResp.getTransType()));
//                detailResp.setPayTime(detailResp.getPayTime());
//                detailResp.setMasterTranLogId(detailResp.getMasterTranLogId());
//                detailResp.setTranlogId(detailResp.getTranLogId());
//                detailResp.setRefundAmount(detailResp.getRefundAmount());
//                detailResp.setOptName(detailResp.getOptName());
//                detailResp.setTipAmount(detailResp.getTipAmount());
//                detailResp.setThirdTradeNo(detailResp.getThirdTradeNo());
//                detailResp.setCnyAmount(detailResp.getCnyAmount());
//                if (!TextUtils.isEmpty(detailResp.getThirdExtName())) {
//                    detailResp.setThirdExtName(detailResp.getThirdExtName());
//                }
//                if (!TextUtils.isEmpty(detailResp.getThirdExtId())) {
//                    detailResp.setThirdExtId(detailResp.getThirdExtId());
//                }
//                statisticsPresenter.reprintCustomerSale(detailResp);
//            }
//            if (getString(R.string.refund_tag).equals(detailResp.getTransKind())) {
//                detailResp.setSingleAmount(detailResp.getTrasnAmount());
//                detailResp.setTransName(Constants.TRAN_TYPE.get(detailResp.getTransType()));
//                detailResp.setPayTime(detailResp.getPayTime());
//                detailResp.setMasterTranLogId(detailResp.getMasterTranLogId());
//                detailResp.setTranlogId(detailResp.getTranLogId());
//                detailResp.setRefundAmount(detailResp.getRefundAmount());
//                detailResp.setOptName(detailResp.getOptName());
//                detailResp.setTipAmount(detailResp.getTipAmount());
//                detailResp.setThirdTradeNo(detailResp.getThirdTradeNo());
//                detailResp.setCnyAmount(detailResp.getCnyAmount());
//                detailResp.setCnyAmount(detailResp.getCnyAmount());
//                if (!TextUtils.isEmpty(detailResp.getThirdExtName())) {
//                    detailResp.setThirdExtName(detailResp.getThirdExtName());
//                }
//                if (!TextUtils.isEmpty(detailResp.getThirdExtId())) {
//                    detailResp.setThirdExtId(detailResp.getThirdExtId());
//                }
//                statisticsPresenter.reprintCustomerRefund(detailResp);
//            }
//        }
//        respList.clear();
//    }
//
//    private void rePrintMerchant() {
//        for (final DailyDetailResp detailResp : respList) {
//            if (getString(R.string.pay_tag).equals(detailResp.getTransKind())) {
//                detailResp.setSingleAmount(detailResp.getTrasnAmount());
//                detailResp.setTransName(Constants.TRAN_TYPE.get(detailResp.getTransType()));
//                detailResp.setPayTime(detailResp.getPayTime());
//                detailResp.setMasterTranLogId(detailResp.getMasterTranLogId());
//                detailResp.setTranlogId(detailResp.getTranLogId());
//                detailResp.setRefundAmount(detailResp.getRefundAmount());
//                detailResp.setOptName(detailResp.getOptName());
//                detailResp.setTipAmount(detailResp.getTipAmount());
//                detailResp.setThirdTradeNo(detailResp.getThirdTradeNo());
//                detailResp.setCnyAmount(detailResp.getCnyAmount());
//                if (!TextUtils.isEmpty(detailResp.getThirdExtName())) {
//                    detailResp.setThirdExtName(detailResp.getThirdExtName());
//                }
//                if (!TextUtils.isEmpty(detailResp.getThirdExtId())) {
//                    detailResp.setThirdExtId(detailResp.getThirdExtId());
//                }
//                statisticsPresenter.reprintMerchantSale(detailResp);
//            }
//            if (getString(R.string.refund_tag).equals(detailResp.getTransKind())) {
//                detailResp.setSingleAmount(detailResp.getTrasnAmount());
//                detailResp.setTransName(Constants.TRAN_TYPE.get(detailResp.getTransType()));
//                detailResp.setPayTime(detailResp.getPayTime());
//                detailResp.setMasterTranLogId(detailResp.getMasterTranLogId());
//                detailResp.setTranlogId(detailResp.getTranLogId());
//                detailResp.setRefundAmount(detailResp.getRefundAmount());
//                detailResp.setOptName(detailResp.getOptName());
//                detailResp.setTipAmount(detailResp.getTipAmount());
//                detailResp.setThirdTradeNo(detailResp.getThirdTradeNo());
//                detailResp.setCnyAmount(detailResp.getCnyAmount());
//                if (!TextUtils.isEmpty(detailResp.getThirdExtName())) {
//                    detailResp.setThirdExtName(detailResp.getThirdExtName());
//                }
//                if (!TextUtils.isEmpty(detailResp.getThirdExtId())) {
//                    detailResp.setThirdExtId(detailResp.getThirdExtId());
//                }
//                statisticsPresenter.reprintMerchantRefund(detailResp);
//            }
//        }
//        respList.clear();
//    }
//
//    public void toInputPasswordActivity(int requestCode) {
//        Intent intent = new Intent(NewTranlogActivity.this, InputPassWordActivity.class);
//        this.startActivityForResult(intent, requestCode);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_INPUT_PASSWORD) {
//                RefundDialogFragment refundDialogFragment = RefundDialogFragment.newInstance(getString(R.string.refund), alreadyAmount);
//                refundDialogFragment.show(getFragmentManager(), null);
//            } else if (requestCode == REQUEST_PAY_CANCEL) {
//                getData(THISWEEK, UNRECHARGEON, null, "", DEFAULTNUM, "", "");
//                printRefund();
//            }
//
//        }
//    }
//
//    private void printRefund() {
//        int printNumber = 1;
//        if (!TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.print_number))) {
//            printNumber = Integer.parseInt(AppConfigHelper.getConfig(AppConfigDef.print_number));
//        }
//        final PrintServiceControllerProxy controller = new PrintServiceControllerProxy(this);
//        switch (printNumber) {
//            case 1:
//                if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
//                    WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT));
////                    PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT)));
//                } else {
//                    controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT));
//                }
//                break;
//            case 2:
//                if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
//                    WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT));
////                    PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT)));
//                } else {
//                    controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT));
//                }
//                final NoticeDialogFragment dialogFragment = NoticeDialogFragment.newInstance("INFORMATION", "Customer Copy?", "YES", "NO");
//                dialogFragment.setListener(new DialogHelper.DialogCallbackAndNo() {
//                    @Override
//                    public void callback() {
//                        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
//                            WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT));
////                            PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT)));
//                        } else {
//                            controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT));
//                        }
//                    }
//
//                    @Override
//                    public void callbackNo() {
//                        dialogFragment.dismiss();
//                    }
//                });
//                dialogFragment.show(getSupportFragmentManager(), "SimpleMsgDialogFragment");
//                break;
//            case 3:
//                if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
//                    WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT));
////                    PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT)));
//                } else {
//                    controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT));
//                }
//                final NoticeDialogFragment fragmentDialog = NoticeDialogFragment.newInstance("INFORMATION", "Customer Copy?", "YES", "NO");
//                fragmentDialog.setListener(new DialogHelper.DialogCallbackAndNo() {
//                    @Override
//                    public void callback() {
//                        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
//                            WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT));
////                            PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT)));
//                        } else {
//                            controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT));
//                        }
//                        final NoticeDialogFragment dialog = NoticeDialogFragment.newInstance("INFORMATION", "Customer Copy?", "YES", "NO");
//                        dialog.setListener(new DialogHelper.DialogCallbackAndNo() {
//                            @Override
//                            public void callback() {
//                                if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
//                                    WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT));
////                                    PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT)));
//                                } else {
//                                    controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT));
//                                }
//                            }
//
//                            @Override
//                            public void callbackNo() {
//                                dialog.dismiss();
//                            }
//                        });
//                        dialog.show(getSupportFragmentManager(), "SimpleMsgDialogFragment2");
//                    }
//
//                    @Override
//                    public void callbackNo() {
//                        fragmentDialog.dismiss();
//                    }
//                });
//                fragmentDialog.show(getSupportFragmentManager(), "SimpleMsgDialogFragment1");
//                break;
//        }
//    }
//
//    /**
//     * 右侧查询相关控件 Song
//     * 封装为Fragment
//     */
//    private void initDrawerLayout() {
//        dlMain = (DrawerLayout) findViewById(R.id.dl_main);
//        dlMain.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//            }
//
//        });
//        queryFragment = QueryFragment.newInstance();
//        if (!queryFragment.isAdded()) {
//            getSupportFragmentManager().beginTransaction().add(R.id.inRight, queryFragment, "query").commit();
//        }
//    }
//
//    @Override
//    public void onQuery(String timeRange, String tranType, String startDate, String endDate, String tranlogId) {
//        getData(timeRange, UNRECHARGEON, tranType, startDate, DEFAULTNUM, endDate, tranlogId);
//        if (dlMain.isDrawerOpen(Gravity.RIGHT)) {
//            dlMain.closeDrawer(Gravity.RIGHT);
//        }
//    }
//
//    private void setTitleTxt(String title) {
//        Toolbar toolbarOwner = (Toolbar) findViewById(R.id.toolbarOwner);
//        if (toolbarOwner != null) {
//            toolbarOwner.setVisibility(View.VISIBLE);
//            setSupportActionBar(toolbarOwner);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////            toolbarOwner.setNavigationIcon(R.drawable.back);
//        }
//        TextView tvTitleOwner = (TextView) findViewById(R.id.tvTitleOwner);
//        RelativeLayout rlToolbarRightOwner = (RelativeLayout) findViewById(R.id.rlToolbarRightOwner);
//        if (tvTitleOwner != null) {
//            tvTitleOwner.setText(title);
//            tvTitleOwner.setVisibility(View.VISIBLE);
//        }
//        TextView tvSettingParams = (TextView) findViewById(R.id.tvSettingParams);
//        if (tvSettingParams != null && rlToolbarRightOwner != null) {
//            rlToolbarRightOwner.setVisibility(View.VISIBLE);
//            rlToolbarRightOwner.setOnClickListener(this);
//            tvSettingParams.setBackgroundResource(R.drawable.ic_nav_search);
//            tvSettingParams.setOnClickListener(this);
//        }
//  /*      ImageView ivLeftIcon = (ImageView) findViewById(R.id.ivLeftIcon);
//        if (ivLeftIcon != null) {
//            ivLeftIcon.setVisibility(View.VISIBLE);
//            ivLeftIcon.setImageResource(R.id.);
//            ivLeftIcon.setOnClickListener(this);
//        }*/
//    }
//
//    /**
//     * 根据主流水号查询相关的所有订单并打印小票
//     */
//    private void getDetailData(String timeRange, String rechargeOn, String transType, String startTime, String pageNumber, String endTime, String tranlogId) {
//        progresser.showProgress();
//        statisticsPresenter.getDetailQuery(timeRange, rechargeOn, transType, startTime, pageNumber, endTime, tranlogId, Constants.TRANLOG_DETAIL_TAG, new ResponseListener() {
//            @Override
//            public void onSuccess(Response response) {
//                progresser.showContent();
//                List<TransDetailResp> list = (List<TransDetailResp>) response.getResult();
//                if (list.size() > 0 && list != null) {
//                    for (int i = 0; i < list.size(); i++) {
//                        relist = list.get(i).getTransDetail();
//                        respList.addAll(relist);
//                    }
//                }
//                if (respList.size() > 0) {
//                    NoticeDialogFragment dialogFragment = NoticeDialogFragment.newInstance("REPRINT", "Make your choice", "Customer Copy", "Merchant Copy");
//                    dialogFragment.setListener(new DialogHelper.DialogCallbackAndNo() {
//                        @Override
//                        public void callback() {
//                            rePrintCustomer();
//                        }
//
//                        @Override
//                        public void callbackNo() {
//                            rePrintMerchant();
//                        }
//                    });
//                    dialogFragment.show(getSupportFragmentManager(), "SimpleMsgDialogFragment");
//                }
//            }
//
//            @Override
//            public void onFaild(Response response) {
//                progresser.showContent();
//                progresser.showError(response.getMsg().toString(), true);
//            }
//        });
//    }
//
//    /**
//     * 查询方法
//     *
//     * @param timeRange  //0 今天 1 昨天 2本周 3上周 4本月 5上月 6时间段 必须传
//     * @param rechargeOn //0不含充值 1含充值
//     * @param transType
//     * @param startTime
//     * @param pageNumber //必须传，查询几天数据
//     * @param endTime
//     * @param tranlogId  //流水号
//     */
//    private void getData(String timeRange, String rechargeOn, String transType, String startTime, String pageNumber, String endTime, String tranlogId) {
//        progresser.showProgress();
//        statisticsPresenter.getQueryDetail(timeRange, rechargeOn, transType, startTime, pageNumber, endTime, tranlogId, Constants.TRANLOG_DETAIL_TAG, new ResponseListener() {
//            @Override
//            public void onSuccess(Response response) {
//                progresser.showContent();
//                List<TransDetailResp> list = (List<TransDetailResp>) response.getResult();
//                adapter.setDataChanged(list);
//                // 遍历所有group,将所有项设置成默认展开
//                int groupCount = expandableListView.getCount();
//                for (int i = 0; i < groupCount; i++) {
//                    expandableListView.expandGroup(i);
//                }
//                if (groupCount == 0) {
//                    progresser.showContent();
//                    progresser.showError(getResources().getString(R.string.no_data), R.drawable.nodata_new, false);
//                }
//            }
//
//            @Override
//            public void onFaild(Response response) {
//                progresser.showContent();
//                progresser.showError(response.getMsg().toString(), true);
//            }
//        });
//    }
//
//
//
//
//    @Override
//    public void onBackPressed() {
//        startActivity(NewMainActivity.getStartIntent(this));
//        NetRequest.getInstance().cancelFirst(Constants.TRANLOG_DETAIL_TAG);
//        super.onBackPressed();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tvSettingParams:
//                if (!dlMain.isDrawerOpen(Gravity.RIGHT)) {
//                    dlMain.openDrawer(Gravity.RIGHT);
//                } else {
//                    dlMain.closeDrawer(Gravity.RIGHT);
//                }
//                break;
//            case R.id.rlToolbarRightOwner:
//                findViewById(R.id.tvSettingParams).performClick();
//                break;
//            case R.id.ivLeftIcon:
//                //back
//                onBackPressed();
//                break;
//            default:
//                break;
//        }
//    }
//
//    ExpandableListView.OnGroupClickListener onGroupClickListener = new ExpandableListView.OnGroupClickListener() {
//        @Override
//        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//            return true;
//        }
//    };
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home || id == R.drawable.back) {
//            onBackPressed();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onSave(String amount) {
//        startActivityForResult(VoidTransActivity.getStartIntent(NewTranlogActivity.this, dailyDetailResp, Calculater.formotYuan(amount)), REQUEST_PAY_CANCEL);
//    }
//}
