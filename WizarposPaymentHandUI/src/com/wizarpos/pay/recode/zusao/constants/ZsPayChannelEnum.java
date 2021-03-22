package com.wizarpos.pay.recode.zusao.constants;

public enum ZsPayChannelEnum {
    ALIPAY("A", "alipay_native", "Alipay"),
    WX("W", "weixin_native", "Wechat Pay"),
    CLOUD("UNS", "unionOvs_native", "UnionOvs"),

    ;

    ZsPayChannelEnum(String channel, String flag, String desc) {
        this.channel = channel;
        this.flag = flag;
        this.desc = desc;
    }

    private final String channel;
    private final String flag;
    private final String desc;

    public String getChannel() {
        return channel;
    }

    public String getFlag() {
        return flag;
    }

    public String getDesc() {
        return desc;
    }
}
