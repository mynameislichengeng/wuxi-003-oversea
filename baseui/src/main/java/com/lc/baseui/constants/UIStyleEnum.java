package com.lc.baseui.constants;

/**
 * Created by licheng on 2017/5/2.
 */

public enum UIStyleEnum {

    TEXTVIEW("1"),
    EDITVIEW("2"),
    EDITVIEW_NUM_ZIMU("3"),//只能0～9和a~z和A~Z
    ;
    private String tag;

    public String getTag() {
        return tag;
    }

    UIStyleEnum(String tag) {
        this.tag = tag;
    }
}
