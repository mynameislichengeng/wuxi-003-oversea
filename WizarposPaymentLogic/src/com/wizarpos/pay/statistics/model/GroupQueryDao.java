package com.wizarpos.pay.statistics.model;

import com.wizarpos.pay.common.Constants;

import java.util.ArrayList;
import java.util.List;

public class GroupQueryDao {

	List<GroupQueryBean> groupQueryResults = new ArrayList<GroupQueryBean>();

	public void clear() {
		groupQueryResults.clear();
	}

	public List<GroupQueryBean> getGroupQueryResults() {
		return groupQueryResults;
	}

	/**
	 * 向交易汇总记录中添加一笔交易,如果汇总记录中有这笔交易,则将增加相应的金额和数量,否则新建一种记录
	 * 
	 * @param transRecord
	 */
	public void addTransRecord(TransRecord transRecord) {
		String transKind = Constants.TRAN_TYPE.get(transRecord.getTranCode());// 交易类型
		for (GroupQueryBean result : groupQueryResults) {
			if (result.getTransKind().equals(transKind)) {// 已经有了同类型的记录
				result.addTransAmount(transRecord.getTotal());// 增加交易金额
				result.addTransCount(transRecord.getTcount());// 增加交易数量
				return;
			}
		}
		groupQueryResults.add(new GroupQueryBean(transKind, transRecord
				.getTotal(), transRecord.getTcount()));
	}

	/**
	 * 汇总处理交易交易记录
	 * 
	 * @param transRecords
	 */
	public void handleTransRecords(List<TransRecord> transRecords) {
		for (TransRecord record : transRecords) {
			addTransRecord(record);
		}
	}
}
