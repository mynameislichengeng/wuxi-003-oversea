package com.wizarpos.pay.recode.hisotory.activitylist.callback;

import com.wizarpos.pay.model.DailyDetailResp;

public interface OnTranLogDetialListener {
    void onPrint(DailyDetailResp resp);

    void onRevoke(DailyDetailResp resp);
}
