package com.wizarpos.pay.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.pay.cardlink.model.BankCardTransUploadReq;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.db.BankTransUploadDao;

import java.util.List;
import java.util.Map;

/**
 * 后台银行卡上送
 * Created by Song on 2016/9/27.
 */

public class BankTransUploadService extends Service {

    public static final String TAG = "BankTransUploadService";
    private int uploadedNum = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        doUploadTrans();
        return super.onStartCommand(intent, flags, startId);
    }

    private void doUploadTrans() {
        try {
            List<BankCardTransUploadReq> bankCardTransUploadReqs = BankTransUploadDao.getInstance().getAllTransUpload();
            final int size = ((null != bankCardTransUploadReqs) ? bankCardTransUploadReqs.size() : 0);
            Log.i(TAG, "开始上送银行卡交易信息... size:" + size);
            if (size == 0) {
                stopSelf();
                return;
            }
            uploadedNum = 0;
            for (final BankCardTransUploadReq req : bankCardTransUploadReqs) {
                Map<String, Object> params = req.getUploadMap();
                if (null != params) {
                    NetRequest.getInstance().addRequest(Constants.SC_700_BANK_CARD_PAY, params, new ResponseListener() {
                        @Override
                        public void onSuccess(Response response) {
                            Log.i(TAG, req.getToken() + "上送成功");
                            BankTransUploadDao.getInstance().deleteTransUpload(req);
                            uploadedNum ++;
                            if (uploadedNum >= size) {
                                stopSelf();
                            }
                        }

                        @Override
                        public void onFaild(Response response) {
                            Log.i(TAG, req.getToken() + "上送失败");
                            uploadedNum ++;
                            if (uploadedNum >= size) {
                                stopSelf();
                            }
                        }
                    });
                } else {
                    Log.i(TAG, req.getToken() + "上送失败,数据错误");
                    uploadedNum ++;
                    if (uploadedNum >= size) {
                        stopSelf();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}