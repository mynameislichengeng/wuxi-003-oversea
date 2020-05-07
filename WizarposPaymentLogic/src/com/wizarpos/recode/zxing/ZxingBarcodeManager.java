package com.wizarpos.recode.zxing;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * zx-条形码
 */
public class ZxingBarcodeManager {
    private static final String TAG = ZxingBarcodeManager.class.getSimpleName();

    private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;
    private static Bitmap.Config BITMAP_CONFIG = Bitmap.Config.RGB_565;


    public static Bitmap creatBarcode(String contents, int desiredWidth, int desiredHeight) {
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        try {
            result = writer.encode(contents, barcodeFormat, desiredWidth,
                    desiredHeight);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int width = result.getWidth();
        int height = result.getHeight();
        log("before width:" + width + ", height:" + height);
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                BITMAP_CONFIG);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static void log(String msg) {
        Log.d("print", TAG + ">>" + msg);
    }
}
