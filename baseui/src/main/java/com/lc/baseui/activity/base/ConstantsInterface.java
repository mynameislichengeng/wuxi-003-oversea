package com.lc.baseui.activity.base;

/**
 * 用于存储一些常量
 * Created by Administrator on 2017/4/11.
 */

public interface ConstantsInterface {
    /**
     * intent中sessionId，用于传值
     **/
    String INTENT_SESSIONID = "sessionId";
    /**
     * intent中style
     **/
    String INTENT_STYLE = "style";
    /**
     * intent中的operate
     **/
    String INTENT_OPERATE = "operate";
    /**
     * id号
     */
    String INTENT_ID = "id";
    /**
     * 实体bean
     **/
    String INTENT_DATA = "data";
    //
    int INTENT_RESULT_ID = 100;
    int INTENT_REQUEST_ID = 100;
    int INTENT_RESULT_DATA = 200;
    int INTENT_REQUEST_DATA = 200;


    //增加，删除，更新
    String OPERATE_ADD = "add";
    String OPERATE_UPDATE = "update";
    String OPERATE_DELETE = "delete";
    String OPERATE_QUERY = "query";
    /**
     * action,用于启动应用工程的主界面
     **/
    String ACTION_MAIN_PROJECT = "android.intent.action.haian.project";

    /**
     * 每页显示的数量
     **/
    String PAGENUMS = "20";
}
