package com.wizarpos.pay.common;

public class LastPrintBean {
	public final static int TYPE_STRING = 1;// String
	public final static int TYPE_BITMAP = 2;// bitmao

	private int type;
	private String content;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
