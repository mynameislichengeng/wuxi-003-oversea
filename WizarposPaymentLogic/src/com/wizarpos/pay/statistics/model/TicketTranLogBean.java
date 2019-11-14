package com.wizarpos.pay.statistics.model;

import com.lidroid.xutils.db.annotation.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu on 15/10/13.
 */
@Table(name = "TicketTranLogBean")
public class TicketTranLogBean {
    private int id;
    private String tranLogId;
    private int tranType;
    private String ticketId;
    private String ticketDes;
    private long amount;
    private long tranTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTranLogId() {
        return tranLogId;
    }

    public void setTranLogId(String tranLogId) {
        this.tranLogId = tranLogId;
    }

    public int getTranType() {
        return tranType;
    }

    public void setTranType(int tranType) {
        this.tranType = tranType;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketDes() {
        return ticketDes;
    }

    public void setTicketDes(String ticketDes) {
        this.ticketDes = ticketDes;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getTranTime() {
        return tranTime;
    }

    public void setTranTime(long tranTime) {
        this.tranTime = tranTime;
    }
}
