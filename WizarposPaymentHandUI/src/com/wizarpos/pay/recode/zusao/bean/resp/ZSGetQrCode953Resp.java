package com.wizarpos.pay.recode.zusao.bean.resp;

public class ZSGetQrCode953Resp {

    private String realPath;

    private OrderDef orderDef;


    public String getRealPath() {
        return realPath;
    }

    public OrderDef getOrderDef() {
        return orderDef;
    }

    public void setOrderDef(OrderDef orderDef) {
        this.orderDef = orderDef;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public static class OrderDef {
        private String orderNo;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }
    }
}
