package com.wizarpos.device.printer.html;

import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.device.printer.html.model.HtmlLine;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;

/**
 * 生成html文件
 * Created by Song on 2017/10/26.
 */

public class ToHTMLUtil {
    public static final String FONT_SIZE_DEFAULT = "15px";
    public static final String FONT_SIZE_Q2 = "12px";
    public static String fontSize = FONT_SIZE_DEFAULT;


    public static final String htmlEnd = "</body></html>";

    public static String getHtmlHead(){
        return "<!DOCTYPE html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>print</title>\n" +
                "<style>\n" +
                "body{ font-size:" + fontSize + ";text-align:left; margin:0px; padding:0px;}\n" +
                "p,table{margin:0px; padding:0px;}\n" +
                "table{ width:100%;}\n" +
                "</style>" +
                "</head>\n" +
                "<body>";
    }
    public static String toHtml(String printStr) {
        String result = getHtmlHead();
        result += convert(printStr);
        result += htmlEnd;
        return result;
    }

    public static String toHtml(HTMLPrintModel model) {
        String result = convert(model);
        return result;
    }

    private static String convert(HTMLPrintModel model) {
        String result = model.getHtmlHead();
        boolean isTable = false;//改变时需要另外添加"</table>"
        for (HtmlLine line : model.getLineList()) {
            if (line instanceof HTMLPrintModel.ThreeStrLine) {
                if (!isTable) {
                    result += "<table>";
                }
                isTable = true;
            } else {
                if (isTable) {
                    result += "</table>";
                }
                isTable = false;
            }
            result += line.convertline();
        }
        result += new Q1PrintBuilder().endPrint();
        result += model.getHtmlEnd();
        return result;
    }

    private static String convert(String printStr) {
        return printStr;
    }

}
