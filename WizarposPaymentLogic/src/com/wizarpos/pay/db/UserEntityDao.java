package com.wizarpos.pay.db;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.model.UserEntity;

import java.util.List;

/**
 * @Author: yaosong
 * @date 2016-2-17 下午4:40:01
 * @Description:操作员放在服务端维护，本地数据需要在登陆成功时增量更新
 */
public class UserEntityDao {
	// private static String dir = Environment.getExternalStorageDirectory()
	// + File.separator + "payDB" + File.separator;
	private static UserEntityDao userDao;

//	private DbUtils dbController;

	private UserEntityDao() {
//		dbController = PaymentApplication.getInstance().getDbController();
	}

	public static UserEntityDao getInstance() {
		if (userDao == null) {
			userDao = new UserEntityDao();
		}
		return userDao;
	}

	/**
	 * 初始化
	 */
	public void init() {
		//TODO 测试代码
//		DbUtils dbController = PaymentApplication.getInstance().getDbController();
//		if (dbController == null) {
//			return;
//		}
//		try {
//			UserEntity testOpt = new UserEntity();
//			testOpt.setAdminFlag("1");
//			testOpt.setCreateTime("0");
//			testOpt.setEmail("test@ys.cn");
//			testOpt.setEnable("true");
//			testOpt.setId("2345");
//			testOpt.setLastTime(0);
//			testOpt.setLoginName("00");
//			testOpt.setName("ysTest");
//			testOpt.setPassword("111111");
//			UserEntityDao.this.addUser(testOpt);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * 添加用户
	 */
	public void addUser(UserEntity userDBBean) {
		DbUtils dbController = PaymentApplication.getInstance().getDbController();
		try {
			if (isUser(userDBBean.getId())) {
				return;
			}
			dbController.save(userDBBean);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public void updateUserBean(UserEntity userDBBeanNew) {
		try {
			DbUtils dbController = PaymentApplication.getInstance().getDbController();
			UserEntity userDBBeanOld = dbController.findFirst(Selector.from(
					UserEntity.class).where("id", "=", userDBBeanNew.getId()));
			userDBBeanOld.setPassword(userDBBeanNew.getPassword());
			userDBBeanOld.setAdminFlag(userDBBeanNew.getAdminFlag());
			userDBBeanOld.setName(userDBBeanNew.getName());
			userDBBeanOld.setEnable(userDBBeanNew.getEnable());
			userDBBeanOld.setEmail(userDBBeanNew.getEmail());
			userDBBeanOld.setLoginName(userDBBeanNew.getLoginName());
			userDBBeanOld.setLastTime(userDBBeanNew.getLastTime());
			dbController.update(userDBBeanOld, "name", "password", "email",
					"loginName", "enable", "adminFlag", "lastTime");
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public void updateUserBeanByLoginName(UserEntity userDBBeanNew) {
		try {
			DbUtils dbController = PaymentApplication.getInstance().getDbController();
			UserEntity userDBBeanOld = dbController.findFirst(Selector.from(
					UserEntity.class).where("loginName", "=", userDBBeanNew.getLoginName()));
			userDBBeanOld.setId(userDBBeanNew.getId());
			userDBBeanOld.setPassword(userDBBeanNew.getPassword());
			userDBBeanOld.setAdminFlag(userDBBeanNew.getAdminFlag());
			userDBBeanOld.setName(userDBBeanNew.getName());
			userDBBeanOld.setEnable(userDBBeanNew.getEnable());
			userDBBeanOld.setEmail(userDBBeanNew.getEmail());
			userDBBeanOld.setLoginName(userDBBeanNew.getLoginName());
			userDBBeanOld.setLastTime(userDBBeanNew.getLastTime());
			dbController.update(userDBBeanOld,"id", "name", "password", "email",
					"enable", "adminFlag", "lastTime");
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public void updateAllUserBean(List<UserEntity> newUserDBBeans) {
		try {
			DbUtils dbController = PaymentApplication.getInstance().getDbController();
			dbController.updateAll(newUserDBBeans, "name", "password", "email",
					"loginName", "enable", "adminFlag", "lastTime");
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查找所有用户
	 *
	 * @return
	 */
	public List<UserEntity> getAllUsers() {
		try {
			DbUtils dbController = PaymentApplication.getInstance().getDbController();
			return dbController.findAll(UserEntity.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<UserEntity> getUsers(int pageSize, int pageIndex) {
		try {
			DbUtils dbController = PaymentApplication.getInstance().getDbController();
			return dbController.findAll(Selector.from(UserEntity.class)
					.limit(pageSize).offset(pageSize * pageIndex));
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 通过用户名取真实姓名
	public String getRealNameByUserName(String loginName) {
		try {
			DbUtils dbController = PaymentApplication.getInstance().getDbController();
			UserEntity userDBBean = dbController.findFirst(Selector.from(
					UserEntity.class).where("loginName", "=", loginName));
			if (userDBBean != null) {
				return userDBBean.getName();
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isUser(String loginName, String password) {
		try {
			DbUtils dbController = PaymentApplication.getInstance().getDbController();
			if(dbController != null) {
				UserEntity userDBBean = dbController.findFirst(Selector
						.from(UserEntity.class).where("loginName", "=", loginName)
						.and(WhereBuilder.b("password", "=", password)));
				if (userDBBean != null) {
					return true;
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isUser(String loginName) {
		try {
			DbUtils dbController = PaymentApplication.getInstance().getDbController();
			UserEntity userDBBean = dbController.findFirst(Selector.from(
					UserEntity.class).where("loginName", "=", loginName));
			if (userDBBean != null) {
				return true;
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return false;
	}

	public UserEntity getUser(String loginName, String password) {
		try {
			DbUtils dbController = PaymentApplication.getInstance().getDbController();
			UserEntity userDBBean = dbController.findFirst(Selector.from(
					UserEntity.class).where("loginName", "=", loginName)
					.and(WhereBuilder.b("password", "=", password)));
			if (userDBBean != null) {
				return userDBBean;
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}


	public UserEntity getUser(String loginName) {
		try {
			DbUtils dbController = PaymentApplication.getInstance().getDbController();
			UserEntity userDBBean = dbController.findFirst(Selector.from(
					UserEntity.class).where("loginName", "=", loginName));
			if (userDBBean != null) {
				return userDBBean;
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}
}
