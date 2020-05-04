package com.wizarpos.device.printer;

import com.wizarpos.recode.print.service.PrintHandleService;

import java.util.Arrays;

public class KeywordTrigger {

    private String[] keywords = null;
    private char[] cs = new char[0];
    private PrintHandleService handle = null;

    private int maxKeywordLength = 0;

    public KeywordTrigger(String... keywords) {
        this.keywords = keywords;
        for (String keyword : keywords) {
            if (keyword.length() > maxKeywordLength) {
                maxKeywordLength = keyword.length();
            }
        }
    }

    public void parse() {
        try {
            StringBuilder sb = new StringBuilder();

            char[] tcs = new char[maxKeywordLength];
            for (int i = 0; i < cs.length; i++) {
                char c = cs[i];
                appendChar(tcs, c); // 临时关键字容器填充
                sb.append(c);

                for (String keyword : keywords) {
                    char[] myKeywords = keyword.toCharArray();
                    // 从临时关键字容器中提取关键字，并与指定关键字比较
                    if (Arrays.equals(extractCharArray(tcs, myKeywords), myKeywords) == true) {
                        // 从字符串中删除关键字
                        sb.delete(sb.length() - myKeywords.length, sb.length());
                        String str = sb.toString();

                        if (str != null && !"".equals(str)) {
                            handle.contentTrigger(str);
                        }
                        handle.keywordTrigger(new String(myKeywords));

                        sb = new StringBuilder();
                        tcs = new char[maxKeywordLength];
                        continue;
                    }
                }
            }
            handle.contentTrigger(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 追加字符，在数组首位置的字符将被抛弃
     *
     * @param cs
     * @param c
     */
    private static void appendChar(char[] cs, char c) {
        int length = cs.length;
        for (int i = 0; i < length; i++) {
            if (i < length - 1) {
                cs[i] = cs[i + 1];
            }
        }
        cs[length - 1] = c;
    }

    private static char[] extractCharArray(char[] cs, char[] kwcs) {
        if (cs.length < kwcs.length) {
            throw new IllegalArgumentException("cs length must greater than kwcs length");
        }
        char[] ncs = new char[kwcs.length];
        int st = cs.length - kwcs.length;
        for (int i = 0; i < cs.length; i++) {
            ncs[i] = cs[i + st];
            if (i + st >= cs.length - 1) {
                break;
            }
        }
        return ncs;
    }

    public void setSource(String src) {
        cs = src.toCharArray();
    }



    public void setHandle(PrintHandleService handle) {
        this.handle = handle;
    }


}
