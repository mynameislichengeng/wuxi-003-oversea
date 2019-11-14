package com.wizarpos.pay.cashier.merchant_apply;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.merchant_apply.base.MerchantApplyConstants;
import com.wizarpos.pay.cashier.merchant_apply.entity.MerchantApplyChangePwdRequest;
import com.wizarpos.pay.cashier.merchant_apply.entity.MerchantApplyRequest;
import com.wizarpos.pay.cashier.merchant_apply.entity.MobileMerchantBindRequest;
import com.wizarpos.pay.cashier.merchant_apply.util.UploadFileController;
import com.wizarpos.pay.cashier.merchant_apply.util.UploadFileController.ImageType;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.base.net.NetRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-11-30 上午10:40:34
 * @Description:商户进件请求管理类 
 */
public class MerchantApplyNetMgr {

	private static MerchantApplyNetMgr applyNetMgr;
	
	private MerchantApplyRequest applyBean;//商户进件注册
	
	private MerchantApplyChangePwdRequest applyChangePwdBean;//修改(初始)密码实体
	
	public static MerchantApplyNetMgr getInstants()
	{
		if(applyNetMgr == null)
			applyNetMgr = new MerchantApplyNetMgr();
		return applyNetMgr;
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-27 下午3:45:06  
	 * @Description:初始化数据,在重新调用前需要调用此方法
	 */
	public void initData()
	{
		this.applyChangePwdBean = null;
		this.applyBean = null;
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-27 下午3:45:06  
	 * @Description:新商户申请(修改)
	 */
	public void merchantApplyRegister(final ResponseListener listener)
	{
		UploadFileController uploadFileController = new UploadFileController();//上传图片获得图片服务器地址
		String bizLicenseImgUrl = MerchantApplyNetMgr.getInstants().getApplyBean().getBizLicenseImgUrl();
		String corpIdentAbvImgUrl = MerchantApplyNetMgr.getInstants().getApplyBean().getCorpIdentAbvImgUrl();
		String corpIdentObvImgUrl = MerchantApplyNetMgr.getInstants().getApplyBean().getCorpIdentObvImgUrl();
		Map<ImageType, String> imageMap = new HashMap<>();
		if(!bizLicenseImgUrl.contains("http://image"))
		{//若不是服务端返回的图片 则上传到服务器
			imageMap.put(ImageType.TYPE_IMAGE_BIZ_LICENSE, bizLicenseImgUrl);
		}
		if(!corpIdentAbvImgUrl.contains("http://image"))
		{
			imageMap.put(ImageType.TYPE_IMAGE_CORP_IDENT, corpIdentAbvImgUrl);
		}
		if(!corpIdentObvImgUrl.contains("http://image"))
		{
			imageMap.put(ImageType.TYPE_IMAGE_CORP_IDENT_OBV, corpIdentObvImgUrl);
		}
		if(imageMap.size() ==0)
		{//不需要重新上传图片(重新填写的时候没有修改图片)
			applyBean.setImei(PaymentApplication.getInstance().getImei());
			merchantApplyNetRequest(listener);
			return;
		}
		uploadFileController.uploadFile(imageMap, new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				Map<ImageType, String> imageMap = (Map<ImageType, String>) response.getResult();
				if(imageMap.containsKey(ImageType.TYPE_IMAGE_BIZ_LICENSE))
				{
					MerchantApplyNetMgr.getInstants().getApplyBean().setBizLicenseImgUrl(imageMap.get(ImageType.TYPE_IMAGE_BIZ_LICENSE));
				}
				if(imageMap.containsKey(ImageType.TYPE_IMAGE_CORP_IDENT))
				{
					MerchantApplyNetMgr.getInstants().getApplyBean().setCorpIdentAbvImgUrl(imageMap.get(ImageType.TYPE_IMAGE_CORP_IDENT));
				}
				if(imageMap.containsKey(ImageType.TYPE_IMAGE_CORP_IDENT_OBV))
				{
					MerchantApplyNetMgr.getInstants().getApplyBean().setCorpIdentObvImgUrl(imageMap.get(ImageType.TYPE_IMAGE_CORP_IDENT_OBV));
				}
				applyBean.setImei(PaymentApplication.getInstance().getImei());
				merchantApplyNetRequest(listener);
			}
			
			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}
	
	private void merchantApplyNetRequest(final ResponseListener listener)
	{
		NetRequest.getInstance().addRequest(Constants.SC_111_MERCHANT_APPLY, this.applyBean, new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				listener.onSuccess(response);
			}
			
			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-27 下午3:39:13  
	 * @Description:商户查询(注册前验证)
	 */
	public void merchantApplyQuery(String IMEI,final ResponseListener listener)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("imei", IMEI);
		params.put("MerchantBankApplyFlag", "1");// 商户进件标示 1:用于商户进件 
		NetRequest.getInstance().addRequest(Constants.SC_114_PRE_MERCHANT_APPLY, params, new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				if(response.getResult() == null)
				{
					listener.onSuccess(response);
				}else
				{
					JSONObject jsonObject = JSON.parseObject(response.getResult().toString());
					int status = jsonObject.getIntValue("status");
					//TODO 处理status151
					if(status == MerchantApplyConstants.STATUS_NOTIFY)
					{
						JSONObject jsonApply = jsonObject.getJSONObject("merchantBankApply");
						MerchantApplyRequest requestBean = JSONObject.parseObject(jsonApply.toJSONString(), MerchantApplyRequest.class);
						response.setResult(requestBean);
						String msg = jsonObject.getString("msg");
						response.setMsg(msg);
						listener.onSuccess(response);
					}
				}
			}
			
			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-30 下午3:44:41 
	 * @param listener 
	 * @Description:商户查询(绑定前验证 code返回157 已经绑定商户 是否解绑加入新商户)
	 */
	public void merchantApplyBindQuery(final ResponseListener listener)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("imei", PaymentApplication.getInstance().getImei());
		params.put("MerchantBankApplyFlag", "1");// 商户进件标示 1:用于商户进件 
		NetRequest.getInstance().addRequest(Constants.SC_117_PRE_MERCHANT_BLIND, params, new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				listener.onSuccess(response);
			}
			
			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-27 下午3:45:06  
	 * @Description:新商户申请(修改)
	 */
	public void merchantApplyRegister(MerchantApplyRequest bean, final ResponseListener listener)
	{
		NetRequest.getInstance().addRequest(Constants.SC_111_MERCHANT_APPLY, bean, new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				listener.onSuccess(response);
			}
			
			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}
	
	
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-27 下午4:28:42 
	 * @param listener 
	 * @Description:绑定设备号
	 */
	public void merchantApplyDeviceBind(MobileMerchantBindRequest bean,final ResponseListener listener)
	{
		NetRequest.getInstance().addRequest(Constants.SC_112_MOBILE_MERCHANT_BLIND, bean, new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				listener.onSuccess(response);
			}
			
			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-30 上午10:04:29  
	 * @Description:验证码验证
	 */
	public void merchantVerify(String mid,String bizTel,String vcode,final ResponseListener listener)
	{
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("bizTel", bizTel);
		params.put("vcode", vcode);
		params.put("mid", mid);
		params.put("MerchantBankApplyFlag", "1");// 商户进件标示 1:用于商户进件 
		NetRequest.getInstance().addRequest(Constants.SC_116_MERCHANT_VCODE, params, new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				listener.onSuccess(response);
			}
			
			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-30 上午10:54:07 
	 * @param listener 
	 * @Description:修改密码 初始化密码
	 */
	public void merchantChangePwd(final ResponseListener listener)
	{
		NetRequest.getInstance().addRequest(Constants.SC_115_MERCHANT_PASSWD_MOBILE, this.applyChangePwdBean, new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				listener.onSuccess(response);
			}
			
			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}
	
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-30 下午5:47:32 
	 * @param mid
	 * @param bizTel
	 * @param listener 
	 * @Description:短信验证码
	 */
	public void sendMsgCode(String mid,String bizTel,final ResponseListener listener)
	{
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("mid", mid);
		params.put("bizTel", bizTel);
		/**模板类型:1线上会员注册2商户进阶验证3商户进阶成功短信4商户进阶失败短信5会员密码变更6商户提现审核通过短信7商户提现审核拒绝短信*/
		params.put("applyType", "2");
		params.put("MerchantBankApplyFlag", "1");// 商户进件标示 1:用于商户进件 
		NetRequest.getInstance().addRequest(Constants.SC_704_CL_SEND_MSG_CODE, params, new ResponseListener() {
			
			@Override
			public void onSuccess(Response response) {
				listener.onSuccess(response);
			}
			
			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}
	


	public MerchantApplyRequest getApplyBean() {
		if(applyBean == null)
			applyBean = new MerchantApplyRequest();
		return applyBean;
	}

	public void setApplyBean(MerchantApplyRequest applyBean) {
		this.applyBean = applyBean;
	}
	

	public boolean isApplyBeanNull()
	{
		return applyBean == null;
	}
	
	
	
	public MerchantApplyChangePwdRequest getApplyChangePwdBean() {
		if(applyChangePwdBean == null)
			applyChangePwdBean = new MerchantApplyChangePwdRequest();
		return applyChangePwdBean;
	}

	public void setApplyChangePwdBean(
			MerchantApplyChangePwdRequest applyChangePwdBean) {
		this.applyChangePwdBean = applyChangePwdBean;
	}

	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-30 上午10:52:46  
	 * @Description:这边初始化对象
	 */
	public void onDestroy()
	{
		this.applyChangePwdBean = null;
		this.applyBean = null;
	}
	
}
