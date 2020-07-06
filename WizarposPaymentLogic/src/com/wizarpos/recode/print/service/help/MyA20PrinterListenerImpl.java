package com.wizarpos.recode.print.service.help;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.motion.libsa920sdk.printer.local.PaxPrinterLocalManager;
import com.pax.poslink.peripheries.POSLinkPrinter;
import com.pax.poslink.peripheries.ProcessResult;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.recode.print.constants.PrintConstants;
import com.wizarpos.recode.receipt.service.ReceiptDataManager;
import com.wizarpos.recode.zxing.ZxingQRcodeManager;
import com.wizarpos.recode.zxing.bean.QRCodeParam;

import java.util.List;

public class MyA20PrinterListenerImpl implements POSLinkPrinter.PrintListener {

    private final String TAG = MyA20PrinterListenerImpl.class.getSimpleName();

    public List<PrintConstants.PartityItem> mReList;
    int postion;

    public MyA20PrinterListenerImpl(List<PrintConstants.PartityItem> mReList) {
        this.mReList = mReList;
    }


    @Override
    public void onSuccess() {
        log("A920打印完成onSuccess()");
        print();
    }

    @Override
    public void onError(ProcessResult processResult) {
        log("A920打印完成onError()");
        print();
    }

    public void print() {
        if (postion == mReList.size()) {
            return;
        }
        PrintConstants.PartityItem item = mReList.get(postion);
        postion++;
        Context context = PaymentApplication.getInstance().getApplicationContext();
        String prCon = item.getContent();
        PrintConstants.PrintContentPartity type = item.getType();
        switch (type) {
            case QC: {
                int width = 250;
                int height = 250;
                int left = (POSLinkPrinter.RecommendWidth.A920_RECOMMEND_WIDTH - width) / 2;
                Bitmap qrbitmap = null;
                if (isOpenBarCodeStatus()) {
                    QRCodeParam qrCodeParam = QRCodeParam.createImgQRCode(prCon, left, width, height);
                    qrbitmap = ZxingQRcodeManager.createOnlyImg(qrCodeParam);
                } else {
                    QRCodeParam.BottomText bottomText = QRCodeParam.createBottomText(prCon, 22, 20);
                    QRCodeParam qrCodeParam = QRCodeParam.createImgAndBottomTextQRCode(prCon, left, width, height, bottomText);
                    qrCodeParam.setPicHeight(275);
                    qrbitmap = ZxingQRcodeManager.createImageAndBottomText(qrCodeParam);
                }
                PaxPrinterLocalManager.getInstance(context).printerBitmap(qrbitmap, this);
            }

            break;
            case BC: {
                int width = 380;
                int height = 60;
                int left = (POSLinkPrinter.RecommendWidth.A920_RECOMMEND_WIDTH - width) / 2;
                QRCodeParam.BottomText bottomText = QRCodeParam.createBottomText(prCon, 22, 25);
                QRCodeParam qrCodeParam = QRCodeParam.createImgAndBottomTextBARCode(prCon, left, width, height, bottomText);
                qrCodeParam.setPicHeight(85);
                Bitmap bitmap = ZxingQRcodeManager.createImageAndBottomText(qrCodeParam);
                PaxPrinterLocalManager.getInstance(context).printerBitmap(bitmap, this);
            }

            break;
            default:
                PaxPrinterLocalManager.getInstance(context).settingPrintWidth(POSLinkPrinter.RecommendWidth.A920_RECOMMEND_WIDTH);
                PaxPrinterLocalManager.getInstance(context).printerText(prCon, this);
                break;
        }

    }


    protected static boolean isOpenBarCodeStatus() {
        return ReceiptDataManager.isOpenBarcodeStatus();
    }

    private void log(String msg) {
        Log.d("print", TAG + ">>" + msg);
    }
}
