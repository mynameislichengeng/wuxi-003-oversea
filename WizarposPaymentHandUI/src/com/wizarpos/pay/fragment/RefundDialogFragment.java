package com.wizarpos.pay.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lc.baseui.widget.dialog.SimpleListViewDialog;
import com.ui.setting.CommonItem;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.recode.constants.IntentConstants;
import com.wizarpos.pay.recode.hisotory.activitylist.widget.TransRecordDialog;
import com.wizarpos.pay.ui.newui.NewTranlogActivity;
import com.wizarpos.pay.view.util.NewCashTextWatcher;
import com.motionpay.pay2.lite.R;
import com.wizarpos.recode.constants.TransRecordLogicConstants;

/**
 * Created by 苏震 on 2016/11/10.
 */

public class RefundDialogFragment extends DialogFragment implements View.OnClickListener {

    private DailyDetailResp dailyDetailResp;
    private String title;


    private String amount;
    private String alreadyAmount;
    private OnSaveListener onSaveListener;
    private CommonItem ciAmount;
    private EditText etAmount;
    private Context context;


    public static RefundDialogFragment newInstance(String title, DailyDetailResp dailyDetailResp) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putSerializable(IntentConstants.TradLogActivity.REFUND_DIALOG_OBJECT.getKey(), dailyDetailResp);
        RefundDialogFragment refundDialogFragment = new RefundDialogFragment();
        refundDialogFragment.setArguments(bundle);
        return refundDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        title = bundle.getString("title");
        dailyDetailResp = (DailyDetailResp) bundle.getSerializable(IntentConstants.TradLogActivity.REFUND_DIALOG_OBJECT.getKey());
        //transAmount-refundAmount
        alreadyAmount = String.valueOf(Integer.parseInt(dailyDetailResp.getTransAmount()) - Integer.parseInt(dailyDetailResp.getRefundAmount()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_refund_dialog, container);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.rvCancel).setOnClickListener(this);
        view.findViewById(R.id.rvCommit).setOnClickListener(this);
        ciAmount = ((CommonItem) view.findViewById(R.id.ciAmount));
        //
        String trancy = TransRecordLogicConstants.TRANSCURRENCY.getSymbol(dailyDetailResp.getTransCurrency());
        ciAmount.getTvRight().setText(trancy);
        ciAmount.getTvRight().setPadding(0, 0, 0, 0);
        etAmount = ciAmount.getEtRight();
        if ("0".equals(alreadyAmount)) {
            etAmount.setText("0.00");
        } else {
            etAmount.setText(Calculater.formotFen(alreadyAmount));
        }
        etAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        etAmount.setSelection(etAmount.getText().toString().trim().length());
        etAmount.addTextChangedListener(new NewCashTextWatcher(etAmount, new NewCashTextWatcher.EditTextChange() {
            @Override
            public void dataChange(String string) {
//                string = etAmount.getText().toString().trim();
            }
        }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rvCancel:
                this.dismiss();
                break;
            case R.id.rvCommit:
                operateOnclickSure();
                break;
        }
    }


    private void operateOnclickSure() {
        amount = etAmount.getText().toString().trim();
        if ("0".equals(Calculater.formotYuan(amount))) {
            Toast.makeText(getActivity(), getResources().getString(R.string.input_cancel_amount), Toast.LENGTH_SHORT).show();
            return;
        }
        if (Integer.parseInt(Calculater.formotYuan(amount)) > Integer.parseInt(alreadyAmount)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.revoke_large), Toast.LENGTH_SHORT).show();
            return;
        }
        //取消
        this.dismiss();
        //弹出确认
        TransRecordDialog.refundDialog(this.context, amount, dailyDetailResp, new SimpleListViewDialog.OnCancleAndSuceClickListener() {
            @Override
            public void onSure(View view) {
                RefundDialogFragment.this.onSaveListener.onSave(amount);
            }

            @Override
            public void onCancle(View view) {

            }
        });
    }

    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public interface OnSaveListener {
        void onSave(String amount);
    }
}
