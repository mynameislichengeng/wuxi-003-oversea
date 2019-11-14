package com.wizarpos.cashier.aidl;

interface ICashierService {
	void go(int pageIndex);
	String getPOSInfo();
	String transact(String msg);
	String transactMember(String msg);
	String transactRegisterPartner(String msg);
	String membertransact(String msg);
	String alllogin(String msg);
	String goshift(String msg);
	String goshiftpay(String msg);
	String islogin();
	String transact_sem(String msg);
}





