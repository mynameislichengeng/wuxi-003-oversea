package com.wizarpos.pay.recode.zusao.bean.acti;

import android.os.Parcel;
import android.os.Parcelable;

import com.wizarpos.pay.recode.zusao.constants.ZsPayChannelEnum;

public class ZSPaySuccessActivityParam implements Parcelable {

    private ZsPayChannelEnum zsPayChannelEnum;

    private String realAmount;

    private String resultJson;

    public ZsPayChannelEnum getZsPayChannelEnum() {
        return zsPayChannelEnum;
    }

    public void setZsPayChannelEnum(ZsPayChannelEnum zsPayChannelEnum) {
        this.zsPayChannelEnum = zsPayChannelEnum;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

    public String getResultJson() {
        return resultJson;
    }

    public void setResultJson(String resultJson) {
        this.resultJson = resultJson;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.zsPayChannelEnum == null ? -1 : this.zsPayChannelEnum.ordinal());
        dest.writeString(this.realAmount);
        dest.writeString(this.resultJson);
    }

    public ZSPaySuccessActivityParam() {
    }

    protected ZSPaySuccessActivityParam(Parcel in) {
        int tmpZsPayChannelEnum = in.readInt();
        this.zsPayChannelEnum = tmpZsPayChannelEnum == -1 ? null : ZsPayChannelEnum.values()[tmpZsPayChannelEnum];
        this.realAmount = in.readString();
        this.resultJson = in.readString();
    }

    public static final Parcelable.Creator<ZSPaySuccessActivityParam> CREATOR = new Parcelable.Creator<ZSPaySuccessActivityParam>() {
        @Override
        public ZSPaySuccessActivityParam createFromParcel(Parcel source) {
            return new ZSPaySuccessActivityParam(source);
        }

        @Override
        public ZSPaySuccessActivityParam[] newArray(int size) {
            return new ZSPaySuccessActivityParam[size];
        }
    };
}
