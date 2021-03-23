package com.lc.baseui.activity.base;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lc.baseui.R;


/**
 * Created by Administrator on 2017/4/6.
 */

public abstract class TitleFragmentActivity extends BaseFragmentActivity {

    /**
     * 加载的布局文件
     **/
    public abstract int getContentLayout();

    /**
     * 加载完布局文件后的操作
     *
     * @param view 加载的整体的布局文件
     **/
    public abstract void init(View view);

    //
    protected RelativeLayout relHeadRoot, rel_base_main;
    protected FrameLayout fl_left_head, fl_center_head, fl_right_head;
    protected Button btn_left, btn_right;
    protected TextView tv_title, tv_right;
    protected View viewMain;

    @Override
    public int getLayout() {
        return R.layout.base_activity;
    }

    @Override
    public void initView() {
        initTitle();
        //添加中间的布局
        initMainView();
        init(viewMain);
    }

    /**
     * 初始化title
     **/
    private void initTitle() {
        //根
        relHeadRoot = (RelativeLayout) findViewById(R.id.rel__base_head_root);
        //左边
        fl_left_head = (FrameLayout) findViewById(R.id.fl_left_head);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_left.setOnClickListener(leftClick);
        //中间
        fl_center_head = (FrameLayout) findViewById(R.id.fl_center_head);
        tv_title = (TextView) findViewById(R.id.tv_title);
        //右边
        fl_right_head = (FrameLayout) findViewById(R.id.fl_right_head);
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_right.setOnClickListener(rightClick);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setOnClickListener(rightClick);

    }

    /**
     * 初始化中间主布局
     **/
    private void initMainView() {
        rel_base_main = (RelativeLayout) findViewById(R.id.rel_base_main);
        viewMain = LayoutInflater.from(this).inflate(getContentLayout(), null);
        rel_base_main.addView(viewMain);
        rel_base_main.setBackgroundColor(getResources().getColor(R.color.bg_main_white));
    }


    /**
     * 左边，响应事件
     **/
    private View.OnClickListener leftClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onLeftClick();
        }
    };
    /**
     * 右边，响应事件
     **/
    private View.OnClickListener rightClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRightClick();
        }
    };

    /**
     * 设置主界面的背景颜色
     *
     * @param res 资源文件
     **/
    protected void setMainLayoutBg(int res) {
        rel_base_main.setBackgroundResource(res);
    }

    /**
     * 左边按钮响应事件
     **/
    protected void onLeftClick() {
        finish();
    }

    /**
     * 中间的title
     *
     * @param title 标题
     **/
    protected void setTitleCenter(String title) {
        tv_title.setText(title);
        tv_title.setVisibility(View.VISIBLE);
    }

    /**
     * 中间的title
     *
     * @param res 标题,资源文件
     **/
    protected void setTitleCenter(int res) {
        tv_title.setText(res);
        tv_title.setVisibility(View.VISIBLE);
    }

    /**
     * 右边的btn，响应事件
     **/
    protected void onRightClick() {
        finish();
    }

    /**
     * 右边的btn，文字
     *
     * @param res 标题,资源文件
     **/
    protected void setRightText(int res) {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(res);
    }

    /**
     * 右边的btn，文字
     *
     * @param text 标题
     **/
    protected void setRightText(String text) {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(text);
    }

    /**
     * 右边的tag
     *
     * @param tag tag
     **/
    protected void setRightTag(String tag) {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setTag(tag);
    }

    protected Object getRightTag() {
        return tv_right.getTag();
    }

    protected void setLeftTitleRootVisible(int visible) {
        fl_left_head.setVisibility(visible);
    }

}
