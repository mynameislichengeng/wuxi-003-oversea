package com.wizarpos.pay.ui.newui;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.model.DailyRes;
import com.wizarpos.pay.model.TodayDetailBean;
import com.wizarpos.pay.statistics.presenter.StatisticsPresenter;
import com.wizarpos.pay.ui.ProgressLayout;
import com.wizarpos.pay.ui.newui.adapter.ShowDetailAdapter;
import com.motionpay.pay2.lite.R;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by blue_sky on 2016/3/23.
 * @deprecated
 */
public class NewDailyStatementActivity extends BaseViewActivity {
    private ListView lvShowDetail;
    private StatisticsPresenter statisticsPresenter;
    private TextView tvTimeRange;
    private TextView tvTransactionDetail;
    private DonutProgress dpConsumption;//消费
    private DonutProgress dpRevoke;//撤销
    private ProgressLayout plDailyDetail;
    private TextView tvCustomAmount;
    private TextView tvRevokeAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setMainView(R.layout.activity_new_daily_statement);
        showTitleBack();
        setTitleText("日结单");
//        setTitleRightImage(R.drawable.search);
        lvShowDetail = (ListView) findViewById(R.id.lvShowDetail);
        tvTimeRange = (TextView) findViewById(R.id.tvTimeRange);
        tvTransactionDetail = (TextView) findViewById(R.id.tvTransactionDetail);
        dpConsumption = (DonutProgress) findViewById(R.id.dpConsumption);
        dpRevoke = (DonutProgress) findViewById(R.id.dpRevoke);
        tvCustomAmount = (TextView) findViewById(R.id.tvCustomAmount);
        tvRevokeAmount = (TextView) findViewById(R.id.tvRevokeAmount);
        plDailyDetail = (ProgressLayout) findViewById(R.id.plDailyDetail);
        statisticsPresenter = new StatisticsPresenter(this);
        tvTimeRange.setText("交易日期：" + getCurrentTime());
        statisticsPresenter = new StatisticsPresenter(this);
    }

    /**
     *
     * @return 当前日期
     */
    private String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date(System.currentTimeMillis());
        return format.format(currentDate);
    }

    private void initData() {
        final ShowDetailAdapter showDetailAdapter = new ShowDetailAdapter(NewDailyStatementActivity.this);
        final List<TodayDetailBean> list = new ArrayList<>();
        TodayDetailBean bean = new TodayDetailBean();
        progresser.showProgress();
        statisticsPresenter.getDailySummary(getCurrentTime(), Constants.DAILY_TRANS_TAG,new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                progresser.showContent();
                DailyRes dailyRes = (DailyRes) response.getResult();
                List<TodayDetailBean> todayDetailBeens = dailyRes.getList();
                if (dailyRes != null) {
                    tvTransactionDetail.setText(Calculater.formotFen(dailyRes.getTotalAmount()));
                    tvCustomAmount.setText(Calculater.formotFen(dailyRes.getConsumptionAmount()));
                    tvRevokeAmount.setText(Calculater.formotFen(dailyRes.getRevokeAmount()));
                    plDailyDetail.showContent();
                    if (todayDetailBeens == null || todayDetailBeens.size() == 0){
                        plDailyDetail.showError("今日暂无交易记录",false);
                    } else {
                        showDetailAdapter.setDataChanged(todayDetailBeens);
                        lvShowDetail.setAdapter(showDetailAdapter);
                        BigDecimal consumptionAmount = new BigDecimal(dailyRes.getConsumptionAmount());
                        BigDecimal revokeAmount = new BigDecimal(dailyRes.getRevokeAmount());
                        BigDecimal totalAmount = new BigDecimal(dailyRes.getTotalAmount());
                        BigDecimal scale = new BigDecimal(100);
                        BigDecimal zero  = new BigDecimal(0);
                        if (totalAmount.compareTo(zero) == 0) {
                            dpConsumption.setProgress(0);
                            dpRevoke.setProgress(0);
                        } else {
                            int consumpcount = consumptionAmount.divide(totalAmount,2,BigDecimal.ROUND_HALF_UP).multiply(scale).intValue();
                            int revoekcount = revokeAmount.divide(totalAmount,2,BigDecimal.ROUND_HALF_UP).multiply(scale).intValue();
                            if (consumpcount < 1 && consumptionAmount.compareTo(zero) == 1){
                                consumpcount = 1;
                            }
                            if (revoekcount < 1 && revokeAmount.compareTo(zero) == 1){
                                revoekcount = 1;
                            }
                            dpConsumption.setProgress(consumpcount);
                            dpRevoke.setProgress(revoekcount);
                        }
                    }
                }
            }

            @Override
            public void onFaild(Response response) {
                progresser.showContent();
            }
        });
    }

    @Override
    protected void onTitleRightClicked() {
        super.onTitleRightClicked();
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
}
