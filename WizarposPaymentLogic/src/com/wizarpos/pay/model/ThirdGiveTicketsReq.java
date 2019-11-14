package com.wizarpos.pay.model;

import java.io.Serializable;

/**
 * 
 * @ClassName: ThirdGiveTicketsReq 
 * @author Huangweicai
 * @date 2015-9-29 下午4:29:48 
 * @Description: 营销二期请求实体 872接口
 */
public class ThirdGiveTicketsReq implements Serializable {
	private String tranLogId;

	public String getTranLogId() {
		return tranLogId;
	}

	public void setTranLogId(String tranLogId) {
		this.tranLogId = tranLogId;
	}
}
