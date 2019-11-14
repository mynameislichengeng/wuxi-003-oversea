package com.wizarpos.atool.util;

import android.app.Activity;
import android.app.ProgressDialog;

import com.alibaba.fastjson.JSONArray;
import com.wizarpos.atool.log.Logger;
import com.wizarpos.atool.util.DialogHelper.DialogCallback;
import com.wizarpos.devices.AccessException;
import com.wizarpos.devices.DeviceManager;
import com.wizarpos.devices.msr.MSCardData;
import com.wizarpos.devices.msr.MSRControl;
import com.wizarpos.devices.msr.MSRDeviceListener;
import com.wizarpos.devices.msr.MSROperationEvent;
import com.wizarpos.impl.devices.msr.MSTrackDataImpl;

public class POSFunction {

	public interface MSRCallback {
		public void msrCallback(int code, final Object data);
	}

	private static final Logger logger = Logger.getLogger();

	private MSRCallback msrCallback;
	// private AbstractActivity activityInstance = null;

	private static MSRControl control = DeviceManager.getInstance()
			.getMSRControl();

	// 磁条卡 打开
	public void msrOpen() {
		try {
			control.open((short) 0);
			control.waitForSwipe(-1, new MSRDeviceListener() {
				public void waitForSwipeCompleted(MSROperationEvent event) {
					MSCardData msCardData = event.getMSCardData();
					try {
						JSONArray jarr = new JSONArray();
						for (int i = 0; i < 3; i++) {
							if (msCardData.getTrackData(i).getTrackError() == MSTrackDataImpl.NO_DATA) {
								jarr.add(i, "");
							} else {
								jarr.add(i,new String(msCardData.getTrackData(i).getTrackData()));
							}
						}
						int code = 0;
						if (jarr.getString(1) == null) {
							code = 1;
						}
						if (code != 0) {
							String data = "刷卡失败";
							msrCallback.msrCallback(code, data);
						} else {
							msrCallback.msrCallback(code, jarr);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void msrOpenWithDialog(Activity activity) {
		logger.debug("msr popup dialog...");
		final ProgressDialog pd = DialogHelper.showProgressDialog(activity,
				"信息", "  请刷卡...", "  取  消  ", new DialogCallback() {
					public void callback() {
						msrCancel();
					}
				});
		try {
			control.open((short) 0);
			control.waitForSwipe(-1, new MSRDeviceListener() {
				public void waitForSwipeCompleted(MSROperationEvent event) {
					logger.debug("读取卡信息...");
					MSCardData msCardData = event.getMSCardData();
					try {
						JSONArray jarr = new JSONArray();
						for (int i = 0; i < 3; i++) {
							if (msCardData.getTrackData(i).getTrackError() == MSTrackDataImpl.NO_DATA) {
								jarr.add(i, "");
							} else {
								jarr.add(i,
										new String(msCardData.getTrackData(i)
												.getTrackData()));
							}
						}
						int code = 0;
						if (jarr.getString(1) == null) {
							code = 1;
						}
						if (code != 0) {
							String data = "刷卡失败";
							msrCallback.msrCallback(code, data);
						} else {
							msrCallback.msrCallback(code, jarr);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						pd.cancel();
						msrCancel();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 磁条卡 取消
	public void msrCancel() {
		try {
			control.close();
		} catch (AccessException e) {
		}
	}

	public void setMSRCallback(MSRCallback msrCallback) {
		this.msrCallback = msrCallback;
	}
}
