package com.wizarpos.pay.recode.hisotory.activitylist.widget;

import android.content.Context;

import com.lc.baseui.widget.dialog.SimpleListViewDialog;

public class TransRecordDialog {

    /**
     * 点击refund按钮，弹出dialog
     */
    public static void refundDialog(Context context) {
        SimpleListViewDialog simpleListViewDialog = new SimpleListViewDialog(context);
        simpleListViewDialog.show();
    }
}
