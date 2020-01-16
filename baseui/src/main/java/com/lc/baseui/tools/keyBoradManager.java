package com.lc.baseui.tools;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.view.inputmethod.InputMethodManager;

public class keyBoradManager {


    /**
     * 从dialog中弹出软盘
     *
     * @param activity
     */
    public static void showFromDialog(final Activity activity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(200);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                });

            }
        }).start();
    }

    public static void closeKeyBorad(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && context.getCurrentFocus() != null) {
            if (context.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
