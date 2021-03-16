package com.wizarpos.pay.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ui.utils.Tools;
import com.motionpay.pay2.lite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 苏震 on 2016/8/30.
 */
public class InputPad extends RelativeLayout implements View.OnClickListener, View.OnFocusChangeListener {

    public enum InputType {
        TYPE_INPUT_NORMAL(1), // 正常
        TYPE_INPUT_NO_DEFAULT(2), // 密码
        TYPE_INPUT_MONEY(3);// 金额

        private final int m_iEnumValue;

        InputType(int iValue) {
            this.m_iEnumValue = iValue;
        }

        public int getValue() {
            return m_iEnumValue;
        }

        public static boolean isSame(InputType oriInputType, InputType secInputType) {
            return secInputType == oriInputType;
        }

    }

    private OnKeyChangedListener onKeyChanged;
    /**
     * 确认监听
     */
    private OnComfirmListener onConfirmListener;
    private List<EditTarget> editTargets;
    private EditTarget currentEditTarget;

    public InputPad(Context context) {
        super(context);
        init();
    }

    public InputPad(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InputPad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnTextChangedListener(OnKeyChangedListener onKeyChanged) {
        this.onKeyChanged = onKeyChanged;
    }

    public void setOnConfirmListener(OnComfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    private void init() {
        editTargets = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inputPadView = inflater.inflate(R.layout.layout_inputpad, this, false);
        Button btn0 = ((Button) inputPadView.findViewById(com.ui.baseview.R.id.btn0));
        Button btn1 = ((Button) inputPadView.findViewById(com.ui.baseview.R.id.btn1));
        Button btn2 = ((Button) inputPadView.findViewById(com.ui.baseview.R.id.btn2));
        Button btn3 = ((Button) inputPadView.findViewById(com.ui.baseview.R.id.btn3));
        Button btn4 = ((Button) inputPadView.findViewById(com.ui.baseview.R.id.btn4));
        Button btn5 = ((Button) inputPadView.findViewById(com.ui.baseview.R.id.btn5));
        Button btn6 = ((Button) inputPadView.findViewById(com.ui.baseview.R.id.btn6));
        Button btn7 = ((Button) inputPadView.findViewById(com.ui.baseview.R.id.btn7));
        Button btn8 = ((Button) inputPadView.findViewById(com.ui.baseview.R.id.btn8));
        Button btn9 = ((Button) inputPadView.findViewById(com.ui.baseview.R.id.btn9));
        Button btnClear = ((Button) inputPadView.findViewById(com.ui.baseview.R.id.btnClear));
        Button btnSubmit = ((Button) inputPadView.findViewById(com.ui.baseview.R.id.btnSubmit));
        this.addView(inputPadView);
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    /**
     * 添加 edittext
     */
    public void addEditView(EditText editText, InputType inputType) {
        addEditView(editText, inputType, true);
    }

    /**
     * 添加 edittext
     */
    public void addEditView(EditText editText, InputType inputType, boolean editable) {
        boolean isAdded = false;
        for (EditTarget editTarget : editTargets) {
            if (editText == editTarget._editView) {
                editTarget._inputType = inputType;
                editTarget._editable = editable;
                isAdded = true;
                break;
            }
        }
        if (!isAdded) {
            EditTarget editTarget = new EditTarget(editText, inputType, editable);
            editText.setOnFocusChangeListener(this);
//            editText.setInputType(EditorInfo.TYPE_NULL);
            this.editTargets.add(editTarget);
            if (InputType.isSame(InputType.TYPE_INPUT_NORMAL, inputType))// 正常数字
            {
                editText.setText("0");
                if (onKeyChanged != null) {
                    onKeyChanged.onTextChanged(editText, "0");
                }
            } else if (InputType.isSame(InputType.TYPE_INPUT_MONEY, inputType))// 金额
            {
                editText.setText("0.00");
                if (onKeyChanged != null) {
                    onKeyChanged.onTextChanged(editText, "0.00");
                }
            } else if (InputType.isSame(InputType.TYPE_INPUT_NO_DEFAULT, inputType)) {
                editText.setText("");
                if (onKeyChanged != null) {
                    onKeyChanged.onTextChanged(editText, "");
                }
            }
//            else if (InputType.isSame(InputType.TYPE_INPUT_PWD, inputType))// 密码
//            {
//                editText.setText("");
//                if (onKeyChanged != null) {
//                    onKeyChanged.formatText(editText, "");
//                }
//            }
        }
        if (currentEditTarget == null) {
            currentEditTarget = editTargets.get(0); //默认值
        }
    }

    /**
     * 设置输入类型
     */
    public void setInputType(EditText editView, InputType inputType) {
        for (EditTarget editTarget : editTargets) {
            if (editView == editTarget._editView) {
                editTarget._inputType = inputType;
                break;
            }
        }
    }

    /**
     * 设置是否可编辑
     */
    public void setEditable(EditText editText, boolean editable) {
        for (EditTarget editTarget : editTargets) {
            if (editText == editTarget._editView) {
                editTarget._editable = editable;
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == com.ui.baseview.R.id.btn0) {
            onNumber("0");
        } else if (i == com.ui.baseview.R.id.btn1) {
            onNumber("1");
        } else if (i == com.ui.baseview.R.id.btn2) {
            onNumber("2");
        } else if (i == com.ui.baseview.R.id.btn3) {
            onNumber("3");
        } else if (i == com.ui.baseview.R.id.btn4) {
            onNumber("4");
        } else if (i == com.ui.baseview.R.id.btn5) {
            onNumber("5");
        } else if (i == com.ui.baseview.R.id.btn6) {
            onNumber("6");
        } else if (i == com.ui.baseview.R.id.btn7) {
            onNumber("7");
        } else if (i == com.ui.baseview.R.id.btn8) {
            onNumber("8");
        } else if (i == com.ui.baseview.R.id.btn9) {
            onNumber("9");
        } else if (i == com.ui.baseview.R.id.btnClear) {
            onClear();
        } else if (i == com.ui.baseview.R.id.btnSubmit) {
            onConfirm();
        }
    }

    public void onNumber(String num) {
        if (currentEditTarget == null) {
            return;
        }
        if (!currentEditTarget._editable) {
            return;
        }
        EditText editText = currentEditTarget._editView;
        InputType inputType = currentEditTarget._inputType;
        if (InputType.isSame(InputType.TYPE_INPUT_NORMAL, inputType))// 正常数字
        {
            if (!TextUtils.isEmpty(num) && num.length() == 1) {
                char numChar = num.charAt(0);
                if (numChar <= '9' && numChar >= '0') {
                    String text = Tools.inputNumber(editText.getText().toString(), num);
                    editText.setText(text);
                    if (onKeyChanged != null) {
                        onKeyChanged.onTextChanged(editText, text);
                    }
                }
            }
        } else if (InputType.isSame(InputType.TYPE_INPUT_MONEY, inputType))// 金额
        {
            if (editText.getText().toString().length() >= 10) {
                return;
            }
            if (!TextUtils.isEmpty(num) && num.length() == 1) {
                char numChar = num.charAt(0);
                if (numChar <= '9' && numChar >= '0') {
                    String text = Tools.inputMoney(editText.getText().toString(), num);
                    editText.setText(text);
                    if (onKeyChanged != null) {
                        onKeyChanged.onTextChanged(editText, text);
                    }
                } else if ('.' == numChar) {
                    if (-1 == editText.getText().toString().indexOf('.')) {
                        editText.append(".");
                        if (onKeyChanged != null) {
                            onKeyChanged.onTextChanged(editText, editText.getText().toString());
                        }
                    }
                }
            }
        } else if (InputType.isSame(InputType.TYPE_INPUT_NO_DEFAULT, inputType)) {
            if (!TextUtils.isEmpty(num) && num.length() == 1) {
                char numChar = num.charAt(0);
                if (numChar <= '9' && numChar >= '0') {
                    String text = Tools.inputNumber(editText.getText().toString(), num);
                    editText.setText(text);
                    if (onKeyChanged != null) {
                        onKeyChanged.onTextChanged(editText, text);
                    }
                }
            }
        }
//        else if (InputType.isSame(InputType.TYPE_INPUT_PWD, inputType))// 密码
//        {
//            String text = Tools.inputNumber(editText.getText().toString().trim(), num);
//            editText.setText(text);
//            if (onKeyChanged != null) {
//                onKeyChanged.formatText(editText, text);
//            }
//        }
    }

    private String getPwdStr(int length) {
        String pwd = "";
        for (int i = 0; i < length; i++) {
            pwd += "*";
        }
        return pwd;
    }

    public void onConfirm() {
        if (currentEditTarget == null) {
            return;
        }
        if (onConfirmListener != null) {
            onConfirmListener.onConfirm(currentEditTarget._editView);
        }


    }

    public void onDel() {
        if (currentEditTarget == null) {
            return;
        }
        if (!currentEditTarget._editable) {
            return;
        }
        EditText editText = currentEditTarget._editView;
        InputType inputType = currentEditTarget._inputType;
        String inputStr = editText.getText().toString(); //除bug 可能会导致崩了
        if (TextUtils.isEmpty(inputStr)) {
            return;
        }
        if (InputType.isSame(InputType.TYPE_INPUT_NORMAL, inputType))// 正常数字
        {
            String text = Tools.deleteNumber(inputStr);
            editText.setText(text);
            if (onKeyChanged != null) {
                onKeyChanged.onTextChanged(editText, text);
            }
        } else if (InputType.isSame(InputType.TYPE_INPUT_MONEY, inputType))// 金额
        {
            String text = Tools.deleteMoney(inputStr);
            editText.setText(text);
            if (onKeyChanged != null) {
                onKeyChanged.onTextChanged(editText, text);
            }
        } else if (InputType.isSame(InputType.TYPE_INPUT_NO_DEFAULT, inputType)) {
            String text = Tools.deleteNumber(inputStr);
            editText.setText(text);
            if (onKeyChanged != null) {
                onKeyChanged.onTextChanged(editText, text);
            }
        }
//        else if (InputType.isSame(InputType.TYPE_INPUT_PWD, inputType))// 密码
//        {
//            String text = Tools.deleteNumber(inputStr);
//            editText.setText(text);
//            if (onKeyChanged != null) {
//                onKeyChanged.formatText(editText, text);
//            }
//        }
    }

    private void onClear() {
        if (currentEditTarget == null) {
            return;
        }
        if (!currentEditTarget._editable) {
            return;
        }
        EditText editText = currentEditTarget._editView;
        InputType inputType = currentEditTarget._inputType;
        if (InputType.isSame(InputType.TYPE_INPUT_NORMAL, inputType))// 正常数字
        {
            editText.setText("0");
            if (onKeyChanged != null) {
                onKeyChanged.onTextChanged(editText, "0");
            }
        } else if (InputType.isSame(InputType.TYPE_INPUT_MONEY, inputType))// 金额
        {
            editText.setText("0.00");
            if (onKeyChanged != null) {
                onKeyChanged.onTextChanged(editText, "0.00");
            }
        } else if (InputType.isSame(InputType.TYPE_INPUT_NO_DEFAULT, inputType)) {
            editText.setText("");
            if (onKeyChanged != null) {
                onKeyChanged.onTextChanged(editText, "");
            }
        }
//        else if (InputType.isSame(InputType.TYPE_INPUT_PWD, inputType))// 密码
//        {
//            editText.setText("");
//            if (onKeyChanged != null) {
//                onKeyChanged.formatText(editText, "");
//            }
//        }
    }


    private class EditTarget {
        EditText _editView;
        InputType _inputType = InputType.TYPE_INPUT_NORMAL;
        boolean _editable = true;

        EditTarget(EditText _mEditView, InputType _inputType, boolean editable) {
            this._editView = _mEditView;
            this._inputType = _inputType;
            this._editable = editable;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            for (EditTarget editTarget : editTargets) {
                if (editTarget._editView == v) {
                    if (!editTarget._editable) {
                        return;
                    }
                    currentEditTarget = editTarget;
                    break;
                }
            }
        }
    }

    public interface OnKeyChangedListener {
        void onTextChanged(EditText editText, String newStr);
    }

    public interface OnComfirmListener {
        void onConfirm(EditText editText);
    }
}
