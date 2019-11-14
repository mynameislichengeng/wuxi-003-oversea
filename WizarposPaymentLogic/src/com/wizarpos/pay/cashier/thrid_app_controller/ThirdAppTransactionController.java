package com.wizarpos.pay.cashier.thrid_app_controller;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppLoginEXJsonResponse;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppLoginResponse;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppTransactionBtnAvailable;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppTransactionRequest;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppTransactionResponse;
import com.wizarpos.pay.cashier.thrid_app_controller.model.ThirdAppVoidTransRequest;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.common.utils.UuidUitl;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;

/**
 * 第三方应用流程控制类
 *
 * @author wu
 */
public class ThirdAppTransactionController {

    public static final String THIRD_APP = "THIRD_APP";
    private static final String TRANS_TYPE_TRANSACT = "transact";
    private static final String TRANS_TYPE_LOGIN = "login";
    private static final String TRANS_TYPE_HANDOVER = "handover";//交接班
    private static final String TRANS_TYPE_RECHARGE = "recharge";
    private static final String TRANS_TYPE_VOID = "void";
	public static final String TRANS_TYPE_NET_TRANSACT = "netTransact";//网络收单支付
    private static final String ForbidNative = "1";
    private static final String ForbidMicro = "2";
    public static final String  RECHARGE = "recharge";

    private ThirdAppTransactionRequest requestBean;

    private ThirdAppVoidTransRequest thirdAppVoidTransRequest;

    private static ThirdAppTransactionController controller;

    private TransactionTemsController temsController;
    private ThirdAppFinisher finisher;

    private boolean isInservice = false;

    private ThirdAppTransactionController() {
    }

    public static ThirdAppTransactionController getInstance() {
        if (controller == null) {
            controller = new ThirdAppTransactionController();
        }
        return controller;
    }

    /**
     * 重置
     */
    public void reset() {
        LogEx.d("third app", "第三方交易结束,重置交易");
        requestBean = null;
        isInservice = false;
        AppStateManager.setState(AppStateDef.isInService, Constants.FALSE);
    }

    /**
     * 设置交易方式控制者
     *
     * @param controller
     */
    public void setTransactionTemsController(
            TransactionTemsController controller) {
        this.temsController = controller;
    }

    public void setThridAppFinisher(ThirdAppFinisher finisher) {
        this.finisher = finisher;
    }

    /**
     * 解析第三方请求参数
     *
     * @param intent
     */
    public void parseRequest(Intent intent) {
        String request = intent.getStringExtra(THIRD_APP);
        if (TextUtils.isEmpty(request) == false) {
            parseRequest(request);
        }
    }

    /**
     * 解析请求参数
     */
    public void parseRequest(String request) {
        Logger2.debug("请求参数:" + request);
        if (TextUtils.isEmpty(request)) {
            return;
        }
        if (isInservice == true) {
            errorRequest("已在进行其他交易");
            return;
        }
        try {
        	requestBean = JSONObject.parseObject(request,
                    ThirdAppTransactionRequest.class);
		} catch (Exception e) {
			e.printStackTrace();
			errorRequest("数据解析错误");
			reset();
			return;
		}
        if (requestBean == null
                || TextUtils.isEmpty(requestBean.getTransType())) {
            errorRequest("请求参数错误");
            reset();
            return;
        }
        String transType = requestBean.getTransType();// 交易类型
        if (TRANS_TYPE_TRANSACT.equals(transType) || TRANS_TYPE_NET_TRANSACT.equals(transType)) {// 消费
            if (TextUtils.isEmpty(requestBean.getAmount())) {
                errorRequest("请求参数错误");
                reset();
            } else if (Constants.TRUE.equals(AppStateManager.getState(AppStateDef.isLogin)) == false) {
                errorRequest("请先登录");
                reset();
            } else {
                setInService();
                if (requestBean.getChoosePayMode() != 0) {//若设置了支付方式 直接跳转对应的界面
                    temsController.skipToTransaction(requestBean);
                    return;
                }
                if (requestBean.getBtnAvailable() != null) {
                    filtePayMode();
                }
            }
        } else if (TRANS_TYPE_LOGIN.equals(transType)) {// 登陆
            if (Constants.TRUE.equals(AppStateManager
                    .getState(AppStateDef.isLogin))) {
                bundleLoginResponse();
            } else {
                setInService();
            }
        } else if (TRANS_TYPE_HANDOVER.equals(transType)) {//交接班
            setInService();
        } else if (TRANS_TYPE_RECHARGE.equals(transType)) {// 充值
            if (TextUtils.isEmpty(requestBean.getAmount())) {
                errorRequest("请求参数错误");
                reset();
            } else if (Constants.TRUE.equals(AppStateManager.getState(AppStateDef.isLogin)) == false) {
                errorRequest("请先登录");
                reset();
            } else {
                setInService();
                ThirdAppTransactionBtnAvailable btnAvailable = new ThirdAppTransactionBtnAvailable();// 会员卡支付不允许会员卡支付,混合支付,其他支付,卡券核销
                btnAvailable.setBtnMemberPay("1");
                btnAvailable.setBtnMixPay("1");
                btnAvailable.setBtnOtherPay("1");
                btnAvailable.setBtnTicketPay("1");
                requestBean.setBtnAvailable(btnAvailable);
                filtePayMode();
            }
        } else if (TRANS_TYPE_VOID.equals(transType)) {//交接班
            thirdAppVoidTransRequest = JSONObject.parseObject(request, ThirdAppVoidTransRequest.class);
            if(TextUtils.isEmpty(thirdAppVoidTransRequest.getOrderNo())){
                errorRequest("请求参数错误");
                reset();
            } else if (Constants.TRUE.equals(AppStateManager.getState(AppStateDef.isLogin)) == false) {
                errorRequest("请先登录");
                reset();
            }else{
                setInService();
            }
        } else {
            errorRequest("不支持的交易类型");
            reset();
        }
    }

    private void setInService() {
        isInservice = true;
        AppStateManager.setState(AppStateDef.isInService, Constants.TRUE);
    }

    /**
     * 过滤支付方法
     */
    private void filtePayMode() {
        isUseCardTransaction();
        isUseMemberTransaction();
        isUseCashTransaciont();
        isUseMixTransaction();
        isUseTicketPassTransaction();
        isUseAlipay();
        isUseWxpay();
        isUseTenpay();
        isUseBaiduPay();
        isUseOtherPay();
    }

    public void bundleLoginResponse() {
        try {
            ThirdAppLoginEXJsonResponse exJsonResponse = new ThirdAppLoginEXJsonResponse();
            exJsonResponse.setOperatorNo(AppConfigHelper.getConfig(AppConfigDef.operatorNo));
            exJsonResponse.setMid(AppConfigHelper.getConfig(AppConfigDef.mid));
            exJsonResponse.setFid(AppConfigHelper.getConfig(AppConfigDef.fid));
            exJsonResponse.setMerchantId(AppConfigHelper.getConfig(AppConfigDef.merchantId));
            exJsonResponse.setTerminalId(AppConfigHelper.getConfig(AppConfigDef.terminalId));
            exJsonResponse.setOperatorId(AppConfigHelper.getConfig(AppConfigDef.operatorId));
            exJsonResponse.setOperatorName(AppConfigHelper.getConfig(AppConfigDef.operatorTrueName));
            exJsonResponse.setMerchantName(AppConfigHelper.getConfig(AppConfigDef.merchantName));
            exJsonResponse.setPermission(AppConfigHelper.getConfig(AppConfigDef.permission));
            exJsonResponse.setIp(AppConfigHelper.getConfig(AppConfigDef.ip));
            exJsonResponse.setPort(AppConfigHelper.getConfig(AppConfigDef.port));
            exJsonResponse.setSaleDeductType(AppConfigHelper.getConfig(AppConfigDef.saleDeductType));
            exJsonResponse.setIsOpenMemberDeduct(AppConfigHelper.getConfig(AppConfigDef.isOpenMemberDeduct));
            exJsonResponse.setServerVersion(Constants.SERVER_VERSION);
            if (Constants.UNIFIEDLOGIN_FLAG){
                exJsonResponse.setAppPushId(UuidUitl.getUuid());
                exJsonResponse.setSessionId(AppConfigHelper.getConfig(AppConfigDef.sessionId));
                exJsonResponse.setAuthFlag(AppConfigHelper.getConfig(AppConfigDef.authFlag));
                exJsonResponse.setUnifiedLogin(Constants.UNIFIEDLOGIN_FLAG?"1":"0");
            }
            ThirdAppLoginResponse response = new ThirdAppLoginResponse();
            response.setCode(0);
            response.setMessage("success");
            response.setExJson(exJsonResponse);
            Intent intent = new Intent();
            intent.putExtra("response", JSONObject.toJSONString(response));
            finisher.finishTransacton(Activity.RESULT_OK, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void errorRequest(String errorInfo) {
        Logger2.debug(errorInfo);
        ThirdAppTransactionResponse response = new ThirdAppTransactionResponse();
        response.setCode(1);
        response.setMessage(errorInfo);
        Intent intent = new Intent();
        intent.putExtra("response", JSONObject.toJSONString(response));
        finisher.finishTransacton(Activity.RESULT_CANCELED, intent);
    }

    /**
     * 是否禁用银行卡
     */
    private void isUseCardTransaction() {
        if (isForbid(requestBean.getBtnAvailable().getBtnBankPay())) {
            if (temsController != null)
                temsController.hideCardTransaction();
        }
    }

    /**
     * 是否禁用会员卡
     */
    private void isUseMemberTransaction() {
        if (isForbid(requestBean.getBtnAvailable().getBtnMemberPay())) {
            if (temsController != null)
                temsController.hideMemberTransaction();
        }
    }

    /**
     * 是否禁用现金
     */
    private void isUseCashTransaciont() {
        if (isForbid(requestBean.getBtnAvailable().getBtnCashPay())) {
            if (temsController != null)
                temsController.hideCashTransaction();
        }
    }

    /**
     * 是否禁用混合支付
     */
    private void isUseMixTransaction() {
        if (isForbid(requestBean.getBtnAvailable().getBtnMixPay())) {
            if (temsController != null)
                temsController.hideMixTransaction();
        }
    }

    /**
     * 是否禁用卡券核销
     */
    private void isUseTicketPassTransaction() {
        if (isForbid(requestBean.getBtnAvailable().getBtnTicketPay())) {
            if (temsController != null)
                temsController.hideTicketPassTransaction();
        }
    }

    /**
     * 是否启用支付宝支付
     */
    private void isUseAlipay() {
        if (isForbid(requestBean.getBtnAvailable().getBtnAliPay())) {
            if (temsController != null)
                temsController.hideAlipayTransaction();
            return;
        }
        if (ForbidNative.equals(requestBean.getBtnAvailable().getAliPayFlag())) {
            if (temsController != null)
                temsController.hideAlipayNativeTransaction();
        }
        if (ForbidMicro.equals(requestBean.getBtnAvailable().getAliPayFlag())) {
            if (temsController != null)
                temsController.hideAlipayMicroTransaction();
        }
    }

    /**
     * 是否启用微信支付
     */
    private void isUseWxpay() {
        if (isForbid(requestBean.getBtnAvailable().getBtnWxPay())) {
            if (temsController != null)
                temsController.hideWxpayTransacton();
            return;
        }
        if (ForbidNative.equals(requestBean.getBtnAvailable().getWxPayFlag())) {
            if (temsController != null)
                temsController.hideWxpayNativeTransaction();
        }
        if (ForbidMicro.equals(requestBean.getBtnAvailable().getWxPayFlag())) {
            if (temsController != null)
                temsController.hideWxpayMicroTransaction();
        }
    }

    /**
     * 是否启用手Q支付
     */
    private void isUseTenpay() {
        if (isForbid(requestBean.getBtnAvailable().getBtnQQPay())) {
            if (temsController != null)
                temsController.hideTenpayTransaction();
            return;
        }
        if (ForbidNative.equals(requestBean.getBtnAvailable().getQqPayFlag())) {
            if (temsController != null)
                temsController.hideTenpayNativeTransaction();
        }
        if (ForbidMicro.equals(requestBean.getBtnAvailable().getQqPayFlag())) {
            if (temsController != null)
                temsController.hideTenpayMicroTransaction();
        }
    }

    /**
     * 是否启用百度支付
     */
    private void isUseBaiduPay() {
        if (isForbid(requestBean.getBtnAvailable().getBtnBaiduPay())) {
            if (temsController != null)
                temsController.hideBaiduTransaction();
            return;
        }
        if (ForbidNative
                .equals(requestBean.getBtnAvailable().getBaiduPayFlag())) {
            if (temsController != null)
                temsController.hideBaiduNativeTransaction();
        }
        if (ForbidMicro.equals(requestBean.getBtnAvailable().getBaiduPayFlag())) {
            if (temsController != null)
                temsController.hideBaiduMicroTransaction();
        }
    }

    private void isUseOtherPay() {
        if (isForbid(requestBean.getBtnAvailable().getBtnOtherPay())) {
            if (temsController != null) temsController.hideOtherTransaction();
            return;
        }
    }

    private boolean isForbid(String req) {
        return TextUtils.isEmpty(req) == false;
    }

    public ThirdAppTransactionRequest getRequestBean() {
        return requestBean;
    }

    public ThirdAppVoidTransRequest getThirdAppVoidTransRequest() {
        return thirdAppVoidTransRequest;
    }

    public boolean isInservice() {
        return isInservice;
    }

    public void bundleResponse(Intent paySuccessIntent) {
        try {
            finisher.finishTransacton(Activity.RESULT_OK,
                    paySuccessIntent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reset();
        }
    }

    public void destory(){
        this.finisher = null;
    }


}
