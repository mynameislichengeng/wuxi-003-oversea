package com.wizarpos.pay.db;

import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cardlink.model.BankCardTransUploadReq;

import java.util.List;

/**
 * @Author: yaosong
 * @date 2016-2-17 下午4:40:01
 * @Description:银行卡交易上送存储转发
 */
public class BankTransUploadDao {
	private final static String TAG = BankTransUploadDao.class.getSimpleName();
	private static BankTransUploadDao bankTransUploadDao;


	private BankTransUploadDao() {
//		dbController = PaymentApplication.getInstance().getDbController();
	}

	public static BankTransUploadDao getInstance() {
		if (bankTransUploadDao == null) {
			bankTransUploadDao = new BankTransUploadDao();
		}
		return bankTransUploadDao;
	}

	/**
	 * 初始化
	 * TODO 测试代码
	 */
	public void init() {
		DbUtils dbController = PaymentApplication.getInstance().getDbController();
		if (dbController == null) {
			return;
		}
		try {
			BankCardTransUploadReq testOpt = new BankCardTransUploadReq();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加上送信息
	 */
	public void addTransUpload(BankCardTransUploadReq bankCardTransUploadReq) {
		Log.i(TAG,"银行卡上送信息储存：" + bankCardTransUploadReq.getToken());
		DbUtils dbController = PaymentApplication.getInstance().getDbController();
		try {
			if (hasSaved(bankCardTransUploadReq.getToken())) {
				return;
			}
			dbController.save(bankCardTransUploadReq);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除上送信息
	 */
	public void deleteTransUpload(BankCardTransUploadReq bankCardTransUploadReq) {
		Log.i(TAG,"银行卡上送信息删除：" + bankCardTransUploadReq.getToken());
		DbUtils dbController = PaymentApplication.getInstance().getDbController();
		try {
			if (hasSaved(bankCardTransUploadReq.getToken())) {
				dbController.delete(bankCardTransUploadReq);
			} else {
				return;
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除上送信息
	 */
	public void deleteTransUpload(String token) {
		try {
			DbUtils dbController = PaymentApplication.getInstance().getDbController();
			BankCardTransUploadReq bankCardTransUploadReq = dbController.findFirst(Selector.from(
					BankCardTransUploadReq.class).where("token", "=", token));
			if (bankCardTransUploadReq != null) {
				deleteTransUpload(bankCardTransUploadReq);
			} else {
				return;
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 查找所有用户
	 * 
	 * @return
	 */
	public List<BankCardTransUploadReq> getAllTransUpload() {
		try {
			DbUtils dbController = PaymentApplication.getInstance().getDbController();
			return dbController.findAll(BankCardTransUploadReq.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean hasSaved(String token) {
		try {
			DbUtils dbController = PaymentApplication.getInstance().getDbController();
			BankCardTransUploadReq bankCardTransUploadReq = dbController.findFirst(Selector.from(
					BankCardTransUploadReq.class).where("token", "=", token));
			if (bankCardTransUploadReq != null) {
				return true;
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return false;
	}

}
