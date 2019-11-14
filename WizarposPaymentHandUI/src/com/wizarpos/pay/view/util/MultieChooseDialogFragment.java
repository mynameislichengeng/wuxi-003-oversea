package com.wizarpos.pay.view.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.SimpleAdapter;

import com.wizarpos.pay2.lite.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultieChooseDialogFragment extends DialogFragment {

    public static final String TIEM = "item";

    private List<MultieChooseItem> items;



    public interface MultieChooseListener {

        void onItemClick(int position);
    }

    private MultieChooseListener multieChooseListener;

    public void setMultieChooseListener(MultieChooseListener multieChooseListener) {
        this.multieChooseListener = multieChooseListener;
    }

    public MultieChooseDialogFragment(){

    }

    public static MultieChooseDialogFragment newInstance(List<MultieChooseItem> items) {
        MultieChooseDialogFragment fragment = new MultieChooseDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(TIEM, (Serializable) items);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            items = (List<MultieChooseItem>) getArguments().getSerializable(TIEM);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(items == null){
            return super.onCreateDialog(savedInstanceState);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(MultieChooseItem item : items){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", item.getImgId());
            map.put("title", item.getTitle());
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.layout_single_choose, new String[] { "img", "title" }, new int[] { R.id.ivImg,
                R.id.tvTitle });
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (multieChooseListener != null) {
                    multieChooseListener.onItemClick(which);
                }
            }
        });
        builder.setCancelable(true);
        return  builder.create();
    }
}
