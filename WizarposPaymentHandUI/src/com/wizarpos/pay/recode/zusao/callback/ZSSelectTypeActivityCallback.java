package com.wizarpos.pay.recode.zusao.callback;

import com.wizarpos.pay.recode.zusao.bean.resp.ZSGetQrCode953Resp;

public interface ZSSelectTypeActivityCallback {

    void onSuccessGetQrCodeCallback(ZSGetQrCode953Resp resp);

    void onErrorGetQrCodeCallback(String msg);
}
