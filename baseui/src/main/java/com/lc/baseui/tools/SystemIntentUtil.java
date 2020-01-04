package com.lc.baseui.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by licheng on 2017/5/21.
 */

public class SystemIntentUtil {

    /**
     * 拨打电话，这个只是跳转到拨号界面而已
     *
     * @param context 上下文呢
     * @param phone   电话号码
     **/
    public static void callPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 发送短信，这个只是跳转到拨号界面而已
     *
     * @param context 上下文呢
     * @param phone   电话号码
     * @param content 如果有内容可以直接写在content 里面
     **/
    public static void sendMsg(Context context, String phone, String content) {
        Uri smsToUri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        //短信内容
        intent.putExtra("sms_body", content);
        context.startActivity(intent);
    }

}
