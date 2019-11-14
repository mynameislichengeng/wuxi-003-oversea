package com.wizarpos.device.printer.html.model;

import com.wizarpos.device.printer.html.ToHTMLUtil;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * html打印实体
 * Created by Song on 2017/10/27.
 */

public class HTMLPrintModel implements Serializable {

    private String htmlHead = ToHTMLUtil.getHtmlHead();
    private List<HtmlLine> lineList;
    private String htmlEnd = ToHTMLUtil.htmlEnd;

    public List<HtmlLine> getLineList() {
        return lineList;
    }

    public void setLineList(List<HtmlLine> lineList) {
        this.lineList = lineList;
    }

    public String getHtmlEnd() {
        return htmlEnd;
    }

    public void setHtmlEnd(String htmlEnd) {
        this.htmlEnd = htmlEnd;
    }

    public String getHtmlHead() {
        return htmlHead;
    }

    public void setHtmlHead(String htmlHead) {
        this.htmlHead = htmlHead;
    }

    public static class SimpleLine extends HtmlLine {

        private boolean isCenter;
        private String lineStr;

        public SimpleLine(String lineStr) {
            this.lineStr = lineStr;
        }

        public SimpleLine(String lineStr, boolean bold, boolean isCenter) {
            this.lineStr = lineStr;
            this.isCenter = isCenter;
            setBold(bold);
        }



        public String getLineStr() {
            return lineStr;
        }

        public void setLineStr(String lineStr) {
            this.lineStr = lineStr;
        }

        @Override
        public String convertline() {
            Q1PrintBuilder pb = new Q1PrintBuilder();
            String line = lineStr;
            line = isBold() ? pb.bold(line) : line;
            line = isCenter ? "<center>" + line + "</center>" : line;
            return line + (isCenter ? "" : "</br>");
        }

        public boolean isCenter() {
            return isCenter;
        }

        public void setCenter(boolean center) {
            isCenter = center;
        }
    }

    public static class LeftAndRightLine extends HtmlLine {

        private String leftStr, rightStr;

        public LeftAndRightLine(String leftStr, String rightStr) {
            this.leftStr = leftStr;
            this.rightStr = rightStr;
        }

        public String getLeftStr() {
            return leftStr;
        }

        public void setLeftStr(String leftStr) {
            this.leftStr = leftStr;
        }

        public String getRightStr() {
            return rightStr;
        }

        public void setRightStr(String rightStr) {
            this.rightStr = rightStr;
        }

        @Override
        public String convertline() {
            Q1PrintBuilder pb = new Q1PrintBuilder();
            return "<p style=\" width:100%;clear:both;\"><label style=\"float:left;\">" +
                    (isBold() ? pb.bold(leftStr) : leftStr) +
                    "</label><p style=\"float:right; text-align:right;\">" +
                    (isBold() ? pb.bold(rightStr) : rightStr) +
                    "</p></p></br>";
        }
    }

    public static class ThreeStrLine extends HtmlLine {

        private String leftStr, centerStr, rightStr;

        public ThreeStrLine(String leftStr, String centerStr, String rightStr) {
            this.leftStr = leftStr;
            this.centerStr = centerStr;
            this.rightStr = rightStr;
        }

        public String getLeftStr() {
            return leftStr;
        }

        public void setLeftStr(String leftStr) {
            this.leftStr = leftStr;
        }

        public String getCenterStr() {
            return centerStr;
        }

        public void setCenterStr(String centerStr) {
            this.centerStr = centerStr;
        }

        public String getRightStr() {
            return rightStr;
        }

        public void setRightStr(String rightStr) {
            this.rightStr = rightStr;
        }

        @Override
        public String convertline() {
            Q1PrintBuilder pb = new Q1PrintBuilder();
            return "<tr>" +
                    "<td style=\"text-align:left;\">" + (isBold() ? pb.bold(leftStr) : leftStr) + "</td>" +
                    "<td style=\"text-align:center;\">" + (isBold() ? pb.bold(centerStr) : centerStr) + "</td>" +
                    "<td style=\"text-align:right;\">" + (isBold() ? pb.bold(rightStr) : rightStr) + "</td>" +
                    "</tr>";
        }
    }

    public static class EmptyLine extends HtmlLine {

        @Override
        public String convertline() {
            return "</br>";
        }
    }

    public static class BranchLine extends HtmlLine {

        @Override
        public String convertline() {
            return "<center>------------------------------------------------------</center>";
        }
    }
}
