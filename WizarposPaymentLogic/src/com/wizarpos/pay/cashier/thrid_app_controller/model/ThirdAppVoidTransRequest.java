package com.wizarpos.pay.cashier.thrid_app_controller.model;

import com.wizarpos.pay.cashier.thrid_app_controller.model.base.ThirdRequest;

import java.io.Serializable;

/**
 * 撤销请求
 * Created by wu on 15/12/9.
 */
public class ThirdAppVoidTransRequest extends ThirdRequest implements Serializable {

    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
