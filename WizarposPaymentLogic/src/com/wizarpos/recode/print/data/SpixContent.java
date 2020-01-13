package com.wizarpos.recode.print.data;

import com.wizarpos.recode.print.constants.PrintTypeEnum;

import java.util.ArrayList;
import java.util.List;

public class SpixContent {

    private final static String CHAR_BC_START = "<bc>";
    private final static String CHAR_BC_END = "</bc>";


    public static List<PrintType> splitFromBC(String content) {
        List<PrintType> mList = new ArrayList<>();
        if (content.contains(CHAR_BC_END)) {
            String reContent = content.replace(CHAR_BC_END, CHAR_BC_START);
            String[] as = reContent.split(CHAR_BC_START);
            for (int i = 0; i < as.length; i++) {
                PrintType printType = new PrintType();

                if (i % 2 == 0) {
                    // 文本
                    printType.setTxt(as[i]);
                    printType.setPrintTypeEnum(PrintTypeEnum.TEXT);
                } else {
                    //一维码
                    String bar = CHAR_BC_START + as[i] + CHAR_BC_END;
                    printType.setTxt(bar);
                    printType.setPrintTypeEnum(PrintTypeEnum.BC);
                }
                mList.add(printType);
            }

        } else {
            PrintType printType = new PrintType();
            printType.setPrintTypeEnum(PrintTypeEnum.TEXT);
            printType.setTxt(content);
            mList.add(printType);
        }
        return mList;
    }


    public static class PrintType {
        private String txt;
        private PrintTypeEnum printTypeEnum;

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public PrintTypeEnum getPrintTypeEnum() {
            return printTypeEnum;
        }

        public void setPrintTypeEnum(PrintTypeEnum printTypeEnum) {
            this.printTypeEnum = printTypeEnum;
        }
    }


}
