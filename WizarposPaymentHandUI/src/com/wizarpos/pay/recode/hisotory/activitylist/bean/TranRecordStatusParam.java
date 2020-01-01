package com.wizarpos.pay.recode.hisotory.activitylist.bean;


/**
 * 用来记录当前activity界面，的状态
 */
public class TranRecordStatusParam {
    private int pageNo;
    private int pageSize;
    private String transType;
    private String tranLogId;
    private String timeRange;
    private String startTime;
    private String endTime;

    private boolean isNextPage;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTranLogId() {
        return tranLogId;
    }

    public void setTranLogId(String tranLogId) {
        this.tranLogId = tranLogId;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isNextPage() {
        return isNextPage;
    }

    public void setNextPage(boolean nextPage) {
        isNextPage = nextPage;
    }
}
