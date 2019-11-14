package com.wizarpos.pay.cashier.pay_tems.bat;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wizarpos.base.net.Response;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.pay_tems.bat.entities.BatCreateOrder;
import com.wizarpos.pay.cashier.pay_tems.bat.entities.BatMicroRsp;
import com.wizarpos.pay.cashier.pay_tems.bat.entities.BatNewReq;
import com.wizarpos.pay.cashier.pay_tems.bat.entities.BatReq;
import com.wizarpos.pay.cashier.pay_tems.bat.inf.MicroTransaction;
import com.wizarpos.pay.cashier.pay_tems.wepay.NetWorkUtils;
import com.wizarpos.pay.cashier.presenter.ticket.TicketManagerFactory;
import com.wizarpos.pay.cashier.presenter.ticket.inf.CommonTicketManager;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.utils.Logger2;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.GetCommonTicketInfoResp;

public class BatMicroTransactionImpl extends BatTransation implements MicroTransaction{

	private CommonTicketManager commonTicketManager;


	public BatMicroTransactionImpl(Context context) {
		super(context);
	}


    @Override
    public void batUnionPay(String payBatType, final String authCode,final ResultListener resultListener) {

//        transactionInfo.setRealAmount(transactionInfo.getInitAmount());// 获取支付金额
        try {
            BatCreateOrder req = new BatCreateOrder();
            req.setAmount(transactionInfo.getRealAmount());
            req.setExtraAmount(transactionInfo.getReduceAmount());
            req.setCardNo(transactionInfo.getCardNo());
            req.setRechargeOn(transactionInfo.getRechargeOn());
//            req.setInputAmount(transactionInfo.getInitAmount());//第三方应用调用时传入的初始金额 
            if(isMixTransaction()){
                req.setInputAmount(transactionInfo.getMixInitAmount());//组合支付总金额
            }else{
                req.setInputAmount(transactionInfo.getInitAmount());
            }
            req.setMasterPayAmount(transactionInfo.getMixInitAmount());//传入混合支付的总金额 wu@[20150923]
            req.setIsPayComingForm(transactionInfo.getIsPayComingForm());//XXX 组合支付增加
            req.setMixFlag(transactionInfo.getMixFlag());
            req.setMasterTranLogId(transactionInfo.getMixTranLogId());
            req.setPayBatType(payBatType);
            req.setCaptcha((AppConfigHelper.getConfig(AppConfigDef.auth_code)));
            req.setCommoncashierOrderId(transactionInfo.getCommoncashierOrderId());
            req.setAuthCode(authCode);
            createUnionBatOrder(req, Constants.SC_873_ONLINE_CREAT_ORDER, new ResultListener() {
                @Override
                public void onSuccess(Response response) {
                    try {
                        doTransActionUnion(authCode, resultListener);
                    } catch (Exception e) {
                        e.printStackTrace();
                        resultListener.onFaild(new Response(1, "未知异常"));
                    }
                }
                @Override
                public void onFaild(Response response) {
                    resultListener.onFaild(response);
                }
            });

        } catch (Exception e) {
            resultListener.onFaild(new Response(1, "请求参数错误"));
            e.printStackTrace();
        }


    }



	/*
	 * bat支付调用
	 */
	@Override
	public void batPay(String payBatType, final String authCode,
			final ResultListener resultListener) {
//		transactionInfo.setRealAmount(transactionInfo.getInitAmount());// 获取支付金额
		try {
			BatCreateOrder req = new BatCreateOrder();
			req.setAmount(transactionInfo.getRealAmount());
			req.setExtraAmount(transactionInfo.getReduceAmount());
			req.setCardNo(transactionInfo.getCardNo());
			req.setRechargeOn(transactionInfo.getRechargeOn());
//			req.setInputAmount(transactionInfo.getInitAmount());//第三方应用调用时传入的初始金额 
			if (TextUtils.isEmpty(transactionInfo.getSaleInputAmount())) {
				if(isMixTransaction()){
					req.setInputAmount(transactionInfo.getMixInitAmount());//组合支付总金额
				}else{
					req.setInputAmount(transactionInfo.getInitAmount());
				}
			}else {
				req.setInputAmount(transactionInfo.getSaleInputAmount());
			}
			req.setMasterPayAmount(transactionInfo.getMixInitAmount());//传入混合支付的总金额 wu@[20150923]
			req.setIsPayComingForm(transactionInfo.getIsPayComingForm());//XXX 组合支付增加
			req.setMixFlag(transactionInfo.getMixFlag());
			req.setMasterTranLogId(transactionInfo.getMixTranLogId());
			req.setPayBatType(payBatType);
			req.setCaptcha((AppConfigHelper.getConfig(AppConfigDef.auth_code)));
			req.setCommoncashierOrderId(transactionInfo.getCommoncashierOrderId());
			req.setIds(transactionInfo.getIds());
			req.setMnFlag(BatCreateOrder.MN_FLAG_MICRO);
			req.setWmHxInfo(transactionInfo.getBatTicket());
			if (Constants.BAT_V1_4_FLAG) {

				createBatOrderNew(req, Constants.SC_873_ONLINE_CREAT_ORDER, new ResultListener() {
					@Override
					public void onSuccess(Response response) {
						try {
							doTransActionNew(authCode, resultListener);
						} catch (Exception e) {
							e.printStackTrace();
							resultListener.onFaild(new Response(1, "未知异常"));
						}
					}

					@Override
					public void onFaild(Response response) {
						resultListener.onFaild(response);
					}
				});

			}else {

				createBatOrder(req, Constants.SC_870_ONLINE_CREAT_ORDER, new ResultListener() {
					@Override
					public void onSuccess(Response response) {
						try {
							doTransAction(authCode, resultListener);
						} catch (Exception e) {
							e.printStackTrace();
							resultListener.onFaild(new Response(1, "未知异常"));
						}
					}

					@Override
					public void onFaild(Response response) {
						resultListener.onFaild(response);
					}
				});

			}

		} catch (Exception e) {
			resultListener.onFaild(new Response(1, "请求参数错误"));
			e.printStackTrace();
		}

	}

    /**
     *
     * @Author: Huangweicai
     * @date 2015-12-4 下午5:05:27
     * @param authCode
     * @param resultListener
     * @Description:生成第三方订单 {@link #doTransActionNew}
     */
    protected void doTransActionUnion(String authCode, final ResultListener resultListener){

        BatNewReq req = new BatNewReq();
        String transType=transactionInfo.getPayTypeFlag();
        String payFlag="";
        if (!TextUtils.isEmpty(transType)) {
            if (!TextUtils.isEmpty(authCode)) {
                req.setAuth_code(authCode);
            }
            if (transType.equals(Constants.ALIPAY_BAT)) {
                payFlag=FLAG_ALIPAY_MICRO;
//                req.setStore_id(store_id);//暂无门店id
                req.setPayee_term_id(AppConfigHelper.getConfig(AppConfigDef.alipay_payeeTermId));//收款方终端号
                req.setTerminal_no(AppConfigHelper.getConfig(AppConfigDef.sn));//终端设备号
                req.setOut_trade_no(transactionInfo.getTranId());//商户订单号
                req.setGoods_info(TextUtils.isEmpty(transactionInfo.getBody()) ? "支付宝支付" : transactionInfo.getBody()); //商品名称
                req.setTotal_fee(transactionInfo.getRealAmount());//支付金额
            }else if (transType.equals(Constants.WEPAY__BAT)) {
                payFlag=FLAG_WECHATPAY_MICRO;
                req.setGoods_info(TextUtils.isEmpty(transactionInfo.getBody()) ? "微信支付" : transactionInfo.getBody());
                req.setOut_trade_no(transactionInfo.getTranId());
                req.setTotal_fee(transactionInfo.getRealAmount());
                req.setTerminal_no(AppConfigHelper.getConfig(AppConfigDef.sn));
//                req.setSpbill_create_ip(NetWorkUtils.getLocalIpAddress(PaymentApplication.getInstance()));
            }else if (transType.equals(Constants.TENPAY_BAT)) {
                payFlag=FLAG_TENPAY_MICRO;
                req.setOut_trade_no(transactionInfo.getTranId());
                req.setGoods_info(TextUtils.isEmpty(transactionInfo.getBody()) ? "QQ钱包支付" : transactionInfo.getBody());
                req.setTotal_fee(transactionInfo.getRealAmount());
                req.setTerminal_no(AppConfigHelper.getConfig(AppConfigDef.sn));
            }else if (transType.equals(Constants.BAIDUPAY_BAT)) {
                payFlag=FLAG_BAIDU_MICRO;
                req.setOut_trade_no(transactionInfo.getTranId());
                req.setGoods_info(TextUtils.isEmpty(transactionInfo.getBody()) ? "百度钱包支付"
                        : transactionInfo.getBody());
                req.setTerminal_no(AppConfigHelper.getConfig(AppConfigDef.sn));
                req.setTotal_fee(transactionInfo.getRealAmount());
            }else if(transType.equals(Constants.UNION_BAT)) {
                payFlag=FLAG_BAIDU_MICRO;
                req.setOut_trade_no(transactionInfo.getTranId());
                req.setGoods_info(TextUtils.isEmpty(transactionInfo.getBody()) ? "移动支付被扫"
                        : transactionInfo.getBody());
                req.setTerminal_no(AppConfigHelper.getConfig(AppConfigDef.sn));
                req.setTotal_fee(transactionInfo.getRealAmount());
            }
        }
        req.setSpbill_create_ip(NetWorkUtils.getLocalIpAddress(PaymentApplication.getInstance()));
        Gson gson = new Gson();
        String microReqJson = gson.toJson(req);
        payUnion(payFlag, microReqJson, new BasePresenter.ResultListener() {

            @Override
            public void onSuccess(Response response) {
                try {
                    Logger2.debug("下单成功" + response.result);
                    //若开启移动支付,返回的处理与以往代码差异较大(对err_code的处理),重起新方法 @hwc
                    parseMicroUnion(resultListener, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    resultListener.onFaild(new Response(1, "未知异常"));
                }

            }

            @Override
            public void onFaild(final Response response) {
                Logger2.debug("下单失败" + response.result);
                if (isMixTransaction() && transactionInfo.isFirstTransaction()) {// 第一步交易失败撤销主单
                    revokeMixTranLog(transactionInfo.getMixTranLogId(), new ResultListener() {

                        @Override
                        public void onSuccess(Response response1) {
                            resultListener.onFaild(response);
                        }

                        @Override
                        public void onFaild(Response response1) {
                            resultListener.onFaild(response);
                        }
                    });
                }else{
                    resultListener.onFaild(response);
                }
            }
        });


    }


	/**
	 * 生成第三方订单
	 * @param authCode
	 * @param resultListener
	 */
	protected void doTransAction(String authCode, final ResultListener resultListener) {
		BatReq req = new BatReq();
		String transType=transactionInfo.getPayTypeFlag();
		String payFlag="";
		if (!TextUtils.isEmpty(transType)) {
			if (transType.equals(Constants.ALIPAY_BAT)) {
				payFlag=FLAG_ALIPAY_MICRO;
				req.setPayeeTermId(AppConfigHelper.getConfig(AppConfigDef.alipay_payeeTermId));
				req.setOutTradeNo(transactionInfo.getTranId());
//				req.setTERMINAL_ID(Build.SERIAL);
				req.setTERMINAL_ID(AppConfigHelper.getConfig(AppConfigDef.sn));
				req.setSubject(TextUtils.isEmpty(transactionInfo.getBody()) ? "支付宝支付" : transactionInfo.getBody());
				req.setTotalFee(transactionInfo.getRealAmount());
			}else if (transType.equals(Constants.WEPAY__BAT)) {
				payFlag=FLAG_WECHATPAY_MICRO;
				req.setBody(TextUtils.isEmpty(transactionInfo.getBody()) ? "微信支付" : transactionInfo.getBody());
				req.setOut_trade_no(transactionInfo.getTranId());
				req.setTotal_fee(transactionInfo.getRealAmount());
//				req.setSpbill_create_ip(NetWorkUtils.getLocalIpAddress(PaymentApplication.getInstance()));
				req.setTERMINAL_ID(AppConfigHelper.getConfig(AppConfigDef.sn));
			}else if (transType.equals(Constants.TENPAY_BAT)) {
				payFlag=FLAG_TENPAY_MICRO;
				req.setBody(TextUtils.isEmpty(transactionInfo.getBody()) ? "QQ钱包支付" : transactionInfo.getBody());
				req.setOut_trade_no(transactionInfo.getTranId());
				req.setSp_device_id(AppConfigHelper.getConfig(AppConfigDef.sn));
				req.setTotal_fee(transactionInfo.getRealAmount());
			}else if (transType.equals(Constants.BAIDUPAY_BAT)) {
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				Date orderCreateDate = new Date(System.currentTimeMillis());
				String orderCreateTime = format.format(orderCreateDate);
				payFlag=FLAG_BAIDU_MICRO;
				req.setSp_no(AppConfigHelper.getConfig(AppConfigDef.baidupay_id));
				req.setOrder_create_time(orderCreateTime);
				req.setOrder_no(transactionInfo.getTranId());
				req.setGoods_name(TextUtils.isEmpty(transactionInfo.getBody()) ? "百度钱包支付"
						: transactionInfo.getBody());
				req.setTotal_amount(transactionInfo.getRealAmount());
				if (!TextUtils.isEmpty(authCode)) {
					req.setPay_code(authCode);
				}
			}
		}
		req.setSpbill_create_ip(NetWorkUtils.getLocalIpAddress(PaymentApplication.getInstance()));
		req.setRechargeOn(transactionInfo.getRechargeOn());
		//transType.equals(Constants.BAIDUPAY_BAT)
		if (!Constants.BAIDUPAY_BAT.equals(transType)) {
			if (!TextUtils.isEmpty(authCode)) {
				req.setAuthCode(authCode);
			}
		}
//		String microReqJson = JSONObject.toJSONString(req);
		Gson gson = new Gson();
		String microReqJson = gson.toJson(req);
		pay(payFlag, microReqJson, new BasePresenter.ResultListener() {

			@Override
			public void onSuccess(Response response) {
				try {
					Logger2.debug("下单成功" + response.result);
					parseMicro(resultListener, response);
				} catch (Exception e) {
					e.printStackTrace();
					resultListener.onFaild(new Response(1, "未知异常"));
				}

			}

			@Override
			public void onFaild(final Response response) {
				Logger2.debug("下单失败" + response.result);
				if (isMixTransaction() && transactionInfo.isFirstTransaction()) {// 第一步交易失败撤销主单
					revokeMixTranLog(transactionInfo.getMixTranLogId(), new ResultListener() {

						@Override
						public void onSuccess(Response response1) {
							resultListener.onFaild(response);
						}

						@Override
						public void onFaild(Response response1) {
							resultListener.onFaild(response);
						}
					});
				}else{
					resultListener.onFaild(response);
				}
			}
		});

	}
	/**
	 * 生成第三方订单
	 * @param authCode
	 * @param resultListener
	 */
	protected void doTransActionNew(String authCode, final ResultListener resultListener) {
		BatNewReq req = new BatNewReq();
		String transType=transactionInfo.getPayTypeFlag();
		String payFlag="";
		if (!TextUtils.isEmpty(transType)) {
			if (!TextUtils.isEmpty(authCode)) {
				req.setAuth_code(authCode);
			}
			if (transType.equals(Constants.ALIPAY_BAT)) {
				payFlag=FLAG_ALIPAY_MICRO;
//				req.setStore_id(store_id);//暂无门店id
				req.setPayee_term_id(AppConfigHelper.getConfig(AppConfigDef.alipay_payeeTermId));//收款方终端号
				req.setTerminal_no(AppConfigHelper.getConfig(AppConfigDef.sn));//终端设备号
				req.setOut_trade_no(transactionInfo.getTranId());//商户订单号
				req.setGoods_info(TextUtils.isEmpty(transactionInfo.getBody()) ? "支付宝支付" : transactionInfo.getBody()); //商品名称
				req.setTotal_fee(transactionInfo.getRealAmount());//支付金额
			}else if (transType.equals(Constants.WEPAY__BAT)) {
				payFlag=FLAG_WECHATPAY_MICRO;
				req.setGoods_info(TextUtils.isEmpty(transactionInfo.getBody()) ? "微信支付" : transactionInfo.getBody());
				req.setOut_trade_no(transactionInfo.getTranId());
				req.setTotal_fee(transactionInfo.getRealAmount());
				req.setTerminal_no(AppConfigHelper.getConfig(AppConfigDef.sn));
//				req.setSpbill_create_ip(NetWorkUtils.getLocalIpAddress(PaymentApplication.getInstance()));
			}else if (transType.equals(Constants.TENPAY_BAT)) {
				payFlag=FLAG_TENPAY_MICRO;
				req.setOut_trade_no(transactionInfo.getTranId());
				req.setGoods_info(TextUtils.isEmpty(transactionInfo.getBody()) ? "QQ钱包支付" : transactionInfo.getBody());
				req.setTotal_fee(transactionInfo.getRealAmount());
				req.setTerminal_no(AppConfigHelper.getConfig(AppConfigDef.sn));
			}else if (transType.equals(Constants.BAIDUPAY_BAT)) {
				payFlag=FLAG_BAIDU_MICRO;
				req.setOut_trade_no(transactionInfo.getTranId());
				req.setGoods_info(TextUtils.isEmpty(transactionInfo.getBody()) ? "百度钱包支付"
						: transactionInfo.getBody());
				req.setTerminal_no(AppConfigHelper.getConfig(AppConfigDef.sn));
				req.setTotal_fee(transactionInfo.getRealAmount());
			}
		}
		req.setSpbill_create_ip(NetWorkUtils.getLocalIpAddress(PaymentApplication.getInstance()));
		Gson gson = new Gson();
		String microReqJson = gson.toJson(req);
		payNew(payFlag, microReqJson, new BasePresenter.ResultListener() {

			@Override
			public void onSuccess(Response response) {
				try {
					Logger2.debug("下单成功" + response.result);
					parseMicro(resultListener, response);
				} catch (Exception e) {
					e.printStackTrace();
					resultListener.onFaild(new Response(1, "未知异常"));
				}

			}

			@Override
			public void onFaild(final Response response) {
				Logger2.debug("下单失败" + response.result);
				if (isMixTransaction() && transactionInfo.isFirstTransaction()) {// 第一步交易失败撤销主单
					revokeMixTranLog(transactionInfo.getMixTranLogId(), new ResultListener() {

						@Override
						public void onSuccess(Response response1) {
							resultListener.onFaild(response);
						}

						@Override
						public void onFaild(Response response1) {
							resultListener.onFaild(response);
						}
					});
				}else{
					resultListener.onFaild(response);
				}
			}
		});

	}

    /**
     *
     * @Author: Huangweicai
     * @date 2015-12-4 上午11:09:23
     * @Description:移动支付返回数据处理,与 {@link #parseMicro} 相比, 对err_code的处理逻辑不同
     */
    protected void parseMicroUnion(ResultListener resultListener, Response response)
    {
        if (response.result != null && !TextUtils.isEmpty(response.getResult().toString()))
        {
            JSONObject object = JSONObject.parseObject(response.result.toString());
            if (object.containsKey("err_code") && object.getIntValue("err_code") == BatMicroRsp.ERROR_CODE)
            {//处理需要密码
                BatMicroRsp rsp = new BatMicroRsp();
                rsp.setMsg("请用户输入密码");
                Logger2.debug("被扫支付下单成功,进入轮询");
                resultListener.onFaild(new Response(1, rsp.getMsg()));
                looperQuery(resultListener);
            }else
            {
                BatMicroRsp rsp = JSONObject.parseObject(response.result.toString(), BatMicroRsp.class);
                Logger2.debug("被扫支付下单成功,支付成功");
                transactionInfo.setThirdTradeNo(rsp.getThirdTradeNo());
				transactionInfo.setPayType(rsp.getPayType());//移动支付对应的支付类型 wu
                printTransInfo();
                resultListener.onSuccess(new Response(0, "支付成功", bundleResult()));
            }
        }
    }

	protected void parseMicro(ResultListener resultListener, Response response) {
		if (response.result != null && !TextUtils.isEmpty(response.getResult().toString())) {
			String type=transactionInfo.getPayTypeFlag();
			if (Constants.ALIPAY_BAT.equals(type)||Constants.WEPAY__BAT.equals(type)) {
				JSONObject object = JSONObject.parseObject(response.result.toString());
				int code = response.code;
				if (code == 0) {
					String payType=transactionInfo.getPayTypeFlag();
					String codeWaitState="";
					if (!TextUtils.isEmpty(payType)) {
						if (payType.equals(Constants.ALIPAY_BAT)) {
							codeWaitState=BatMicroRsp.CODE_WAITE_ALIPAY;
						}else if (payType.equals(Constants.WEPAY__BAT)) {
							codeWaitState=BatMicroRsp.CODE_WAITE_WEPAY;
						}else if (payType.equals(Constants.TENPAY_BAT)) {
							codeWaitState=BatMicroRsp.CODE_WAITE_TENPAY;
						}else if (payType.equals(Constants.BAIDUPAY_BAT)) {
							codeWaitState=BatMicroRsp.CODE_WAITE_BAIDU;
						}
					}
					if (object.containsKey("err_code")
							&& codeWaitState.equals(object.get("err_code").toString())) {
						BatMicroRsp rsp = new BatMicroRsp();
						rsp.setRet(codeWaitState);
						rsp.setMsg("请用户输入密码");
						Logger2.debug("被扫支付下单成功,进入轮询");
						resultListener.onFaild(new Response(1, rsp.getMsg() + "[" + rsp.getRet() + "]"));
						looperQuery(resultListener);
					} else {
						BatMicroRsp rsp = JSONObject.parseObject(response.result.toString(), BatMicroRsp.class);
						Logger2.debug("被扫支付下单成功,支付成功");
						transactionInfo.setThirdTradeNo(rsp.getThirdTradeNo());
//						transactionInfo.settList(rsp.gettList());
						printTransInfo();
						resultListener.onSuccess(new Response(0, "支付成功", bundleResult()));
					}
				} else {
					Logger2.debug("被扫支付下单成功:支付失败");
					BatMicroRsp rsp = JSONObject.parseObject(response.result.toString(), BatMicroRsp.class);
					resultListener.onFaild(new Response(1, rsp.getMsg() + "[" + rsp.getRet() + "]"));
				}
			}else {
				BatMicroRsp rsp = JSONObject.parseObject(
						response.result.toString(), BatMicroRsp.class);
				if (BatMicroRsp.CODE_SUCCESS.equals(rsp.getRet())) {
					Logger2.debug("下单成功:支付成功");
					transactionInfo.setThirdTradeNo(rsp.getThirdTradeNo());
					printTransInfo();
					resultListener
							.onSuccess(new Response(0, "支付成功", bundleResult()));
				} else if (BatMicroRsp.CODE_WAITE_ALIPAY.equals(rsp.getRet())) {
					transactionInfo.setThirdTradeNo(rsp.getThirdTradeNo());
					resultListener.onFaild(new Response(1, rsp.getMsg() + "[" + rsp.getRet() + "]"));
					looperQuery(resultListener);
				} else if (BatMicroRsp.CODE_WAITE_WEPAY.equals(rsp.getRet())) {
					transactionInfo.setThirdTradeNo(rsp.getThirdTradeNo());
					resultListener.onFaild(new Response(1, rsp.getMsg() + "[" + rsp.getRet() + "]"));
					looperQuery(resultListener);
				} else if (BatMicroRsp.CODE_WAITE_TENPAY.equals(rsp.getRet())) {
					transactionInfo.setThirdTradeNo(rsp.getThirdTradeNo());
					resultListener.onFaild(new Response(1, rsp.getMsg() + "[" + rsp.getRet() + "]"));
					looperQuery(resultListener);
				} else if (BatMicroRsp.CODE_WAITE_BAIDU.equals(rsp.getRet())) {
					transactionInfo.setThirdTradeNo(rsp.getThirdTradeNo());
					resultListener.onFaild(new Response(1, rsp.getMsg() + "[" + rsp.getRet() + "]"));
					looperQuery(resultListener);
				} else {
					Logger2.debug("百度被扫支付下单成功:支付失败");
					resultListener.onFaild(new Response(1, rsp.getMsg() + "["
							+ rsp.getRet() + "]"));
				}
			}

		}
	}
	@Override
	public void pay(String authCode, ResultListener resultListener) {
		// TODO Auto-generated method stub

	}
	@Override
	public void getTicketInfo(String ticketNum, final ResultListener resultListener) {
		if(commonTicketManager == null)
		{
			commonTicketManager = TicketManagerFactory.createCommonTicketManager(context);
		}
		commonTicketManager.getTicketInfo(ticketNum, transactionInfo.getInitAmount(), new ResultListener() {

			@Override
			public void onSuccess(Response response) {
				GetCommonTicketInfoResp commonTicketInfoResp = (GetCommonTicketInfoResp) response.result;
//				if (commonTicketInfoResp.getWemengTicket() != null) {
//					wemengTicketInfo = commonTicketInfoResp.getWemengTicket();
//				}
				resultListener.onSuccess(new Response(0, "获取券信息成功", commonTicketInfoResp.getTicketInfo()));
			}

			@Override
			public void onFaild(Response response) {
				resultListener.onFaild(response);
			}
		});
	}
}
