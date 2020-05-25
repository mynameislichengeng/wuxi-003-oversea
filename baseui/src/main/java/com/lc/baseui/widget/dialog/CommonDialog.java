package com.lc.baseui.widget.dialog;//package com.haoqee.humanaffair.wiget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lc.baseui.R;
import com.lc.baseui.constants.UIStyleEnum;
import com.lc.baseui.widget.ed.EditViewUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Description:常用DIALOG
 * @author: zhuchunlin@uzoo.cn
 */
public class CommonDialog extends Dialog {
    private static final String TAG = CommonDialog.class.getSimpleName();


    private TextView dialog_title_tv, dialog_content_tv;
    private Button dialog_left_btn, dialog_right_btn;
    private EditText dialog_ed_content;
    private UIStyleEnum uiStyle = UIStyleEnum.TEXTVIEW;


    /**
     * @param context     上下文
     * @param uiStyleEnum 用于判断要显示的类型
     **/
    public CommonDialog(Context context, UIStyleEnum uiStyleEnum) {
        super(context, R.style.common_dialog);
        uiStyle = uiStyleEnum;
    }

    public CommonDialog(Context context) {
        super(context, R.style.common_dialog);
    }

    public CommonDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_toptitle_middlecontent_bottom_two_btn);
        initView();
    }

    protected void initView() {
        dialog_title_tv = (TextView) findViewById(R.id.dialog_title_tv);
        dialog_left_btn = (Button) findViewById(R.id.dialog_left_btn);
        dialog_right_btn = (Button) findViewById(R.id.dialog_right_btn);
        dialog_left_btn.setOnClickListener(clickListener);
        dialog_right_btn.setOnClickListener(clickListener);
        initContent();
    }

    /**
     * 初始化中间的内容
     **/
    private void initContent() {
        dialog_content_tv = (TextView) findViewById(R.id.dialog_content_tv);
        dialog_ed_content = (EditText) findViewById(R.id.dialog_ed_content);
        switch (uiStyle) {
            case NULLVIEW:
                break;
            case EDITVIEW:
                dialog_ed_content.setVisibility(View.VISIBLE);
                break;
            case EDITVIEW_NUM_ZIMU:
                settingEditViewNumAndChar();
                break;
            default:
                dialog_content_tv.setVisibility(View.VISIBLE);
                break;
        }
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CommonDialog.this.dismiss();
        }
    };

    public void setCommonTitle(String title) {
        if (title != null) {
            dialog_title_tv.setText(title);
        }
    }

    /**
     * 设置中间的值
     **/
    public void setContent(String content) {
        if (TextUtils.isEmpty(content)) {
            content = "";
        }
        switch (uiStyle) {
            case EDITVIEW:
            case EDITVIEW_NUM_ZIMU:
                dialog_ed_content.setText(content);
                dialog_ed_content.setSelection(content.length());
                break;
            default:
                dialog_content_tv.setText(content);
                break;
        }

    }

    /**
     * 获得中间的值
     **/
    public String getContent() {
        String content;
        switch (uiStyle) {
            case EDITVIEW_NUM_ZIMU:
            case EDITVIEW:
                content = dialog_ed_content.getText().toString();
                break;
            default:
                content = dialog_content_tv.getText().toString();
                break;
        }
        return content;
    }

    public void setLeftBtnText(String leftBtnText) {
        dialog_left_btn.setText(leftBtnText);
    }

    public void setRightBtnText(String rightBtnText) {
        dialog_right_btn.setText(rightBtnText);
    }

    public void setLeftBtnClick(View.OnClickListener clickListener) {
        if (clickListener == null) {
            return;
        }
        dialog_left_btn.setOnClickListener(clickListener);
    }

    public void setRightBtnClick(View.OnClickListener clickListener) {
        if (clickListener == null) {
            return;
        }
        dialog_right_btn.setOnClickListener(clickListener);
    }

    public EditText getDialog_ed_content() {
        return dialog_ed_content;
    }

    /**
     * 设置只能输入字母[a~z][A~Z]或者数字[0~9]
     */
    private void settingEditViewNumAndChar() {
        dialog_ed_content.setVisibility(View.VISIBLE);
        EditViewUtil.setttingEditNumAndChar(dialog_ed_content);

    }


}
