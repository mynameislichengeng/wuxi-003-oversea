package com.wizarpos.pay.recode.hisotory.activitylist.widget;

import android.content.Context;
import android.view.View;

import com.lc.baseui.constants.UIStyleEnum;
import com.lc.baseui.widget.dialog.CommonDialog;
import com.lc.baseui.widget.dialog.SimpleListViewDialog;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.recode.hisotory.activitylist.adapter.RefundClickAdapter;
import com.wizarpos.pay.recode.hisotory.widget.IssueDialog;
import com.wizarpos.pay2.lite.R;

public class TransRecordDialog {

    /**
     * 点击refund按钮，弹出dialog
     */
    public static void refundDialog(Context context, String inputAmount, DailyDetailResp resp, final SimpleListViewDialog.OnCancleAndSuceClickListener listener) {
        SimpleListViewDialog simpleListViewDialog = new SimpleListViewDialog(context);
        simpleListViewDialog.show();
        //title
        simpleListViewDialog.setDialogTitle(R.string.refund);
        //中间
        RefundClickAdapter adapter = new RefundClickAdapter(context, resp);
        adapter.setInputRefundAmount(inputAmount);//用户输入的当前需要退款的金额
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
        simpleListViewDialog.setBtnLeftText(R.string.btn_cancle_big);
    }

    public static void issueReceiptDialog(Context context, final View.OnClickListener leftcustomOnclick, final View.OnClickListener rightmerchantOnclick) {

        final IssueDialog commonDialog = new IssueDialog(context, UIStyleEnum.NULLVIEW);
        commonDialog.show();
        //
        commonDialog.setRightBtnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonDialog.dismiss();
                rightmerchantOnclick.onClick(v);
            }
        });
        //
        commonDialog.setLeftBtnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonDialog.dismiss();
                leftcustomOnclick.onClick(v);
            }
        });
        //
        commonDialog.setThreeBtnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonDialog.dismiss();
            }
        });


    }
}
