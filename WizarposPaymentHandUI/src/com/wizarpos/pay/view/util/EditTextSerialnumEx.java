package com.wizarpos.pay.view.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class EditTextSerialnumEx extends EditText {
	public EditTextSerialnumEx(Context context) {
		super(context);
		this.setText("p");
	}

	public EditTextSerialnumEx(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setText("p");
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		System.out.println(event.getKeyCode());
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
			if (keyCode <= KeyEvent.KEYCODE_9 && keyCode >= KeyEvent.KEYCODE_0) {
				String inputStr = Tools.inputNumber(this.getText().toString(), String.valueOf(keyCode));
				if (getText().toString().length() == 0) {
					inputStr = "P" + inputStr;
				}
				setText(inputStr);

			} else if (keyCode == KeyEvent.KEYCODE_DEL) {
				String delStr = Tools.deleteNumber(this.getText().toString());
				setText(delStr);
				setSelection(this.getText().toString().length());
			}
			return true;
		}

		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}
}
