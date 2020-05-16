package com.wizarpos.recode.receipt.constants;

public enum ReceiptStatusEnum {

    OPEN("1"),
    CLOSE("0");
    private String status;

    ReceiptStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
