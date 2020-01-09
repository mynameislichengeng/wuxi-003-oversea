package com.wizarpos.pay.recode.hisotory.activitylist.widget;

import android.content.Context;

import com.lc.baseui.widget.dialog.SimpleListViewDialog;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.recode.hisotory.activitylist.adapter.RefundClickAdapter;

public class TransRecordDialog {

    /**
     * 点击refund按钮，弹出dialog
     */
    public static void refundDialog(Context context, DailyDetailResp resp) {
        SimpleListViewDialog simpleListViewDialog = new SimpleListViewDialog(context);
        simpleListViewDialog.show();
        RefundClickAdapter adapter = new RefundClickAdapter(context,resp);
        simpleListViewDialog.setAdapter(adapter);

    }
}
