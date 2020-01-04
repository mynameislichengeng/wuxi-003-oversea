package com.wizarpos.pay.recode.hisotory.activitylist.data;

import com.wizarpos.pay.recode.hisotory.activitylist.bean.TranRecordStatusParam;
import com.wizarpos.pay.recode.constants.TransRecordConstants;

public class TranRecordStatusDataUtil {

    public static TranRecordStatusParam createDefault() {
        TranRecordStatusParam tranRecordStatusParam = new TranRecordStatusParam();
        tranRecordStatusParam.setPageNo(TransRecordConstants.PAGENO_DEFAULT);
        tranRecordStatusParam.setTimeRange(TransRecordConstants.TimeRange.THIS_WEEK.getType());
        tranRecordStatusParam.setTransType(TransRecordConstants.TransType.ALL.getType());
        tranRecordStatusParam.setPageSize(TransRecordConstants.PAGE_SIZE);
        tranRecordStatusParam.setNextPage(TransRecordConstants.IS_NEXT_REFRESH);
        return tranRecordStatusParam;
    }
}
