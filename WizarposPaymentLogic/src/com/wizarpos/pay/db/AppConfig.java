package com.wizarpos.pay.db;

import com.lidroid.xutils.db.annotation.Table;

@Table(name = "tb_config")
public class AppConfig {
	private int id;
	private String config_name;
	private String config_value;

	public AppConfig() {
	}

	public AppConfig(String config_name, String config_value) {
		super();
		this.config_name = config_name;
		this.config_value = config_value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getConfig_name() {
		return config_name;
	}

	public void setConfig_name(String config_name) {
		this.config_name = config_name;
	}

	public String getConfig_value() {
		return config_value;
	}

	public void setConfig_value(String config_value) {
		this.config_value = config_value;
	}

}
