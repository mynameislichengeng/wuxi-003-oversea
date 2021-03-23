package com.wizarpos.pay.recode.zusao.constants;

public enum ZsPayChannelEnum {
    ALIPAY("A", "alipay_native", "ALIPAY", "Alipay"),
    WX("W", "weixin_native", "WECHAT PAY","Wechat Pay"),
    CLOUD("UNS", "unionOvs_native","UNION PAY", "Union Pay"),

    ;

    ZsPayChannelEnum(String channel, String flag, String title, String desc) {
        this.channel = channel;
        this.flag = flag;
        this.title = title;
        this.desc = desc;
    }

    private final String channel;
    private final String flag;
    private final String desc;

    private final String title;

    public String getChannel() {
        return channel;
    }

    public String getFlag() {
        return flag;
    }

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }

    public static ZsPayChannelEnum of(String channel){
        ZsPayChannelEnum[] items = values();
        for (ZsPayChannelEnum item:items){
            if(item.getChannel().equals(channel)){
                return item;
            }
        }
        return ALIPAY;
    }
}
