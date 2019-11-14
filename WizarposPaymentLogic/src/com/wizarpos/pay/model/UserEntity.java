package com.wizarpos.pay.model;

import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * @Author: yaosong
 * @date 2016-2-17 上午9:59:29
 * @Description:用户(操作员)实体 ,放在服务端维护
 */
@Table(name = "cashierOpreator")
public class UserEntity implements Serializable{

	private String id;

	private String name;

	private String password;

	private String email;

	private String createTime;

	private String loginName;

	private String enable;

	/**	1管理员,3店长,4操作员 可以登陆POS,1管理员类似原00账号;2 财务	**/
	private String adminFlag;

	private long lastTime;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getEnable() {
		return this.enable;
	}

	public void setAdminFlag(String adminFlag) {
		this.adminFlag = adminFlag;
	}

	public String getAdminFlag() {
		return this.adminFlag;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public long getLastTime() {
		return this.lastTime;
	}

}
