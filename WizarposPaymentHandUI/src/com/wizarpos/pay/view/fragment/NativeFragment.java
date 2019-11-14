package com.wizarpos.pay.view.fragment;

import com.wizarpos.pay2.lite.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * 
 * @ClassName: MicroFragment 
 * @author Huangweicai
 * @date 2015-8-31 下午6:20:02 
 * @Description: 有摄像头的Fragment 被扫
 */
public class NativeFragment extends BaseFragment{

	@Override
	public void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setMainView(R.layout.fragment_micro);
	}

}
