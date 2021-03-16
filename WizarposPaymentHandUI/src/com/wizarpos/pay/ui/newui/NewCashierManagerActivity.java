package com.wizarpos.pay.ui.newui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wizarpos.pay.common.base.BaseLogicAdapter;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.base.ViewHolder;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.UserBean;
import com.wizarpos.pay.manage.activity.AddCashierManagerActivity;
import com.wizarpos.pay.manage.activity.EditCashierManagerActivity;
import com.wizarpos.pay.manage.adapter.CashierListAdapter.CashierListOpt;
import com.wizarpos.pay.manager.presenter.CashierManager;
import com.wizarpos.pay.ui.widget.CommonToastUtil;
import com.motionpay.pay2.lite.R;

import java.io.Serializable;

/**
 * 收银员列表
 *
 * @author wu
 *
 */
public class NewCashierManagerActivity extends BaseViewActivity implements OnClickListener, CashierListOpt {
    private static final int REQUEST_EDIT = 1001;
    private static final int REQUEST_ADD = 1000;
    private CashierManager cashierManager;
    private ListView lvCashier;
    private BaseLogicAdapter<UserBean> adapter;
    private MaterialDialog chooseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cashierManager = new CashierManager(this);
        initView();
    }

    private void initView() {
        setMainView(R.layout.activity_cashier_list);
        setTitleText(getResources().getString(R.string.cashier_manager));
        showTitleBack();
        setTitleRightImage(R.drawable.add);
        lvCashier = (ListView) findViewById(R.id.lvCashier);
//        adapter = new CashierListAdapter(this);
        adapter = new BaseLogicAdapter<UserBean>(this, cashierManager.getAllCashier(), R.layout.item_view_cahiser_new) {
            @Override
            public void convert(ViewHolder helper,final UserBean item,final int position) {
                String opt = "收银员";
                helper.setImageResource(R.id.ivUser, R.drawable.ic_cashier);
                if (item.getType() != 0){
                    opt = "管理员";
                    helper.setImageResource(R.id.ivUser, R.drawable.ic_administrators);
                }
                helper.setText(R.id.tvUserAbove, opt + item.getRealName());
                if (!TextUtils.isEmpty(item.getPhone())){
                    helper.setText(R.id.tvUserBelow, "联系电话：" + item.getPhone());
                }else{
                    helper.setText(R.id.tvUserBelow, "联系电话：");
                }
                helper.getView(R.id.llUser).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showChooseDialog(position, item);
                    }
                });
            }
        };
        lvCashier.setAdapter(adapter);
//        adapter.setOpt(this);
//        updateView();
    }

    private void showChooseDialog(final int position, final UserBean item) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_cashier_choose,null);
        Button btnEdit = (Button) dialogView.findViewById(R.id.btnEdit);
        Button btnDelete = (Button) dialogView.findViewById(R.id.btnDelete);
        ImageView ivExit = (ImageView) dialogView.findViewById(R.id.ivExit);
        ivExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseDialog != null && chooseDialog.isShowing()){
                    chooseDialog.dismiss();
                }
            }
        });
        btnEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onEdit(position,item);
                if (chooseDialog != null && chooseDialog.isShowing()){
                    chooseDialog.dismiss();
                }
            }
        });
        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelete(position,item);
                if (chooseDialog != null && chooseDialog.isShowing()){
                    chooseDialog.dismiss();
                }
            }
        });
        MaterialDialog.Builder chooseDialogBuilder = new MaterialDialog.Builder(this);
        chooseDialog = chooseDialogBuilder.customView(dialogView,false).build();
        chooseDialog.show();
    }

    private void updateView() {
        adapter.setmDatas(cashierManager.getAllCashier());
        adapter.notifyDataSetChanged();
//        adapter.setDataChanged(cashierManager.getAllCashier());
    }

    @Override
    protected void onTitleRightClicked() {
        String permission = AppConfigHelper.getConfig(AppConfigDef.permission);
        if (permission.equals("0")) {
            CommonToastUtil.showMsgBelow(NewCashierManagerActivity.this, CommonToastUtil.LEVEL_WARN, getResources().getString(R.string.no_permission));
//            UIHelper.ToastMessage(NewCashierManagerActivity.this, getResources().getString(R.string.no_permission));
        } else {
            startActivityForResult(new Intent(this, AddCashierManagerActivity.class), REQUEST_ADD);
        }
    }

    @Override
    public void onDelete(int position, UserBean userBean) {
        String permission = AppConfigHelper.getConfig(AppConfigDef.permission);
        if (permission.equals("0")) {
            CommonToastUtil.showMsgBelow(NewCashierManagerActivity.this, CommonToastUtil.LEVEL_WARN, getResources().getString(R.string.no_permission));
//            UIHelper.ToastMessage(NewCashierManagerActivity.this, getResources().getString(R.string.no_permission));
            return;
        }
        if (userBean.getType() == 2) {
            CommonToastUtil.showMsgBelow(NewCashierManagerActivity.this, CommonToastUtil.LEVEL_WARN, getResources().getString(R.string.supper_manager_do_not_del));
//            UIHelper.ToastMessage(this, getResources().getString(R.string.supper_manager_do_not_del));
            return;
        }
        cashierManager.removeCashier(userBean);
        updateView();
    }

    @Override
    public void onEdit(int position, UserBean userBean) {
        String permission = AppConfigHelper.getConfig(AppConfigDef.permission);
        if (permission.equals("0")) {
            CommonToastUtil.showMsgBelow(NewCashierManagerActivity.this, CommonToastUtil.LEVEL_WARN, getResources().getString(R.string.no_permission));
//            UIHelper.ToastMessage(NewCashierManagerActivity.this, getResources().getString(R.string.no_permission));
            return;
        }
        if (userBean.getType() == 2) {
            CommonToastUtil.showMsgBelow(NewCashierManagerActivity.this, CommonToastUtil.LEVEL_WARN, getResources().getString(R.string.no_permission));
//            UIHelper.ToastMessage(this, getResources().getString(R.string.no_permission));
            return;
        }
        Intent intent = getIntent();
        intent.putExtra("bean", (Serializable) userBean);
        intent.setClass(this, EditCashierManagerActivity.class);
        startActivityForResult(intent, REQUEST_EDIT);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == RESULT_OK) {
            updateView();
        }
    }

}
