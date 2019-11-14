package com.wizarpos.pay.login.merchant.input;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.app.Pay2Application;
import com.wizarpos.pay.cashier.merchant_apply.MerchantApplyNetMgr;
import com.wizarpos.pay.cashier.merchant_apply.base.MerchantApplyConstants;
import com.wizarpos.pay.cashier.merchant_apply.entity.MerchantApplyRequest;
import com.wizarpos.pay.login.util.DigitsTextWatcher;
import com.wizarpos.pay.login.util.RegexUtil;
import com.wizarpos.pay.view.fragment.common.BaseViewFragment;
import com.wizarpos.pay2.lite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yaosong
 * @date 2015-11-26 下午2:03:05
 * @Description:	商户信息
 */
public class NewMerchantFragment extends BaseViewFragment implements OnClickListener{

	private EditText et_merchant_name,et_merchant_alias,et_contact_person,et_email,et_telephone;

	private String merchantName,merchantShortName,bizLinker,bizEmail,bizTel;
	
	private MerchantApplyRequest applyBean;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		checkNotify();
	}

	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-1 上午10:44:27  
	 * @Description:是否是修改数据,取决于服务端的 {@link MerchantApplyConstants #STATUS_NOTIFY} 状态
	 */
	private void checkNotify() 
	{
		if(MerchantApplyNetMgr.getInstants().isApplyBeanNull() || mainView == null)
		{
			return;
		}
		
		applyBean = MerchantApplyNetMgr.getInstants().getApplyBean();
		et_merchant_name.setText(applyBean.getMerchantManagementName());
		et_merchant_alias.setText(applyBean.getMerchantShortName());
		et_contact_person.setText(applyBean.getBizLinker());
		et_email.setText(applyBean.getBizEmail());
		et_telephone.setText(applyBean.getBizTel());
	}

	public void initView() {
		setTitleText("商户信息");
		setMainView(R.layout.activity_new_merchant);
		mainView.findViewById(R.id.btn_next).setOnClickListener(this);
		mainView.findViewById(R.id.btn_cancle).setOnClickListener(this);
		et_merchant_name = (EditText) mainView.findViewById(R.id.et_merchant_name);
		et_merchant_alias = (EditText) mainView.findViewById(R.id.et_merchant_alias);
		et_contact_person = (EditText) mainView.findViewById(R.id.et_contact_person);
		et_email = (EditText) mainView.findViewById(R.id.et_email);
		et_telephone = (EditText) mainView.findViewById(R.id.et_telephone);
		et_merchant_name.addTextChangedListener(new DigitsTextWatcher(et_merchant_name));
		et_merchant_alias.addTextChangedListener(new DigitsTextWatcher(et_merchant_alias));
		et_contact_person.addTextChangedListener(new DigitsTextWatcher(et_contact_person));
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.btn_next:
//			testUpload();
			if(matchInfo())
			{
				((NewMerChantFillFormActivity)getActivity()).toFragmentAndSave(new SettingsFragment(),NewMerChantFillFormActivity.NEWMERCHANT_FRAGMENT_FLAG);
//				startActivity(new Intent(this, UploadInformationActivity.class));
//				finish();
//				doSubmit();
			}
			break;
		case R.id.btn_cancle:
			getActivity().finish();
//			finish();
			break;
		}
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-1 下午4:57:48  
	 * @Description:测试上传图片
	 */
//	private void testUpload()
//	{
//		progresser.showProgress();
//		UploadFileController control = new UploadFileController();
//		File file1 = new File(Environment.getExternalStorageDirectory() + File.separator + "test.jpg");
//		File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "test1.jpg");
//		File file3 = new File(Environment.getExternalStorageDirectory() + File.separator + "test2.jpg");
//		File[] fileList = new File[3];
//		fileList[0] = file1;
//		fileList[1] = file2;
//		fileList[2] = file3;
//		control.uploadFile(fileList, new ResponseListener() {
//			
//			@Override
//			public void onSuccess(Response response) {
//				@SuppressWarnings("unchecked")
//				List<String> dataList = (List<String>) response.getResult();
//				for(String data:dataList)
//				{
//					System.out.println(data + "---------");
//				}
//				progresser.showContent();
//			}
//			
//			@Override
//			public void onFaild(Response response) {
//				progresser.showContent();
//			}
//		});
//	}
	
	
	private void doSubmit()
	{
		List<String> payRange = new ArrayList<String>();
		payRange.add("A");
		payRange.add("C");
		MerchantApplyRequest bean = new MerchantApplyRequest();
		bean.setBizEmail("327253591@qq.com");
		bean.setBizLicenseImgUrl("http://image.wizarpos.com/MerchantApply/20151118151919.jpg");
		bean.setBizLinker("联系人");
		bean.setBizTel("13205244957");
		bean.setCorpIdentAbvImgUrl("http://image.wizarpos.com/MerchantApply/20151118151919.jpg");
		bean.setCorpIdentObvImgUrl("http://image.wizarpos.com/MerchantApply/20151118151919.jpg");
		bean.setMerchantManagementName("测试账户名称");
		bean.setMerchantRegisterName("测试账户简称");
		bean.setMerchantShortName(merchantShortName);
		bean.setOpenBankAccount("中国银行");
		bean.setOpenBankNo("11111111111111111");
		bean.setThirdPayRange(payRange.toString());
		bean.setThirdPayType("");
		bean.setImei(Pay2Application.getInstance().getImei());
		MerchantApplyNetMgr.getInstants().setApplyBean(bean);
		progresser.showProgress();
		MerchantApplyNetMgr.getInstants().merchantApplyRegister(new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				progresser.showContent();
			}
			
			@Override
			public void onFaild(Response response) {
				progresser.showContent();
			}
		});
	}

	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-30 上午11:14:08 
	 * @return 
	 * @Description:验证格式
	 */
	private boolean matchInfo()
	{
		merchantName = et_merchant_name.getText().toString();
		merchantShortName = et_merchant_alias.getText().toString();
		bizLinker = et_contact_person.getText().toString();
		bizEmail = et_email.getText().toString();
		bizTel = et_telephone.getText().toString();
		if(TextUtils.isEmpty(merchantName) || TextUtils.isEmpty(merchantShortName) || TextUtils.isEmpty(bizLinker)
				|| TextUtils.isEmpty(bizEmail) || TextUtils.isEmpty(bizTel))
		{
			Toast.makeText(getActivity(), "内容不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!RegexUtil.isEmail(bizEmail))
		{
			Toast.makeText(getActivity(), "邮箱格式不正确", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!RegexUtil.isMobile(bizTel))
		{
			Toast.makeText(getActivity(), "手机格式不正确", Toast.LENGTH_SHORT).show();
			return false;
		}
		MerchantApplyNetMgr.getInstants().getApplyBean().setMerchantManagementName(merchantName);
		MerchantApplyNetMgr.getInstants().getApplyBean().setMerchantShortName(merchantShortName);
		MerchantApplyNetMgr.getInstants().getApplyBean().setBizLinker(bizLinker);
		MerchantApplyNetMgr.getInstants().getApplyBean().setBizEmail(bizEmail);
		MerchantApplyNetMgr.getInstants().getApplyBean().setBizTel(bizTel);
		MerchantApplyNetMgr.getInstants().getApplyBean().setImei(Pay2Application.getInstance().getImei());
		return true;
	}

}
