package com.wizarpos.pay.recode.zusao.bean.acti;

import android.os.Parcel;
import android.os.Parcelable;

import com.wizarpos.pay.recode.zusao.constants.ZsPayChannelEnum;

public class ZSShowQrCodeActivityParam implements Parcelable {

    private ZsPayChannelEnum zsPayChannelEnum;

    private String realPath;

    private String orderNo;

    private String realAmount;


    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

    public ZsPayChannelEnum getZsPayChannelEnum() {
        return zsPayChannelEnum;
    }

    public void setZsPayChannelEnum(ZsPayChannelEnum zsPayChannelEnum) {
        this.zsPayChannelEnum = zsPayChannelEnum;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public ZSShowQrCodeActivityParam() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.zsPayChannelEnum == null ? -1 : this.zsPayChannelEnum.ordinal());
        dest.writeString(this.realPath);
        dest.writeString(this.orderNo);
        dest.writeString(this.realAmount);
    }

    protected ZSShowQrCodeActivityParam(Parcel in) {
        int tmpZsPayChannelEnum = in.readInt();
        this.zsPayChannelEnum = tmpZsPayChannelEnum == -1 ? null : ZsPayChannelEnum.values()[tmpZsPayChannelEnum];
        this.realPath = in.readString();
        this.orderNo = in.readString();
        this.realAmount = in.readString();
    }

    public static final Creator<ZSShowQrCodeActivityParam> CREATOR = new Creator<ZSShowQrCodeActivityParam>() {
        @Override
        public ZSShowQrCodeActivityParam createFromParcel(Parcel source) {
            return new ZSShowQrCodeActivityParam(source);
        }

        @Override
        public ZSShowQrCodeActivityParam[] newArray(int size) {
            return new ZSShowQrCodeActivityParam[size];
        }
    };
}
