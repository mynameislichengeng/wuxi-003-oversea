package com.wizarpos.pay.ui;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-12-2 下午8:53:06
 * @Description:倒计时控件
 */
public class TimeTextViewEx extends TextView implements OnClickListener{
    
    /** 延迟时间*/
    private long delayMillis = 1 * 1000;
    /** 总共时间*/
    private long totalMillis = 10 * 1000;
    private long remainMillis = totalMillis;
    
    private Handler mHandler = new Handler();
    /** 需要更新的文字*/
    private String updateTextAfter = "秒后再发送";
    
    private String showTxt = "获取验证码";

    public TimeTextViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }
    
    public TimeTextViewEx(Context context) {
        super(context);
        setOnClickListener(this);
    }
    

    @Override
    public void onClick(View v) {
        v.setEnabled(false);
        remainMillis = totalMillis;
        this.setText(remainMillis/1000 + updateTextAfter);
        mHandler.postDelayed(runnable, delayMillis);
    }
    
    private Runnable runnable = new Runnable() {
        
        @Override
        public void run() {
            if(!updateView())
            {
                return;
            }
            mHandler.postDelayed(this, delayMillis);
        }
    };
    
    /**
     * 
     * @Author: Huangweicai
     * @date 2015-12-2 下午9:33:19  
     * @Description:初始化参数 移除监听(在请求失败后可以调用此方法)
     */
    public void init()
    {
        this.setEnabled(true);
        this.setText(showTxt);
        remainMillis = totalMillis;
        mHandler.removeCallbacks(runnable);
    }
    
    /**
     * 
     * @Author: Huangweicai
     * @date 2015-12-2 下午9:34:00 
     * @return 
     * @Description:更新UI,若时间小于等于 {@link #remainMillis}时间 则初始化
     */
    private boolean updateView()
    {
        remainMillis -= 1000;
        if (remainMillis <= 0) 
        {
            this.setEnabled(true);
            init();
            return false;
        }
        this.setText(remainMillis / 1000 + updateTextAfter);
        return true;
    }
    
    public void onDestroy()
    {
        mHandler.removeCallbacks(runnable);
        mHandler = null;
    }
}
