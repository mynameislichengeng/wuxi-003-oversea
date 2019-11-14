package com.wizarpos.pay.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.setting.presenter.RateUpdate;

import java.util.Calendar;

/**
 * Created by blue_sky on 2016/11/8.
 */

public class Alarmreceiver extends BroadcastReceiver {
    public static final String ACTION_GET_EXCHANGE = "action_get_exchange";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(Constants.UPDATE)) {
//            Toast.makeText(context, "update", Toast.LENGTH_LONG).show();
            Calendar cal = Calendar.getInstance();// 当前日期
            int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
            int minute = cal.get(Calendar.MINUTE);// 获取分钟
            int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
            final int time = 10 * 60;// 结束时间 10:00的分钟数
            if ((minuteOfDay > time && Constants.TRUE.equals(AppStateManager.getState(AppStateDef.isLogin, Constants.FALSE))) ||
                    (TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.exchangeRate)) &&
                            Constants.TRUE.equals(AppStateManager.getState(AppStateDef.isLogin, Constants.FALSE)))) {
                RateUpdate rateUpdate = new RateUpdate();
                rateUpdate.rateUpdate(new ResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        JSONObject jsonObject = (JSONObject) response.getResult();
                        if (null != jsonObject) {
                            String exchangeRate = jsonObject.getString("rate");
                            if (!TextUtils.isEmpty(exchangeRate)) {
                                AppConfigHelper.setConfig(AppConfigDef.exchangeRate, exchangeRate);
                                Intent intent1 = new Intent();
                                intent1.setAction(ACTION_GET_EXCHANGE);
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
                            }
                        }
                    }

                    @Override
                    public void onFaild(Response response) {
                        Toast.makeText(context, response.msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
