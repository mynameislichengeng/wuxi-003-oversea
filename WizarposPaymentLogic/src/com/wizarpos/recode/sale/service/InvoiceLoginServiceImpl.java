package com.wizarpos.recode.sale.service;

import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

public class InvoiceLoginServiceImpl {

    private static InvoiceLoginServiceImpl invoiceLoginService = new InvoiceLoginServiceImpl();

    public static InvoiceLoginServiceImpl getInstatnce() {
        return invoiceLoginService;
    }

    protected void setAppconfigInvoiceValue(String invoiceValue) {
        AppConfigHelper.setConfig(AppConfigDef.invoicenum, invoiceValue);
    }

    public String getAppconfigInvoiceValue() {
        return AppConfigHelper.getConfig(AppConfigDef.invoicenum, "");
    }

    protected String getAppconfigMandatoryFlag() {
        return AppConfigHelper.getConfig(AppConfigDef.mandatoryFlag, "");//invoice码是否强制输入(0 关闭 ，1开启)
    }
}
