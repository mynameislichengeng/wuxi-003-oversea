package com.wizarpos.recode.print.constants;

public interface PrintConstants {

    /**
     * 左右对齐特殊
     */
    String LEFT_RIGHT_MARK = "@#&";

    enum PrintContentPartity {
        BC(HtmlRemarkConstans.BARCODE_START.getValue(), HtmlRemarkConstans.BARCODE_END.getValue()),
        QC(HtmlRemarkConstans.QRCODE_START.getValue(), HtmlRemarkConstans.QRCODE_END.getValue()),
        COMMON("", ""),
        ;

        private String start;
        private String end;

        PrintContentPartity(String start, String end) {
            this.start = start;
            this.end = end;
        }

        public String getStart() {
            return start;
        }

        public String getEnd() {
            return end;
        }

        public static PrintContentPartity getEnumFromStart(String start) {
            PrintContentPartity[] enums = PrintContentPartity.values();
            for (PrintContentPartity p : enums) {
                if (p.getStart().equals(start)) {
                    return p;
                }
            }
            return null;
        }
    }

    class PartityItem {
        String content;
        PrintContentPartity type;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public PrintContentPartity getType() {
            return type;
        }

        public void setType(PrintContentPartity type) {
            this.type = type;
        }
    }
}
