package com.wizarpos.pay.recode.hisotory.activitylist.widget;

import android.content.Context;
import android.view.View;

import com.lc.baseui.widget.dialog.SimpleListViewDialog;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.recode.hisotory.activitylist.adapter.RefundClickAdapter;
import com.wizarpos.pay2.lite.R;

public class TransRecordDialog {

    /**
     * 点击refund按钮，弹出dialog
     */
    public static void refundDialog(Context context, DailyDetailResp resp, final SimpleListViewDialog.OnCancleAndSuceClickListener listener) {
        SimpleListViewDialog simpleListViewDialog = new SimpleListViewDialog(context);
        simpleListViewDialog.show();
        //title
//        simpleListViewDialog.setDialogTitleVisible(View.GONE);
        simpleListViewDialog.setDialogTitle(R.string.refund);
        //中间
        RefundClickAdapter adapter = new RefundClickAdapter(context, resp);
        simpleListViewDialog.setAdapter(adapter);
        simpleListViewDialog.setOnCancleAndSuceClickListener(new SimpleListViewDialog.OnCancleAndSuceClickListener() {
            @Override
            public void onSure(View view) {
                if (listener != null) {
                    listener.onSure(view);
                }
            }

            @Override
            public void onCancle(View view) {
                if (listener != null) {
                    listener.onCancle(view);
                }
            }
        });

    }
}
