package com.wizarpos.pay.ui.newui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.model.TodayDetailBean;
import com.wizarpos.pay2.lite.R;

import java.util.ArrayList;
import java.util.List;

public class ShowDetailAdapter extends BaseAdapter {

    private Context context;

    public ShowDetailAdapter(Context context) {
        this.context = context;
    }

    private List<TodayDetailBean> list = new ArrayList<TodayDetailBean>();

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private ViewHolder viewHolder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_today_detail_data_new, null,false);
            viewHolder = new ViewHolder();
            viewHolder.tvTransactionType = convertView.findViewById(R.id.tvTransactionType);
            viewHolder.tvTransactionAcount = convertView.findViewById(R.id.tvTransactionAcount);
            viewHolder.tvTransactionAmount = convertView.findViewById(R.id.tvTransactionAmount);
            viewHolder.ivIcon = convertView.findViewById(R.id.ivIcon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTransactionType.setText(list.get(position).getDetailName());
        if ( null != list.get(position).getImage()){
            viewHolder.ivIcon.setImageResource(list.get(position).getImage());
//            viewHolder.tvTransactionType.setCompoundDrawables(list.get(position).getImage(),null,null,null);
        }
        viewHolder.tvTransactionAcount.setText(String.valueOf(list.get(position).getCount()));
        viewHolder.tvTransactionAmount.setText("$"+Calculater.formotFen(list.get(position).getAmount()));
        return convertView;
    }

    public class ViewHolder {
        TextView tvTransactionType;
        TextView tvTransactionAcount;
        TextView tvTransactionAmount;
        ImageView ivIcon;
    }

    public void setDataChanged(List<TodayDetailBean> list) {
        if(list == null){
            this.list.clear();
        }else{
            this.list = list;
        }
        this.notifyDataSetChanged();
    }

}
