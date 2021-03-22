package com.wizarpos.pay.recode.zusao.connect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.wizarpos.pay.recode.zusao.bean.connect.ZsConnectIntentBeanReq;
import com.wizarpos.pay.recode.zusao.constants.ZsConstants;
import com.wizarpos.pay.recode.zusao.constants.ZsSettingEnum;
import com.wizarpos.pay.recode.zusao.sp.ZSSettingManager;

public class ZsConnectManager {
    private final static String TAG = ZsConnectManager.class.getSimpleName();

    public static void init(Intent intent) {
        ZSSettingManager.clearScanCodePayType();
        ZSSettingManager.clearConnectJson();
        if (intent == null) {
            Log.d("tagtagtag", TAG + " init intent = null");
            return;
        }
        String json = intent.getStringExtra(ZsConstants.INTENT_CONNECT_KEY);
        if (TextUtils.isEmpty(json)) {
            Log.d("tagtagtag", TAG + " init intent json = null");
            return;
        }
        //设置相关的扫码方式
        ZSSettingManager.setScanCodePayType(ZsSettingEnum.ACTIVELY);
        //设置内容
        ZSSettingManager.setConnectJson(json);
    }

    /**
     * 是否是主动扫码
     *
     * @return
     */
    public static boolean isZsPayType() {
        return ZSSettingManager.getScanCodePayType().equals(ZsSettingEnum.ACTIVELY.getType());
    }

    public static ZsConnectIntentBeanReq getConnectIntentBeanParam() {
        return JSON.parseObject(ZSSettingManager.getConnectJson(), ZsConnectIntentBeanReq.class);
    }







    public static void onActivityMyselfIntentResult(Activity activity, Intent intent) {
        String json = intent.getStringExtra(ZsConstants.INTENT_MYSELF_KEY);
        //
        resultIntentForStart(activity, json);
    }

    public static void resultIntentForStart(Activity activity, String json) {
        Intent intent = new Intent();
        intent.putExtra(ZsConstants.INTENT_MYSELF_KEY, json);
        activity.setResult(ZsConstants.INTENT_MYSELF_RESULT_CODE, intent);
        activity.finish();
    }

    public static void requestIntent(Context context, Intent intent) {
        ((Activity) context).startActivityForResult(intent, ZsConstants.INTENT_MYSELF_REQUEST_CODE);
    }
    public static void requestIntent(Context context,Class cls){
        Intent intent = new Intent();
        intent.setClass(context, cls);
        requestIntent(context, intent);
    }

    public static void connectIntentResult(Activity context){
        Intent intent = new Intent();
        intent.putExtra("MotionPayTransactionStatus", "RESULTCODE_OK");
        context.setResult(Activity.RESULT_OK,intent);
        context.finish();
    }

}
