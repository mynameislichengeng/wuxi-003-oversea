package com.wizarpos.pay.common.channel;

import android.content.Context;

/**
 * Created by wu on 15/12/15.
 */
public class Channel {

    public static String getChannelName(Context context) {
        return PackerNg.getMarket(context);
    }

}
