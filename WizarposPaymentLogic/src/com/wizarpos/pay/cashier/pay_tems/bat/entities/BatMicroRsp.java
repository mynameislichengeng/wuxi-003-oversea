package com.wizarpos.pay.cashier.pay_tems.bat.entities;

import java.io.Serializable;
import java.util.List;

import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.pay_tems.bat.BatMicroTransactionImpl;

/**
 * 
 * @author hong
 *
 */
public class BatMicroRsp implements Serializable{
	public static final String CODE_WAITE_ALIPAY = "ORDER_SUCCESS_PAY_INPROCESS";
	public static final String CODE_WAITE_TENPAY = "66227005";//需支付密码
	public static final String CODE_WAITE_WEPAY = "USERPAYING";
	public static final String CODE_WAITE_BAIDU = "69556";
	public static final String CODE_SUCCESS = "0";
	/** {@code BatMicroTransactionImpl parseMicroUnion}方法里用到*/
	public static final int ERROR_CODE = 999;//需要支付密码,移动支付修改 @hwc
//	private List<TicketInfo> tList;
	private String ret;
	private String msg;
	private String thirdTradeNo;
	private String err_code;
	private String payType;//移动支付的支付类型 wu

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getThirdTradeNo() {
		return thirdTradeNo;
	}
	public void setThirdTradeNo(String thirdTradeNo) {
		this.thirdTradeNo = thirdTradeNo;
	}
	public String getErr_code() {
		return err_code;
	}
	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}
	
//	public List<TicketInfo> gettList() {
//		return tList;
//	}
//	public void settList(List<TicketInfo> tList) {
//		this.tList = tList;
//	}
//	
}
