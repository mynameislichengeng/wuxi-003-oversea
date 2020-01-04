package com.lc.baseui.task;

import com.lc.baseui.init.BaseRegisterListener;

/**
 * Created by licheng on 2017/5/1.
 */

public interface BaseUiRegisterListener extends BaseRegisterListener{

    int getResourceArrayField(String s);

     int getResourceStringField(String s);



    boolean isPersonal();
}
