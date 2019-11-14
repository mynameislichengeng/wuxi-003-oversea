package com.wizarpos.pay.cashier.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wizarpos.pay.cashier.bean.BaseListDataBean;
import com.wizarpos.pay.view.TransTypeItem;
import com.wizarpos.pay2.lite.R;

/**
 * @Author: yaosong
 * @date 2015-12-9 下午1:37:55
 * @Description:第三方支付调用手持收款，平铺的支付方式适配器
 */
public class TransTypeItemsAdapter extends BaseAdapter {
    private Context context;
    
    private List<TransTypeItem> mList = new ArrayList<TransTypeItem>();

    public TransTypeItemsAdapter(Context context) {
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
        TransTypeItem bean = mList.get(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.itemview_gather_types, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv_icon.setImageResource(bean.getIcon());
        holder.tv_tran_type.setText(bean.getShowValue());
        return convertView;
    }

    public class ViewHolder {
        public final TextView tv_tran_type;
        public final ImageView iv_icon;
        public final View root;

        public ViewHolder(View root) {
            tv_tran_type = (TextView) root.findViewById(R.id.tv_tran_type);
            iv_icon = (ImageView) root.findViewById(R.id.iv_icon);
            this.root = root;
        }
    }
    
    public void setDataChanged(List<TransTypeItem> mList)
    {
        this.mList = mList;
        notifyDataSetChanged();
    }
}
