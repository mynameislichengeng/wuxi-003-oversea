package com.wizarpos.recode.activi.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.log.util.PreferenceHelper;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.AppInfoUtils;
import com.wizarpos.pay.common.utils.MD5Util;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.model.LoginResp;
import com.wizarpos.pay.model.LoginedMerchant;
import com.wizarpos.recode.constants.HttpConstants;
import com.wizarpos.recode.data.info.MidConfigManager;
import com.wizarpos.recode.data.info.RefundRelationMidsManager;
import com.wizarpos.recode.data.info.SnManager;
import com.wizarpos.recode.util.PackageAndroidManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranRecordHttpManager {

    /**
     * @param rechargeOn //0不含充值 1含充值 不传返回全部
     * @param pageSize   //每页个数
     * @param pageNumber 当前页数
     * @param timeRange  //0 今天 1 昨天 2本周 3上周 4本月 5上月 6时间段
     * @param transType  交易类型，可传空(//1 充值  2撤销 3消费)
     * @param startTime  起始时间
     * @param endTime    截至时间
     * @param listent
     */
    public static void getQueryDetailNew(String rechargeOn, int pageSize, String pageNumber, String timeRange, String transType, String startTime, String endTime, String tranLogId, String invoiceNum, String tag, final ResponseListener listent) {//极简版收款根据时间范围查询交易@hong[20160325]
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("transType", transType);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("rechargeOn", rechargeOn);//暂时没有意义
        params.put("timeRange", timeRange);
        params.put("pageNo", Integer.valueOf(pageNumber));
        params.put("pageSize", pageSize);

        if (!TextUtils.isEmpty(tranLogId)) {
//            params.put("tranLogId", "P" + AppConfigHelper.getConfig(AppConfigDef.mid) + tranLogId.substring(1));
            params.put("tranLogId", tranLogId);
            params.remove("transType");
            params.remove("timeRange");
        }
        if (!TextUtils.isEmpty(invoiceNum)) {
            params.put(HttpConstants.API_965_PARAM.INVOICENO.getKey(), invoiceNum);
        }


        NetRequest.getInstance().addRequest(Constants.SC_965_TRAN_DETAIL_PAGE, params, tag, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
//                String jsonStr = JSON.toJSONString(response.getResult());
//                listent.onSuccess(new Response(0, "交易成功", jsonStr));
                listent.onSuccess(response);

            }

            @Override
            public void onFaild(Response response) {
                listent.onFaild(response);
            }
        });
    }

    public static void doRefunmidInfo(Context context) {
        Map<String, Object> params = new HashMap<String, Object>();
        PreferenceHelper spHelper = new PreferenceHelper(context, AppConfigDef.SP_loginedInfo);
        String lastLoginId = spHelper.getString(AppConfigDef.SP_lastLoginId, null);
        String lastPasswd = spHelper.getString(AppConfigDef.SP_lastPasswd, null);

        String lastLoginMid = MidConfigManager.getMid();

        String oldMerchants = spHelper.getString(AppConfigDef.SP_loginedMerchant, "[]");
        List<LoginedMerchant> loginedMerchants = null;
        try {
            loginedMerchants = JSONArray.parseArray(oldMerchants, LoginedMerchant.class);
        } catch (Exception e) {

        }
        LoginedMerchant loginedMerchant = null;
        if (loginedMerchants != null) {
            for (LoginedMerchant item : loginedMerchants) {
                if (item.getMid().equals(lastLoginMid)) {
                    loginedMerchant = item;
                }
            }
        }
        String lasttime = null;
        if (loginedMerchant == null) {
            lasttime = "0";
        } else {
            lasttime = String.valueOf(loginedMerchant.getLastOpreatorUpateTime());
        }

        params.put("loginName", lastLoginId);//1001
        params.put("passwd", MD5Util.getMd5Str(lastPasswd));//md5加密
        params.put("mid", lastLoginMid);//
        params.put("terminalUniqNo", SnManager.getSn(context));//G3100000003
        params.put("lastTime", lasttime);//lastTime -> 1589815600300
        params.put(HttpConstants.API_100_PARAM.TERMINALVERSION.getKey(), PackageAndroidManager.getVersionName(context));


        if (!DeviceManager.getInstance().isWizarDevice()) {
            params.put("appBelong", AppInfoUtils.getPayVersionBelong(context));
            params.put("packageName", AppInfoUtils.getPackageName(context));
            params.put("appVersion", AppInfoUtils.getPayVersionName(context));
        }

        NetRequest.getInstance().addRequest(Constants.SC_100_MERCHANT_INFO_SUBMIT, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                LoginResp loginResp = JSONObject.parseObject(response.getResult().toString(), LoginResp.class);
                Log.d("tagtagtag", "返回结果:" + JSON.toJSONString(loginResp));
                if (loginResp != null && loginResp.getRefundRelationMids() != null) {
                    RefundRelationMidsManager.settingRefundNameDefault(loginResp.getRefundRelationMids());
                }

            }

            @Override
            public void onFaild(Response response) {

            }
        });
    }
}
