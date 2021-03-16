package com.wizarpos.pay.cashier.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.motionpay.pay2.lite.R;

/**
 * @author Huangweicai
 * @ClassName: SignLoginActivity
 * @date 2015-9-2 下午5:06:51
 * @Description: 银行签到
 */
public class SignLoginActivity extends BaseViewActivity implements OnClickListener {
    private final String LOG_TAG = SignLoginActivity.class.getName();
    private EditText etSignOnOptCode, etSignOnPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainView(R.layout.activity_sign_on);
        setTitleText("签到");
        showTitleBack();
        initView();
    }

    private void initView() {
        this.findViewById(R.id.btn_login).setOnClickListener(this);
        etSignOnOptCode = (EditText) this.findViewById(R.id.etSignOnOptCode);
        etSignOnPwd = (EditText) this.findViewById(R.id.etSignOnPwd);

        //增加逻辑, 自动重新签到, 如果失败, 则让用户自行输入操作员和密码 wu
        progresser.showProgress();
        signOn("01", "0000");
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_login:
                final String optCode = etSignOnOptCode.getText().toString();
                final String optPass = etSignOnPwd.getText().toString();
                if (TextUtils.isEmpty(optCode) || TextUtils.isEmpty(optPass)) {
                    Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                signOn(optCode, optPass);
                break;
        }
    }

    /**
     * @Author: Huangweicai
     * @date 2015-9-2 下午5:12:54
     * @Description: 签到
     */
    private void signOn(final String optCode, final String optPass) {

    }

    public void retry() {
        etSignOnOptCode.setText("");
        etSignOnPwd.setText("");
    }
}
