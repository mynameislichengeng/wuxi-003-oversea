package com.lc.baseui.widget.dialog.base;//package com.haoqee.humanaffair.wiget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.lc.baseui.R;


/**
 * @Description:常用DIALOG
 * @author: zhuchunlin@uzoo.cn
 */
public abstract class CustomerDialog extends Dialog implements View.OnClickListener {
    public abstract int getLayout();

    public abstract void init();

    public Context context;

    public CustomerDialog(Context context) {
        super(context, R.style.common_dialog);
        this.context = context;
    }

    public CustomerDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }

    protected CustomerDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        init();
    }

    /** **/
    @Override
    public void onClick(View view) {
        dismiss();
    }

}
