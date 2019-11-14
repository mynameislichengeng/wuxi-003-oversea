package com.wizarpos.pay.statistics.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.common.Constants;

public class GroupQueryResp {

	private List<String[]> recordList = new ArrayList<String[]>(); // 所有交易记录(包含支付交易和券交易)
	private List<String[]> recordListShow = new ArrayList<String[]>(); // 用来显示的交易记录

	private List<String[]> recordTicketListSum = new ArrayList<String[]>();// 券记录,包含撤销的和使用的

	private List<GroupQueryPay> queryPayList = new ArrayList<GroupQueryPay>();//将对象存储在集合里,方便首页传递tranCode @Hwc
	
	private String currentPrintTime;//616接口 开始时间
	private String lastPrintTime;//616接口 结束时间
	
	private long sumTran = 0;// 总金额
	private int countTran = 0;// 总笔数

	public void handleResult(JSONObject tranLogSum) {
		// 查询
		sumTran = 0;
		countTran = 0;
		String getListName = "";
		String getTicketListName = "";
		String membertranLogId = "";
		String tickettranLogId = "";
		currentPrintTime = "";
		lastPrintTime = "";
		recordList.clear();
		recordListShow.clear();
		recordTicketListSum.clear();
		if(tranLogSum.containsKey("currentPrintTime") && tranLogSum.containsKey("lastPrintTime"))
		{
			currentPrintTime = DateUtil.format(tranLogSum.getLong("currentPrintTime"), DateUtil.P4);
			lastPrintTime = DateUtil.format(tranLogSum.getLong("lastPrintTime"), DateUtil.P4);
		}
		JSONArray payJsonArray = tranLogSum.getJSONArray("pay");
		JSONArray ticketJsonArray = tranLogSum.getJSONArray("ticket");
		if (payJsonArray.size() < 1 && ticketJsonArray.size() < 1) {
			return;
		}
		List<GroupQueryPay> pays = JSONArray.parseArray(
				tranLogSum.getString("pay"), GroupQueryPay.class);
		try {
			Collections.sort(pays, new Comparator<GroupQueryPay>() {

				@Override
				public int compare(GroupQueryPay lhs, GroupQueryPay rhs) {
					if (lhs.getTranCode() > rhs.getTranCode()) {
						return 1;
					} else if (lhs.getTranCode() == rhs.getTranCode()) {
						return 0;
					} else {
						return -1;
					}
				}
			});
			this.queryPayList = pays;
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (GroupQueryPay pay : pays) {
			String transTypeDes = Constants.TRAN_TYPE.get(pay.getTranCode() + "");
            if ((pay.getTranCode()+"").equals("815")) {//威富通微信支付 @yaosongqwe[20151109]
            	transTypeDes = "威富通微信支付";
			}else if ((pay.getTranCode()+"").equals("817")) {//威富通支付宝支付 
            	transTypeDes = "威富通支付宝支付";
			}else if((pay.getTranCode()+"").equals("821")){//威富通QQ钱包支付
            	transTypeDes = "威富通QQ钱包支付";
			}else if ((pay.getTranCode()+"").equals("816")) {//威富通微信支付撤销
            	transTypeDes = "威富通微信支付撤销";
			}else if ((pay.getTranCode()+"").equals("818")) {//威富通支付宝支付撤销
            	transTypeDes = "威富通支付宝支付撤销";
			}else if ((pay.getTranCode()+"").equals("822")) {//威富通QQ钱包支付撤销
            	transTypeDes = "威富通QQ钱包支付撤销";
			}
			String[] tempStrs = new String[] {transTypeDes,	pay.getTcount(),
					Tools.formatFen(pay.getTotal()), };
			
			List<String> typeArray = new ArrayList<String>();
			typeArray.add("威富通微信支付");
			typeArray.add("威富通支付宝支付");
			typeArray.add("威富通QQ钱包支付");
			typeArray.add("威富通微信支付撤销");
			typeArray.add("威富通支付宝支付撤销");
			typeArray.add("威富通QQ钱包支付撤销");
			typeArray.addAll(Arrays.asList(Constants.kinds));
			
			if (typeArray.contains(tempStrs[0])) {
				try {
					for (int ii = 0; ii < ticketJsonArray.size(); ii++) {
						membertranLogId = pay.getTranLogId();
						tickettranLogId = ticketJsonArray.getJSONObject(ii)
								.getString("tranLogId");
						if (TextUtils.isEmpty(membertranLogId)
								|| TextUtils.isEmpty(tickettranLogId)) {
							continue;
						}
						if (membertranLogId.equals(tickettranLogId)) {
							if (!tempStrs[0].contains("(券)")) {
								tempStrs[0] = tempStrs[0] + "  (券)";
							}
						}
					}
					recordList.add(tempStrs);// 维护支付交易记录
					if (!isAlreadyContainType(tempStrs[0],getListName)) {//修改判断条件@yaosong[20151208]
						getListName += tempStrs[0] + "|";
					}
//					if (!getListName.contains(tempStrs[0])) {// 维护支付交易类型
//						getListName += tempStrs[0] + "|";
//					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		String[] strArray = getListName.split("\\|");// 将交易类型记录分割为数组
		for (String selectKind : strArray) {
			int selectKindCount = 0;
			long selectKindSum = 0;
			for (int iiii = 0; iiii < recordList.size(); iiii++) {
				String[] recordListCount = recordList.get(iiii);
				if (recordListCount[0].equals(selectKind)) {
					selectKindCount += Integer.valueOf(recordListCount[1]);// 计算某种交易的总笔数
					selectKindSum += Tools.toIntMoney(recordListCount[2]);// 计算某种交易的总金额
				}
			}
			String[] selectKindAdd = new String[] { selectKind,
					String.valueOf(selectKindCount),
					Tools.formatFen(selectKindSum), };
			recordListShow.add(selectKindAdd);// 显示出来的记录{交易类型--笔数--交易金额}
			sumTran += selectKindSum; // 总金额维护
			countTran += selectKindCount; // 总笔数维护
		}
		sumTran = 0;
		// -----------------------------

		for (int i = 0; i < ticketJsonArray.size(); i++) {
			JSONObject record = ticketJsonArray.getJSONObject(i);
			String[] tempStrs = new String[] {
					record.getString("ticketName"),
					record.getString("tcount"),
					Tools.formatFen(record.getLong("total") == null ? 0
							: record.getLong("total")), };
			recordList.add(tempStrs);// 维护券使用记录
			if (!isAlreadyContainType(tempStrs[0],getTicketListName)) {//修改判断条件@yaosong[20151208]
				getTicketListName += tempStrs[0] + "|";
			}
//			if (!getTicketListName.contains(tempStrs[0])) {// 维护券类型
//				getTicketListName += tempStrs[0] + "|";
//			}
		}
		String[] strTicketArray = getTicketListName.split("\\|");// 将券记录分割为数组
		if(TextUtils.isEmpty(getTicketListName))
		{
			return;
		}
		for (int iii = 0; iii < strTicketArray.length; iii++) {
			String selectKind = strTicketArray[iii];
			int selectKindCount = 0;
			long selectKindSum = 0;
			for (int iiii = 0; iiii < recordList.size(); iiii++) {
				String[] recordListCount = recordList.get(iiii);
				if (recordListCount[0].equals(selectKind)) {
					selectKindCount += Integer.valueOf(recordListCount[1]);// 计算某种券的总笔数
					selectKindSum += Tools.toIntMoney(recordListCount[2]);// 计算某种券的总金额
				}
			}
			String[] selectKindAdd = new String[] { selectKind,
					String.valueOf(selectKindCount),
					Tools.formatFen(selectKindSum), };
			recordTicketListSum.add(selectKindAdd);// 券记录,包含撤销的和使用的
		}

	}

	/**
	 * 获取总笔数
	 * 
	 * @return
	 */
	public int getCountTran() {
		return countTran;
	}

	/**
	 * 获取总金额
	 * 
	 * @return
	 */
	public long getSumTran() {
		return sumTran;
	}

	/**
	 * 获取所有的交易记录
	 * 
	 * @return
	 */
	public List<String[]> getRecordList() {
		return recordList;
	}

	/**
	 * 获取用来展现的交易记录
	 * 
	 * @return
	 */
	public List<String[]> getRecordListShow() {
		return recordListShow;
	}

	/**
	 * 获取用券记录
	 * 
	 * @return
	 */
	public List<String[]> getRecordTicketListSum() {
		return recordTicketListSum;
	}
	
	/**
	 * 
	 * @Author: Huangweicai
	 * @date 2015-11-9 下午6:09:53 
	 * @return 
	 * @Description: 获得券和交易记录list
	 */
	public List<String[]> getLAWSONList()
	{
		if(recordTicketListSum == null)
		{
			return recordListShow;
		}
		if(recordListShow == null)
		{
			return recordListShow;
		}
		recordListShow.addAll(recordTicketListSum);
		return recordListShow;
	}

	/**
	 * 获取对象集合 @Hwc
	 * 
	 */
	public List<GroupQueryPay> getQueryPayList() {
		return queryPayList;
	}

	public String getCurrentPrintTime() {
		return currentPrintTime;
	}


	/**
	 * 获得最后打印时间 @Hwc
	 * 
	 */
	public String getLastPrintTime() {
		return lastPrintTime;
	}

	/**
	 * @author Song
	 * @param type
	 * @param getListName
	 * @return
	 */
	private boolean isAlreadyContainType(String type,String getListName) {
		String[] strArray = getListName.split("\\|");
		for (int i = 0; i < strArray.length; i++) {
			if (type.equals(strArray[i])) {
				return true;
			}
		}
		return false;
	}
	
}
