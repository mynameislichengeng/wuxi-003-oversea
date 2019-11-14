package com.wizarpos.pay.cashier.view;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewController implements OnClickListener {

	public interface WebViewStateListener {
		void onPageStarted();

		void onPageFinished();
	}

	private WeakReference<Activity> mActivityRef;

	private WebView mWebView;

	private WebViewStateListener stateListener;
	// private ImageButton mBackButton;
	// private ImageButton mForwardButton;
	// private ImageButton mReadabilityButton;
	// private ImageButton mRefreshButton;
	// private ImageButton mWebSiteButton;

	// private View mToolBar;

	private String mCurrentUrl;

	public WebViewController(Activity activity) {
		mActivityRef = new WeakReference<Activity>(activity);
	}

	public void setStateListener(WebViewStateListener stateListener) {
		this.stateListener = stateListener;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("SetJavaScriptEnabled")
	public void initControllerView(WebView webView, View view) {
		if (webView == null || view == null) { return; }
		mWebView = webView;
		// mWebView.setWebChromeClient(new MyWebChromeClient());
		// mWebView.setWebViewClient(new MyWebViewClient());
		// mWebView.clearCache(true);
		WebSettings settings = mWebView.getSettings();
		settings.setSupportZoom(true);
		// settings.setBuiltInZoomControls(true);
		// if (hasHoneycomb()) {
		// settings.setDisplayZoomControls(false);
		// }
		settings.setJavaScriptEnabled(true);
		// settings.setUseWideViewPort(false);
		// settings.setLoadWithOverviewMode(true);
		// mBackButton = (ImageButton) view.findViewById(R.id.browse_back);
		// mForwardButton = (ImageButton) view.findViewById(R.id.browse_forward);
		// mReadabilityButton = (ImageButton) view.findViewById(R.id.browse_readability);
		// mRefreshButton = (ImageButton) view.findViewById(R.id.browse_refresh);
		// mWebSiteButton = (ImageButton) view.findViewById(R.id.browse_website);

		// mToolBar = view.findViewById(R.id.browse_bar);

		// mBackButton.setOnClickListener(this);
		// mForwardButton.setOnClickListener(this);
		// mReadabilityButton.setOnClickListener(this);
		// mRefreshButton.setOnClickListener(this);
		// mWebSiteButton.setOnClickListener(this);
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			setCurrentUrl(url);
			if (stateListener != null) stateListener.onPageStarted();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			setCurrentUrl(url);
			if (stateListener != null) stateListener.onPageFinished();
		}
	}

	private void setCurrentUrl(String url) {
		mCurrentUrl = url;
	}

	private String getCurrentUrl() {
		return mCurrentUrl; // TextUtils.isEmpty(mCurrentUrl) ? mOriginalUrl :
							// mCurrentUrl;
	}

	private class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			int progress = (Window.PROGRESS_END - Window.PROGRESS_START) / 100 * newProgress;
			Activity activity = mActivityRef.get();
			// if (activity != null && activity instanceof BaseFragmentActivity)
			// {
			// ((BaseFragmentActivity) activity).setSupportProgress(progress);
			// }
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			mActivityRef.get().setTitle(title);
		}

	}

	@Override
	public void onClick(View v) {
		// switch (v.getId()) {
		// case R.id.browse_back:
		// back();
		// break;
		// case R.id.browse_forward:
		// forward();
		// break;
		// case R.id.browse_readability:
		// readability();
		// break;
		// case R.id.browse_refresh:
		// refresh();
		// break;
		// case R.id.browse_website:
		// webSite();
		// break;
		// default:
		// break;
		// }

	}

	public void back() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		}
	}

	public void forward() {
		if (mWebView.canGoForward()) {
			mWebView.goForward();
		}
	}

	public void readability() {
		if (TextUtils.isEmpty(mCurrentUrl)) { return; }
		mWebView.loadUrl("http://www.readability.com/m?url=" + getCurrentUrl());
	}

	public void refresh() {
		mWebView.reload();
	}

	public void webSite() {
		// 打开原链接，还是转码的链接呢？
		if (TextUtils.isEmpty(mCurrentUrl)) { return; }
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(getCurrentUrl()));
		mActivityRef.get().startActivity(intent);
	}

	public WebView getWebView() {
		return mWebView;
	}

	public void destroy() {

		if (mWebView != null) {
			((ViewGroup) mWebView.getParent()).removeAllViews();
			mWebView.clearHistory();
			mWebView.clearCache(true);
			mWebView.loadUrl("about:blank");
			mWebView.pauseTimers();
			mWebView.destroy();
			mWebView = null;
		}
		mActivityRef.clear();

	}

	public void loadUrl(String url) {
		if (TextUtils.isEmpty(url) || url.equals(mCurrentUrl)) { return; }
		mWebView.clearHistory();
		mWebView.loadUrl(url);
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}
}
