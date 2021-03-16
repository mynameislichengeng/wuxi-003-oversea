package com.wizarpos.pay.cashier.activity;

import android.os.Bundle;
import android.widget.ListView;
import com.wizarpos.pay.cashier.adapter.TicketRecordAdapter;
import com.wizarpos.pay.common.base.BaseViewActivity;
import com.motionpay.pay2.lite.R;

/**
 * Created by Tea on 16/3/23.
 */
public class TicketUseRecordActivity extends BaseViewActivity{
    private ListView lvTicket;
    private TicketRecordAdapter ticketAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainView(R.layout.activity_ticket_use_record);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        lvTicket = (ListView) this.findViewById(R.id.lvTicket);
        ticketAdapter = new TicketRecordAdapter(this);
        lvTicket.setAdapter(ticketAdapter);
    }
}
