package com.wizarpos.pay.common.channel;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ChannelUtil {

    public static final String CHANNEL_KEY_IP = "ip";
    public static final String CHANNEL_KEY_BAT_FLAG = "bat";
    public static final String CHANNEL_KEY_PORT = "port";
    public static final String CHANNEL_KEY_APP_CHANNEL = "channel";

    /**
     * 返回市场。  如果获取失败返回""
     *
     * @param context
     * @return
     */
    public static String getAPKChannelConfig(Context context, String key) {
        return getAPKChannelConfig(context, key, "");
    }

    /**
     * 返回市场。  如果获取失败返回defaultChannel
     *
     * @param context
     * @param defaultConfig
     * @return
     */
    public static String getAPKChannelConfig(Context context, String key, String defaultConfig) {

        //从apk中获取
        String config = getChannelFromApk(context, key);
        if (!TextUtils.isEmpty(config)) {
            return config;
        }
        //全部获取失败
        return defaultConfig;
    }

    /**
     * 从apk中获取版本信息
     *
     * @param context
     * @param channelKey
     * @return
     */
    private static String getChannelFromApk(Context context, String channelKey) {
        //从apk包中获取
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        //默认放在meta-inf/里， 所以需要再拼接一下
        String key = "META-INF/" + channelKey;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith(key)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] split = ret.split("_");
        String channel = "";
        if (split != null && split.length >= 2) {
            channel = ret.substring(split[0].length() + 1);
        }
        return channel;
    }

}
