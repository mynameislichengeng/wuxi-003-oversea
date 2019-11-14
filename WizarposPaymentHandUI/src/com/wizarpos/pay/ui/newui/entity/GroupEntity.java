package com.wizarpos.pay.ui.newui.entity;

import java.util.List;

/**
 * Created by vienan on 2015/9/17.
 */
public class GroupEntity {

    private String groupName;
    private List<ChildEntity> childEntities;
    private String amount;

    public GroupEntity(String groupName) {
        this.groupName = groupName;
    }

    public List<ChildEntity> getChildEntities() {
        return childEntities;
    }

    public void setChildEntities(List<ChildEntity> childEntities) {
        this.childEntities = childEntities;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}