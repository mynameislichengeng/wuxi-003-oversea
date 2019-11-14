package com.wizarpos.pay.cashier.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.wizarpos.pay.view.fragment.InputPadFragment;
import com.wizarpos.pay.view.fragment.OnMumberClickListener;
import com.wizarpos.pay2.lite.R;

public class HumanWorkFragment extends Fragment implements OnMumberClickListener {
	EditText etCardNum;
//	Button btnConfirm;
	private ScanListener listener;
	View view;
	private InputPadFragment inputPadFragment;

	public void setListener(ScanListener listener) {
		this.listener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.human_work, container , false);
		setupView(view);
		inputPadFragment = InputPadFragment.newInstance(InputPadFragment.KEYBOARDTYPE_SIMPLE);
		getFragmentManager().beginTransaction().replace(R.id.flInputPad, inputPadFragment).commit();
		inputPadFragment.setOnMumberClickListener(this);
		inputPadFragment.setEditView(etCardNum, com.wizarpos.pay.view.fragment.InputPadFragment.InputType.TYPE_INPUT_NORMAL);
		return view;
	}

	private void setupView(View view) {
		etCardNum = (EditText) view.findViewById(R.id.input_code);
//		btnConfirm = (Button) view.findViewById(R.id.key_ok);
	}

	@Override
	public void onSubmit() {
		String cardNum = etCardNum.getText().toString();
		// String cardNum = "0000140000001741";

		if (TextUtils.isEmpty(cardNum)) { return; }
		if (listener != null) {
			listener.onScan(cardNum, false);
		}

	}
}
