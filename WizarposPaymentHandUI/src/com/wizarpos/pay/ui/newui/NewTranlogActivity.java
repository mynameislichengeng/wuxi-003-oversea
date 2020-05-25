package com.wizarpos.pay.ui.newui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lc.baseui.tools.keyBoradManager;
import com.lc.librefreshlistview.linear.SimpleLinearRecycleView;
import com.lc.librefreshlistview.listener.RefreshEventListener;
import com.ui.dialog.DialogHelper;
import com.ui.dialog.NoticeDialogFragment;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.device.printer.html.WebPrintHelper;
import com.wizarpos.hspos.api.TransInfo;
import com.wizarpos.pay.cardlink.QueryAnyProxy;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.fragment.RefundDialogFragment;
import com.wizarpos.pay.manage.activity.InputPassWordActivity;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.SendTransInfo;
import com.wizarpos.pay.recode.hisotory.activitylist.activity.TranLogDetailActivity;
import com.wizarpos.pay.recode.hisotory.activitylist.adapter.TranRecoderAdapter;
import com.wizarpos.pay.recode.hisotory.activitylist.bean.TranRecordStatusParam;
import com.wizarpos.pay.recode.hisotory.activitylist.bean.http.RespTranRecItemByDayPageBean;
import com.wizarpos.pay.recode.hisotory.activitylist.bean.http.ResponseTranRecoderListBean;
import com.wizarpos.pay.recode.hisotory.activitylist.callback.OnTranLogDetialListener;
import com.wizarpos.pay.recode.constants.TransRecordConstants;
import com.wizarpos.pay.recode.hisotory.activitylist.data.TranRecordStatusDataUtil;
import com.wizarpos.pay.recode.hisotory.activitylist.data.TransRecordDataUtil;
import com.wizarpos.pay.recode.hisotory.activitylist.widget.TransRecordDialog;
import com.wizarpos.pay.statistics.presenter.StatisticsPresenter;
import com.wizarpos.pay.ui.newui.fragment.QueryFragment;
import com.wizarpos.pay2.lite.R;
import com.wizarpos.recode.activi.http.TranRecordHttpManager;
import com.wizarpos.recode.print.data.SettingPrinterModeManager;

import java.util.ArrayList;
import java.util.List;

public class NewTranlogActivity extends NewBaseTranlogActivity implements TransRecordConstants, QueryFragment.QueryFragmentListener, View.OnClickListener, RefundDialogFragment.OnSaveListener {
    private static String TAG = NewTranlogActivity.class.getSimpleName();
    private DrawerLayout dlMain;

    private SimpleLinearRecycleView simpleLinearRecycleView;
    private TranRecoderAdapter adapter;

    //右侧抽屉相关数据
    private QueryFragment queryFragment;

    private StatisticsPresenter statisticsPresenter;

    private QueryAnyProxy cardLinkProxy;

    private static final int REQUEST_PAY_CANCEL = 2001;
    private static final int REQUEST_INPUT_PASSWORD = 2002;
    private DailyDetailResp dailyDetailResp;

    private List<DailyDetailResp> respList = new ArrayList<>();

    private List<DailyDetailResp> itemList;//列表list

    private TranRecordStatusParam tranRecordStatusParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        getDataNew();
        initDrawerLayout();

    }

    private void initData() {
        tranRecordStatusParam = TranRecordStatusDataUtil.createDefault();
        adapter = new TranRecoderAdapter(NewTranlogActivity.this);
        itemList = new ArrayList<>();
        adapter.setLists(itemList);

        adapter.setOnTranLogDetialListener(new OnTranLogDetialListener() {
            @Override
            public void onPrint(final DailyDetailResp resp) {
                if (Constants.SC_700_BANK_CARD_PAY.equals(resp.getTransType())) {
                    // TODO: 2016/4/27 调用收单查询数据

                    SendTransInfo sendTransInfo = JSON.parseObject(resp.getBank_info(), SendTransInfo.class);
                    TransInfo transInfo = new TransInfo();
                    if (sendTransInfo != null) {
                        transInfo.setOldTrace(sendTransInfo.getTrace());
                    }
                    cardLinkProxy.queryAnyTrans(transInfo);
                } else {
//                    String tranlogId = Tools.deleteMidTranLog(resp.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
                    getDetailData(resp.getTranLogId());
                }
            }

            @Override
            public void onRevoke(DailyDetailResp resp) {
                operateRefundOnclick(resp);
            }

            @Override
            public void onDetail(DailyDetailResp resp) {
                operateDetailActivity(resp);
            }
        });
        //这里是，更新一下商户列表，商户列表是用在，右边抽屉中的
        TranRecordHttpManager.doRefunmidInfo(this);
    }

    private void initView() {
//        setMainView(R.layout.activity_tranlog_detail_new);
        statisticsPresenter = new StatisticsPresenter(this);
        cardLinkProxy = new QueryAnyProxy(this);
        setTitleTxt(getResources().getString(R.string.trans_detail));
        simpleLinearRecycleView = findViewById(R.id.rel_list);

        simpleLinearRecycleView.setRecycleViewAdapter(adapter);
//        simpleLinearRecycleView.settingEnablePullToRefresh(false);//关闭下拉刷新
        simpleLinearRecycleView.setRefreshEventListener(new RefreshEventListener() {
            @Override
            public void onTopDownRefresh(boolean isManual) {
                //下拉刷新
                operateOnPullDownRefresh();
            }

            @Override
            public void onBottomLoadMore(boolean isSilence) {
                //上拉加载
                operateOnPullUpLoad();
            }

            @Override
            public void onCompleteRefresh() {

            }
        });

    }

    /**
     * 右侧查询相关控件 Song
     * 封装为Fragment
     */
    private void initDrawerLayout() {
        dlMain = (DrawerLayout) findViewById(R.id.dl_main);
        dlMain.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("activity", "onDrawerClosed");
                //关闭键盘
                keyBoradManager.closeKeyBorad(NewTranlogActivity.this);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("activity", "onDrawerOpened");
            }

        });
        queryFragment = QueryFragment.newInstance();
        if (!queryFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().add(R.id.inRight, queryFragment, "query").commit();
        }
    }

    private void setTitleTxt(String title) {
        Toolbar toolbarOwner = (Toolbar) findViewById(R.id.toolbarOwner);
        if (toolbarOwner != null) {
            toolbarOwner.setVisibility(View.VISIBLE);
            setSupportActionBar(toolbarOwner);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            toolbarOwner.setNavigationIcon(R.drawable.back);
        }
        TextView tvTitleOwner = (TextView) findViewById(R.id.tvTitleOwner);
        RelativeLayout rlToolbarRightOwner = (RelativeLayout) findViewById(R.id.rlToolbarRightOwner);
        if (tvTitleOwner != null) {
            tvTitleOwner.setText(title);
            tvTitleOwner.setVisibility(View.VISIBLE);
        }
        TextView tvSettingParams = (TextView) findViewById(R.id.tvSettingParams);
        if (tvSettingParams != null && rlToolbarRightOwner != null) {
            rlToolbarRightOwner.setVisibility(View.VISIBLE);
            rlToolbarRightOwner.setOnClickListener(this);
            tvSettingParams.setBackgroundResource(R.drawable.ic_nav_search);
            tvSettingParams.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSettingParams:
                if (!dlMain.isDrawerOpen(Gravity.RIGHT)) {
                    dlMain.openDrawer(Gravity.RIGHT);
                } else {
                    dlMain.closeDrawer(Gravity.RIGHT);
                }
                break;
            case R.id.rlToolbarRightOwner:
                findViewById(R.id.tvSettingParams).performClick();
                break;
            case R.id.ivLeftIcon:
                //back
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(NewMainActivity.getStartIntent(this));
        NetRequest.getInstance().cancelFirst(Constants.TRANLOG_DETAIL_TAG);
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home || id == R.drawable.back) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_INPUT_PASSWORD) {
                RefundDialogFragment refundDialogFragment = RefundDialogFragment.newInstance(getString(R.string.refund), dailyDetailResp);
                refundDialogFragment.setOnSaveListener(this);
                refundDialogFragment.setContext(this);
                refundDialogFragment.show(getFragmentManager(), null);
            } else if (requestCode == REQUEST_PAY_CANCEL) {
//                getDataNew(THISWEEK, UNRECHARGEON, null, "", DEFAULTNUM, "", "");
                operatePayForCancle();
            }

        }
    }

    private void setLayoutRefreshOnComplete() {
        simpleLinearRecycleView.stopRefresh();
    }

    private void setLayoutListView(List<DailyDetailResp> mList) {
        adapter.addDataChanged(mList);
    }

    private void setLayoutListViewEmpty() {
        adapter.clearList();
    }

    private void setLayoutEmptyView() {
        progresser.showError(getResources().getString(R.string.no_data), R.drawable.nodata_new, false);

    }


    /**
     * 请求数据成功后，需要更新的状态数据
     *
     * @param isNextPage
     * @param pageNum
     */
    private void setDataActivityStatusHttpOnSuccess(boolean isNextPage, int pageNum) {
        tranRecordStatusParam.setNextPage(isNextPage);
        tranRecordStatusParam.setPageNo(pageNum);
    }

    /**
     * 下拉刷新
     */
    private void operateOnPullDownRefresh() {
        tranRecordStatusParam = TranRecordStatusDataUtil.createDefault();
        setLayoutListViewEmpty();
        getDataNew();
    }

    /**
     * 上拉加载更多
     */
    private void operateOnPullUpLoad() {
        if (tranRecordStatusParam.isNextPage()) {
            getDataNew();
        } else {
            Toast.makeText(this, R.string.refresh_last_page, Toast.LENGTH_SHORT).show();
            setLayoutRefreshOnComplete();
        }
    }

    private void operateRefundOnclick(DailyDetailResp resp) {
        dailyDetailResp = resp;
        toInputPasswordActivity(REQUEST_INPUT_PASSWORD);

    }

    private void operateDetailActivity(DailyDetailResp resp) {
        TranLogDetailActivity.startOpenActivity(this, resp);
    }

    /**
     * 操作listView
     *
     * @param resultBeanX
     */
    private void operateListView(ResponseTranRecoderListBean.ResultBeanX resultBeanX) {

        List<ResponseTranRecoderListBean.ResultBeanX.ResultBean> mList = resultBeanX.getResult();
        if (mList != null && mList.size() > 0) {
            List<DailyDetailResp> tempItem = transProtocol(resultBeanX);
            setLayoutListView(tempItem);
        } else {
            setLayoutListViewEmpty();
            setLayoutEmptyView();
        }
    }

    private List<DailyDetailResp> transProtocol(ResponseTranRecoderListBean.ResultBeanX resultBeanX) {
        List<ResponseTranRecoderListBean.ResultBeanX.ResultBean> mList = resultBeanX.getResult();
        List<DailyDetailResp> tempItem = TransRecordDataUtil.create(mList);
        return tempItem;
    }

    /**
     * 请求成功的回调
     *
     * @param responseJson
     */
    private void operateOnSuccess(String responseJson) {
        //刷新完成
        setLayoutRefreshOnComplete();
        RespTranRecItemByDayPageBean re = JSON.parseObject(responseJson, RespTranRecItemByDayPageBean.class);
        ResponseTranRecoderListBean responseTranRecoderListBean = TransRecordDataUtil.createBeanAdapterFromDay(re);

        if (responseTranRecoderListBean != null && responseTranRecoderListBean.getResult() != null) {
            //
            ResponseTranRecoderListBean.ResultBeanX resultBeanX = responseTranRecoderListBean.getResult();
            //
            setDataActivityStatusHttpOnSuccess(resultBeanX.isHasNext(), resultBeanX.getPageNo());
            //
            operateListView(resultBeanX);
        } else {
            Response response1 = new Response();
            response1.setMsg("请求返回数据error");
            operateOnFaild(response1);
        }
    }

    /**
     * 请求错误的回调
     *
     * @param response
     */
    private void operateOnFaild(Response response) {
        //刷新完成
        setLayoutRefreshOnComplete();
        progresser.showError(response.getMsg().toString(), true);
    }

    /**
     * 侧边栏查询回调
     *
     * @param tranType
     * @param timeRange
     * @param startDate
     * @param endDate
     * @param tranlogId
     */
    private void operateFragmentOnQuery(String tranType, String timeRange, String startDate, String endDate, String tranlogId, String invoiceNum) {
        //关闭键盘
//        keyBoradManager.closeKeyBorad(this);
        //设置状态值
        tranRecordStatusParam.setPageNo(0);
        tranRecordStatusParam.setNextPage(true);
        tranRecordStatusParam.setTimeRange(timeRange);
        tranRecordStatusParam.setTransType(tranType);
        tranRecordStatusParam.setStartTime(startDate);
        tranRecordStatusParam.setEndTime(endDate);
        tranRecordStatusParam.setTranLogId(tranlogId);
        tranRecordStatusParam.setInvoiceNum(invoiceNum);

        //清空数据
        setLayoutListViewEmpty();

        //隐藏侧边栏
        if (dlMain.isDrawerOpen(Gravity.RIGHT)) {
            dlMain.closeDrawer(Gravity.RIGHT);
        }

        // 请求数据
        getDataNew();

    }


    private void operatePayForCancle() {
        //清空状态数据
        tranRecordStatusParam = TranRecordStatusDataUtil.createDefault();
        //清空数据
        setLayoutListViewEmpty();
        //
        getDataNew();
        printRefund();
    }


    private void getDataNew() {
        progresser.showProgress();
        doPostTranRecordList();
    }

    /**
     * 请求列表数据
     */
    private void doPostTranRecordList() {
        //时间类型
        String timeRange = tranRecordStatusParam.getTimeRange();
        //查看类型
        String transType = tranRecordStatusParam.getTransType();
        //如果是自定义时间,则有开始时间
        String startTime = tranRecordStatusParam.getStartTime();
        //如果是自定义时间,则有结束时间
        String endTime = tranRecordStatusParam.getEndTime();
        //TranLogId
        String tranlogId = tranRecordStatusParam.getTranLogId();
        //invoiceId
        String invoiceNum = tranRecordStatusParam.getInvoiceNum();
        //第几页
        String pageNumber = String.valueOf(tranRecordStatusParam.getPageNo() + 1);

        String rechargeOn = TransRecordConstants.UNRECHARGEON;
        int pageSize = TransRecordConstants.PAGE_SIZE;



        TranRecordHttpManager.getQueryDetailNew(rechargeOn, pageSize, pageNumber, timeRange, transType, startTime, endTime, tranlogId, invoiceNum, Constants.TRANLOG_DETAIL_TAG, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                progresser.showContent();
                operateOnSuccess(response.getResult().toString());
            }

            @Override
            public void onFaild(Response response) {
                progresser.showContent();
                operateOnFaild(response);
            }
        });

    }


    /**
     * 根据主流水号查询相关的所有订单并打印小票
     */
    private void getDetailData(String tranlogId) {
        progresser.showProgress();
        int pageSize = TransRecordConstants.ALL_PAGE_SIZE;
        String pageNum = "1";

        TranRecordHttpManager.getQueryDetailNew("", pageSize, pageNum, "", "", "", "", tranlogId, "", Constants.TRANLOG_DETAIL_TAG, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                progresser.showContent();
                RespTranRecItemByDayPageBean respTranRecItemByDayPageBean = JSON.parseObject(response.getResult().toString(), RespTranRecItemByDayPageBean.class);
                ResponseTranRecoderListBean responseTranRecoderListBean = TransRecordDataUtil.createBeanAdapterFromDay(respTranRecItemByDayPageBean);

                if (responseTranRecoderListBean != null && responseTranRecoderListBean.getResult() != null) {
                    ResponseTranRecoderListBean.ResultBeanX tmpResult = responseTranRecoderListBean.getResult();
                    if (tmpResult.getResult() != null) {
                        List<DailyDetailResp> m = transProtocol(tmpResult);
                        respList.addAll(m);
                    }

                }

                if (respList.size() > 0) {

                    TransRecordDialog.issueReceiptDialog(NewTranlogActivity.this, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rePrintCustomer();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rePrintMerchant();
                        }
                    });
                }
            }

            @Override
            public void onFaild(Response response) {
                progresser.showContent();
                progresser.showError(response.getMsg().toString(), true);
            }
        });
    }

    /**
     * refund确认后的回调
     *
     * @param amount
     */
    @Override
    public void onSave(String amount) {
        startActivityForResult(VoidTransActivity.getStartIntent(NewTranlogActivity.this, dailyDetailResp, Calculater.formotYuan(amount)), REQUEST_PAY_CANCEL);
    }

    private void rePrintCustomer() {
        for (final DailyDetailResp detailResp : respList) {
            if (getString(R.string.pay_tag).equals(detailResp.getTransKind())) {

                statisticsPresenter.reprintCustomerSale(detailResp);
            }
            if (getString(R.string.refund_tag).equals(detailResp.getTransKind())) {

                statisticsPresenter.reprintCustomerRefund(detailResp);
            }
        }
        respList.clear();
    }

    private void rePrintMerchant() {
        for (final DailyDetailResp detailResp : respList) {
            if (getString(R.string.pay_tag).equals(detailResp.getTransKind())) {

                statisticsPresenter.reprintMerchantSale(detailResp);
            }
            if (getString(R.string.refund_tag).equals(detailResp.getTransKind())) {

                statisticsPresenter.reprintMerchantRefund(detailResp);
            }
        }
        respList.clear();
    }

    public void toInputPasswordActivity(int requestCode) {
        Intent intent = new Intent(NewTranlogActivity.this, InputPassWordActivity.class);
        this.startActivityForResult(intent, requestCode);
    }


    private void printRefund() {
//        int printNumber = 1;
//        if (!TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.print_number))) {
//            printNumber = Integer.parseInt(AppConfigHelper.getConfig(AppConfigDef.print_number));
//        }
        int printNumber = Integer.valueOf(SettingPrinterModeManager.getCachePrintMode());
        final PrintServiceControllerProxy controller = new PrintServiceControllerProxy(this);
        switch (printNumber) {
            case 0:
                break;
            case 1:
                if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
                    WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT));
//                    PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT)));
                } else {
                    controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT));
                }
                break;
            case 2:
                if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
                    WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT));
//                    PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT)));
                } else {
                    controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT));
                }
                final NoticeDialogFragment dialogFragment = NoticeDialogFragment.newInstance("INFORMATION", "Customer Copy?", "YES", "NO");
                dialogFragment.setListener(new DialogHelper.DialogCallbackAndNo() {
                    @Override
                    public void callback() {
                        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
                            WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT));
//                            PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT)));
                        } else {
                            controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT));
                        }
                    }

                    @Override
                    public void callbackNo() {
                        dialogFragment.dismiss();
                    }
                });
                dialogFragment.show(getSupportFragmentManager(), "SimpleMsgDialogFragment");
                break;
            case 3:
                if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
                    WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT));
//                    PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT)));
                } else {
                    controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_SALE_REFUND_CONTEXT));
                }
                final NoticeDialogFragment fragmentDialog = NoticeDialogFragment.newInstance("INFORMATION", "Customer Copy?", "YES", "NO");
                fragmentDialog.setListener(new DialogHelper.DialogCallbackAndNo() {
                    @Override
                    public void callback() {
                        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
                            WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT));
//                            PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT)));
                        } else {
                            controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT));
                        }
                        final NoticeDialogFragment dialog = NoticeDialogFragment.newInstance("INFORMATION", "Customer Copy?", "YES", "NO");
                        dialog.setListener(new DialogHelper.DialogCallbackAndNo() {
                            @Override
                            public void callback() {
                                if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
                                    WebPrintHelper.getInstance().print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT));
//                                    PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT)));
                                } else {
                                    controller.print(AppConfigHelper.getConfig(AppConfigDef.PRINT_CUSTOMER_REFUND_CONTEXT));
                                }
                            }

                            @Override
                            public void callbackNo() {
                                dialog.dismiss();
                            }
                        });
                        dialog.show(getSupportFragmentManager(), "SimpleMsgDialogFragment2");
                    }

                    @Override
                    public void callbackNo() {
                        fragmentDialog.dismiss();
                    }
                });
                fragmentDialog.show(getSupportFragmentManager(), "SimpleMsgDialogFragment1");
                break;
        }
    }

    /**
     * 点击侧边栏回调
     *
     * @param timeRange
     * @param tranType
     * @param startDate
     * @param endDate
     * @param tranlogId
     */
    @Override
    public void onQuery(String timeRange, String tranType, String startDate, String endDate, String tranlogId, String invoiceNum) {
        operateFragmentOnQuery(tranType, timeRange, startDate, endDate, tranlogId, invoiceNum);
    }
}
