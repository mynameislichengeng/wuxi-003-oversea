package com.wizarpos.device.printer.html.model;

import java.io.Serializable;

/**
 * TODO
 * Created by Song on 2017/10/27.
 */

public abstract class HtmlLine implements Serializable{
    private boolean isBold = false;

    abstract public String convertline();

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }
}
