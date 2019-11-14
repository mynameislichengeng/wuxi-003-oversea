package com.wizarpos.pay.manager.presenter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.db.UserBean;
import com.wizarpos.pay.db.UserDao;

public class CashierManager extends BasePresenter {

    private UserDao userDao;

    public CashierManager(Context context) {
        super(context);
        userDao = UserDao.getInstance();
    }

    /**
     * 获取所有收银员
     *
     * @return
     */
    public List<UserBean> getAllCashier() {
        return userDao.getAllUsers();
    }

    /**
     * 增加收银员
     *
     * @param user
     */
    public void addCashier(UserBean user) {
        userDao.addUser(user);
    }

    /**
     * 移除收银员
     *
     * @param user
     */
    public void removeCashier(UserBean user) {
        if (user == null || TextUtils.isEmpty(user.getOperId())) {
            return;
        }
        userDao.deleteUser(user.getOperId());
    }

    /**
     * 移除收银员
     *
     * @param name
     *            用户名
     */
    public void removeCashier(String name) {
        userDao.deleteUser(name);
    }

    //
    // /**
    // * 更新管理员信息
    // *
    // * @param name
    // * @param passwd
    // * @param realName
    // * @param phone
    // */
    // public void updateManager(String name, String passwd, String realName,
    // String phone) {
    // userDao.updateManagerBean(name, passwd, realName, phone);
    // }

    /**
     * 更新收银员信息
     *
     * @param name
     *            用户名
     * @param passwd
     *            密码
     * @param realName
     *            真实姓名
     * @param phone
     *            手机
     */
    public void updateCashier(String name, String passwd, String realName,
                              String phone, int type) {
        userDao.updateUserBean(name, passwd, realName, phone, type);
    }
}
