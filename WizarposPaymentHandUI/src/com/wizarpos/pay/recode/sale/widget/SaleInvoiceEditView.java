package com.wizarpos.pay.recode.sale.widget;


import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import com.lc.baseui.constants.UIStyleEnum;
import com.lc.baseui.tools.keyBoradManager;
import com.lc.baseui.widget.dialog.CommonDialog;
import com.wizarpos.pay.recode.sale.callback.InvoiceUIClickListener;
import com.wizarpos.pay2.lite.R;

/**
 * 编辑invoice对话框
 */
public class SaleInvoiceEditView {

    private static final int LENGTH_EDIT_VIEW = 25;//最大输入长度

    public static void show(Context context, String content, final InvoiceUIClickListener invoiceUIClickListener) {
        final CommonDialog commonDialog = new CommonDialog(context, UIStyleEnum.EDITVIEW_NUM_ZIMU);
        commonDialog.show();
        //主题
        String title = context.getString(R.string.invoice_title_input);
        commonDialog.setCommonTitle(title);
        //右边按钮
        String rightText = context.getString(R.string.ok);
        commonDialog.setRightBtnText(rightText);
        commonDialog.setRightBtnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonDialog.dismiss();
                if (invoiceUIClickListener != null) {
                    String str = commonDialog.getContent();
                    if (str == null) {
                        str = "";
                    }
                    invoiceUIClickListener.onClick(str);
                }
            }
        });
        //左边按钮
        String leftText = context.getString(R.string.cancle_str);
        commonDialog.setLeftBtnText(leftText);
        //中间内容
        commonDialog.setContent(content);
        //设置最大输入长度
        EditText editText = commonDialog.getDialog_ed_content();
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(LENGTH_EDIT_VIEW)});
        keyBoradManager.showFromDialog((Activity) context);


    }

}
