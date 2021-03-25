package com.wizarpos.pay.recode.zusao.connect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.amobilepayment.android.tmslibrary.DownloadStatus;
import com.amobilepayment.android.tmslibrary.FirmwareUpdateStatus;
import com.amobilepayment.android.tmslibrary.IServerResponse;
import com.amobilepayment.android.tmslibrary.STATE;
import com.amobilepayment.android.tmslibrary.TMS;
import com.amobilepayment.android.tmslibrary.TMS_FILE_TYPE;
import com.lc.baseui.tools.data.SPUtils;
import com.wizarpos.pay.recode.zusao.bean.connect.ZsConnectIntentBeanReq;
import com.wizarpos.pay.recode.zusao.bean.connect.ZsConnectIntentBeanResp;
import com.wizarpos.pay.recode.zusao.constants.ZsConstants;
import com.wizarpos.pay.recode.zusao.constants.ZsSettingEnum;
import com.wizarpos.pay.recode.zusao.sp.ZSSettingManager;
import com.wizarpos.recode.data.info.SnManager;

import java.util.ArrayList;

public class ZsConnectManager {
    private final static String TAG = ZsConnectManager.class.getSimpleName();

    public static void init(Context context, Intent intent) {
//        TMS.getInstance().init(context, new ServerResponseCallback());
//        settingLibs(context);
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


    public static void intentSettingResultForSuccessOnActivity(Activity activity, Intent intent) {
        String json = intent.getStringExtra(ZsConstants.INTENT_MYSELF_KEY);
        //
        intentSettingResultForSuccessStart(activity, json);
    }

    public static void intentSettingResultForSuccessStart(Activity activity, String json) {
        Intent intent = new Intent();
        intent.putExtra(ZsConstants.INTENT_MYSELF_KEY, json);
        activity.setResult(ZsConstants.INTENT_MYSELF_RESULT_CODE, intent);
        activity.finish();
    }


    public static void starActivityForResultMethodSuccess(Context context, Class cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
//        starActivityForResultMethodSuccess(context, intent);
        ((Activity) context).startActivityForResult(intent, ZsConstants.INTENT_MYSELF_REQUEST_CODE);
    }

    public static void startActivityForResultMethodMiddle(Activity context, Class cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        context.startActivity(intent);
        context.finish();
    }

    public static void startActivityForResultMethodMiddle(Activity context, Intent intent) {

        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        context.startActivity(intent);
        context.finish();
    }

    /**
     * 返回成功结果  到connect
     *
     * @param context
     * @param jsonIntent
     */
    public static void connectIntentResultSuccess(Activity context, Intent jsonIntent) {
        String json = jsonIntent.getStringExtra(ZsConstants.INTENT_MYSELF_KEY);
        Log.d("tagtagtag", TAG + "connectIntentResultSuccess()--返回json:" + json);
        ZsConnectIntentBeanResp resp = ZsConnectIntentBeanResp.convertFormPay(json);

        Intent intent = new Intent();
        intent.putExtra("MotionPayTransactionStatus", "RESULT_OK");
        intent.putExtra("MotionPayTransactionPayload", JSON.toJSONString(resp));
        context.setResult(Activity.RESULT_OK, intent);
        context.finish();
    }

    public static void connectIntentResultCanceled(Activity context) {
        Intent intent = new Intent();
        intent.putExtra("MotionPayTransactionStatus", "RESULT_CANCELED");
        //todo
        context.setResult(Activity.RESULT_CANCELED, intent);
        context.finish();
    }


    public static void settingLibs(Context context) {
        String sn = SnManager.getSn(context);
//        TMS.getInstance().setConfigurationParameter(sn,
//                "SETTINGS.ALTPAYMENTAPP", "MOTIONPAY", "com.amobilepayment.android.ecrconnect");
//
//        String settings = (String) SPUtils.get(context, "sp_zs_lib_setting", "");
//        if (settings.equals("1")) {
//            Log.d("tagtagtag", "has setting:");
//            return;
//        }
//        SPUtils.put(context, "sp_zs_lib_setting", "1");
//
//        Log.d("tagtagtag", "sn:" + sn);


    }


}
