package com.wizarpos.pay.ui.newui.entity;

import com.wizarpos.pay.model.DailyDetailResp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Tea on 16/3/24.
 */
public class OrderMap {
    LinkedHashMap<String,List<DailyDetailResp>> linkedHashMap = new LinkedHashMap<>();
    List<String> indexList = new ArrayList<>();


    public void put(String key,List<DailyDetailResp> value) {
        if(linkedHashMap.containsValue(key)) {
            linkedHashMap.get(key).addAll(value);
            return;
        }
        linkedHashMap.put(key,value);
        indexList.add(key);
    }

    public List<DailyDetailResp> getByPosition(int position) {
        if(indexList.size() > 0 && position+1 <= indexList.size()) {
            String key = indexList.get(position);
            if(linkedHashMap.containsKey(key)) {
                return linkedHashMap.get(key);
            }
            return null;
        }
        return null;
    }

    public String getKeyByPosition(int position) {
        if(indexList.size() > 0 && position+1 <= indexList.size()) {
            String key = indexList.get(position);
            return key;
        }
        return "";
    }

    public int size() {
        return linkedHashMap.size();
    }

}
