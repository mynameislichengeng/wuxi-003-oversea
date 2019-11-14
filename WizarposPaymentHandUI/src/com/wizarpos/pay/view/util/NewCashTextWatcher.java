package com.wizarpos.pay.view.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @Author: yaosong
 * @Description:现金输入限制
 */
public class NewCashTextWatcher implements TextWatcher {
    public final static String digits = "`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#%……&*（）——+|{}【】‘；：”“’。，、？\t\n";// 要过滤掉的字符

    private String tmp = "";
    private EditText mEditText;
    private EditTextChange mEditTextChange;
    public NewCashTextWatcher(EditText et) {
        mEditText = et;
    }
    public NewCashTextWatcher(EditText et, EditTextChange editTextChange) {
        mEditText = et;
        this.mEditTextChange = editTextChange;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mEditText.setSelection(s.length());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        tmp = s.toString();
    }

    @Override
    public void afterTextChanged(Editable s) {
        String str = s.toString();
        if (str.equals(tmp)) {
            return;
        }
        String temp = "0.00";
        for (int i = 0; i < str.length(); i++) {
            temp = Tools.inputMoney(temp, str.charAt(i) + "");
        }
        tmp = temp;
        mEditText.setText(tmp);
        if (mEditTextChange != null) {
            mEditTextChange.dataChange(temp);
        }
    }

    public interface EditTextChange {
        void dataChange(String string);
    }
}