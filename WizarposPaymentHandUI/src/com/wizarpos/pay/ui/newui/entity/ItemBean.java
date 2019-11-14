package com.wizarpos.pay.ui.newui.entity;

import com.wizarpos.pay.view.ArrayItem;

/**
 * Created by blue_sky on 2016/3/22.
 * 显示右侧条目
 */
public class ItemBean extends ArrayItem{
    private int img;
    private String title;
    private boolean isUpdate=false;

    public ItemBean(int img, String title, boolean isUpdate) {
        this.img = img;
        this.title = title;
        this.isUpdate = isUpdate;
    }

    public ItemBean(int img, String title, boolean isUpdate ,int realValue) {
        super(realValue,title);
        this.img = img;
        this.title = title;
        this.isUpdate = isUpdate;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    @Override
    public String toString() {
        return "ItemBean{" +
                "img=" + img +
                ", title='" + title + '\'' +
                ", isUpdate=" + isUpdate +
                '}';
    }
}
