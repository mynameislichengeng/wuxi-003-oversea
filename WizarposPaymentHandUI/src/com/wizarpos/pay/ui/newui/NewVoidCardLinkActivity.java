package com.wizarpos.pay.ui.newui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.wizarpos.hspos.api.EnumCommand;
import com.wizarpos.hspos.api.EnumProgressCode;
import com.wizarpos.hspos.api.TransInfo;
import com.wizarpos.pay.cardlink.VoidProxy;
import com.wizarpos.pay.cardlink.CardLinkListener;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.SendTransInfo;
import com.wizarpos.pay.ui.widget.CommonToastUtil;
import com.motionpay.pay2.lite.R;

/**
 * 银行卡撤销
 * Created by wu on 16/3/24.
 */
public class NewVoidCardLinkActivity extends BaseViewActivity implements CardLinkListener {

    private VoidProxy voidProxy;

    private Dialog cardlinkDialog;


    private DailyDetailResp transDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("银行卡撤销");
        showTitleBack();
        progresser.showProgress();
//        setMainView(R.layout.activity_new_void_trans);
//        findViewById(R.id.btnVoidSale).setOnClickListener(this);

        transDetail = (DailyDetailResp) getIntent().getSerializableExtra(VoidTransActivity.EXTRA_TRANS_DETIAL);
        if (transDetail == null) {
            finish();
        }

        voidProxy = new VoidProxy(this);
        voidProxy.setCardLinkListener(this);

        if (Constants.SC_700_BANK_CARD_PAY.equals(transDetail.getTransType())) {
            voidProxy.voidSale();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btnVoidSale) {
            voidProxy.voidSale();
        }
    }

    @Override
    public void onProgress(EnumCommand cmd, final int progressCode, String progress, final boolean continueTrans) {
        hideCardlinkDialog();
        final int oldTrace = getOldTrace();
        if (progressCode == EnumProgressCode.InputOldTicket.getCode() && oldTrace > 0) {
            String msg = "待撤销凭证号: " + oldTrace + "\n"
                    + "请确认该凭证号是否与签购单一致";
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
            builder.content(progress);
            builder.cancelable(false);
            builder.positiveText("取消");
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                    materialDialog.dismiss();
                    voidProxy.getCardLinkPresenter().endTrans();
                    NewVoidCardLinkActivity.this.finish();
                }
            });
            if(continueTrans){
                builder.negativeText("确定");
                builder.onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        materialDialog.dismiss();
                        TransInfo transInfo = voidProxy.getTransInfo();
                        transInfo.setOldTrace(oldTrace);
                        voidProxy.getCardLinkPresenter().continueTrans();
                    }
                });
            }
            cardlinkDialog = builder.show();

        } else if (progressCode == EnumProgressCode.InputAuthCode.getCode() ||                                     //1
//                progressCode == EnumProgressCode.InputAdminPass.getCode() ||                              //2
//                progressCode == EnumProgressCode.InputExpiryDate.getCode() ||                             //3
//                progressCode == EnumProgressCode.InputOldBatchNumber.getCode() ||                         //4
                progressCode == EnumProgressCode.InputOldRRN.getCode() ||                                   //5
                progressCode == EnumProgressCode.InputOldTicket.getCode() ||                                //6
                progressCode == EnumProgressCode.InputOldTransDate.getCode()                                //7
                ) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                    .cancelable(false)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            endTrans();
                        }
                    })
                    .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    .input(progress, "", new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            if (TextUtils.isEmpty(input)) {
                                return;
                            }
                            String inputStr = String.valueOf(input);
                            TransInfo transInfo = voidProxy.getTransInfo();
                            if (progressCode == EnumProgressCode.InputAuthCode.getCode()) {                   //1
                                dialog.dismiss();
                                transInfo.setAuthCode(inputStr);
                                if (continueTrans) {
                                    voidProxy.getCardLinkPresenter().continueTrans();
                                }
                            } else if (progressCode == EnumProgressCode.InputAdminPass.getCode()) {           //2

                            } else if (progressCode == EnumProgressCode.InputExpiryDate.getCode()) {          //3
                                dialog.dismiss();
                                transInfo.setExpiryDate(inputStr);
                                if (continueTrans) {
                                    voidProxy.getCardLinkPresenter().continueTrans();
                                }
                            } else if (progressCode == EnumProgressCode.InputOldBatchNumber.getCode()) {      //4
                                int inputInt = checkInputInt(inputStr);
                                if (inputInt == -1) {
                                    return;
                                }
                                dialog.dismiss();
                                transInfo.setOldBatch(inputInt);
                                if (continueTrans) {
                                    voidProxy.getCardLinkPresenter().continueTrans();
                                }
                            } else if (progressCode == EnumProgressCode.InputOldRRN.getCode()) {              //5
                                dialog.dismiss();
                                transInfo.setOldRRN(inputStr);
                                if (continueTrans) {
                                    voidProxy.getCardLinkPresenter().continueTrans();
                                }
                            } else if (progressCode == EnumProgressCode.InputOldTicket.getCode()) {           //6
                                int inputInt = checkInputInt(inputStr);
                                if (inputInt == -1) {
                                    return;
                                }
                                dialog.dismiss();
                                transInfo.setOldTrace(inputInt);
                                if (continueTrans) {
                                    voidProxy.getCardLinkPresenter().continueTrans();
                                }
                            } else if (progressCode == EnumProgressCode.InputOldTransDate.getCode()) {        //7
                                dialog.dismiss();
                                transInfo.setOldTransDate(inputStr);
                                if (continueTrans) {
                                    voidProxy.getCardLinkPresenter().continueTrans();
                                }
                            }
                        }
                    });
            builder.show();
        } else {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
            builder.content(progress);
            builder.cancelable(false);
            builder.positiveText("取消");
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                    materialDialog.dismiss();
                    voidProxy.getCardLinkPresenter().endTrans();
                    NewVoidCardLinkActivity.this.finish();
                }
            });
            if(continueTrans){
                builder.negativeText("确定");
                builder.onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        materialDialog.dismiss();
                        voidProxy.getCardLinkPresenter().continueTrans();
                    }
                });
            }
            cardlinkDialog = builder.show();
        }
    }

    private int getOldTrace() {
        try{
            SendTransInfo sendTransInfo = JSON.parseObject(transDetail.getBank_info(), SendTransInfo.class);
            return sendTransInfo.getTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    private void endTrans() {
        voidProxy.endTrans();
    }

    @Override
    public void onTransSucceed(EnumCommand cmd) {
        setResult(RESULT_OK);
        this.finish();
    }

    @Override
    public void onTransFailed(EnumCommand cmd, String message) {
        getIntent().putExtra(VoidTransActivity.EXTRA_TRANS_VOID_FAILD, message);
        CommonToastUtil.showMsgBelow(NewVoidCardLinkActivity.this, CommonToastUtil.LEVEL_ERROR, message);
//        Toast.makeText(NewVoidCardLinkActivity.this, message, Toast.LENGTH_SHORT).showFromDialog();
        this.finish();
    }

    private void hideCardlinkDialog() {
        if (this.isFinishing()) {
            return;
        }
        if (cardlinkDialog != null && cardlinkDialog.isShowing()) {
            cardlinkDialog.dismiss();
        }
    }

    private void showContent() {
        hideCardlinkDialog();
        progresser.showContent();
    }

    private int checkInputInt(String inputStr) {
        try {
            return Integer.parseInt(inputStr);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(NewVoidCardLinkActivity.this, "输入有误", Toast.LENGTH_SHORT).show();
            return -1;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        voidProxy.removeCardListener();
    }
}
