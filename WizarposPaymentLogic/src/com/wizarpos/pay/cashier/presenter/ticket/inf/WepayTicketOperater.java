package com.wizarpos.pay.cashier.presenter.ticket.inf;

import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;

import java.util.List;

/**
 * 微信券操作类 Created by wu on 2015/4/13 0013.
 */
public interface WepayTicketOperater extends TicketOperater {

	/**
	 * 判断券列表中是否已经还有了微信券
	 * 
	 * @param wxcode
	 *            微信会员卡二维码对应的字符串
	 * @param tickets
	 *            券列表
	 */
	boolean isAddWepayTicket(String wxcode, List<TicketInfo> tickets);

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
	TicketInfo getTicketByTicketDef(String ticketDefId,
			List<TicketInfo> allTickets, List<TicketInfo> addedTickets);

	/**
	 * 判断券列表中是否包含着这种券
	 * 
	 * @param ticketDefId
	 *            微信会员卡二维码对应的字符串
	 * @param tickets
	 *            券列表
	 * @return TicketInfo 券实体
	 */
	boolean isContainsTicketDef(String ticketDefId, List<TicketInfo> tickets);

	/**
	 * 获取微信券的打印信息
	 */
	String getWepayPrintInfo(List<TicketInfo> memberTickets);

	/**
	 * 批量核销微信券
	 */
	void passWepay(List<TicketInfo> ticketInfos,
			BasePresenter.ResultListener listener);
	
	/**
	 * 获取券的详细信息(需要传入应付金额,507 折扣券)
	 * 
	 * @param code
	 *            券唯一标识
	 * @param listener
	 */
	void getTicketDetial(String code,String shouldPayAmount, BasePresenter.ResultListener listener);
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2016-1-5 下午2:05:19 
	 * @param cardId 卡号
	 * @param shouldPayAmount 应付金额
	 * @param listener 
	 * @Description:获得微信券信息
	 */
	void getTicketDefByCardId(String cardId,String shouldPayAmount,final ResultListener listener);
}
