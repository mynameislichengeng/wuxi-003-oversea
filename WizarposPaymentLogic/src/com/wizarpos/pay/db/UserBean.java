package com.wizarpos.pay.db;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Table;

@Table(name = "cashier")
public class UserBean implements Serializable {
    private int id;
    private String operId;// 用户名
    private String password;// 密码
    private String realName;
    private String phone;
    private int type;// 0 普通用户 1: 管理员 2:超级管理员

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
