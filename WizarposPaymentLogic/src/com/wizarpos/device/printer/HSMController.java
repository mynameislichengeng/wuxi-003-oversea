package com.wizarpos.device.printer;

import android.util.Log;

import com.wizarpos.devices.jniinterface.HSMInterface;

public class HSMController {
    public static final String APP_TAG = "CRT";
    public static final String PUB_CERT_AILAS = "terminal_wizarposagent";
    private static HSMController instance;

    private HSMController() {
    }

    public static HSMController getInstance() {
        if(instance == null) {
            instance = new HSMController();
        }

        return instance;
    }

    public boolean open() {
        int result = HSMInterface.open();
        Log.d("CRT", "invoke open method , result = " + result);
        return result >= 0;
    }

    public boolean close() {
        int result = HSMInterface.close();
        Log.d("CRT", "invoke close method , result = " + result);
        return result >= 0;
    }

    public byte[] getPubCertificate(String ailas) {
        boolean isSuccess = this.open();
        if(!isSuccess) {
            return null;
        } else {
            byte[] buf = new byte[8196];
            int result = HSMInterface.getCertificate(2, ailas.toString(), buf, buf.length, 0);
            this.close();
            if(result < 0) {
                Log.e("CRT", "getCertificate result is " + result);
                return null;
            } else {
                byte[] tempBuf = new byte[result];
                System.arraycopy(buf, 0, tempBuf, 0, result);
                return tempBuf;
            }
        }
    }

    public String queryCert() {
        Log.d("CRT", "invoke queryCert method !");
        String msg = "";
        int result = HSMInterface.queryCertCount(1);
        msg = msg + "CERT_TYPE_OWNER result = " + result;
        result = HSMInterface.queryCertCount(3);
        msg = msg + "\nCERT_TYPE_APP_ROOT result = " + result;
        result = HSMInterface.queryCertCount(4);
        msg = msg + "\nCERT_TYPE_COMMUNICATE result = " + result;
        result = HSMInterface.queryCertCount(2);
        msg = msg + "\nCERT_TYPE_PUBLIC_KEY result = " + result;
        result = HSMInterface.queryPrivateKeyCount();
        msg = msg + "\nqueryPrivateKeyCount result = " + result;
        byte[] privateKey = new byte[512];
        result = HSMInterface.queryPrivateKeyLabels(privateKey, 512);
        if(result > 0) {
            msg = msg + "\n" + (new String(privateKey)).substring(0, result);
        }

        Log.d("CRT", "invoke nqueryPrivateKeyCount method , result = " + result);
        byte[] data_test1 = new byte[512];
        result = HSMInterface.queryCertLabels(5, data_test1, 512);
        if(result > 0) {
            String allLabels = "\n" + (new String(data_test1)).substring(0, result);
            msg = msg + allLabels;
        }

        Log.d("CRT", "invoke queryCertLabels method , result = " + result);
        Log.d("CRT", "invoke queryCert method ! " + msg);
        return msg;
    }

    public byte[] encrypt(String alias, byte[] bufPlain) {
        boolean result = true;
        byte[] resultData = new byte[256];
        int result1 = HSMInterface.doRSAEncrypt(alias, bufPlain, resultData, resultData.length);
        if(result1 > 0) {
            byte[] tempData = new byte[result1];
            System.arraycopy(resultData, 0, tempData, 0, result1);
            return tempData;
        } else {
            return null;
        }
    }

    public byte[] decrypt(String alias, byte[] bufPlain) {
        boolean result = true;
        byte[] resultData = new byte[256];
        int result1 = HSMInterface.doRSADecrypt(alias, bufPlain, resultData, resultData.length);
        if(result1 > 0) {
            byte[] tempData = new byte[result1];
            System.arraycopy(resultData, 0, tempData, 0, result1);
            return tempData;
        } else {
            return null;
        }
    }
}