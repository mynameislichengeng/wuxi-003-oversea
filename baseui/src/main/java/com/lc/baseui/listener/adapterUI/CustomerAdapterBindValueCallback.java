package com.lc.baseui.listener.adapterUI;

import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;

import java.util.ArrayList;

/**
 * 在adapter中如何来定义值，目前是用来绑定adapter右边的value
 * Created by licheng on 2017/5/1.
 */

public interface CustomerAdapterBindValueCallback<T> {

    /**
     * 绑定value的值
     *@param adapter 对应的adapter
     * @param field    属性字段，用于反射
     * @param position 位置
     * @param t        实体
     **/
    String bindValueOne(BaseRecycleAdapter adapter,int position, ArrayList<T> t, String[] field);


}
