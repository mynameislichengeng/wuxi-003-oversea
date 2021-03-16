package com.wizarpos.pay.recode.sale.service.impl;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.wizarpos.pay.recode.constants.SaleConstants;
import com.wizarpos.pay.recode.sale.callback.InvoiceUIClickListener;
import com.wizarpos.pay.recode.sale.service.InvoiceService;
import com.wizarpos.pay.recode.sale.widget.SaleInvoiceEditView;
import com.motionpay.pay2.lite.R;
import com.wizarpos.recode.sale.service.InvoiceLoginServiceImpl;


public class InvoiceServiceImpl extends InvoiceLoginServiceImpl implements InvoiceService {

    private static InvoiceService instance = new InvoiceServiceImpl();

    public static InvoiceService getInstance() {
        return instance;
    }

    /**
     * 设置invoice的值
     *
     * @param context
     */
    @Override
    public void settingInvoiceCallback(Context context, final InvoiceUIClickListener invoiceUIClickListener) {
        String invoice = getAppconfigInvoiceValue();
        SaleInvoiceEditView.show(context, invoice, new InvoiceUIClickListener() {
            @Override
            public void onClick(String invoice) {
                setAppconfigInvoiceValue(invoice);
                invoiceUIClickListener.onClick(invoice);
            }
        });

    }

    /**
     * 获得invoice的值
     *
     * @param context
     * @return
     */
    @Override
    public String gettingInvoice(Context context) {
        return getAppconfigInvoiceValue();

    }

    /**
     * 检查是否符合要求
     *
     * @param context
     * @return
     */
    @Override
    public boolean validateInvoice(Context context) {
        // 必须有
        boolean flag = isNeedInvoiceFlag();
        if (flag) {
            String invoiceStr = getAppconfigInvoiceValue();
            if (TextUtils.isEmpty(invoiceStr)) {
                Toast.makeText(context, R.string.please_invoice_input, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void clearInvoiceValue() {
        setAppconfigInvoiceValue("");
    }


    public boolean isNeedInvoiceFlag() {
        String str = getAppconfigMandatoryFlag();
        if (SaleConstants.InvoiceAuthority.NEED == SaleConstants.InvoiceAuthority.getAuthority(str)) {
            return true;
        }
        return false;
    }


}
