package com.wizarpos.pay.setting.activity;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.flyco.roundview.RoundTextView;
import com.ui.setting.CommonItem;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.TipsConfigInfo;
import com.wizarpos.pay.setting.presenter.TipsConfig;
import com.wizarpos.pay.ui.newui.NewMainActivity;
import com.motionpay.pay2.lite.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TipParameterSettingActivity extends BaseViewActivity {
    private SwitchCompat scCollectionTip;
    private SwitchCompat scCustomAmount;
    private SwitchCompat scSetPercentageAmount;
    private EditText etP1;
    private EditText etP2;
    private EditText etP3;
    private RoundTextView rtButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainView(R.layout.activity_tip_parameter_setting);
        setTitleText(getResources().getString(R.string.tipParameterSetting));
        showTitleBack();
        initView();
        initData();
    }

    private void initData() {
        String p1 = AppConfigHelper.getConfig(AppConfigDef.percentP1, "");
        String p2 = AppConfigHelper.getConfig(AppConfigDef.percentP2, "");
        String p3 = AppConfigHelper.getConfig(AppConfigDef.percentP3, "");
        etP1.setText(p1);
        etP2.setText(p2);
        etP3.setText(p3);
        String allowCustomSetting = AppConfigHelper.getConfig(AppConfigDef.tipsTerminalAllow);
        if (TextUtils.isEmpty(allowCustomSetting) || allowCustomSetting.contains(Constants.NOTALLOW)) {
            scCollectionTip.setEnabled(false);
            scCustomAmount.setEnabled(false);
            scSetPercentageAmount.setEnabled(false);
            rtButton.setEnabled(false);
            etP1.setEnabled(false);
            etP2.setEnabled(false);
            etP3.setEnabled(false);
        } else {
            String collectTips = AppConfigHelper.getConfig(AppConfigDef.collectTips);
            if (!TextUtils.isEmpty(collectTips) && collectTips.equals(Constants.COLLETOFF)) {
                scCollectionTip.setChecked(false);
                scSetPercentageAmount.setChecked(false);
                scCustomAmount.setChecked(false);
                scCustomAmount.setEnabled(false);
                scSetPercentageAmount.setEnabled(false);
            } else {
                scCollectionTip.setChecked(true);
                scCustomAmount.setEnabled(true);
                scSetPercentageAmount.setEnabled(true);
            }
            String tipsPercentageAllow = AppConfigHelper.getConfig(AppConfigDef.tipsPercentageAllow);
            if (!TextUtils.isEmpty(tipsPercentageAllow) && tipsPercentageAllow.equals(Constants.ALLOW)) {
                scSetPercentageAmount.setChecked(true);
                etP1.setEnabled(true);
                etP2.setEnabled(true);
                etP3.setEnabled(true);
            } else {
                scSetPercentageAmount.setChecked(false);
                etP1.setEnabled(false);
                etP2.setEnabled(false);
                etP3.setEnabled(false);
            }
            String tipsCustomAllow = AppConfigHelper.getConfig(AppConfigDef.tipsCustomAllow);
            if (!TextUtils.isEmpty(tipsCustomAllow) && tipsCustomAllow.contains(Constants.ALLOW)) {
                scCustomAmount.setChecked(true);
            } else {
                scCustomAmount.setChecked(false);
            }
        }


    }

    private void initView() {
        etP1 = (EditText) findViewById(R.id.etP1);
        etP2 = (EditText) findViewById(R.id.etP2);
        etP3 = (EditText) findViewById(R.id.etP3);
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1) {
            hideKeyboard(etP1);
            hideKeyboard(etP2);
            hideKeyboard(etP3);
        }
        final CommonItem ciCollectionTip = ((CommonItem) findViewById(R.id.ciCollectionTip));
        scCollectionTip = ciCollectionTip.getScRight();
        scSetPercentageAmount = (SwitchCompat) findViewById(R.id.scSetPercentageAmount);
        CommonItem ciCustomAmount = (CommonItem) findViewById(R.id.ciCustomAmount);
        scCustomAmount = ciCustomAmount.getScRight();
        scCollectionTip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    scCollectionTip.setChecked(false);
                    scSetPercentageAmount.setChecked(false);
                    scCustomAmount.setChecked(false);
                    scCustomAmount.setEnabled(false);
                    scSetPercentageAmount.setEnabled(false);
                } else {
                    scCustomAmount.setEnabled(true);
                    scSetPercentageAmount.setEnabled(true);
                }
            }
        });
        scSetPercentageAmount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    AppConfigHelper.setConfig(AppConfigDef.tipsPercentageAllow,Constants.ALLOW);
                    etP1.setEnabled(true);
                    etP2.setEnabled(true);
                    etP3.setEnabled(true);
                } else {
//                    AppConfigHelper.setConfig(AppConfigDef.tipsPercentageAllow,Constants.NOTALLOW);
                    etP1.setEnabled(false);
                    etP2.setEnabled(false);
                    etP3.setEnabled(false);
                }
            }
        });
//        scCustomAmount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    AppConfigHelper.setConfig(AppConfigDef.tipsCustomAllow, Constants.ALLOW);
//                } else {
//                    AppConfigHelper.setConfig(AppConfigDef.tipsCustomAllow, Constants.NOTALLOW);
//                }
//
//            }
//        });
        rtButton = (RoundTextView) findViewById(R.id.rtButton);
        rtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipsConfig tipsConfig = new TipsConfig();
                TipsConfigInfo tipsConfigInfo = new TipsConfigInfo();
                if (scCollectionTip.isChecked()) {
                    tipsConfigInfo.setCollectTips(Constants.COLLETON);
                } else {
                    tipsConfigInfo.setCollectTips(Constants.COLLETOFF);
                }
                if (scSetPercentageAmount.isChecked()) {
                    tipsConfigInfo.setTipsPercentageAllow(Constants.ALLOW);
                } else {
                    tipsConfigInfo.setTipsPercentageAllow(Constants.NOTALLOW);
                }
                if (scCustomAmount.isChecked()) {
                    tipsConfigInfo.setTipsCustomAllow(Constants.ALLOW);
                } else {
                    tipsConfigInfo.setTipsCustomAllow(Constants.NOTALLOW);
                }
                Map<String, Object> precent = new HashMap<>();
                int p1 = 0;
                int p2 = 0;
                int p3 = 0;
                if (!TextUtils.isEmpty(etP1.getText().toString().trim())) {
                    p1 = Integer.parseInt(etP1.getText().toString().trim());
                    etP1.setSelection(etP1.getText().toString().length());
                }
                if (!TextUtils.isEmpty(etP2.getText().toString().trim())) {
                    p2 = Integer.parseInt(etP2.getText().toString().trim());
                    etP2.setSelection(etP2.getText().toString().length());
                }
                if (!TextUtils.isEmpty(etP3.getText().toString().trim())) {
                    p3 = Integer.parseInt(etP3.getText().toString().trim());
                    etP3.setSelection(etP3.getText().toString().length());
                }
                if (p1 <= p2 && p2 <= p3 && p3 <= 100) {
                    precent.put("p1", etP1.getText().toString());
                    precent.put("p2", etP2.getText().toString());
                    precent.put("p3", etP3.getText().toString());
                    tipsConfigInfo.setTipsPercentage(JSON.toJSONString(precent));
                } else {
                    Toast.makeText(TipParameterSettingActivity.this, "Please input correct percentage", Toast.LENGTH_SHORT).show();
                    return;
                }
                progresser.showProgress();
                tipsConfig.upLoadTips(tipsConfigInfo, new ResponseListener() {
                    @Override
                    public void onSuccess(Response response) {
                        startActivity(NewMainActivity.getStartIntent(TipParameterSettingActivity.this));
                        saveData();
                        finish();
                    }

                    @Override
                    public void onFaild(Response response) {
                        progresser.showContent();
                        Toast.makeText(TipParameterSettingActivity.this, response.msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void saveData() {
        AppConfigHelper.setConfig(AppConfigDef.percentP1, etP1.getText().toString());
        AppConfigHelper.setConfig(AppConfigDef.percentP2, etP2.getText().toString());
        AppConfigHelper.setConfig(AppConfigDef.percentP3, etP3.getText().toString());
        if (scCollectionTip.isChecked()) {
            AppConfigHelper.setConfig(AppConfigDef.collectTips, Constants.COLLETON);
        } else {
            AppConfigHelper.setConfig(AppConfigDef.collectTips, Constants.COLLETOFF);
        }
        if (scSetPercentageAmount.isChecked()) {
            AppConfigHelper.setConfig(AppConfigDef.tipsPercentageAllow, Constants.ALLOW);
        } else {
            AppConfigHelper.setConfig(AppConfigDef.tipsPercentageAllow, Constants.NOTALLOW);
        }
        if (scCustomAmount.isChecked()) {
            AppConfigHelper.setConfig(AppConfigDef.tipsCustomAllow, Constants.ALLOW);
        } else {
            AppConfigHelper.setConfig(AppConfigDef.tipsCustomAllow, Constants.NOTALLOW);
        }
    }

    private void hideKeyboard(EditText editText) {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT <= 10) {
            // 点击EditText，屏蔽默认输入法
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            // 点击EditText，隐藏系统输入法
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method method = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);// 4.0的是setShowSoftInputOnFocus，4.2的是setSoftInputShownOnFocus
                method.setAccessible(false);
                method.invoke(editText, false);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onTitleBackClikced() {
        startActivity(NewMainActivity.getStartIntent(TipParameterSettingActivity.this));
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onTitleBackClikced();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
