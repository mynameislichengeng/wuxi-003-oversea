package com.wizarpos.pay.cashier.presenter.ticket.inf;

import java.util.List;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.model.ThirdTicketInfo;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.common.base.BasePresenter;

/**
 * 券管理 Created by wu on 2015/4/11 0011.
 */
public interface TicketManager {

	/**
	 * 添加券
	 * 
	 * @param ticket
	 *            券实体
	 * @param isFromscan
	 *            是否是扫码添加
	 */
	Response addTicket(TicketInfo ticket, boolean isFromscan);

	/**
	 * 移除券
	 * 
	 * @param tickek
	 *            券实体
	 */
	boolean removeAddedTicket(TicketInfo tickek);

	/**
	 * 移除券
	 * 
	 * @param ticketId
	 *            券唯一标识
	 */
	boolean removeAddedTicket(String ticketId);

	/**
	 * 获取已添加的券
	 */
	List<TicketInfo> getAddedTickets();

	/**
	 * 核销已添加的券
	 */
	void passAddedTickets(BasePresenter.ResultListener listener);

	/**
	 * 获取券的信息(后台拉取)
	 * 
	 * @param ticketNum
	 *            券唯一标识
	 * @param listener
	 *            回调
	 */
	void getTicketInfo(String ticketNum, String amount, final BasePresenter.ResultListener listener);
	/**
	 * 获取券的信息(后台 传入应付金额)
	 * 
	 * @param ticketNum
	 *            券唯一标识
	 * @param listener
	 *            回调
	 */
	void getTicketInfo(String ticketNum, String amount,String shouldPayAmount, final BasePresenter.ResultListener listener);

	/**
	 * @param isThirdPay	是否第三方支付扫券 "Y"是 "N"否
	 * @param isPaying		是否含支付0否1是
	 * @param shouldPayAmount 应收金额
	 */
	void getTicketInfo(String ticketNum, String amount, final BasePresenter.ResultListener listener, String isThirdPay, String isPaying, String shouldPayAmount);
	
	/**
	 * 是否已添加这张券
	 * 
	 * @param ticketNum
	 *            券唯一标识
	 * @return
	 */
	boolean isAddedTicket(String ticketNum);

	/**
	 * 打印券
	 */
	void printTickets(List<TicketInfo> tickets);

	/**
	 * 拉取第三方卡券信息
	 * 
	 * @param ticketNum
	 * @param listener
	 */
	void getThirdTicketInfo(String ticketNum, final BasePresenter.ResultListener listener);

	/**
	 * 核销第三方卡券
	 * 
	 * @param ticket
	 * @param listener
	 */
	void passThirdTicket(ThirdTicketInfo ticket, final BasePresenter.ResultListener listener);

	/**
	 * 微盟券核销
	 * @param ticket
	 * @param listener
	 */
	void passWeimengTicket(TicketInfo ticket, final BasePresenter.ResultListener listener);

	/**
	 * 微信券核销
	 * 
	 * @param ticketNum
	 * @param listener
	 */
	void passWepayTicket(String ticketNum, final BasePresenter.ResultListener listener);

	/**
	 * 微信券核销
	 *
	 * @param ticketNum
	 * @param listener
	 */
	void passWizarGiftTicket(String ticketNum, final BasePresenter.ResultListener listener);

	void printWizarGiftTicket(TicketInfo ticketInfo);
	/**
	 * 过滤微信ticketQrcode,根据ticketQrcode请求图片
	 * @param ticketInfos
	 * @param listener
	 */
	void filterWepayQRTicket(List<TicketInfo> ticketInfos,final BasePresenter.ResultListener listener);

	/**
	 * 当前券列表中是否包含微盟券
	 * @return
	 */
	boolean isContainsWemengTicket();
	
	/**
	 * 获取已经添加的微盟券的券号
	 * @return
	 */
	String getAddedWemengTicketNo();
	
	/**
	 * 验证券是否能用 507
	 * @param ticketNo 券号
	 * @param shouldPayAmount 应付金额
	 * @return
	 */
	void verifyTicket(String ticketNo,String shouldPayAmount,final BasePresenter.ResultListener listener);

}
