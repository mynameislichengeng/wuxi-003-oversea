package com.lc.baseui.listener.adapterUI;

import com.lc.baseui.bean.var.ItemBean;

/**
 * 这个adapter每一项时的响应时间
 * Created by licheng on 2017/4/30.
 */

public interface CustomerEventAdapterCallback<T> {
    /**
     * 执行自定义事件
     * **/
    void doCustomerEvent(int position, ItemBean items, T t);
}
