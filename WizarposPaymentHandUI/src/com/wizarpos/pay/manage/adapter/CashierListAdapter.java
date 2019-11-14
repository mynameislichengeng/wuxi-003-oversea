package com.wizarpos.pay.manage.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.SwipeLayout.DoubleClickListener;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.wizarpos.pay.db.UserBean;
import com.wizarpos.pay2.lite.R;

public class CashierListAdapter extends BaseSwipeAdapter {

    private Context context;

    private List<UserBean> userList = new ArrayList<UserBean>();

    public interface CashierListOpt {
        public void onDelete(int position, UserBean userBean);

        public void onEdit(int position, UserBean userBean);
    }

    private CashierListOpt opt;

    public void setOpt(CashierListOpt opt) {
        this.opt = opt;
    }

    public CashierListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void fillValues(int arg0, View v) {

        TextView tvLoginNum = (TextView) v.findViewById(R.id.tv_login_num);
        ImageView ivUserLogo = (ImageView) v.findViewById(R.id.iv_icon);
        TextView tvName = (TextView) v.findViewById(R.id.tv_manager_name);
        TextView tvPhone = (TextView) v.findViewById(R.id.tv_tel_num);

        tvLoginNum.setText(userList.get(arg0).getId() + "");
        ivUserLogo.setImageResource(userList.get(arg0).getType() == 2 ? R.drawable.user_admin : R.drawable.user_normal);
        tvName.setText(userList.get(arg0).getRealName());
        tvPhone.setText(userList.get(arg0).getPhone());
    }

    @Override
    public View generateView(final int position, ViewGroup arg1) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_view_cahiser, null);
        SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                // YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        v.findViewById(R.id.edit).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (opt != null) {
                    opt.onEdit(position, userList.get(position));
                }
            }
        });
        v.findViewById(R.id.delete).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (opt != null) {
                    opt.onDelete(position, userList.get(position));
                }
            }
        });

        swipeLayout.setOnDoubleClickListener(new DoubleClickListener() {

            @Override
            public void onDoubleClick(SwipeLayout arg0, boolean arg1) {
                if (opt != null) {
                    opt.onEdit(position, userList.get(position));
                }
            }
        });

        swipeLayout.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (opt != null) {
                    opt.onEdit(position, userList.get(position));
                    return true;
                }
                return false;
            }
        });

        return v;
    }

    @Override
    public int getSwipeLayoutResourceId(int arg0) {
        return R.id.swipe;
    }

    public void setDataChanged(List<UserBean> beans) {
        if (beans == null) {
            userList.clear();
        } else {
            this.userList = beans;
        }
        this.notifyDataSetChanged();
        this.closeAllItems();
    }

}
