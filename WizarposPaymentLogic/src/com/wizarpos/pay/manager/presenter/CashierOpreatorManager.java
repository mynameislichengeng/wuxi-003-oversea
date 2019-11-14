package com.wizarpos.pay.manager.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.utils.MD5Util;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.UserEntityDao;
import com.wizarpos.pay.model.UserEntity;

import java.util.HashMap;
import java.util.List;

public class CashierOpreatorManager extends BasePresenter {

	private UserEntityDao userDao;

	public CashierOpreatorManager(Context context) {
		super(context);
		userDao = UserEntityDao.getInstance();
	}

	/**
	 * 获取所有收银员
	 *
	 * @return
	 */
	public List<UserEntity> getAllCashier() {
		return userDao.getAllUsers();
	}

	/**
	 * 增加收银员
	 *
	 * @param user
	 */
	public void addCashier(UserEntity user) {
		userDao.addUser(user);
	}

	/**
	 * 更新收银员信息
	 */
	public void updateCashier(UserEntity newBean) {
		userDao.updateUserBeanByLoginName(newBean);
	}

	/**
	 * 增量更新收银员信息
	 */
	public void updateLastCashier(List<UserEntity> newBeans) {
		for (int i = 0; i < newBeans.size(); i++) {
			if (userDao.isUser(newBeans.get(i).getLoginName())) {
				userDao.updateUserBeanByLoginName(newBeans.get(i));
			} else {
				userDao.addUser(newBeans.get(i));
			}
		}
	}

	/**
	 * 更新所有收银员信息
	 */
	public void updateAllCashier(List<UserEntity> newBeans) {
		userDao.updateAllUserBean(newBeans);
	}

	public long getLastUpdateTime() {
		List<UserEntity> beans = userDao.getAllUsers();
		long lastTime = 0;
		for (int i = 0; i < beans.size(); i++) {
			if (beans.get(i).getLastTime() > lastTime)
				lastTime = beans.get(i).getLastTime();
		}
		return lastTime;
	}

	/**
	 * 修改密码输入验证
	 *
	 * @param oldPwd
	 * @param newPwd
	 * @param newPwdConfirm
	 * @param listener
	 */
	public void verifyResetPwd(String oldPwd, String newPwd, String newPwdConfirm, ResultListener listener) {
		if (!TextUtils.isEmpty(oldPwd) && !TextUtils.isEmpty(newPwd) && newPwd.equals(newPwdConfirm)) {
			if (!TextUtils.isEmpty(newPwd) && newPwd.length() < 6) {
				listener.onFaild(new Response(-1, "新密码不得小于6位"));
				return;
			} else {
				listener.onSuccess(new Response());
			}
		} else {
			listener.onFaild(new Response(-1, (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(oldPwd)) ? "输入信息不能为空" : "两次密码输入不一致"));
		}
	}

	public void resetNowOperatorPwd(String oldPassword, String password, final ResultListener listener) {
		final String nowOperatorLoginName = AppConfigHelper.getConfig(AppConfigDef.operatorNo);
		if (TextUtils.isEmpty(nowOperatorLoginName)) {
			listener.onFaild(new Response(-1, "操作员登录状态异常"));
		}
		final String md5Password = MD5Util.getMd5Str(password);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", nowOperatorLoginName);
		params.put("passwd", md5Password);
		params.put("oldPasswd", MD5Util.getMd5Str(oldPassword));
		NetRequest.getInstance().addRequest(Constants.SC_943_OPERATOR_RESET_PASSWORD, params, new ResponseListener() {
			@Override
			public void onSuccess(Response response) {
				try {
					UserEntity nowUser = userDao.getUser(nowOperatorLoginName);
					userDao.updateUserBeanByLoginName(nowUser);
					if (response.msg.equals("success")) {
						response.setMsg("密码修改成功");
					}
					listener.onSuccess(response);
				} catch (Exception e) {
					e.printStackTrace();
					Log.w("CashierOpreatorManager", "密码修改成功，但本地保存出现异常！");
					listener.onFaild(response);
				}
			}

			@Override
			public void onFaild(Response response) {
				listener.onFaild(response);
			}
		});
	}

	public int getNowOperatorType() {
		UserEntity nowUser = userDao.getUser(AppConfigHelper.getConfig(AppConfigDef.operatorNo));
		if (null == nowUser) {
			return -1;
		} else {
			return Integer.parseInt(nowUser.getAdminFlag());
		}
	}
}
