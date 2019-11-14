package com.wizarpos.pay.cardlink;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.atool.log.Logger;
import com.wizarpos.hspos.api.EnumCommand;
import com.wizarpos.hspos.api.ParamInfo;
import com.wizarpos.pay.common.pay.PosInfo;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

import java.util.Calendar;

/**
 * 签到
 * Created by wu on 16/3/10.
 */
public class SignOnProxy extends BaseCardLinkProxy implements CardLinkPresenter.CardLinkPresenterListener {

    private SignOnListener signOnListener;

    protected PosInfo posInfo = new PosInfo();

    protected String termCap;

    public SignOnProxy(Context context) {
        super(context);
    }

    @Override
    public void onTransFailed(EnumCommand cmd, String error, String message) {
        String msg = message + "[" + error + "]";
        if (cmd == EnumCommand.GetParam) {
            signOnFailed(msg);
        } else if (cmd == EnumCommand.Login) {
            signOnFailed(msg);
        }
    }

    @Override
    public void onProgress(EnumCommand cmd, int progressCode, String message) {
        if (cmd == EnumCommand.Login) {
            cardLinkPresenter.continueTrans();
        }
    }

    @Override
    public void onTransSucceed(EnumCommand cmd, Object params) {
        if (cmd == EnumCommand.GetParam) {
            ParamInfo paramInfo = cardLinkPresenter.getParamInfo();
            if(paramInfo != null){
                termCap = paramInfo.getTermCap();
            }
            onGetParams(params);
        } else if (cmd == EnumCommand.Login) {
            signOnSuccess();
        }
    }

    private void onGetParams(Object params) {
        if (params == null) {
            signOnFailed("无法获取收单应用相关信息");
            return;
        }
        Logger.debug(params.toString());
        ParamInfo paramInfo = (ParamInfo) params;
        posInfo.setTerminalId(paramInfo.getTid());
        posInfo.setMerchantId(paramInfo.getMid());
        if (isTodayFirstLogin() || !paramInfo.isSignOn()) {//当天首次登陆调用签到接口 @yaosong [20151211] 增加判断是否签到,没有签到则执行签到 wu
            doSignOnAction();
        } else {
            signOnSuccess();
        }
    }

    private void doSignOnAction() {
        cardLinkPresenter.onLogin();
    }

    protected void signOnFailed(String msg) {
        if (signOnListener != null) {
            signOnListener.onSignOnFailed(msg);
        }
    }

    protected void signOnSuccess() {
        if (signOnListener != null) {
            signOnListener.onSignOnSuccess(posInfo);
        }
    }

    private void doGetParamsAction() {
        cardLinkPresenter.onGetParam();
    }

    public void signOn() {
        doGetParamsAction();
    }

    /**
     * 是否当天首次登陆
     *
     * @return
     */
    public static boolean isTodayFirstLogin() {
        try {
            String lastPOSLoginTime = AppConfigHelper.getConfig(AppConfigDef.lastPOSLoginTime);
            if (!TextUtils.isEmpty(lastPOSLoginTime)) {
                long now = System.currentTimeMillis();
                Calendar nowCalendar = Calendar.getInstance();
                nowCalendar.setTimeInMillis(now);
                int nowday = nowCalendar.get(Calendar.DAY_OF_MONTH);
                int nowmonth = nowCalendar.get(Calendar.MONTH);
                long last = Long.parseLong(lastPOSLoginTime);
                Calendar lastCalendar = Calendar.getInstance();
                lastCalendar.setTimeInMillis(last);
                int lastday = lastCalendar.get(Calendar.DAY_OF_MONTH);
                int lastmonth = nowCalendar.get(Calendar.MONTH);
                if (nowday == lastday && nowmonth == lastmonth) {
                    return false;
                }
            }
        } catch (Exception e) {
            return true;
        }
        return true;
    }

    public void setSignOnListener(SignOnListener signOnListener) {
        this.signOnListener = signOnListener;
    }

    public interface SignOnListener {

        void onSignOnSuccess(PosInfo posInfo);

        void onSignOnFailed(String msg);
    }


}
