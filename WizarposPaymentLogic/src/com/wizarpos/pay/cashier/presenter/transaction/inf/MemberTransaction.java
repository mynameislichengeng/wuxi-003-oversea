package com.wizarpos.pay.cashier.presenter.transaction.inf;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.cashier.model.MemberCardInfoBean;
import com.wizarpos.pay.cashier.model.TicketInfo;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员卡交易 Created by wu on 2015/4/13 0013.
 */
public interface MemberTransaction extends Transaction {

	public interface MemberTransactionListener {
		/**
		 * 加载到券列表
		 */
		void onInit(Response response);
	}

	/**
	 * 添加卡券按钮 / 券扫描
	 * 
	 * @param ticketNo
	 *            券唯一标识
	 */
	void getCommonTicketInfo(String ticketNo, final BasePresenter.ResultListener listener);

	/**
	 * 添加普通券按钮
	 */
	Response addCommonTicket(TicketInfo ticket, boolean isFromScan);

	/**
	 * 获取微信券信息
	 * 
	 * @param wepayCode
	 *            微信卡的二维码对应的字符串
	 */
	void getWepayTicketInfo(final String wepayCode,
			final BasePresenter.ResultListener listener);

	/**
	 * 添加微信券
	 * 
	 * @param ticket
	 *            券实体
	 * @param isFromScan
	 *            是否扫码添加
	 * @param listener
	 *            回调
	 */
	void addWepayTicket(TicketInfo ticket, boolean isFromScan,
			final BasePresenter.ResultListener listener);

	/**
	 * 添加会员券
	 * 
	 * @param ticket
	 *            券实体
	 */
	Response addMemberTicket(TicketInfo ticket, boolean isFromScan);

	/**
	 * 删除会员券
	 * 
	 * @param ticket
	 *            券实体
	 */
	boolean removeMemberTicket(TicketInfo ticket);

	/**
	 * 清空所有已添加的会员券
	 */
	void removeAllMemberTciket();

	/**
	 * 获取已添加的会员券
	 * 
	 * @return 已添加会员券列表
	 */
	List<TicketInfo> getAddedMemberTickets();

	/**
	 * 获取所有会员券
	 * 
	 * @return 所有会员券列表
	 */
	List<TicketInfo> getAllMemberTickets();
	
	public ArrayList<TicketInfo> getAddTicketList();
	
	MemberCardInfoBean getMemeberCardInfo();
	
	/**
     * 核销组合支付微信券
     * @param infos
     * @param listener
     */
    void passMixMemberTickets(List<TicketInfo> infos,ResultListener listener);
}
