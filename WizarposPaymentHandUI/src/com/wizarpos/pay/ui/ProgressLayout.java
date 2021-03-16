package com.wizarpos.pay.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wizarpos.pay.common.base.BaseViewStateListener;
import com.wizarpos.pay.ui.ErrorView.OnRetryListener;
import com.motionpay.pay2.lite.R;

/**
 * Created by wu on 2015/4/16 0016.
 */
public class ProgressLayout extends RelativeLayout {

	private BaseViewStateListener baseViewStateListener;
	
	public void setBaseViewStateListener(BaseViewStateListener baseViewStateListener) {
		this.baseViewStateListener = baseViewStateListener;
	}
	
	private static final String TAG_PROGRESS = "ProgressLayout.TAG_PROGRESS";
	private static final String TAG_ERROR = "ProgressLayout.TAG_ERROR";

	public static enum State {
		CONTENT, PROGRESS, ERROR
	}

//	private ProgressBar indeterminateProgressLarge;
	private RelativeLayout mProgressView;
	private TextView tvProgressTips;
	private ErrorView mErrorView;
	private List<View> mContentViews = new ArrayList<View>();

	public void setRetryListener(OnRetryListener listener) {
		if (mErrorView != null) {
			mErrorView.setRetryListener(listener);
		}
	}

	private State mState = State.CONTENT;

	public ProgressLayout(Context context) {
		super(context);
	}

	public ProgressLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ProgressLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

		mErrorView = new ErrorView(getContext());
		mErrorView.setTag(TAG_ERROR);
		addView(mErrorView, layoutParams);

		mProgressView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_process, this, false);
		mProgressView.setTag(TAG_PROGRESS);
		tvProgressTips = (TextView) mProgressView.findViewById(R.id.tvProgressTips);
//		indeterminateProgressLarge = (ProgressBar) mProgressView.findViewById(R.id.indeterminate_progress_large_library);
//		indeterminateProgressLarge.setIndeterminateDrawable(new IndeterminateProgressDrawable(getContext()));
		layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(mProgressView, layoutParams);

		mProgressView.setVisibility(GONE);
		mErrorView.setVisibility(GONE);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		int childCount = getChildCount();
		if (childCount > 0) {
			for (int i = 0; i < childCount; i++) {
				addContentViewIfPassable(getChildAt(i));
			}
		}
	}

	@Override
	public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		super.addView(child, params);
		addContentViewIfPassable(child);
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
		addContentViewIfPassable(child);
	}

	private void addContentViewIfPassable(View child) {
		if (child.getTag() == null || (!child.getTag().equals(TAG_PROGRESS) && !child.getTag().equals(TAG_ERROR))) {
			mContentViews.add(child);
		}
	}

	public void showProgress(String tips) {
		if(TextUtils.isEmpty(tips)){
			tvProgressTips.setText("");
			tvProgressTips.setVisibility(View.GONE);
		}else{
			tvProgressTips.setText(tips);
			tvProgressTips.setVisibility(View.VISIBLE);
		}

		if(baseViewStateListener != null){ //隐藏标题栏 wu
			baseViewStateListener.hideTitleRightText();
		}
		switchState(State.PROGRESS, null, Collections.<Integer> emptyList());
	}

	public void showProgress() {
		showProgress(null);
	}

	// public void showProgress(List<Integer> skipIds) {
	// switchState(State.PROGRESS, null, skipIds);
	// }

	public void showError(boolean showRetry) {
		if(baseViewStateListener != null){ //显示标题栏 wu
			baseViewStateListener.showTitleRightText();
		}
		if (showRetry) {
			mErrorView.showRetry();
		} else {
			mErrorView.hideRetry();
		}
		showError(null, showRetry);
	}

	// public void showErrorText(List<Integer> skipIds) {
	// switchState(State.ERROR, null, skipIds);
	// }

	public void showError(String error, boolean showRetry) {
		switchState(State.ERROR, error, Collections.<Integer> emptyList());
	}

	public void showError(String error, int resId, boolean showRetry) {
		switchState(State.ERROR, error, resId, Collections.<Integer> emptyList());
	}

	// public void showErrorText(String error, List<Integer> skipIds) {
	// switchState(State.ERROR, error, skipIds);
	// }

	public void showContent() {
		if(baseViewStateListener != null){ //显示标题栏 wu
			baseViewStateListener.showTitleRightText();
		}
		switchState(State.CONTENT, null, Collections.<Integer> emptyList());
	}

	// public void showContent(List<Integer> skipIds) {
	// switchState(State.CONTENT, null, skipIds);
	// }

	private void switchState(State state) {
		switchState(state, null, Collections.<Integer> emptyList());
	}

	private void switchState(State state, String errorText) {
		switchState(state, errorText, Collections.<Integer> emptyList());
	}

	private void switchState(State state, List<Integer> skipIds) {
		switchState(state, null, skipIds);
	}

	private void switchState(State state, String errorText, List<Integer> skipIds) {
		mState = state;

		switch (state) {
		case CONTENT:
			tvProgressTips.setText("");
			mErrorView.setVisibility(View.GONE);
			mProgressView.setVisibility(View.GONE);
			setContentVisibility(true, skipIds);
			break;
		case PROGRESS:
			mErrorView.setVisibility(View.GONE);
			mProgressView.setVisibility(View.VISIBLE);
			setContentVisibility(false, skipIds);
			break;
		case ERROR:
			tvProgressTips.setText("");
			mErrorView.setError(errorText);
			mErrorView.setVisibility(View.VISIBLE);
			mProgressView.setVisibility(View.GONE);
			setContentVisibility(false, skipIds);
			break;
		}
	}


	private void switchState(State state, String errorText, int resId, List<Integer> skipIds) {
		mState = state;

		switch (state) {
			case CONTENT:
				tvProgressTips.setText("");
				mErrorView.setVisibility(View.GONE);
				mProgressView.setVisibility(View.GONE);
				setContentVisibility(true, skipIds);
				break;
			case PROGRESS:
				mErrorView.setVisibility(View.GONE);
				mProgressView.setVisibility(View.VISIBLE);
				setContentVisibility(false, skipIds);
				break;
			case ERROR:
				tvProgressTips.setText("");
				mErrorView.setError(errorText, resId);
				mErrorView.setVisibility(View.VISIBLE);
				mProgressView.setVisibility(View.GONE);
				setContentVisibility(false, skipIds);
				break;
		}
	}

	public State getState() {
		return mState;
	}

	public boolean isProgress() {
		return mState == State.PROGRESS;
	}

	public boolean isContent() {
		return mState == State.CONTENT;
	}

	public boolean isError() {
		return mState == State.ERROR;
	}

	private void setContentVisibility(boolean visible, List<Integer> skipIds) {
		for (View v : mContentViews) {
			if (!skipIds.contains(v.getId())) {
				v.setVisibility(visible ? View.VISIBLE : View.GONE);
			}
		}
	}
}
