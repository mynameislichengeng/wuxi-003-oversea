package com.lc.baseui.task;

import com.lc.baseui.init.BaseModuleManager;

/**
 * Created by licheng on 2017/5/1.
 */

public class BaseUIModuleManager extends BaseModuleManager{
   private static  BaseUIModuleManager instance;

   public static BaseUIModuleManager getInstance(){
       if(instance==null){
           instance = new BaseUIModuleManager();
       }
       return instance;
   }



   /**
    * 获得反射R数据的能力,array
    * **/
    public int getResourceArrayField(String field){
        return ((BaseUiRegisterListener)registerListener).getResourceArrayField(field);
    }
    /**
     * 获得反射R数据的能力,string
     * **/
    public int getResourceStringField(String field){
        return ((BaseUiRegisterListener)registerListener).getResourceStringField(field);
    }

    /**
     * 获得sessionId
     **/
    public String getSessionId(){
        return registerListener.getSessionId();
    }
    /**
     * 获得是不是企业版
     * **/
    public boolean isPersonal(){
        return ((BaseUiRegisterListener) registerListener).isPersonal();
    }
}
