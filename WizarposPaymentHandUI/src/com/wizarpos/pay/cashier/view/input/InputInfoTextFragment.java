package com.wizarpos.pay.cashier.view.input;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.view.fragment.InputPadFragment;
import com.wizarpos.pay.view.fragment.OnMumberClickListener;
import com.motionpay.pay2.lite.R;

public class InputInfoTextFragment extends Fragment implements OnMumberClickListener {
	private EditText etCardNum,inputCode;
	private View view;
	private InputPadFragment inputPadFragment;
	
	private InputInfoListener listener;
	
	private String inputMsg = "";

	public void setListener(InputInfoListener listener) {
		this.listener = listener;
	}
	
	public void setInputMsg(String msg) {
		this.inputMsg = msg;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.human_work, container , false);
		if(!TextUtils.isEmpty(inputMsg)) {
			EditText inputCode = (EditText) view.findViewById(R.id.input_code);
			inputCode.setHint(inputMsg);
		}
		etCardNum = (EditText) view.findViewById(R.id.input_code);
		inputPadFragment = InputPadFragment.newInstance(InputPadFragment.KEYBOARDTYPE_SIMPLE);
		getFragmentManager().beginTransaction().replace(R.id.flInputPad, inputPadFragment).commit();
		inputPadFragment.setOnMumberClickListener(this);
		inputPadFragment.setEditView(etCardNum, com.wizarpos.pay.view.fragment.InputPadFragment.InputType.TYPE_INPUT_NORMAL);
		return view;
	}

	@Override
	public void onSubmit() {
		String cardNum = etCardNum.getText().toString();
		// String cardNum = "0000140000001741";

		if (TextUtils.isEmpty(cardNum)) { return; }
		if (listener != null) {
			listener.onGetInfo(cardNum, InputInfoListener.INPUT_TYPE_TEXT);
		}

	}
}
