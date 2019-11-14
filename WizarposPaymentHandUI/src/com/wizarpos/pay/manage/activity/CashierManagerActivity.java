package com.wizarpos.pay.manage.activity;

import java.io.Serializable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.common.utils.UIHelper;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.db.UserBean;
import com.wizarpos.pay.manage.adapter.CashierListAdapter;
import com.wizarpos.pay.manage.adapter.CashierListAdapter.CashierListOpt;
import com.wizarpos.pay.manager.presenter.CashierManager;
import com.wizarpos.pay2.lite.R;

/**
 * 收银员列表
 *
 * @author wu
 *
 */
public class CashierManagerActivity extends BaseViewActivity implements OnClickListener, CashierListOpt {
    private static final int REQUEST_EDIT = 1001;
    private static final int REQUEST_ADD = 1000;
    private CashierManager cashierManager;
    private ListView lvCashier;
    private CashierListAdapter adapter;

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
        adapter = new CashierListAdapter(this);
        lvCashier.setAdapter(adapter);
        adapter.setOpt(this);
        updateView();
    }

    private void updateView() {
        adapter.setDataChanged(cashierManager.getAllCashier());
    }

    @Override
    protected void onTitleRightClicked() {
        String permission = AppConfigHelper.getConfig(AppConfigDef.permission);
        if (permission.equals("0")) {
            UIHelper.ToastMessage(CashierManagerActivity.this, getResources().getString(R.string.no_permission));
        } else {
            startActivityForResult(new Intent(this, AddCashierManagerActivity.class), REQUEST_ADD);
        }
    }

    @Override
    public void onDelete(int position, UserBean userBean) {
        String permission = AppConfigHelper.getConfig(AppConfigDef.permission);
        if (permission.equals("0")) {
            UIHelper.ToastMessage(CashierManagerActivity.this, getResources().getString(R.string.no_permission));
            return;
        }
        if (userBean.getType() == 2) {
            UIHelper.ToastMessage(this, getResources().getString(R.string.supper_manager_do_not_del));
            return;
        }
        cashierManager.removeCashier(userBean);
        updateView();
    }

    @Override
    public void onEdit(int position, UserBean userBean) {
        String permission = AppConfigHelper.getConfig(AppConfigDef.permission);
        if (permission.equals("0")) {
            UIHelper.ToastMessage(CashierManagerActivity.this, getResources().getString(R.string.no_permission));
            return;
        }
        if (userBean.getType() == 2) {
            UIHelper.ToastMessage(this, getResources().getString(R.string.no_permission));
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
