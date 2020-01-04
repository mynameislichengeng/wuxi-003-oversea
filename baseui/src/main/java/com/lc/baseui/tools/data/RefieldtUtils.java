package com.lc.baseui.tools.data;

import java.lang.reflect.Field;

/**
 * 反射的工具类
 * Created by Administrator on 2017/4/24.
 */

public class RefieldtUtils {

    /**
     * 反射得到指定字段的值
     *
     * @param t           对象
     * @param filedString 对象t的字段
     **/
    public static <T, E> E getFieldValue(T t, String filedString) {
        if(t==null){
            return null;
        }
        Class<T> cls = (Class<T>) t.getClass();
        E value = null;
        try {
            Field field = cls.getDeclaredField(filedString);
            field.setAccessible(true);
            value = (E) field.get(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 设置值
     **/
    public static <T, E> void setFieldValue(T t, String filedString, E value) {
        if(t==null){
            return;
        }
        Class<T> cls = (Class<T>) t.getClass();
        try {
            Field field = cls.getDeclaredField(filedString);
            field.setAccessible(true);
            field.set(t, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
