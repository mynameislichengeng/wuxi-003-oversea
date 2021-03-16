package com.wizarpos.pay.recode.hisotory.activitylist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.recode.constants.IntentConstants;
import com.wizarpos.pay.recode.hisotory.activitylist.adapter.TranDetailAdapter;
import com.wizarpos.pay.recode.sale.widget.BarcodeView;
import com.motionpay.pay2.lite.R;

public class TranLogDetailActivity extends BaseViewActivity {

    private final String TAG = TranLogDetailActivity.class.getName();

    private BarcodeView barcodeView;
    private RecyclerView recyclerView;
    private TranDetailAdapter tranDetailAdapter;
    private DailyDetailResp resp;


    public static void startOpenActivity(Context context, DailyDetailResp resp) {
        Intent intent = new Intent(context, TranLogDetailActivity.class);
        intent.putExtra(IntentConstants.TradDetailActivty.DETAIL_OBJ.getKey(), resp);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        resp = (DailyDetailResp) getIntent().getSerializableExtra(IntentConstants.TradDetailActivty.DETAIL_OBJ.getKey());
        if (resp == null) {
            Log.e(TAG, "---传进来的数据null--");
        }
        tranDetailAdapter = new TranDetailAdapter(this, resp);
    }

    private void initView() {
        showTitleBack();
        setTitleText(getResources().getString(R.string.tran_detail));
        //
        setMainView(R.layout.activity_trandetail);
        barcodeView = findViewById(R.id.barcodeview);
        barcodeView.setBarcodePayFor(resp.getTranLogId());
        recyclerView = findViewById(R.id.list);
        recyclerView.setAdapter(tranDetailAdapter);

    }
}
