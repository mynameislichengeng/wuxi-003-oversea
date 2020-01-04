package com.wizarpos.pay.recode.sale.widget;


import android.content.Context;
import android.text.InputFilter;
import android.view.View;

import com.lc.baseui.constants.UIStyleEnum;
import com.lc.baseui.widget.dialog.CommonDialog;
import com.wizarpos.pay.recode.sale.callback.InvoiceUIClickListener;
import com.wizarpos.pay2.lite.R;

/**
 * 编辑invoice对话框
 */
public class SaleInvoiceEditView {

    private static final int LENGTH_EDIT_VIEW = 25;//最大输入长度

    public static void show(Context context, String content ,final InvoiceUIClickListener invoiceUIClickListener) {
        final CommonDialog commonDialog = new CommonDialog(context, UIStyleEnum.EDITVIEW_NUM_ZIMU);
        commonDialog.show();
        //主题
        String title = context.getString(R.string.pay_invoice);
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
        //设置最大输入长度,从0开始算的，所以减1，这样常量数字就表示最大长度了
        commonDialog.getDialog_ed_content().setFilters(new InputFilter[]{new InputFilter.LengthFilter(LENGTH_EDIT_VIEW - 1)});

    }

}
