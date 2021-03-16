package com.wizarpos.pay.ui.newui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lc.baseui.widget.swith.CommonSwitchButton;
import com.lc.baseui.widget.swith.callback.OnSwitchChangeListener;
import com.wizarpos.pay.common.base.BaseLogicAdapter;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.base.ViewHolder;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.manage.activity.InputPassWordActivity;
import com.wizarpos.pay.manage.activity.ModifyPasswordActivity;
import com.wizarpos.pay.setting.activity.TipParameterSettingActivity;
import com.wizarpos.pay.ui.newui.util.ItemDataUtils;
import com.wizarpos.pay.view.ArrayItem;
import com.motionpay.pay2.lite.R;
import com.wizarpos.recode.print.constants.SettingPrintModeEnum;
import com.wizarpos.recode.print.data.SettingPrinterModeManager;
import com.wizarpos.recode.receipt.constants.ReceiptStatusEnum;
import com.wizarpos.recode.receipt.service.ReceiptDataManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewSettingActivity extends BaseViewActivity {
    @Bind(R.id.llSettingPrint)
    LinearLayout llSettingPrint;//打印设置
    @Bind(R.id.llAuthCode)
    LinearLayout llAuthCode;//设置验证码
    @Bind(R.id.llKeyDownload)
    LinearLayout llKeyDownload;//密钥下载
    @Bind(R.id.llSettingSignAgain)
    LinearLayout llSettingSignAgain;//重新签到
    @Bind(R.id.llPasswordManager)
    LinearLayout llPasswordManager;//密码管理
    @Bind(R.id.llAbout)
    LinearLayout llAbout;//关于
    @Bind(R.id.llCancel)
    LinearLayout llCancel;//交易撤销

    @Bind(R.id.ll_receipt_barcode)
    RelativeLayout ll_receipt_barcode;//条形码根布局

    @Bind(R.id.ll_receipt_qrcode)
    RelativeLayout ll_receipt_qrcode;//条形码根布局

    private CommonSwitchButton barCodeSwitchButton;//switch
    private CommonSwitchButton qrCodeSwitchButton;

    private TextView tvNumber;

    private int TIP_SETTING_CODE = 102;


    BaseLogicAdapter<ArrayItem> printItemAdapter = null;
    MaterialDialog printDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {//初始化数据
    }

    private void initView() {
        setMainView(R.layout.activity_setting_layout);
        showTitleBack();
        setTitleText(getResources().getString(R.string.setting));
        tvNumber = (TextView) findViewById(R.id.tvNumber);
        LinearLayout llChangeCode = (LinearLayout) findViewById(R.id.llChangeCode);

        String number = SettingPrinterModeManager.getLayoutShowPrintMode();
        tvNumber.setText(number);

        ButterKnife.bind(this);
        llChangeCode.setVisibility(View.GONE);

        //条形码按钮
        settingBarcodeSwitchOnClick();
        //二维码
        settingQRCodeSwitchOnClick();
    }

    @Override
    protected void onTitleBackClikced() {
        startActivity(NewMainActivity.getStartIntent(this));
        super.onTitleBackClikced();
    }

    @Override
    public void onBackPressed() {
        startActivity(NewMainActivity.getStartIntent(this));
        super.onBackPressed();
    }

    @OnClick({R.id.llSettingPrint, R.id.llAuthCode, R.id.llKeyDownload, R.id.llSettingSignAgain, R.id.llPasswordManager, R.id.llAbout, R.id.llCancel, R.id.llTipSetting, R.id.llChangeCode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llSettingPrint://打印设置
                if (printDialog == null || (!printDialog.isShowing() && printDialog.isCancelled())) {
                    showPrintItems();
                }
                break;
            case R.id.llAuthCode://验证码
                startActivity(NewAuthCodeSettingActivity.getStartIntent(this));
                break;
            case R.id.llKeyDownload://密钥下载
                doDownloadKeyAction();
                break;
            case R.id.llSettingSignAgain://再次签到
                doSignOnAction();
                break;
            case R.id.llPasswordManager://收银员管理
                startActivity(new Intent(NewSettingActivity.this, NewCashierManagerActivity.class));
//                startActivity(new Intent(NewSettingActivity.this, NewPasswordSettingActivity.class));
                break;
            case R.id.llAbout://关于
                startActivity(new Intent(NewSettingActivity.this, NewAboutActivity.class));
                break;
            case R.id.llCancel://交易撤销
                voidTrans();
                break;
            case R.id.llTipSetting:
                startActivityForResult(new Intent(NewSettingActivity.this, InputPassWordActivity.class), TIP_SETTING_CODE);
                break;
            case R.id.llChangeCode:
                startActivity(new Intent(NewSettingActivity.this, ModifyPasswordActivity.class));
                break;

        }
    }

    /**
     * 设置barcode的switch按钮
     */
    private void settingBarcodeSwitchOnClick() {
//        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_PAX_A920) {
//            ll_receipt_barcode.setVisibility(View.GONE);
//            return;
//        }

        barCodeSwitchButton = ll_receipt_barcode.findViewById(R.id.swbtn_common);


        if (ReceiptDataManager.isOpenBarcodeStatus()) {
            barCodeSwitchButton.setViewSwitchCheckedStatus(true);
        } else {
            barCodeSwitchButton.setViewSwitchCheckedStatus(false);
        }

        barCodeSwitchButton.setOnSwitchChangeListener(new OnSwitchChangeListener() {
            @Override
            public void onSwitchCloseClick() {
                ReceiptDataManager.settingBarcodeStatus(ReceiptStatusEnum.CLOSE);
            }

            @Override
            public void onSwitchOpenClick() {
                ReceiptDataManager.settingBarcodeStatus(ReceiptStatusEnum.OPEN);
            }
        });
    }

    private void settingQRCodeSwitchOnClick() {
//        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_PAX_A920) {
//            ll_receipt_qrcode.setVisibility(View.GONE);
//            return;
//        }

        qrCodeSwitchButton = ll_receipt_qrcode.findViewById(R.id.swbtn_common);
        if (ReceiptDataManager.isOpenQRCodeStatus()) {
            qrCodeSwitchButton.setViewSwitchCheckedStatus(true);
        } else {
            qrCodeSwitchButton.setViewSwitchCheckedStatus(false);
        }
        qrCodeSwitchButton.setOnSwitchChangeListener(new OnSwitchChangeListener() {
            @Override
            public void onSwitchCloseClick() {
                ReceiptDataManager.settingQRCodeStatus(ReceiptStatusEnum.CLOSE);
            }

            @Override
            public void onSwitchOpenClick() {
                ReceiptDataManager.settingQRCodeStatus(ReceiptStatusEnum.OPEN);
            }
        });
    }


    private void showPrintItems() {
        final List<ArrayItem> printItems = ItemDataUtils.getPrintNums();
        printItemAdapter = new BaseLogicAdapter<ArrayItem>(this, printItems, R.layout.item_print) {

            @Override
            public void convert(ViewHolder helper, final ArrayItem item, final int position) {
                //当前显示的指
                final String showCurrentPrintModeValue = item.getShowValue();
                helper.setText(R.id.tvPrintItem, showCurrentPrintModeValue);
                final ImageView ivPrintItem = helper.getView(R.id.ivPrintItem);

                //当前
                String num = SettingPrinterModeManager.getLayoutShowPrintMode();
                boolean isFlag = showCurrentPrintModeValue.equals(num);
                //圆按钮
                ivPrintItem.setPressed(isFlag);
                //蓝色线
                helper.getView(R.id.ivPrintItemDivider).setPressed(isFlag);
                helper.getView(R.id.llPrintItem).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvNumber.setText(showCurrentPrintModeValue);
                        SettingPrinterModeManager.settingCachePrintModeFromShowValue(showCurrentPrintModeValue);
                        if (null != printItemAdapter) {
                            printItemAdapter.notifyDataSetChanged();
                        }
                        if (null != printDialog && printDialog.isShowing()) {
                            printDialog.dismiss();
                        }
                    }
                });
            }

        };
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_item_print, null);
        dialogView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        ListView lvPrintItem = (ListView) dialogView.findViewById(R.id.lvPrintItem);
        lvPrintItem.setAdapter(printItemAdapter);
        MaterialDialog.Builder printDialogBuilder = new MaterialDialog.Builder(this);
        printDialog = printDialogBuilder.customView(dialogView, false).build();
        printDialog.show();
    }

    private void voidTrans() {
//        startActivity(NewVoidTransActivity.getStartIntent(this, ));
    }

    private void doSignOnAction() {
        startActivity(NewSettleActivity.getStartIntnet(this));
    }

    private void doDownloadKeyAction() {
        startActivity(NewDownloadKeyActivity.getStartIntnet(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TIP_SETTING_CODE && resultCode == RESULT_OK) {
            startActivity(new Intent(NewSettingActivity.this, TipParameterSettingActivity.class));
            finish();
        }
    }


}
