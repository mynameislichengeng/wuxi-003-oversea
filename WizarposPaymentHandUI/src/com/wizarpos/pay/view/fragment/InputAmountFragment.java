package com.wizarpos.pay.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.wizarpos.pay.ui.AmountEditText;
import com.wizarpos.pay.view.fragment.InputPadFragment.InputType;
import com.wizarpos.pay2.lite.R;

/**
 * @Author:Huangweicai
 * @Date:2015-7-28 下午8:08:27
 * @Reason:底部折扣(包含键盘)
 */
public class InputAmountFragment extends Fragment implements OnClickListener, OnMumberClickListener {

	private String maxAmount;

	private InputPadFragment inputPadFragment;

	private RelativeLayout rlDirectDiscount, rlPercentDiscount;

	private AmountEditText tvMixPayInputAmountTips;

	private View rlInputAmount, llChooser;

	public interface InputAmountListener {

		public void onAmount(String amount);

	}

	private InputAmountListener discountListener;

	public void setInputAmountListener(InputAmountListener discountListener) {
		this.discountListener = discountListener;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		maxAmount = getArguments().getString("maxAmount");

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

		rlInputAmount.setVisibility(View.VISIBLE);
		llChooser.setVisibility(View.GONE);
		setEditView(tvMixPayInputAmountTips, InputType.TYPE_INPUT_MONEY);
		tvMixPayInputAmountTips.setMaxAmount(maxAmount);

	}

	private void initView(View v) {

		rlInputAmount = v.findViewById(R.id.rlInputAmount);
		llChooser = v.findViewById(R.id.llChooser);
		tvMixPayInputAmountTips = (AmountEditText) v.findViewById(R.id.tvMixPayInputAmountTips);
		v.findViewById(R.id.etMixAmount).setVisibility(View.GONE);
		rlDirectDiscount = (RelativeLayout) v.findViewById(R.id.rlDirectDiscount);
		rlPercentDiscount = (RelativeLayout) v.findViewById(R.id.rlPercentDiscount);
		v.findViewById(R.id.tvUnit).setVisibility(View.GONE);
		rlDirectDiscount.setOnClickListener(this);
		rlPercentDiscount.setOnClickListener(this);
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

	}

	@Override
	public void onSubmit() {
		if (discountListener != null) {
			discountListener.onAmount(tvMixPayInputAmountTips.getAmountByFen() + "");
		}
	}

}
