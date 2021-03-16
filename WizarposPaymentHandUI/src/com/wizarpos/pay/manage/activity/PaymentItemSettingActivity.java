package com.wizarpos.pay.manage.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;

import com.wizarpos.log.util.StringUtil;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.manage.entity.CheckDataBean;
import com.motionpay.pay2.lite.R;

/**
 * 支付方式选择
 */
public class PaymentItemSettingActivity extends BaseViewActivity implements View.OnClickListener {


    private Button btnSave;
    
    private List<CheckDataBean> checkList;
    
    
    private CheckDataBean bankBean = new CheckDataBean("银行卡",AppConfigDef.isSupportBankCard);
    private CheckDataBean memberBean = new CheckDataBean("会员卡",AppConfigDef.isSupportMemberCard);
    private CheckDataBean cashBean = new CheckDataBean("现金",AppConfigDef.isSupportCash);
    private CheckDataBean wepayBean = new CheckDataBean("微信",AppConfigDef.isSupportWepay);
    private CheckDataBean alipayBean = new CheckDataBean("支付宝",AppConfigDef.isSupportAlipay);
    private CheckDataBean tenpayBean = new CheckDataBean("QQ钱包",AppConfigDef.isSupportTenpay);
    private CheckDataBean baiduBean = new CheckDataBean("百度钱包",AppConfigDef.isSupportBaiduPay);
    private CheckDataBean unionpayBean = new CheckDataBean("移动支付支付",AppConfigDef.isSupportUnionPay);
    private CheckDataBean mixpayBean = new CheckDataBean("组合支付",AppConfigDef.isSupportMixPay);
    private CheckDataBean ticketCancelBean = new CheckDataBean("卡券核销",AppConfigDef.isSupportTicketCancel);
    private CheckDataBean otherpayBean = new CheckDataBean("其他支付",AppConfigDef.isSupportOhterPay);
    private GridView gvChoosePay;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainView(R.layout.activity_payment_item_setting);
        showTitleBack();
        setTitleText("支付方式选择");
        initChooseList();
        initData();
        initViews();
    }

    
    private void initChooseList()
    {
    		if(checkList == null)
    			checkList = new ArrayList<CheckDataBean>();
    		checkList.add(bankBean);
    		checkList.add(memberBean);
    		checkList.add(cashBean);
    		checkList.add(wepayBean);
    		checkList.add(alipayBean);
    		checkList.add(tenpayBean);
    		checkList.add(baiduBean);
    		checkList.add(unionpayBean);
    		checkList.add(mixpayBean);
    		checkList.add(ticketCancelBean);
    		checkList.add(otherpayBean);
    }

    private void initData() {
        if(!DeviceManager.getInstance().isSupportBankCard()){
            checkList.remove(bankBean);
        }


        ticketCancelBean.setChecked(doString2Boolean(AppConfigHelper.getConfig(AppConfigDef.isSupportTicketCancel)));
        mixpayBean.setChecked(doString2Boolean(AppConfigHelper.getConfig(AppConfigDef.isSupportMixPay)));
        baiduBean.setChecked(doString2Boolean(AppConfigHelper.getConfig(AppConfigDef.isSupportBaiduPay)));
        tenpayBean.setChecked(doString2Boolean(AppConfigHelper.getConfig(AppConfigDef.isSupportTenpay)));
        alipayBean.setChecked(doString2Boolean(AppConfigHelper.getConfig(AppConfigDef.isSupportAlipay)));
        wepayBean.setChecked(doString2Boolean(AppConfigHelper.getConfig(AppConfigDef.isSupportWepay)));
        cashBean.setChecked(doString2Boolean(AppConfigHelper.getConfig(AppConfigDef.isSupportCash)));
        memberBean.setChecked(doString2Boolean(AppConfigHelper.getConfig(AppConfigDef.isSupportMemberCard)));
        bankBean.setChecked(doString2Boolean(AppConfigHelper.getConfig(AppConfigDef.isSupportBankCard)));
        otherpayBean.setChecked(doString2Boolean(AppConfigHelper.getConfig(AppConfigDef.isSupportOhterPay)));
        unionpayBean.setChecked(doString2Boolean(AppConfigHelper.getConfig(AppConfigDef.isSupportUnionPay)));
    }

    private void initViews() {
        this.btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(this);
        
        gvChoosePay = (GridView) this.findViewById(R.id.gvChoosePay)	;
        gvChoosePay.setAdapter(new ChoosePayAdapter());
    }
    
    public class ChoosePayAdapter extends BaseAdapter
    {

		@Override
		public int getCount() {
			return checkList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			CheckDataBean bean = checkList.get(position);
			final CheckBox cb = new CheckBox(PaymentItemSettingActivity.this);
			cb.setEms(6);
			cb.setText(bean.getCheckName());
			cb.setChecked(bean.isChecked());
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					checkList.get(position).setChecked(isChecked);
				}
			});
			return cb;
		}
		
		public void setDataChanged()
		{
			
		}
    	
    }

    private boolean doString2Boolean(String str) {
        return Constants.TRUE.equals(str);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                save();
                break;
        }
    }

    private void save() {
    		boolean hasChoose = false;
        for(CheckDataBean bean : checkList)
        {
        		if(bean.isChecked() && !StringUtil.isSameString(bean.getConfigDef(),AppConfigDef.isSupportTicketCancel))
        		{
        			hasChoose = true;
        		}
        		AppConfigHelper.setConfig(bean.getConfigDef(), bean.isChecked() + "");
        }
        
        if(!hasChoose)
        {
        		UIHelper.ToastMessage(this, "至少选择一种支付方式" + (doString2Boolean(AppConfigHelper.getConfig(AppConfigDef.isSupportTicketCancel)) ? "(卡券核销除外)" : ""));
        		return;
        }
        this.finish();
    }

}
