package com.wizarpos.pay.common.device;

import com.wizarpos.pay.common.base.BasePresenter;

import javax.xml.transform.Result;

/**
 * Created by wu on 15/12/11.
 */
public interface SmartCardListener {

    void setSmartCardListener(BasePresenter.ResultListener listener);

    void close();

}
