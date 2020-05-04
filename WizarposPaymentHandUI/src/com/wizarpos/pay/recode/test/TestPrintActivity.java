package com.wizarpos.pay.recode.test;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.print.PrintHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nexgo.oaf.apiv3.device.printer.AlignEnum;
import com.nexgo.oaf.apiv3.device.printer.OnPrintListener;
import com.pax.poslink.peripheries.POSLinkPrinter;
import com.pax.poslink.peripheries.ProcessResult;
import com.pos.device.printer.PrintCanvas;
import com.pos.device.printer.PrintTask;
import com.pos.device.printer.Printer;
import com.pos.device.printer.PrinterCallback;
import com.wizarpos.atool.log.Logger;
import com.wizarpos.device.printer.PrinterHelper;
import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay2.lite.R;
import com.wizarpos.recode.print.constants.HtmlRemarkConstans;
import com.wizarpos.recode.print.devicesdk.amp.AMPPrintManager;
import com.wizarpos.recode.print.service.impl.PrintDeviceForN3N5HandleImpl;

public class TestPrintActivity extends Activity implements View.OnClickListener {

    private final String TAG = TestPrintActivity.class.getSimpleName();

    private Button btn;
    private EditText ed_zxing_barcode_test;

    PrintTask printTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn = (Button) findViewById(R.id.btn_zxing_test);
        btn.setOnClickListener(this);
        ed_zxing_barcode_test = (EditText) findViewById(R.id.ed_zxing_barcode_test);

        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_AMP8) {
            AMPPrintManager.getInstance().init(this);
        }
    }

    @Override
    public void onClick(View v) {
        String str = ed_zxing_barcode_test.getText().toString();

//        if (TextUtils.isEmpty(str)) {
//            Toast.makeText(this, "请输入打印内容", Toast.LENGTH_SHORT).show();
//            return;
//        }
        str += "<end/>";

        PrinterHelper.print(str);


//        printA920(str);

//        printN3N5();
//        printAMP(str);
    }

    private void printA920(String str) {
        POSLinkPrinter.getInstance(this).print(str, POSLinkPrinter.CutMode.DO_NOT_CUT, new POSLinkPrinter.PrintListener() {
            @Override
            public void onSuccess() {
                log("A920打印完成onSuccess()");

            }

            @Override
            public void onError(ProcessResult processResult) {
                log("A920打印完成onError()");
            }
        });
    }


    private void printAMP(final String str) {
        try {

            printTask = new PrintTask();
            PrintCanvas printCanvas = new PrintCanvas();
            Paint paint = new Paint();
            paint.setTextSize(26);
            printCanvas.drawText(str, paint);

            printTask.setPrintCanvas(printCanvas);

            printTask.addFeedPaper(100);

            printTask.setGray(130);

            Printer.getInstance().startPrint(printTask, printerCallback);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private PrinterCallback printerCallback = new PrinterCallback() {

        @Override
        public void onResult(int arg0, PrintTask arg1) {
            Log.d("tag", "Printer Callback onResult");
            String str = "Printer Callback onResult " + arg0 + "   Gray: " + arg1.getGray();
            Log.d("tag", str);
            toastOnUI(str);
        }
    };

    private void toastOnUI(final String str) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TestPrintActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void printN3N5() {
        PrintDeviceForN3N5HandleImpl printDeviceForN3N5Handle = new PrintDeviceForN3N5HandleImpl();
//        printDeviceForN3N5Handle.getPrinterN3N5().appendPrnStr("12", 24, AlignEnum.LEFT, false);

        printDeviceForN3N5Handle.getPrinterN3N5().appendPrnStr("123", "rigth", 24, false);
        printDeviceForN3N5Handle.keywordTrigger(HtmlRemarkConstans.END.getValue());
    }

    private void log(String msg) {
        Log.d("tag", TAG + ">>" + msg);
    }
}
