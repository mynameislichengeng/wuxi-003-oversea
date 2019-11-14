package com.wizarpos.pay.statistics.model;

import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by wu on 15/10/13.
 */
@Table(name = "MixTranLogBean")
public class MixTranLogBean {
    private int id;
    private String tranLogId;
    private int tranType;
    private String tranDes;
    private long tranAmount;
    private long tranTime;
    private int tranState;

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

    public String getTranDes() {
        return tranDes;
    }

    public void setTranDes(String tranDes) {
        this.tranDes = tranDes;
    }

    public long getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(long tranAmount) {
        this.tranAmount = tranAmount;
    }

    public long getTranTime() {
        return tranTime;
    }

    public void setTranTime(long tranTime) {
        this.tranTime = tranTime;
    }

    public int getTranState() {
        return tranState;
    }

    public void setTranState(int tranState) {
        this.tranState = tranState;
    }
}
