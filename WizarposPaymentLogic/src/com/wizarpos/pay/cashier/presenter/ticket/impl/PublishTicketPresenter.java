package com.wizarpos.pay.cashier.presenter.ticket.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.SparseArray;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.cashier.model.TicketDef;
import com.wizarpos.pay.cashier.presenter.TransactionTemsController;
import com.wizarpos.pay.cashier.presenter.transaction.impl.TransactionImpl;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.common.ticketdisplay.DisplayTicektBean;
import com.wizarpos.pay.common.ticketdisplay.DisplayTicketActivity;
import com.wizarpos.pay.common.utils.FileUtil;
import com.wizarpos.pay.db.AppStateDef;
import com.wizarpos.pay.db.AppStateManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PublishTicketPresenter extends TransactionImpl {
	// private String customerOpenId = null;
	// private Long cid;

	int ticketCount = 0;
	public String[] kxitem1 = { "", "", "" };
	public String[] kxitem2 = { "", "", "" };
	public String[] kxitem3 = { "", "", "" };
	public String[] kxitem4 = { "", "", "" };
	public int kxamount = 0;

	private TicketPublisher commonticketPublisher;

	private WemengTicketPublisher wemengTicketPublisher;

	private SparseArray<Bitmap> weChatQRCodes = new SparseArray<Bitmap>();
	
	public PublishTicketPresenter(Context context) {
		super(context);
		commonticketPublisher = new TicketPublisher(context);
	}

	public void handleIntent(Intent intent) {
		super.handleIntent(intent);
		tranLogId = intent.getStringExtra("tranLogId");
		giftPoints = intent.getStringExtra("giftPoints");
		// customerOpenId = intent.getStringExtra("customerOpenId");
		// cid = intent.getLongExtra("cid", 0);

		// if (functionNo == BANK_CARD) { // 银行卡消费
		// cardNo = null;
		// }
	}

	@Override
	@Deprecated
	public void trans(ResultListener listener) {

	}

	@Override
	@Deprecated
	public boolean revokeTrans(ResultListener listener) {
		return false;
	}

	/**
	 * 获取可发行的券列表
	 * 
	 * @param resultListener
	 *            onSuccess 返回 List<TicketDef> publishableTicketDefs
	 */
	public void getPublishableTicket(ResultListener resultListener) {
		if (Constants.TRUE.equals(AppStateManager.getState(
				AppStateDef.isOffline, Constants.FALSE))) {
			return;
		}
		commonticketPublisher.getPublishableTicket(tranLogId, cardType, showFlag,
				resultListener);
	}

	public void publishTicket(int[] countArray, final ResultListener listener) {
		commonticketPublisher.publishTicket(tranLogId, cardType, cardNo, countArray,
				new ResultListener() {

					@Override
					public void onSuccess(Response response) {
						JSONArray jarr = JSONArray.parseArray(response.result
								.toString());
						if(hasWeChatTicket(jarr)){
							printWeChatTicket(jarr,listener);
						} else{
							print(jarr);
							listener.onSuccess(new Response(0, "success"));
						}
					}

					@Override
					public void onFaild(Response response) {
						listener.onFaild(response);
					}
				});
	}

	// public void getPublishableTicket(ResultListener listener) {
	// // 加载卡券定义列表 ---> 银行卡 || 现金 || 商户卡 || 微信会员卡
	// if (functionNo == BANK_CARD || functionNo == CASH || functionNo ==
	// MEMBER_CARD || functionNo == WEPAY_MEMBER_CARD) {
	// ticketPublisher.getPublishableTicket(tranLogId, cardType, showFlag,
	// listener);
	// }
	// }

	/**
	 * 要打印的券里是否含有微信券
	 * @param jarr 
	 * @return
	 */
	private boolean hasWeChatTicket(JSONArray jarr) {
		for (int i = 0; i < jarr.size(); i++) {
			if (!TextUtils.isEmpty(jarr.getJSONObject(i).getString("ticketQrCode"))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取微信券二维码并调用打印
	 * @param jarr
	 * @author yaosong
	 * @param listener 
	 */
	private void printWeChatTicket(final JSONArray jarr, final ResultListener listener) {
		listener.onSuccess(new Response(0, "success"));
//		new Thread() {
//			public void run() {
//				try {
//					weChatQRCodes.clear();
//					for (int i = 0; i < jarr.size(); i++) {
//						String ticketQrCode = jarr.getJSONObject(i).getString("ticketQrCode");
//						if (!TextUtils.isEmpty(ticketQrCode)) {
//							HttpClient httpClient = HttpUtil.getHttpClient();
//							HttpGet httpGet = new HttpGet("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticketQrCode);
//							HttpResponse response = httpClient.execute(httpGet);
//							if (response.getStatusLine().getStatusCode() != 200) {
//								((Activity)context).runOnUiThread(new Runnable() {
//									@Override
//									public void run() {
//										Toast.makeText(context, "请求微信二维码发生异常", Toast.LENGTH_LONG).show();
//									}
//								});
//							}
//							Bitmap bm = BitmapFactory.decodeStream(response.getEntity().getContent());
//							weChatQRCodes.put(i, bm);
//						}
//					}
//					((Activity)context).runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							print(jarr);
//							listener.onSuccess(new Response(0, "success"));
//						}
//					});
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}.start();
	};
	
	public void print(JSONArray jarr) {
		if (transactionType == TransactionTemsController.TRANSACTION_TYPE_BANK_CARD
				|| transactionType == TransactionTemsController.TRANSACTION_TYPE_CASH) {// 银行卡或现金打印
			if(DeviceManager.getInstance().isSupprotPrint()){
				PrintServiceControllerProxy printer = new PrintServiceControllerProxy(context);
				Q1PrintBuilder builder = new Q1PrintBuilder();
				String printString = "";
				int resizeBitmapSize = builder.getResizeBitmapSize(Constants.QRCODE_LENGTH);
				printString += builder.center(builder.bold("券发行"));
				for (int i = 0; i < jarr.size(); i++) {
					JSONObject jobj = jarr.getJSONObject(i);
					JSONObject jobDef = jobj.getJSONObject("ticketDef");
					String ticketName = jobDef.getString("ticketName");
					printString += builder.branch();
					printString += builder.normal("时间："
							+ builder.normal(DateUtil.format(new Date(),
									DateUtil.P2)))+builder.br();;
					printer.print(printString);
					printString = "";
					
					Bitmap bitmap;
					//微信券标识：  wxFlag = "1""3"
					boolean isWeiXin = 
//							!TextUtils.isEmpty(jobj.getString("ticketQrCode"))&&
							(jobDef.getString("wxFlag").equals("1") || jobDef.getString("wxFlag").equals("3"));
					if (isWeiXin){
						//微信券二维码@yaosong [20151026]
						bitmap = weChatQRCodes.get(i);
					} else {
						//非微信券
						String printWx = jobj.getString("ticketNo");
						bitmap = Tools.genQRCodeTow(printWx);
					}
					
					printer.print(Tools.resizeBitmap(bitmap, resizeBitmapSize, resizeBitmapSize));
					printString += builder.br();
					
					if (isWeiXin){
						printString += builder.normal("券类型:"+"微信券") + builder.br();
					}
					printString += builder.normal("名称:"
							+ builder.normal(ticketName))+builder.br();
					
					if (TextUtils.isEmpty(jobj.getString("ticketQrCode"))){
						//非微信券才打印券号@yaosong [20151026]
						printString += builder.normal("券号:"
								+ builder.normal(jobj.getString("ticketNo")))+builder.br();
					}
					
					printString += builder.normal("截止日期:"
							+ builder.normal(DateUtil.format(
									jobj.getLongValue("expriyTime"), DateUtil.P1)))+builder.br();
					if (isWeiXin){
						printString += builder.normal("扫一扫领券！") + builder.br();
					}
					printString += builder.branch();
					printer.print(printString);
					printString = "";
					printer.print(builder.br() + builder.br());
				}
				printer.print(builder.endPrint());
			}else{
				List<DisplayTicektBean> displayTicektBeans = new ArrayList<DisplayTicektBean>();
				for (int i = 0; i < jarr.size(); i++) {
					DisplayTicektBean bean = new DisplayTicektBean();
					bean.setTitle("券发行");
					
					JSONObject jobj = jarr.getJSONObject(i);
					JSONObject jobDef = jobj.getJSONObject("ticketDef");
					String ticketName = jobDef.getString("ticketName");
					String subTitle = "时间：" + DateUtil.format(new Date(), DateUtil.P2);
					bean.setSubTitle(subTitle);
					
					String printWx = jobj.getString("ticketNo");
					Bitmap bitmap = Tools.genQRCodeTow(printWx);
					String filePath =FileUtil.saveBitmap(bitmap);
					bean.setBitmapPath(filePath);

					String endString = "名称:\n" + ticketName + "\n";
					endString += "券号:\n" + jobj.getString("ticketNo")+ "\n";
					endString += "截止日期:\n" + DateUtil.format(jobj.getLongValue("expriyTime"), DateUtil.P1);
					bean.setEndString(endString);
					
					displayTicektBeans.add(bean);
				}
				if(displayTicektBeans.isEmpty() == false){
					Intent intent = new Intent(context , DisplayTicketActivity.class);
					intent.putExtra(DisplayTicketActivity.DISPLAY_TICKET, (Serializable) displayTicektBeans);
					context.startActivity(intent);
				}
			}
		} else { // 非 银行卡 ,非 现金 打印
			PrintServiceControllerProxy printer = new PrintServiceControllerProxy(context);
			Q1PrintBuilder builder = new Q1PrintBuilder();
			String printString = "";
			printString += builder.center(builder.bold("券发行"));
			printString += builder.br();
			printString += "名称" + builder.tab("") + builder.tab("") + builder.tab("有效期") + builder.br();
			printString += builder.tab("金额(元)") + builder.tab("") + builder.tab("数量") + builder.br();
			printString += builder.branch();
			for (int i = 0; i < jarr.size(); i++) {
				JSONObject jobj = jarr.getJSONObject(i);
				JSONObject jobDef = jobj.getJSONObject("ticketDef");
				String ticketName = jobDef.getString("ticketName");
				printString += builder.normal(ticketName) + builder.tab("") + builder.tab(DateUtil.format(jobj.getLongValue("expriyTime"), DateUtil.P1)) + builder.br();
				if(jobDef.containsKey("balance")) {
					printString += builder.tab(Tools.formatFen(jobDef.getInteger("balance"))) + builder.tab("") + builder.tab("1") + builder.br();
				}
				printString += builder.br();
			}
			printer.print(printString);
			printer.print(builder.endPrint());
		}

	}
	
	/**
	 * 设置券列表
	 */
	public void setPublishableTicketDefs(List<TicketDef> publishableTicketDefs) {
		commonticketPublisher.setPublishableTicketDefs(publishableTicketDefs);
	}

	@Override
	public int getTransactionType() {
		return -1;
	}
}
