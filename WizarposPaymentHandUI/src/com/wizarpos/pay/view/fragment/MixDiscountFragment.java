package com.wizarpos.pay.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.ui.AmountEditText;
import com.wizarpos.pay.view.fragment.InputPadFragment.InputType;
import com.motionpay.pay2.lite.R;

/**
 * @Author:Huangweicai
 * @Date:2015-7-28 下午8:08:27
 * @Reason:底部折扣(包含键盘)
 */
public class MixDiscountFragment extends Fragment implements OnClickListener, OnMumberClickListener {

	private int shouldPayAmount;

	private InputPadFragment inputPadFragment;

	private RelativeLayout rlDirectDiscount, rlPercentDiscount;

	private View vDirectLine, vPercentLine;

	private EditText etMixAmount;
	private AmountEditText tvMixPayInputAmountTips;
	private TextView tvUntil;

	private View rlInputAmount, llChooser;

	private static final int MODE_PRECENT = 1;
	private static final int MODE_AMOUNT = 2;

	// private int currentMode = MODE_AMOUNT;

	public interface DiscountListener {
		/**
		 * 直接折扣
		 */
		public void onReduceAmount(int reduce);

		/**
		 * 比例折扣
		 * 
		 * @param precent
		 */
		public void onReducePrecent(int precent);
	}

	private DiscountListener discountListener;

	public void setDiscountListener(DiscountListener discountListener) {
		this.discountListener = discountListener;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		shouldPayAmount = getArguments().getInt("shouldPayAmount");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment_mix_discount, container, false);
		inputPadFragment = new InputPadFragment();
		inputPadFragment.setOnMumberClickListener(this);
		getChildFragmentManager().beginTransaction().replace(R.id.flInputPad, inputPadFragment).commit();
		initView(mView);
		return mView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		rlInputAmount.setVisibility(View.GONE);
		llChooser.setVisibility(View.VISIBLE);
		setEditView(etMixAmount, InputType.TYPE_INPUT_MONEY);
	}

	private void initView(View v) {
		rlInputAmount = v.findViewById(R.id.rlInputAmount);
		llChooser = v.findViewById(R.id.llChooser);
		tvUntil = (TextView) v.findViewById(R.id.tvUnit);
		tvMixPayInputAmountTips = (AmountEditText) v.findViewById(R.id.tvMixPayInputAmountTips);
		rlDirectDiscount = (RelativeLayout) v.findViewById(R.id.rlDirectDiscount);
		rlPercentDiscount = (RelativeLayout) v.findViewById(R.id.rlPercentDiscount);
		etMixAmount = (EditText) v.findViewById(R.id.etMixAmount);
		vDirectLine = v.findViewById(R.id.vDirectLine);
		vPercentLine = v.findViewById(R.id.vPercentLine);
		rlDirectDiscount.setOnClickListener(this);
		rlPercentDiscount.setOnClickListener(this);
		inputPadFragment.setEditView(etMixAmount, InputType.TYPE_INPUT_MONEY);
	}

	/**
	 * @param mEditView
	 * @param inputType
	 * @Author:Huangweicai
	 * @Date:2015-7-28 下午9:46:40
	 * @Reason:为键盘设置输入类型和控件
	 */
	public void setEditView(EditText mEditView, InputType inputType) {
		inputPadFragment.setEditView(mEditView, inputType);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlDirectDiscount:// 直接折扣
			vDirectLine.setVisibility(View.VISIBLE);
			vPercentLine.setVisibility(View.GONE);
			etMixAmount.setText("0.00");
			tvUntil.setText("元");
			inputPadFragment.setEditView(etMixAmount, InputType.TYPE_INPUT_MONEY);
			break;
		case R.id.rlPercentDiscount:// 比例折扣
			vDirectLine.setVisibility(View.GONE);
			vPercentLine.setVisibility(View.VISIBLE);
			inputPadFragment.setEditView(etMixAmount, InputType.TYPE_INPUT_NORMAL);
			tvUntil.setText("%");
			etMixAmount.setText("");
			// currentMode = MODE_PRECENT;
			break;
		}
	}

	@Override
	public void onSubmit() {

		if (vDirectLine.getVisibility() == View.VISIBLE) {
			String precent = etMixAmount.getText().toString();
			if (TextUtils.isEmpty(precent) || "0.00".equals(precent)) { return; }
			int reduced = Integer.parseInt(Calculater.formotYuan(precent));
			if (reduced > shouldPayAmount) {
				reduced = shouldPayAmount;
			}
			discountListener.onReduceAmount(reduced);
			etMixAmount.setText("0.00");
		} else {
			String str = etMixAmount.getText().toString();
			if (TextUtils.isEmpty(str)) { return; }
			int precent = Integer.parseInt(str);
			if (precent <= 0 || precent >= 100) {
				UIHelper.ToastMessage(getActivity(), "请输入正确的折扣");
				return;
			}
			discountListener.onReducePrecent(precent);
		}
	}

}
