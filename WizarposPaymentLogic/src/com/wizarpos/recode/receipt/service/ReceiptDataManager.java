package com.wizarpos.recode.receipt.service;

import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.recode.receipt.constants.ReceiptStatusEnum;


public class ReceiptDataManager {

    private final static String BARCODE_KEY = "barcode_status";

    private final static String QRCODE_KEY = "qrcode_status";

    /**
     * 设置条形码状态
     *
     * @param statusEnum
     */
    public static void settingBarcodeStatus(ReceiptStatusEnum statusEnum) {
        AppConfigHelper.setConfig(BARCODE_KEY, statusEnum.getStatus());
    }

    /**
     * 获得条形码状态
     *
     * @return
     */
    public static String gettingBarcodeStatus() {
        return AppConfigHelper.getConfig(BARCODE_KEY, ReceiptStatusEnum.CLOSE.getStatus());
    }

    /**
     * 是否打开条形码
     *
     * @return
     */
    public static boolean isOpenBarcodeStatus() {
        String status = gettingBarcodeStatus();
        if (ReceiptStatusEnum.OPEN.getStatus().equals(status)) {
            return true;
        }
        return false;
    }


    /**
     * 设置二维码状态
     *
     * @param statusEnum
     */
    public static void settingQRCodeStatus(ReceiptStatusEnum statusEnum) {
        AppConfigHelper.setConfig(QRCODE_KEY, statusEnum.getStatus());
    }

    /**
     * 获得二维码状态
     *
     * @return
     */
    public static String gettingQRCodeStatus() {
        return AppConfigHelper.getConfig(QRCODE_KEY, ReceiptStatusEnum.CLOSE.getStatus());
    }

    /**
     * 是否打开二维码
     *
     * @return
     */
    public static boolean isOpenQRCodeStatus() {
        String status = gettingQRCodeStatus();
        if (ReceiptStatusEnum.OPEN.getStatus().equals(status)) {
            return true;
        }
        return false;
    }


}
