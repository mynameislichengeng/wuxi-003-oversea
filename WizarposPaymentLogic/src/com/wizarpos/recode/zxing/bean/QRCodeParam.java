package com.wizarpos.recode.zxing.bean;

import android.graphics.Color;

import com.google.zxing.BarcodeFormat;

public class QRCodeParam {

    private int picWidth = 720;//生成图片的宽度
    private int picHeight = 765;//生成图片的高度
    private int qrWidth = 366;//二维码图片宽度
    private int qrHeight = 366;//二维码图片高度
    private int hintType_MARGIN = 0;//二维码的边距
    private String qrContent;//二维码的内容
    private int qrColor = Color.BLACK;//二维码的颜色
    private BarcodeFormat barcodeFormat;


    private BottomText bottomText;//底本文本信息

    public int getPicWidth() {
        return picWidth;
    }

    public void setPicWidth(int picWidth) {
        this.picWidth = picWidth;
    }

    public int getPicHeight() {
        return picHeight;
    }

    public void setPicHeight(int picHeight) {
        this.picHeight = picHeight;
    }

    public int getQrWidth() {
        return qrWidth;
    }

    public void setQrWidth(int qrWidth) {
        this.qrWidth = qrWidth;
    }

    public int getQrHeight() {
        return qrHeight;
    }

    public void setQrHeight(int qrHeight) {
        this.qrHeight = qrHeight;
    }


    public String getQrContent() {
        return qrContent;
    }

    public void setQrContent(String qrContent) {
        this.qrContent = qrContent;
    }


    public int getHintType_MARGIN() {
        return hintType_MARGIN;
    }

    public void setHintType_MARGIN(int hintType_MARGIN) {
        this.hintType_MARGIN = hintType_MARGIN;
    }


    public int getQrColor() {
        return qrColor;
    }

    public void setQrColor(int qrColor) {
        this.qrColor = qrColor;
    }

    public BottomText getBottomText() {
        return bottomText;
    }

    public void setBottomText(BottomText bottomText) {
        this.bottomText = bottomText;
    }

    public BarcodeFormat getBarcodeFormat() {
        return barcodeFormat;
    }

    public void setBarcodeFormat(BarcodeFormat barcodeFormat) {
        this.barcodeFormat = barcodeFormat;
    }

    /**
     * 当二维码下面需要显示文本时
     */
    public static class BottomText {
        private int marginTop = 0;//文本距离顶部距离
        private int marginLeft = 25;//文本左边距
        private int marginRight = 25;//文本右边距
        private int bottomTextSize = 12;//文本的大小
        private int textColor = Color.BLACK;//文本的颜色
        private String bottomContent;//二维码底部显示的内容
        private int lineTextCount = 50;//每一行对大文本数量

        public int getMarginTop() {
            return marginTop;
        }

        public void setMarginTop(int marginTop) {
            this.marginTop = marginTop;
        }

        public int getBottomTextSize() {
            return bottomTextSize;
        }

        public void setBottomTextSize(int bottomTextSize) {
            this.bottomTextSize = bottomTextSize;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public String getBottomContent() {
            return bottomContent;
        }

        public void setBottomContent(String bottomContent) {
            this.bottomContent = bottomContent;
        }

        public int getMarginLeft() {
            return marginLeft;
        }

        public void setMarginLeft(int marginLeft) {
            this.marginLeft = marginLeft;
        }

        public int getMarginRight() {
            return marginRight;
        }

        public void setMarginRight(int marginRight) {
            this.marginRight = marginRight;
        }

        public int getLineTextCount() {
            return lineTextCount;
        }

        public void setLineTextCount(int lineTextCount) {
            this.lineTextCount = lineTextCount;
        }
    }

    /**
     * @param content 二维码的数据
     * @param left    向左偏移多少
     * @param width   二维码宽度
     * @param height  二维码高度
     * @return
     */
    public static QRCodeParam createImgQRCode(String content, int left, int width, int height) {
        QRCodeParam qrCodeParam = createImgBase(content, left, width, height, BarcodeFormat.QR_CODE);
        return qrCodeParam;
    }

    public static QRCodeParam createImgAndBottomTextQRCode(String content, int left, int qrwidth, int qrheight, BottomText bottomText) {
        QRCodeParam qrCodeParam = createImgBase(content, left, qrwidth, qrheight, BarcodeFormat.QR_CODE);
        qrCodeParam.setBottomText(bottomText);

        int allheight = qrCodeParam.getQrHeight() + bottomText.getMarginTop() + bottomText.getBottomTextSize();
        qrCodeParam.setPicHeight(allheight);
        return qrCodeParam;
    }

    /**
     * @param content 二维码的数据
     * @param left    向左偏移多少
     * @param width   二维码宽度
     * @param height  二维码高度
     * @return
     */
    public static QRCodeParam createImgBARCode(String content, int left, int width, int height) {
        QRCodeParam qrCodeParam = createImgBase(content, left, width, height, BarcodeFormat.CODE_128);
        return qrCodeParam;
    }

    public static QRCodeParam createImgAndBottomTextBARCode(String content, int left, int width, int height, BottomText bottomText) {
        QRCodeParam qrCodeParam = createImgBase(content, left, width, height, BarcodeFormat.CODE_128);
        qrCodeParam.setBottomText(bottomText);
        return qrCodeParam;
    }


    public static BottomText createBottomText(String text, int textSize, int marginTop) {
        BottomText bottomText = new BottomText();
        bottomText.setBottomContent(text);
        bottomText.setBottomTextSize(textSize);
        bottomText.setMarginTop(marginTop);

        return bottomText;
    }

    /**
     * @param content 二维码的数据
     * @param left    向左偏移多少
     * @param width   二维码宽度
     * @param height  二维码高度
     * @return
     */
    private static QRCodeParam createImgBase(String content, int left, int width, int height, BarcodeFormat barcodeFormat) {
        QRCodeParam qrCodeParam = new QRCodeParam();
        qrCodeParam.setPicWidth(left * 2 + width);
        qrCodeParam.setPicHeight(height);
        qrCodeParam.setQrWidth(width);
        qrCodeParam.setQrHeight(height);
        qrCodeParam.setQrContent(content);
        qrCodeParam.setBarcodeFormat(barcodeFormat);
        return qrCodeParam;
    }
}
