package com.lc.baseui.widget.pp.impl;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;

import com.lc.baseui.R;
import com.lc.baseui.tools.SystemIntentUtil;

/**
 * Created by licheng on 2017/5/21.
 */

public class SimplePopBtnUtil {

    /**
     * 打电话的popwindow
     *
     * @param activity 上下文
     * @param phone    电话话吗
     **/
    public static void phoneWindow(final Activity activity, View view, final String phone) {
        final SimplePopWindowBtn3 pop = new SimplePopWindowBtn3(activity);
        pop.setText(R.string.callphone, R.string.sendmsg, R.string.cancle_str);
        pop.setClickEventBeforTWoBtn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                SystemIntentUtil.callPhone(activity, phone);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                SystemIntentUtil.sendMsg(activity, phone, "");
            }
        });
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 10);
    }


}
