package com.wizarpos.pay.db;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.wizarpos.pay.app.PaymentApplication;

import java.util.List;

public class UserDao {
    // private static String dir = Environment.getExternalStorageDirectory()
    // + File.separator + "payDB" + File.separator;
    private static UserDao userDao;

    private DbUtils dbController;

    private UserDao() {
        dbController = PaymentApplication.getInstance().getDbController();
    }

    public static UserDao getInstance() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao;
    }

    /**
     * 初始化
     */
    public void init() {
        if (dbController == null) {
            return;
        }
        try {
            UserBean supermanager = dbController.findFirst(Selector.from(
                    UserBean.class).where("type", "=", 2));
            if (supermanager != null) {
                return;
            }
            supermanager = new UserBean();
            supermanager.setPassword("111111");
            supermanager.setOperId("00");
            supermanager.setRealName("超级管理员");
            supermanager.setType(2);
            dbController.save(supermanager);
            UserBean manager = dbController.findFirst(Selector.from(
                    UserBean.class).where("type", "=", 1));
            if (manager != null) {
                return;
            }
            manager = new UserBean();
            manager.setPassword("111111");
            manager.setOperId("01");
            manager.setRealName("管理员");
            manager.setType(1);
            dbController.save(manager);
            manager = new UserBean();
            manager = new UserBean();
            manager.setPassword("111111");
            manager.setOperId("0001");
            manager.setRealName("管理员");
            manager.setType(1);
            dbController.save(manager);
            UserBean commomCashier = dbController.findFirst(Selector.from(
                    UserBean.class).where("type", "=", 0));
            commomCashier = new UserBean();
            commomCashier.setPassword("111111");
            commomCashier.setOperId("0002");
            commomCashier.setRealName("普通收银员");
            commomCashier.setType(0);
            dbController.save(commomCashier);
            commomCashier = new UserBean();
            commomCashier.setPassword("111111");
            commomCashier.setOperId("0003");
            commomCashier.setRealName("普通收银员");
            commomCashier.setType(0);
            dbController.save(commomCashier);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加用户
     */
    public void addUser(UserBean userDBBean) {
        if (userDBBean == null || userDBBean.getType() == 2) {
            return;
        }
        try {
            if (isUser(userDBBean.getOperId())) {
                return;
            }
            dbController.save(userDBBean);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void updateUserBean(String name, String passwd, String realName,
                               String phone, int type) {
        try {
            UserBean userDBBean = dbController.findFirst(Selector.from(
                    UserBean.class).where("operId", "=", name));
            userDBBean.setPassword(passwd);
            userDBBean.setPhone(phone);
            userDBBean.setRealName(realName);
            userDBBean.setType(type);
            dbController.update(userDBBean, "password", "phone", "realName",
                    "type");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除用户
     *
     * @param name
     * @param passwd
     */
    public void deleteUser(String name) {
        try {
            dbController.delete(UserBean.class,
                    WhereBuilder.b("operId", "=", name));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找所有用户
     *
     * @return
     */
    public List<UserBean> getAllUsers() {
        try {
            return dbController.findAll(UserBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UserBean> getUsers(int pageSize, int pageIndex) {
        try {
            return dbController.findAll(Selector.from(UserBean.class)
                    .limit(pageSize).offset(pageSize * pageIndex));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 通过用户名取真实姓名
    public String getRealNameByUserName(String name) {
        try {
            UserBean userDBBean = dbController.findFirst(Selector.from(
                    UserBean.class).where("operId", "=", name));
            if (userDBBean != null) {
                return userDBBean.getRealName();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isUser(String name, String passwd) {
        try {
            UserBean userDBBean = dbController.findFirst(Selector
                    .from(UserBean.class).where("operId", "=", name)
                    .and(WhereBuilder.b("password", "=", passwd)));
            if (userDBBean != null) {
                return true;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isSuperManger(String name, String passwd) {
        try {
            UserBean userDBBean = dbController.findFirst(Selector
                    .from(UserBean.class)
                    .where("operId", "=", name)
                    .and(WhereBuilder.b("password", "=", passwd).and("type",
                            "=", 2)));
            if (userDBBean != null) {
                return true;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isManger(String name, String passwd) {
        try {
            UserBean userDBBean = dbController.findFirst(Selector
                    .from(UserBean.class)
                    .where("operId", "=", name)
                    .and(WhereBuilder.b("password", "=", passwd).and("type",
                            "=", 1)));
            if (userDBBean != null) {
                return true;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isUser(String name) {
        try {
            UserBean userDBBean = dbController.findFirst(Selector.from(
                    UserBean.class).where("operId", "=", name));
            if (userDBBean != null) {
                return true;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }
}
