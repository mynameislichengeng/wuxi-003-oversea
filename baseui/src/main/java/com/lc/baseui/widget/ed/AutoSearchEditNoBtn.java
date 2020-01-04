package com.lc.baseui.widget.ed;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;

import com.lc.baseui.R;

/**
 * 搜索edit
 * Created by licheng on 2017/5/16.
 */

public class AutoSearchEditNoBtn extends RelativeLayout implements View.OnClickListener, TextWatcher {
    public AutoSearchEditNoBtn(Context context) {
        super(context);
        init(context);
    }

    public AutoSearchEditNoBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoSearchEditNoBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Context context;
    private AutoCompleteTextView autoEdit;//搜索框

    private ArrayAdapter<String> adapter;

    private SearchResultListener callback;

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.widget_customer_search_edit, null);
        autoEdit = (AutoCompleteTextView) view.findViewById(R.id.autoed_search);
        autoEdit.addTextChangedListener(this);
        addView(view);
    }


    /**
     * 搜索的响应事件
     **/
    @Override
    public void onClick(View v) {

    }

    public void setCallback(SearchResultListener callback) {
        this.callback = callback;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        switchEditAdapter(s.toString());
    }

    protected long currentTime;

    private synchronized void switchEditAdapter(final String s) {
        mHandler.removeCallbacksAndMessages(null);
        if (System.currentTimeMillis() - currentTime > 3 * 1000) {
            currentTime = System.currentTimeMillis();
            mHandler.sendEmptyMessage(0);
        } else {
            mHandler.sendEmptyMessageDelayed(0, System.currentTimeMillis() - currentTime);
        }


    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = autoEdit.getText().toString();
            if (callback != null) {
                callback.getAutoEditData(str);
            }
        }
    };

    public static interface SearchResultListener {
        /**
         * @param str 返回的autoEdit结果
         **/
        void getAutoEditData(String str);
    }


}
