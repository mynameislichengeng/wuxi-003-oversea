package com.wizarpos.pay.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.wizarpos.atool.tool.Tools;
import com.wizarpos.log.util.LogEx;
import com.motionpay.pay2.lite.R;

/**
 * 
 * @ClassName: ScalQrFragment 
 * @author Huangweicai
 * @date 2015-9-1 上午10:59:55 
 * @Description: 放大二维码fragment
 */
public class ScalQrFragment extends Fragment {
		
	private final String LOG_TAG = ScalQrFragment.class.getName();
	private String bitmapUrl;
	private ImageView ivScalQr;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		if(getArguments().containsKey("url"))
		{
			bitmapUrl = getArguments().getString("url");
		}
		if(TextUtils.isEmpty(bitmapUrl))
		{
			getFragmentManager().popBackStack();
			LogEx.e(LOG_TAG, "传递过来的URL为空");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle bundle) {
		View v = inflater.inflate(R.layout.fragment_scal_qr, container, false);
		initView(v);
		return v;
	}

	private void initView(View v) {
		ivScalQr = (ImageView) v.findViewById(R.id.ivScalQr);
		ivScalQr.setImageBitmap(Tools.genQRCode(bitmapUrl));
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
				getFragmentManager().popBackStack();
			}
		});
	}

}
