package com.wizarpos.pay.cardlink;

import com.wizarpos.hspos.api.EnumCommand;

/**
 * 收单接口
 * Created by wu on 16/5/29.
 */
public interface CardLinkListener {

    void onProgress(EnumCommand cmd, int progressCode, String progress, boolean continueTrans);

    void onTransSucceed(EnumCommand cmd);

    void onTransFailed(EnumCommand cmd, String message);
}
