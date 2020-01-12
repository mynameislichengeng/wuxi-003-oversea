package com.wizarpos.recode.receipt.constants;

public enum ReceiptBarcodeStatusEnum {

    OPEN("1"),
    CLOSE("0");
    private String status;

    ReceiptBarcodeStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
