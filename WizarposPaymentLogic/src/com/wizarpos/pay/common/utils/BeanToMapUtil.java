package com.wizarpos.pay.common.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @Author: Huangweicai
 * @date 2015-11-26
 * @Description: beanת转化为Map的工具
 */
public class BeanToMapUtil {

    /**
     * 实体转化为Map<String, Object>
     * @param obj
     * @return
     * @throws Exception
     * @author HWC
     */
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }
}
