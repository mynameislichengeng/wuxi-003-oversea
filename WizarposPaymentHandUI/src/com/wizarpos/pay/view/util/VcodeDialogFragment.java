package com.wizarpos.pay.view.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.view.util.DialogHelper2.DialogChoiseListener;
import com.wizarpos.pay.view.util.DialogHelper2.DialogListener;
import com.wizarpos.pay2.lite.R;

public class VcodeDialogFragment extends DialogFragment {

    private static final String ARGMENTS_MSG = "msg";
    private static final String ARGMENTS_RESET_PASSWORD_FLAG = "argments_reset_password_flag";

    private String msg;
    private boolean resetPasswordFlag;

    private DialogListener mListener;
    private DialogChoiseListener mChoiseListener;
    private VcodeListener mVcodeListener;

    public static VcodeDialogFragment newInstance(String msg,boolean isNeedResetPassword) {
        VcodeDialogFragment fragment = new VcodeDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARGMENTS_MSG, msg);
        args.putBoolean(ARGMENTS_RESET_PASSWORD_FLAG, isNeedResetPassword);
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(DialogListener mListener) {
        this.mListener = mListener;
    }

    public void setChoiseListener(DialogChoiseListener mChoiseListener) {
		this.mChoiseListener = mChoiseListener;
	}

    public VcodeDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            msg = getArguments().getString(ARGMENTS_MSG);
            resetPasswordFlag = getArguments().getBoolean(ARGMENTS_RESET_PASSWORD_FLAG);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        getDialog().getWindow().findViewById(R.id.etVcode).requestFocus();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_vcode, null);
        final TextView tvMsg = (TextView) view.findViewById(R.id.tvVcodeMsg);
        tvMsg.setText(msg);
        final EditText etVcode = (EditText) view.findViewById(R.id.etVcode);
        final EditText etNewPwd = (EditText) view.findViewById(R.id.etNewPwd);
        final EditText etNewPwdConfirm = (EditText) view.findViewById(R.id.etNewPwdConfirm);
        if (resetPasswordFlag) {
        	etNewPwd.setVisibility(View.VISIBLE);
        	etNewPwdConfirm.setVisibility(View.VISIBLE);
		}
        builder.setView(view).setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    mListener.onOK();
                } else if (mChoiseListener != null) {
                    mChoiseListener.onOK();
                }
                if (mVcodeListener != null) {
                	String newPwd = etNewPwd.getText().toString();
                	String newPwdConfirm = etNewPwdConfirm.getText().toString();
					mVcodeListener.onVcodeInput(etVcode.getText().toString(), newPwd, newPwdConfirm);
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
        etVcode.requestFocus();
        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface VcodeListener{
        void onVcodeInput(String vcode, String newPwd, String newPwdConfirm);
    }

    public VcodeListener getmVcodeListener() {
        return mVcodeListener;
    }

    public void setmVcodeListener(VcodeListener mVcodeListener) {
        this.mVcodeListener = mVcodeListener;
    }
}
