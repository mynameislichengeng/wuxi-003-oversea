package com.wizarpos.pay.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.pay.app.Pay2Application;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.push.presenter.PushPresenter;

/**
 * Created by 苏震 on 2017/10/23.
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */


public class ReceiveIntentService extends GTIntentService {
    public static final String ACTION_GET_PUSH_MESSAGE = "action_get_push_message";
    public static final String ACTION_GET_CLENTID = "ACTION_GET_CLENTID";

    public ReceiveIntentService() {
    }

    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, Thread.currentThread().getId() + "");
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        Pay2Application.CLIENT_ID = clientid;
        if (Constants.FALSE.equals(AppConfigHelper.getConfig(AppStateDef.isRegisterTerminal, Constants.FALSE))
                && Constants.TRUE.equals(AppConfigHelper.getConfig(AppStateDef.isLogin))) {
            new PushPresenter().registerTerminal(clientid, new ResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    AppConfigHelper.setConfig(AppStateDef.isRegisterTerminal, Constants.TRUE);
                }

                @Override
                public void onFaild(Response response) {
                }
            });
        }
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        byte[] pushContent = gtTransmitMessage.getPayload();
        Log.e(TAG, "onReceiveGTTransmitMessage -> " + pushContent.toString());
        if (pushContent == null) {
            return;
        }
        String data = new String(pushContent);
        Intent intent = new Intent();
        intent.putExtra("pushMessage", data);
        intent.setAction(ACTION_GET_PUSH_MESSAGE);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        Log.d(TAG, "onReceiveCommandResult -> " + gtCmdMessage);
    }
}
