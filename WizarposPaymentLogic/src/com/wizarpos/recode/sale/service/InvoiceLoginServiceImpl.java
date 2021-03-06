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


    /**
     * 从后台服务后的，账号是否需要invoice标记
     *
     * @param flag
     */
    public void setAppconfifMandatoryFlag(String flag) {
        AppConfigHelper.setConfig(AppConfigDef.mandatoryFlag, flag);//invoice码是否强制输入(0 关闭 ，1开启)

    }

    protected String getAppconfigMandatoryFlag() {
        return AppConfigHelper.getConfig(AppConfigDef.mandatoryFlag, "");//invoice码是否强制输入(0 关闭 ，1开启)
    }
}
