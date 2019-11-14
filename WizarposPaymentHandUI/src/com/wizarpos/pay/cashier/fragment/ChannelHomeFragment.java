package com.wizarpos.pay.cashier.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.activity.MixPayActivity;
import com.wizarpos.pay.cashier.activity.ThirdTicketCancleActivity;
import com.wizarpos.pay.cashier.activity.WepayTicketCancleActivity;
import com.wizarpos.pay.cashier.adapter.HomeFragmentAdapter;
import com.wizarpos.pay.cashier.adapter.HomeRecordsAdapter;
import com.wizarpos.pay.cashier.bean.BaseListDataBean;
import com.wizarpos.pay.cashier.fragment.HomeNavAmountFragment.OnUpdateUiListener;
import com.wizarpos.pay.cashier.view.GatheringActivity;
import com.wizarpos.pay.view.fragment.common.BaseViewFragment;
import com.wizarpos.pay2.lite.R;

/**
 * 收款fragment
 * 
 * @author wu
 */
public class ChannelHomeFragment extends BaseViewFragment implements OnPageChangeListener,OnUpdateUiListener{

	private ListView lvRecords;
	private View mainView;
	private Button btnPay;
	private Button btnTicketCancel;

	private HomeRecordsAdapter recordAdapter;
	/** viewpagerAdapter*/
	private HomeFragmentAdapter fragmentAdapter;
	private List<BaseListDataBean> dataList;
	private HomeNavFragment nvFragment;
	private ViewPager vpTitle;

	public ChannelHomeFragment() {
	}
	
	@Override
	public void initView() {
		mainView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, null);
		setMainView(mainView);
		lvRecords = (ListView) mainView.findViewById(R.id.lvRecords);
		btnPay = (Button) mainView.findViewById(R.id.btnPay);
		btnTicketCancel = (Button) mainView.findViewById(R.id.btnTicketCancel);
		vpTitle = (ViewPager)mainView.findViewById(R.id.vpTitle);
		setWidgetListener();
		initTestData();
	}
	
	private void setWidgetListener()
	{
		recordAdapter = new HomeRecordsAdapter(getActivity());
		lvRecords.setAdapter(recordAdapter);

		fragmentAdapter = new HomeFragmentAdapter(getFragmentManager(),this);
		vpTitle.addOnPageChangeListener(this);
		vpTitle.setOffscreenPageLimit(3);
		vpTitle.setAdapter(fragmentAdapter);
		vpTitle.setCurrentItem(0);
		
		btnPay.setOnClickListener(this);
		btnTicketCancel.setOnClickListener(this);
	}
	
	/**
	 * 
	 * @Author:Huangweicai
	 * @Date:2015-7-29 下午2:06:41
	 * @Reason:初始化测试数据
	 */
	private void initTestData()
	{
		dataList = new ArrayList<BaseListDataBean>();
//		BaseListDataBean bean = new BaseListDataBean();
//		bean.setDrableSouce(R.drawable.ic_launcher);
//		bean.setTitle("测试标题");
//		bean.setValue("0.01");
//		dataList.add(bean);
//		dataList.add(bean);
//		dataList.add(bean);
//		dataList.add(bean);
//		recordAdapter.setDataChanged(dataList);
	}
	

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnPay:// 支付按钮
			startActivity(new Intent(getActivity(), GatheringActivity.class));
			break;
		case R.id.btnTicketCancel:// 券核销
//			test();
			ticketCancel();
			break;

		default:
			break;
		}
	}
	

	/**
	 * 卡券核销
	 */
	protected void ticketCancel() {
		// thirdTicketCancel();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setCancelable(false);
		builder.setTitle("卡券核销");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("img", R.drawable.btn_selector_member_card_pay);
		map.put("title", getResources().getString(R.string.third_ticket_cancle));
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.btn_selector_wepay_card_pay);
		map.put("title", getResources().getString(R.string.wepay_ticket_cancle));
		list.add(map);
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.layout_single_choose, new String[] { "img", "title" }, new int[] { R.id.ivImg,
				R.id.tvTitle });
		builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which ==0){
					thirdTicketCancel();
				}else{
					wepayTicketCancel();
				}
				dialog.dismiss();
			}
		});
		builder.setCancelable(true);
		AlertDialog dialog = builder.create();
		try {
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 第三方卡券核销
	 */
	private void thirdTicketCancel() {
		Intent intent = new Intent(getActivity(), ThirdTicketCancleActivity.class);
		getActivity().startActivity(intent);
	}
	
	protected void wepayTicketCancel() {
		Intent intent = new Intent(getActivity(), WepayTicketCancleActivity.class);
		getActivity().startActivity(intent);
	}
	
	
	/**
	 * 
	 * @Author:Huangweicai
	 * @Date:2015-7-24 上午11:01:52
	 * @Reason:测试类
	 */
	private void test()
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), MixPayActivity.class);
		this.startActivity(intent);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		LogEx.i("TAG", "onPageSelected : " + position);
		BaseNavFragment fragment = fragmentAdapter.getItem(position);
		fragment.updateUI();
	}

	@Override
	public void onUpdate(List<BaseListDataBean> bean) {
		recordAdapter.setDataChanged(bean);
	}
	
	
	
}
