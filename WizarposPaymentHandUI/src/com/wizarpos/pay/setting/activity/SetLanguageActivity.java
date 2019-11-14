package com.wizarpos.pay.setting.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.db.AppConfig;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.login.view.LoginMerchantRebuildActivity;
import com.wizarpos.pay.setting.util.LanguageUtils;
import com.wizarpos.pay2.lite.R;

import org.xclcharts.common.IFormatterDoubleCallBack;

public class SetLanguageActivity extends BaseViewActivity {

    protected Spinner spLanguage;
    protected int times;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SetLanguageActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainView(R.layout.activity_set_language);
        showTitleBack();
        setTitleText(getResources().getString(R.string.redmineInfo));
        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt("times", times);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        times = savedInstanceState.getInt("times");
    }

    private void initView() {
        spLanguage = (Spinner) findViewById(R.id.spLanguage);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.language_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLanguage.setAdapter(adapter);
        try {
            spLanguage.setSelection(LanguageUtils.getLanguage(this));
        } catch (Exception e) {
            spLanguage.setSelection(0);
        }
        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                times++;
                if (times == 1) {
                    return;
                }
                LanguageUtils.changeLanguage(SetLanguageActivity.this, position);

                reset();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void reset() {
        this.finish();
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    @Override
    protected void onTitleBackClikced() {
        super.onTitleBackClikced();
        back();
    }

    public void back() {
        startActivity(LoginMerchantRebuildActivity.getStartIntent(this));
        this.finish();
        overridePendingTransition(0, 0);
    }
}
