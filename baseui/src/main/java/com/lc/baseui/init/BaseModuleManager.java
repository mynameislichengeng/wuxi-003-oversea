package com.lc.baseui.init;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by Administrator on 2017/4/24.
 */

public abstract class BaseModuleManager {
    /**
     * 基本的共同监听
     **/
    protected BaseRegisterListener registerListener;


    /**
     * 初始化模块
     **/
    public <T extends BaseRegisterListener> void initModule(T t) {
        registerListener = t;
    }

    /**
     * 获得sessionId
     **/
    public String getSessionId() {
        String sessionId = null;
        if (registerListener != null) {
            sessionId = registerListener.getSessionId();
        }
        return TextUtils.isEmpty(sessionId) ? "" : sessionId;
    }
}
