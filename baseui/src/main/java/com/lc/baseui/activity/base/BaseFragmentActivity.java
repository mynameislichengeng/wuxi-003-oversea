package com.lc.baseui.activity.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.lc.baseui.task.BaseUIModuleManager;
import com.lc.baseui.widget.progress.CirclePointProgressDialog;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by Administrator on 2017/4/6.
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements ConstantsInterface {

    protected CirclePointProgressDialog circlePointProgressDialog;

    /**
     * 进入activity，加载布局前的调用的方法
     **/
    public abstract void initData();

    public abstract int getLayout();

    /**
     * 加载完布局文件后，调用的方法
     **/
    public abstract void initView();

    //表示是添加还是编辑
    protected String operate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(getLayout());
        initView();

    }

    /**
     * toast弹窗
     **/
    public void toast(int res) {
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
    }

    /**
     * toast弹窗
     **/
    public void toast(String tips) {
        Toast.makeText(this, tips, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示进度的dialog
     **/
    public void showProgressDialog(String msg) {
        hiddenProgressDialog();
        if (circlePointProgressDialog == null) {
            circlePointProgressDialog = CirclePointProgressDialog.createDialog(this);
        }
        circlePointProgressDialog.setMessage(msg);
        circlePointProgressDialog.show();
    }

    /**
     * 显示进度的dialog
     **/
    public void showProgressDialog(int msg) {
        hiddenProgressDialog();
        if (circlePointProgressDialog == null) {
            circlePointProgressDialog = CirclePointProgressDialog.createDialog(this);
        }
        circlePointProgressDialog.setMessage(getString(msg));
        circlePointProgressDialog.show();
    }

    /**
     * 隐藏进度的dialog
     **/
    public void hiddenProgressDialog() {
        if (circlePointProgressDialog != null) {
            circlePointProgressDialog.dismiss();
        }
    }

    /**
     * 是否是编辑
     **/
    public boolean isOperateEdit() {
        return operate.equals(OPERATE_UPDATE) ? true : false;
    }

    /**
     * 是否是添加
     **/
    public boolean isOperateAdd() {
        return operate.equals(OPERATE_ADD) ? true : false;
    }

    /**
     * 是否是查询
     **/
    public boolean isOperateQuery() {
        return operate.equals(OPERATE_QUERY) ? true : false;
    }

    /**
     * 跳转到activity
     *
     * @param action 动作
     **/
    public void startActivityMaster(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        startActivity(intent);
    }

    /**
     * 跳转到activity,而且带有值
     *
     * @param map    传递值的关键字
     * @param action 跳转界面的
     **/
    public void startActivityMaster(HashMap<String, Object> map, String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            Object val = entry.getValue();
            if (val instanceof Boolean) {
                intent.putExtra(key, (Boolean) val);
            } else if (val instanceof String) {
                intent.putExtra(key, (String) val);
            } else if (val instanceof Integer) {
                intent.putExtra(key, (Integer) val);
            } else if (val instanceof Parcelable) {
                intent.putExtra(key, (Parcelable) val);
            } else if (val instanceof Serializable) {
                intent.putExtra(key, (Serializable) val);
            }
        }
        startActivity(intent);
    }

    /**
     * 获得当前的sessionId
     **/
    public String getSessionId() {
        return BaseUIModuleManager.getInstance().getSessionId();
    }

    /**
     * 获得当前是不是企业版
     **/
    public boolean isPersonal() {
        return BaseUIModuleManager.getInstance().isPersonal();
    }
}
