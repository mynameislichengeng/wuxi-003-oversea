package com.wizarpos.pay.common.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wizarpos.wizarpospaymentlogic.R;

public class BaseFragment extends Fragment {
	protected TextView tvTitle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_test_base, null);
		tvTitle = (TextView) view.findViewById(R.id.title);
		init();
		return view;
	}

	protected void init() {

	}

}
