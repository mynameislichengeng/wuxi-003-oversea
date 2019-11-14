package com.wizarpos.pay.view.util;

import java.io.Serializable;

/**
 * Created by wu on 15/10/13.
 */
public class MultieChooseItem implements Serializable {//bugfix wu

    private int imgId;
    private String title;

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getImgId() {
        return imgId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
