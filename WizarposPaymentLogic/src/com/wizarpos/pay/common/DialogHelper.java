package com.wizarpos.pay.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class DialogHelper {

    private static final int DEFAULT_TEXT_SIZE = 18;

    public static interface DialogCallback {
        public void callback();
    }

    public static interface DialogCallbackAndNo {
        public void callback();

        public void callbackNo();

    }

    /**
     * 提示信息对话框
     *
     * @param context
     * @param msg
     */
    public static void showDialog(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("提示");
        builder.setMessage("    " + msg + "    ");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        try {
            dialog.show();
            updateTextSize(dialog.getWindow().getDecorView(), DEFAULT_TEXT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提示信息对话框
     *
     * @param context
     * @param msg
     * @param dialogCallback
     */
    public static void showDialog(Context context, String msg,
                                  final DialogCallback dialogCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("提示");
        builder.setMessage("    " + msg + "    ");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialogCallback.callback();
            }
        });
        AlertDialog dialog = builder.create();
        try {
            dialog.show();
            updateTextSize(dialog.getWindow().getDecorView(), DEFAULT_TEXT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提示信息可选对话框
     *
     * @param context
     * @param msg
     * @param dialogCallback
     */
    public static void showChoiseDialog(Context context, String msg,
                                        final DialogCallbackAndNo dialogCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("提示");
        builder.setMessage("    " + msg + "    ");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialogCallback.callback();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialogCallback.callbackNo();
            }
        });
        AlertDialog dialog = builder.create();
        try {
            dialog.show();
            updateTextSize(dialog.getWindow().getDecorView(), DEFAULT_TEXT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 进度条对话框
     *
     * @param activity
     * @param title
     * @param message
     * @param btnText
     * @param callback
     * @return
     */
    public static ProgressDialog showProgressDialog(Activity activity,
                                                    String title, String message, String btnText,
                                                    final DialogCallback callback) {
        ProgressDialog progressDialog = new ProgressDialog(activity);

        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);

        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, btnText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (callback != null) {
                            callback.callback();
                        }
                    }
                });
        try {
            progressDialog.show();
            updateTextSize(progressDialog.getWindow().getDecorView(),
                    DEFAULT_TEXT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressDialog;
    }

    /**
     * 调整字体
     *
     * @param v
     * @param textSize
     */
    public static void updateTextSize(View v, int textSize) throws Exception {
        if (v instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) v;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = parent.getChildAt(i);
                updateTextSize(child, textSize);
            }
        } else if (v instanceof TextView) {
            ((TextView) v).setTextSize(textSize);
        }
    }

    public static void showMultiChoiceDialog(Context context, String title,
                                             String[] items, boolean[] selected,
                                             DialogInterface.OnMultiChoiceClickListener choiceListener,
                                             DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMultiChoiceItems(items, selected, choiceListener);
        builder.setPositiveButton("确定", clickListener);
        AlertDialog dialog = builder.create();
        try {
            dialog.show();
            updateTextSize(dialog.getWindow().getDecorView(), DEFAULT_TEXT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Dialog showMultiChoiceDialog(Context context, String title,
                                               String[] items, final boolean[] selected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMultiChoiceItems(items, selected,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        selected[which] = isChecked;
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        try {
            dialog.show();
            updateTextSize(dialog.getWindow().getDecorView(), DEFAULT_TEXT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    public static void showMultiChoiceDialog(Context context, String title,
                                             String[] items, final boolean[] selected,
                                             DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMultiChoiceItems(items, selected,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        selected[which] = isChecked;
                    }
                });
        builder.setPositiveButton("确定", clickListener);
        AlertDialog dialog = builder.create();
        try {
            dialog.show();
            updateTextSize(dialog.getWindow().getDecorView(), DEFAULT_TEXT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDateDialog(Context context, final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        Locale locale = context.getResources().getConfiguration().locale;
        Locale.setDefault(locale);
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                String month = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1)
                        : (monthOfYear + 1) + "";
                String day = (dayOfMonth + 1) < 10 ? "0" + (dayOfMonth)
                        : (dayOfMonth) + "";
				editText.setText(year + "-" + month + "-" + day);
//                editText.setText(getConvertMonth(month) + " " + day + ", " + year);
            }
        }, year, monthOfYear, dayOfMonth).show();
    }

    public static String getConvertMonth(String month) {
        switch (month) {
            case "1":
                return "Jan";
            case "2":
                return "Feb";
            case "3":
                return "Mar";
            case "4":
                return "Apr";
            case "5":
                return "May";
            case "6":
                return "Jun";
            case "7":
                return "Jul";
            case "8":
                return "Aug";
            case "9":
                return "Sep";
            case "10":
                return "Oct";
            case "11":
                return "Nov";
            case "12":
                return "Dec";
            default:
                return "";
        }
    }
}
