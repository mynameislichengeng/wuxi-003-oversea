package com.wizarpos.pay.cashier.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wizarpos.pay.cashier.activity.QwalletNativeActivity;
import com.wizarpos.pay.cashier.view.WebViewController.WebViewStateListener;
import com.wizarpos.pay.view.smooth.SmoothProgressBar;
import com.motionpay.pay2.lite.R;

public class BrowseFragment extends Fragment implements WebViewStateListener {

	private static final String LOG_TAG = BrowseFragment.class.getSimpleName();

	private ObservableWebView mWebView;

	private WebViewController mWebViewController;

	private String mTitle;

	private String mOriginalUrl;

	private SmoothProgressBar smoothBar;

	private WebViewStateListener stateListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWebViewController = new WebViewController(getActivity());
		mWebViewController.setStateListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		final View view = inflater.inflate(R.layout.fragment_web, null);
		smoothBar = (SmoothProgressBar) view.findViewById(R.id.smoothBar);
		mWebView = (ObservableWebView) view.findViewById(R.id.webview);
		mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		mWebViewController.initControllerView(mWebView, view);
		Bundle args = getArguments();
		if (args != null && args.containsKey(QwalletNativeActivity.EXTRA_TITLE) && args.containsKey(QwalletNativeActivity.EXTRA_URL)) {
			mTitle = args.getString(QwalletNativeActivity.EXTRA_TITLE);
			mOriginalUrl = args.getString(QwalletNativeActivity.EXTRA_URL);
			final String url = mOriginalUrl;
			mWebViewController.loadUrl(url);
		}
		return view;
	}

	@Override
	public void onDestroy() {
		mWebViewController.destroy();
		super.onDestroy();
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public void load(String url) {
		mOriginalUrl = url;
		mWebViewController.loadUrl(mOriginalUrl);

	}

	@Override
	public void onPageStarted() {
		if (smoothBar != null && smoothBar.getVisibility() != View.VISIBLE) {
			smoothBar.setVisibility(View.VISIBLE);
		}
		if (stateListener != null) stateListener.onPageStarted();
	}

	@Override
	public void onPageFinished() {
		if (smoothBar != null && smoothBar.getVisibility() == View.VISIBLE) {
			smoothBar.setVisibility(View.GONE);
		}
		if (stateListener != null) stateListener.onPageFinished();
	}

	public void setListener(WebViewStateListener listener) {
		this.stateListener = listener;
	}

}
