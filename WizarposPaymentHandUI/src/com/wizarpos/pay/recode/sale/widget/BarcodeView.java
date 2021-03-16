package com.wizarpos.pay.recode.sale.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wizarpos.log.util.StringUtil;
import com.motionpay.pay2.lite.R;
import com.wizarpos.recode.zxing.ZxingBarcodeManager;
import com.wizarpos.recode.zxing.ZxingQRcodeManager;
import com.wizarpos.recode.zxing.bean.QRCodeParam;

/**
 * 条形码view
 */
public class BarcodeView extends RelativeLayout {

    private Context context;
    private ImageView barCodeImg;
    private TextView barCodeText;

    public BarcodeView(Context context) {
        super(context);
        init(context);
    }

    public BarcodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BarcodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.widget_layout_barcode_view, null);
        barCodeImg = view.findViewById(R.id.iv_barcode_img);
        barCodeText = view.findViewById(R.id.tv_barcode_text);
        addView(view);
    }

    /**
     * 支付成功时显示
     *
     * @param transLog
     */
    public void setBarcodePayFor(String transLog) {
        float scale = this.getResources().getDisplayMetrics().density;
        int width = (int) (400 * scale + 0.5f);
        int high = (int) (60 * scale + 0.5f);
        setBarcodeTextAndImg(transLog, width, high);

    }


    private void setBarcodeTextAndImg(String text, int width, int hight) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        //移除不要的字符
        String content = transText(text);
        setBarCodeImgarCodeText(content);
        QRCodeParam qrCodeParam = QRCodeParam.createImgBARCode(content, 0, width, hight);
        Bitmap bitmap = ZxingQRcodeManager.createOnlyImg(qrCodeParam);
        setBarCodeImg(bitmap);
    }

    public void setBarCodeImgarCodeText(String text) {
        barCodeText.setText(text);
    }

    public void setBarCodeImg(Bitmap bitmap) {

        barCodeImg.setImageBitmap(bitmap);
    }


    private String transText(String text) {

        return text;
    }
}
