package com.wizarpos.pay.recode.sale.service;

import android.content.Context;

import com.wizarpos.pay.recode.sale.callback.InvoiceUIClickListener;

public interface InvoiceService {

    /**
     * 设置用户的Invoice
     */
    void settingInvoiceCallback(Context context, InvoiceUIClickListener invoiceUIClickListener);

    /**
     * 获得用户的Invoice
     */
    String gettingInvoice(Context context);

     boolean validateInvoice(Context context);

    void clearInvoiceValue();




}
