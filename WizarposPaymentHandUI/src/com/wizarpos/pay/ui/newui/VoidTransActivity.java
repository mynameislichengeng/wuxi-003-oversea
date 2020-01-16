package com.wizarpos.pay.ui.newui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.model.PayTranRsp;
import com.wizarpos.pay.cashier.presenter.transaction.impl.TransactionCancelPresenter;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.ui.widget.CommonToastUtil;
import com.wizarpos.pay.view.util.Tools;
import com.wizarpos.pay2.lite.R;

/**
 * 撤销
 * Created by wu on 16/3/28.
 */
public class VoidTransActivity extends BaseViewActivity {

    public static final String EXTRA_TRANS_VOID_FAILD = "EXTRA_TRANS_VOID_FAILD";
    public static final String EXTRA_TRANS_DETIAL = "EXTRA_TRANS_DETIAL";
    public static final int REQUEST_VOID_CARD_LINK = 1001;
    private PayTranRsp payTranRsp;
    private String refundAmount;
    private DailyDetailResp transDetail;
    private TransactionCancelPresenter presenter;

    public static Intent getStartIntent(Context context, DailyDetailResp transDetailResp, String refundAmount) {
        Intent intent = new Intent(context, VoidTransActivity.class);
        intent.putExtra(EXTRA_TRANS_DETIAL, transDetailResp);
        intent.putExtra("refundAmount", refundAmount);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new TransactionCancelPresenter(this);
        transDetail = (DailyDetailResp) getIntent().getSerializableExtra(EXTRA_TRANS_DETIAL);
        refundAmount = getIntent().getStringExtra("refundAmount");
        if (transDetail == null) {
            finish();
        }

        if (Constants.SC_700_BANK_CARD_PAY.equals(transDetail.getTransType())) {
            Intent intent = new Intent(VoidTransActivity.this, NewVoidCardLinkActivity.class);
            intent.putExtra(VoidTransActivity.EXTRA_TRANS_DETIAL, transDetail);
            startActivityForResult(intent, REQUEST_VOID_CARD_LINK);
        } else {
            transactionCancle(false);
        }

        setTitleText(getResources().getString(R.string.issue_refund));
        showTitleBack();
    }

    private void transactionCancle(final boolean bankcardpay) {
        progresser.showProgress();
        presenter.getTransactionDetial(Tools.deleteMidTranLog(transDetail.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid)), bankcardpay, new BasePresenter.ResultListener() {
            @Override
            public void onSuccess(Response response) {
                payTranRsp = (PayTranRsp) response.result;
                presenter.cancel(payTranRsp, bankcardpay, refundAmount, new BasePresenter.ResultListener() {
                    @Override
                    public void onSuccess(Response response) {
                        if (response.getMsg().contains("success")) {
                            response.setMsg("success");
                        }
                        CommonToastUtil.showMsgBelow(VoidTransActivity.this, CommonToastUtil.LEVEL_SUCCESS, response.getMsg());
//                        Toast.makeText(VoidTransActivity.this,response.getMsg(),Toast.LENGTH_SHORT).showFromDialog();
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFaild(Response response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(VoidTransActivity.this);
                        builder.setMessage(response.getMsg());
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        //      CommonToastUtil.showMsgBelow(VoidTransActivity.this, CommonToastUtil.LEVEL_ERROR, response.getMsg());
//                        Toast.makeText(VoidTransActivity.this,response.getMsg(),Toast.LENGTH_SHORT).showFromDialog();

                    }
                });
            }

            @Override
            public void onFaild(Response response) {
                Toast.makeText(VoidTransActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VOID_CARD_LINK) {
            if (resultCode == RESULT_OK) {
                transactionCancle(true);
            } else {
//                String errorMsg = data.getStringExtra(EXTRA_TRANS_VOID_FAILD);
                this.finish();
            }
        }
    }
}
