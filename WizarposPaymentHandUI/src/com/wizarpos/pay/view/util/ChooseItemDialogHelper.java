package com.wizarpos.pay.view.util;

import android.content.Context;

import com.wizarpos.pay.view.ActionSheetDialog;
import com.wizarpos.pay.view.ArrayItem;

import java.util.List;

/**
 * Created by Song on 2016/10/17.
 */

public class ChooseItemDialogHelper {

    /**
     * 仿IOS弹窗选择
     * @param context
     * @param arrayItems
     * @param title
     * @param listener
     * @return
     */
    public ActionSheetDialog createArrayItemSheetDialog(Context context, List<ArrayItem> arrayItems, String title, ActionSheetDialog.ActionSheetListener listener) {
        ActionSheetDialog actionSheetDialog = new ActionSheetDialog(context, arrayItems, listener)
                .builder()
                .setTitle(title)
                .setCancelable(true)
                .setCanceledOnTouchOutside(true);
        return actionSheetDialog;
    }

}
