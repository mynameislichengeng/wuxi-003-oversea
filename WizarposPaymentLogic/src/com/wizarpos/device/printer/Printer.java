package com.wizarpos.device.printer;

import android.graphics.Bitmap;

/**
 * 打印驱动接口
 * 抛给硬件
 *
 * @author wu
 */
public interface Printer {

    void print(String str);

    void print(Bitmap bitmap);

    void cutPaper();
}
