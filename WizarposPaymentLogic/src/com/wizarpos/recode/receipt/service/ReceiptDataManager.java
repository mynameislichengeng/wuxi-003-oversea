package com.wizarpos.recode.receipt.service;

import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.recode.receipt.constants.ReceiptBarcodeStatusEnum;

public class ReceiptDataManager {

    private final static String BARCODE_KEY = "barcode_status";

    public static void settingBarcodeStatus(ReceiptBarcodeStatusEnum statusEnum) {
        AppConfigHelper.setConfig(BARCODE_KEY, statusEnum.getStatus());
    }

    public static String gettingBarcodeStatus() {
        return AppConfigHelper.getConfig(BARCODE_KEY, ReceiptBarcodeStatusEnum.CLOSE.getStatus());
    }


}
