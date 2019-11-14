package com.wizarpos.pay.model;

import java.io.Serializable;

public class RefundOperResp  implements Serializable {

    /**
     * refundOperId : 6134494510
     * refundOperName : Shen shen
     */

    private String refundOperId;
    private String refundOperName;

    public String getRefundOperId() {
        return refundOperId;
    }

    public void setRefundOperId(String refundOperId) {
        this.refundOperId = refundOperId;
    }

    public String getRefundOperName() {
        return refundOperName;
    }

    public void setRefundOperName(String refundOperName) {
        this.refundOperName = refundOperName;
    }
}
