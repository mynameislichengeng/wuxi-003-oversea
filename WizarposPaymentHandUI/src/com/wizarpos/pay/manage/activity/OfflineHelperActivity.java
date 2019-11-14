package com.wizarpos.pay.manage.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.db.CashPayRepair;
import com.wizarpos.pay.manager.presenter.OfflineHelper;
import com.wizarpos.pay.view.adapter.OfflineTransDataAdapter;
import com.wizarpos.pay2.lite.R;

/**
 *  离线助手
 *
 */
public class OfflineHelperActivity extends BaseViewActivity {
	PopupWindow popupWindow;
	Button btnOnline, btnOffline;
	ListView listView;
	List<CashPayRepair> listPay = new ArrayList<CashPayRepair>();
	OfflineTransDataAdapter offlineTransDataAdapter;
	OfflineHelper offlineHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setMainView(R.layout.offline_assistant_activity);
		setTitleText(getResources().getString(R.string.offline_assistant));
		showTitleBack();
		listView = (ListView) findViewById(R.id.data_list);
		offlineHelper = new OfflineHelper(this);
		listPay = offlineHelper.getOfflineTransaction();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		offlineTransDataAdapter = new OfflineTransDataAdapter();
		offlineTransDataAdapter.inflater = inflater;
		offlineTransDataAdapter.list = listPay;
		listView.setAdapter(offlineTransDataAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.popup_window, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		final boolean net = Constants.TRUE.equals(AppStateManager.getState(AppStateDef.isOffline));
		if (id == R.id.btn_sync) {
			update(net);
		} else if(id==R.id.btn_offline){
			upload(net);
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 更新
	 * @param net
	 */
	private void update(final boolean net) {
		if (net) {
			UIHelper.ToastMessage(OfflineHelperActivity.this, "请检查网络");
			return;
		}
		progresser.showProgress();
		offlineHelper.update(new ResultListener() {
			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(OfflineHelperActivity.this, response.msg);
			}

			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(OfflineHelperActivity.this, response.msg);
			}
		});
	}

	/**
	 * 上送
	 * @param net
	 */
	private void upload(final boolean net) {
		if (net) {
			UIHelper.ToastMessage(OfflineHelperActivity.this, "请检查网络");
			return ;
		}
		progresser.showProgress();
		offlineHelper.upload(new ResultListener() {
			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
				// UIHelper.ToastMessage(OfflineAssistantActivity.this, response.msg);
				UIHelper.ToastMessage(OfflineHelperActivity.this, response.msg);
				listPay.removeAll(listPay);
				offlineTransDataAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFaild(Response response) {
				progresser.showContent();
				UIHelper.ToastMessage(OfflineHelperActivity.this, response.msg);
			}
		});
	}

}
