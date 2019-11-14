package com.wizarpos.pay.cashier.presenter.transaction.inf;

public interface OtherPayTransaction extends Transaction {

	public void setMark(String mark);
	
	public void setServiceName(String serviceName);
	
	public void setServiceId(String serviceId);
	
	public void setRound(String round);
	
}