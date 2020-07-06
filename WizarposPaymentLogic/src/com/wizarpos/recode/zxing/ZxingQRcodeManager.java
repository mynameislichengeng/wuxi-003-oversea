package com.wizarpos.recode.zxing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.wizarpos.recode.zxing.bean.QRCodeParam;

import java.util.Hashtable;

public class ZxingQRcodeManager {

    private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static final String CHARSET = "UTF-8";
    private static final Bitmap.Config BIT_MAP_FORMAT = Bitmap.Config.RGB_565;


    /**
     * user: Rex
     * date: 2016年12月29日  上午12:31:29
     *
     * @param qrCodeParam 二维码内容
     * @return 返回二维码图片
     * @throws
     * @throws
     */
    public static Bitmap createOnlyImg(QRCodeParam qrCodeParam) {
        //二维码尺寸
        int picWidth = qrCodeParam.getPicWidth();
        int picHeight = qrCodeParam.getPicHeight();
        int qrWidth = qrCodeParam.getQrWidth();
        int qrHeight = qrCodeParam.getQrHeight();
        int hintMargin = qrCodeParam.getHintType_MARGIN();
        int qrColor = qrCodeParam.getQrColor();
        String content = qrCodeParam.getQrContent();
        BarcodeFormat mBarFormat = qrCodeParam.getBarcodeFormat();

        Canvas canvas = null;

        Bitmap result = Bitmap.createBitmap(picWidth, picHeight, BIT_MAP_FORMAT);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas = new Canvas(result);

        //先画一整块白色矩形块
        {
            canvas.drawRect(0, 0, picWidth, picHeight, paint);
        }

        {
            //画二维码或者条形码
            Bitmap image = createNativeCodeBitMap(content, mBarFormat, qrWidth, qrHeight, hintMargin);
            paint.setColor(qrColor);
            int bitmapLeft = (picWidth - qrWidth) / 2;
            int bitmapTop = 0;
            canvas.drawBitmap(image, bitmapLeft, bitmapTop, paint);
        }
        canvas.save();
        canvas.restore();
        return result;
    }

    /**
     * 生成图片  加上底部文本
     *
     * @param qrCodeParam
     * @return
     */
    public static Bitmap createImageAndBottomText(QRCodeParam qrCodeParam) {
        int picWidth = qrCodeParam.getPicWidth();//生成图片的宽度
        int picHeight = qrCodeParam.getPicHeight();//生成图片的高度
        int qrWidth = qrCodeParam.getQrWidth();
        int qrHeight = qrCodeParam.getQrHeight();
        int qrColor = qrCodeParam.getQrColor();
        String content = qrCodeParam.getQrContent();
        int hintMargin = qrCodeParam.getHintType_MARGIN();
        BarcodeFormat mBarFormat = qrCodeParam.getBarcodeFormat();

        //最终生成的图片
        Bitmap result = Bitmap.createBitmap(picWidth, picHeight, BIT_MAP_FORMAT);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
//        paint.setTypeface(Typeface.DEFAULT_BOLD);
        Canvas canvas = new Canvas(result);

        //先画一整块白色矩形块
        {
            canvas.drawRect(0, 0, picWidth, picHeight, paint);
        }


        int qrTop = 0;//条码距离顶部的距离
        {
            //画二维码
            Bitmap image = createNativeCodeBitMap(content, mBarFormat, qrWidth, qrHeight, hintMargin);
            paint.setColor(qrColor);
            int bitmapLeft = (picWidth - qrWidth) / 2;
            canvas.drawBitmap(image, bitmapLeft, qrTop, paint);
        }


        {
            Rect bounds = new Rect();
            //画文字
            QRCodeParam.BottomText bottomText = qrCodeParam.getBottomText();
            int textMariginTop = bottomText.getMarginTop();
            int bottomTextSize = bottomText.getBottomTextSize();
            int lineTextCount = bottomText.getLineTextCount();
            int textColor = bottomText.getTextColor();
            String bottomContent = bottomText.getBottomContent();


            paint.setColor(textColor);
            paint.setTextSize(bottomTextSize);
            //每一行文本的字数

            //可能需要几行文字
            int line = (int) (Math.ceil(Double.valueOf(bottomContent.length()) / Double.valueOf(lineTextCount)));
            int textTop = qrTop + qrHeight + textMariginTop;//地址的顶部高度
            for (int i = 0; i < line; i++) {
                String s;
                if (i == line - 1) {
                    s = bottomContent.substring(i * lineTextCount);
                } else {
                    s = bottomContent.substring(i * lineTextCount, (i + 1) * lineTextCount);
                }
                paint.getTextBounds(bottomContent, 0, s.length(), bounds);

                canvas.drawText(s, picWidth / 2 - bounds.width() / 2, textTop + i * (bounds.height()), paint);
            }
        }


        canvas.save();
        canvas.restore();

        return result;
    }


    /**
     * 生成
     *
     * @param content
     * @param mBarFormat
     * @param oriWidth
     * @param oriHeight
     * @param hintMargin
     * @return
     */
    private static Bitmap createNativeCodeBitMap(String content, BarcodeFormat mBarFormat, int oriWidth, int oriHeight, int hintMargin) {
        //画二维码
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, hintMargin);
        BitMatrix bitMatrix;
        Bitmap image = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, mBarFormat, oriWidth, oriHeight, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            image = Bitmap.createBitmap(width, height, BIT_MAP_FORMAT);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setPixel(x, y, bitMatrix.get(x, y) ? BLACK : WHITE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
}
