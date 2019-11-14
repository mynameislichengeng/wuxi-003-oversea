package com.wizarpos.pay.statistics.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易明细返回
 * Created by wu on 15/10/13.
 */
public class TransDetialResp {
    private int count;
    private int pageNo;
    private List<TranLogBean> logs = new ArrayList<TranLogBean>();
    private List<TicketTranLogBean> tickets = new ArrayList<TicketTranLogBean>();
    private List<MixTranLogBean> mixTrans = new ArrayList<MixTranLogBean>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<TranLogBean> getLogs() {
        return logs;
    }

    public void setLogs(List<TranLogBean> logs) {
        this.logs = logs;
    }

    public List<TicketTranLogBean> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketTranLogBean> tickets) {
        this.tickets = tickets;
    }

    public List<MixTranLogBean> getMixTrans() {
        return mixTrans;
    }

    public void setMixTrans(List<MixTranLogBean> mixTrans) {
        this.mixTrans = mixTrans;
    }
}
