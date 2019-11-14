package com.wizarpos.pay.view.fragment.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.wizarpos.pay.ui.ErrorView.OnRetryListener;
import com.wizarpos.pay.ui.ProgressLayout;
import com.wizarpos.pay2.lite.R;

public abstract class BaseViewFragment extends Fragment implements OnRetryListener, OnClickListener {

	private Toolbar toolbar;
	private TextView tvTitle;
	private TextView tvRight;
	private ImageView ivLeft;
	private RelativeLayout rlMain,rlRight;
	protected ProgressLayout progresser;

	protected Spinner spToolbarTitle;

	protected View mainView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_base, container, false);
		initToolbar(rootView);
		rlMain = (RelativeLayout) rootView.findViewById(R.id.rlMain);
		progresser = (ProgressLayout) rootView.findViewById(R.id.progress);
		spToolbarTitle = (Spinner) rootView.findViewById(R.id.spToolbarTitle);
		progresser.setRetryListener(this);
		initView();
		return rootView;
	}

	public abstract void initView();

	protected void setMainView(int layoutId) {
		mainView = LayoutInflater.from(getActivity()).inflate(layoutId, null);
		setMainView(mainView);
	}

	protected void setMainView(View view) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		rlMain.addView(view, params);
		progresser.showContent();
	}

	/**
	 * 初始化状态栏
	 */
	protected void initToolbar(View view) {
		toolbar = (Toolbar) view.findViewById(R.id.toolbar);
		tvTitle = (TextView) toolbar.findViewById(R.id.tvToolbarTitle);
		ivLeft = (ImageView) toolbar.findViewById(R.id.ivToolbarLeft);
		tvRight = (TextView) toolbar.findViewById(R.id.tvToolbarRight);
		rlRight = (RelativeLayout) toolbar.findViewById(R.id.rlToolbarRight);
		((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
	}

	protected void showTitleLeftImg(String imgUrl){
//		if(!TextUtils.isEmpty(imgUrl)){
//			ImageLoader.getInstance().displayImage(imgUrl, ivLeft, ImageLoadApp.getOptions());
//			ivLeft.setVisibility(View.VISIBLE);
//		}
	}

	/**
	 * 显示标题栏左侧返回按钮
	 */
	protected void showTitleBack() {
		((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * 设置标题栏文字
	 * 
	 * @param title
	 */
	protected void setTitleText(int title) {
		tvTitle.setText(title);
		showToolbar();
	}

	/**
	 * 设置标题栏文字
	 * 
	 * @param title
	 */
	protected void setTitleText(CharSequence title) {
		tvTitle.setText(title);
		showToolbar();
	}

	public void showToolbar() {
		if (toolbar.getVisibility() != View.VISIBLE) {
			toolbar.setVisibility(View.VISIBLE);
		}
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	/**
	 * 设置标题栏右侧文字
	 * 
	 * @param title
	 */
	protected void setTitleRight(int title) {
		tvRight.setText(title);
		tvRight.setOnClickListener(this);
		rlRight.setOnClickListener(this);
		setTitleRightVisible(true);
	}

	/**
	 * 设置标题栏右侧文字并添加监听
	 * 
	 * @param title
	 */
	protected void setTitleRight(CharSequence title) {
		tvRight.setText(title);
		tvRight.setOnClickListener(this);
		rlRight.setOnClickListener(this);
		setTitleRightVisible(true);
	}

	/**
	 * 设置标题栏右侧文字并添加监听
	 * 
	 * @param title
	 */
	protected void setTitleRightImage(int title) {
		tvRight.setBackgroundResource(title);
		tvRight.setOnClickListener(this);
		rlRight.setOnClickListener(this);
		setTitleRightVisible(true);
	}

	protected void setTitleRightVisible(boolean flag) {
		if (flag) {
			tvRight.setVisibility(View.VISIBLE);
			rlRight.setVisibility(View.VISIBLE);
		} else {
			tvRight.setVisibility(View.GONE);
			rlRight.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvToolbarRight:
			rlRight.performClick();
			break;
		case R.id.rlToolbarRight:
			onTitleRightClicked();
		default:
			break;
		}
	}

	/**
	 * 点击返回按钮
	 */
	protected void onTitleBackClikced() {}

	/**
	 * 标题栏右侧文字被点击
	 */
	protected void onTitleRightClicked() {}

	@Override
	public void onRetry() {

	}

	public void refresh() {

	}
}
