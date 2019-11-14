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
import android.widget.TextView;

import com.wizarpos.pay.view.util.DialogHelper2.DialogChoiseListener;
import com.wizarpos.pay.view.util.DialogHelper2.DialogListener;
import com.wizarpos.pay2.lite.R;

public class AgreementDialogFragment extends DialogFragment {

    private static final String MSG = "msg";

    private String msg;

    private DialogListener mListener;
    private DialogChoiseListener mChoiseListener;
    private AgreemnetListener mAgreemnetListener;

    public static AgreementDialogFragment newInstance(String msg) {
        AgreementDialogFragment fragment = new AgreementDialogFragment();
        Bundle args = new Bundle();
        args.putString(MSG, msg);
        fragment.setArguments(args);
        return fragment;
    }

    public static AgreementDialogFragment newInstance() {
        AgreementDialogFragment fragment = new AgreementDialogFragment();
//        Bundle args = new Bundle();
//        args.putString(MSG, msg);
//        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(DialogListener mListener) {
        this.mListener = mListener;
    }

    public void setChoiseListener(DialogChoiseListener mChoiseListener) {
		this.mChoiseListener = mChoiseListener;
	}

    public AgreementDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            msg = getArguments().getString(MSG);
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
        final View view = inflater.inflate(R.layout.dialog_agreement, null);
        final TextView tvMsg = (TextView) view.findViewById(R.id.tv_agreement);
        if (!TextUtils.isEmpty(msg)){
            tvMsg.setText(msg);
        }
        builder.setView(view).setNegativeButton("同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    mListener.onOK();
                } else if (mChoiseListener != null) {
                    mChoiseListener.onOK();
                }
                if (mAgreemnetListener != null) {
                    mAgreemnetListener.onAgree();
                }
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mChoiseListener != null) {
                    mChoiseListener.onOK();
                }
                if (mAgreemnetListener != null) {
                    mAgreemnetListener.onCancle();
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface AgreemnetListener {
        void onAgree();
        void onCancle();
    }

    public AgreemnetListener getmAgreemnetListener() {
        return mAgreemnetListener;
    }

    public void setmAgreemnetListener(AgreemnetListener mAgreemnetListener) {
        this.mAgreemnetListener = mAgreemnetListener;
    }
}
