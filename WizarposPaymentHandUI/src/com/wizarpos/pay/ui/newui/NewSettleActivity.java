package com.wizarpos.pay.ui.newui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wizarpos.hspos.api.EnumCommand;
import com.wizarpos.pay.cardlink.CardLinkListener;
import com.wizarpos.pay.cardlink.SettleAndLoginProxy;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.ui.widget.CommonToastUtil;
import com.motionpay.pay2.lite.R;

/**
 * Created by wu on 16/3/28.
 */
public class NewSettleActivity extends BaseViewActivity implements CardLinkListener {

    private SettleAndLoginProxy settleProxy;
    private LinearLayout llMsg;
    private TextView tvToastContent;
    private ImageView ivToastIcon;
    private final static int LEVEL_INFO = 0;
    private final static int LEVEL_SUCCESS = 1;
    private final static int LEVEL_ERROR = 2;

    public static Intent getStartIntnet(Context context){
        return new Intent(context, NewSettleActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("日结/签到");
        setMainView(R.layout.activity_download_key);
        settleProxy = new SettleAndLoginProxy(this);
        settleProxy.setCardLinkListener(this);
        initViews();
//        progresser.showProgress();
        signOn();
    }

    private void initViews() {
        llMsg = (LinearLayout) findViewById(R.id.llMsg);
        ivToastIcon = (ImageView) findViewById(R.id.ivToastIcon);
        tvToastContent = (TextView) findViewById(R.id.tvToastContent);

        ImageView imageView = (ImageView) findViewById(R.id.ivGif);
        Glide.with(this).load(R.drawable.pic_bg).crossFade().into(imageView);
//        GifView gifView = (GifView) findViewById(R.id.gifView);
//        gifView.setGifImage(R.drawable.pic_bg);
//        gifView.setShowDimension(DensityUtil.getScreenWidth(this), DensityUtil.dip2px(this, 200));
//        gifView.setGifImageType(GifView.GifImageType.WAIT_FINISH);
    }

    private void signOn() {
        settleProxy.login();
    }

    @Override
    protected void onTitleBackClikced() {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onProgress(EnumCommand cmd, int progressCode, final String progress, boolean continueTrans) {
        showMsg(progress,LEVEL_INFO);
//        progresser.showProgress(progress);
//        AlertDialogWrapper.Builder builder = new AlertDialogWrapper.Builder(this)
//                .setMessage(progress)
//                .setCancelable(false)
//                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (NewSettingActivity.this.isFinishing()) {
//                            return;
//                        }
//                        dialog.dismiss();
//                        progresser.showContent();
//                        cardLinkProxy.getCardLinkPresenter().endTrans();
//                    }
//                });
//        if (continueTrans) {
//            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    if (NewSettingActivity.this.isFinishing()) {
//                        return;
//                    }
//                    dialog.dismiss();
//                    cardLinkProxy.getCardLinkPresenter().continueTrans();
//                }
//            });
//        }
//        if(this.isFinishing()){
//            return;
//        }
    }

    @Override
    public void onTransSucceed(EnumCommand cmd) {
        showMsg("交易成功!", LEVEL_SUCCESS);
        CommonToastUtil.showMsgBelow(this, CommonToastUtil.LEVEL_SUCCESS, "交易成功!");
//            Toast.makeText(this, "交易成功!", Toast.LENGTH_SHORT).showFromDialog();
        settleProxy.endTrans();
        this.finish();
    }

    @Override
    public void onTransFailed(EnumCommand cmd, String message) {
        showMsg("交易失败!\n" + message, LEVEL_ERROR);
        CommonToastUtil.showMsgBelow(this, CommonToastUtil.LEVEL_ERROR, "交易失败!\n" + message);
        settleProxy.endTrans();
        this.finish();
    }

    private void showMsg(String msg, int level) {
        tvToastContent.setText(msg);
        switch (level) {
            case LEVEL_INFO:
                ivToastIcon.setImageResource(R.drawable.ic_info);
                tvToastContent.setTextColor(0xff666666);
                break;
            case LEVEL_SUCCESS:
                ivToastIcon.setImageResource(R.drawable.ic_succese);
                tvToastContent.setTextColor(0xff6EA626);
                break;
            case LEVEL_ERROR:
                ivToastIcon.setImageResource(R.drawable.ic_cancel);
                tvToastContent.setTextColor(0xffDD0B0B);
                break;
            default:
                break;
        }
        llMsg.invalidate();
    }
}
