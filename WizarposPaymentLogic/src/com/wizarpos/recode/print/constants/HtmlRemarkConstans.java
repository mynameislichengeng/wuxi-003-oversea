package com.wizarpos.recode.print.constants;

public enum HtmlRemarkConstans {
    LEFT_START("<l>"),
    LEFT_END("</l>"),

    RIGHT_START("<r>"),
    RIGHT_END("</r>"),

    CENTER_START("<c>"),
    CENTER_END("</c>"),

    LEFT_RIGHT_START("<lr>"),//2端对齐
    LEFT_RIGHT_END("</lr>"),//2端对齐

    BOLD_END("<b>"),//


    BARCODE_START("<bc>"),
    BARCODE_END("</bc>"),

    LINE("<br/>"),
    LINE_N("<nbr/>"),
//    LINE_TAG_NEW("<line/>"),//换行
    LINE_SPACE_NEW("<linsp/>"),//空行
    END("<end/>"),
    ;
    private String value;

    HtmlRemarkConstans(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
