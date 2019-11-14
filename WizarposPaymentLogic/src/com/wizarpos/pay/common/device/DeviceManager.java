package com.wizarpos.pay.common.device;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.wizarpos.base.net.Response;
import com.wizarpos.device.printer.HSMController;
import com.wizarpos.devices.jniinterface.HSMInterface;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;

import java.security.MessageDigest;

/**
 * 设备管理类
 *
 * @author wu
 */
public class DeviceManager {

    private static DeviceManager manager;

    private CardListener cardListener;
    private Displayer displayer;
    private SmartCardListener smartCardListener;

    private DeviceManager() {
    }

    public static DeviceManager getInstance() {
        if (manager == null) {
            manager = new DeviceManager();
        }
        return manager;
    }

    public void startSmartCardListener(ResultListener listener) {
        if (smartCardListener == null) {
            smartCardListener = new DefaultSamrtCardListenerImpl();
        }
        smartCardListener.setSmartCardListener(listener);
    }

    public void closeSmartCard() {
        if (smartCardListener != null) {
            smartCardListener.close();
        }
    }

    /**
     * 获取二磁道信息
     *
     * @return
     */
    public void getTrack2(Handler handler, final ResultListener listener) {
        try {
            if (cardListener == null) {
                cardListener = new DefaultCardListenerImpl();
            }
            LogEx.d("device", "获取2磁道");
            cardListener.getTrack2(handler, new ResultListener() {

                @Override
                public void onSuccess(Response response) {
                    cardListener.close();
                    listener.onSuccess(response);
                }

                @Override
                public void onFaild(Response response) {
                    cardListener.close();
                    listener.onFaild(response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取二磁道信息
     *
     * @return
     */
    public void startSwipeListener(Handler handler, final ResultListener listener) {
        try {
            if (cardListener == null) {
                cardListener = new DefaultCardListenerImpl();
            }
            LogEx.d("device", "获取2磁道");
            cardListener.setSwipeCardListener(handler, new ResultListener() {

                @Override
                public void onSuccess(Response response) {
                    cardListener.close();
                    listener.onSuccess(response);
                }

                @Override
                public void onFaild(Response response) {
                    cardListener.close();
                    listener.onFaild(response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeCardListener() {
        try {
            if (cardListener != null) {
                LogEx.d("device", "关闭刷卡");
                cardListener.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 客显调用
     *
     * @param bitmap
     */
    boolean display(Bitmap bitmap) {
        if (displayer != null) {
            displayer.display(bitmap);
            return true;
        }
        return false;
    }

    //
    // /**
    // * 打印
    // *
    // * @param str
    // */
    // public void print(String str) {
    // if (printer != null) {
    // printer.print(str);
    // }
    // }
    //
    // public void print(Bitmap bitmap) {
    // if (printer != null) {
    // printer.print(bitmap);
    // }
    // }

    /**
     * 取证书</br>
     * 主线程调用
     *
     * @return
     */
    synchronized public void getPubCertificate(ResultListener listener) {
        LogEx.d("取证书", "start");
        // 测试模式直接返回虚假数据
//		if (Constants.FALSE.equals(AppConfigHelper
//				.getConfig(AppConfigDef.test_load_safe_mode))) {
//			listener.onSuccess(new Response(0, "success", ""));
//			return;
//		}
        if (!DeviceManager.getInstance().isWizarDevice()) { //如果不是慧银机具,直接绕过 wu
            LogEx.d("取证书", "非慧银机具,返回空");
            listener.onSuccess(new Response(0, "success", ""));
            return;
        }
        // 尝试从缓存中获取
        String PUB_CERT_AILAS = AppStateManager.getState(AppStateDef.PUB_CERT_AILAS);
        if (!TextUtils.isEmpty(PUB_CERT_AILAS)) {
            LogEx.d("取证书", "从缓存取出");
            listener.onSuccess(new Response(0, "success", PUB_CERT_AILAS));
            return;
        }

        // 耗时操作
        AsyncTask<ResultListener, Void, String> task = new AsyncTask<ResultListener, Void, String>() {
            private ResultListener listener;

            @Override
            protected String doInBackground(ResultListener... params) {
                this.listener = params[0];
                try {
                    return getPubCertificateSynchronized();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String pubCertificate) {
                super.onPostExecute(pubCertificate);
                if (!TextUtils.isEmpty(pubCertificate)) {
                    listener.onSuccess(new Response(0, "success", pubCertificate));
                } else {
                    LogEx.d("取证书", "取出结果为空");
                    listener.onFaild(new Response(1, "未获取到公钥证书，请联系客服"));
                }
            }
        };
        task.execute(listener);
    }

    synchronized public String getPubCertificateSynchronized() {
        LogEx.d("取证书", "start");
        if (!DeviceManager.getInstance().isWizarDevice()) { //如果不是慧银机具,直接绕过 wu
            LogEx.d("取证书", "非慧银机具,空");
            return "";
        }
        // 尝试从缓存中获取
        String PUB_CERT_AILAS = AppStateManager.getState(AppStateDef.PUB_CERT_AILAS);
        if (!TextUtils.isEmpty(PUB_CERT_AILAS)) {
            LogEx.d("取证书", "从缓存取出");
            return PUB_CERT_AILAS;
        }
        String pubCertificateStr = "";
        if (isMenshengDevice()) {
            final byte[] pubCertificate = HSMController.getInstance().getPubCertificate("terminal");
            if (pubCertificate != null) {
                pubCertificateStr = new String(pubCertificate); //民生
            }
        } else {
            final byte[] pubCertificate = HSMController.getInstance().getPubCertificate(HSMController.PUB_CERT_AILAS);
            if (pubCertificate != null) {
                pubCertificateStr = new String(pubCertificate);// 慧银
            }
        }
        AppStateManager.setState(AppStateDef.PUB_CERT_AILAS, pubCertificateStr);
        LogEx.d("取证书", "同步 从机器取出");
        return pubCertificateStr;
    }

    /**
     * RSA加密
     *
     * @param msg
     * @return
     */
    public synchronized byte[] doRSAEncrypt(String msg) {
//		if (Constants.FALSE.equals(AppConfigHelper
//				.getConfig(AppConfigDef.test_load_safe_mode))) {
//			return "test".getBytes();
//		}
        int BUF_SIZE = 1024;
        byte[] bufResult = new byte[BUF_SIZE];
        try {
            byte[] bufData = msg.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA256");
            byte[] bufMD = md.digest(bufData);

            int openResult = HSMInterface.open();
            if (openResult >= 0) {
                int doRSAResult = HSMInterface.doRSAEncrypt("terminal", bufMD,
                        bufResult, BUF_SIZE);
                if (doRSAResult >= 0) {
                    byte[] tempData = new byte[doRSAResult];
                    System.arraycopy(bufResult, 0, tempData, 0, doRSAResult);
                    HSMInterface.close();
                    return tempData;
                }
            }
            HSMInterface.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufResult;
    }

    private String getSystemPropertie(String name) {
        Object bootloaderVersion = null;
        try {
            Class<?> systemProperties = Class
                    .forName("android.os.SystemProperties");
            Log.i("systemProperties", systemProperties.toString());
            bootloaderVersion = systemProperties.getMethod("get",
                    new Class[]{String.class, String.class}).invoke(
                    systemProperties, new Object[]{name, "unknown"});
            Log.i("bootloaderVersion", bootloaderVersion.getClass().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bootloaderVersion.toString();
    }

//	public static final String DEVICE_TYPE_WIZARPOS = "wizarpos";
//	public static final String DEVICE_TYPE_WIZARPAD = "wizarpad";
//	public static final String DEVICE_TYPE_WIZARHAND_Q1 = "wizarHAAN_Q1";
//	public static final String DEVICE_TYPE_WIZARHAND_M0 = "wizarHAAN_Q1";
//	public static final String DEVICE_TYPE_OTHER = "other";

    public static final int DEVICE_TYPE_WIZARPOS = 1;
    public static final int DEVICE_TYPE_WIZARPAD = 2;
    public static final int DEVICE_TYPE_WIZARHAND_Q1 = 3;
    public static final int DEVICE_TYPE_WIZARHAND_M0 = 4;
    public static final int DEVICE_TYPE_SHENGTENG_M10 = 5;
    public static final int DEVICE_TYPE_PULAN = 6;
    public static final int DEVICE_TYPE_MINSHENG = 7;
    public static final int DEVICE_TYPE_OTHER = 0;
    public static final int DEVICE_TYPE_WIZARHAND_Q2 = 8;
    public static final int DEVICE_TYPE_WIZARHAND_K2 = 9;
    public static final int DEVICE_TYPE_N3_OR_N5 = 10;

    /**
     * 判断设备类型
     */
    public int getDeviceType() {
        /*
         * 1.wizarHAND_Q1
		 * 2.WIZARPOS 1
		 * 3. wizarHAND_M0
		 * 
		 * */
        String systemPropertie = getSystemPropertie("ro.product.model");
        if (TextUtils.isEmpty(systemPropertie)) {
            return DEVICE_TYPE_OTHER;
        }
        LogEx.d("deviceType", systemPropertie);
        String upperCaseDeviceType = systemPropertie.toUpperCase();
        if (upperCaseDeviceType.contains("WIZARPOS")) {
            return DEVICE_TYPE_WIZARPOS;
        } else if (upperCaseDeviceType.contains("WIZARHAND")) {
            if (upperCaseDeviceType.contains("M0")) {
                return DEVICE_TYPE_WIZARHAND_M0;
            }
            return DEVICE_TYPE_WIZARHAND_Q1;
        } else if (upperCaseDeviceType.contains("Q2")) {
            if (Build.SERIAL.contains("Q2")) {
                return DEVICE_TYPE_WIZARHAND_Q2;
            }
            return DEVICE_TYPE_WIZARHAND_K2;
        } else if (upperCaseDeviceType.contains("WIZARPAD")) {
            return DEVICE_TYPE_WIZARPAD;
        } else if (upperCaseDeviceType.contains("MINSHENG")) {
            return DEVICE_TYPE_MINSHENG;
        } else if (upperCaseDeviceType.contains("ALLWINNER-TABLET")) {
            return DEVICE_TYPE_SHENGTENG_M10;
        } else if (upperCaseDeviceType.contains("PL-I107")) {
            return DEVICE_TYPE_PULAN;
        }else if (upperCaseDeviceType.contains("N3") || upperCaseDeviceType.contains("N5")) {
            return DEVICE_TYPE_N3_OR_N5;
        } else {
            return DEVICE_TYPE_OTHER;
        }
    }

    /**
     * 获取摄像头调用顺序
     * 0                       1
     * wizarpos 默认摄像头（前置）             客显摄像头（后置）
     * wizarpad 默认摄像头（后置）             客显摄像头（后置）
     * H0              变焦摄像头（后置）             定焦摄像头（后置）
     * M0              变焦摄像头（后置）
     *
     * @return
     */

    public int getCameraIndex() {
        switch (getDeviceType()) {
            case DEVICE_TYPE_WIZARPOS:
                return 0;
            case DEVICE_TYPE_WIZARPAD:
                return 0;
            case DEVICE_TYPE_WIZARHAND_M0:
                return 0;
            case DEVICE_TYPE_WIZARHAND_Q1:
                return 1;
            default:
                return 0;
        }
    }

    /**
     * 是否是慧银机具
     *
     * @return
     */
    public boolean isWizarDevice() {
        int deviceType = getDeviceType();
        return (DEVICE_TYPE_WIZARHAND_M0 == deviceType || DEVICE_TYPE_WIZARHAND_Q1 == deviceType
                || DEVICE_TYPE_WIZARPAD == deviceType || DEVICE_TYPE_WIZARPOS == deviceType
                || DEVICE_TYPE_WIZARHAND_Q2 == deviceType || DEVICE_TYPE_WIZARHAND_K2 == deviceType);
    }

    public String getDeviceLogo() {
        try {
            return getSystemPropertie("ro.wp.logo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isMenshengDevice() {
        String systemPropertie = getDeviceLogo();
        Log.d("deviceLogo", systemPropertie);
        return TextUtils.isEmpty(systemPropertie) == false && systemPropertie.contains("minsheng");
    }

    /**
     * 是否支持刷卡
     *
     * @return
     */
    public boolean isSupportSwipe() {
        int deviceType = getDeviceType();
        return (DEVICE_TYPE_WIZARHAND_Q1 == deviceType || DEVICE_TYPE_WIZARPAD == deviceType || DEVICE_TYPE_WIZARPOS == deviceType);
    }

    /**
     * 是否支持银行卡交易
     *
     * @return
     */
    public boolean isSupportBankCard() {
        int deviceType = getDeviceType();
        return (DEVICE_TYPE_WIZARHAND_Q1 == deviceType || DEVICE_TYPE_WIZARPAD == deviceType || DEVICE_TYPE_WIZARPOS == deviceType);
    }

    /**
     * 是否支持打印
     *
     * @return
     */
    public boolean isSupprotPrint() {
        return true;
    }

    public CardListener getCardListener() {
        return cardListener;
    }

    public void setCardListener(CardListener cardListener) {
        LogEx.d("device", "初始化刷卡");
        this.cardListener = cardListener;
    }

    public Displayer getDisplayer() {
        return displayer;
    }

    public void setDisplayer(Displayer displayer) {
        this.displayer = displayer;
    }


    //获取IMEI地址
    public static String getImei(Context context) {
        String sn = "";
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        sn = mTelephonyMgr.getDeviceId();
        return sn;
    }
}
