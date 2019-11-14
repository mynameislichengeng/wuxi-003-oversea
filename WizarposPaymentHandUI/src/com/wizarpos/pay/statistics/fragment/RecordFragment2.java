package com.wizarpos.pay.statistics.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.wizarpos.base.net.Response;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.activity.CancelTransactionActivity;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.manage.activity.InputPassWordActivity;
import com.wizarpos.pay.statistics.activity.ItemDetailActivity;
import com.wizarpos.pay.statistics.activity.RecordActivity;
import com.wizarpos.pay.statistics.adapter.RecordDetialAdapter;
import com.wizarpos.pay.statistics.presenter.StatisticsPresenter;
import com.wizarpos.pay.ui.ProgressLayout;
import com.wizarpos.pay.view.swipyrefresh.SwipyRefreshLayout;
import com.wizarpos.pay.view.swipyrefresh.SwipyRefreshLayout.OnRefreshListener;
import com.wizarpos.pay.view.swipyrefresh.SwipyRefreshLayoutDirection;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.wizarpos.pay.view.util.MultieChooseDialogFragment;
import com.wizarpos.pay.view.util.MultieChooseItem;
import com.wizarpos.pay2.lite.R;

/**
 * 收款记录
 */
public class RecordFragment2 extends Fragment implements OnRefreshListener, OnItemLongClickListener, OnItemClickListener {

    private static final String PAGE_SIZE = "10";
    private TextView tvDate;
    private ListView resListview;
    private RecordDetialAdapter recordDetialAdapter;
    private SwipyRefreshLayout mSwipyRefreshLayout;

    private StatisticsPresenter presenter;

    private ProgressLayout progress;

    private long totalPage;// 总页数
    private long currentPage;// 当前页数 从1开始
    private int tranCode = -1;// 交易类型 默认为全部(-1)
    private int timeRange = 0;// 时间范围 默认为今天(0)
    private String startTime = null;// 起始时间
    private String endTime = null;// 终止时间
    private String orderNo = null;// 订单号

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        tvDate = (TextView) view.findViewById(R.id.tv_date);

        mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
        mSwipyRefreshLayout.setOnRefreshListener(this);

        recordDetialAdapter = new RecordDetialAdapter(getActivity());

        resListview = (ListView) view.findViewById(R.id.lv_list_detail_Recode);
        resListview.setAdapter(recordDetialAdapter);
        resListview.setOnItemClickListener(this);
        resListview.setOnItemLongClickListener(this);// 收款记录：增加长按弹出撤销交易 wu

        progress = (ProgressLayout) view.findViewById(R.id.progress);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new StatisticsPresenter(getActivity());
        presenter.getLogs().clear();
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            String startTime = bundle.getString(RecordActivity.START_TIME_TAG);
            String endTime = bundle.getString(RecordActivity.END_TIME_TAG);
            String orderNo = bundle.getString(RecordActivity.ORDER_NO_TAG);
            int tranCode = bundle.getInt(RecordActivity.TRAN_CODE_TAG, -1);
            int timeRange = bundle.getInt(RecordActivity.TIME_RANGE_TAG, 0);
            query(tranCode, timeRange, startTime, endTime, orderNo);
        } else {
            getTodayDetail();
        }
    }

    private void getTodayDetail() {
        initParams();
        showProgress();
        loadDetialData(true);
    }

    private void showProgress() {
        if (presenter != null) { //bugfix
            progress.showProgress();
        }
    }

    private void showContent() {
        if (progress != null) {
            progress.showContent();
        }
    }

    /**
     * 初始化参数
     */
    private void initParams() {
        recordDetialAdapter.clear();
        totalPage = 0;
        currentPage = 1;
        tranCode = -1;
        timeRange = 0;
        startTime = endTime = orderNo = null;
        tvDate.setText(StatisticsPresenter.convertTimeRange2String(timeRange));
    }

    /**
     * 加载数据
     */
    private void loadDetialData(final boolean isRefresh) {
        LogEx.i("loadDetialData", "loadDetialData");
        startRefresh();
        canLoadMore();
//		if (!TextUtils.isEmpty(orderNo)) {// 根据订单好来查询@hong
//			getTransactionDetial(isRefresh);
//		} else {
        transactionDetialQuery(isRefresh);
//		}
    }

    private void transactionDetialQuery(final boolean isRefresh) {
        presenter.transactionDetialQuery(tranCode, timeRange, startTime, endTime, orderNo, currentPage + "", PAGE_SIZE, new ResultListener() {

            @Override
            public void onSuccess(Response response) {
                stopRefresh();
                List<String[]> detialRecords = (ArrayList<String[]>) response.result;
                // if(detialRecords == null || detialRecords.isEmpty()){
                // progress.showError("暂无数据", true);
                // return;
                // }
                totalPage = presenter.getTotalPage();// 总页数
                if (detialRecords == null || detialRecords.isEmpty()) {
                    recordDetialAdapter.clear();
                    showErrorView("暂无数据");
                } else {
                    if (isRefresh) {
                        recordDetialAdapter.setDataChanged(detialRecords);
                    } else {
                        recordDetialAdapter.addDataChanged(detialRecords);
                    }
                }
                // if(isNoMorePage()){
                // cannotLoadMore();
                // }
            }

            @Override
            public void onFaild(Response response) {
                stopRefresh();
                showErrorView(response.msg);
                // UIHelper.ToastMessage(getActivity(), response.msg);
            }
        });
    }

    private void getTransactionDetial(final boolean isRefresh) {
        presenter.getTransactionDetial(orderNo, new ResultListener() {

            @Override
            public void onSuccess(Response response) {
                stopRefresh();
                List<String[]> detialRecords = (ArrayList<String[]>) response.result;
                totalPage = presenter.getTotalPage();// 总页数
                if (detialRecords == null || detialRecords.isEmpty()) {
                    recordDetialAdapter.clear();
                    showErrorView("暂无数据");
                } else {
                    if (isRefresh) {
                        recordDetialAdapter.setDataChanged(detialRecords);
                    } else {
                        recordDetialAdapter.addDataChanged(detialRecords);
                    }
                }
                // if (isNoMorePage()) {
                // cannotLoadMore();
                // }
            }

            @Override
            public void onFaild(Response response) {
                stopRefresh();
                showErrorView(response.msg);
            }
        });
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        Log.d("direction", direction.name());
        Log.d("onRefresh", "onRefresh");

        if (SwipyRefreshLayoutDirection.TOP == direction) {
            presenter.getLogs().clear();
            currentPage = 1;
            // initParams();
            loadDetialData(true);
        } else if (SwipyRefreshLayoutDirection.BOTTOM == direction) {
            if (isNoMorePage()) {
                UIHelper.ToastMessage(getActivity(), "没有更多数据了");
                stopRefresh();
                // cannotLoadMore();
            } else {
                currentPage++;
                loadDetialData(false);
            }
        }
    }

    /**
     * 不允许加载更多
     */
    private void cannotLoadMore() {
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
    }

    private void canLoadMore() {
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
    }

    /**
     * 开始加载
     */
    private void startRefresh() {
        Log.d("recordfragment", "开始加载");
        mSwipyRefreshLayout.setRefreshing(true);
    }

    /**
     * 停止加载
     */
    private void stopRefresh() {
        mSwipyRefreshLayout.setRefreshing(false);
        showContent();
    }

    /**
     * 能否加载更多
     *
     * @return
     */
    private boolean isNoMorePage() {
        return currentPage >= totalPage;
    }

    public void query(int tranCode, int timeRange, String startTime, String endTime, String orderNo) {
        LogEx.i("TAG", "query");
        showProgress();
        presenter.getLogs().clear();
        recordDetialAdapter.clear();
        tvDate.setText(StatisticsPresenter.convertTimeRange2String(timeRange));
        this.totalPage = 0;
        this.currentPage = 1;
        this.tranCode = tranCode;
        this.timeRange = timeRange;
        this.startTime = startTime;
        this.endTime = endTime;
        this.orderNo = orderNo;
        loadDetialData(true);
    }

    private void showErrorView(String msg) {
        if (progress != null) {
            if (TextUtils.isEmpty(msg) == false) {
                progress.showError(msg, false);
            } else {
                progress.showError("未知异常", false);
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == resListview.getId()) {
            String[] itemContent = (String[]) recordDetialAdapter.getItem(position);
            String orderNo = itemContent[0];
            if (TextUtils.isEmpty(orderNo)) {
                return false;
            }
            if (DeviceManager.getInstance().isSupprotPrint()) { //增加逻辑，Q1支持重新打印凭条
                showActionItemDialog(orderNo);
                return true;
            } else {
                revoke(orderNo);
                return true;
            }
        }
        return false;
    }

    private void showActionItemDialog(final String orderNo) {
        MultieChooseItem itemPrint = new MultieChooseItem();
        itemPrint.setTitle("打印");
        itemPrint.setImgId(R.drawable.icon_action_print);
        MultieChooseItem itemRevoke = new MultieChooseItem();
        itemRevoke.setTitle("撤销");
        itemRevoke.setImgId(R.drawable.icon_mix_back);
        List<MultieChooseItem> items = new ArrayList<MultieChooseItem>(2);
        items.add(itemPrint);
        items.add(itemRevoke);
        DialogHelper2.showMultieChooseDialog(getActivity(), items, new MultieChooseDialogFragment.MultieChooseListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        print(orderNo);
                        break;
                    case 1:
                        revoke(orderNo);
                        break;
                }
            }
        });
    }

    /**
     * 打印凭条
     *
     * @param orderNo
     */
    private void print(String orderNo) {
        showProgress();
        presenter.printDetialTransaction(orderNo, new ResultListener() {
            @Override
            public void onSuccess(Response response) {
                showContent();
            }

            @Override
            public void onFaild(Response response) {
                showContent();
            }
        });
    }

    /**
     * 撤销
     *
     * @param orderNo
     */
    private void revoke(String orderNo) {
        Intent intent = new Intent(getActivity(), InputPassWordActivity.class);
        intent.putExtra(CancelTransactionActivity.ORDER_NO, orderNo);
        startActivity(intent);
        return;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//XXX 暂时隐藏改功能
        List<String[]> detials = presenter.getDetialItems(position);
        if (detials == null || detials.isEmpty()) {
            return;
        }

        Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
        intent.putExtra("MIXTRANS", (Serializable) detials);
        startActivity(intent);
    }


}
