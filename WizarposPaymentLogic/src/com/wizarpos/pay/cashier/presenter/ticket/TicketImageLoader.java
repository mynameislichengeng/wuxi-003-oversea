package com.wizarpos.pay.cashier.presenter.ticket;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;

public class TicketImageLoader {
	private List<TicketInfo> ticketInfos, filtedTicketInfos;
	private ResultListener listener;

	public TicketImageLoader(List<TicketInfo> ticketInfos, ResultListener listener) {
		this.ticketInfos = ticketInfos;
		this.listener = listener;
	}

	public final String ticketUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";

	private int step = 0;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			listener.onSuccess(new Response(0, "success", ticketInfos));
		}

	};

	public boolean startLoad() {
		if (ticketInfos == null || listener == null) {
			handler.sendEmptyMessage(0);
			return false;
		}
		filtedTicketInfos = new ArrayList<TicketInfo>();
		for (TicketInfo ticketInfo : ticketInfos) {
			if (TextUtils.isEmpty(ticketInfo.getTicketQrcode())) {
				continue;
			}
			filtedTicketInfos.add(ticketInfo);
		}
		LogEx.d("现金消费返回券", "需要请求图片的张数" + ticketInfos.size() + "");
		if (filtedTicketInfos.isEmpty()) {
			handler.sendEmptyMessage(0);
			return false;
		}
		ImageLoadTask task = new ImageLoadTask();
		task.start();
		return true;
	}

	private class ImageLoadTask extends Thread {

		@Override
		public void run() {
			super.run();
			for (TicketInfo ticketInfo : filtedTicketInfos) {
				loadImg(ticketInfo);
			}
		}

		protected void loadImg(final TicketInfo ticketInfo) {
			ImageLoader.getInstance().loadImage(ticketUrl + ticketInfo.getTicketQrcode(), new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String arg0, View arg1) {

				}

				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					LogEx.d("现金消费返回券", "图片加载失败");
					isFinsih();
					listener.onFaild(new Response(-1, "请求微信券失败，送券失败"));
				}

				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
					LogEx.d("现金消费返回券", "图片加载成功");
					String _path = Tools.getSDPath() + File.separator + "common" + File.separator;
					File file = new File(_path);
					if (!file.exists()) {
						file.mkdirs();
					}
					String filePath = _path + System.currentTimeMillis() + ticketInfo.getId() + ".jpg";
					Tools.writePng(bitmap, new File(filePath));
					try {
						if (!bitmap.isRecycled()) {
							bitmap.recycle();
						}
					} catch (Exception e) {
					}
					ticketInfo.setTicketQRCodeLocalPath(filePath);
					isFinsih();
				}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					LogEx.d("现金消费返回券", "图片加载取消");
					isFinsih();
				}
			});

		}

		private void isFinsih() {
			step++;
			LogEx.d("现金消费返回券", "step:" + step + "  --券的张数  " + filtedTicketInfos.size());
			if (step == filtedTicketInfos.size()) {
				handler.sendEmptyMessage(0);
			}
		}
	}

}
