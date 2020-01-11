package com.wizarpos.pay.recode.hisotory.activitylist.data;

import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.recode.hisotory.activitylist.bean.http.ResponseTranRecoderListBean;

import java.util.ArrayList;
import java.util.List;

public class TransRecordDataUtil {


    public static DailyDetailResp create(ResponseTranRecoderListBean.ResultBeanX.ResultBean resultBean) {
        DailyDetailResp dailyDetailResp = new DailyDetailResp();
        dailyDetailResp.setTransAmount(String.valueOf(resultBean.getTransAmount()));
        dailyDetailResp.setOptName(resultBean.getOptName());
        dailyDetailResp.setPayTime(resultBean.getPayTime());
        dailyDetailResp.setTipAmount(String.valueOf(resultBean.getTipAmount()));
        dailyDetailResp.setTranLogId(resultBean.getTranLogId());
        dailyDetailResp.setDiscountAmount(String.valueOf(resultBean.getDiscountAmount()));
        dailyDetailResp.setThirdExtId(resultBean.getThirdExtId());
        dailyDetailResp.setTranTime(resultBean.getTranTime());
        dailyDetailResp.setTransKind(resultBean.getTransKind());
        dailyDetailResp.setCnyAmount(String.valueOf(resultBean.getCnyAmount()));
        dailyDetailResp.setThirdTradeNo(resultBean.getThirdTradeNo());
        dailyDetailResp.setTransType(resultBean.getTransType());
        dailyDetailResp.setTransName(Constants.TRAN_TYPE.get(resultBean.getTransType()));
        dailyDetailResp.setThirdExtName(resultBean.getThirdExtName());
        dailyDetailResp.setExchangeRate(resultBean.getExchangeRate());
        dailyDetailResp.setMasterTranLogId(resultBean.getMasterTranLogId());
        dailyDetailResp.setRefundAmount(String.valueOf(resultBean.getRefundAmount()));
        dailyDetailResp.setTransCurrency(resultBean.getTransCurrency());
        dailyDetailResp.setSettlementAmount(resultBean.getSettlementAmount());
        dailyDetailResp.setSettlementCurrency(resultBean.getSettlementCurrency());
        dailyDetailResp.setSn(resultBean.getSn());
        dailyDetailResp.setMerchantTradeCode(resultBean.getMerchantTradeCode());
        return dailyDetailResp;
    }

    public static List<DailyDetailResp> create(List<ResponseTranRecoderListBean.ResultBeanX.ResultBean> mlist) {
        List<DailyDetailResp> arList = new ArrayList<>();
        for (ResponseTranRecoderListBean.ResultBeanX.ResultBean temp : mlist) {
            DailyDetailResp item = create(temp);
            arList.add(item);
        }
        return arList;
    }
}
