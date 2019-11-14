package com.wizarpos.pay.ui.newui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wizarpos.atool.log.Logger;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.log.util.DateUtil;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.model.DailyRes;
import com.wizarpos.pay.model.TodayDetailBean;
import com.wizarpos.pay.statistics.presenter.StatisticsPresenter;
import com.wizarpos.pay.ui.ProgressLayout;
import com.wizarpos.pay.ui.newui.adapter.ShowDetailAdapter;
import com.wizarpos.pay.ui.newui.util.TodayTotalUtil;
import com.wizarpos.pay.ui.widget.PieChart02View;
import com.wizarpos.pay2.lite.R;

import org.xclcharts.chart.PieData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Deprecated
public class NewDailySumActivity extends BaseViewActivity {
    private ListView lvShowDetail;
    private StatisticsPresenter statisticsPresenter;
    private TextView tvTimeRange;
    private TextView tvTransactionDetail;
    private LinearLayout llRevoke;
    private TextView tvRevokeAmount;
    private TextView tvRevokeCount;
    private ProgressLayout plDailyDetail;

    //饼图
    private PieChart02View pie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statisticsPresenter = new StatisticsPresenter(this);
        initView();
        initData();
    }

    private void initView() {
        setMainView(R.layout.activity_new_daily_sum);
        showTitleBack();
        setTitleText(getResources().getString(R.string.daily_summary_international));
        setTitleRight(getResources().getString(R.string.print));
        lvShowDetail = (ListView) findViewById(R.id.lvShowDetail);
        tvTimeRange = (TextView) findViewById(R.id.tvTimeRange);
        tvTransactionDetail = (TextView) findViewById(R.id.tvTransactionDetail);
        plDailyDetail = (ProgressLayout) findViewById(R.id.plDailyDetail);
        llRevoke = (LinearLayout) findViewById(R.id.llRevoke);
        tvRevokeAmount = (TextView) findViewById(R.id.tvRevokeAmount);
        tvRevokeCount = (TextView) findViewById(R.id.tvRevokeCount);

    }

    /**
     * 初始化饼图
     */
    private void initPie(ArrayList<PieData> chartData) {
        pie = new PieChart02View(this, chartData);
        FrameLayout flPie = (FrameLayout) findViewById(R.id.flPie);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        flPie.addView(pie);
    }

    /**
     * @return 当前日期
     */
    private String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date(System.currentTimeMillis());
        return format.format(currentDate);
    }

    /**
     * @return 当前日期
     */
    private String getShowCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("MM dd,yyyy");
        Date currentDate = new Date(System.currentTimeMillis());
        return format.format(currentDate);
    }

    /**
     * @return 当前日期
     */
    private String ShowCurrentTime(long t) {
//        Date currentDate = new Date(System.currentTimeMillis());
        Date currentDate = new Date(t);
        String currentTime = getConvertMonth(DateUtil.getMonth(currentDate) + 1) + " " + DateUtil.getDay(currentDate) + "," + DateUtil.getYear(currentDate);
        return currentTime;
    }

    private String getConvertMonth(int month) {
        switch (month) {
            case 1:
                return getString(R.string.jan);
            case 2:
                return getString(R.string.feb);
            case 3:
                return getString(R.string.mar);
            case 4:
                return getString(R.string.apr);
            case 5:
                return getString(R.string.may);
            case 6:
                return getString(R.string.june);
            case 7:
                return getString(R.string.july);
            case 8:
                return getString(R.string.aug);
            case 9:
                return getString(R.string.sept);
            case 10:
                return getString(R.string.oct);
            case 11:
                return getString(R.string.nov);
            case 12:
                return getString(R.string.dec);
            default:
                return "";
        }
    }

    private void initData() {
        final ShowDetailAdapter showDetailAdapter = new ShowDetailAdapter(NewDailySumActivity.this);
        final List<TodayDetailBean> list = new ArrayList<>();
        TodayDetailBean bean = new TodayDetailBean();
        progresser.showProgress();
        statisticsPresenter.getDailySummary(getCurrentTime(), Constants.DAILY_TRANS_TAG, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                progresser.showContent();
                DailyRes dailyRes = (DailyRes) response.getResult();
                List<TodayDetailBean> todayDetailBeans = dailyRes.getList();
                if (dailyRes != null) {
                    tvTransactionDetail.setText("$" + Calculater.formotFen(dailyRes.getTotalAmount()));
                    Logger.error("毫秒值 1 = "+ShowCurrentTime(dailyRes.getCurrentDay()));
                    tvTimeRange.setText("" + ShowCurrentTime(dailyRes.getCurrentDay()));
//                    tvCustomAmount.setText(Calculater.formotFen(dailyRes.getConsumptionAmount()));
//                    tvRevokeAmount.setText(Calculater.formotFen(dailyRes.getRevokeAmount()));
                    plDailyDetail.showContent();
                    if (todayDetailBeans == null || todayDetailBeans.size() == 0) {
                        plDailyDetail.showError(getResources().getString(R.string.no_today_transaction), R.drawable.nodata_new, false);
                    } else {
                        for (TodayDetailBean bean : todayDetailBeans) {
                            TodayTotalUtil.setTodatTotalBeanShowDetail(bean);
                        }
                        String revokeAmount = "";
                        String revokeCount = "";
                        if (TextUtils.isEmpty(dailyRes.getRefundAmount())) {
                            revokeAmount = "0.00";
                        } else {
                            revokeAmount = Calculater.formotFen(dailyRes.getRefundAmount());
                        }
                        if (TextUtils.isEmpty(dailyRes.getRefundCount())) {
                            revokeCount = "0";
                        } else {
                            revokeCount = dailyRes.getRefundCount() + "";
                        }
                        tvRevokeAmount.setText("$" + revokeAmount);
                        tvRevokeCount.setText(revokeCount);
                        showDetailAdapter.setDataChanged(todayDetailBeans);
                        lvShowDetail.setAdapter(showDetailAdapter);
                        //计算消费饼图
                        initPie(PieChart02View.getCharData(todayDetailBeans, dailyRes.getConsumptionAmount()));
                    }
                }
            }

            @Override
            public void onFaild(Response response) {
                plDailyDetail.showError(getResources().getString(R.string.no_data), R.drawable.nodata_new, false);
            }
        });
    }

    @Override
    protected void onTitleRightClicked() {
        super.onTitleRightClicked();
        printDay();
    }

    @Override
    protected void onTitleBackClikced() {
        startActivity(NewMainActivity.getStartIntent(this));
        super.onTitleBackClikced();
    }

    @Override
    public void onBackPressed() {
        startActivity(NewMainActivity.getStartIntent(this));
        NetRequest.getInstance().cancelFirst(Constants.DAILY_TRANS_TAG);
        super.onBackPressed();
    }

    /**
     * @Author: Huangweicai
     * @date 2015-11-4 下午6:59:33
     * @Description:打印交易汇总(日结单)
     */
    private void printDay() {
        statisticsPresenter.printTodaySum();
        /*
        progresser.showProgress();
        statisticsPresenter.transactionGroupQuery(new BasePresenter.ResultListener() {

            @Override
            public void onSuccess(Response response) {
                Log.i("tag", "请求成功");
                statisticsPresenter.printGroupQuery();
                progresser.showContent();
            }

            @Override
            public void onFaild(Response response) {
                Log.i("tag", "请求失败");
                progresser.showContent();
                Toast.makeText(NewDailySumActivity.this, response.msg, Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
