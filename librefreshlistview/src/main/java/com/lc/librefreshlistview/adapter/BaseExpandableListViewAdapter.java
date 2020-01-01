package com.lc.librefreshlistview.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;


import com.lc.librefreshlistview.holder.ViewHolder;

import java.util.ArrayList;

/**
 * Created by licheng on 2017/5/7.
 */

public abstract class BaseExpandableListViewAdapter<T, E> extends BaseExpandableListAdapter {

    protected boolean isChildrenSelectAble;//子项是否可以选中
    protected ArrayList<E> parentLists;
    protected ArrayList<T> childrenLists;
    protected Context context;
    protected boolean isParentOveried = true;
    protected boolean isChildrenOveried = true;

    @Override
    public int getGroupCount() {
        return parentLists != null ? parentLists.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childrenLists != null ? childrenLists.size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {

        return parentLists.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childrenLists.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return isChildrenSelectAble;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (isParentOveried) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(getParentLayout(), null);
            }
        } else {
            convertView = LayoutInflater.from(context).inflate(getParentLayout(), null);
        }

        bindParentView(convertView, groupPosition);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (isChildrenOveried) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(getChildrenLayout(), null);
            }
        } else {
            convertView = LayoutInflater.from(context).inflate(getChildrenLayout(), null);
        }
        bindChildrenView(convertView, groupPosition, childPosition);
        return convertView;
    }


    public void setChildrenSelectAble(boolean childrenSelectAble) {
        isChildrenSelectAble = childrenSelectAble;
    }

    public void setChildrenLists(ArrayList<T> childrenLists) {
        this.childrenLists = childrenLists;
    }

    public abstract int getParentLayout();

    public abstract int getChildrenLayout();

    /**
     * 初始父类控件
     **/
    public abstract void bindParentView(View view, int position);

    /**
     * 初始子类控件
     **/
    public abstract void bindChildrenView(View view, int parentPosition, int childrenPosition);


    /**
     * 快速产生View
     **/
    protected View getWidget(View view, int id) {
        return ViewHolder.get(view, id);
    }

    /**
     * 解析
     **/
    protected String[] parsePart(String str) {
        String[] str1 = str.split(":");
        return str1;
    }

    /**
     *
     * **/
    protected String[] parseKeyPart(String str) {
        String[] str1 = str.split("/");
        return str1;
    }

    protected String[] parseDouyPart(String str) {
        String[] str1 = str.split(",");
        return str1;
    }
}
