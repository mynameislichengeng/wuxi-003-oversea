package com.lc.baseui.listener.adapterUI;

import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;

/**
 *
 * 专门在spinner中使用，监听
 * Created by licheng on 2017/5/1.
 */

public class SpinnerSelectItemCallback implements AdapterView.OnItemSelectedListener {


    private String[] values;
    private HashMap<String, String> map;
    private String field;

    public SpinnerSelectItemCallback(String[] values, HashMap<String, String> map, String field) {
        this.values = values;
        this.map = map;
        this.field = field;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = values[position];
        map.put(field, value);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
