package com.wizarpos.pay.recode.sale.widget;


import android.content.Context;
import android.view.View;

import com.lc.baseui.constants.UIStyleEnum;
import com.lc.baseui.widget.dialog.CommonDialog;
import com.wizarpos.pay2.lite.R;

public class SaleInvoiceEditView {

    public static void show(Context context) {
        CommonDialog commonDialog = new CommonDialog(context, UIStyleEnum.EDITVIEW);
        commonDialog.show();
        //
        String title = context.getString(R.string.pay_invoice);
        commonDialog.setCommonTitle(title);
        //
        String rightText = context.getString(R.string.ok);
        commonDialog.setRightBtnText(rightText);
        commonDialog.setRightBtnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //
        String leftText = context.getString(R.string.cancle_str);
        commonDialog.setLeftBtnText(leftText);

    }

}
