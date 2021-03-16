package com.wizarpos.pay.cashier.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wizarpos.pay.cashier.model.TicketDef;
import com.motionpay.pay2.lite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tea on 16/3/23.
 */
public class TicketRecordAdapter extends BaseAdapter{

    private List<TicketDef> ticketList;
    private Context mContext;

    public TicketRecordAdapter(Context mContext, List<TicketDef> ticketList) {
        this.ticketList = ticketList;
        this.mContext = mContext;
    }

    public TicketRecordAdapter(Context mContext) {
        this.mContext = mContext;
        if(ticketList == null) {
            ticketList = new ArrayList<TicketDef>();
        }
    }

    @Override
    public int getCount() {
        return 12;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_ticket_use_record, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public class ViewHolder {

    }


    public void setDataChanged(List<TicketDef> ticketList) {
        this.ticketList = ticketList;
        this.notifyDataSetChanged();
    }
}
