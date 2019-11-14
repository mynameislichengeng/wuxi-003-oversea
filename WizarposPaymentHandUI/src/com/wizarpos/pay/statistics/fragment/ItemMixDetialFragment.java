package com.wizarpos.pay.statistics.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.wizarpos.pay.statistics.adapter.MixDetialAdapter;
import com.wizarpos.pay2.lite.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ItemMixDetialFragment extends Fragment{

	private static final String MIX_TRANS = "mixTrans";
	private List<String[]> mixTrans; 
	
	public ItemMixDetialFragment() {
	}
	
	public static ItemMixDetialFragment newInstance(List<String[]> mixTrans){
		ItemMixDetialFragment fragment = new ItemMixDetialFragment();
		Bundle bundel = new Bundle();
		bundel.putSerializable(MIX_TRANS, (Serializable) mixTrans);
		fragment.setArguments(bundel);
		return fragment;
	}
	
//	FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//	transaction.setCustomAnimations(R.anim.popup_enter, R.anim.popup_exit);
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mixTrans = (List<String[]>) getArguments().getSerializable(MIX_TRANS);
	}
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//		return super.onCreateView(inflater, container, savedInstanceState);
		View mainView = inflater.inflate(R.layout.fargment_item_mix_detail, container, false);
		ListView lvDetial = (ListView) mainView.findViewById(R.id.lvDetail);
		MixDetialAdapter adapter = new MixDetialAdapter(getActivity());
		lvDetial.setAdapter(adapter);
		adapter.setDataChanged(mixTrans);
		return mainView;
	}

}
