package com.wizarpos.pay.recode.hisotory.activitylist.data;

import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.recode.hisotory.activitylist.bean.http.RespTranRecItemByDayPageBean;
import com.wizarpos.pay.recode.hisotory.activitylist.bean.http.ResponseTranRecoderListBean;

import java.util.ArrayList;
import java.util.List;

public class TransRecordDataUtil {

    /**
     * 这里因为ui的数据是按964来做的，所以虽然请求的接口是965，但是还是需要使用这一步骤，来进行转换一下
     *
     * @param originObject
     * @return
     */
    public static ResponseTranRecoderListBean createBeanAdapterFromDay(RespTranRecItemByDayPageBean originObject) {

        if (originObject == null) {
            return null;
        }
        if (originObject.getCounts() == null) {
            return null;
        }
        if (originObject.getLogs() == null) {
            return null;
        }

        RespTranRecItemByDayPageBean.CountsBean originCountsBean = originObject.getCounts();
        List<RespTranRecItemByDayPageBean.LogsBean> logsBeans = originObject.getLogs();

        ResponseTranRecoderListBean resultRep = new ResponseTranRecoderListBean();
        ResponseTranRecoderListBean.ResultBeanX resultBeanX = new ResponseTranRecoderListBean.ResultBeanX();
        resultRep.setResult(resultBeanX);

        resultBeanX.setHasNext(originCountsBean.isHasNext());
        resultBeanX.setPageNo(originCountsBean.getPageNo());
        List<ResponseTranRecoderListBean.ResultBeanX.ResultBean> itemList = new ArrayList<>();
        resultBeanX.setResult(itemList);


        for (RespTranRecItemByDayPageBean.LogsBean originlog : logsBeans) {
            int monthAllTransAmount = 0;
            List<RespTranRecItemByDayPageBean.LogsBean.TransDetailBean> originRecordResult = originlog.getTransDetail();
            if (originRecordResult == null) {
                continue;
            }

            for (int i = 0; i < originRecordResult.size(); i++) {
                ResponseTranRecoderListBean.ResultBeanX.ResultBean item = new ResponseTranRecoderListBean.ResultBeanX.ResultBean();
                RespTranRecItemByDayPageBean.LogsBean.TransDetailBean detailBean = originRecordResult.get(i);

                //每天的记录，相加
                monthAllTransAmount = monthAllTransAmount + detailBean.getTransAmount();
                item.setCnyAmount(detailBean.getCnyAmount());
                item.setDiscountAmount(detailBean.getDiscountAmount());
                item.setExchangeRate(String.valueOf(detailBean.getExchangeRate()));
                item.setMasterTranLogId(detailBean.getMasterTranLogId());
                item.setMerchantTradeCode(detailBean.getMerchantTradeCode());
                item.setOptName(detailBean.getOptName());
                item.setOrderNo(detailBean.getOrderNo());
                item.setPayTime(detailBean.getPayTime());
                item.setRefundAmount(detailBean.getRefundAmount());
                item.setSettlementAmount(String.valueOf(detailBean.getSettlementAmount()));
                item.setSettlementCurrency(detailBean.getSettlementCurrency());
                item.setSn(detailBean.getSn());
                item.setThirdExtId(detailBean.getThirdExtId());
                item.setThirdExtName(detailBean.getThirdExtName());
                item.setThirdTradeNo(detailBean.getThirdTradeNo());
                item.setTipAmount(detailBean.getTipAmount());
                item.setTranLogId(detailBean.getTranLogId());
                item.setTranTime(detailBean.getTranTime());
                item.setTransAmount(detailBean.getTransAmount());
                item.setTransCurrency(detailBean.getTransCurrency());
                item.setTransKind(detailBean.getTransKind());
                item.setTransType(detailBean.getTransType());
                item.setDiffCode(detailBean.getDiffCode());
                itemList.add(item);
            }


        }
        return resultRep;
    }


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
        dailyDetailResp.setDiffCode(resultBean.getDiffCode());
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
