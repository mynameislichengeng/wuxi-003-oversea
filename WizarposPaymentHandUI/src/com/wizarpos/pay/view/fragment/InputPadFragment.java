package com.wizarpos.pay.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.device.DeviceManager;
import com.motionpay.pay2.lite.R;

/**
 * 键盘fragment
 * 
 * @author wu
 */
public class InputPadFragment extends Fragment implements OnClickListener, OnLongClickListener {

	public boolean numberEditable = true;

	/**
	 * 是否可编辑
	 * @param editable
	 */
	public void setNumberEditable(boolean editable) {
		this.numberEditable = editable;
	}

	public interface OnKeyChangedListener{
		void onTextChanged(String newStr);
	}

	private OnKeyChangedListener onKeyChanged;
	
	public void setOnTextChangedListener(OnKeyChangedListener onKeyChanged) {
		this.onKeyChanged = onKeyChanged;
	}
	
	private static final String KEYBOARDTYPE = "keyboradType";
	public static final int KEYBOARDTYPE_COMMON = 0;
	public static final int KEYBOARDTYPE_SIMPLE = 1;

	private int keyboradType = 0;

	private OnMumberClickListener listener;

	private EditText mEditView;
	private InputType inputType;
	
	private LinearLayout llKeyboardMain;
	private View widgetKeyboard;
	
	public enum InputType {
		TYPE_INPUT_NORMAL(1), // 正常
		TYPE_INPUT_PWD(2), // 密码
		TYPE_INPUT_MONEY(3);// 金额

		InputType(int iValue) {
			this.m_iEnumValue = iValue;
		}

		public int getValue() {
			return m_iEnumValue;
		}

		public static boolean isSame(InputType oriInputType, InputType secInputType) {
			return secInputType == oriInputType;
		}

		private final int m_iEnumValue;
	}

	public void setOnMumberClickListener(OnMumberClickListener listener) {
		this.listener = listener;
	}

	public static InputPadFragment newInstance(int type) {
		InputPadFragment fragment = new InputPadFragment();
		Bundle args = new Bundle();
		args.putInt(KEYBOARDTYPE, type);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			keyboradType = getArguments().getInt(KEYBOARDTYPE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View inputPadView = inflater.inflate(R.layout.fragment_inputpad, container, false);
		View v0 = inputPadView.findViewById(R.id.iv0);
		v0.setTag("0");
		View v1 = inputPadView.findViewById(R.id.iv1);
		v0.setTag("1");
		View v2 = inputPadView.findViewById(R.id.iv2);
		v0.setTag("2");
		View v3 = inputPadView.findViewById(R.id.iv3);
		v0.setTag("3");
		View v4 = inputPadView.findViewById(R.id.iv4);
		v0.setTag("4");
		View v5 = inputPadView.findViewById(R.id.iv5);
		v0.setTag("5");
		View v6 = inputPadView.findViewById(R.id.iv6);
		v0.setTag("6");
		View v7 = inputPadView.findViewById(R.id.iv7);
		v0.setTag("7");
		View v8 = inputPadView.findViewById(R.id.iv8);
		v0.setTag("8");
		View v9 = inputPadView.findViewById(R.id.iv9);
		v0.setTag("9");
		View vDel = inputPadView.findViewById(R.id.ivDel);
		View vClear = inputPadView.findViewById(R.id.ivClear);
		View vSubmit = inputPadView.findViewById(R.id.ivSubmit);
		widgetKeyboard = inputPadView.findViewById(R.id.widgetKeyboard);
		llKeyboardMain = (LinearLayout) inputPadView.findViewById(R.id.llKeyboardMain);
		v0.setOnClickListener(this);
		v1.setOnClickListener(this);
		v2.setOnClickListener(this);
		v3.setOnClickListener(this);
		v4.setOnClickListener(this);
		v5.setOnClickListener(this);
		v6.setOnClickListener(this);
		v7.setOnClickListener(this);
		v8.setOnClickListener(this);
		v9.setOnClickListener(this);
		vDel.setOnClickListener(this);
		vClear.setOnClickListener(this);
		vSubmit.setOnClickListener(this);
		
		widgetKeyboard.setOnClickListener(this);//点击显示键盘
		
		vDel.setOnLongClickListener(this);//添加长按清空所有内容 wu@[20150902]

		if (keyboradType == KEYBOARDTYPE_SIMPLE) {
			vClear.setVisibility(View.GONE);
		}

		if(Constants.SWITCH_KEYBOARD_FLAG){
			if(DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_M0
					|| (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1))
			{//如果是Q1或M0 一开始默认隐藏键盘 点击widget弹出键盘
				llKeyboardMain.setVisibility(View.GONE);
				widgetKeyboard.setVisibility(View.VISIBLE);
			}
		}

		return inputPadView;
	}

	public void setEditView(EditText mEditView, InputType inputType) {
		this.mEditView = mEditView;
		this.inputType = inputType;
	}

	@Override
	public void onClick(View v) {
		if (listener == null && mEditView == null) { return; }
		switch (v.getId()) {
		case R.id.iv0:
			onNumber("0");
			break;
		case R.id.iv1:
			onNumber("1");
			break;
		case R.id.iv2:
			onNumber("2");
			break;
		case R.id.iv3:
			onNumber("3");
			break;
		case R.id.iv4:
			onNumber("4");
			break;
		case R.id.iv5:
			onNumber("5");
			break;
		case R.id.iv6:
			onNumber("6");
			break;
		case R.id.iv7:
			onNumber("7");
			break;
		case R.id.iv8:
			onNumber("8");
			break;
		case R.id.iv9:
			onNumber("9");
			break;
		case R.id.ivDel:
			onDel();
			break;
		case R.id.ivSubmit:
			listener.onSubmit();
			break;
		case R.id.ivClear:
			onCliear();
			break;
		case R.id.widgetKeyboard:
			onWidget();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-6 下午4:36:55  
	 * @Description: 点击显示键盘
	 */
	private void onWidget() {
		if(llKeyboardMain.getVisibility() != View.VISIBLE) {
			TranslateAnimation mShowAction = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
					0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			mShowAction.setDuration(500);
			llKeyboardMain.startAnimation(mShowAction);     
			llKeyboardMain.setVisibility(View.VISIBLE);  
//			widgetKeyboard.setVisibility(View.GONE);
		} else {
			TranslateAnimation mShowAction = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
					0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 1.0f);
			mShowAction.setDuration(500);
			llKeyboardMain.startAnimation(mShowAction);     
			llKeyboardMain.setVisibility(View.GONE);  
		}
	}

	/**
	 * 
	 * @Author:Huangweicai
	 * @Date:2015-7-28 下午9:37:32
	 * @Reason:
	 * @param num
	 */
	public void onNumber(String num) {
		if(!numberEditable){return;}
//		if (mEditView == null) throw (new RuntimeException("未设置EditView,请调用setEditView函数"));
		if(mEditView == null){ return; }
		if (InputType.isSame(InputType.TYPE_INPUT_NORMAL, inputType))// 正常数字
		{
			String text = Tools.inputNumber(mEditView.getText().toString(), num);
			mEditView.setText(text);
			if (onKeyChanged != null) {
				onKeyChanged.onTextChanged(text);
			}
		} else if (InputType.isSame(InputType.TYPE_INPUT_MONEY, inputType))// 金额
		{
			String text = Tools.inputMoney(mEditView.getText().toString(), num);
			mEditView.setText(text);
			if (onKeyChanged != null) {
				onKeyChanged.onTextChanged(text);
			}
		} else if (InputType.isSame(InputType.TYPE_INPUT_PWD, inputType))// 密码
		{
			
		}
	}

	/**
	 * 
	 * @Author:Huangweicai
	 * @Date:2015-7-29 上午8:53:55
	 * @Reason:清空
	 */
	private void onCliear() {
		if (InputType.isSame(InputType.TYPE_INPUT_NORMAL, inputType))// 正常数字
		{
			mEditView.setText("0");
			if (onKeyChanged != null) {
				onKeyChanged.onTextChanged("0");
			}
		} else if (InputType.isSame(InputType.TYPE_INPUT_MONEY, inputType))// 金额
		{
			mEditView.setText("0.00");
			if (onKeyChanged != null) {
				onKeyChanged.onTextChanged("0.00");
			}
		} else if (InputType.isSame(InputType.TYPE_INPUT_PWD, inputType))// 密码
		{
			mEditView.setText("");
			if (onKeyChanged != null) {
				onKeyChanged.onTextChanged("");
			}
		}
	}

	/**
	 * 
	 * @Author:Huangweicai
	 * @Date:2015-7-29 上午8:55:39
	 * @Reason:删除
	 */
	public void onDel() {
		if(!numberEditable){return;}
		if(mEditView == null){
			return;
		}
		String inputStr = mEditView.getText().toString(); //除bug 可能会导致崩了 wu
		if(TextUtils.isEmpty(inputStr)){
			return;
		}
		if (InputType.isSame(InputType.TYPE_INPUT_NORMAL, inputType))// 正常数字
		{
			String text = Tools.deleteNumber(inputStr);
			mEditView.setText(text);
			if(onKeyChanged != null){
				onKeyChanged.onTextChanged(text);
			}
		} else if (InputType.isSame(InputType.TYPE_INPUT_MONEY, inputType))// 金额
		{
			String text = Tools.deleteMoney(inputStr);
			mEditView.setText(text);
			if(onKeyChanged != null){
				onKeyChanged.onTextChanged(text);
			}
		} else if (InputType.isSame(InputType.TYPE_INPUT_PWD, inputType))// 密码
		{

		}
	}

	@Override
	public boolean onLongClick(View v) {
		if(!numberEditable){return false;}
		if(mEditView == null){
			return false;
		}
		mEditView.setText("");
		return true;
	}

}
