package com.wizarpos.pay.cashier.pay_tems;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.log.util.StringUtil;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.OrderDef;

import java.util.HashMap;
import java.util.Map;

/**
 * 轮询查询订单状态接口 Created by wu on 2015/6/26 0026.
 */
public class LooperQueryerTransactionState extends LooperTask {

    private String orderNo; // 流水号

    // 订单状态(1未支付2已支付3撤销4交易关闭)

    public LooperQueryerTransactionState(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    protected void doTask() {
        if (TextUtils.isEmpty(orderNo)) {
            return;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderNo", orderNo);
        NetRequest.getInstance().addRequest(Constants.SC_954_ORDER_DEF_DETAIL,
                params, new ResponseListener() {

                    @Override
                    public void onSuccess(Response response) {
                        if (response.result == null) {
                            response.code = 1;
                            LogEx.d("LooperQueryerTransactionState", String.valueOf(response.code));
                            response.msg = "Data parsing failed";
                            finish(response);
                            return;
                        }
                        JSONObject obj = (JSONObject) response.getResult();
                        String exchangeRate = obj.getString("exchangeRate");
                        String cnyAmount = obj.getString("cnyAmount");
                        if (!TextUtils.isEmpty(exchangeRate)) {
                            AppConfigHelper.setConfig(AppConfigDef.exchangeRate, exchangeRate);
                        }
                        if (!TextUtils.isEmpty(cnyAmount)) {
                            AppConfigHelper.setConfig(AppConfigDef.CNY_AMOUNT, cnyAmount);
                        }
                        OrderDef order = JSONObject.parseObject(
                                response.result.toString(), OrderDef.class);
                        Logger2.debug("轮询到订单状态"
                                + OrderDef.getOrderStateDes(order.getState()));
                        if (StringUtil.isSameString(order.getTimeOutFlag(), OrderDef.TIME_OUT)) {
                            order.setState(OrderDef.STATE_TIME_OUT);
                        }
                        if (OrderDef.STATE_NOT_PAY == (order.getState())) {
                            goon();
                        } else {
                            Response result = new Response(0, OrderDef
                                    .getOrderStateDes(order.getState()), order);
                            finish(result);
                        }
                    }

                    @Override
                    public void onFaild(Response response) {
                        Logger2.debug("无法轮询到订单状态,继续轮询");
                        goon();
                    }
                });
    }
}
