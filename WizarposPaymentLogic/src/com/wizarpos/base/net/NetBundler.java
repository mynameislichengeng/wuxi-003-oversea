package com.wizarpos.base.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wizarpos.atool.log.Logger;
import com.wizarpos.atool.tool.ByteUtil;
import com.wizarpos.atool.util.GetSnHelper;
import com.wizarpos.pay.app.AppConfigInitUtil;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.common.utils.RSAHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;

import static com.wizarpos.pay.db.AppConfigHelper.getConfig;

/**
 * Created by wu on 16/3/16.
 */
public class NetBundler {

    public String getUrl() {
        return "https://"
                + Constants.DEFAULT_IP
//                + ":"
//                + Constants.DEFAULT_PORT
                + Constants.SUFFIX_URL;
    }

    /**
     * 请求实体
     *
     * @param servierCode
     * @param params
     * @return
     */
    public String bundleMsgRequest(String servierCode, Object params) {
        appendParams((Map<String, Object>) params);

        MsgRequest request = new MsgRequest();
        request.setServiceCode(servierCode);
        request.setParam(params);
        request.setPem(getPubCertAlias());
        request.setSuffix(createSuffix());
        Log.d(NetRequest.LOG_TAG, request.toString());
//        if (DeviceManager.getInstance().isWizarDevice()) { //非慧银机具直接绕过 wu
//            String signPart = JSON.toJSONString(request.getParam(), SerializerFeature.WriteDateUseDateFormat);
//            byte[] s = DeviceManager.getInstance().doRSAEncrypt(signPart);
//            request.setSignature(ByteUtil.byteToHex(s));
//        }
//        if (!DeviceManager.getInstance().isWizarDevice()) { //非慧银机具加密报文
//            //除train环境中暂不支持加密 @yaosong [20160112]
//            if (AppConfigHelper.getConfig(AppConfigDef.ip).contains(AppConfigInitUtil.IP_TRAIN)) {
//                return getSecureParams(request);
//            }
//        }
        return JSON.toJSONString(request, SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 请求头
     *
     * @return
     */
    public Headers bundleHeader() {
        boolean isDbNull = (null == PaymentApplication.getInstance().getDbController());
        String defaultSn = "";
        if (isDbNull)
            if (DeviceManager.getInstance().isWizarDevice() || DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_SHENGTENG_M10) {
                defaultSn = android.os.Build.SERIAL;//终端序列号
            } else if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_PULAN) {
                defaultSn = GetSnHelper.getMacAndSn(PaymentApplication.getInstance());
            } else if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_N3_OR_N5) {
                defaultSn = PaymentApplication.getInstance().deviceEngine.getDeviceInfo().getSn();
            } else if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_PAX_A920) {
                defaultSn = android.os.Build.SERIAL;//终端序列号
            } else {
                defaultSn = DeviceManager.getImei(PaymentApplication.getInstance());//IMEI地址
            }
        String sn = AppConfigHelper.getConfig(AppConfigDef.sn);
        String operatorId = AppConfigHelper.getConfig(AppConfigDef.operatorNo);
        String operatorName = AppConfigHelper.getConfig(AppConfigDef.operatorTrueName);
        Headers.Builder builder = new Headers.Builder();
        builder.add("charset", "UTF-8");
        builder.add("sn", isDbNull ? defaultSn : sn);
        builder.add("language", AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE));
        builder.add("mid", isDbNull ? "" : AppConfigHelper.getConfig(AppConfigDef.mid));
        builder.add("operatorId", operatorId);
        builder.add("version", Constants.SERVER_VERSION);//增加传入接口版本 wu
//        if (!DeviceManager.getInstance().isWizarDevice()) { // 如果不是慧银机具,直接绕过 wu
        builder.add("mobileType", "hanxin");
        //现在除train环境中，还暂不支持rsaFlag @yaosong [20160314]
//            if (AppConfigHelper.getConfig(AppConfigDef.ip).contains(AppConfigInitUtil.IP_TRAIN)) {
//                builder.add("rsaFlag", "1");    //非慧银机具 header中此标志不为空时，服务器便会进行解密操作。(不再以mobileType和service_code判断) @yaosong [20160314]
//            }
//        }
        //加入sessionId
        if (!TextUtils.isEmpty(AppConfigHelper.getConfig(AppConfigDef.sessionId)) && Constants.SESSION_FLAG) {
            builder.add(AppConfigDef.sessionId, AppConfigHelper.getConfig(AppConfigDef.sessionId));
        }
        return builder.build();
    }

    /**
     * 获取证书
     *
     * @return
     */
    @NonNull
    private String getPubCertAlias() {
        String pubCertAlias = AppStateManager.getState(AppStateDef.PUB_CERT_AILAS);
        if (TextUtils.isEmpty(pubCertAlias) && DeviceManager.getInstance().isWizarDevice()) {
            pubCertAlias = DeviceManager.getInstance().getPubCertificateSynchronized();
        }
        return pubCertAlias;
    }

    /**
     * 获取 Suffix
     *
     * @return
     */
    @NonNull
    private Map<String, Object> createSuffix() {
        boolean isDbNull = (null == PaymentApplication.getInstance().getDbController());
        String mid = isDbNull ? "" : getConfig(AppConfigDef.mid);
        String fid = isDbNull ? "" : getConfig(AppConfigDef.fid);
        String operatorNo = isDbNull ? "" : getConfig(AppConfigDef.operatorNo);
        String operatorName = isDbNull ? "" : getConfig(AppConfigDef.operatorTrueName);
        String unifiedLogin = com.wizarpos.pay.common.Constants.UNIFIEDLOGIN_FLAG ? "1" : "0";//商户终端体系改造后此变量为"1"
        String authFlag = isDbNull ? "" : getConfig(AppConfigDef.authFlag);
        String appPushId = isDbNull ? "" : getConfig(AppConfigDef.appPushId);
        Map<String, Object> suffix = new HashMap<String, Object>();
        suffix.put("mid", mid);
        suffix.put("fid", fid);
        suffix.put("operatorNo", operatorNo);
        suffix.put("operatorName", operatorName);
        if (com.wizarpos.pay.common.Constants.UNIFIEDLOGIN_FLAG) {
            suffix.put("unifiedLogin", unifiedLogin);
            suffix.put("authFlag", authFlag);
            suffix.put("appPushId", appPushId);
        }
        return suffix;
    }

    /**
     * 每次请求带上收单商户号,终端号,慧商户号,收单渠道,收单操作员号
     *
     * @param params
     */
    private void appendParams(Map<String, Object> params) {
        if (!params.containsKey("merchantId")) {
            params.put("merchantId", AppConfigHelper.getConfig(AppConfigDef.merchantId));
        }
        if (!params.containsKey("terminalId")) {
            params.put("terminalId", AppConfigHelper.getConfig(AppConfigDef.terminalId));
        }
        if (!params.containsKey("payId")) {
            params.put("payId", AppConfigHelper.getConfig(AppConfigDef.pay_id));
        }
        if (!params.containsKey("mid")) {
            params.put("mid", AppConfigHelper.getConfig(AppConfigDef.mid));
        }
        if (!params.containsKey("operatorId")) {
            params.put("operatorId", AppConfigHelper.getConfig(AppConfigDef.operatorId));
        }
    }

    /**
     * 获取加密的报文参数
     *
     * @param msgRequest
     * @return
     */
    private String getSecureParams(MsgRequest msgRequest) {
        String secureParam = null;
        String es = null;
        try {
            secureParam = JSON.toJSONString(msgRequest, SerializerFeature.WriteDateUseDateFormat);
            Logger.debug("请求实体：" + secureParam);
//			Logger2.debug("请求实体:" + secureParam);
            es = RSAHelper.encryptString(RSAHelper.publicKey, secureParam);
        } catch (Exception e) {
            e.printStackTrace();
            Logger2.debug("加密失败");
        }
        return es != null ? es : null;
    }

}
