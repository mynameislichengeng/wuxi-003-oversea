package com.wizarpos.pay.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;

import com.wizarpos.pay2.lite.R;

/**
 * 
 * @ClassName: CommonSelectDialog 
 * @author Huangweicai
 * @date 2015-8-31 下午8:27:14 
 * @Description: 这里用一句话描述这个类的作用
 */
public class CommonSelectDialog extends Dialog{

	//定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
            public void back(String name);
    }
    
    private String name;
    EditText etName;

    public CommonSelectDialog(Context context,String name) {
            super(context);
            this.name = name;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) { 
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_selected_micro);
            //设置标题
            setTitle(name);
    }
}
