package com.wizarpos.pay.login.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wizarpos.atool.util.GetSnHelper;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.hspos.api.EnumCommand;
import com.wizarpos.log.util.PreferenceHelper;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cardlink.CardLinkPresenter;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.DialogHelper;
import com.wizarpos.pay.common.DialogHelper.DialogCallbackAndNo;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.pay.common.utils.AppInfoUtils;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.common.utils.MD5Util;
import com.wizarpos.pay.common.utils.TimeJudgeUtil;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.common.utils.UuidUitl;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;
import com.wizarpos.pay.db.TransactionManager;
import com.wizarpos.pay.db.UserDao;
import com.wizarpos.pay.db.UserEntityDao;
import com.wizarpos.pay.manager.presenter.CashierOpreatorManager;
import com.wizarpos.pay.model.AppInfo;
import com.wizarpos.pay.model.LoginResp;
import com.wizarpos.pay.model.LoginedMerchant;
import com.wizarpos.pay.model.SysParam;
import com.wizarpos.pay.model.UserEntity;
import com.wizarpos.pay.test.TestStartMenuActivity;
import com.wizarpos.recode.data.info.MidConfigManager;
import com.wizarpos.recode.data.info.RefundRelationMidsManager;
import com.wizarpos.recode.constants.HttpConstants;
import com.wizarpos.recode.data.info.SnManager;
import com.wizarpos.recode.print.devicesdk.amp.AMPPrintManager;
import com.wizarpos.recode.sale.service.InvoiceLoginServiceImpl;
import com.wizarpos.recode.util.PackageAndroidManager;
import com.wizarpos.wizarpospaymentlogic.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wu
 */
public class LoginPresenter2 extends BasePresenter {

    protected LoginPresenterListener loginPresenterListener;
    private String verifeCode = null;//验证码
    private boolean isUnNeedVerification = false;//是否免验证
    private boolean isNeedResetPassword = false;//是否需要重设密码
    private String newPassword2Reset = null;
    private String inputMid = null;//用户选择的商户号
    protected List<LoginedMerchant> localMerchants;//本地保存的商户

    protected LoginResp loginResp;
    /**
     * 慧商户操作员
     */
    protected String operatorNo = null;
    protected String passwd = "";

    /* 用户管理 */
    protected UserEntityDao cashierController;

    private CardLinkPresenter cardLinkPresenter;
    private JSONObject loginObj;
    private boolean isOffline;
    private AppInfo info = null;//应用信息，每周只上送一次

    public void setVerifeCode(String verifeCode) {
        this.verifeCode = verifeCode;
    }

    public interface LoginPresenterListener {

        void onUnNeedVerification(Response response);

        void onNeedVerification(Response response);

        void onLoginSuccess(Response response);

        void onLoginFaild(Response response);
    }

    public LoginPresenter2(Context context) {
        super(context);
        this.loginPresenterListener = (LoginPresenterListener) context;
        cardLinkPresenter = new CardLinkPresenter();
        cardLinkPresenter.init();
    }


    public void handleIntent(Intent intent) {
        cashierController = UserEntityDao.getInstance();
    }

    /**
     * @deprecated 登录
     */
    public void login(String username, String passwd) {
        this.operatorNo = username;
        this.passwd = passwd;
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(passwd)) {
            loginPresenterListener.onLoginFaild(new Response(1, PaymentApplication.getInstance().getResources().getString(R.string.pleas_input_password)));
            return;
        }
        if (Constants.DEBUGING) {
            if ("99".equals(username) && "122112".equals(passwd)) {
                context.startActivity(new Intent(context, TestStartMenuActivity.class));
                ((Activity) context).finish();
                return;
            }
        }
        if (!cashierController.isUser(username, passwd)) { // 首先验证用户名和密码,如果用户名和密码验证失败,则直接返回,不再进行后续操作
            loginPresenterListener.onLoginFaild(new Response(1, PaymentApplication.getInstance().getResources().getString(R.string.password_error)));
            return;
        }
        updateMerchantInfo();
    }

    /**
     * 登录
     */
    public void login(String username, String passwd, String mid) {
        this.operatorNo = username;
        this.passwd = passwd;
        this.inputMid = mid;
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(passwd)
                || TextUtils.isEmpty(mid)) {
            loginPresenterListener.onLoginFaild(new Response(1, PaymentApplication.getInstance().getResources().getString(R.string.please_input_complete_info)));
            return;
        }
//        if (Constants.DEBUGING) {
//            if ("99".equals(username) && "122112".equals(passwd)) {
//                context.startActivity(new Intent(context, TestStartMenuActivity.class));
//                ((Activity) context).finish();
//                return;
//            }
//        }
//        if (!cashierController.isUser(username, passwd)) { // 首先验证用户名和密码,如果用户名和密码验证失败,则直接返回,不再进行后续操作
//            loginPresenterListener.onLoginFaild(new Response(1, "用户名密码错误"));
//            return;
//        }  //现在操作员都在服务端那边存储 本地只负责存储，不进行修改
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mid", mid);
        params.put("loginName", username);
        params.put("passwd", MD5Util.getMd5Str(passwd));

        String terminalUniqNo = getSn();
        params.put("terminalUniqNo", terminalUniqNo);
        AppConfigHelper.setConfig(AppConfigDef.appPushId, terminalUniqNo);
        if (!TextUtils.isEmpty(verifeCode) || isUnNeedVerification) {
            LoginedMerchant temp = isLoginedMid(mid);
            String lastTime = (temp == null) ? "0" : "" + temp.getLastOpreatorUpateTime();
            params.put("lastTime", lastTime);
            if (!TextUtils.isEmpty(verifeCode)) {
                params.put("vcode", verifeCode);
            }
            if (!TextUtils.isEmpty(getNewPassword2Reset()) && isNeedResetPassword) {
                params.put("newPasswd", MD5Util.getMd5Str(getNewPassword2Reset()));
            }
            updateMerchantInfo(params);
        } else {
            doVerifacion(params);
        }
    }

    private String getSn() {
        return SnManager.getSn(context);
    }

    /**
     * 商户终端体系改造 首次登陆进行短信验证
     */
    private void doVerifacion(Map<String, Object> params) {
        Logger2.debug("开始请求是否需要验证");
        NetRequest.getInstance().addRequest(Constants.SC_119_FIRST_LOGIN, params, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                JSONObject jsonObject = JSONObject.parseObject(response.getResult().toString());
                //判断是否需要验证码
                if (jsonObject.getBoolean("smsFlag")) {
                    if (!TextUtils.isEmpty(jsonObject.getString("markedWords"))) {
                        response.setMsg(jsonObject.getString("markedWords"));
                    }
                    if (jsonObject.getBoolean("changePassWdFlag")) {
                        setNeedResetPassword(true);
                    } else {
                        setNeedResetPassword(false);
                    }
                    loginPresenterListener.onNeedVerification(response);
                } else {
                    isUnNeedVerification = true;
                    loginPresenterListener.onUnNeedVerification(response);
                }
            }

            @Override
            public void onFaild(Response response) {
                if (NetRequest.NETWORK_ERR == response.getCode()) {
                    isUnNeedVerification = true;
                    if (loginPresenterListener != null) {
//                        loginPresenterListener.onUnNeedVerification(new Response(1, "网络不通，跳过请求验证码"));
                        loginPresenterListener.onUnNeedVerification(new Response(1, PaymentApplication.getInstance().getResources().getString(R.string.no_network)));
                    }
                } else if (response.getCode() == 174) {//短信验证码已经发过，30分钟内有效
                    JSONObject jsonObject = JSONObject.parseObject(response.getResult().toString());
                    Response response2 = new Response(0, "短信验证码已发，在三十分钟内有效，请输入验证码");
                    if (!TextUtils.isEmpty(jsonObject.getString("markedWords"))) {
                        response.setMsg(jsonObject.getString("markedWords"));
                    }
                    if (loginPresenterListener != null) {
                        loginPresenterListener.onNeedVerification(response2);
                    }
                } else {
                    if (loginPresenterListener != null) {
                        loginPresenterListener.onLoginFaild(new Response(1, response.msg + "[" + response.code + "]"));
                    }
                }
            }
        });
    }

    /**
     * @deprecated 更新商户信息
     */
    public void updateMerchantInfo() {
        Logger2.debug("开始更新商户信息");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("weixinMemberCardUsable", true);
        params.put("bankCardUsable", true);
        params.put("sysPosTimeStamp", "");
        if (Constants.APP_VERSION_NAME == Constants.APP_VERSION_LAWSON) {
            params.put("luosenFlag", "1");//1:罗森 0:正常
        }
        params.put(HttpConstants.API_100_PARAM.TERMINALVERSION.getKey(), PackageAndroidManager.getVersionName(context));
        NetRequest.getInstance().addRequest(Constants.SC_100_MERCHANT_INFO_SUBMIT, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                AppStateManager.setState(AppStateDef.isOffline, Constants.FALSE);
                try {
                    loginResp = JSONObject.parseObject(response.getResult().toString(), LoginResp.class);
                    if (loginResp == null || loginResp.getMerchant() == null || loginResp.getTerminal() == null) {
                        loginPresenterListener.onLoginFaild(new Response(1, PaymentApplication.getInstance().getResources().getString(R.string.merchant_data_parse_error)));
                        return;
                    }
                    //根据mid来创建表
                    PaymentApplication.getInstance().initDb(loginResp.getMerchant().getMid());
                    Logger2.debug("获取到商户信息");
                    signOnFinish();
//                    getPosInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                    loginPresenterListener.onLoginFaild(new Response(1, PaymentApplication.getInstance().getResources().getString(R.string.merchant_data_parse_error)));
                }
            }

            @Override
            public void onFaild(Response response) {
                if (NetRequest.NETWORK_ERR == response.getCode()) {
                    Logger2.debug("无法登陆,无法连接到服务器");
                    AppStateManager.setState(AppStateDef.isOffline, Constants.TRUE);
                    loginPresenterListener.onLoginFaild(new Response(NetRequest.NETWORK_ERR, PaymentApplication.getInstance().getResources().getString(R.string.cannot_conect_service)));
                    //离线状态不允许第三方应用登录 wu@[20151123]
//                    if (ThirdAppTransactionController.getInstance().isInservice()) {
//                        loginPresenterListener.onLoginFaild(new Response(BaseRequest.NETWORK_ERR, "无法连接到服务器"));
//                        return;
//                    }
//
//                    if (Constants.APP_VERSION_NAME == Constants.APP_VERSION_SIMPLE){//极简版不允许离线登录
//                        Logger2.debug("无法连接到服务器,请检查网络是否异常!");
//                        loginPresenterListener.onLoginFaild(new Response(1, "无法连接到服务器,请检查网络是否异常!"));
//                        return;
//                    }
//
//                    String offlineStr = AppConfigHelper.getConfig(AppConfigDef.offline_save);
//                    if (TextUtils.isEmpty(offlineStr)) {
//                        Logger2.debug("首次登陆需要连接到服务器,请检查网络是否异常!");
//                        loginPresenterListener.onLoginFaild(new Response(1, "首次登陆需要连接到服务器,请检查网络是否异常!" + "["
//                                + response.code + "]"));
//                        return;
//                    }
//                    loginResp = JSONObject.parseObject(offlineStr, LoginResp.class);
//                    if (loginResp == null || loginResp.getMerchant() == null || loginResp.getTerminal() == null) {
//                        Logger2.debug("首次登陆需要连接到服务器,请检查网络是否异常!");
//                        loginPresenterListener.onLoginFaild(new Response(1, "首次登陆需要连接到服务器,请检查网络是否异常!" + "["
//                                + response.code + "]"));
//                        return;
//                    }
//                    Logger2.debug("离线登陆");
//                    if (DeviceManager.getInstance().isSupportBankCard()) {
//                        getPosInfo();
//                    } else {
//                        newDefaultPaymentRouter();
//                        showOfflineDialog();
//                    }

                } else {
                    Logger2.debug("无法登陆,且非网络或服务器故障：" + response.msg);
                    AppStateManager.setState(AppStateDef.isOffline, Constants.FALSE);
                    loginPresenterListener.onLoginFaild(new Response(1, response.msg + "[" + response.code + "]"));
                }
            }
        });
    }

    private void updateMerchantInfo(Map<String, Object> params) {
        Logger2.debug("开始更新商户信息");
        if (params == null) {
            return;
        }
        if (!DeviceManager.getInstance().isWizarDevice()) {
            params.put("appBelong", AppInfoUtils.getPayVersionBelong(PaymentApplication.getInstance().getApplicationContext()));
            params.put("packageName", AppInfoUtils.getPackageName(PaymentApplication.getInstance().getApplicationContext()));
            params.put("appVersion", AppInfoUtils.getPayVersionName(PaymentApplication.getInstance().getApplicationContext()));
        }
        if (isLoginedMid(inputMid) == null
                || TimeJudgeUtil.isWeeklyFirst(isLoginedMid(inputMid).getLastTerminalInfoUploadTime())) {
            Gson gson = new Gson();
            info = AppInfoUtils.getAppInfo(context);
            params.put("terminalInfo", gson.toJson(info));
//            Log.i("YS!!", gson.toJson(AppInfoUtils.getAppInfo(context)));
        }
        params.put(HttpConstants.API_100_PARAM.TERMINALVERSION.getKey(), PackageAndroidManager.getVersionName(context));
        NetRequest.getInstance().addRequest(Constants.SC_100_MERCHANT_INFO_SUBMIT, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                AppStateManager.setState(AppStateDef.isOffline, Constants.FALSE);
                isOffline = false;
                try {

                    loginResp = JSONObject.parseObject(response.getResult().toString(), LoginResp.class);
                    loginObj = (JSONObject) response.getResult();
                    //根据mid来创建表
                    PaymentApplication.getInstance().initDb(loginResp.getMerchant().getMid());
                    if (loginResp == null || loginResp.getMerchant() == null || loginResp.getTerminal() == null) {
                        loginPresenterListener.onLoginFaild(new Response(1, PaymentApplication.getInstance().getResources().getString(R.string.merchant_data_parse_error)));
                        return;
                    }
                    Logger2.debug("获取到商户信息");
                    signOnFinish();
                } catch (Exception e) {
                    e.printStackTrace();
                    loginPresenterListener.onLoginFaild(new Response(1, PaymentApplication.getInstance().getResources().getString(R.string.merchant_data_parse_error)));
                }
            }

            @Override
            public void onFaild(Response response) {
                //登录失败清空验证码
                verifeCode = null;
                if (NetRequest.NETWORK_ERR == response.getCode()) {
                    Logger2.debug("无法登陆,无法连接到服务器");
                    AppStateManager.setState(AppStateDef.isOffline, Constants.TRUE);
                    isOffline = true;
                    loginPresenterListener.onLoginFaild(new Response(NetRequest.NETWORK_ERR, PaymentApplication.getInstance().getResources().getString(R.string.cannot_conect_service)));
                } else {
                    Logger2.debug("无法登陆,且非网络或服务器故障：" + response.msg);
                    AppStateManager.setState(AppStateDef.isOffline, Constants.FALSE);
                    isOffline = false;
                    loginPresenterListener.onLoginFaild(new Response(1, response.msg + "[" + response.code + "]"));
                }
            }
        });
    }

    protected void offlineCheck() {
        Logger2.debug("验证数据库数据是否合法");
        String checkResult = checkMechantAndOperator();
        if (!TextUtils.isEmpty(checkResult)) {
            loginPresenterListener.onLoginFaild(new Response(1, checkResult));
            return;
        }
        showOfflineDialog();
    }

    private void showOfflineDialog() {
        DialogHelper.showChoiseDialog(context, "网络不通,是否启用离线登陆?", new DialogCallbackAndNo() {
            public void callback() {
                bundleResult();
            }

            @Override
            public void callbackNo() {
                loginPresenterListener.onLoginFaild(new Response(NetRequest.NETWORK_ERR, "无法连接到服务器"));
            }
        });
    }

    /**
     * 解析并保存商户信息
     */
    private void saveMerchantInfo() {
        Logger2.debug("解析并保存商户信息");
        try {
            //根据mid来创建表
//            PaymentApplication.getInstance().initDb(loginResp.getMerchant().getMid());

            // 存储慧商户信息
            AppConfigHelper.setConfig(AppConfigDef.pay_id, loginResp.getMerchant().getPayId());// 支付渠道
            MidConfigManager.setMid(loginResp.getMerchant().getMid());
//            AppConfigHelper.setConfig(AppConfigDef.mid, );// 慧商户号
            AppConfigHelper.setConfig(AppConfigDef.fid, loginResp.getTerminal().getFid());// 慧商户联盟号
            AppConfigHelper.setConfig(AppConfigDef.collectTips, loginResp.getMerchantDefSuffix().getCollectTips());//是否启用小费  ON启用 OFF禁用
            AppConfigHelper.setConfig(AppConfigDef.tipsPercentageAllow, loginResp.getMerchantDefSuffix().getTipsPercentageAllow());//是否允许手动设置百分比，T允许，F不允许
            AppConfigHelper.setConfig(AppConfigDef.tipsCustomAllow, loginResp.getMerchantDefSuffix().getTipsCustomAllow());//是否允许顾客输入小费金额  T允许  F不允许
            //invoice码是否强制输入(0 关闭 ，1开启)
            InvoiceLoginServiceImpl.getInstatnce().setAppconfifMandatoryFlag(loginResp.getMerchantDefSuffix().getMandatoryFlag());
            JSONObject jsonObject = JSONObject.parseObject(loginResp.getMerchantDefSuffix().getTipsPercentage());
            if (null != jsonObject) {
                AppConfigHelper.setConfig(AppConfigDef.percentP1, jsonObject.getString("p1"));
                AppConfigHelper.setConfig(AppConfigDef.percentP2, jsonObject.getString("p2"));
                AppConfigHelper.setConfig(AppConfigDef.percentP3, jsonObject.getString("p3"));
            }
            AppConfigHelper.setConfig(AppConfigDef.tipsTerminalAllow, loginResp.getMerchantDefSuffix().getTipsTerminalAllow());//是否允许客户端输入小费金额  T允许  F不允许
            AppConfigHelper.setConfig(AppConfigDef.fid, loginResp.getTerminal().getFid());// 慧商户联盟号
            AppConfigHelper.setConfig(AppConfigDef.sysPosTimeStamp, loginResp.getSysPosTimeStamp() + "");
            AppConfigHelper.setConfig(AppConfigDef.merchantName, loginResp.getMerchant().getMerchantName());// 商户名称
            if (!TextUtils.isEmpty(loginResp.getMerchant().getMerchantAddr())) {
                AppConfigHelper.setConfig(AppConfigDef.merchantAddr, loginResp.getMerchant().getMerchantAddr());
            }//获取商户地址
            if (!TextUtils.isEmpty(loginResp.getMerchant().getMerchantTel())) {
                AppConfigHelper.setConfig(AppConfigDef.merchantTel, loginResp.getMerchant().getMerchantTel());
            }//获取商户电话号码
            AppConfigHelper.setConfig(AppConfigDef.use_pay_route_config, loginResp.getMerchant().getUsePayRouteConfig());
            AppConfigHelper.setConfig(AppConfigDef.merchant_has_weixin, loginResp.isMerchant_has_weixin() + "");
            AppConfigHelper.setConfig(AppConfigDef.merchantType, loginResp.getMerchantType());//增加录入商户类型 (为微盟添加) wu
            AppConfigHelper.setConfig(AppConfigDef.isAlipay, loginResp.getAlipay());
            AppConfigHelper.setConfig(AppConfigDef.isWepay, loginResp.getWeixin());
            AppConfigHelper.setConfig(AppConfigDef.isBaidupay, loginResp.getBaidu());
            AppConfigHelper.setConfig(AppConfigDef.isTenpay, loginResp.getQq());
            //销售整单提成  wu@[20151119]
            AppConfigHelper.setConfig(AppConfigDef.saleDeductType, loginResp.getMerchant().getSaleDeductType());
            //会员充值提成  yaosong@[20160118]
            AppConfigHelper.setConfig(AppConfigDef.isOpenMemberDeduct, loginResp.getMerchant().getIsOpenMemberDeduct());
            //商户 logo
            AppConfigHelper.setConfig(AppConfigDef.merchantHandLogo, loginResp.getMerchant().getHandLogoUrl());
            AppConfigHelper.setConfig(AppConfigDef.merchantPOSLogo, loginResp.getMerchant().getAppLogoUrl());
            if (loginResp.getAgentMerchant() != null) {
                AppConfigHelper.setConfig(AppConfigDef.agentHandLogo, loginResp.getAgentMerchant().getHandLogoUrl());
                AppConfigHelper.setConfig(AppConfigDef.agentPosLogo, loginResp.getAgentMerchant().getAppLogoUrl());
            }
            AppConfigHelper.setConfig(AppConfigDef.agentHandLogo, loginResp.getMerchant().getHandLogoUrl());
            AppConfigHelper.setConfig(AppConfigDef.agentPosLogo, loginResp.getMerchant().getAppLogoUrl());
            for (SysParam param : loginResp.getSysParams()) {
                if (param.getSysKey().equals("app_service_addr")) {
                    AppConfigHelper.setConfig(AppConfigDef.ip, param.getSysValue());
                } else if (param.getSysKey().equals("app_service_port")) {
                    AppConfigHelper.setConfig(AppConfigDef.port, param.getSysValue());
                } else if (param.getSysKey().equals("mina_port")) {
                    AppConfigHelper.setConfig(AppConfigDef.order_port, param.getSysValue());
                }
            }
            AppConfigHelper.setConfig(AppConfigDef.offline_save, JSONObject.toJSONString(loginResp));
            AppConfigHelper.setConfig(AppConfigDef.operatorNo, operatorNo);
            AppConfigHelper.setConfig(AppConfigDef.operatorTrueName,
                    UserDao.getInstance().getRealNameByUserName(operatorNo));
            if (DeviceManager.getInstance().isSupportBankCard() == false) {//手机不支持银行卡消费，不能
//                newDefaultPaymentRouter();
            }
            RefundRelationMidsManager.settingRefundNameDefault(loginResp.getRefundRelationMids());

            // 存储收单应用信息
            AppConfigHelper.setConfig(AppConfigDef.CARDLINK_MECHANTID, loginResp.getMechantId());
            AppConfigHelper.setConfig(AppConfigDef.CARDLINK_TERMINALID, loginResp.getTerminalId());
            AppConfigHelper.setConfig(AppConfigDef.CARDLINK_AUTHCODE, loginResp.getAuthCode());
            AppConfigHelper.setConfig(AppConfigDef.CARDLINK_TPDU, loginResp.getTpddu());
            AppConfigHelper.setConfig(AppConfigDef.CARDLINK_SERVERPORT, loginResp.getServerPort());
            AppConfigHelper.setConfig(AppConfigDef.CARDLINK_SERVERIP, loginResp.getServerIP());

            //商户终端体系改造，存储虚拟终端号
            AppConfigHelper.setConfig(AppConfigDef.sn, loginResp.getTerminal().getSn());
            CashierOpreatorManager cashierOpreatorManager = new CashierOpreatorManager(context);
            //存储操作员信息
            AppConfigHelper.setConfig(AppConfigDef.operatorNo, operatorNo);
            AppConfigHelper.setConfig(AppConfigDef.operatorTrueName,
                    loginResp.getMuser().getName());
            //更新登录操作员自身数据
            UserEntity self = loginResp.getMuser();
            if (isNeedResetPassword) {
                self.setPassword(MD5Util.getMd5Str(getNewPassword2Reset()));
            }
            List<UserEntity> upDataList = new ArrayList<UserEntity>();
            if (null != loginResp.getUserList()) {
                upDataList.addAll(loginResp.getUserList());
            }
            upDataList.add(self);
            cashierOpreatorManager.updateLastCashier(upDataList);
            //取所有操作员中最大的lastTime存储
            AppConfigHelper.setConfig(AppConfigDef.lastOpreatorUpateTime, cashierOpreatorManager.getLastUpdateTime() + "");
            AppConfigHelper.setConfig(AppConfigDef.authFlag, loginResp.getMuser().getAdminFlag());
            saveLoginedMerchant(loginResp.getMerchant().getMerchantName(), loginResp.getMerchant().getMid(), loginResp.getUserList());
        } catch (Exception e) {
            e.printStackTrace();
            loginPresenterListener.onLoginFaild(new Response(1, PaymentApplication.getInstance().getResources().getString(R.string.merchant_data_parse_error)));
        }
    }

    /**
     * yaosong [20160218]
     * 存储商户名和商户号、管理员到SP中
     */
    private void saveLoginedMerchant(String name, String mid, List<UserEntity> userList) {
        LoginedMerchant bean = new LoginedMerchant();
        bean.setMerchantName(name);
        bean.setMid(mid);
        bean.setLastOpreatorUpateTime(new CashierOpreatorManager(context).getLastUpdateTime());
        PreferenceHelper sp = new PreferenceHelper(context, AppConfigDef.SP_loginedInfo);
        String oldMerchants = sp.getString(AppConfigDef.SP_loginedMerchant, "[]");
        sp.putString(AppConfigDef.SP_lastLoginMid, mid);
        List<LoginedMerchant> beans = localMerchants;
        List<String> admins = new ArrayList<String>();
        LoginedMerchant temp = isLoginedMid(mid);
        bean.setMerchantName(name);
        bean.setMid(mid);
        bean.setLastOpreatorUpateTime(new CashierOpreatorManager(context).getLastUpdateTime());
        if (null != info) {
            bean.setLastTerminalInfoUploadTime(info.getUploadTime());
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Log.i("YS!!", dateFormater.format(new Date(info.getUploadTime())) + "应用信息上送成功");
        }
        if (temp != null) {
            for (UserEntity user : userList) {
                if (user.getAdminFlag().equals("1")) {
                    admins.add(user.getLoginName());
                }
            }
            //是否有管理员相关的员工信息变更
            if (!admins.isEmpty()) {
                bean.setAdministrators(admins);
            }
            beans.remove(temp);
        } else {
            //首次登陆管理员必须返回
            for (UserEntity user : userList) {
                if (user.getAdminFlag().equals("1")) {
                    admins.add(user.getLoginName());
                }
            }
            bean.setAdministrators(admins);
        }
        beans.add(bean);
        String newMerchants = JSONArray.toJSONString(beans);
        sp.putString(AppConfigDef.SP_loginedMerchant, newMerchants);
    }

    /**
     * 此mid是否已经登录过本机器
     *
     * @param mid
     * @return
     */
    private LoginedMerchant isLoginedMid(String mid) {
        for (LoginedMerchant temp : localMerchants) {
            if (temp.getMid().equals(mid)) {
                return temp;
            }
        }
        return null;
    }

    private boolean isMidLogined(String mid) {
        PreferenceHelper sp = new PreferenceHelper(context, AppConfigDef.SP_loginedInfo);
        String oldMerchants = sp.getString(AppConfigDef.SP_loginedMerchant, "[]");
        List<LoginedMerchant> Beans = JSONArray.parseArray(oldMerchants, LoginedMerchant.class);
        for (LoginedMerchant bean : Beans) {
            if (bean.getMid().equals(mid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 打包返回数据
     */
    protected void bundleResult() {

//		PaymentApplication.getInstance().initNetRequest();// 更新网络状态

        Logger2.debug("打包返回数据");
        Intent mainIntent = new Intent();
        // if (allloginPay) {
        // mainIntent.putExtra("allloginPay", "1");
        // }
        String permission = "0";
        //bugfix 使用返回的数据判断[20160224]
        UserEntity userEntity = loginResp.getMuser();
        if (userEntity == null) {
//        	loginPresenterListener.onLoginFaild(new Response(-1, "无用户数据"));
            return;
        }
        if (userEntity.getAdminFlag().equals("1")) {// 管理员，权限同原00账号
            AppConfigHelper.setConfig("permission", "2");
            mainIntent.putExtra("roles", "superManager");
            permission = "2";
        } else if (userEntity.getAdminFlag().equals("3")
                || userEntity.getAdminFlag().equals("2")) {// 店长和财务
            AppConfigHelper.setConfig("permission", "1");
            mainIntent.putExtra("roles", "manager");
            permission = "1";
        } else {
            AppConfigHelper.setConfig("permission", "0");
            mainIntent.putExtra("roles", "common");
            permission = "0";
        }
//        if (cashierController.isSuperManger(operatorNo, passwd)) {
//            AppConfigHelper.setConfig("permission", "2");
//            mainIntent.putExtra("roles", "superManager");
//            permission = "2";
//        } else if (cashierController.isManger(operatorNo, passwd)) {
//            AppConfigHelper.setConfig("permission", "1");
//            mainIntent.putExtra("roles", "manager");
//            permission = "1";
//        } else {
//            AppConfigHelper.setConfig("permission", "0");
//            mainIntent.putExtra("roles", "common");
//            permission = "0";
//        }
        mainIntent.putExtra("operatorNo", operatorNo);
        mainIntent.putExtra("mid", loginResp.getMerchant().getMid());
        mainIntent.putExtra("fid", loginResp.getTerminal().getTerminalNo());
        mainIntent.putExtra("merchantName", loginResp.getMerchant().getMerchantName());
        mainIntent.putExtra("permission", permission);
        mainIntent.putExtra("operatorName", AppConfigHelper.getConfig(AppConfigDef.operatorTrueName));
        loginPresenterListener.onLoginSuccess(new Response(0, "success", mainIntent));
    }

    /**
     * 验证商户号,终端号和操作员号
     *
     * @param merchantId
     * @param terminalId
     * @param operatorId
     * @return
     */
    protected boolean checkConfig(String merchantId, String terminalId, String operatorId) {
        boolean _result = checkConfig(merchantId, terminalId);
        if (_result == false) {
            return false;
        }
        if (TextUtils.isEmpty(operatorId)) {
            Toast.makeText(context, "未获取到操作员号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 验证商户号和终端号
     *
     * @param merchantId
     * @param terminalId
     * @return
     */
    protected boolean checkConfig(String merchantId, String terminalId) {
        if (TextUtils.isEmpty(merchantId.trim())) {
            Toast.makeText(context, "未获取到商户号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(terminalId.trim())) {
            Toast.makeText(context, "未获取到终端号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 验证 当前信息 和 数据库存储的信息 是否一致<br>
     * 当一致时,开启离线登陆
     *
     * @return
     */
    private String checkMechantAndOperator() {
        String opt = checkOpt();
        if (TextUtils.isEmpty(opt)) {
            return null;
        } else {
            return opt;
        }
    }

    private String checkOpt() {
        try {
            String oldOptNo = AppConfigHelper.getConfig(AppConfigDef.operatorNo);
            if (oldOptNo.equals(operatorNo)) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "操作员变更,需要联网更新";
    }

    /**
     * 离线数据集合发送！！！！
     */
    public void uploadOfflineTrans() {
        TransactionManager.getInstance().uploadTransaction(new ResultListener() {

            @Override
            public void onSuccess(Response response) {
                Logger2.debug("上送离线交易数据完成");
                AppStateManager.setState(AppStateDef.isLogin, Constants.TRUE);
                bundleResult();
            }

            @Override
            public void onFaild(Response response) {
                UIHelper.ToastMessage(context, "上送离线数据失败");
                AppStateManager.setState(AppStateDef.isLogin, Constants.FALSE);
                bundleResult();
            }
        });
    }

    /**
     * @param listener
     * @Author: Huangweicai
     * @date 2016-1-5 上午11:48:03
     * @Description:供交接班使用(先上送数据 成功后再登录 )
     */
    public void uploadOfflineTrans(final ResultListener listener) {
        TransactionManager.getInstance().uploadTransaction(new ResultListener() {

            @Override
            public void onSuccess(Response response) {
                Logger2.debug("上送离线交易数据完成");
                listener.onSuccess(response);
            }

            @Override
            public void onFaild(Response response) {
                UIHelper.ToastMessage(context, "上送离线数据失败");
                listener.onFaild(response);
            }
        });
    }

    private void loginFailed() {

    }

//    @Override
//    public void onSignOnSuccess(PosInfo posInfo) {
//        this.posInfo = posInfo;
//        signOnFinish();
//    }

//    @Override
//    public void onSignOnFailed(String msg) {
//        newDefaultPaymentRouter();
//        signOnFinish();
//    }

//    private void newDefaultPaymentRouter() {
//        if(posInfo == null){
//            posInfo = new PosInfo();
//        }
//        this.posInfo.setMerchantId("000000");
//        this.posInfo.setOperatorId("000000");
//        this.posInfo.setTerminalId("000000");
//    }

    protected void signOnFinish() {
        Log.d("tagtagtag", "登陆完成signOnFinish()");
        saveMerchantInfo();
        AppStateManager.setState(AppStateDef.isLogin, Constants.TRUE);
        cardLinkPresenter.setCardLinkPresenterListener(new CardLinkPresenter.CardLinkPresenterListener() {
            @Override
            public void onTransFailed(EnumCommand cmd, String error, String message) {
                bundleResult();
            }

            @Override
            public void onProgress(EnumCommand cmd, int progressCode, String message) {
                cardLinkPresenter.continueTrans();
            }

            @Override
            public void onTransSucceed(EnumCommand cmd, Object params) {
                bundleResult();
            }
        });
        cardLinkPresenter.onSetParam();
//        uploadOfflineTrans();
    }

    public void onDestory() {
        loginPresenterListener = null;
    }

    public boolean isNeedResetPassword() {
        return isNeedResetPassword;
    }


    private void setNeedResetPassword(boolean isNeedResetPassword) {
        this.isNeedResetPassword = isNeedResetPassword;
    }


    private String getNewPassword2Reset() {
        return newPassword2Reset;
    }


    public void setNewPassword2Reset(String newPassword2Reset) {
        this.newPassword2Reset = newPassword2Reset;
    }

    public boolean isUnNeedVerification() {
        return isUnNeedVerification;
    }

    public void setUnNeedVerification(boolean isUnNeedVerification) {
        this.isUnNeedVerification = isUnNeedVerification;
    }

    /**
     * TODO 使用本地验证必须设置localMerchants
     *
     * @param localMerchants
     */
    public void setLocalMerchants(List<LoginedMerchant> localMerchants) {
        this.localMerchants = localMerchants;
    }

    public JSONObject getLoginObj() {
        return loginObj;
    }

    /**
     * 供POS判断是否为离线状态
     *
     * @return
     */
    public boolean isOffline() {
        return isOffline;
    }


}
