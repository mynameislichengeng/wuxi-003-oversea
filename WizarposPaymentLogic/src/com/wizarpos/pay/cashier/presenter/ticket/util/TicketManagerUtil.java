package com.wizarpos.pay.cashier.presenter.ticket.util;

import com.wizarpos.base.net.Response;
import com.wizarpos.log.util.StringUtil;
import com.wizarpos.pay.cashier.model.TicketDef;
import com.wizarpos.pay.cashier.model.TicketInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-12-28 下午3:31:20
 * @Description:券管理工具类
 */
public class TicketManagerUtil {
	/** 添加券返回结果start*/
	//券为空
	protected static final int RESPONSE_TICKET_NULL = -1;
	//添加成功
	protected static final int RESPONSE_TICKET_SUCCESS = 0;
	//已经有券
	protected static final int RESPONSE_TICKET_CONTAIN = 1;
	//不同类型的券不能添加
	protected static final int RESPONSE_TICKET_DIFF_TYPE = -2;
	/** 添加券返回结果end*/

	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-29 上午10:59:05 
	 * @param amount
	 * @param cardType
	 * @return 
	 * @Description:接口 793返回
	 */
	public static TicketInfo getTicketInfoFromWeTicket(String amount,String cardType)
	{
		TicketInfo ticketInfo = new TicketInfo();
		TicketDef ticketDef = new TicketDef();
		ticketDef.setBalance(Integer.valueOf(amount));
		ticketInfo.setBalance(Integer.valueOf(amount));
		if(cardType.equals("DISCOUNT"))
		{
			ticketDef.setTicketType(TicketDef.TICKET_TYPE_DISCOUNT);
		}else {
			ticketDef.setTicketType("");
		}
		ticketInfo.setTicketDef(ticketDef);
		
		return ticketInfo;
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-12-28 下午3:36:22 
	 * @param ticketInfo 需要添加的券
	 * @param ticketList 已经添加的券集合
	 * @Description:本地检查添加的券是否符合(券改造) {@link MemberCardPayActivity}
	 */
	public static Response verifyAddTicket(TicketInfo ticketInfo,List<TicketInfo> ticketAddedList)
	{
		Response response = new Response();
		if(ticketInfo == null)
		{
			response.code = RESPONSE_TICKET_NULL;
			response.msg = "添加券";
			return response;
		}
		if(ticketAddedList.contains(ticketInfo))
		{
			response.code = RESPONSE_TICKET_CONTAIN;
			response.msg = "已使用该券，不能重复添加";
			return response;
		}
		String ticketType = ticketInfo.getTicketDef().getTicketType();
		if(ticketType.equals(TicketDef.TICKET_TYPE_DISCOUNT))
		{//如果是折扣券
			if(ticketAddedList.size()>0)
			{
				response.code = RESPONSE_TICKET_DIFF_TYPE;
				response.msg = "折扣券只能单独使用";
				return response;
			}
		}else
		{//如果是代金券
			for(TicketInfo ticketBean:ticketAddedList)
			{
				if(ticketBean.getTicketDef().getTicketType().equals(TicketDef.TICKET_TYPE_DISCOUNT))
				{
					response.code = RESPONSE_TICKET_DIFF_TYPE;
					response.msg = "类型不同的券不能同时使用";
					return response;
				}
			}
		}
		response.code = RESPONSE_TICKET_SUCCESS;
		response.msg = "添加成功";
		return response;
	}
	
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2016-1-5 下午7:25:51 
	 * @param ticket 需要添加的ticketDef
	 * @param ticketList 已经添加集合
	 * @return 
	 * @Description:判断券是否能添加
	 */
	public static Response addTicketDef(TicketDef ticket,ArrayList<TicketDef> ticketList)
	{
		Response response = new Response();
		if(ticket == null)
		{
			response.code = RESPONSE_TICKET_NULL;
			response.msg = "添加券";
			return response;
		}
		for(TicketDef bean:ticketList)
		{
			if(StringUtil.isSameString(bean.getTicketId(),ticket.getTicketId()))
			{
				response.code = RESPONSE_TICKET_CONTAIN;
				response.msg = "已使用该券,不能重复添加";
				return response;
			}
		}
		String ticketType = ticket.getTicketType();
		if(ticketType.equals(TicketDef.TICKET_TYPE_DISCOUNT))
		{//如果是折扣券
			if(ticketList.size()>0)
			{
				response.code = RESPONSE_TICKET_DIFF_TYPE;
				response.msg = "折扣券只能单独使用";
				return response;
			}
		}else
		{//如果是代金券
			for(TicketDef ticketBean:ticketList)
			{
				if(ticketBean.getTicketType().equals(TicketDef.TICKET_TYPE_DISCOUNT))
				{
					response.code = RESPONSE_TICKET_DIFF_TYPE;
					response.msg = "类型不同的券不能同时使用";
					return response;
				}
			}
		}
		response.code = RESPONSE_TICKET_SUCCESS;
		response.msg = "添加成功";
		return response;
	}

}
