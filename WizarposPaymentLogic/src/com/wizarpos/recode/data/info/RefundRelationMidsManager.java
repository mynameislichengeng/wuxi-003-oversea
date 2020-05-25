package com.wizarpos.recode.data.info;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class RefundRelationMidsManager {
    private static List<String> nameLists = new ArrayList<>();


    /**
     * 返回跟当前账号有关的商户，并且当前商户放在第一个
     *
     * @param contents
     */
    public static void settingRefundNameDefault(List<String> contents) {
        nameLists.clear();
        String str = MidConfigManager.getMid();
        //将当前商户放在第一个
        if (!TextUtils.isEmpty(str)) {
            nameLists.add(str);
        }

        if (contents != null) {
            for (String item : contents) {
                if (!item.equals(str)) {
                    nameLists.add(item);
                }
            }

        }
    }

    public static List<String> getRelationMid() {
        return nameLists;
    }

}
