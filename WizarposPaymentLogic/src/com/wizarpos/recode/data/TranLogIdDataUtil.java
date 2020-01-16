package com.wizarpos.recode.data;

import android.text.TextUtils;

import com.wizarpos.recode.constants.TransRecordLogicConstants;

public class TranLogIdDataUtil {

    /**
     * 移除P
     *
     * @param tranLogId
     * @return
     */
    public static String removeCharPForTranLogId(String tranLogId) {
        if (!TextUtils.isEmpty(tranLogId)) {
            if (tranLogId.startsWith(TransRecordLogicConstants.TRANLOGSTART_PRFIX)) {
                return tranLogId.substring(1);
            }
        }
        return tranLogId;
    }
}
