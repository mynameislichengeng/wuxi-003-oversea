package com.wizarpos.pay.setting.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

import java.util.Locale;

/**
 * Created by wu on 2016/10/25.
 */

public class LanguageUtils {

    public static void changeLanguage(Context context, int language) {
        ResourcesConfig.setLanguage(context, language);
        doChangeLanguageAction(context, language);
    }

    private static void doChangeLanguageAction(Context context, int language) {
        switch (language) {
            case 0:
                setLanguage(context, Locale.ENGLISH);
                AppConfigHelper.setConfig(AppConfigDef.SWITCH_LANGUAGE, "en");
                break;
            case 1:
                setLanguage(context, Locale.FRENCH);
                AppConfigHelper.setConfig(AppConfigDef.SWITCH_LANGUAGE, "fr");
                break;
            case 2:
                setLanguage(context, Locale.CHINA);
                AppConfigHelper.setConfig(AppConfigDef.SWITCH_LANGUAGE, "zh");
                break;
        }
    }

    public static void setLanguage(Context context, Locale locale) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.locale = locale;
        resources.updateConfiguration(config, dm);
    }

    public static int getLanguage(Context context) {
        return ResourcesConfig.getLanguage(context);
    }

    public static void init(Context context) {
        doChangeLanguageAction(context, getLanguage(context));
    }
}
