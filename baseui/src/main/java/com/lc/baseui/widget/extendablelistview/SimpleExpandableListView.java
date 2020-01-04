package com.lc.baseui.widget.extendablelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.lc.baseui.R;
import com.lc.baseui.widget.listview.CustomerExpandableListView;
import com.lc.librefreshlistview.adapter.BaseRecycleAdapter;

import java.util.ArrayList;

/**
 * Created by licheng on 2017/5/9.
 */

public class SimpleExpandableListView extends RelativeLayout {
    public SimpleExpandableListView(Context context) {
        super(context);
        init(context);
    }

    public SimpleExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected Context context;

    protected CustomerExpandableListView elOne, elTwo, elThree, elFour;
    private ArrayList<CustomerExpandableListView> listViewArrayList = new ArrayList<>();
    private int counts;

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.widget_customer_expandable_listview_num4, null);
        elOne = (CustomerExpandableListView) view.findViewById(R.id.el_one);
        elTwo = (CustomerExpandableListView) view.findViewById(R.id.el_two);
        elThree = (CustomerExpandableListView) view.findViewById(R.id.el_three);
        elFour = (CustomerExpandableListView) view.findViewById(R.id.el_four);
        listViewArrayList.add(elOne);
        listViewArrayList.add(elTwo);
        listViewArrayList.add(elThree);
        listViewArrayList.add(elFour);
        addView(view);
    }


    /**
     * 设置显示几个
     **/
    public void setVisible(int nums) {
        counts = nums;
        for (int i = 0; i < nums; i++) {
            listViewArrayList.get(i).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置title
     **/
    public void setTitle(String[] strs) {
        for (int i = 0; i < strs.length; i++) {
            listViewArrayList.get(i).setParentText(strs[i]);
        }
    }

    /**
     * 是否显示底部
     **/
    public void setBottomVisible(int position, int visible) {
        listViewArrayList.get(position).setBottomVisible(visible);
    }

    /**
     * 设置底部text
     **/
    public void setBottomText(int position, String str) {
        listViewArrayList.get(position).setBtnText(str);
    }

    /**
     * 设置底部响应时间
     **/
    public void setBottomOnClick(int position, View.OnClickListener clickListener) {
        listViewArrayList.get(position).setButtonClickEvent(clickListener);
    }

    /**
     * 设置第一个adapter
     **/
    public void setAdapterOne(BaseRecycleAdapter adapter) {
        elOne.setAdapter(adapter);
    }

    /**
     * 设置第一个adapter
     **/
    public void setAdapterTwo(BaseRecycleAdapter adapter) {
        elTwo.setAdapter(adapter);
    }

    /**
     * 设置第一个adapter
     **/
    public void setAdapterThree(BaseRecycleAdapter adapter) {
        elThree.setAdapter(adapter);
    }

    /**
     * 设置第一个adapter
     **/
    public void setAdapterFour(BaseRecycleAdapter adapter) {
        elFour.setAdapter(adapter);
    }
}
