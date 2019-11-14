package com.wizarpos.pay.cashier.pay_tems.bat.inf;

import com.wizarpos.pay.cashier.pay_tems.bat.BatTransation;
import com.wizarpos.pay.cashier.presenter.transaction.inf.OnlinePaymentNativeTransaction;
import com.wizarpos.pay.common.base.BasePresenter;

public interface BatCommonTransaction extends OnlinePaymentNativeTransaction {
    void commonPay(String flag, String payChannel, String authCode, BasePresenter.ResultListener resultListener);

    void setPrintListener(BatTransation.PrintListener printListener);

    void continuePrint();

    void cancelPrint();
}
