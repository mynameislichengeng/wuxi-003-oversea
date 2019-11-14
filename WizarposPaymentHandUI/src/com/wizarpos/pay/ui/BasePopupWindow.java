package com.wizarpos.pay.ui;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

/**
 * 
 * @Author:Huangweicai
 * @Date:2015-8-5 下午10:06:50
 * @Reason:这里用一句话说明
 */
public class BasePopupWindow extends PopupWindow implements OnTouchListener {
	/** 接口实例，用于调用接口 */
	IBasePopupWindow mInstance = null;

	/** 默认不自动隐藏 */
	private int miAutoDismissInterval = 0;

	private Handler mHandler = new Handler();

	private View mView;

	/**
     * 
     */
	public BasePopupWindow() {
		super();
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public BasePopupWindow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public BasePopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setAutoDismissInterval(int iAutoDismissInterval) {
		miAutoDismissInterval = iAutoDismissInterval;
	}

	/**
	 * @param context
	 */
	public BasePopupWindow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param width
	 * @param height
	 */
	public BasePopupWindow(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param contentView
	 *            :the popup's content
	 * @param width
	 *            ：the popup's width
	 * @param height
	 *            :the popup's height
	 * @param focusable
	 *            :true if the popup can be focused, false otherwise
	 */
	public BasePopupWindow(View contentView, int width, int height, boolean focusable) {
		super(contentView, width, height, focusable);
		// 响应点击系统退出键，如果是展开的，点击以后收起来，然后再次点击弹出退出程序确认框
		this.mView = contentView;
		mView.setOnTouchListener(this);
		contentView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && BasePopupWindow.this.isShowing()) {
					// 收起弹出窗口
					BasePopupWindow.this.dismiss();
					// 根据具体需要做一些其他的取消动作，由每个页面自己实现接口
					if (null != mInstance) {
						mInstance.onCancelPopupWindow(BasePopupWindow.this);
					}
					// return true代表此事件已被消费，不再继续传递
					return true;
				}
				// return false代表其他地方可以继续接受并处理此事件
				return false;
			}
		});
	}

	/**
	 * @param contentView
	 * @param width
	 * @param height
	 */
	public BasePopupWindow(View contentView, int width, int height) {
		super(contentView, width, height);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param contentView
	 */
	public BasePopupWindow(View contentView) {
		super(contentView);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		if (miAutoDismissInterval > 0) {
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					BasePopupWindow.this.dismiss();
				}
			}, miAutoDismissInterval);
		}
		super.showAtLocation(parent, gravity, x, y);
	}

	/**
	 * 
	 * @Author:Huangweicai
	 * @Date:2015-8-5 下午10:21:50
	 * @Reason: 用于各个页面自行实现，做一些popupwindow消掉前的操作
	 */
	public interface IBasePopupWindow {

		/**
		 * 做一些popupwindow消掉前的操作
		 * 
		 * @date 2012-11-14
		 * @param popupWindow
		 *            要消掉的popupwindow
		 */
		public void onCancelPopupWindow(PopupWindow popupWindow);
	}

	/**
	 * 
	 * @Author:Huangweicai
	 * @Date:2015-8-5 下午10:22:02
	 * @Reason:设置BasePopupWindow实例，用于回调函数
	 * @param instance
	 */
	public void setIBasePopupWindowInstance(IBasePopupWindow instance) {
		mInstance = instance;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int top = mView.getTop();
		int bottom = mView.getBottom();
		int left = mView.getLeft();
		int right = mView.getRight();
		int y = (int) event.getY();
		int x = (int) event.getX();
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (x < left || x > right) {
				if (this.isShowing()) {
					this.dismiss();
					return true;
				}
			}
			if (y > bottom || y < top) {
				if (this.isShowing()) {
					this.dismiss();
					return true;
				}
			}
		}
		return true;
	}

}
