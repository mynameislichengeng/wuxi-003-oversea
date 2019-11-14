package com.wizarpos.pay.common.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class ZXingUtil {

	public static Bitmap genQRCodeTow(String content) {
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		try {
			BitMatrix matrix = new MultiFormatWriter().encode(content,
					BarcodeFormat.QR_CODE, 300, 300);
			int width = matrix.getWidth();
			int height = matrix.getHeight();
			// 二维矩阵转为一维像素数组,也就是一直横着排了，注意添加白色背景，否则打印会是黑色。
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = 0xFF000000;
					} else {// 二维码背景白色
						pixels[y * width + x] = 0xFFFFFFFF;
					}
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			// 通过像素数组生成bitmap,具体参考api
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
