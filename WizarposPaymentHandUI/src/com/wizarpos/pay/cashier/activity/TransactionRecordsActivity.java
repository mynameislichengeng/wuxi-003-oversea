package com.wizarpos.pay.cashier.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;

import com.wizarpos.pay.cashier.adapter.ExpandAdapter;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.wizarpos.pay.ui.PinnedSectionListView;
import com.motionpay.pay2.lite.R;

/**
 * Created by Tea on 16/3/22.
 */
public class TransactionRecordsActivity extends BaseViewActivity{

    private ExpandableListView elRecords;

    private static final String[] groupname = {"Bangalore","Mysore","Kodagu"};

    private static final String[][] data = {{"Vidhanasouda","Cubbon park","Lalbagh"},
            {"Palace","Chamundi Hills","Zoo"},
            {"Abbey Falls","Talakaveri"}};

    private static final String[][] listinfo = {{"Dr Ambedkar rd,, Sampangi Ramnagar, Bangalore, Karnataka","Kasturba Road, Bangalore, Karnataka","Lal Bagh Road, Lalbagh, Mavalli, Bangalore, Karnataka"},
            {"Sayyaji Rao Rd, Mysore, Karnataka","Mysore","Ittige gudu, Mysore, Karnataka"},
            {"Kodagu,Karnataka","Kodagu,Karnataka"}};


    private static final int[] ImgBckgrnd = {R.drawable.add,R.drawable.add,R.drawable.add};


    private PinnedSectionListView psRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainView(R.layout.activity_transaction_records);
        initView();
    }

    private void initView() {
//        elRecords = (ExpandableListView) this.findViewById(R.id.elRecords);
//        elRecords.setFocusable(false);
//        elRecords.setAdapter(new ExpandAdapter(this, TransactionRecordsActivity.this, groupname, ImgBckgrnd, listinfo,data));


        psRecords = (PinnedSectionListView) this.findViewById(R.id.psRecords);


    }

    public class PinnedSectionAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

        @Override
        public int getCount() {
            return 0;
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
            return null;
        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return false;
        }
    }


}
