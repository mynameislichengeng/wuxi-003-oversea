package com.wizarpos.pay.recode.hisotory.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lc.baseui.constants.UIStyleEnum;
import com.lc.baseui.widget.dialog.CommonDialog;
import com.motionpay.pay2.lite.R;

public class IssueDialog extends CommonDialog {

    private Button dialog_btn_three;

    public IssueDialog(Context context, UIStyleEnum uiStyleEnum) {
        super(context, uiStyleEnum);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_acti_issue);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        dialog_btn_three = findViewById(R.id.dialog_btn_three);
        //
        String title = getContext().getString(R.string.acti_tran_issue_title);
        setCommonTitle(title);
        //
        String rightText = getContext().getString(R.string.acti_tran_issue_btn_merchant_copy);
        setRightBtnText(rightText);
        //
        String leftText = getContext().getString(R.string.acti_tran_issue_btn_customer_copy);
        setLeftBtnText(leftText);
        //

    }

    public void setThreeBtnClick(View.OnClickListener clickListener) {
        if (clickListener == null) {
            return;
        }
        dialog_btn_three.setOnClickListener(clickListener);
    }
}
