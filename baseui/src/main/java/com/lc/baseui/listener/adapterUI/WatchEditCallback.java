package com.lc.baseui.listener.adapterUI;

/**
 * Created by licheng on 2017/5/1.
 */

import android.text.Editable;
import android.text.TextWatcher;

import com.lc.baseui.tools.lg;

import java.util.HashMap;

/**
 * 监听edit变化
 **/
public class WatchEditCallback implements TextWatcher {
    private String TAG = WatchEditCallback.class.getSimpleName();
    protected HashMap<String, String> map;
    protected String field;
    protected boolean isChange = true;

    public WatchEditCallback(HashMap<String, String> map, String field) {
        this.map = map;
        this.field = field;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        lg.d(lg.TAG, TAG + "-----beforeTextChanged----s:" + s.toString());
        if (isChange) {
            map.put(field, s.toString());
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        lg.d(lg.TAG, TAG + "-----beforeTextChanged----s:" + s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
        lg.d(lg.TAG, TAG + "-----beforeTextChanged----s:" + s.toString());
        if (isChange) {
            map.put(field, s.toString());
        }

    }

    public void setChange(boolean change) {
        isChange = change;
    }
}
