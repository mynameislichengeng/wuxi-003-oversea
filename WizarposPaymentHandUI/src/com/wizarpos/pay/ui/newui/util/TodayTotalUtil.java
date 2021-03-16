package com.wizarpos.pay.ui.newui.util;

import android.util.SparseArray;

import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.model.TodayDetailBean;
import com.wizarpos.pay.model.TranLogVo;
import com.motionpay.pay2.lite.R;

/**
 * Created by Song on 2016/4/7.
 */
public class TodayTotalUtil {
    public static final int FLAG_REVOKE_COLOR = 0xffffffff;
    public static final int FLAG_BAIDU_COLOR = 0xffDA5430;
    public static final int FLAG_BANK_COLOR = 0xffFEE074;
    public static final int FLAG_QQPAY_COLOR = 0xffAF4E96;
    public static final int FLAG_ALIPAY_COLOR = 0xff2091CF;
    public static final int FLAG_WEXIN_COLOR = 0xff68BC31;
    public static final int FLAG_UNION_COLOR = 0xffEE5636;
    private static final String FLAG_REVOKE_NAME = PaymentApplication.getInstance().getString(R.string.refund);
    private static final String FLAG_BAIDU_NAME = "百度";
    private static final String FLAG_BANK_NAME = "银行卡";
    private static final String FLAG_QQPAY_NAME = "QQ";
    private static final String FLAG_ALIPAY_NAME = "Alipay";
    private static final String FLAG_WEXIN_NAME = "Wechat";
    private static final String FLAG_UNION_NAME = "Union";
    private static SparseArray<Integer> resArray = new SparseArray<Integer>();

    static {
        resArray.put(FLAG_REVOKE_COLOR, R.drawable.ic_back_arrow);
        resArray.put(FLAG_BAIDU_COLOR, R.drawable.ic_pay_baidu);
        resArray.put(FLAG_BANK_COLOR, R.drawable.ic_pay_bank);
        resArray.put(FLAG_QQPAY_COLOR, R.drawable.ic_pay_qq);
        resArray.put(FLAG_ALIPAY_COLOR, R.drawable.ic_pay_zhifubao);
        resArray.put(FLAG_WEXIN_COLOR, R.drawable.ic_pay_weixin);
        resArray.put(FLAG_UNION_COLOR, R.drawable.ic_pay_union);
    }

    public static void setTodatTotalBeanShowDetail(TodayDetailBean bean) {
        if (bean.getDetailName().contains(FLAG_ALIPAY_NAME)) {
            bean.setColor(FLAG_ALIPAY_COLOR);
            bean.setImage(resArray.get(FLAG_ALIPAY_COLOR));
        } else if (bean.getDetailName().contains(FLAG_BAIDU_NAME)) {
            bean.setColor(FLAG_BAIDU_COLOR);
            bean.setDetailName("百度钱包消费");   //根据需求修改，服务端返回的是"百度消费"  TODO 服务端修改后去掉
            bean.setImage(resArray.get(FLAG_BAIDU_COLOR));
        } else if (bean.getDetailName().contains(FLAG_BANK_NAME)) {
            bean.setColor(FLAG_BANK_COLOR);
            bean.setImage(resArray.get(FLAG_BANK_COLOR));
        } else if (bean.getDetailName().contains(FLAG_QQPAY_NAME)) {
            bean.setColor(FLAG_QQPAY_COLOR);
            bean.setImage(resArray.get(FLAG_QQPAY_COLOR));
        } else if (bean.getDetailName().contains(FLAG_REVOKE_NAME)) {
            bean.setColor(FLAG_REVOKE_COLOR);
            bean.setImage(resArray.get(FLAG_REVOKE_COLOR));
        } else if (bean.getDetailName().contains(FLAG_WEXIN_NAME)) {
            bean.setColor(FLAG_WEXIN_COLOR);
            bean.setImage(resArray.get(FLAG_WEXIN_COLOR));
        }else if (bean.getDetailName().contains(FLAG_UNION_NAME)) {
            bean.setColor(FLAG_UNION_COLOR);
            bean.setImage(resArray.get(FLAG_UNION_COLOR));
        }
    }

    public static int getPayItemImage(String transType) {
        if (transType.contains(FLAG_ALIPAY_NAME)) {
            return resArray.get(FLAG_ALIPAY_COLOR);
        } else if (transType.contains(FLAG_BAIDU_NAME)) {
            return resArray.get(FLAG_BAIDU_COLOR);
        } else if (transType.contains(FLAG_BANK_NAME)) {
            return resArray.get(FLAG_BANK_COLOR);
        } else if (transType.contains(FLAG_QQPAY_NAME)) {
            return resArray.get(FLAG_QQPAY_COLOR);
        } else if (transType.contains(FLAG_WEXIN_NAME)) {
            return resArray.get(FLAG_WEXIN_COLOR);
        }else if (transType.contains(FLAG_UNION_NAME)) {
            return resArray.get(FLAG_UNION_COLOR);
        } else return resArray.get(FLAG_REVOKE_COLOR);
    }

}
