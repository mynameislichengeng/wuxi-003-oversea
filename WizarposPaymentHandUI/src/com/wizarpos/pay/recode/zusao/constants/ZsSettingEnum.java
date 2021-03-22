package com.wizarpos.pay.recode.zusao.constants;

public enum ZsSettingEnum {
    ACTIVELY("1", "主扫"),
    PASSIVE("2", "被扫"),
    ;

    private final String type;
    private final String desc;

    ZsSettingEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
