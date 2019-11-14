package com.wizarpos.pay.view.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.wizarpos.pay.view.util.DialogHelper2.DialogChoiseListener;
import com.wizarpos.pay.view.util.DialogHelper2.DialogListener;

public class SimpleMsgDialogFragment extends DialogFragment {

    private static final String MSG = "msg";

    private String msg;

    private DialogListener mListener;
    private DialogChoiseListener mChoiseListener;

    public static SimpleMsgDialogFragment newInstance(String msg) {
        SimpleMsgDialogFragment fragment = new SimpleMsgDialogFragment();
        Bundle args = new Bundle();
        args.putString(MSG, msg);
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(DialogListener mListener) {
        this.mListener = mListener;
    }

    public void setChoiseListener(DialogChoiseListener mChoiseListener) {
        this.mChoiseListener = mChoiseListener;
    }

    public SimpleMsgDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            msg = getArguments().getString(MSG);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              if(mListener != null){
                  mListener.onOK();
              }else if(mChoiseListener != null){
            	  mChoiseListener.onOK();
              }
            }
        });
        if(mChoiseListener != null){
        	builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					mChoiseListener.onNo();
				}
			});
        }
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        this.setCancelable(false);
        return dialog;
        //替换原来的dialog,统一格式
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
