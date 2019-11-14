package com.wizarpos.pay.setting.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wu on 16/9/9.
 */
public class ResourcesConfig {

    private static final String RESOURCE = "RESOURCE";
    private static final String SKIN_CONFIG = "SKIN_CONFIG";
    private static final String LANGUAGE_CONFIG = "LANGUAGE_CONFIG";

    public static void setSkin(Context context, int skin) {
        setConfig(context, SKIN_CONFIG, skin);
    }

    public static void setLanguage(Context context, int language) {
        setConfig(context, LANGUAGE_CONFIG, language);
    }

    public static int getSkin(Context context) {
        return getIntConfig(context, SKIN_CONFIG);
    }

    public static int getLanguage(Context context) {
        return getIntConfig(context, LANGUAGE_CONFIG);
    }

    private static void setConfig(Context context, String configName, int config) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(configName, config);
        editor.apply();
    }

    private static void setConfig(Context context, String configName, String config) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(configName, config);
        editor.apply();
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(RESOURCE, Context.MODE_PRIVATE);
        return sharedPref.edit();
    }

    private static int getIntConfig(Context context, String configName) {
        SharedPreferences sharedPref = context.getSharedPreferences(RESOURCE, Context.MODE_PRIVATE);
        return sharedPref.getInt(configName, 0);
    }

    private static String getStringConfig(Context context, String configName) {
        SharedPreferences sharedPref = context.getSharedPreferences(RESOURCE, Context.MODE_PRIVATE);
        return sharedPref.getString(configName, "");
    }
}
