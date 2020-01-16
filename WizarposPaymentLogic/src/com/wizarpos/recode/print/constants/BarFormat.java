package com.wizarpos.recode.print.constants;


import com.nexgo.oaf.apiv3.device.printer.BarcodeFormatEnum;


public enum BarFormat {


    CODE39(0, "code39", BarcodeFormatEnum.CODE_39),

    CODE93(1, "code93", BarcodeFormatEnum.CODE_93),

    CODE128(2, "code128", BarcodeFormatEnum.CODE_128),

    EAN13(3, "ean13", BarcodeFormatEnum.EAN_13),
    ;

    int type;
    String name;
    BarcodeFormatEnum n3Value;

    BarFormat(int type, String name, BarcodeFormatEnum n3Value) {
        this.type = type;
        this.name = name;
        this.n3Value = n3Value;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public BarcodeFormatEnum getN3Value() {
        return n3Value;
    }

    public static BarFormat getEnum(int type) {
        for (BarFormat s : BarFormat.values()) {
            if (s.getType() == type) {
                return s;
            }
        }
        return BarFormat.CODE39;
    }

}
