package com.wizarpos.pay.recode.hisotory.activitylist.bean.http;

import com.wizarpos.pay.recode.domain.base.ResponseHttpBaseResult;

import java.util.List;

/**
 * activity交易记录列表
 */
public class ResponseTranRecoderListBean extends ResponseHttpBaseResult {


    /**
     * result : {"autoCount":true,"first":1,"hasNext":true,"hasPre":false,"nextPage":2,"orderBySetted":false,"pageNo":1,"pageSize":10,"prePage":1,"result":[{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290019","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290019","payTime":"2019-12-29 05:10:44","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181405182398","tipAmount":0,"tranLogId":"P100100020000067201912290019","tranTime":"2019-12-29 05:10:44","transAmount":-1,"transKind":"Refund","transType":"809"},{"cnyAmount":5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290018","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290018","payTime":"2019-12-29 04:49:10","refundAmount":1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181405182398","tipAmount":0,"tranLogId":"P100100020000067201912290018","tranTime":"2019-12-29 04:49:10","transAmount":1,"transKind":"Pay","transType":"813"},{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290017","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290017","payTime":"2019-12-29 04:48:30","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122822001409181404872920","tipAmount":0,"tranLogId":"P100100020000067201912290017","tranTime":"2019-12-29 04:48:30","transAmount":-1,"transKind":"Refund","transType":"809"},{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290016","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290016","payTime":"2019-12-29 04:47:02","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181404997777","tipAmount":0,"tranLogId":"P100100020000067201912290016","tranTime":"2019-12-29 04:47:02","transAmount":-1,"transKind":"Refund","transType":"809"},{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290015","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290015","payTime":"2019-12-29 04:46:15","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181405021203","tipAmount":0,"tranLogId":"P100100020000067201912290015","tranTime":"2019-12-29 04:46:15","transAmount":-1,"transKind":"Refund","transType":"809"},{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290014","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290014","payTime":"2019-12-29 04:45:53","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181405190406","tipAmount":0,"tranLogId":"P100100020000067201912290014","tranTime":"2019-12-29 04:45:53","transAmount":-1,"transKind":"Refund","transType":"809"},{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290013","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290013","payTime":"2019-12-29 04:45:31","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181405242418","tipAmount":0,"tranLogId":"P100100020000067201912290013","tranTime":"2019-12-29 04:45:31","transAmount":-1,"transKind":"Refund","transType":"809"},{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290012","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290012","payTime":"2019-12-29 04:43:53","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181404979826","tipAmount":0,"tranLogId":"P100100020000067201912290012","tranTime":"2019-12-29 04:43:53","transAmount":-1,"transKind":"Refund","transType":"809"},{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290011","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290011","payTime":"2019-12-29 04:42:30","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181405282823","tipAmount":0,"tranLogId":"P100100020000067201912290011","tranTime":"2019-12-29 04:42:30","transAmount":-1,"transKind":"Refund","transType":"809"},{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290010","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290010","payTime":"2019-12-29 04:42:03","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181405242419","tipAmount":0,"tranLogId":"P100100020000067201912290010","tranTime":"2019-12-29 04:42:03","transAmount":-1,"transKind":"Refund","transType":"809"}],"totalCount":60,"totalPages":6}
     */

    private ResultBeanX result;

    public ResultBeanX getResult() {
        return result;
    }

    public void setResult(ResultBeanX result) {
        this.result = result;
    }

    public static class ResultBeanX {
        /**
         * autoCount : true
         * first : 1
         * hasNext : true
         * hasPre : false
         * nextPage : 2
         * orderBySetted : false
         * pageNo : 1
         * pageSize : 10
         * prePage : 1
         * result : [{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290019","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290019","payTime":"2019-12-29 05:10:44","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181405182398","tipAmount":0,"tranLogId":"P100100020000067201912290019","tranTime":"2019-12-29 05:10:44","transAmount":-1,"transKind":"Refund","transType":"809"},{"cnyAmount":5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290018","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290018","payTime":"2019-12-29 04:49:10","refundAmount":1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181405182398","tipAmount":0,"tranLogId":"P100100020000067201912290018","tranTime":"2019-12-29 04:49:10","transAmount":1,"transKind":"Pay","transType":"813"},{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290017","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290017","payTime":"2019-12-29 04:48:30","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122822001409181404872920","tipAmount":0,"tranLogId":"P100100020000067201912290017","tranTime":"2019-12-29 04:48:30","transAmount":-1,"transKind":"Refund","transType":"809"},{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290016","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290016","payTime":"2019-12-29 04:47:02","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181404997777","tipAmount":0,"tranLogId":"P100100020000067201912290016","tranTime":"2019-12-29 04:47:02","transAmount":-1,"transKind":"Refund","transType":"809"},{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290015","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290015","payTime":"2019-12-29 04:46:15","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181405021203","tipAmount":0,"tranLogId":"P100100020000067201912290015","tranTime":"2019-12-29 04:46:15","transAmount":-1,"transKind":"Refund","transType":"809"},{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290014","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290014","payTime":"2019-12-29 04:45:53","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181405190406","tipAmount":0,"tranLogId":"P100100020000067201912290014","tranTime":"2019-12-29 04:45:53","transAmount":-1,"transKind":"Refund","transType":"809"},{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290013","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290013","payTime":"2019-12-29 04:45:31","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181405242418","tipAmount":0,"tranLogId":"P100100020000067201912290013","tranTime":"2019-12-29 04:45:31","transAmount":-1,"transKind":"Refund","transType":"809"},{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290012","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290012","payTime":"2019-12-29 04:43:53","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181404979826","tipAmount":0,"tranLogId":"P100100020000067201912290012","tranTime":"2019-12-29 04:43:53","transAmount":-1,"transKind":"Refund","transType":"809"},{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290011","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290011","payTime":"2019-12-29 04:42:30","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181405282823","tipAmount":0,"tranLogId":"P100100020000067201912290011","tranTime":"2019-12-29 04:42:30","transAmount":-1,"transKind":"Refund","transType":"809"},{"cnyAmount":-5,"discountAmount":0,"exchangeRate":5.3592,"masterTranLogId":"P100100020000067201912290010","optName":"Cashier0001(1001)","orderNo":"01100100020000067201912290010","payTime":"2019-12-29 04:42:03","refundAmount":-1,"thirdExtId":"2088212038909181","thirdExtName":"223***@qq.*","thirdTradeNo":"2019122922001409181405242419","tipAmount":0,"tranLogId":"P100100020000067201912290010","tranTime":"2019-12-29 04:42:03","transAmount":-1,"transKind":"Refund","transType":"809"}]
         * totalCount : 60
         * totalPages : 6
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
             * cnyAmount : -5
             * discountAmount : 0
             * exchangeRate : 5.3592
             * masterTranLogId : P100100020000067201912290019
             * optName : Cashier0001(1001)
             * orderNo : 01100100020000067201912290019
             * payTime : 2019-12-29 05:10:44
             * refundAmount : -1
             * thirdExtId : 2088212038909181
             * thirdExtName : 223***@qq.*
             * thirdTradeNo : 2019122922001409181405182398
             * tipAmount : 0
             * tranLogId : P100100020000067201912290019
             * tranTime : 2019-12-29 05:10:44
             * transAmount : -1
             * transKind : Refund
             * transType : 809
             */

            private int cnyAmount;
            private int discountAmount;
            private String exchangeRate;
            private String masterTranLogId;
            private String optName;
            private String orderNo;
            private String payTime;
            private int refundAmount;
            private String thirdExtId;
            private String thirdExtName;
            private String thirdTradeNo;
            private int tipAmount;
            private String tranLogId;
            private String tranTime;
            private int transAmount;
            private String transKind;
            private String transType;
            private String transCurrency;//结算类型

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

            public String getExchangeRate() {
                return exchangeRate;
            }

            public void setExchangeRate(String exchangeRate) {
                this.exchangeRate = exchangeRate;
            }

            public String getMasterTranLogId() {
                return masterTranLogId;
            }

            public void setMasterTranLogId(String masterTranLogId) {
                this.masterTranLogId = masterTranLogId;
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

            public String getTransCurrency() {
                return transCurrency;
            }

            public void setTransCurrency(String transCurrency) {
                this.transCurrency = transCurrency;
            }
        }
    }
}
