package com.wizarpos.pay.view.util;

import android.text.InputType;
import android.text.method.DigitsKeyListener;

import com.wizarpos.pay.app.Pay2Application;

/**
 * 使用非InputType方式限制输入内容
 * Created by Song on 2016/10/20.
 */

public class CommonDigistKeyListener extends DigitsKeyListener {
    private boolean isOnlyNumber;
    private int resId;

    public CommonDigistKeyListener(boolean isOnlyNumber, int resId) {
        this.isOnlyNumber = isOnlyNumber;
        this.resId = resId;
    }

    @Override
    public int getInputType() {
        return isOnlyNumber ? InputType.TYPE_CLASS_NUMBER : InputType.TYPE_TEXT_VARIATION_PASSWORD;
    }

    @Override
    protected char[] getAcceptedChars() {
        char[] data = getStringData(resId).toCharArray();
        return data;
    }

    public String getStringData(int id) {
        return Pay2Application.getInstance().getResources().getString(id);
    }
}
