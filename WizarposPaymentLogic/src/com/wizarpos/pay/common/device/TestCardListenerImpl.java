package com.wizarpos.pay.common.device;

import android.os.Handler;

import com.wizarpos.base.net.Response;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter.ResultListener;

/**
 * 测试模式刷卡实现
 * 
 * @author wu
 */
public class TestCardListenerImpl implements CardListener {

    @Override
    public void getTrack2(Handler handler, final ResultListener listener) {
        listener.onSuccess(new Response(0, "success", Constants.TEST_CARD_NO));
    }

    @Override
    public void setSwipeCardListener(Handler handler, ResultListener listener) {

    }

    @Override
	public void close() {

	}

}
