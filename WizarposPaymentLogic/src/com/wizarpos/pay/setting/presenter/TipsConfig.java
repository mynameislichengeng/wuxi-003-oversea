package com.wizarpos.pay.setting.presenter;

import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.model.TipsConfigInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by blue_sky on 2016/11/5.
 */

public class TipsConfig {
    public void upLoadTips(TipsConfigInfo tipsConfigInfo, final ResponseListener responseListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("collectTips", tipsConfigInfo.getCollectTips());
        params.put("tipsPercentageAllow", tipsConfigInfo.getTipsPercentageAllow());
        params.put("tipsPercentage", tipsConfigInfo.getTipsPercentage());
        params.put("tipsCustomAllow", tipsConfigInfo.getTipsCustomAllow());
        params.put("tipsTerminalAllow", tipsConfigInfo.getTipsTerminalAllow());
        NetRequest.getInstance().addRequest(Constants.SC_956_TIPS_UPDATE, params, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFaild(Response response) {
                responseListener.onFaild(response);
            }
        });
    }
}
