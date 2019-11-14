package com.wizarpos.pay.cashier.presenter.ticket.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.base.net.Response;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.cashier.model.TicketDef;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.cashier.presenter.ticket.inf.WepayTicketOperater;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.pay.common.utils.Logger2;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;

/**
 * 微信券帮助类
 * 
 * @author wu
 */
public class WepayTicketOperaterImpl implements WepayTicketOperater {

	
	@Override
	public void getTicketDetial(final String wepayCode, final String shouldPayAmount,final ResultListener listener) {
		/*
		 * 流程: 二维码 -->token-->cardId--->TicktDef ---> 返回TicketInfo
		 */
		getAccessToken(new ResultListener() {// 获取token
			@Override
			public void onSuccess(Response response) {
				String token = response.result.toString();
				// AppConfigHelper.setConfig(AppConfigDef.wepay_token,
				// response.result.toString());//缓存token
				getWepayTicketCardId(wepayCode, token, new ResultListener() {// 获取cardId
							@Override
							public void onSuccess(Response response) {
								final String cardId = (String) response.result;// 拿到微信券的cardId
								getTicketDefByCardId(cardId,shouldPayAmount,
										new ResultListener() {// 获取券定义信息
											@Override
											public void onSuccess(
													Response response) {
												TicketDef ticketDef = (TicketDef) response.result;

												/*
												 * 将整个流程中取到的cardId,wepayCode,
												 * ticketDef拼成TicketInfo对象返回
												 */
												TicketInfo ticketInfo = new TicketInfo();
												ticketInfo
														.setTicketDef(ticketDef);
												ticketInfo
														.setWx_code(wepayCode);
												ticketInfo.setCardId(cardId);
												listener.onSuccess(new Response(
														0, "success",
														ticketInfo));
											}

											@Override
											public void onFaild(
													Response response) {
												listener.onFaild(response);
											}
										});
							}

							@Override
							public void onFaild(Response response) {
								listener.onFaild(response);
							}
						});
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}
	
	@Override
	public void getTicketDetial(final String wepayCode,
			final ResultListener listener) {
		getTicketDetial(wepayCode, "", listener);
	}

	/**
	 * 获取微信accessToken
	 */
	private void getAccessToken(final ResultListener listener) {
		NetRequest.getInstance().addRequest(Constants.SC_904_ACCESS_TOKEN,
				null, new ResponseListener() {

					@Override
					public void onSuccess(Response response) {
						JSONObject robj = (JSONObject) response.getResult();
						String accessToken = robj.getString("accessToken");
						if (TextUtils.isEmpty(accessToken)) {
							onFaild(null);
						} else {
							listener.onSuccess(new Response(0, "success",
									accessToken));
						}
					}

					@Override
					public void onFaild(Response response) {
						listener.onFaild(new Response(1, "获取token失败"));
					}
				});
	}

	/**
	 * 获取微信券的cardid <br>
	 * 调用此方法之前,建议调用{@link #getAccessToken(ResultListener)}
	 * 
	 * @param wxCode
	 *            二维码
	 * @param token
	 *            微信token
	 * @param listener
	 */
	private void getWepayTicketCardId(final String wxCode, String token,
			final ResultListener listener) {
		String url = "https://api.weixin.qq.com/card/code/get?access_token=" + token;
		Headers.Builder builder = new Headers.Builder();
		builder.add("Content-Type", "application/json");
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", wxCode);
		String reqParam = JSON.toJSONString(params, SerializerFeature.WriteDateUseDateFormat);
		NetRequest.getInstance().addThirdPostRequest(url, reqParam, builder.build(), null, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				listener.onFaild(new Response(-1, "请求失败!"));
			}

			@Override
			public void onResponse(Call call, okhttp3.Response response) throws IOException {
				JSONObject rjobj = JSON.parseObject(response.body().string());
				String errcode = rjobj.getString("errcode");
				// errcode是0 表示成功 这一步是获取券的card_id
				if ("0".equals(errcode)) {
					JSONObject card = rjobj.getJSONObject("card");
					if (card == null) {
						listener.onFaild(new Response(1, "获取微信cardId失败"));
					} else {
						String cardid = card.getString("card_id");
						if (TextUtils.isEmpty(cardid)) {
							listener.onFaild(new Response(1,
									"获取微信cardId失败"));
						} else {
							listener.onSuccess(new Response(0,
									"success", cardid));
						}
					}
				} else if ("40001".equals(errcode)
						|| "42001".equals(errcode)) {
					// handler.sendEmptyMessage(HANDLE_CODE_5);
					listener.onFaild(new Response(1, "token错误,请重试"));
				} else if ("40056".equals(errcode)) {
					listener.onFaild(new Response(1,
							"无效 code,获取微信cardId失败"));
				} else {
					// handler.sendEmptyMessage(HANDLE_CODE_4);
					listener.onFaild(new Response(1, "获取微信cardId失败"));
				}
			}
		});
	}

	/**
	 * 根据券的def 从券类别 获取一张 券
	 * 
	 * @param ticketDefId
	 *            微信会员卡二维码对应的字符串
	 * @param allTickets
	 *            会员卡下包含的所有券的
	 * @param addedTickets
	 *            已经添加过的券
	 * @return TicketInfo 券实体
	 */
	public TicketInfo getTicketByTicketDef(String ticketDefId,
			List<TicketInfo> allTickets, List<TicketInfo> addedTickets) {
		if (TextUtils.isEmpty(ticketDefId)) {
			return null;
		}
		/* XXX 如果有多张这种券,默认取第一张没有添加过的券(不太合理,后期应该修改这个逻辑) */
		for (TicketInfo _allticket : allTickets) {
			if (ticketDefId.equals(_allticket.getTicketDef().getId())) { // 所有券列表中有这张券
				boolean isContains = false;
				for (TicketInfo _addTicket : addedTickets) {// 判断这张券有没有被添加,如果这张券已添加,继续遍历,否则返回这张券
					if (_addTicket.getId().equals(_allticket.getId())) {
						isContains = true;
						break;
					}
				}
				if (isContains) {
					return null;
				} else
					return _allticket;
			}
		}
		return null;
	}
	
	
	/**
	 * 根据cardId 到后台 获取微信券的定义信息
	 * 
	 * @param cardId
	 *            微信cardId
	 */
	public void getTicketDefByCardId(String cardId,String shouldPayAmount,final ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("wxCodeId", cardId);
		params.put("shouldPayAmount", shouldPayAmount);
		NetRequest.getInstance().addRequest(
				Constants.SC_519_WECHAT_TTICKET_DEF_QUERY_NO_MEMBER, params,
				new ResponseListener() {

					@Override
					public void onSuccess(Response response) {
						TicketDef ticketDef = JSONObject.parseObject(
								response.result.toString(), TicketDef.class);
						listener.onSuccess(new Response(0, "success", ticketDef));
					}

					@Override
					public void onFaild(Response response) {
						listener.onFaild(new Response(1, response.msg));
					}
				});
	}

	/**
	 * 根据cardId 到后台 获取微信券的定义信息
	 * 
	 * @param cardId
	 *            微信cardId
	 */
	private void getTicketDefByCardId(String cardId,
			final ResultListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("wxCodeId", cardId);
		NetRequest.getInstance().addRequest(
				Constants.SC_519_WECHAT_TTICKET_DEF_QUERY_NO_MEMBER, params,
				new ResponseListener() {

					@Override
					public void onSuccess(Response response) {
						TicketDef ticketDef = JSONObject.parseObject(
								response.result.toString(), TicketDef.class);
						listener.onSuccess(new Response(0, "success", ticketDef));
					}

					@Override
					public void onFaild(Response response) {
						listener.onFaild(new Response(1, response.msg));
					}
				});
	}

	/**
	 * 判断券列表中是否已经还有了微信券
	 * 
	 * @param wxcode
	 *            微信会员卡二维码对应的字符串
	 * @param tickets
	 *            券列表
	 */
	public boolean isAddWepayTicket(String wxcode, List<TicketInfo> tickets) {
		if (TextUtils.isEmpty(wxcode)) {
			return false;
		}
		for (TicketInfo ticket : tickets) {
			if (wxcode.equals(ticket.getWx_code())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断券列表中是否包含着这种券
	 * 
	 * @param ticketDefId
	 *            微信会员卡二维码对应的字符串
	 * @param tickets
	 *            券列表
	 * @return TicketInfo 券实体
	 */
	public boolean isContainsTicketDef(String ticketDefId,
			List<TicketInfo> tickets) {
		if (TextUtils.isEmpty(ticketDefId)) {
			return false;
		}
		for (TicketInfo ticket : tickets) {
			if (ticketDefId.equals(ticket.getTicketDef().getId())) { // 包含这种券
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取微信券的打印信息
	 */
	public String getWepayPrintInfo(List<TicketInfo> commonTickets) {
		Q1PrintBuilder builder = new Q1PrintBuilder();
		int WePayNum = 0;
		for (TicketInfo ticket : commonTickets) {
			if (("1".equals(ticket.getTicketDef().getWxFlag()) || "3"
					.equals(ticket.getTicketDef().getWxFlag()))) {
				WePayNum ++;
			}
		}
		if (commonTickets == null || commonTickets.isEmpty() || WePayNum <= 0) {
			return "";
		}
		String printString = "";
		for (TicketInfo ticket : commonTickets) {
			if (("1".equals(ticket.getTicketDef().getWxFlag()) || "3"
					.equals(ticket.getTicketDef().getWxFlag()))) {// 判读是不是微信券或朋友券
				if (TextUtils.isEmpty(printString)) {
					printString = builder.center(builder.bold("微信券使用"))
							+ builder.br() + builder.branch();
					
					printString += "时间:" + DateUtil.format(System.currentTimeMillis(), DateUtil.P2) + builder.br();//券使用 打印时间 @yaosong [20151026]
					printString += builder.branch();
					
					printString += "名称" + builder.br() + builder.tab("金额(元)")
							+ builder.br();
					printString += builder.branch();
				}
				printString += builder.normal(ticket.getTicketDef()
						.getTicketName())
						+ builder.br()
						+ builder.tab(Tools.formatFen(ticket.getTicketDef().getBalance()) + "")
						+ builder.br();
			}
		}
		return printString;
	}

	/**
	 * 批量核销微信券
	 */
	@Override
	public void passWepay(List<TicketInfo> tickets, ResultListener listener) {
		if (tickets == null || tickets.isEmpty()) {
			listener.onSuccess(new Response(0, "success"));
			return;
		}
		UploadTransTask task = new UploadTransTask(tickets, listener);
		task.execute();
	}

	@Override
	public void pass(final TicketInfo ticket, final ResultListener listener) {
		if (ticket == null) {
			listener.onFaild(new Response(1, "操作失败"));
			return;
		}
		if (!"1".equals(ticket.getTicketDef().getWxFlag())) {
			listener.onFaild(new Response(1, "这张券不是微信券"));
			return;
		}
		getAccessToken(new ResultListener() {// 获取token
			@Override
			public void onSuccess(Response response) {
				pass(ticket.getWx_code(), ticket.getCardId(),
						response.result.toString(), listener);
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}

	/**
	 * 核销微信券
	 * 
	 * @param wxCode
	 *            二维码
	 * @param cardId
	 *            微信cardId
	 * @param accessToken
	 *            token
	 * @param listener
	 */
	private void pass(final String wxCode, final String cardId,
			final String accessToken, final ResultListener listener) {
		Headers.Builder builder = new Headers.Builder();
		builder.add("Content-Type", "application/json");
		String url = "https://api.weixin.qq.com/card/code/consume?access_token=" + accessToken;
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", wxCode);
		params.put("card_id", cardId);
		String reqParam = JSON.toJSONString(params, SerializerFeature.WriteDateUseDateFormat);
		NetRequest.getInstance().addThirdPostRequest(url, reqParam, builder.build(), null, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				listener.onFaild(new Response(-1, "请求失败!"));
			}

			@Override
			public void onResponse(Call call, okhttp3.Response response) throws IOException {
				JSONObject jsonObj = JSON.parseObject(response.body().string());
				String errcode = jsonObj.getString("errcode");
				if (errcode.equals("0")) {
					listener.onSuccess(new Response(0, "success"));
				} else if (errcode.equals("40001")
						|| errcode.equals("42001")) {
					// handler.sendEmptyMessage(HANDLE_CODE_5);
					listener.onFaild(new Response(1, "获取微信card_id失败"));
				} else {
					// handler.sendEmptyMessage(HANDLE_CODE_4);
					listener.onFaild(new Response(1, "获取微信card_id失败"));
				}
			}
		});
	}

	/**
	 * 批量进行微信卡券核销的内部类
	 * 
	 * @author wu
	 */
	class UploadTransTask extends AsyncTask<Void, Void, Void> {

		private List<TicketInfo> tikcets;
		private int position = 0;
		private boolean isUploadFinish;
		private ResultListener listener;

		public UploadTransTask(List<TicketInfo> tickets, ResultListener listener) {
			this.tikcets = tickets;
			this.listener = listener;
			position = -1;
			isUploadFinish = false;
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (tikcets == null || tikcets.isEmpty()) {
				return null;
			}
			continueUpload();
			while (!isUploadFinish) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		private void continueUpload() {
			position++;
			if (position < tikcets.size()) {
				pass(tikcets.get(position), new ResultListener() {
					@Override
					public void onSuccess(Response response) {
						Logger2.debug("微信卡券核销:" + response.msg);
					}

					@Override
					public void onFaild(Response response) {
						Logger2.debug("微信卡券核销:" + response.msg);
					}
				});
			} else {
				isUploadFinish = true;
			}
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			if (listener != null)
				listener.onSuccess(new Response(0, "success"));
		}
	}

}
