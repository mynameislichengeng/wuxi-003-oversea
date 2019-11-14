package com.wizarpos.pay.cashier.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.common.view.DashedCircularProgress;
import com.wizarpos.pay.common.view.RunningTextView;
import com.wizarpos.pay2.lite.R;

/** 
 * @Author:Huangweicai
 * @Date:2015-7-29 上午11:24:40
 * @Reason:这里用一句话说明
 */
public class HomeNavFragment extends BaseNavFragment{
	private RunningTextView tvValue;
	private DashedCircularProgress dashedCircularProgress;
	private ObjectAnimator mProgressBarAnimator;
	
	public HomeNavFragment() {
	}
	
	@Override
	public void initView() {
		View mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_nav, null);
		setMainView(mView);
		tvValue = (RunningTextView) mView.findViewById(R.id.tvHomeValue);
//        dashedCircularProgress = (DashedCircularProgress) mView.findViewById(
//                R.id.holoCircularProgressBar);
        updateUI();
	}
	
    
    public void updateUI()
    {
		tvValue.setFormat("00.00");
		tvValue.playNumber(1111.04);
		
//		dashedCircularProgress.reset();
//		dashedCircularProgress.setValue(999);
		
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	LogEx.i("onCreate", "onCreate");
    }

}
