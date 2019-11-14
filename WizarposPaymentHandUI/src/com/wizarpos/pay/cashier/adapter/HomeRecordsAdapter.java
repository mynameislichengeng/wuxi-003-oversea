package com.wizarpos.pay.cashier.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wizarpos.pay.cashier.bean.BaseListDataBean;
import com.wizarpos.pay2.lite.R;

public class HomeRecordsAdapter extends BaseAdapter {
    private Context context;
    
    private List<BaseListDataBean> mList = new ArrayList<BaseListDataBean>();

    public HomeRecordsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private ViewHolder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	BaseListDataBean bean = mList.get(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.itemview_home_records, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ivIcon.setImageResource(bean.getDrableSouce());
        holder.tvRecordValue.setText(bean.getValue());
        holder.tvRecordTitle.setText(bean.getTitle());
        return convertView;
    }

    public class ViewHolder {
        public final ImageView ivIcon;
        public final TextView tvRecordTitle;
        public final TextView tvRecordValue;
        public final TextView tvRecordEnd;
        public final View root;

        public ViewHolder(View root) {
            ivIcon = (ImageView) root.findViewById(R.id.ivIcon);
            tvRecordTitle = (TextView) root.findViewById(R.id.tvRecordTitle);
            tvRecordValue = (TextView) root.findViewById(R.id.tvRecordValue);
            tvRecordEnd = (TextView) root.findViewById(R.id.tvRecordEnd);
            this.root = root;
        }
    }
    
    public void setDataChanged(List<BaseListDataBean> mList)
    {
    	this.mList = mList;
    	notifyDataSetChanged();
    }
}
