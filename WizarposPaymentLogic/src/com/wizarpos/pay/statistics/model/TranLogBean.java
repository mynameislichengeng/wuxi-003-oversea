package com.wizarpos.pay.statistics.model;

import com.lidroid.xutils.db.annotation.Table;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.setting.presenter.AppConfiger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu on 15/10/13.
 */
@Table(name = "TranLogBean")
public class TranLogBean implements Serializable {
    private int id;
    private String tranLogId;
    private String tranType;
    private long amount;
    private long showAmount; //总金额(计入券消费的金额)
    private String tranTime;
    private String optName;
    private int tranState;

    private List<TicketTranLogBean> ticketTranLogBeans = new ArrayList<TicketTranLogBean>();

    List<MixTranLogBean>  mixTrans = new ArrayList<MixTranLogBean>();

    public void setMixTrans(List<MixTranLogBean> mixTrans) {
        this.mixTrans = mixTrans;
    }

    public List<MixTranLogBean> getMixTrans() {
        return mixTrans;
    }

    public List<TicketTranLogBean> getTicketTranLogBeans() {
        return ticketTranLogBeans;
    }

    public void setTicketTranLogBeans(List<TicketTranLogBean> ticketTranLogBeans) {
        this.ticketTranLogBeans = ticketTranLogBeans;
    }

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

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getTranTime() {
        return tranTime;
    }

    public void setTranTime(String tranTime) {
        this.tranTime = tranTime;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getTranState() {
        return tranState;
    }

    public void setTranState(int tranState) {
        this.tranState = tranState;
    }

    public long getShowAmount() {
        return showAmount;
    }

    public void setShowAmount(long showAmount) {
        this.showAmount = showAmount;
    }

    public String[] toShowStrings(){
        return new String[] { tranLogId,
                tranType,
                tranTime,
                Tools.formatFen(getShowAmount())};
    }

    public List<String[]> toMixDetialString() {
        if(mixTrans == null){return null;}
        List<String[]> mixDetials = new ArrayList<String[]>();
        for(MixTranLogBean bean: mixTrans){
            String[] mixTran = new String[]{
                    bean.getTranDes(),
                    DateUtil.format(bean.getTranTime(), "yyyy-MM-dd HH:mm:ss"),
                    Tools.formatFen(bean.getTranAmount())};
            mixDetials.add(mixTran);
        }
        return mixDetials;
    }

    public List<String[]> toTicketDetialString(){
        if(ticketTranLogBeans == null){return null;}
        List<String[]> ticketDetials = new ArrayList<String[]>();
        for(TicketTranLogBean bean: ticketTranLogBeans){
            String[] ticketTran = new String[]{
                    bean.getTicketDes(),
                    DateUtil.format(bean.getTranTime(), "yyyy-MM-dd HH:mm:ss"),
                    Tools.formatFen(bean.getAmount())};
            ticketDetials.add(ticketTran);
        }
        return ticketDetials;
    }
}
