package com.wizarpos.pay.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wizarpos.pay.ui.newui.NewMainActivity;
import com.wizarpos.pay2.lite.R;

import org.xclcharts.common.DensityUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: yaosong
 * @date 2016-3-31 上午9:59:56
 * @Description:自定义Toast工具
 *
 */
public class CommonToastUtil {

    private final static int toastTime = Toast.LENGTH_SHORT;  //toast时间长度
    public static final int Y_TOP = 1;
    public static final int Y_ABOVE_CENTER = 2;
    public static final int Y_BELOW_CENTER = 3;
    public static final int Y_BOTTOM = 4;
    public static final int Y_CENTER = 5;
    public static final int LEVEL_INFO = 0;
    public static final int LEVEL_WARN = 1;
    public static final int LEVEL_SUCCESS = 2;
    public static final int LEVEL_ERROR = 3;
    //toast位置相对于中央偏移量,默认中央显示
    private static int XOFFSET = 0, YOFFSET = -60;
    private static Toast toast = null;
    private static Timer timer = null;         //显示计时器
    private static ToastTimerTask timeTask = null;  //定时器任务
    private static int toastId = 0;            //当前toast层数，为0时，会在toast结束后将toast置null
//    public static String foreverToastShowingString = null;  //上次永久显示的字符串

    /**
     * 常用方法
     * @param context
     * @param level
     * @param msg
     */
    public static void showMsgAbove(Context context,int level,String msg){
        setYOFFSET(context, Y_ABOVE_CENTER);
        ToastShow(context, getImageId(level), msg);
    }
    /**
     * 常用方法
     * @param context
     * @param level
     * @param msg
     */
    public static void showMsgBelow(Context context,int level,String msg){
        setYOFFSET(context, Y_BELOW_CENTER);
        ToastShow(context, getImageId(level), msg);
    }

    /**
     * 自定义位置、图标、内容的Toast
     * @param context
     * @param ImageResId
     * @param tvString
     * @param yOffset
     */
    public static void ToastShow(Context context,int ImageResId,String tvString,int yOffset){
        setYOFFSET(context, yOffset);
        ToastShow(context, ImageResId, tvString);
    }

    /**
     * 自定义位置、图标、内容的Toast
     * @param context
     * @param tvString
     * @param level
     * @param yOffset
     */
    public static void ToastShow(Context context, String tvString, int level,int yOffset){
        setYOFFSET(context, yOffset);
        ToastShow(context, getImageId(level), tvString);
    }

    /**
     * 自定义位置、图标、内容的永久Toast
     * @param context
     * @param tvString
     * @param level
     * @param yOffset
     */
    public static void showForeverToast(Context context, String tvString, int level,int yOffset) {
        setYOFFSET(context, yOffset);
        showForeverToast(context, tvString, level);
    }

    /**
     * 显示Toast  
     * @param context  
     * @param ImageResId
     * @param tvString  
     */
    public static void ToastShow(Context context,int ImageResId,String tvString){
        stopAnyway();
        View layout = LayoutInflater.from(context).inflate(R.layout.toast_common,null);
        TextView tvToastContent = (TextView) layout.findViewById(R.id.tvToastContent);
        ImageView ivToastIcon = (ImageView) layout.findViewById(R.id.ivToastIcon);
        tvToastContent.setText(tvString);
        ivToastIcon.setImageResource(ImageResId);
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, XOFFSET, YOFFSET);
        if (context instanceof NewMainActivity){
            toast.setDuration(Toast.LENGTH_LONG);
        } else {
            toast.setDuration(toastTime);
        }
        toast.setView(layout);
        toast.show();
        toastId ++;
        destroyToastAfterShow(context);
    }

    //FIXME 如有更好的方法请修改
    private static void destroyToastAfterShow(Context context) {
        long destroyTime = 0L;
        if (toastTime == Toast.LENGTH_SHORT){
            destroyTime = 2000L;
        } else if (toastTime == Toast.LENGTH_LONG || context instanceof NewMainActivity){
            destroyTime = 3500L;
        } else {
            destroyTime = toastTime;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (toastId > 0){
                    toastId --;
                    if (toastId == 0){
                        stopAnyway();
                    }
                }
            }
        }, destroyTime);
    }

    /**
     * @param context
     * @param tvString
     * @param level
     */
    public static void showForeverToast(Context context, String tvString, int level) {
        stopAnyway();
        View layout = LayoutInflater.from(context).inflate(R.layout.toast_common,null);
        TextView tvToastContent = (TextView) layout.findViewById(R.id.tvToastContent);
        ImageView ivToastIcon = (ImageView) layout.findViewById(R.id.ivToastIcon);
        tvToastContent.setText(tvString);
//        foreverToastShowingString = tvString;
        ivToastIcon.setImageResource(getImageId(level));
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, XOFFSET, YOFFSET);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        if (null == timer){
            timer = new Timer();
            timeTask = new ToastTimerTask();
            timer.schedule(timeTask, 0, 3000);
        }
    }

    public static void stopAnyway(){
        if (toast != null){
            if (timer != null){
                timer.cancel();
                timeTask.cancel();
                timeTask = null;
                timer = null;
            }
            toast.cancel();
            toast = null;
        }
    }

    public static void setYOFFSET(Context context,int Ytype) {
        switch (Ytype){
            case Y_TOP:
                YOFFSET = 0 - DensityUtil.getScreenHeight(context)/3 ;
                break;
            case Y_ABOVE_CENTER:
                YOFFSET = 0 - DensityUtil.getScreenHeight(context)/6 ;
                break;
            case Y_BELOW_CENTER:
                YOFFSET = 0 + DensityUtil.getScreenHeight(context)/5 ;
                break;
            case Y_BOTTOM:
                YOFFSET = 0 + DensityUtil.getScreenHeight(context)/3 ;
                break;
            case Y_CENTER:
                YOFFSET = 0 ;
                break;
            default: YOFFSET = Ytype;
                break;
        }
    }

    private static int getImageId(int level){
        int imageId = 0;
        switch (level){
            case LEVEL_INFO:
                imageId = R.drawable.ic_info;
                break;
            case LEVEL_WARN:
                imageId = R.drawable.ic_warning;
                break;
            case LEVEL_SUCCESS:
                imageId = R.drawable.ic_succese;
                break;
            case LEVEL_ERROR:
                imageId = R.drawable.ic_cancel;
                break;
        }
        return imageId;
    }

    private static class ToastTimerTask extends TimerTask{
        @Override
        public void run() {
            if (toast != null){
                toast.show();
            }
        }
    }
}
