package com.wizarpos.recode.print.constants;

public class KeyWords {

    public static String[] keywords = {
            "<b>", "</b>",        // 加粗
            "<w>", "</w>",        // 加宽
            "<h>", "</h>",        // 加高
            HtmlRemarkConstans.LEFT_START.getValue(), HtmlRemarkConstans.LEFT_END.getValue(), // 居左
            HtmlRemarkConstans.CENTER_START.getValue(), HtmlRemarkConstans.CENTER_END.getValue(),        // 居中
            HtmlRemarkConstans.RIGHT_START.getValue(), HtmlRemarkConstans.RIGHT_END.getValue(), // 居右
            HtmlRemarkConstans.LEFT_RIGHT_START.getValue(), HtmlRemarkConstans.LEFT_RIGHT_END.getValue(),    //
            HtmlRemarkConstans.BARCODE_START.getValue(), HtmlRemarkConstans.BARCODE_END.getValue(),    // 一维码
            HtmlRemarkConstans.QRCODE_START.getValue(), HtmlRemarkConstans.QRCODE_END.getValue(),    // 二维码
            "<t/>",                // tab
            "<ul>", "</ul>",    // 下划线
            "<img>", "</img>",    // 图片
            "<1>", "</1>",        // 1倍字体
            "<2>", "</2>",        // 2倍字体
            "<3>", "</3>",        // 3倍字体
            "<4>", "</4>",        // 4倍字体
            "<5>", "</5>",        // 5倍字体
            "<6>", "</6>",        // 6倍字体
            "<7>", "</7>",        // 7倍字体
            "<8>", "</8>",        // 8倍字体
            "<s>", "</s>",        // 小字体
            "<sls>", "</sls>",     // 小行间距


            HtmlRemarkConstans.LINE.getValue(),            // 换行
            HtmlRemarkConstans.LINE_N.getValue(),           //N3或者N5的换行
//            HtmlRemarkConstans.LINE_TAG_NEW.getValue(),  //新的换行
            HtmlRemarkConstans.LINE_SPACE_NEW.getValue(),  //新的空格行


            HtmlRemarkConstans.END.getValue()             // 打印完成
    };

}
