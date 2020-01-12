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
import com.wizarpos.pay2.lite.R;
import com.wizarpos.recode.zxing.ZxingBarcodeManager;

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
        if (scale < 1.4) {
//            LayoutParams lp = (LayoutParams) barCodeText.getLayoutParams();
//
////            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(0, -15, 0, 0);
//            barCodeText.setLayoutParams(lp);
        } else {

        }
    }


    private void setBarcodeTextAndImg(String text, int width, int hight) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        //移除不要的字符
        String content = transText(text);
        setBarCodeImgarCodeText(content);
        Bitmap bitmap = ZxingBarcodeManager.creatBarcode(content, width, hight);
        setBarCodeImg(bitmap);
    }

    public void setBarCodeImgarCodeText(String text) {
        barCodeText.setText(text);
    }

    public void setBarCodeImg(Bitmap bitmap) {

        barCodeImg.setImageBitmap(bitmap);
    }


    private String transText(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (text.startsWith("P")) {
                return text.substring(1);
            }

        }
        return text;
    }
}
