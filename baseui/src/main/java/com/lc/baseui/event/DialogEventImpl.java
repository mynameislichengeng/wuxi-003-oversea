package com.lc.baseui.event;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.lc.baseui.constants.UIStyleConstants;
import com.lc.baseui.listener.adapterUI.CustomerEventNextCallback;
import com.lc.baseui.tools.data.RefieldtUtils;
import com.lc.baseui.widget.dialog.CommonDialog;

/**
 * Created by licheng on 2017/5/2.
 */

public class DialogEventImpl {
    private CustomerEventNextCallback callback;
    private Context context;

    public DialogEventImpl(CustomerEventNextCallback callback, Context context) {
        this.callback = callback;
        this.context = context;
    }

    /**
     * @param title 标题
     **/
    public void show(String title, final boolean isInput) {
        final CommonDialog dialog = new CommonDialog(context, UIStyleConstants.EDITVIEW);
        dialog.show();
        dialog.setCommonTitle(title);
        dialog.setContent("");
        dialog.setRightBtnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = dialog.getContent();
                if (isInput) {
                    if (TextUtils.isEmpty(str)) {
                        if (callback != null) {
                            callback.doSomething("");
                        }
                        return;
                    }
                }
                dialog.dismiss();
                if (callback != null) {
                    callback.doSomething(str);
                }
            }
        });
    }

}
