package com.wizarpos.pay.view.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @ClassName: MapValueGetKey 
 * @author Admin
 * @date 2015-9-15 下午3:50:09 
 * @Description: 根据value获得key
 */
public class MapValueGetKey {
	Map map;

	public MapValueGetKey(Map map) { // 初始化操作
		this.map = map;
	}

	public Object getKey(Object value) {
		Object o = null;
		ArrayList all = new ArrayList(); // 建一个数组用来存放符合条件的KEY值
		Set set = map.entrySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (entry.getValue().equals(value)) {
				o = entry.getKey();
				return o;
			}
		}
		return null;
	}
}