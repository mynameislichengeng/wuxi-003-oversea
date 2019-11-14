package com.wizarpos.device.printer.html;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ui.progress.ProgressLayout;
import com.wizarpos.device.printer.html.model.BitmapPrintModel;
import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.pay.common.utils.TokenGenerater;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 以图片方式打印
 * Created by Song on 2017/10/26.
 */
public class WebPrintActivity extends Activity {
    private WebView webView = null;
    private File imageFile = null;
    private Handler handler = new Handler();
    private String printStr = "";
    private HTMLPrintModel printModel;
    private ProgressLayout progress;
    private int printType = -1;//0 HTMLPrintModel, 1 String

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("YS", "printActivity started");
        setContentView(R.layout.activity_web_print);
//        //设置Theme.Dialog View高度   在setContentView(id);之后添加以下代码
//        WindowManager m = getWindowManager();
//        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
//        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
////        p.width = (int) (d.getWidth() * 0.7); // 宽度设置为屏幕的0.7
//        p.width = 366;
//        getWindow().setAttributes(p);
        if (!getData()) {
            finish();
            return;
        }
        printModel = (HTMLPrintModel) getIntent().getSerializableExtra("printModel");
        progress = (ProgressLayout) findViewById(R.id.progress);
        progress.showProgress();
        webView = (WebView) findViewById(R.id.wvPrint);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setDrawingCacheEnabled(false);

        webView.getLayoutParams().width = 386;

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.clearCache(true);
        imageFile = new File(Environment.getExternalStorageDirectory(), "webPrint" + TokenGenerater.newToken() + ".png");
        initWebView();
    }

    private boolean getData() {
        if (getIntent().hasExtra("printType")) {
            printType = getIntent().getIntExtra("printType", 0);
            if (getIntent().hasExtra("printStr")) {
                printStr = getIntent().getStringExtra("printStr");
            }
            if (getIntent().hasExtra("printModel")) {
                printModel = (HTMLPrintModel) getIntent().getSerializableExtra("printModel");
            }
        }
        return printType > -1;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static Intent getStartIntent(Context context, HTMLPrintModel printModel) {
        Intent intent = new Intent(context, WebPrintActivity.class);
        intent.putExtra("printModel", printModel);
        intent.putExtra("printType", 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent getStartIntent(Context context, String printStr) {
        Intent intent = new Intent(context, WebPrintActivity.class);
        intent.putExtra("printStr", printStr);
        intent.putExtra("printType", 1);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private void printImage() {
        savePicture();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bm = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    Log.i("YS", "bitmap printing");
                    //进Bitmap打印队列
                    BitmapPrinterHelper.getInstance().print(new BitmapPrintModel(bm, imageFile));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Log.i("YS", "tryFinish");
        finish();
    }

    private void initWebView() {
        try {
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true); // 启用JS脚本
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(final WebView view, String url) {
                    System.out.println("page finished....");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            printImage();
                        }
                    }, 800L);
                }

             /*   @Override
                public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(WebPrintActivity.this);
                    builder.setMessage("存在风险，是否继续");
                    builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.proceed();
                        }
                    });
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.cancel();
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                }*/

            });
            String html = printType == 1 ? printStr : ToHTMLUtil.toHtml(printModel);
            try {
                webView.removeAllViews();
                webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void savePicture() {
        webView.refreshDrawableState();
        Picture picture = webView.capturePicture();
        // View picture = this.getWindow().getDecorView();
        int width = picture.getWidth();
        int height = picture.getHeight();
        System.out.println("width: " + width + ", height: " + height);
        if (width > 0 && height > 0) {
            // 创建指定高宽的Bitmap对象
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//            bitmap = zoomImage(bitmap, 366);
            // 创建Canvas,并以bitmap为绘制目标
            Canvas canvas = new Canvas(bitmap);
            // 将WebView影像绘制在Canvas上
            picture.draw(canvas);
            if (imageFile.exists()) {
                imageFile.delete();
            }
            try {
                FileOutputStream fos = new FileOutputStream(imageFile);
                // 压缩bitmap到输出流中
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                Log.i("YS", "bitmap saved:" + imageFile.getAbsoluteFile());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                Log.e("生成二维码图片", e.getMessage());
            } finally {
                webView.clearHistory();
//                finish();
            }
        } else {
            // 如果图片没有存下来，等100ms再次调用
            handler.postDelayed(new Runnable() {
                public void run() {
                    savePicture();
                }
            }, 100L);
        }
    }

    private Bitmap zoomImage(Bitmap bgimage, double newWidth) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleWidth);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("YS", "webPrint finished.");
        WebPrintHelper.isWebPrinting = false;
    }
}
