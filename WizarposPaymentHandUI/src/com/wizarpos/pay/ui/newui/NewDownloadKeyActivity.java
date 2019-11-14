package com.wizarpos.pay.ui.newui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.hspos.api.EnumCommand;
import com.wizarpos.pay.cardlink.CardLinkListener;
import com.wizarpos.pay.cardlink.DownloadKeyProxy;
import com.wizarpos.pay.cardlink.model.CardLinkConfigBean;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.ui.widget.CommonToastUtil;
import com.wizarpos.pay.view.util.DialogHelper2;
import com.wizarpos.pay2.lite.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wu on 16/3/28.
 */
public class NewDownloadKeyActivity extends BaseViewActivity implements CardLinkListener {

    private DownloadKeyProxy downloadKeyProxy;
    private LinearLayout llMsg;
    private ImageView ivToastIcon;
    private TextView tvToastContent;
    private final static int LEVEL_INFO = 0;
    private final static int LEVEL_SUCCESS = 1;
    private final static int LEVEL_ERROR = 2;

    private boolean canEndTrans = false;

    public static Intent getStartIntnet(Context context) {
        return new Intent(context, NewDownloadKeyActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("密钥下载");
        showTitleBack();
        initViews();
        downloadKeyProxy = new DownloadKeyProxy(this);
        downloadKeyProxy.setCardLinkListener(this);
        String paramsFlag = AppConfigHelper.getConfig(AppConfigDef.CARDLINK_AUTHCODE);
        if (TextUtils.isEmpty(paramsFlag)) {
            download();
        } else {
            DialogHelper2.showChooseDialog(this, "当前已有收单密钥，是否重新下载？", new DialogHelper2.DialogChoiseListener() {
                @Override
                public void onOK() {
                    download();
                }

                @Override
                public void onNo() {
                    finish();
                }
            });

        }
//        progresser.showProgress();
    }

    private void initViews() {
        setMainView(R.layout.activity_download_key);
//        wvGif = (WebView) findViewById(R.id.wvGif);
//        ivGif = (GifImageView) findViewById(R.id.ivGif);
        llMsg = (LinearLayout) findViewById(R.id.llMsg);
        ivToastIcon = (ImageView) findViewById(R.id.ivToastIcon);
        tvToastContent = (TextView) findViewById(R.id.tvToastContent);

//        GifView gifView = (GifView) findViewById(R.id.gifView);
//        gifView.setGifImage(R.drawable.pic_bg);
//        gifView.setShowDimension(DensityUtil.getScreenWidth(this), DensityUtil.dip2px(this, 200));
//        gifView.setGifImageType(GifView.GifImageType.WAIT_FINISH);
//        wvGif.loadDataWithBaseURL(null,"<HTML><meta name='viewport' content='width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable = [yes | no],target-densitydpi = [dpi_value | device-dpi | high-dpi | medium-dpi | low-dpi]'/><body bgcolor='#ffffff'><div align=left><IMG src='file:///android_asset/pic_bg.gif'/></div></body></html>", "text/html", "UTF-8",null);
//        wvGif.setEnabled(false);

        ImageView imageView = (ImageView) findViewById(R.id.ivGif);
        Glide.with(this).load(R.drawable.pic_bg).crossFade().into(imageView);
    }

    @Override
    protected void onTitleBackClikced() {
        super.onTitleBackClikced();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onProgress(EnumCommand cmd, int progressCode, final String progress, boolean continueTrans) {
        String msg = "";
        if (cmd == EnumCommand.DownloadAID) {
            msg = "下载AID参数\n";
        } else if (cmd == EnumCommand.DownloadCAPK) {
            msg = "下载公钥参数\n";
        } else if (cmd == EnumCommand.InitKey) {
            msg = "下载主密钥\n";
        }
        msg += progress;
        showMsg(msg, LEVEL_INFO);
//        progresser.showProgress(msg);
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
        canEndTrans = false;
        showMsg("下载成功!", LEVEL_SUCCESS);
//        Toast.makeText(this, "下载成功!", Toast.LENGTH_SHORT).show();
        downloadKeyProxy.endTrans();
        CommonToastUtil.showMsgBelow(NewDownloadKeyActivity.this, CommonToastUtil.LEVEL_SUCCESS, "密钥下载成功");
        NewDownloadKeyActivity.this.finish();
//        DialogHelper2.showDialog(NewDownloadKeyActivity.this,"密钥下载成功",
//        new DialogHelper2.DialogListener() {
//            @Override
//            public void onOK() {
//                  NewDownloadKeyActivity.this.finish();
//            }
//        });
    }

    @Override
    public void onTransFailed(EnumCommand cmd, String message) {
        canEndTrans = false;
        showMsg("下载失败!\n" + message, LEVEL_ERROR);
//        Toast.makeText(this, "下载失败!\n" + message, Toast.LENGTH_SHORT).show();
        CommonToastUtil.showMsgBelow(NewDownloadKeyActivity.this, CommonToastUtil.LEVEL_ERROR, "下载失败！");
//        downloadKeyProxy.endTrans();
//        this.finish();
    }

    @Override
    protected void onDestroy() {
        endTrans();
        super.onDestroy();
    }

    private void endTrans() {
        if (canEndTrans) {
            downloadKeyProxy.endTrans();
        }
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

    //下载参数
    private void download() {
//        progresser.showProgress();
        Map<String, Object> params = new HashMap<String, Object>();
        NetRequest.getInstance().addRequest(Constants.SC_1000_CARD_LINK, params, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                progresser.showContent();
                CardLinkConfigBean cardLinkConfigBean = JSONObject.parseObject(response.getResult().toString(), CardLinkConfigBean.class);
                AppConfigHelper.setConfig(AppConfigDef.CARDLINK_AUTHCODE, cardLinkConfigBean.getAuthCode());
                AppConfigHelper.setConfig(AppConfigDef.CARDLINK_MECHANTID, cardLinkConfigBean.getMechantId());
                AppConfigHelper.setConfig(AppConfigDef.CARDLINK_TERMINALID, cardLinkConfigBean.getTerminalId());
                AppConfigHelper.setConfig(AppConfigDef.CARDLINK_SERVERIP, cardLinkConfigBean.getServerIP());
                AppConfigHelper.setConfig(AppConfigDef.CARDLINK_SERVERPORT, cardLinkConfigBean.getServerPort());
                AppConfigHelper.setConfig(AppConfigDef.CARDLINK_TPDU, cardLinkConfigBean.getTpdu());
                if (downloadKeyProxy != null) {
                    canEndTrans = true;
                    downloadKeyProxy.downloadKey();
                }
//                AppMsg appMsg = AppMsg.makeText(NewCardLinkSettingActivity.this, "配置发生变更后请重新下载收单秘钥并签到", AppMsg.STYLE_INFO);
//                appMsg.setParent(R.id.rlMain);
//                appMsg.show();
            }

            @Override
            public void onFaild(Response response) {
                progresser.showContent();
                showMsg(response.getMsg(), LEVEL_ERROR);
                CommonToastUtil.showMsgBelow(NewDownloadKeyActivity.this, CommonToastUtil.LEVEL_ERROR, response.getMsg());
//                Toast.makeText(NewDownloadKeyActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
//                AppMsg appMsg = AppMsg.makeText(NewCardLinkSettingActivity.this, "配置发生变更后请重新下载收单秘钥并签到", AppMsg.STYLE_INFO);
//                appMsg.setParent(R.id.rlMain);
//                appMsg.show();
//                updateView();
            }
        });
    }
}
