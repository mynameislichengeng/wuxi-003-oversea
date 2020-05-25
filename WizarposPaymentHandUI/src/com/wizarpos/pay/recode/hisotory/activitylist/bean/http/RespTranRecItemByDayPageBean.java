package com.wizarpos.pay.recode.hisotory.activitylist.bean.http;

import java.util.List;

/**
 * 这是965按天分页
 */
public class RespTranRecItemByDayPageBean {


    /**
     * counts : {"autoCount":true,"first":1,"hasNext":true,"hasPre":false,"nextPage":2,"orderBySetted":false,"pageNo":1,"pageSize":2,"prePage":1,"result":[{"tranTime":"2020-05-15"},{"tranTime":"2020-05-13"}],"totalCount":4,"totalPages":2}
     * logs : [{"transDetail":[{"cnyAmount":5,"discountAmount":0,"exchangeRate":5.063353,"masterTranLogId":"P100100020000067202005160002","merchantTradeCode":"rer","optName":"Cashier0001(1001)","orderNo":"01100100020000067202005160002","payTime":"2020-05-15 21:59:18","refundAmount":0,"settlementAmount":1,"settlementCurrency":"CAD","sn":"G3100000003","thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2020051622001409181110159760","tipAmount":0,"tranLogId":"P100100020000067202005160002","tranTime":"2020-05-15 21:59:18","transAmount":1,"transCurrency":"CAD","transKind":"Pay","transType":"813"},{"cnyAmount":5,"discountAmount":0,"exchangeRate":5.063353,"masterTranLogId":"P100100020000067202005160001","merchantTradeCode":"bnbnbn","optName":"Cashier0001(1001)","orderNo":"01100100020000067202005160001","payTime":"2020-05-15 21:58:26","refundAmount":0,"settlementAmount":1,"settlementCurrency":"CAD","sn":"G3100000003","thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2020051622001409181110212569","tipAmount":0,"tranLogId":"P100100020000067202005160001","tranTime":"2020-05-15 21:58:26","transAmount":1,"transCurrency":"CAD","transKind":"Pay","transType":"813"}],"transTime":"2020-05-15"},{"transDetail":[{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.04280044,"masterTranLogId":"P100100020000067202005140002","merchantTradeCode":"alltmm","optName":"Cashier0001(1001)","orderNo":"02100100020000067202005140003","payTime":"2020-05-13 23:30:54","refundAmount":-1,"settlementAmount":-1,"settlementCurrency":"CAD","sn":"N300W110614","thirdExtId":"o8HL9wc_UjBQMzwlCj6icjLePj4g","thirdExtName":"","thirdTradeNo":"4200000536202005141236695347","tipAmount":0,"tranLogId":"P100100020000067202005140003","tranTime":"2020-05-13 23:30:54","transAmount":-1,"transCurrency":"CAD","transKind":"Refund","transType":"820"},{"cnyAmount":5,"discountAmount":0,"exchangeRate":5.04280044,"masterTranLogId":"P100100020000067202005140002","merchantTradeCode":"alltmm","optName":"Cashier0001(1001)","orderNo":"02100100020000067202005140001","payTime":"2020-05-13 23:28:39","refundAmount":1,"settlementAmount":1,"settlementCurrency":"CAD","sn":"N300W110614","thirdExtId":"o8HL9wc_UjBQMzwlCj6icjLePj4g","thirdExtName":"","thirdTradeNo":"4200000536202005141236695347","tipAmount":0,"tranLogId":"P100100020000067202005140002","tranTime":"2020-05-13 23:28:39","transAmount":1,"transCurrency":"CAD","transKind":"Pay","transType":"814"},{"cnyAmount":-11,"discountAmount":0,"exchangeRate":5.0656,"masterTranLogId":"P100100020000067202005130003","merchantTradeCode":"","optName":"Cashier0001(1001)","orderNo":"01100100020000067202005140001","payTime":"2020-05-13 23:26:53","refundAmount":-11,"settlementAmount":-2,"settlementCurrency":"CAD","sn":"N300W110614","thirdExtId":"2088002951112302","thirdTradeNo":"2020051322001412301423283106","tipAmount":0,"tranLogId":"P100100020000067202005140001","tranTime":"2020-05-13 23:26:53","transAmount":-11,"transCurrency":"CNY","transKind":"Refund","transType":"809"}],"transTime":"2020-05-13"}]
     */

    private CountsBean counts;
    private List<LogsBean> logs;

    public CountsBean getCounts() {
        return counts;
    }

    public void setCounts(CountsBean counts) {
        this.counts = counts;
    }

    public List<LogsBean> getLogs() {
        return logs;
    }

    public void setLogs(List<LogsBean> logs) {
        this.logs = logs;
    }

    public static class CountsBean {
        /**
         * autoCount : true
         * first : 1
         * hasNext : true
         * hasPre : false
         * nextPage : 2
         * orderBySetted : false
         * pageNo : 1
         * pageSize : 2
         * prePage : 1
         * result : [{"tranTime":"2020-05-15"},{"tranTime":"2020-05-13"}]
         * totalCount : 4
         * totalPages : 2
         */

        private boolean autoCount;
        private int first;
        private boolean hasNext;
        private boolean hasPre;
        private int nextPage;
        private boolean orderBySetted;
        private int pageNo;
        private int pageSize;
        private int prePage;
        private int totalCount;
        private int totalPages;
        private List<ResultBean> result;

        public boolean isAutoCount() {
            return autoCount;
        }

        public void setAutoCount(boolean autoCount) {
            this.autoCount = autoCount;
        }

        public int getFirst() {
            return first;
        }

        public void setFirst(int first) {
            this.first = first;
        }

        public boolean isHasNext() {
            return hasNext;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }

        public boolean isHasPre() {
            return hasPre;
        }

        public void setHasPre(boolean hasPre) {
            this.hasPre = hasPre;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public boolean isOrderBySetted() {
            return orderBySetted;
        }

        public void setOrderBySetted(boolean orderBySetted) {
            this.orderBySetted = orderBySetted;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * tranTime : 2020-05-15
             */

            private String tranTime;

            public String getTranTime() {
                return tranTime;
            }

            public void setTranTime(String tranTime) {
                this.tranTime = tranTime;
            }
        }
    }

    public static class LogsBean {
        /**
         * transDetail : [{"cnyAmount":5,"discountAmount":0,"exchangeRate":5.063353,"masterTranLogId":"P100100020000067202005160002","merchantTradeCode":"rer","optName":"Cashier0001(1001)","orderNo":"01100100020000067202005160002","payTime":"2020-05-15 21:59:18","refundAmount":0,"settlementAmount":1,"settlementCurrency":"CAD","sn":"G3100000003","thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2020051622001409181110159760","tipAmount":0,"tranLogId":"P100100020000067202005160002","tranTime":"2020-05-15 21:59:18","transAmount":1,"transCurrency":"CAD","transKind":"Pay","transType":"813"},{"cnyAmount":5,"discountAmount":0,"exchangeRate":5.063353,"masterTranLogId":"P100100020000067202005160001","merchantTradeCode":"bnbnbn","optName":"Cashier0001(1001)","orderNo":"01100100020000067202005160001","payTime":"2020-05-15 21:58:26","refundAmount":0,"settlementAmount":1,"settlementCurrency":"CAD","sn":"G3100000003","thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2020051622001409181110212569","tipAmount":0,"tranLogId":"P100100020000067202005160001","tranTime":"2020-05-15 21:58:26","transAmount":1,"transCurrency":"CAD","transKind":"Pay","transType":"813"}]
         * transTime : 2020-05-15
         */

        private String transTime;
        private List<TransDetailBean> transDetail;

        public String getTransTime() {
            return transTime;
        }

        public void setTransTime(String transTime) {
            this.transTime = transTime;
        }

        public List<TransDetailBean> getTransDetail() {
            return transDetail;
        }

        public void setTransDetail(List<TransDetailBean> transDetail) {
            this.transDetail = transDetail;
        }

        public static class TransDetailBean {
            /**
             * cnyAmount : 5
             * discountAmount : 0
             * exchangeRate : 5.063353
             * masterTranLogId : P100100020000067202005160002
             * merchantTradeCode : rer
             * optName : Cashier0001(1001)
             * orderNo : 01100100020000067202005160002
             * payTime : 2020-05-15 21:59:18
             * refundAmount : 0
             * settlementAmount : 1
             * settlementCurrency : CAD
             * sn : G3100000003
             * thirdExtId : 2088212038909181
             * thirdExtName : 223***@qq.*
             * thirdTradeNo : 2020051622001409181110159760
             * tipAmount : 0
             * tranLogId : P100100020000067202005160002
             * tranTime : 2020-05-15 21:59:18
             * transAmount : 1
             * transCurrency : CAD
             * transKind : Pay
             * transType : 813
             *
             */

            private int cnyAmount;
            private int discountAmount;
            private double exchangeRate;
            private String masterTranLogId;
            private String merchantTradeCode;
            private String optName;
            private String orderNo;
            private String payTime;
            private int refundAmount;
            private int settlementAmount;
            private String settlementCurrency;
            private String sn;
            private String thirdExtId;
            private String thirdExtName;
            private String thirdTradeNo;
            private int tipAmount;
            private String tranLogId;
            private String tranTime;
            private int transAmount;
            private String transCurrency;
            private String transKind;
            private String transType;

            private String diffCode;//相当于remark

            public int getCnyAmount() {
                return cnyAmount;
            }

            public void setCnyAmount(int cnyAmount) {
                this.cnyAmount = cnyAmount;
            }

            public int getDiscountAmount() {
                return discountAmount;
            }

            public void setDiscountAmount(int discountAmount) {
                this.discountAmount = discountAmount;
            }

            public double getExchangeRate() {
                return exchangeRate;
            }

            public void setExchangeRate(double exchangeRate) {
                this.exchangeRate = exchangeRate;
            }

            public String getMasterTranLogId() {
                return masterTranLogId;
            }

            public void setMasterTranLogId(String masterTranLogId) {
                this.masterTranLogId = masterTranLogId;
            }

            public String getMerchantTradeCode() {
                return merchantTradeCode;
            }

            public void setMerchantTradeCode(String merchantTradeCode) {
                this.merchantTradeCode = merchantTradeCode;
            }

            public String getOptName() {
                return optName;
            }

            public void setOptName(String optName) {
                this.optName = optName;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public String getPayTime() {
                return payTime;
            }

            public void setPayTime(String payTime) {
                this.payTime = payTime;
            }

            public int getRefundAmount() {
                return refundAmount;
            }

            public void setRefundAmount(int refundAmount) {
                this.refundAmount = refundAmount;
            }

            public int getSettlementAmount() {
                return settlementAmount;
            }

            public void setSettlementAmount(int settlementAmount) {
                this.settlementAmount = settlementAmount;
            }

            public String getSettlementCurrency() {
                return settlementCurrency;
            }

            public void setSettlementCurrency(String settlementCurrency) {
                this.settlementCurrency = settlementCurrency;
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public String getThirdExtId() {
                return thirdExtId;
            }

            public void setThirdExtId(String thirdExtId) {
                this.thirdExtId = thirdExtId;
            }

            public String getThirdExtName() {
                return thirdExtName;
            }

            public void setThirdExtName(String thirdExtName) {
                this.thirdExtName = thirdExtName;
            }

            public String getThirdTradeNo() {
                return thirdTradeNo;
            }

            public void setThirdTradeNo(String thirdTradeNo) {
                this.thirdTradeNo = thirdTradeNo;
            }

            public int getTipAmount() {
                return tipAmount;
            }

            public void setTipAmount(int tipAmount) {
                this.tipAmount = tipAmount;
            }

            public String getTranLogId() {
                return tranLogId;
            }

            public void setTranLogId(String tranLogId) {
                this.tranLogId = tranLogId;
            }

            public String getTranTime() {
                return tranTime;
            }

            public void setTranTime(String tranTime) {
                this.tranTime = tranTime;
            }

            public int getTransAmount() {
                return transAmount;
            }

            public void setTransAmount(int transAmount) {
                this.transAmount = transAmount;
            }

            public String getTransCurrency() {
                return transCurrency;
            }

            public void setTransCurrency(String transCurrency) {
                this.transCurrency = transCurrency;
            }

            public String getTransKind() {
                return transKind;
            }

            public void setTransKind(String transKind) {
                this.transKind = transKind;
            }

            public String getTransType() {
                return transType;
            }

            public void setTransType(String transType) {
                this.transType = transType;
            }

            public String getDiffCode() {
                return diffCode;
            }

            public void setDiffCode(String diffCode) {
                this.diffCode = diffCode;
            }
        }
    }
}
