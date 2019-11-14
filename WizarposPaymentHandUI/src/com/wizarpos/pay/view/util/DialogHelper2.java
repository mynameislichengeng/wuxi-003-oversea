package com.wizarpos.pay.view.util;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.wizarpos.pay2.lite.R;

import java.util.List;

public class DialogHelper2 {

    public static interface DialogListener {
        public void onOK();
    }

    public static interface DialogChoiseListener {
        public void onOK();

        public void onNo();
    }

    /**
     * 提示信息对话框
     */
    public static void showDialog(FragmentActivity activity, String msg) {
        SimpleMsgDialogFragment fragment = SimpleMsgDialogFragment.newInstance(msg);
        fragment.show(activity.getSupportFragmentManager(), msg);
    }

    public static void showDialog(FragmentActivity activity, String msg, DialogListener listener) {
        SimpleMsgDialogFragment fragment = SimpleMsgDialogFragment.newInstance(msg);
        fragment.setListener(listener);
        if (!activity.isFinishing()) {
            fragment.show(activity.getSupportFragmentManager(), msg);
        }
    }

    public static void showChoiseDialog(FragmentActivity activity, String msg, DialogChoiseListener listener) {
        SimpleMsgDialogFragment fragment = SimpleMsgDialogFragment.newInstance(msg);
        fragment.setChoiseListener(listener);
        fragment.show(activity.getSupportFragmentManager(), msg);
    }

    public static void showMultieChooseDialog(FragmentActivity activity, List<MultieChooseItem> items, MultieChooseDialogFragment.MultieChooseListener listener) {
        MultieChooseDialogFragment fragment = MultieChooseDialogFragment.newInstance(items);
        fragment.setMultieChooseListener(listener);
        fragment.show(activity.getSupportFragmentManager(), "showMultieChooseDialog");
    }

    public static void showCompleteDialog(Context context, int gravity,
                                          boolean expanded, int contextId, final DialogChoiseListener listener) {
        final DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(contextId))
                .setCancelable(true)
                .setGravity(gravity)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final DialogPlus dialog, View view) {
                        dialog.dismiss();
                        listener.onNo();
                    }
                })
                .setExpanded(expanded)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOverlayBackgroundResource(android.R.color.transparent)
                .create();
        dialog.show();
    }

    public static void showChooseDialog(Context context, String msg,final DialogChoiseListener listener) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_main, null);
        Button btnCancle = (Button) dialogView.findViewById(R.id.btnCancel);
        TextView tvInfo = (TextView) dialogView.findViewById(R.id.tvRemindInfo);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOK);
        tvInfo.setText(msg);
        MaterialDialog.Builder chooseDialogBuilder = new MaterialDialog.Builder(context);
        final MaterialDialog chooseDialog = chooseDialogBuilder.customView(dialogView, false).build();
        chooseDialog.show();
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseDialog != null) {
                    chooseDialog.dismiss();
                }
                listener.onNo();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseDialog != null) {
                    chooseDialog.dismiss();
                }
                listener.onOK();
            }
        });
    }
}
