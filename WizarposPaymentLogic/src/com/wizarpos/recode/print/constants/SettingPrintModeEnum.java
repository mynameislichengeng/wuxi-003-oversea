package com.wizarpos.recode.print.constants;

public enum SettingPrintModeEnum {
    MODE_0("0", "0"),
    MODE_1("1", "1"),
    MODE_2("2", "2"),
//    MODE_3("3", "3"),
    ;
    private String mode;
    private String show;

    SettingPrintModeEnum(String mode, String show) {
        this.mode = mode;
        this.show = show;
    }

    public String getMode() {
        return mode;
    }

    public String getShow() {
        return show;
    }

    public static SettingPrintModeEnum getEnumFromMode(String mode) {
        SettingPrintModeEnum[] modes = SettingPrintModeEnum.values();
        for (SettingPrintModeEnum mo : modes) {
            if (mo.mode.equals(mode)) {
                return mo;
            }
        }
        return SettingPrintModeEnum.MODE_0;
    }

    public static SettingPrintModeEnum getEnumFromShow(String show) {
        SettingPrintModeEnum[] modes = SettingPrintModeEnum.values();
        for (SettingPrintModeEnum mo : modes) {
            if (mo.show.equals(show)) {
                return mo;
            }
        }
        return SettingPrintModeEnum.MODE_0;
    }
}
