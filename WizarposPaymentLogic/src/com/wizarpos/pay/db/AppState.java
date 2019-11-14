package com.wizarpos.pay.db;

import com.lidroid.xutils.db.annotation.Table;

@Table(name = "tb_state")
public class AppState {
	private int id;
	private String state_name;
	private String state_value;

	public AppState() {
	}

	public AppState(String state_name, String state_value) {
		super();
		this.state_name = state_name;
		this.state_value = state_value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	public String getState_value() {
		return state_value;
	}

	public void setState_value(String state_value) {
		this.state_value = state_value;
	}

}
