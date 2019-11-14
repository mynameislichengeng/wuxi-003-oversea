package com.wizarpos.pay.setting.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.exception.DbException;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.device.printer.PrintServiceController;
import com.wizarpos.device.printer.PrinterManager;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.app.Pay2Application;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.device.printer.MidFilterPrinterBuilder;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay2.lite.R;

import java.util.Date;

/**
 * Created by wu on 15/11/9.
 */
public class DebugActivity extends BaseViewActivity {

    private PrintServiceController printer;
    private MidFilterPrinterBuilder printerSync;

    private Button btnPrintNeverEnding;
    private int step;
    private boolean goon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainView(R.layout.activity_debug);
        setTitleText("Debug");
        findViewById(R.id.btnClearApp).setOnClickListener(this);
        findViewById(R.id.btnPrintSync).setOnClickListener(this);
        findViewById(R.id.btnPrint).setOnClickListener(this);
        findViewById(R.id.btnICStart).setOnClickListener(this);
        findViewById(R.id.btnICEND).setOnClickListener(this);
        btnPrintNeverEnding = (Button) findViewById(R.id.btnPrintNeverEnding);
        btnPrintNeverEnding.setOnClickListener(this);
        findViewById(R.id.btnPrintEnd).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnClearApp:
                clearApp();
                break;
            case R.id.btnPrintSync:
                testPrintSync();
                break;
            case R.id.btnPrint:
                testPrint();
                break;
            case R.id.btnPrintNeverEnding:
                printNeverEndding();
                break;
            case R.id.btnPrintEnd:
                printEnd();
                break;
            case R.id.btnICStart:
                startIC();
                break;
            case R.id.btnICEND:
                endIC();
                break;

        }

    }

    private void startIC() {
        if(DeviceManager.getInstance().isSupportBankCard()){
            DeviceManager.getInstance().startSmartCardListener(new BasePresenter.ResultListener() {
                @Override
                public void onSuccess(Response response) {
                    LogEx.d("IC", response.msg);
                }

                @Override
                public void onFaild(Response response) {
                    LogEx.d("IC", response.msg);
                }
            });
        }
    }

    private void endIC(){
        if(DeviceManager.getInstance().isSupportBankCard()){
            DeviceManager.getInstance().closeSmartCard();
        }
    }

    private void clearApp() {
        try {
            Pay2Application.getInstance().getDbController().dropDb();
        } catch (DbException e) {
            e.printStackTrace();
        }
        System.exit(0);
        throw new RuntimeException("BOOM!");
    }


    public void testPrintSync() {
        if (printerSync == null) {
            printerSync = new MidFilterPrinterBuilder();
        }
        printerSync.print(getPrintStr());
    }

    public void testPrint() {
        if (printer == null) {
            PrinterManager.getInstance().setPrinter(new MidFilterPrinterBuilder());
            printer = new PrintServiceController(this);
        }
        printer.print(getPrintStr());
    }

    String toPrintStr = null;

//    private String getPrintStr() {
//        if (TextUtils.isEmpty(toPrintStr)) {
//            Q1PrintBuilder builder = new Q1PrintBuilder();
//            StringBuilder sb = new StringBuilder();
//            sb.append(builder.branch());
//            sb.append(builder.center("Wizarpos"));
//            sb.append(builder.center("Wizarpos"));
//            sb.append(builder.center("Wizarpos"));
//            sb.append(builder.center("Wizarpos"));
//            sb.append(builder.branch());
//            toPrintStr = sb.toString();
//        }
//        return toPrintStr;
//    }

    private String getPrintStr() {
        Q1PrintBuilder builder = new Q1PrintBuilder();
        String printString = "";
        printString += builder.center(builder.bold("其它支付"));
        printString += builder.branch();
        printString += "慧商户号：" + AppConfigHelper.getConfig(AppConfigDef.mid, "") + builder.br();
        printString += "商户名称：" + AppConfigHelper.getConfig(AppConfigDef.merchantName, "") + builder.br();
        printString += "终端号：" + AppConfigHelper.getConfig(AppConfigDef.terminalId, "") + builder.br();
        printString += "流水号：" + "P1234567890" + builder.br();
        printString += "收银：" + 1.00 + "元" + builder.br();
        printString += "实收：" + 1.00 + "元" + builder.br();
        printString += "时间：" + DateUtil.format(new Date(), DateUtil.P2) + builder.br();
        printString += builder.branch();
        printString += builder.endPrint();
        return printString;
    }

    public void printNeverEndding() {
        for (int i = 0; i < 200; i++) {
            testPrint();
        }
    }

//    public void printNeverEndding() {
//        if (goon) {
//            return;
//        }
//        goon = true;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (goon) {
//                    testPrint();
//                    step++;
//                    DebugActivity.this.runOnUiThread(new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            btnPrintNeverEnding.setText("打印测试(一直打印)" + "(" + step + ")");
//                        }
//                    }));
//                    try {
//                        Thread.sleep(2000);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//    }

    public void printEnd() {
        goon = false;
    }
}
