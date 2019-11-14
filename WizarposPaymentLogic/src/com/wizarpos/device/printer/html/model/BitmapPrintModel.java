package com.wizarpos.device.printer.html.model;

import android.graphics.Bitmap;

import java.io.File;
import java.io.Serializable;

/**
 * Bitmap打印队列模型
 * Created by Song on 2017/10/31.
 */

public class BitmapPrintModel implements Serializable{

    public BitmapPrintModel(Bitmap bm, File imageFile) {
        this.bm = bm;
        this.imageFile = imageFile;
    }

    private Bitmap bm;
    private File imageFile;

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }
}
