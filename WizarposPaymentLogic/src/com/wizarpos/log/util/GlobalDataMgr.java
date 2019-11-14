/**
 * <p>
 * Copyright: Copyright (c) 2012
 * Company: ZTE
 * Description: 全局数据管理类的实现文件
 * </p>
 * @Title GlobalDataMgr.java
 * @Package com.zte.iptvclient.android.uiframe
 * @version 1.0
 * @author jamesqiao10065075
 * @date 2012-7-15
 */
package com.wizarpos.log.util;


import java.util.HashMap;
import java.util.Map;


/** 
 * 全局数据管理类
 * @ClassName:GlobalDataMgr 
 * @Description: 全局数据管理类
 * @author: jamesqiao10065075
 * @date: 2012-7-15
 *  
 */
public class GlobalDataMgr
{
    /** 日志标签 */
    public final static String LOG_TAG = "GlobalDataMgr";
    /** 全局数据区 */
    private Map<String, Object> mmapGlobalDatas = new HashMap<String, Object>();

    /** 唯一实例  */
    private static GlobalDataMgr m_instance = null;

    /**
     * 不允许实例化
     */
    private GlobalDataMgr()
    {

    }

    /**
     * 
     * 获取唯一实例
     * @date 2012-7-15 
     * @author jamesqiao10065075
     * @return 唯一实例
     */
    public static GlobalDataMgr getInstance()
    {
        synchronized (GlobalDataMgr.class)
        {
            if (null != m_instance)
            {
                return m_instance;
            }
            else
            {
                m_instance = new GlobalDataMgr();
                return m_instance;
            }
        }
    }

    /**
     * 保存全局数据
     * @date 2012-7-15 
     * @author jamesqiao10065075
     * @param strKey 关键字，用于获取时使用
     * @param objData 数据
     */
    public void putData(String strKey, Object objData)
    {
        synchronized (mmapGlobalDatas)
        {
            mmapGlobalDatas.put(strKey, objData);
        }
    }

    /**
     * 
     * 获取全局数据，取完后原有数据还在。
     * @date 2012-7-15 
     * @author jamesqiao10065075
     * @param strKey 关键字
     * @return 全局数据
     */
    public Object getData(String strKey)
    {
        Object objDataObject = null;
        synchronized (mmapGlobalDatas)
        {
            objDataObject = mmapGlobalDatas.get(strKey);
        }

        return objDataObject;
    }

    /**
     * 
     * 从全局数据区中获取数据，并从全局数据去删除。
     * @date 2012-7-17 
     * @author jamesqiao10065075
     * @param strKey 数据关键字
     * @return 全局数据
     */
    public Object getAndRemoveData(String strKey)
    {
        Object objDataObject = null;
        synchronized (mmapGlobalDatas)
        {
            objDataObject = mmapGlobalDatas.remove(strKey);
        }

        return objDataObject;
    }
}
