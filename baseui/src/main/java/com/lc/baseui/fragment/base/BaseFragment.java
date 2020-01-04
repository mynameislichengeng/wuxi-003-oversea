package com.lc.baseui.fragment.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.lc.baseui.task.BaseUIModuleManager;
import com.lc.baseui.widget.progress.CirclePointProgressDialog;

/**
 * Created by Administrator on 2017/4/16.
 */

public abstract class BaseFragment extends Fragment {

    protected CirclePointProgressDialog circlePointProgressDialog;


    /**
     * 显示进度的dialog
     **/
    public void showProgressDialog(String msg) {

        hiddenProgressDialog();
        if (circlePointProgressDialog == null) {
            circlePointProgressDialog = CirclePointProgressDialog.createDialog(getContext());
        }
        circlePointProgressDialog.setMessage(msg);
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
     * toast弹窗
     **/
    public void toast(int res) {
        Toast.makeText(getContext(), res, Toast.LENGTH_SHORT).show();
    }

    /**
     * toast弹窗
     **/
    public void toast(String tips) {
        Toast.makeText(getContext(), tips, Toast.LENGTH_SHORT).show();
    }
    /**
     * 获得当前的sessionId
     * **/
    public String getSessionId(){
        return BaseUIModuleManager.getInstance().getSessionId();
    }

    /**
     * 获得当前是不是企业版
     * **/
    public boolean isPersonal(){
        return BaseUIModuleManager.getInstance().isPersonal();
    }
}
