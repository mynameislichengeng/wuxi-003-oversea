//package com.wizarpos.pay.ui.newui;
//
//import android.os.Bundle;
//import android.widget.FrameLayout;
//import android.widget.TextView;
//
//import com.wizarpos.base.net.NetRequest;
//import com.wizarpos.base.net.Response;
//import com.wizarpos.base.net.ResponseListener;
//import com.wizarpos.pay.common.Constants;
//import com.wizarpos.pay.common.base.BaseViewActivity;
//import com.wizarpos.pay.db.AppConfigDef;
//import com.wizarpos.pay.db.AppConfigHelper;
//import com.wizarpos.pay.model.TranLogVo;
//import com.wizarpos.pay.statistics.presenter.StatisticsPresenter;
//import com.wizarpos.pay.ui.ProgressLayout;
//import com.wizarpos.pay.ui.newui.util.TodayTotalUtil;
//import com.wizarpos.pay.ui.widget.PieChart02View;
//import com.wizarpos.pay.view.util.Tools;
//import com.motionpay.pay2.lite.R;
//
//import org.xclcharts.chart.PieData;
//
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//
//import static com.wizarpos.pay.db.AppConfigHelper.getConfig;
//
///**
// * 今日汇总新版
// *
// * @author wu at 2019-06-08
// */
//public class NewDailySumActivityPlusBackup extends BaseViewActivity {
//    private StatisticsPresenter statisticsPresenter;
//    private TextView tvTimeRange,tvDevice;
//    private TextView tvGrossSalesCount;
//    private TextView tvGrossSalesAmount;
//    private TextView tvRefundCount;
//    private TextView tvRefundAmount;
//    private TextView tvNetSaleCount;
//    private TextView tvNetSaleAmount;
//
//    private TextView tvWechatNetSaleAmount;
//    private TextView tvWechatNetSaleCount;
//
//    private TextView tvTipsAmount;
//    private TextView tvTipsCount;
//
//    private TextView tvAliPayNetSaleAmount;
//    private TextView tvAliPayNetSaleCount;
//
//    private TextView tvUnionPayNetSalesAmount;
//    private TextView tvUnionPayNetSalesCount;
//
//    private TextView tvTotalCollectedAmount;
//
//    private ProgressLayout plDailyDetail;
//
//    private TranLogVo tranLogVo;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        statisticsPresenter = new StatisticsPresenter(this);
//        initView();
//        initData();
//    }
//
//    private void initView() {
//        setMainView(R.layout.activity_new_daily_sum_plus);
//        showTitleBack();
//        setTitleText(getResources().getString(R.string.daily_summary_international));
//        setTitleRight(getResources().getString(R.string.print));
//        tvTimeRange = findViewById(R.id.tvTimeRange);
//        tvDevice = findViewById(R.id.tvDevice);
//        plDailyDetail = findViewById(R.id.plDailyDetail);
//
//        tvGrossSalesCount = findViewById(R.id.tvGrossSalesCount);
//        tvGrossSalesAmount = findViewById(R.id.tvGrossSalesAmount);
//        tvRefundCount = findViewById(R.id.tvRefundCount);
//        tvRefundAmount = findViewById(R.id.tvRefundAmount);
//        tvNetSaleCount = findViewById(R.id.tvNetSaleCount);
//        tvNetSaleAmount = findViewById(R.id.tvNetSaleAmount);
//        tvTipsCount = findViewById(R.id.tvTipsCount);
//        tvTipsAmount = findViewById(R.id.tvTipsAmount);
//
//        tvWechatNetSaleAmount = findViewById(R.id.tvWechatNetSaleAmount);
//        tvWechatNetSaleCount = findViewById(R.id.tvWechatNetSaleCount);
//
//        tvAliPayNetSaleAmount = findViewById(R.id.tvAliPayNetSaleAmount);
//        tvAliPayNetSaleCount = findViewById(R.id.tvAliPayNetSaleCount);
//
//        tvUnionPayNetSalesAmount = findViewById(R.id.tvUnionPayNetSalesAmount);
//        tvUnionPayNetSalesCount = findViewById(R.id.tvUnionPayNetSalesCount);
//
//        tvTotalCollectedAmount = findViewById(R.id.tvTotalCollectedAmount);
//        if("4".equals(getConfig(AppConfigDef.authFlag))){
//            tvDevice.setText("Device: "+AppConfigHelper.getConfig(AppConfigDef.sn, ""));
//        }else {
//            tvDevice.setText("Device: All");
//        }
//    }
//
//    /**
//     * 初始化饼图
//     */
//    private void initPie(ArrayList<PieData> chartData) {
//        if (chartData == null) {
//            return;
//        }
//        //饼图
//        PieChart02View pie = new PieChart02View(this, chartData);
//        FrameLayout flPie = findViewById(R.id.flPie);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//        flPie.addView(pie);
//    }
//
//    /**
//     * @return 当前日期
//     */
//    private String getCurrentTime() {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        Date currentDate = new Date(System.currentTimeMillis());
//        return format.format(currentDate);
//    }
//
//    private void initData() {
//        progresser.showProgress();
//        statisticsPresenter.getDailySummaryPlus(getCurrentTime(), Constants.DAILY_TRANS_TAG, new ResponseListener() {
//            @Override
//            public void onSuccess(Response response) {
//                progresser.showContent();
//                tranLogVo = (TranLogVo) response.getResult();
//                tvTimeRange.setText(com.wizarpos.atool.tool.DateUtil.formatInternationalDate(tranLogVo.getBeginTime())
//                        + " - "
//                        + com.wizarpos.atool.tool.DateUtil.formatInternationalDate(tranLogVo.getEndTime()));
//
//                tvGrossSalesCount.setText(String.valueOf(tranLogVo.getGrossSalesNumber()));
//                tvGrossSalesAmount.setText("$" + Tools.formatFen(tranLogVo.getGrossSalesAmount()));
//
//                tvRefundCount.setText(String.valueOf(tranLogVo.getRefundsNumber()));
//                tvRefundAmount.setText("$" + Tools.formatFen(tranLogVo.getRefundsAmount()));
//
//                tvNetSaleCount.setText(String.valueOf(tranLogVo.getNetSalesNumber()));
//                tvNetSaleAmount.setText("$" + Tools.formatFen(tranLogVo.getNetSalesAmount()));
//
//                tvTipsCount.setText(String.valueOf(tranLogVo.getTipsNumber()));
//                tvTipsAmount.setText("$" + Tools.formatFen(tranLogVo.getTipsAmount()));
//
//                tvWechatNetSaleCount.setText(String.valueOf(tranLogVo.getWechatNetSalesNumber()));
//                tvWechatNetSaleAmount.setText("$" + Tools.formatFen(tranLogVo.getWechatNetSalesAmount()));
//
//                tvAliPayNetSaleCount.setText(String.valueOf(tranLogVo.getAlipayNetSalesNumber()));
//                tvAliPayNetSaleAmount.setText("$" + Tools.formatFen(tranLogVo.getAlipayNetSalesAmount()));
//
//                tvUnionPayNetSalesCount.setText(String.valueOf(tranLogVo.getUnionPayNetSalesNumber()));
//                tvUnionPayNetSalesAmount.setText("$" + Tools.formatFen(tranLogVo.getUnionPayNetSalesAmount()));
//
//                tvTotalCollectedAmount.setText("$" + Tools.formatFen(tranLogVo.getTotalCollected()));
//
//                initPie(initPieData(tranLogVo));
//
//                plDailyDetail.showContent();
//            }
//
//            @Override
//            public void onFaild(Response response) {
//                progresser.showContent();
//                plDailyDetail.showError(getResources().getString(R.string.no_data), R.drawable.nodata_new, false);
//            }
//        });
//    }
//
//    private ArrayList<PieData> initPieData(TranLogVo tranLogVo) {
//        ArrayList<PieData> chartData = new ArrayList<>();
//        PieData wechatPieData = getPieData(tranLogVo, tranLogVo.getWechatSalesAmount(), "Wechat Pay", TodayTotalUtil.FLAG_WEXIN_COLOR);
//        PieData alipayPieData = getPieData(tranLogVo, tranLogVo.getAlipaySalesAmount(), "Alipay", TodayTotalUtil.FLAG_ALIPAY_COLOR);
//        PieData unionPieData = getPieData(tranLogVo, tranLogVo.getUnionPaySalesAmount(), "Union Pay QC", TodayTotalUtil.FLAG_UNION_COLOR);
//        chartData.add(wechatPieData);
//        chartData.add(alipayPieData);
//        chartData.add(unionPieData);
//
//        return chartData;
//    }
//
//    private PieData getPieData(TranLogVo tranLogVo, int grossAmount, String key, int color) {
//        BigDecimal totalAmountDec = new BigDecimal(tranLogVo.getGrossSalesAmount());
//        BigDecimal scale = new BigDecimal(100);
//        BigDecimal zero = new BigDecimal(0);
//        if (totalAmountDec.compareTo(zero) == 0) {
//            return null;
//        }
//
//        BigDecimal beanAmount = new BigDecimal(grossAmount);
//        int beanCount = beanAmount.divide(totalAmountDec, 2, BigDecimal.ROUND_HALF_UP).multiply(scale).intValue();
//        if (beanCount < 1 && beanAmount.compareTo(zero) > 0) {
//            beanCount = 1;
//        }
//        float floatCount = beanAmount.multiply(scale).divide(totalAmountDec, 1, BigDecimal.ROUND_HALF_UP).floatValue();
//        return new PieData(key + floatCount + "%", beanCount + "%", beanCount, color);
//    }
//
//    @Override
//    protected void onTitleRightClicked() {
//        super.onTitleRightClicked();
//        printDay();
//    }
//
//    @Override
//    protected void onTitleBackClikced() {
//        startActivity(NewMainActivity.getStartIntent(this));
//        super.onTitleBackClikced();
//    }
//
//    @Override
//    public void onBackPressed() {
//        startActivity(NewMainActivity.getStartIntent(this));
//        NetRequest.getInstance().cancelFirst(Constants.DAILY_TRANS_TAG);
//        super.onBackPressed();
//    }
//
//    /**
//     * @Author: Huangweicai
//     * @date 2015-11-4 下午6:59:33
//     * @Description:打印交易汇总(日结单)
//     */
//    private void printDay() {
//        if(tranLogVo!=null){
//            try {
//                statisticsPresenter.printTodaySumPlus(tranLogVo);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
