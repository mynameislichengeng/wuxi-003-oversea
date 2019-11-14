package com.wizarpos.pay.model;

import java.io.Serializable;

/**
 * Created by blue_sky on 2016/3/23.
 * 显示日结单
 */
public class TodayDetailBean implements Serializable {
    private int image;
    private Integer color = 0xffffff;
    private String detailName;
    private long count;
    private String amount;

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
