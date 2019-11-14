package com.wizarpos.pay.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wizarpos.pay.common.base.BaseLogicAdapter;
import com.wizarpos.pay.common.base.ViewHolder;
import com.wizarpos.pay2.lite.R;

import java.util.List;

public class ActionSheetDialog {
	private Context context;
	private Dialog dialog;
	private TextView txt_title;
	private TextView txt_cancel;
//	private LinearLayout lLayout_content;
	private ScrollView sLayout_content;
	private ListView lvContent;
	private boolean showTitle = false;
	private List<ArrayItem> arrayItems;
	private Display display;
	private ActionSheetListener listener;
	private String currentValue = null;

	public ActionSheetDialog(Context context, List<ArrayItem> arrayItems, ActionSheetListener listener) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
		setArrayItems(arrayItems);
		setListener(listener);
	}

	public ActionSheetDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_actionsheet, null);

		// 设置Dialog最小宽度为屏幕宽度
		view.setMinimumWidth(display.getWidth());

		// 获取自定义Dialog布局中的控件
		sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
		lvContent = (ListView) view.findViewById(R.id.lvContent);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
		txt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
		dialog.setContentView(view);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if (null != listener) {
					listener.onCancel();
				}
			}
		});
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.x = 0;
		lp.y = 0;
		dialogWindow.setAttributes(lp);

		return this;
	}

	public ActionSheetDialog setTitle(String title) {
		showTitle = true;
		txt_title.setVisibility(View.VISIBLE);
		txt_title.setText(title);
		return this;
	}

	public ActionSheetDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
		dialog.setCanceledOnTouchOutside(cancel);
		return this;
	}

	/** 设置条目布局 */
	private void setSheetItems() {
		if (arrayItems == null || arrayItems.size() <= 0) {
			return;
		}

		int size = arrayItems.size();

		// TODO 高度控制，最佳解决办法
		// 添加条目过多的时候控制高度
//		if (size >= 5) {
//			LayoutParams params = (LayoutParams) sLayout_content
//					.getLayoutParams();
//			params.height = display.getHeight() / 2;
//			sLayout_content.setLayoutParams(params);
//		}

		lvContent.setAdapter(new BaseLogicAdapter<ArrayItem>(context, arrayItems, R.layout.item_action_sheet) {
			@Override
			public void convert(ViewHolder helper, final ArrayItem sheetItem, final int position) {
				String strItem = sheetItem.getShowValue();
				TextView textView = helper.getView(R.id.tvActionSheetItem);
				textView.setText(strItem);

				if (!TextUtils.isEmpty(currentValue) && currentValue.equals(sheetItem.getRealValue() + "")) {
					textView.setTextColor(0xff0074e1);
				} else {
					textView.setTextColor(0xff666666);
				}
				// 背景图片
				if (arrayItems.size() == 1) {
					if (showTitle) {
						textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
					} else {
						textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
					}
				} else {
					if (showTitle) {
						if (position >= 0 && position < arrayItems.size() - 1) {
							textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
						} else {
							textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
						}
					} else {
						if (position == 0) {
							textView.setBackgroundResource(R.drawable.actionsheet_top_selector);
						} else if (position < arrayItems.size() - 1) {
							textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
						} else {
							textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
						}
					}
				}

				// 高度
				float scale = context.getResources().getDisplayMetrics().density;
				int height = (int) (45 * scale + 0.5f);
				textView.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, height));

				// 点击事件
				textView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (null != listener) {
							listener.onClick(sheetItem);
						}
						dialog.dismiss();
					}
				});

			}
		});

	}

	public void show() {
		setSheetItems();
		dialog.show();
	}

	public void setArrayItems(List<ArrayItem> arrayItems) {
		this.arrayItems = arrayItems;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	public interface ActionSheetListener {
		void onCancel();

		void onClick(ArrayItem item);
	}

	public void setListener(ActionSheetListener listener) {
		this.listener = listener;
	}

	public boolean isShowing() {
		return dialog != null ? dialog.isShowing() : false;
	}

}
