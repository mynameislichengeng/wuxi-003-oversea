package com.wizarpos.pay.manage.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.UserBean;
import com.wizarpos.pay.manager.presenter.CashierManager;
import com.wizarpos.pay.view.ListViewCompat;
import com.wizarpos.pay.view.MessageItem;
import com.wizarpos.pay.view.SlideView;
import com.wizarpos.pay.view.SlideView.OnSlideListener;
import com.wizarpos.pay2.lite.R;

/**
 * 废弃 wu
 *
 */
public class CashierManagerActivity2 extends BaseViewActivity implements OnClickListener {
    CashierManager cashierManager;
    UserBean chooseUser;
    private static final String TAG = "CashierManagerActivity";
    private ListViewCompat mListView;
    private List<MessageItem> mMessageItems = new ArrayList<MessageItem>();
    List<UserBean> userList = new ArrayList<UserBean>();
    private SlideView mLastSlideViewWithStatusOn;
    private MessageItem data;
    private int itemPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cashierManager = new CashierManager(this);
        initView();
    }

    private void initView() {
        setMainView(R.layout.activity_list_delete);
        setTitleText(getResources().getString(R.string.cashier_manager));
        showTitleBack();
        setTitleRightImage(R.drawable.add);
        mListView = (ListViewCompat) findViewById(R.id.list);
        int[] btnIds = { R.id.btn_add_manager };
        setOnClickListenerByIds(btnIds, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        mMessageItems.clear();
        userList = cashierManager.getAllCashier();
        for (int i = 0; i < userList.size(); i++) {
            MessageItem item = new MessageItem();
            if (userList.get(i).getType() == 2) {
                item.iconRes = R.drawable.user_admin;
            } else {
                item.iconRes = R.drawable.user_normal;
            }
            item.type = userList.get(i).getType();
            item.loginNum = String.valueOf(userList.get(i).getId());
            item.managerName = userList.get(i).getRealName();
            item.telNum = userList.get(i).getPhone();
            item.password = userList.get(i).getPassword();
            item.oprateId = userList.get(i).getOperId();
            mMessageItems.add(item);
        }
        mListView.setAdapter(new SlideAdapter());
    }

    private class SlideAdapter extends BaseAdapter implements OnSlideListener {

        private LayoutInflater mInflater;

        SlideAdapter() {
            super();
            mInflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mMessageItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mMessageItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            SlideView slideView = (SlideView) convertView;
            if (slideView == null) {
                View itemView = mInflater.inflate(R.layout.list_items, null);
                slideView = new SlideView(CashierManagerActivity2.this);
                slideView.setContentView(itemView);
                holder = new ViewHolder(slideView);
                slideView.setOnSlideListener(this);
                slideView.setTag(holder);
            } else {
                holder = (ViewHolder) slideView.getTag();
            }
            MessageItem item = mMessageItems.get(position);
            item.slideView = slideView;
            item.slideView.shrink();
            holder.icon.setImageResource(item.iconRes);
            if (item.iconRes == R.drawable.user_admin) {
                holder.managerName.setTextColor(getResources().getColor(R.color.orange_color));
            }
            holder.longinNum.setText(item.loginNum);
            holder.managerName.setText(item.managerName + "(" + item.oprateId + ")");
            if (item.type == 1) {
                holder.managerName.setTextColor(getResources().getColor(R.color.blue_color));
            }
            holder.telNum.setText(item.telNum);
            holder.delete.setOnClickListener(CashierManagerActivity2.this);
            holder.edit.setOnClickListener(CashierManagerActivity2.this);
            holder.llFront.setOnClickListener(CashierManagerActivity2.this);

            return slideView;
        }

        @Override
        public void onSlide(View view, int status) {
            LogEx.d("status", status+"");
            if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
                mLastSlideViewWithStatusOn.shrink();
            }

            if (status == SLIDE_STATUS_ON) {
                mLastSlideViewWithStatusOn = (SlideView) view;
            }

            if(status == SLIDE_STATUS_CLICK){ //点击 wu
                data = mListView.getItemData();
                edit();
            }
        }

    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView longinNum;
        public TextView managerName;
        public TextView telNum;
        public TextView delete;
        public TextView edit;
        public View llFront;

        ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.iv_icon);
            longinNum = (TextView) view.findViewById(R.id.tv_login_num);
            managerName = (TextView) view.findViewById(R.id.tv_manager_name);
            telNum = (TextView) view.findViewById(R.id.tv_tel_num);
            delete = (TextView) view.findViewById(R.id.delete);
            edit = (TextView) view.findViewById(R.id.edit);
            llFront = view.findViewById(R.id.llFront);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        String permission = AppConfigHelper.getConfig(AppConfigDef.permission);
        data = mListView.getItemData();
        itemPosition = mListView.getPosition();
        if (v.getId() == R.id.delete) {
            if (permission.equals("0")) {
                UIHelper.ToastMessage(CashierManagerActivity2.this, getResources().getString(R.string.no_permission));
                return;
            } else {
                if (data.type != 2) {
                    remove();
                    mListView.setAdapter(new SlideAdapter());
                } else {
                    UIHelper.ToastMessage(this, getResources().getString(R.string.supper_manager_do_not_del));
                    return;
                }
            }

        } else if (v.getId() == R.id.edit) {
            if (permission.equals("0")) {
                UIHelper.ToastMessage(CashierManagerActivity2.this, getResources().getString(R.string.no_permission));
                return;
            } else {
                if (data.type != 2) {
                    edit();
                } else {
                    UIHelper.ToastMessage(this, getResources().getString(R.string.no_permission));
                    return;
                }
            }
        } else if (v.getId() == R.id.llFront) {
            data = mListView.getItemData();
            edit();// 收银员管理：增加点击操作员出现编辑页面功能 wu
        }

    }

    private void remove() {
        chooseUser = new UserBean();
        chooseUser.setId(Integer.valueOf(data.loginNum));
        chooseUser.setPhone(data.telNum);
        chooseUser.setRealName(data.managerName);
        chooseUser.setType(data.type);
        chooseUser.setPassword(data.password);
        chooseUser.setOperId(data.oprateId);
        cashierManager.removeCashier(chooseUser);
        mMessageItems.remove(itemPosition);
    }

    private void edit() {
        Intent intent = getIntent();
        // intent.putExtra("operId", data.oprateId);
        // intent.putExtra("password", data.password);
        // intent.putExtra("realName", data.managerName);
        // intent.putExtra("phone", data.telNum);
        UserBean bean = new UserBean();
        bean.setOperId(data.oprateId);
        bean.setPassword(data.password);
        bean.setRealName(data.managerName);
        bean.setPhone(data.telNum);
        bean.setType(data.type);
        intent.putExtra("bean", (Serializable) bean);
        intent.setClass(this, EditCashierManagerActivity.class);
        startActivity(intent);
//		finish();
    }

    @Override
    protected void onTitleRightClicked() {
        String permission = AppConfigHelper.getConfig(AppConfigDef.permission);
        if (permission.equals("0")) {
            UIHelper.ToastMessage(CashierManagerActivity2.this, getResources().getString(R.string.no_permission));
            return;
        } else {
            startNewActivity(AddCashierManagerActivity.class);
            finish();
        }
    }

}
