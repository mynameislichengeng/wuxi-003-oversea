package com.wizarpos.pay.statistics.activity;

import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.statistics.adapter.TicketDetialAdapter;
import com.wizarpos.pay.statistics.model.TicketTranLogResp;
import com.wizarpos.pay.statistics.presenter.StatisticsPresenter;
import com.wizarpos.pay2.lite.R;

/**
 * 券明细列表
 * @author wu
 *
 */
public class TicketDetialActivity extends BaseViewActivity{

	private StatisticsPresenter presenter;
	private TicketDetialAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleText("券交易明细");
		showTitleBack();
		setMainView(R.layout.activity_ticket_detial);
		
		adapter = new TicketDetialAdapter(this);
		ListView lvTicketials = (ListView) findViewById(R.id.lv_list_detail_Recode);
		lvTicketials.setAdapter(adapter);
		
		presenter = new StatisticsPresenter(this);
		
		getTicketDetials();
	}

	private void getTicketDetials() {
		Bundle result = getIntent().getExtras();
		int timeRange = Integer.parseInt((String) result.get("transTime"));
		String startTime = (String) result.get("startDate");
		String endTime = (String) result.get("endDate");
		progresser.showProgress();
		presenter.getTticketDetail(timeRange+"", startTime, endTime, new ResultListener() {
			
			@Override
			public void onSuccess(Response response) {
				List<TicketTranLogResp> ticketTranLogResps = (List<TicketTranLogResp>) response.result;
				if(ticketTranLogResps .isEmpty()){
					progresser.showError("暂无数据", false);
				}else{
					progresser.showContent();
					adapter.setDataChanged(ticketTranLogResps);
				}
			}
			
			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(TicketDetialActivity.this, response.getMsg());
			}
		});
	}
	
}
