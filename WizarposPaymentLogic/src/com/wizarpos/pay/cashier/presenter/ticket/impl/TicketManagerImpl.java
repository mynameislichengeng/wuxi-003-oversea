package com.wizarpos.pay.cashier.presenter.ticket.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.model.SysInToken;
import com.wizarpos.pay.cashier.model.ThirdTicketInfo;
import com.wizarpos.pay.cashier.model.TicketDef;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.ticket.TicketImageLoader;
import com.wizarpos.pay.cashier.presenter.ticket.inf.TicketManager;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.GetCommonTicketInfoResp;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 券管理类
 *
 * @author wu
 */
public class TicketManagerImpl implements TicketManager {

    protected Context context;

    // 已添加的券列表
    protected List<TicketInfo> addedTickets = new ArrayList<TicketInfo>();

    public TicketManagerImpl(Context context) {
        this.context = context;
    }

    public Response addTicket(TicketInfo ticket, boolean isFromScan) {
        if (ticket == null) {
            return new Response(1, "添加失败:券号为空");
        }
        if (isFromScan) {
            ticket.setIsFromScan(Constants.TRUE);
        }
        if (isContainsWemengTicket()) {// 微盟券只允许添加一张 wu@[20150819]
            return new Response(1, "添加失败:该类型券只能使用一张,无法重复使用");
        }
        // if (addedTickets.contains(ticket)) { return false; }
        // isAddedTicket(ticket.getId());
        if (addedTickets.add(ticket)) {
            return new Response(0, "添加券成功");
        } else {
            return new Response(1, "该券已添加,不能重复添加");
        }
    }

    /**
     * 当前券列表中是否已经包含微盟券
     */
    public boolean isContainsWemengTicket() {
        for (TicketInfo info : addedTickets) {
            if (isWemengTicket(info)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是微盟券
     *
	 * @param info
	 * @return
	 */
	protected boolean isWemengTicket(TicketInfo info) {
		return "1".equals(info.isWeiMengTicket);
	}

	public List<TicketInfo> getAddedTickets() {
		return addedTickets;
	}

	@Override
	public void passAddedTickets(ResultListener listener) {

	}
	
	@Override
	public void getTicketInfo(String ticketNum, String amount,final ResultListener listener) {
		getTicketInfo(ticketNum, amount, "", listener);
	}

	@Override
	public void getTicketInfo(String ticketNum, String amount,final ResultListener listener,String isThirdPay,String isPaying,String shouldPayAmount) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ticketNo", ticketNum);
		if(PaymentApplication.getInstance().isWemengMerchant()){
			params.put("amount", amount);//微盟券新增传入金额 wu@[20150817]
		}
		params.put("isThirdPay", TextUtils.isEmpty(isThirdPay)?"":isThirdPay);
		params.put("isPaying", TextUtils.isEmpty(isPaying)?null:isPaying);
		params.put("shouldPayAmount", TextUtils.isEmpty(shouldPayAmount)?null:shouldPayAmount);
		NetRequest.getInstance().addRequest(Constants.SC_503_TICKET_DEF_QUERY_NO_MEMBER, params,
				new ResponseListener() {

					@Override
					public void onSuccess(Response response) {
						JSONObject robj = (JSONObject) response.getResult();
						if (!TextUtils.isEmpty(robj.getString("ticketInfo")) && !TextUtils.isEmpty(robj.getString("ticketDef"))) {
							TicketInfo _ticketInfo = JSONObject.parseObject(robj.getString("ticketInfo"),TicketInfo.class);
							TicketDef _ticketDef = JSONObject.parseObject(robj.getString("ticketDef"), TicketDef.class);
							_ticketInfo.setTicketDef(_ticketDef);
							//---------- 微盟券 wu----------------------
							TicketInfo wemengTicketInfo = null;
							try {
								if(robj.containsKey("giftTicket") ){
									if(((JSONObject)robj.get("giftTicket")).isEmpty() == false){
										wemengTicketInfo = Tools.jsonObjectToJavaBean((JSONObject) robj.get("giftTicket"), TicketInfo.class);
										wemengTicketInfo.setIsWeiMengTicket("1");//标示是微盟券
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							//---------- end 微盟券 wu ----------------
							GetCommonTicketInfoResp commonTicketInfoResp = new GetCommonTicketInfoResp();
							commonTicketInfoResp.setTicketInfo(_ticketInfo);
							commonTicketInfoResp.setWemengTicket(wemengTicketInfo);
							listener.onSuccess(new Response(0, "success", commonTicketInfoResp));
						} else {
							listener.onFaild(new Response(1, "数据解析失败"));
						}
					}

					@Override
					public void onFaild(Response response) {
						listener.onFaild(response);
					}
				});
	}
	
	public void getTicketInfo(String ticketNum, String amount,final boolean isFromScan, final ResultListener listener) {
		getTicketInfo(ticketNum, amount, new ResultListener() {
			@Override
			public void onSuccess(Response response) {
				GetCommonTicketInfoResp commonTicketInfoResp = (GetCommonTicketInfoResp) response.result;
				TicketInfo _ticketInfo = commonTicketInfoResp.getTicketInfo();
				if (isFromScan) {
					_ticketInfo.setIsFromScan("true");
				} else {
					_ticketInfo.setIsFromScan("false");
				}
				listener.onSuccess(new Response(0, "success", commonTicketInfoResp));
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}

	public boolean isAddedTicket(String ticketNum) {
		if (TextUtils.isEmpty(ticketNum)) { return false; }
		for (TicketInfo _ticket : addedTickets) {
			if (ticketNum.equals(_ticket.getId())/* id 就是 二维码扫出来的值 , 根据该条件来判断 */) { return true; }
		}
		return false;
	}

	public boolean removeAddedTicket(String ticketId) {
		if (TextUtils.isEmpty(ticketId)) { return false; }
		for (TicketInfo ticket : addedTickets) {
			if (ticketId.equals(ticket.getId())) { /* id 就是 二维码扫出来的值 , 根据该条件来判断 */
				addedTickets.remove(ticket);
				return true;
			}
		}
		return false;
	}

	public boolean removeAddedTicket(TicketInfo ticket) {
		return !(ticket == null || TextUtils.isEmpty(ticket.getId())) && removeAddedTicket(ticket.getId());
	}

	public void printTickets(List<TicketInfo> tickets) {
		if (tickets == null || tickets.isEmpty()) { return; }
		List<TicketInfo> wxTicketInfo = new ArrayList<TicketInfo>();
		PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
		Q1PrintBuilder builder = new Q1PrintBuilder();
		int resizeBitmapSize = builder.getResizeBitmapSize(Constants.QRCODE_LENGTH);//@hong 修改二维码大小
		String printString = "";
		printString += builder.center(builder.bold("券发行"));
		for (int i = 0; i < tickets.size(); i++) {
			TicketInfo ticket = tickets.get(i);
			printString += builder.branch();
			if(ticket.getTicketDef().getWxFlag().equals("1")||ticket.getTicketDef().getWxFlag().equals("3")) {
				//微信会员卡打印
				wxTicketInfo.add(ticket);
				continue;
			}
			printString += builder.normal("时间：" + builder.normal(DateUtil.format(new Date(),DateUtil.P2)))+builder.br();
			controller.print(printString);
			printString = "";
			Bitmap bitmap = Tools.genQRCodeTow(ticket.getTicketNo());
			controller.print(Tools.resizeBitmap(bitmap, resizeBitmapSize, resizeBitmapSize));
			printString += builder.center("名称:" + builder.normal(ticket.getTicketDef().getTicketName()));
			if(PaymentApplication.getInstance().isWemengMerchant() && !TextUtils.isEmpty(ticket.getDisplayCode())) {
				printString += builder.center("券号:" + builder.normal(ticket.getDisplayCode()));
			}else {
				printString += builder.center("券号:" + builder.normal(ticket.getTicketNo()));
			}
			printString += builder.center("截止日期:"
					+ builder.normal(DateUtil.format(ticket.getExpriyTime(), DateUtil.P1)));
//			printStringPayFor += builder.normal(ticket.getTicketDef().getTicketName()) + builder.tab("")
//					+ builder.tab(DateUtil.format(ticket.getExpriyTime(), DateUtil.P1)) + builder.br();
//			printStringPayFor += builder.tab(ticket.getTicketNo()) + builder.tab("")
//					+ builder.tab(Tools.formatFen(ticket.getTicketDef().getBalance())) + builder.tab("") + builder.br();
			printString += builder.branch();
			controller.print(printString);
			printString = "";
			printString += builder.br();
			controller.print(builder.br() + builder.br());
		}
		controller.print(builder.endPrint());
		
		if(wxTicketInfo != null && wxTicketInfo.size() > 0) {
			TicketImageLoader imgLoader = new TicketImageLoader(wxTicketInfo, new ResultListener() {
				
				@Override
				public void onSuccess(Response response) {
					List<TicketInfo> listTicket = (List<TicketInfo>) response.getResult();
					printWxTicket(listTicket);
				}
				
				@Override
				public void onFaild(Response response) {
					Log.w("TAG", "warring msg:" + response.getMsg());
				}
			});
			imgLoader.startLoad();
		}
	}
	
	private void printWxTicket(List<TicketInfo> listTicket) {

		PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
        Q1PrintBuilder builder = new Q1PrintBuilder();
        int resizeBitmapSize = builder.getResizeBitmapSize(Constants.QRCODE_LENGTH);
        String printString = "";
        printString += builder.center(builder.bold("券发行"));
        for (TicketInfo info : listTicket) {
            printString += builder.branch();
			printString += builder.normal("时间：" + builder.normal(DateUtil.format(new Date(),DateUtil.P2)))+builder.br();
			controller.print(printString);
			printString = "";
			if (!TextUtils.isEmpty(info.getTicketQrcode()))
			{
				controller.print(Tools.resizeBitmap(readPng(info.getTicketQRCodeLocalPath()), resizeBitmapSize, resizeBitmapSize));
			}
			printString += builder.center("券类型:" + "微信券");
			printString += builder.center("名称:" + builder.normal(info.getTicketDef().getTicketName()));
			printString += builder.center("券号:" + builder.normal(info.getTicketNo()));
			printString += builder.center("截止日期:"
					+ builder.normal(DateUtil.format(info.getExpriyTime(), DateUtil.P1)));
			printString += builder.center("扫一扫领券！");
			controller.print(printString);
            printString = "";
			controller.print(builder.br() + builder.br());
		}
        controller.print(builder.endPrint());
        controller.cutPaper();
	
	}
	

	@Override
	public void getThirdTicketInfo(String ticketNum, final ResultListener listener) {
		if (TextUtils.isEmpty(ticketNum)) {
			listener.onFaild(new Response(1, "券号为空"));
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
//		Log.e("ticketNum", ticketNum);
//		params.put("ticketId", ticketNum);
        if (PaymentApplication.getInstance().isWemengMerchant()) {//区分微盟券逻辑 wu@[20150826]
            params.put("ticketNo", ticketNum);
        } else {
            params.put("ticketId", ticketNum);
        }
        NetRequest.getInstance().addRequest(Constants.SC_910_THIRD_TICKET_INFO, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                if (PaymentApplication.getInstance().isWemengMerchant()) { //区别微盟券 wu@[20150826]
                    TicketInfo wemengTicketInfo = JSONObject.parseObject(response.getResult().toString(), TicketInfo.class);
                    if (wemengTicketInfo == null) {
                        listener.onFaild(new Response(1, "获取券信息失败"));
                    } else {
                        listener.onSuccess(new Response(0, "获取券信息成功", wemengTicketInfo));
                    }
                } else {
                    SysInToken sysInToken = JSONObject.parseObject(response.getResult().toString(), SysInToken.class);
                    if (sysInToken == null || TextUtils.isEmpty(sysInToken.getInfo())) {
                        listener.onFaild(new Response(1, "获取券信息失败"));
                        return;
                    }
                    ThirdTicketInfo ticketInfo = JSONObject.parseObject(sysInToken.getInfo(), ThirdTicketInfo.class);
                    if (ticketInfo == null) {
                        listener.onFaild(new Response(1, "获取券信息失败"));
                        return;
                    } else {
                        listener.onSuccess(new Response(0, "获取券信息成功", ticketInfo));
                    }
                }
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);
            }
        });
    }

    public void passWeimengTicket(TicketInfo ticket, final BasePresenter.ResultListener listener) {
        if (ticket == null || TextUtils.isEmpty(ticket.getTicketNo())) {
            listener.onFaild(new Response(1, "数据解析错误"));
            return;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ticketNo", ticket.getTicketNo());
        params.put("amount", "0");
        NetRequest.getInstance().addRequest(Constants.SC_911_THIRD_TICKET_INFO, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                listener.onSuccess(response);
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);
            }
        });
    }

    @Override
    public void passThirdTicket(final ThirdTicketInfo ticket, final ResultListener listener) {
        if (ticket == null || TextUtils.isEmpty(ticket.getCallBackUrl())) {
            listener.onFaild(new Response(1, "数据解析错误"));
            return;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("callBackUrl", ticket.getCallBackUrl());
        NetRequest.getInstance().addRequest(Constants.SC_911_THIRD_TICKET_INFO, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                printThirdTicketPass(ticket);
                listener.onSuccess(response);
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);
            }
        });
    }

    /**
     * 第三方卡券核销打印
     */
    private void printThirdTicketPass(ThirdTicketInfo ticketInfo) {
        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
        Q1PrintBuilder pb = new Q1PrintBuilder();
        String printString = "";
        printString += pb.center(pb.bold("第三方券核销"));
        printString += pb.branch();
        printString += "慧商户号：" + AppConfigHelper.getConfig(AppConfigDef.mid) + pb.br();
        printString += "商户名称：" + AppConfigHelper.getConfig(AppConfigDef.merchantName) + pb.br();
        printString += "终端号：" + AppConfigHelper.getConfig(AppConfigDef.terminalId) + pb.br();
        printString += "券号：" + ticketInfo.getCode() + pb.br();
        printString += "名称：" + ticketInfo.getName() + pb.br();
        printString += "金额：" + ticketInfo.getAmount() + "元" + pb.br();
        printString += "时间：" + DateUtil.format(new Date(), DateUtil.P2) + pb.br();
        printString += pb.branch();
        printString += pb.endPrint();
        controller.print(printString);
    }

    @Override
    public void passWepayTicket(String ticketNum, final ResultListener listener) {
        if (TextUtils.isEmpty(ticketNum)) {
            listener.onFaild(new Response(1, "券号为空"));
            return;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("wxCode", ticketNum);
        if (Constants.APP_VERSION_NAME == Constants.APP_VERSION_LAWSON) {
            final String FLAG_LAWSON = "1"; //罗森
            final String FLAG_NORMAL = "0"; //非罗森
            params.put("luosenFlag", FLAG_LAWSON);
        }
        NetRequest.getInstance().addRequest(Constants.SC_701_WEPAY_TICKET_PASS, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                if (Constants.APP_VERSION_NAME == Constants.APP_VERSION_LAWSON) {
                    printWeixinPass(response.getResult().toString());//打印核销微信
                }
                listener.onSuccess(response);
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);
            }
        });
    }

    @Override
    public void passWizarGiftTicket(String ticketNum, final ResultListener listener) {
        Map<String, Object> m = new HashMap<String, Object>();
        List<String> ids = new ArrayList<String>();
        ids.add(ticketNum + "|0");
        m.put("ids", ids);
        NetRequest.getInstance().addRequest(Constants.SC_502_TICKET_USE, m, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                listener.onSuccess(response);
            }

            @Override
            public void onFaild(Response response) {
                listener.onSuccess(response);
            }
        });
    }

    public void printWizarGiftTicket(TicketInfo ticketInfo) {
        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
        Q1PrintBuilder builder = new Q1PrintBuilder();
        String printString = "";

        printString += builder.center(builder.bold("慧商卡券核销"));
        printString += builder.branch();
        printString += "慧商户号：" + AppConfigHelper.getConfig(AppConfigDef.mid, "") + builder.br();
        printString += "商户名称：" + AppConfigHelper.getConfig(AppConfigDef.merchantName, "") + builder.br();
        printString += "终端号：" + AppConfigHelper.getConfig(AppConfigDef.terminalId, "") + builder.br();
        printString += "券名称：" + ticketInfo.getTicketDef().getTicketName() + builder.br();
        printString += "券号：" + ticketInfo.getTicketNo() + builder.br();
        printString += "操作员：" + AppConfigHelper.getConfig(AppConfigDef.operatorTrueName, "") + builder.br();
        printString += "时间：" + DateUtil.format(System.currentTimeMillis(), DateUtil.P4) + builder.br();
        printString += builder.branch() + builder.br();

        printString += builder.endPrint();
        controller.print(printString);
        controller.cutPaper();
    }

    /**
     * @Author: Huangweicai
     * @date 2015-11-9 下午1:37:03
     * @Description:打印微信核销(LAWSON)
     */
    public void printWeixinPass(String response) {
        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONObject tranLog = jsonObject.getJSONObject("tranLog");
        JSONObject ticketDef = jsonObject.getJSONObject("ticketDef");


        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
        Q1PrintBuilder builder = new Q1PrintBuilder();
        String printString = "";

        printString += builder.center(builder.bold("微信卡券核销"));
        printString += builder.branch();


        printString += "慧商户号：" + AppConfigHelper.getConfig(AppConfigDef.mid, "") + builder.br();
        printString += "商户名称：" + AppConfigHelper.getConfig(AppConfigDef.merchantName, "") + builder.br();
        printString += "终端号：" + AppConfigHelper.getConfig(AppConfigDef.terminalId, "") + builder.br();
        printString += "券名称：" + ticketDef.getString("ticketName") + builder.br();

        printString += "券金额：" + Tools.formatFen(ticketDef.getLong("reduceCost")) + "元" + builder.br();
        printString += "流水号：" + tranLog.getString("masterTranLogId") + builder.br();
        printString += "操作员：" + AppConfigHelper.getConfig(AppConfigDef.operatorTrueName, "") + builder.br();
        printString += "时间：" + DateUtil.format(System.currentTimeMillis(), DateUtil.P4) + builder.br();
        printString += builder.branch() + builder.br();

        printString += builder.endPrint();
        controller.print(printString);
        controller.cutPaper();
    }

    /**
     * 营销二期券打印
     *
     * @param publishTicketsBean
     * @param isWeixin
     */
    public void printThirdGiveTickets(List<TicketInfo> publishTicketsBean, boolean isWeixin) {
        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
        Q1PrintBuilder builder = new Q1PrintBuilder();
        int resizeBitmapSize = builder.getResizeBitmapSize();
        String printString = "";

        printString += builder.center(builder.bold("券发行"));
        controller.print(printString);
        for (TicketInfo info : publishTicketsBean) {
            printString = "";
            printString += builder.branch();
            printString += builder.normal("时间："
                    + builder.normal(DateUtil.format(new Date(),
                    DateUtil.P2))) + builder.br();
            controller.print(printString);
            printString = "";
			boolean isWeiXin = !TextUtils.isEmpty(info.getTicketQrcode());
            if (isWeiXin) {
                controller.print(Tools.resizeBitmap(readPng(info.getTicketQRCodeLocalPath()), resizeBitmapSize, resizeBitmapSize));
            } else {
                controller.print(Tools.resizeBitmap(Tools.genQRCode(info.getTicketNo()), resizeBitmapSize, resizeBitmapSize));
            }
            printString = "";
            if (isWeiXin){
                printString += builder.center("券类型："+"微信券") + builder.br();
            }
            printString += builder.center("名称：" + info.getTicketDef().getTicketName()) + builder.br();
            printString += builder.center("券号：" + info.getTicketNo()) + builder.br();
            printString += builder.center("截止日期：" + DateUtil.format(DateUtil.addDay(new Date(System.currentTimeMillis()), info.getTicketDef().getValidPeriod()), DateUtil.P1)) + builder.br();
            if (!TextUtils.isEmpty(info.getDescription())) {
                printString += builder.center("备注" + info.getDescription()) + builder.br();
            }
			if (isWeiXin){
				printString += builder.center("扫一扫领券！") + builder.br();
			}
            printString += builder.branch() + builder.br();
            controller.print(printString);
            printString = "";
            controller.print(builder.br() + builder.br());
        }
        controller.print(builder.endPrint());
        controller.cutPaper();
    }

    @Override
    public void filterWepayQRTicket(List<TicketInfo> ticketInfos, ResultListener listener) {
        if (ticketInfos == null) {
            return;
        }
        LogEx.d("现金消费券返回张数", ticketInfos.size() + "");
        TicketImageLoader loader = new TicketImageLoader(ticketInfos, listener);
        loader.startLoad();
    }

    @Override
    public String getAddedWemengTicketNo() {
        for (TicketInfo info : addedTickets) {
            if (isWemengTicket(info)) {
                return info.getTicketNo();
            }
        }
        return null;
    }

    private Bitmap readPng(String path) {
        try {
            File mfile = new File(path);
            if (mfile.exists()) {// 若该文件存在
                Bitmap bm = BitmapFactory.decodeFile(path);
                return bm;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void verifyTicket(String ticketNo, String shouldPayAmount,
                             final ResultListener listener) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ticketNo", ticketNo);
        params.put("shouldPayAmount", shouldPayAmount);
        NetRequest.getInstance().addRequest(Constants.SC_507_QUERY_TICKET, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                JSONObject robj = (JSONObject) response.getResult();
                if (!TextUtils.isEmpty(robj.getString("ticketInfo")) && !TextUtils.isEmpty(robj.getString("ticketDef"))) {
                    TicketInfo _ticketInfo = JSONObject.parseObject(robj.getString("ticketInfo"), TicketInfo.class);
                    TicketDef ticketDef = JSONObject.parseObject(robj.getString("ticketDef"), TicketDef.class);
                    _ticketInfo.setTicketDef(ticketDef);
                    listener.onSuccess(new Response(0, "success", _ticketInfo));
                } else {
                    listener.onFaild(new Response(1, "数据解析失败"));
                }
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);
            }
        });
    }

    @Override
    public void getTicketInfo(String ticketNum, String amount, String shouldPayAmount, final ResultListener listener) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ticketNo", ticketNum);
        params.put("shouldPayAmount", shouldPayAmount);
        if (PaymentApplication.getInstance().isWemengMerchant()) {
            params.put("amount", amount);//微盟券新增传入金额 wu@[20150817]
        }
        NetRequest.getInstance().addRequest(Constants.SC_503_TICKET_DEF_QUERY_NO_MEMBER, params,
                new ResponseListener() {

                    @Override
                    public void onSuccess(Response response) {
                        JSONObject robj = (JSONObject) response.getResult();
                        if (!TextUtils.isEmpty(robj.getString("ticketInfo")) && !TextUtils.isEmpty(robj.getString("ticketDef"))) {
                            TicketInfo _ticketInfo = JSONObject.parseObject(robj.getString("ticketInfo"), TicketInfo.class);
                            TicketDef _ticketDef = JSONObject.parseObject(robj.getString("ticketDef"), TicketDef.class);
                            _ticketInfo.setTicketDef(_ticketDef);
                            //---------- 微盟券 wu----------------------
                            TicketInfo wemengTicketInfo = null;
                            try {
                                if (robj.containsKey("giftTicket")) {
                                    if (((JSONObject) robj.get("giftTicket")).isEmpty() == false) {
                                        wemengTicketInfo = Tools.jsonObjectToJavaBean((JSONObject) robj.get("giftTicket"), TicketInfo.class);
                                        wemengTicketInfo.setIsWeiMengTicket("1");//标示是微盟券
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //---------- end 微盟券 wu ----------------
                            GetCommonTicketInfoResp commonTicketInfoResp = new GetCommonTicketInfoResp();
                            commonTicketInfoResp.setTicketInfo(_ticketInfo);
                            commonTicketInfoResp.setWemengTicket(wemengTicketInfo);
                            listener.onSuccess(new Response(0, "success", commonTicketInfoResp));
                        } else {
                            listener.onFaild(new Response(1, "数据解析失败"));
                        }
                    }

                    @Override
                    public void onFaild(Response response) {
                        listener.onFaild(response);
                    }
                });
    }

}
