package com.wizarpos.recode.data;

import android.text.TextUtils;

import com.wizarpos.recode.constants.TransRecordLogicConstants;
import com.wizarpos.recode.data.info.MidConfigManager;

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

    /**
     * @param prefixMid 前缀-商户号
     * @param suffixId
     * @return
     */
    public static String createTranLogId(String prefixMid, String suffixId) {
        return TransRecordLogicConstants.TRANLOGSTART_PRFIX + prefixMid + suffixId;
    }

    /**
     * 整理tranlog格式，按照 商户号-log
     *
     * @param tranlogId
     * @return
     */
    public static String createTranlogFormartToActivity(String tranlogId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TransRecordLogicConstants.TRANLOGSTART_PRFIX);
        stringBuilder.append(spliteMid(tranlogId));
        stringBuilder.append("-");
        stringBuilder.append(spliteTranlogSubffix(tranlogId));
        return stringBuilder.toString();
    }

    public static String createTranlogFormatMid(String tranlogId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TransRecordLogicConstants.TRANLOGSTART_PRFIX);
        stringBuilder.append(spliteMid(tranlogId));
        return stringBuilder.toString();
    }


    public static String createTranlogFormatSuffixlog(String tranlogId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("-");
        stringBuilder.append(spliteTranlogSubffix(tranlogId));
        return stringBuilder.toString();
    }


    public static String[] createTranlogForPrintFormart(String tranlogId) {
        try {
            String[] arr = new String[2];
            String mid = spliteMid(tranlogId);
            arr[0] = TransRecordLogicConstants.TRANLOGSTART_PRFIX + mid;

            String logs = spliteTranlogSubffix(tranlogId);
            StringBuffer sbtemps = new StringBuffer();
            sbtemps.append("-");
            for (int i = 0; i < logs.length(); i++) {
                char a = logs.charAt(i);
                sbtemps.append(a);
                if ((i + 1) % 4 == 0 && (i + 1) != logs.length()) {
                    sbtemps.append(" ");
                }
            }
            arr[1] = sbtemps.toString();
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 求出商户号，因为tranlog是由商户号+tranlogSubffix（后缀）组成
     *
     * @param tranLogId
     * @return
     */
    public static String spliteMid(String tranLogId) {
        if (tranLogId.startsWith(TransRecordLogicConstants.TRANLOGSTART_PRFIX)) {
            tranLogId = tranLogId.substring(1);
        }
        //因为商户号，都是等长的，所以取出自己本身的商户号，求出长度
        int midLenght = MidConfigManager.getMid().length();
        if (tranLogId.length() < midLenght) {
            return "";
        } else {
            return tranLogId.substring(0, midLenght);
        }
    }

    /**
     * 求出后缀
     *
     * @param tranLogId
     * @return
     */
    public static String spliteTranlogSubffix(String tranLogId) {
        if (tranLogId.startsWith(TransRecordLogicConstants.TRANLOGSTART_PRFIX)) {
            tranLogId = tranLogId.substring(1);
        }
        //因为商户号，都是等长的，所以取出自己本身的商户号，求出长度
        int midLenght = MidConfigManager.getMid().length();
        if (tranLogId.length() < midLenght) {
            return "";
        } else {
            return tranLogId.substring(midLenght);
        }

    }


}
