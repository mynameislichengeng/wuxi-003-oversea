/**
 * <p>
 * Copyright: Copyright (c) 2012
 * Company: ZTE
 * Description: 一般配置信息保存方法管理类的实现文件
 * </p>
 * @Title PerformanceHelper.java
 * @Package com.zte.iptvclient.android.common
 * @version 1.0
 * @author jamesqiao10065075
 * @date 2012-6-19
 */
package com.wizarpos.log.util;


import java.security.InvalidParameterException;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * 
 *  一般配置信息保存方法管理类
 * @ClassName:PreferenceHelper 
 * @Description:  一般配置信息保存方法管理类
 * @author: jamesqiao10065075
 * @date: 2013-2-25
 *
 */
public class PreferenceHelper
{
    /** 日志标签 */
    private static final String LOG_TAG = "PreferenceHelper";

    /** 共享引用 */
    private SharedPreferences mPreferences = null;
    /** 编辑器 */
    private Editor mEditor = null;

    /**
     * 
     * @param context 上下文环境，如果为空，则抛出InvalidParameterException。
     * @param strFileName 保存的配置文件名
     */
    public PreferenceHelper(Context context, String strFileName)
    {
        if (null != context)
        {
            mPreferences = context
                    .getSharedPreferences(strFileName, Context.MODE_PRIVATE);
        }
        else
        {
            throw new InvalidParameterException();
        }

        if (null != mPreferences)
        {
            mEditor = mPreferences.edit();
        }
        else
        {
            LogEx.w(LOG_TAG, "mEditor is null!");
        }
    }

    /**
     * 保存key 对应的值value
     * @param strKey 键值
     * @param value 对应值
     */
    public void putString(String strKey, String value)
    {
        if (null != mEditor)
        {
            mEditor.putString(strKey, value);
            mEditor.commit();
        }
        else
        {
            LogEx.w(LOG_TAG, "mEditor is null!");
        }
    }

    /**
     * 保存key 对应的值value
     * @param strKey 键值
     * @param value 对应值
     */
    public void putBoolean(String strKey, Boolean value)
    {
        if (null != mEditor)
        {
            mEditor.putBoolean(strKey, value);
            mEditor.commit();
        }
        else
        {
            LogEx.w(LOG_TAG, "mEditor is null!");
        }
    }

    /**
     * 保存key 对应的值value
     * @param strKey 键值
     * @param value 对应值
     */
    public void putFloat(String strKey, float value)
    {
        if (null != mEditor)
        {
            mEditor.putFloat(strKey, value);
            mEditor.commit();
        }
        else
        {
            LogEx.w(LOG_TAG, "mEditor is null!");
        }
    }

    /**
     * 保存key 对应的值value
     * @param strKey 键值
     * @param value 对应值
     */
    public void putInt(String strKey, int value)
    {
        if (null != mEditor)
        {
            mEditor.putInt(strKey, value);
            mEditor.commit();
        }
        else
        {
            LogEx.w(LOG_TAG, "mEditor is null!");
        }
    }

    /**
     * 
     * 这里写方法名
     * <p>
     * Description: 这里用一句话描述这个方法的作用
     * <p>
     * @date 2013-5-27 
     * @author jamesqiao10065075
     * @param strKey 关键字
     * @param value 值
     */
    public void putLong(String strKey, long value)
    {
        if (null != mEditor)
        {
            mEditor.putLong(strKey, value);
            mEditor.commit();
        }
        else
        {
            LogEx.w(LOG_TAG, "mEditor is null!");
        }
    }

    /**
     * 
     * 获取所有的键值对数据
     * <p>
     * Description: 获取所有的键值对数据，放入到一个哈希表中。
     * <p>
     * @date 2013-5-27 
     * @author jamesqiao10065075
     * @return 存放全部键值对数据的哈希表
     */
    public Map<String, ?> getAll()
    {
        if (null == mPreferences)
        {
            LogEx.w(LOG_TAG, "mPreferences is null!");
            return null;
        }

        return mPreferences.getAll();
    }

    /**
     * 
     * 根据关键字获取字符串数值
     * <p>
     * Description: 根据关键字获取字符串数值，如果没有，则返回默认值。如果数值类型不是字符串不对，则抛出ClassCastException异常。
     * <p>
     * @date 2013-5-24 
     * @author jamesqiao10065075
     * @param strKey 待查找的关键字
     * @param defValue 没有找到关键字对应的值时，使用这个值返回。
     * @return 返回查找到的数值，如果没有找到，则返回defValue。如果找到的值不是字符串，则抛出ClassCastException异常。
     */
    public String getString(String strKey, String defValue)
    {
        if (mPreferences == null)
        {
            LogEx.w(LOG_TAG, "mPreferences is null,return defValue:" + defValue);
            return defValue;
        }

        return mPreferences.getString(strKey, defValue);
    }

    /**
     * 
     * 根据关键字获取整型数值
     * <p>
     * Description: 根据关键字获取整型数值，如果没有，则返回默认值。如果数值类型不是整型不对，则抛出ClassCastException异常。
     * <p>
     * @date 2013-5-24 
     * @author jamesqiao10065075
     * @param strKey 待查找的关键字
     * @param defValue 没有找到关键字对应的值时，使用这个值返回。
     * @return 返回查找到的数值，如果没有找到，则返回defValue。如果找到的值不是整型，则抛出ClassCastException异常。
     */
    public int getInt(String strKey, int defValue)
    {
        if (mPreferences == null)
        {
            LogEx.w(LOG_TAG, "mPreferences is null,return defValue:" + defValue);
            return defValue;
        }

        return mPreferences.getInt(strKey, defValue);
    }

    /**
     * 
     * 根据关键字获取长整型数值
     * <p>
     * Description: 根据关键字获取长整型数值，如果没有，则返回默认值。如果数值类型不是长整型不对，则抛出ClassCastException异常。
     * <p>
     * @date 2013-5-24 
     * @author jamesqiao10065075
     * @param strKey 待查找的关键字
     * @param defValue 没有找到关键字对应的值时，使用这个值返回。
     * @return 返回查找到的数值，如果没有找到，则返回defValue。如果找到的值不是长整型，则抛出ClassCastException异常。
     */
    public long getLong(String strKey, long defValue)
    {
        if (mPreferences == null)
        {
            LogEx.w(LOG_TAG, "mPreferences is null,return defValue:" + defValue);
            return defValue;
        }

        return mPreferences.getLong(strKey, defValue);
    }

    /**
     * 
     * 根据关键字获取浮点型数值
     * <p>
     * Description: 根据关键字获取浮点型数值，如果没有，则返回默认值。如果数值类型不是浮点型不对，则抛出ClassCastException异常。
     * <p>
     * @date 2013-5-24 
     * @author jamesqiao10065075
     * @param strKey 待查找的关键字
     * @param defValue 没有找到关键字对应的值时，使用这个值返回。
     * @return 返回查找到的数值，如果没有找到，则返回defValue。如果找到的值不是浮点型，则抛出ClassCastException异常。
     */
    public float getFloat(String strKey, float defValue)
    {
        if (mPreferences == null)
        {
            LogEx.w(LOG_TAG, "mPreferences is null,return defValue:" + defValue);
            return defValue;
        }

        return mPreferences.getFloat(strKey, defValue);
    }

    /**
     * 
     * 根据关键字获取布尔型数值
     * <p>
     * Description: 根据关键字获取布尔型数值，如果没有，则返回默认值。如果数值类型不是布尔型不对，则抛出ClassCastException异常。
     * <p>
     * @date 2013-5-24 
     * @author jamesqiao10065075
     * @param strKey 待查找的关键字
     * @param defValue 没有找到关键字对应的值时，使用这个值返回。
     * @return 返回查找到的数值，如果没有找到，则返回defValue。如果找到的值不是布尔型，则抛出ClassCastException异常。
     */
    public boolean getBoolean(String strKey, boolean defValue)
    {
        if (mPreferences == null)
        {
            LogEx.w(LOG_TAG, "mPreferences is null,return defValue:" + defValue);
            return defValue;
        }

        return mPreferences.getBoolean(strKey, defValue);
    }

    /**
     * 
     * 清除指定关键字对应的数据
     * <p>
     * Description: 清除指定关键字对应的数据
     * <p>
     * @date 2013-5-27 
     * @author jamesqiao10065075
     * @param strKey 关键字
     */
    public void remove(String strKey)
    {
        if (null != mEditor)
        {
            mEditor.remove(strKey).commit();
        }
        else
        {
            LogEx.w(LOG_TAG, "mEditor is null!");
        }
    }

    /**
     * 
     * 清除全部保存的数据
     * <p>
     * Description: 清除全部保存的数据
     * <p>
     * @date 2013-5-27 
     * @author jamesqiao10065075
     */
    public void clear()
    {
        if (null != mEditor)
        {
            mEditor.clear().commit();
        }
        else
        {
            LogEx.w(LOG_TAG, "mEditor is null!");
        }
    }
}