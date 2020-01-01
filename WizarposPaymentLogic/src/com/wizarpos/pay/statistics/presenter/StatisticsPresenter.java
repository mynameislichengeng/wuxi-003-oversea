package com.wizarpos.pay.statistics.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.DbException;
import com.wizarpos.atool.log.Logger;
import com.wizarpos.atool.tool.DateUtil;
import com.wizarpos.atool.tool.Tools;
import com.wizarpos.base.net.NetRequest;
import com.wizarpos.base.net.Response;
import com.wizarpos.base.net.ResponseListener;
import com.wizarpos.device.printer.html.WebPrintActivity;
import com.wizarpos.device.printer.html.WebPrintHelper;
import com.wizarpos.device.printer.html.model.HTMLPrintModel;
import com.wizarpos.device.printer.html.model.HtmlLine;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.app.PaymentApplication;
import com.wizarpos.pay.cashier.model.TicketTotalRespBean;
import com.wizarpos.pay.common.Constants;
import com.wizarpos.pay.common.base.BasePresenter;
import com.wizarpos.pay.common.device.printer.Q1PrintBuilder;
import com.wizarpos.pay.common.print.PrintServiceControllerProxy;
import com.wizarpos.pay.common.utils.Calculater;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;
import com.wizarpos.pay.model.DailyDescript;
import com.wizarpos.pay.model.DailyDetailResp;
import com.wizarpos.pay.model.DailyRes;
import com.wizarpos.pay.model.DailyResPlus;
import com.wizarpos.pay.model.RefundDetailResp;
import com.wizarpos.pay.model.SerialNumInfo;
import com.wizarpos.pay.model.TodayDetailBean;
import com.wizarpos.pay.model.TranLogVo;
import com.wizarpos.pay.model.TransDetailResp;
import com.wizarpos.pay.statistics.model.GroupQueryResp;
import com.wizarpos.pay.statistics.model.MixTranLogBean;
import com.wizarpos.pay.statistics.model.TicketTranLogBean;
import com.wizarpos.pay.statistics.model.TicketTranLogResp;
import com.wizarpos.pay.statistics.model.TranLogBean;
import com.wizarpos.wizarpospaymentlogic.R;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wizarpos.pay.db.AppConfigHelper.getConfig;

/**
 * 查询
 *
 * @author wu
 */
public class StatisticsPresenter extends BasePresenter {

    private final int PAGE_SIZE = 10;
    private GroupQueryResp handler;
    private long count;
    private long totalPage = 0;// 总页数

    private List<String[]> detialRecords_private = new ArrayList<String[]>();// 交易明细,打印使用
    private List<String[]> detialRecords = new ArrayList<String[]>();// 交易明细


    private List<TranLogBean> logs = new ArrayList<TranLogBean>();
    private DailyRes dailyRes;//今日汇总(日结单)


    public StatisticsPresenter(Context context) {
        super(context);
    }

    public static String convertTimeRange2String(int timeRagnge) {
//		< "0", "今天">; < "1", "昨天">; < "2", "本周">; < "3", "上周">; < "4", "本月">; < "5", "上月">; < "6", "指定范围">
        if (timeRagnge == 0) {
            return "今天";
        } else if (timeRagnge == 1) {
            return "昨天";
        } else if (timeRagnge == 2) {
            return "本周";
        } else if (timeRagnge == 3) {
            return "上周";
        } else if (timeRagnge == 4) {
            return "本月";
        } else if (timeRagnge == 5) {
            return "上月";
        } else {
            return "指定范围";
        }
    }

    /**
     * 根据主流水号查询相关的所有订单
     *
     * @param transType  交易类型，可传空(//1 充值  2撤销 3消费)
     * @param startTime  起始时间
     * @param pageNumber 当前页数
     * @param endTime    截至时间
     * @param rechargeOn //0不含充值 1含充值 不传返回全部
     * @param timeRange  //0 今天 1 昨天 2本周 3上周 4本月 5上月 6时间段
     * @param listent
     */
    public void getDetailQuery(String rechargeOn,int pageSize,int pageNumber,String timeRange , String transType, String startTime , String endTime, String masterTranLogId, String tag, final ResponseListener listent) {//极简版收款根据时间范围查询交易@hong[20160325]
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("transType", transType);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
//        params.put("pageNo", pageNumber);
        params.put("rechargeOn", rechargeOn);
        params.put("timeRange", timeRange);
        params.put("pageNo", pageNumber);
        params.put("pageSize", pageSize);
        if (!TextUtils.isEmpty(masterTranLogId) && (masterTranLogId.startsWith("P") || masterTranLogId.startsWith("p"))) {
            params.put("masterTranLogId", "P" + AppConfigHelper.getConfig(AppConfigDef.mid) + masterTranLogId.substring(1));
//            params.put("tranLogId", "P" + AppConfigHelper.getConfig(AppConfigDef.mid) + masterTranLogId.substring(1));
            params.remove("transType");
            params.remove("timeRange");
        }
        NetRequest.getInstance().addRequest(Constants.SC_924_TRAN_DETAIL, params, tag, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                JSONObject detailResult = (JSONObject) response.getResult();
//                JSONObject detailLog = (JSONObject) detailResult.get("logs");
                List<TransDetailResp> list = JSONArray.parseArray(detailResult.get("logs").toString(), TransDetailResp.class);
                listent.onSuccess(new Response(0, "交易成功", list));
            }

            @Override
            public void onFaild(Response response) {
                listent.onFaild(response);
            }
        });
    }



    /**
     * @param rechargeOn  //0不含充值 1含充值 不传返回全部
     * @param pageSize  //每页个数
     * @param pageNumber 当前页数
     * @param timeRange  //0 今天 1 昨天 2本周 3上周 4本月 5上月 6时间段
     * @param transType  交易类型，可传空(//1 充值  2撤销 3消费)
     * @param startTime  起始时间
     * @param endTime    截至时间
     * @param listent
     */
    public void getQueryDetailNew(String rechargeOn,int pageSize, String pageNumber,String timeRange, String transType, String startTime, String endTime, String tranLogId, String tag, final ResponseListener listent) {//极简版收款根据时间范围查询交易@hong[20160325]
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("transType", transType);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("rechargeOn", rechargeOn);//暂时没有意义
        params.put("timeRange", timeRange);
        params.put("pageNo", Integer.valueOf(pageNumber));
        params.put("pageSize", pageSize);

        if (!TextUtils.isEmpty(tranLogId) && (tranLogId.startsWith("P") || tranLogId.startsWith("p"))) {
            params.put("tranLogId", "P" + AppConfigHelper.getConfig(AppConfigDef.mid) + tranLogId.substring(1));
            params.remove("transType");
            params.remove("timeRange");
        }
        NetRequest.getInstance().addRequest(Constants.SC_964_TRAN_DETAIL_PAGE, params, tag, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                String jsonStr = JSON.toJSONString(response);
                listent.onSuccess(new Response(0, "交易成功", jsonStr));
                //之前的
//                JSONObject detailResult = (JSONObject) response.getResult();
//                JSONObject detailLog = (JSONObject) detailResult.get("logs");
//                List<TransDetailResp> list = JSONArray.parseArray(detailResult.get("logs").toString(), TransDetailResp.class);
//                listent.onSuccess(new Response(0, "交易成功", list));
            }

            @Override
            public void onFaild(Response response) {
                listent.onFaild(response);
            }
        });
    }

    /**
     * 获取券的信息
     *
     * @param tranLogId 流水号
     */
    public void getTransactionTicketInfo(String tranLogId, final ResponseListener responseListener) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tranLogId", tranLogId);
        NetRequest.getInstance().addRequest(Constants.SC_926_DAILY_SUMMARY, params, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                responseListener.onSuccess(response);
            }

            @Override
            public void onFaild(Response response) {
                responseListener.onFaild(response);
            }
        });
    }

    /**
     * 日结单
     *
     * @param date 日期
     */
    public void getDailySummary(String date, String tag, final ResponseListener responseListener) {
        Map<String, Object> params = new HashMap<String, Object>();
        dailyRes = new DailyRes();
        final List<TodayDetailBean> todayDetail = new ArrayList<>();
        params.put("transTime", date);
        params.put("rechargeOn", "0");  //极简收款只能传 0 ，不然会有其他支付方式返回
        NetRequest.getInstance().addRequest(Constants.SC_926_DAILY_SUMMARY, params, tag, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                JSONObject dailyRecord = (JSONObject) response.getResult();
                long currentDay = dailyRecord.getLong("currentDay");
                JSONObject transInfo = (JSONObject) dailyRecord.get("transInfo");
                JSONObject refund = (JSONObject) dailyRecord.get("refund");
                JSONObject transSum = (JSONObject) dailyRecord.get("tranSum");
                Logger.error("毫秒值 = " + currentDay);
                if (currentDay != 0) {
                    Logger.error("毫秒值 1 ");
                    dailyRes.setCurrentDay(currentDay);
                }
                if (transSum.containsKey("tranAmount")) {
                    dailyRes.setTransTotalAmount(transSum.getString("tranAmount"));
                } else {
                    dailyRes.setTransTotalAmount("0");
                }
                if (transSum.containsKey("tranCount")) {
                    dailyRes.setTransTotalAcount(transSum.getString("tranCount"));
                } else {
                    dailyRes.setTransTotalAcount("0");
                }
                if (refund.containsKey("refundAmount") && refund.containsKey("refundCount")) {
                    if (!TextUtils.isEmpty(refund.getString("refundAmount"))) {
                        dailyRes.setRefundAmount(refund.getString("refundAmount"));
                    }
                    if (!TextUtils.isEmpty(refund.getString("refundCount"))) {
                        dailyRes.setRefundCount(refund.getString("refundCount"));
                    }
                }
                if (transInfo.containsKey("totalAmount")) {
                    if (!TextUtils.isEmpty(transInfo.getString("totalAmount"))) {
                        dailyRes.setTotalAmount(transInfo.getString("totalAmount"));
                    }
                }
                if (transInfo.containsKey("transAmount")) {
                    if (!TextUtils.isEmpty(transInfo.getString("transAmount"))) {
                        dailyRes.setConsumptionAmount(transInfo.getString("transAmount"));
                    }
                } else {
                    dailyRes.setConsumptionAmount("0");
                }
                if (transInfo.containsKey("voidAmount")) {
                    if (!TextUtils.isEmpty(transInfo.getString("voidAmount"))) {
                        dailyRes.setRevokeAmount(transInfo.getString("voidAmount"));
                    }
                } else {
                    dailyRes.setRevokeAmount("0");
                }
                List<DailyDescript> dailyDescripts = JSONArray.parseArray(dailyRecord.get("pay").toString(), DailyDescript.class);
                TodayDetailBean todayDetailBean = null;
                for (int i = 0; i < dailyDescripts.size(); i++) {
                    todayDetailBean = new TodayDetailBean();
                    todayDetailBean.setAmount(String.valueOf(dailyDescripts.get(i).getTranAmountSum()));
                    todayDetailBean.setCount(dailyDescripts.get(i).getTranAmountCount());
                    todayDetailBean.setDetailName(dailyDescripts.get(i).getTranCodeDesc());
                    todayDetail.add(todayDetailBean);
                }
                if (todayDetail.size() != 0) {
                    dailyRes.setList(todayDetail);
                }
                responseListener.onSuccess(new Response(0, "", dailyRes));
            }

            @Override
            public void onFaild(Response response) {
                responseListener.onFaild(response);
            }
        });
    }


    /**
     * 日结单
     *
     * @param date 日期
     */
    public void getDailySummaryPlus(String date, final String tag, final ResponseListener responseListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("timeRange", "0"); // 暂时只支持当天
        NetRequest.getInstance().addRequest(Constants.SC_961_DAILY_SUMMARY_PLUS, params, tag, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                try {
                    JSONObject result = JSON.parseObject(String.valueOf(response.getResult()));
                    String beginTime = result.getString("beginTime");
                    String endTime = result.getString("endTime");

                    if (result.containsKey("datas")) {
                        JSONArray datas = result.getJSONArray("datas");
                        if (datas.size() > 0) {
                            JSONObject jsonObject = datas.getJSONObject(0);
                            TranLogVo tranLogVo = JSONObject.parseObject(String.valueOf(jsonObject), TranLogVo.class);
                            tranLogVo.setBeginTime(beginTime);
                            tranLogVo.setEndTime(endTime);
                            responseListener.onSuccess(new Response(0, "", tranLogVo));
                        } else {
                            responseListener.onFaild(response);
                        }
                    } else {
                        TranLogVo tranLogVo = new TranLogVo();
                        tranLogVo.setBeginTime(beginTime);
                        tranLogVo.setEndTime(endTime);
                        responseListener.onSuccess(new Response(0, "", tranLogVo));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    responseListener.onFaild(response);
                    ;
                }
            }

            @Override
            public void onFaild(Response response) {
                responseListener.onFaild(response);
            }
        });
    }

    /**
     * 交易汇总查询 (当天,全部交易类型)
     *
     * @param listener
     */
    public void transactionGroupQuery(final ResultListener listener) {
        transactionGroupQuery(-1, 0, "", "", listener);
    }

    public void transactionGroupQuery(int tranCode, String startTime, String endTime, final ResultListener listener) {
        int TIME_RANGE_LAWSON = 7;//HWC 服务端根据时间戳判断
        final int timeRange = TIME_RANGE_LAWSON;
        transactionGroupQuery(tranCode, timeRange, startTime, endTime, listener);
    }

    /**
     * 交易汇总查询
     *
     * @param tranCode  <"-1", "全部">; <"700", "银行卡消费">; <"304", "会员卡消费">; <"302", "会员卡充值">; <"310", "会员卡交易撤销">; <"307", "会员卡激活">; <"400", "现金消费">; <"830", "会员卡换卡">;
     *                  <"813", "支付宝消费">; <"814", "微信消费">; <"820", "微信/支付宝消费撤销">,<"841","手Q支付">,<"842","手Q支付撤销">,<"850","百度支付" >,<"851","百度支付撤销">
     * @param timeRange < "0", "今天">; < "1", "昨天">; < "2", "本周">; < "3", "上周">; < "4", "本月">; < "5", "上月">; < "6", "指定范围">
     * @param startTime yyyy-MM-dd 可为空 (DateUtil.format(time, "yyyy-MM-dd"))
     * @param endTime   yyyy-MM-dd 可为空 (DateUtil.format(time, "yyyy-MM-dd"))
     * @param listener
     */
    public void transactionGroupQuery(int tranCode, final int timeRange, String startTime, String endTime, final ResultListener listener) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("timeRange", timeRange);
        params.put("tranCode", tranCode);

        String tranLogCode;
        if (Constants.APP_VERSION_NAME == Constants.APP_VERSION_LAWSON)//罗森版本
        {
            tranLogCode = Constants.SC_616_TRAN_LOG_SUM;
        } else {
            tranLogCode = Constants.SC_609_TRAN_LOG_SUM;
        }

        NetRequest.getInstance().addRequest(tranLogCode, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                if (timeRange == 7) {
                    updateDayTime();
                }
                handleGroupQueryResp(response.getResult().toString(), listener); // 耗时操作,放置到其他线程处理
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);
            }
        });
    }

    private void updateDayTime() {
        NetRequest.getInstance().addRequest(Constants.SC_618_UPDATE_DAYTIME, new HashMap<String, Object>(), new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
            }

            @Override
            public void onFaild(Response response) {
            }
        });
    }

    /**
     * 交易汇总查询(同步)
     *
     * @param tranCode  <"-1", "全部">; <"700", "银行卡消费">; <"304", "会员卡消费">; <"302", "会员卡充值">; <"310", "会员卡交易撤销">; <"307", "会员卡激活">; <"400", "现金消费">; <"830", "会员卡换卡">;
     *                  <"813", "支付宝消费">; <"814", "微信消费">; <"820", "微信/支付宝消费撤销">,<"841","手Q支付">,<"842","手Q支付撤销">,<"850","百度支付" >,<"851","百度支付撤销">
     * @param timeRange < "0", "今天">; < "1", "昨天">; < "2", "本周">; < "3", "上周">; < "4", "本月">; < "5", "上月">; < "6", "指定范围">
     * @param startTime yyyy-MM-dd 可为空 (DateUtil.format(time, "yyyy-MM-dd"))
     * @param endTime   yyyy-MM-dd 可为空 (DateUtil.format(time, "yyyy-MM-dd"))
     * @param listener
     */
    public void transactionGroupQuerySync(int tranCode, int timeRange, String startTime, String endTime, final ResultListener listener) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("timeRange", timeRange);
        params.put("tranCode", tranCode);

        String tranLogCode;
        if (Constants.APP_VERSION_NAME == Constants.APP_VERSION_LAWSON)//罗森版本
        {
            tranLogCode = Constants.SC_616_TRAN_LOG_SUM;
        } else {
            tranLogCode = Constants.SC_609_TRAN_LOG_SUM;
        }

        NetRequest.getInstance().addRequest(tranLogCode, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                if (response.getResult() == null || TextUtils.isEmpty(response.getResult().toString())) {
                    listener.onFaild(new Response(1, "数据解析异常"));
                    return;
                }
                JSONObject tranLogSum = JSONObject.parseObject(response.getResult().toString());
                JSONArray jarr = tranLogSum.getJSONArray("pay");
                if (jarr.size() < 1) {
                    listener.onFaild(new Response(1, "数据解析异常"));
                    return;
                }
                // String _payJson = tranLogSum.getString("pay");// 交易记录json
                // String _ticketJson = tranLogSum.getString("ticket");//
                // 券记录json
                // if (TextUtils.isEmpty(_payJson)) { return new Response(1,
                // "没有找到相关数据"); }
                // List<TransRecord> transRecords =
                // JSONArray.parseArray(_payJson, TransRecord.class);// 交易记录列表
                // List<TransRecord> ticketRecords = null;// 券记录列表
                // if (TextUtils.isEmpty(_ticketJson) == false) {
                // ticketRecords = JSONArray.parseArray(_payJson,
                // TransRecord.class);
                // }
                handler = new GroupQueryResp();
                // 遍历处理相关数据
                handler.handleResult(tranLogSum);
                listener.onSuccess(new Response(0, "success", handler));
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);
            }
        });
    }


    /**
     * 处理交易汇总查询的数据
     */
    private void handleGroupQueryResp(String respJson, final ResultListener listener) {
        new AsyncTask<String, String, Response>() {

            @Override
            protected Response doInBackground(String... params) {
                if (TextUtils.isEmpty(params[0])) {
                    return new Response(1, "数据解析异常");
                }
                JSONObject tranLogSum = JSONObject.parseObject(params[0]);
                JSONArray jarr = tranLogSum.getJSONArray("pay");
                JSONArray ticketJsonArray = tranLogSum.getJSONArray("ticket");
                if (jarr.size() < 1 && ticketJsonArray.size() < 1) {
                    return new Response(1, "没有找到相关数据");
                }
                // String _payJson = tranLogSum.getString("pay");// 交易记录json
                // String _ticketJson = tranLogSum.getString("ticket");//
                // 券记录json
                // if (TextUtils.isEmpty(_payJson)) { return new Response(1,
                // "没有找到相关数据"); }
                // List<TransRecord> transRecords =
                // JSONArray.parseArray(_payJson, TransRecord.class);// 交易记录列表
                // List<TransRecord> ticketRecords = null;// 券记录列表
                // if (TextUtils.isEmpty(_ticketJson) == false) {
                // ticketRecords = JSONArray.parseArray(_payJson,
                // TransRecord.class);
                // }
                handler = new GroupQueryResp();
                // 遍历处理相关数据
                handler.handleResult(tranLogSum);

                return new Response(0, "success", handler);
            }

            protected void onPostExecute(Response result) {
                if (result.code == 0) {
                    listener.onSuccess(result);
                } else {
                    listener.onFaild(result);
                }
            }

            ;
        }.execute(respJson);
    }

    /**
     * 打印交易汇总查询
     */
    public void printGroupQuery() {
        // GroupQueryResp handler = new GroupQueryResp();
        List<String[]> recordListShow = handler.getRecordListShow();
        List<String[]> recordTicketListSum = handler.getRecordTicketListSum();

        if (recordListShow.size() < 1) {
            return;
        }
        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
        Q1PrintBuilder builder = new Q1PrintBuilder();
        String printString = "";

        printString += builder.center(builder.bold("交易汇总"));
        printString += builder.branch();
        printString += "SN号：" + AppConfigHelper.getConfig(AppConfigDef.sn, "") + builder.br();
        printString += "慧商户号：" + AppConfigHelper.getConfig(AppConfigDef.mid, "") + builder.br();
        printString += "商户名称：" + AppConfigHelper.getConfig(AppConfigDef.merchantName, "") + builder.br();
        printString += builder.branch();
        // -----------------------------------------------------------------
        List<String[]> recordListTickets = new ArrayList<String[]>();
        List<String[]> recordListTickets2 = new ArrayList<String[]>();
        List<String[]> recordListNoneTickets = new ArrayList<String[]>();
        for (int i = 0; i < recordListShow.size(); i++) {
            if ((recordListShow.get(i)[0]).contains("(券)")) {
                String[] tickets = new String[]{recordListShow.get(i)[0], recordListShow.get(i)[1], recordListShow.get(i)[2],};
                if (Tools.toIntMoney(recordListShow.get(i)[2]) > 0) {
                    recordListTickets.add(tickets);
                } else {
                    recordListTickets2.add(tickets);
                }
            } else {
                String[] nonetickets = new String[]{recordListShow.get(i)[0], recordListShow.get(i)[1], recordListShow.get(i)[2],};
                recordListNoneTickets.add(nonetickets);
            }
        }
        // -----------------------------------------------------------------
        // 不带券
        for (int i = 0; i < recordListNoneTickets.size(); i++) {
            String[] recordPrinter = recordListNoneTickets.get(i);
            printString += "交易类型：" + recordPrinter[0] + builder.br();
            printString += "笔    数：" + recordPrinter[1] + builder.br();
            printString += "交易金额：" + recordPrinter[2] + "元" + builder.br();
            printString += builder.branch();
        }
//        printString += builder.br();
        // 带券
        for (int i = 0; i < recordListTickets.size(); i++) {
            String[] recordPrinter = recordListTickets.get(i);
            printString += "交易类型：" + recordPrinter[0] + builder.br();
            printString += "笔    数：" + recordPrinter[1] + builder.br();
            printString += "交易金额：" + recordPrinter[2] + "元" + builder.br();
            printString += builder.branch();
        }
//        printString += builder.br();
        // for(int i = 0; i < recordTicketUsed.size(); i++){
        // String[] recordPrinter = recordTicketUsed.get(i);
        // printString += "交易类型：" + recordPrinter[0] + pb.br();
        // printString += "笔    数：" + recordPrinter[1] + pb.br();
        // printString += "交易金额：" + recordPrinter[2] + pb.br();
        // printString += pb.normal("--------------------------------") +
        // pb.br() ;
        // }
        // printString += pb.br();
        for (int i = 0; i < recordListTickets2.size(); i++) {
            String[] recordPrinter = recordListTickets2.get(i);
            printString += "交易类型：" + recordPrinter[0] + builder.br();
            printString += "笔    数：" + recordPrinter[1] + builder.br();
            printString += "交易金额：" + recordPrinter[2] + "元" + builder.br();
            printString += builder.branch();
        }

        // for(int i = 0; i < recordTicketCancel.size(); i++){
        // String[] recordPrinter = recordTicketCancel.get(i);
        // printString += "交易类型：" + recordPrinter[0] + pb.br();
        // printString += "笔    数：" + recordPrinter[1] + pb.br();
        // printString += "交易金额：" + recordPrinter[2] + pb.br();
        // printString += pb.normal("--------------------------------");
        // }
        // printString += pb.br() ;
        for (int i = 0; i < recordTicketListSum.size(); i++) {
            String[] recordPrinter = recordTicketListSum.get(i);
            printString += "券 类 型：" + recordPrinter[0] + builder.br();
            printString += "张    数：" + recordPrinter[1] + builder.br();
            printString += "交易金额：" + recordPrinter[2] + "元" + builder.br();
            printString += builder.branch();
        }
        if (Constants.APP_VERSION_NAME == Constants.APP_VERSION_LAWSON) {
            printString += "开始时间:" + handler.getLastPrintTime() + builder.br();
            printString += "结束时间:" + handler.getCurrentPrintTime() + builder.br();
            printString += "操作员号:" + AppConfigHelper.getConfig(AppConfigDef.operatorTrueName) + builder.br();
            printString += builder.branch();
        }
        printString += builder.endPrint();
        controller.print(printString);
        controller.cutPaper();
    }

    /**
     * 获取三组交易汇总数据(今天、本周、本月)
     */
    public void getTransactionTotalData(final ResultListener listener) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tranCode", "-1");
        String tranLogCode;
        if (Constants.APP_VERSION_NAME == Constants.APP_VERSION_LAWSON)//罗森版本
        {
            tranLogCode = Constants.SC_617_HANDER_STATIC_TOTAL;
        } else {
            tranLogCode = Constants.SC_614_HANDER_STATIC_TOTAL;
        }
        NetRequest.getInstance().addRequest(tranLogCode, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                JSONObject object = (JSONObject) response.result;
                JSONArray todayArray = (JSONArray) object.get("currPay");
                JSONArray weekArray = (JSONArray) object.get("weekPay");
                JSONArray monthArray = (JSONArray) object.get("monthPay");
                long todayAmount = parseTransactionTotalDataJson(todayArray);
                long weekAmount = parseTransactionTotalDataJson(weekArray);
                long monthAmount = parseTransactionTotalDataJson(monthArray);
                List<Long> list = new ArrayList<Long>(3);
                list.add(todayAmount);
                list.add(weekAmount);
                list.add(monthAmount);
                listener.onSuccess(new Response(0, "success", list));
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);
            }
        });

    }

    private long parseTransactionTotalDataJson(JSONArray jsonArray) {
        try {
            JSONObject amountJsonObject = jsonArray.getJSONObject(0);
            long todayAmount = 0;
            if (amountJsonObject != null) {
                todayAmount = amountJsonObject.getLong("total");
            }
            return todayAmount;
        } catch (Exception e) {
        }
        return 0;
    }

    public void getTicketTotalData(final ResultListener listener) {
        Map<String, Object> params = new HashMap<String, Object>();
        NetRequest.getInstance().addRequest(Constants.SC_880_HANDER_CHANEL_TOTAL, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                try {
                    TicketTotalRespBean ticketTotalRespBean = JSONObject.parseObject(response.result.toString(), TicketTotalRespBean.class);
                    if (ticketTotalRespBean == null) {
                        throw new Exception("解析出结果为空");
                    }
                    listener.onSuccess(new Response(0, "success", ticketTotalRespBean));
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFaild(new Response(1, "数据解析异常"));
                }
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);
            }
        });

    }

    /**
     * 获取今日的券
     *
     * @param listener
     */
    public void getTodayTicketDetial(ResultListener listener) {
        getTticketDetail("-1", "", "", listener);
    }


    /**
     * 券交易金额明细
     *
     * @param timeRange < "0", "今天">; < "1", "昨天">; < "2", "本周">; < "3", "上周">; < "4", "本月">; < "5", "上月">; < "6", "指定范围">
     * @param startTime yyyy-MM-dd 可为空 (DateUtil.format(time, "yyyy-MM-dd"))
     * @param endTime   yyyy-MM-dd 可为空 (DateUtil.format(time, "yyyy-MM-dd"))
     * @param listener  onSuccess方法返回 List<\String[]> --->{"交易流水号","受理类型","交易日期","交易金额"}
     */
    public void getTticketDetail(String timeRange, String startTime, String endTime, final ResultListener listener) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", TextUtils.isEmpty(startTime) ? "" : startTime);
        params.put("endTime", TextUtils.isEmpty(endTime) ? "" : endTime);
        params.put("timeRange", timeRange);
        NetRequest.getInstance().addRequest(Constants.SC_881_TICKET_DETIAL, params, new ResponseListener() {

            @Override
            public void onSuccess(Response arg0) {
                if (arg0.getResult() == null) {
                    listener.onFaild(new Response(1, "数据解析异常"));
                    return;
                }
                JSONObject jsonObject = JSONObject.parseObject(arg0.getResult().toString());
                if (jsonObject.containsKey("ticketTranLogLists")) {
                    List<TicketTranLogResp> ticketTranLogResps = JSONArray.parseArray(jsonObject.getString("ticketTranLogLists"), TicketTranLogResp.class);
                    listener.onSuccess(new Response(0, "success", ticketTranLogResps));
                } else {
                    listener.onFaild(new Response(1, "数据解析异常"));
                }
            }

            @Override
            public void onFaild(Response arg0) {
                listener.onFaild(arg0);
            }

        });
    }

    /**
     * 查询交易明细 (今天 全部 所有记录)
     *
     * @param listener
     */
    public void transactionDetialQuery(final ResultListener listener) {
        transactionDetialQuery(-1, 0, "", "", "", "1", "10", listener);
    }


    /**
     * 交易明细查询
     *
     * @param tranCode  <"-1", "全部">; <"700", "银行卡消费">; <"304", "会员卡消费">; <"302", "会员卡充值">; <"310", "会员卡交易撤销">; <"307", "会员卡激活">; <"400", "现金消费">; <"830", "会员卡换卡">;
     *                  <"813", "支付宝消费">; <"814", "微信消费">; <"820", "微信/支付宝消费撤销">
     * @param timeRange < "0", "今天">; < "1", "昨天">; < "2", "本周">; < "3", "上周">; < "4", "本月">; < "5", "上月">; < "6", "指定范围">
     * @param startTime yyyy-MM-dd 可为空 (DateUtil.format(time, "yyyy-MM-dd"))
     * @param endTime   yyyy-MM-dd 可为空 (DateUtil.format(time, "yyyy-MM-dd"))
     * @param orderNo   订单号 可为空
     * @param pageNo    第几页 从1开始
     * @param perNo     每页有几条记录
     * @param listener  onSuccess方法返回 List<\String[]> --->{"交易流水号","受理类型","交易日期","交易金额"}
     */
    public void transactionDetialQuery(int tranCode, int timeRange, String startTime, String endTime, String orderNo, final String pageNo, String perNo,
                                       final ResultListener listener) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tranCode", tranCode);
        params.put("timeRange", timeRange);
        params.put("startTime", TextUtils.isEmpty(startTime) ? "" : startTime);
        params.put("endTime", TextUtils.isEmpty(endTime) ? "" : endTime);
        params.put("timeRange", timeRange);
        if (!TextUtils.isEmpty(orderNo) && (orderNo.startsWith("P") || orderNo.startsWith("p"))) {
            orderNo = "P" + AppConfigHelper.getConfig(AppConfigDef.mid) + orderNo.substring(1);
        }
        params.put("tranLogId", orderNo);
        params.put("pageNo", pageNo);
        params.put("perNo", perNo);
        String tranLogCode;
        if (Constants.APP_VERSION_NAME == Constants.APP_VERSION_LAWSON)//罗森版本
        {
            tranLogCode = Constants.SC_615_TRAN_LOG_QUERY;
        } else {
            tranLogCode = Constants.SC_608_TRAN_LOG_QUERY;
        }
        NetRequest.getInstance().addRequest(tranLogCode, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
//				clearData();
                totalPage = 0;
                detialRecords = new ArrayList<String[]>();
                detialRecords_private.clear();
                JSONObject o = JSONObject.parseObject(response.getResult().toString());

                final String mid = AppConfigHelper.getConfig(AppConfigDef.mid);

                // tickets
                JSONArray jarr = o.getJSONArray("tickets");
                List<TicketTranLogBean> tickets = new ArrayList<TicketTranLogBean>();
                if (jarr.size() > 0) {
                    for (int i = 0; i < jarr.size(); i++) {
                        JSONArray jarrArry = jarr.getJSONArray(i);
                        if (jarrArry == null) {
                            return;
                        }
                        TicketTranLogBean bean = new TicketTranLogBean();
                        bean.setTranLogId(Tools.deleteMidTranLog(jarrArry.get(0).toString(), mid));
                        bean.setTranType(jarrArry.getInteger(1));
                        bean.setTicketId(jarrArry.getString(2));
                        bean.setTicketDes(jarrArry.getString(3));
                        bean.setAmount(jarrArry.getLongValue(4));
                        bean.setTranTime(jarrArry.getLongValue(5));
                        tickets.add(bean);
                    }
                    // saveTickets(tickets);
                }

                // mixTrans
                List<MixTranLogBean> mixTrans = new ArrayList<MixTranLogBean>();
                jarr = o.getJSONArray("mixTrans");
                if (jarr.size() > 0) {
                    for (int i = 0; i < jarr.size(); i++) {
                        JSONArray jarrArry = jarr.getJSONArray(i);
                        if (jarrArry == null) {
                            return;
                        }
                        MixTranLogBean bean = new MixTranLogBean();
                        bean.setTranLogId(Tools.deleteMidTranLog(jarrArry.get(0).toString(), mid));
                        bean.setTranType(jarrArry.getInteger(1));
                        bean.setTranDes(jarrArry.getString(2));
                        bean.setTranAmount(jarrArry.getLongValue(5));
                        bean.setTranTime(jarrArry.getLongValue(6));
                        bean.setTranState(jarrArry.getInteger(7));
                        mixTrans.add(bean);
                    }
                    // saveMixTrans(mixTrans);
                }

                // logs
                jarr = o.getJSONArray("logs");
                if (jarr == null || jarr.isEmpty()) {
                    listener.onFaild(new Response(1, "没有找到相关信息"));
                    return;
                }
                ArrayList<TranLogBean> logs = new ArrayList<TranLogBean>();
                for (int i = 0; i < jarr.size(); i++) {
                    JSONArray jarrArry = jarr.getJSONArray(i);
                    if (jarrArry == null) {
                        listener.onFaild(new Response(1, "没有找到相关信息"));
                        return;
                    }
                    String transType = jarrArry.get(1).toString();
                    String transTypeDes = Constants.TRAN_TYPE.get(transType);
                    if (transType.equals("815")) {//815 威富通微信支付 @yaosongqwe[20151109]
                        transTypeDes = "威富通微信支付";
                    } else if (transType.equals("817")) {//817  威富通支付宝支付
                        transTypeDes = "威富通支付宝支付 ";
                    } else if (transType.equals("821")) {//821  威富通QQ钱包支付
                        transTypeDes = "威富通QQ钱包支付";
                    } else if (transType.equals("816")) {//816  威富通微信支付撤销
                        transTypeDes = "威富通微信支付撤销";
                    } else if (transType.equals("818")) {//818  威富通支付宝支付撤销
                        transTypeDes = "威富通支付宝支付撤销";
                    } else if (transType.equals("822")) {//822  威富通QQ钱包支付撤销
                        transTypeDes = "威富通QQ钱包支付撤销";
                    }
                    if (TextUtils.isEmpty(transTypeDes) || transTypeDes.contains("会员卡")) {// 所有会员卡消费的信息全部踢除
                        continue;
                    }
                    try {
                        TranLogBean log = new TranLogBean();
                        log.setTranLogId(Tools.deleteMidTranLog(jarrArry.get(0).toString(), mid));
                        log.setTranType(transTypeDes);
                        log.setTranTime(DateUtil.format(Long.valueOf(jarrArry.get(3).toString()), "yyyy-MM-dd HH:mm:ss"));
                        log.setAmount(Long.valueOf(jarrArry.get(2).toString()));
                        log.setShowAmount(log.getAmount());// 显示金额
                        List<TicketTranLogBean> tickesTemp = new ArrayList<TicketTranLogBean>();
                        // 筛选出这笔交易包含的券信息
                        long ticketAmount = 0; // 券消费金额
                        for (TicketTranLogBean bean : tickets) { // 遍历券列表,获取这笔交易中所用到的券
                            if (bean.getTranLogId().equals(log.getTranLogId())) {
                                tickesTemp.add(bean);
                                ticketAmount += bean.getAmount();
                            }
                        }
                        if (tickesTemp.isEmpty() == false) {
                            log.setTranType(log.getTranType() + "-券");
                        }
//                      log.setShowAmount(log.getShowAmount() + ticketAmount);// 消费总金额
                        log.setShowAmount(log.getShowAmount());// 券消费金额不计入  消费总金额  @yaosong[20151122]
                        log.setTicketTranLogBeans(tickesTemp);// 附带用券列表

                        // 如果这笔支付是组合支付,筛选出这笔交易包含的组合支付信息
                        if (Constants.SC_800_MIXPAY.equals(transType) || Constants.SC_801_MIXPAY_CANCEL.equals(transType)) {
                            List<MixTranLogBean> mixTransTemp = new ArrayList<MixTranLogBean>();
                            for (MixTranLogBean bean : mixTrans) {
                                if (bean.getTranLogId().equals(log.getTranLogId())) {
                                    mixTransTemp.add(bean);
                                }
                            }
                            log.setMixTrans(mixTransTemp);
                        }

                        logs.add(log);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (logs.isEmpty()) {
                    listener.onFaild(new Response(1, "没有找到相关信息"));
                    return;
                }
                // saveTranLogs(logs);
                StatisticsPresenter.this.logs.addAll(logs);

                for (TranLogBean log : logs) {
                    detialRecords.add(log.toShowStrings());
                    detialRecords_private.add(log.toShowStrings());
                }

                count = o.getLong("count");// 记录数
                totalPage = Tools.getTotalPage(count, 10);
                listener.onSuccess(new Response(0, "success", detialRecords));
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);
            }
        });
    }

    public List<TranLogBean> getLogs() {
        return logs;
    }

    public List<String[]> getDetialItems(int position) {
        if (logs == null) {
            return null;
        }
        try {
            TranLogBean log = logs.get(position);
            String transTypeDes = log.getTranType();
            if (transTypeDes.contains("组合支付")) {
                return log.toMixDetialString();
            } else {
                return log.toTicketDetialString();
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 打印一条记录
     */
    public void printDetialTransaction(String transNum, final ResultListener listener) {
        if (transNum.startsWith("P") || transNum.startsWith("p")) {
            transNum = "P" + AppConfigHelper.getConfig(AppConfigDef.mid) + transNum.substring(1);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tranLogId", transNum);

        NetRequest.getInstance().addRequest(Constants.SC_610_TRAN_LOG_DETAIL, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                JSONArray jsonArray = (JSONArray) response.result;
                if (jsonArray.size() < 1) {
                    listener.onFaild(new Response(1, "没有找到相关信息"));
                    return;
                }
                printDetial(jsonArray);
                listener.onSuccess(new Response(0, "打印完成"));
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);
            }
        });
    }

    private void printDetial(JSONArray jobj) {
        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
        Q1PrintBuilder builder = new Q1PrintBuilder();
        String printString = "";

        printString += builder.center(builder.bold("交易明细")) + builder.br();
        for (int i = 0; i < jobj.size(); i++) {
            JSONObject job = jobj.getJSONObject(i);
            // JSONObject jobMerchantDef = job.getJSONObject("merchantDef");
            printString += builder.branch();
            printString += "流水号：" + job.getString("id") + builder.br();
//            printString += "支付宝交易号：" + builder.br() + job.getString("thirdTradeNo") + builder.br();
            printString += "慧商户号：" + job.getString("mid") + builder.br();
            printString += "终端设备号：" + AppConfigHelper.getConfig(AppConfigDef.sn) + builder.br();
            String tranType = tranTypeChange(job);//解决组合支付部分trancode不识别的问题@yaosong
            printString += "消费类型：" + tranType + builder.br();
            printString += "交易日期：" + DateUtil.format(job.getLong("tranTime"), DateUtil.P2) + builder.br();
            printString += "交易金额：" + Calculater.formotFen(job.getString("tranAmount")) + " 元" + builder.br();

            printString += "补打" + builder.br();
            printString += builder.branch();
        }
        printString += builder.endPrint();
        controller.print(printString);
        controller.cutPaper();
    }

    /**
     * 解决组合支付部分trancode不识别的问题@yaosong
     *
     * @param jsonObject
     * @return trancode string
     */
    private String tranTypeChange(JSONObject jsonObject) {
        String type = Constants.TRAN_TYPE.get(jsonObject.get("tranCode"));
        int tranCode = jsonObject.getInteger("tranCode");
        switch (tranCode) {
            case 791:
                type = "组合支付|非会员券";
                break;

            case 792:
                type = "组合支付|会员券";
                break;

            case 793:
                type = "组合支付|微信卡券核销";
                break;

            case 795:
                type = "组合支付|抹零";
                break;

            case 796:
                type = "组合支付|折扣";
                break;

            default:
                break;
        }
        return type;
    }

    /**
     * 根据流水号查询订单
     *
     * @param transNum
     * @param listener
     */
    public void getTransactionDetial(String transNum, final ResultListener listener) {
        if (TextUtils.isEmpty(transNum)) {
            listener.onFaild(new Response(1, "订单号为空"));
            return;
        }
        if (transNum.startsWith("P") || transNum.startsWith("p")) {
            transNum = "P" + AppConfigHelper.getConfig(AppConfigDef.mid) + transNum.substring(1);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tranLogId", transNum);

        NetRequest.getInstance().addRequest(Constants.SC_610_TRAN_LOG_DETAIL, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                totalPage = 0;
                detialRecords.clear();
                detialRecords_private.clear();
                JSONArray jsonArray = (JSONArray) response.result;
                if (jsonArray.size() < 1) {
                    listener.onFaild(new Response(1, "没有找到相关信息"));
                    return;
                }
                final String mid = AppConfigHelper.getConfig(AppConfigDef.mid);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    LogEx.d("JsonObject", jsonObject.toString());
                    SerialNumInfo info = new SerialNumInfo();
                    info.setId(jsonObject.get("id").toString());
                    info.setMid(jsonObject.get("mid").toString());
                    info.setPosCode(jsonObject.get("posCode").toString());
                    info.setTranAmount(jsonObject.get("tranAmount").toString());
                    info.setTranTime(jsonObject.get("tranTime").toString());
                    info.setTranCode(jsonObject.get("tranCode").toString());
                    String transType = Constants.TRAN_TYPE.get(info.getTranCode());
                    if (TextUtils.isEmpty(transType)) {
                        continue;
                    }
                    if (!transType.contains("会员卡")) {// 所有会员卡消费的信息全部踢除
                        try {
                            String tranLogId = Tools.deleteMidTranLog(info.getId(), mid);
                            String[] tempStrs = new String[]{tranLogId, transType, DateUtil.format(Long.valueOf(info.getTranTime()), "yyyy-MM-dd HH:mm:ss"),
                                    Tools.formatFen(Long.valueOf(info.getTranAmount())),};
                            detialRecords.add(tempStrs);
                            detialRecords_private.add(tempStrs);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                listener.onSuccess(new Response(0, "success", detialRecords));
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);
            }
        });
    }

    /**
     * 根据第三方支付的订单号查询(eg.支付宝生成的订单号，非慧银平台生成)
     *
     * @param tranCode
     * @param timeRange
     * @param startTime
     * @param endTime
     * @param orderNo
     * @param pageNo
     * @param perNo
     * @param listener
     */
    public void getTransDetialByTradeNo(int tranCode, int timeRange, String startTime, String endTime, String orderNo, final String pageNo, String perNo,
                                        final ResultListener listener) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tranCode", tranCode);
        params.put("startTime", TextUtils.isEmpty(startTime) ? "" : startTime);
        params.put("endTime", TextUtils.isEmpty(endTime) ? "" : endTime);
        params.put("timeRange", timeRange);
        if (TextUtils.isEmpty(orderNo)) {
            params.put("thirdTradeNo", "");
        } else {
            if (orderNo.startsWith("P") || orderNo.startsWith("p")) {
                orderNo = "P" + AppConfigHelper.getConfig(AppConfigDef.mid) + orderNo.substring(1);
            }
            params.put("thirdTradeNo", orderNo);
        }
        params.put("pageNo", pageNo);
        params.put("perNo", perNo);

        NetRequest.getInstance().addRequest(Constants.SC_620_TRAN_LOG_QUERY_MS, params, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                totalPage = 0;
                detialRecords.clear();
                detialRecords_private.clear();
                JSONObject o = JSONObject.parseObject(response.getResult().toString());
                count = o.getLong("count");
                JSONArray jarr_tickets = o.getJSONArray("tickets");
                JSONArray jarr = o.getJSONArray("logs");
                if (jarr.size() < 1) {
                    listener.onFaild(new Response(1, "没有找到相关信息"));
                    return;
                }
                totalPage = Tools.getTotalPage(count, 10);
                final String mid = AppConfigHelper.getConfig(AppConfigDef.mid);
                for (int i = 0; i < jarr.size(); i++) {
                    JSONArray jarrArry = jarr.getJSONArray(i);
                    if (jarrArry == null) {
                        listener.onFaild(new Response(1, "没有找到相关信息"));
                        return;
                    }
                    String transType = Constants.TRAN_TYPE.get(jarrArry.get(1).toString());
                    if (TextUtils.isEmpty(transType)) {
                        continue;
                    }
                    if (!transType.contains("会员卡")) {// 所有会员卡消费的信息全部踢除
                        try {
                            String[] tempStrs = new String[]{jarrArry.get(9).toString(),
                                    DateUtil.format(Long.valueOf(jarrArry.get(3).toString()), "yyyy-MM-dd HH:mm:ss"),
                                    Tools.formatFen(Long.valueOf(jarrArry.get(2).toString())),};
                            detialRecords.add(tempStrs);
                            String tranLogId = jarrArry.get(0).toString();
                            tranLogId = Tools.deleteMidTranLog(tranLogId, mid);
                            String[] tempStrs_private = new String[]{tranLogId, transType,
                                    DateUtil.format(Long.valueOf(jarrArry.get(3).toString()), "yyyy-MM-dd HH:mm:ss"),
                                    Tools.formatFen(Long.valueOf(jarrArry.get(2).toString())),};
                            detialRecords_private.add(tempStrs_private);
                            if (Arrays.asList(Constants.kinds).contains(tempStrs[1])) {
                                for (int j = 0; j < jarr_tickets.size(); j++) {
                                    JSONArray jarry_Tickets = jarr_tickets.getJSONArray(j);
                                    String logId = jarrArry.get(0).toString();
                                    String ticketsId = jarry_Tickets.get(0).toString();
                                    if (logId.equals((ticketsId))) {
                                        if (!tempStrs[1].contains("券"))
                                            tempStrs[1] = tempStrs[1] + "(券)";
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                listener.onSuccess(new Response(0, "success", detialRecords));
            }

            @Override
            public void onFaild(Response response) {
                listener.onFaild(response);
            }
        });
    }

    /**
     * 获取总页数
     *
     * @return
     */
    public long getTotalPage() {
        if (totalPage == 0) {
            return 1;
        }
        return totalPage;
    }

    public long getCount() {
        return count;
    }

    /**
     * 清空数据
     */
    private void clearData() {
        try {
            PaymentApplication.getInstance().getDbController().dropTable(TranLogBean.class);
            PaymentApplication.getInstance().getDbController().dropTable(TicketTranLogBean.class);
            PaymentApplication.getInstance().getDbController().dropTable(MixTranLogBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void saveMixTrans(List<MixTranLogBean> mixTranLogBeans) {
        try {
            PaymentApplication.getInstance().getDbController().saveAll(mixTranLogBeans);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void saveTickets(List<TicketTranLogBean> tickets) {
        try {
            PaymentApplication.getInstance().getDbController().saveAll(tickets);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void saveTranLogs(List<TranLogBean> logs) {
        try {
            PaymentApplication.getInstance().getDbController().saveAll(logs);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    public void printRefund(RefundDetailResp resp) {
        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
        Q1PrintBuilder builder = new Q1PrintBuilder();
        String printString = "";
        String merchant = AppConfigHelper.getConfig(AppConfigDef.merchantName);
        printString += builder.center(builder.bold(merchant));
        printString += builder.center("228 Hunt Club Rd.");
        printString += builder.center("Ottawa,Ontario,K1V 1C1");
        printString += builder.center("(613)3196686");
        String address = "";
        String merchantId = AppConfigHelper.getConfig(AppConfigDef.mid);
        printString += "Merchant ID:" + merchantId + builder.br();
        String terminalId = AppConfigHelper.getConfig(AppConfigDef.sn);
        printString += "Terminal ID:" + terminalId + builder.br();
        String cahierId = AppConfigHelper.getConfig(AppConfigDef.operatorTrueName);
        printString += "Cashier ID:" + cahierId + builder.br();
        printString += builder.br();
        printString += builder.center(builder.bold("REFUND")) + builder.br();

        printString += "Total:" + multipleSpaces(25 - Calculater.formotFen(resp.getRefundAmount()).length()) + "$" + Calculater.formotFen(resp.getRefundAmount()) + builder.br();
        String exchangeRate = AppConfigHelper.getConfig(AppConfigDef.exchangeRate);
        if (TextUtils.isEmpty(exchangeRate)) {
            exchangeRate = "1";
        }
        String cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(Calculater.formotFen(resp.getRefundAmount()), exchangeRate)));
        printString += multipleSpaces(28 - cnyAmount.length()) + "CNY " + cnyAmount + builder.br();
        printString += builder.br();
        String tranlogId = Tools.deleteMidTranLog(resp.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
        printString += "Receipt#" + multipleSpaces(24 - tranlogId.length()) + tranlogId + builder.br();
        printString += DateUtil.format(new Date(), DateUtil.P1) + multipleSpaces(22 - DateUtil.format(new Date(), DateUtil.P12).length()) + DateUtil.format(new Date(), DateUtil.P12) + builder.br();
        String payType = resp.getTransKind();
        printString += "Type:" + multipleSpaces(27 - payType.length()) + payType + builder.br();
        String thirdTransOrder = resp.getThirdTradeNo();
        if (!TextUtils.isEmpty(thirdTransOrder)) {
            printString += "Trans#:" + builder.br();
            printString += multipleSpaces(32 - thirdTransOrder.length()) + thirdTransOrder + builder.br();
        }
        String acctName = resp.getThirdExtName();
        if (!TextUtils.isEmpty(acctName)) {
            printString += "Acct Name:" + multipleSpaces(22 - acctName.length()) + acctName + builder.br();
        }
        String acct = resp.getThirdExtId();
        if (!TextUtils.isEmpty(acct)) {
            printString += "Acct:" + multipleSpaces(27 - acct.length()) + acct + builder.br();
        }

        printString += builder.br();
        printString += builder.center(builder.bold("APPPROVED"));
        printString += builder.br();
        printString += builder.center(builder.bold("MERCHANT COPY"));
        printString += builder.center(builder.bold("-important-"));
        printString += builder.center(builder.bold("Please retain this Copy"));
        printString += builder.center(builder.bold("for your records."));
        printString += builder.branch() + builder.endPrint();
        controller.print(printString);
        controller.cutPaper();
    }

    public void reprintCustomerRefund(DailyDetailResp resp) {
        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
            reprintRefundByBitmap(resp, context.getString(R.string.print_customer_copy));
            return;
        }
        try {
            PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
            Q1PrintBuilder builder = new Q1PrintBuilder();
            String printString = "";
            String merchant = AppConfigHelper.getConfig(AppConfigDef.merchantName);
            printString += builder.center(builder.bold(merchant + context.getString(R.string.print_reprint)));
            String address = AppConfigHelper.getConfig(AppConfigDef.merchantAddr);
            if (!TextUtils.isEmpty(address)) {
                if (address.getBytes("GBK").length <= 32) {
                    printString += builder.center(address);
                } else if (address.getBytes("GBK").length <= 64) {
                    printString += builder.center(address.substring(0, 32));
                    printString += builder.center(address.substring(32));
                } else if (address.getBytes("GBK").length <= 96) {
                    printString += builder.center(address.substring(0, 32));
                    printString += builder.center(address.substring(32, 64));
                    printString += builder.center(address.substring(64));
                }
            }
            String tel = AppConfigHelper.getConfig(AppConfigDef.merchantTel);
            if (!TextUtils.isEmpty(tel)) {
                printString += builder.center(tel);
            }
            String merchantId = AppConfigHelper.getConfig(AppConfigDef.mid);
            printString += context.getString(R.string.print_merchant_id) + merchantId + builder.br();
            String terminalId = AppConfigHelper.getConfig(AppConfigDef.sn);
            printString += context.getString(R.string.print_terminal_id) + terminalId + builder.br();
            String cahierId = AppConfigHelper.getConfig(AppConfigDef.operatorTrueName);
            printString += context.getString(R.string.print_cashier_id) + cahierId + builder.br();
            printString += builder.br() + builder.nBr();
            printString += builder.center(builder.bold(context.getString(R.string.refund_uppercase))) + builder.br() + builder.nBr();
            printString += "Total:" + multipleSpaces(25 - Calculater.formotFen(resp.getSingleAmount().replace("-", "").trim()).length()) + "$" + Calculater.formotFen(resp.getSingleAmount().replace("-", "").trim()) + builder.br();
            String exchangeRate = resp.getExchangeRate();
            if (TextUtils.isEmpty(exchangeRate)) {
                exchangeRate = "1";
            }
            String cnyAmount = Calculater.formotFen(resp.getCnyAmount()).replace("-", "").trim();
            if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
                cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(Calculater.formotFen(resp.getSingleAmount().replace("-", "").trim()), exchangeRate)));
            }
            printString += multipleSpaces(28 - cnyAmount.length()) + "CNY " + cnyAmount + builder.br();
            printString += builder.br() + builder.nBr();
            String tranlogId = Tools.deleteMidTranLog(resp.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
            String printRecepit = context.getString(R.string.print_receipt);
            printString += printRecepit + "#" + multipleSpaces(31 - printRecepit.getBytes("GBK").length - tranlogId.length()) + tranlogId + builder.br();
            printString += resp.getPayTime().substring(0, 10) + multipleSpaces(22 - resp.getPayTime().substring(10).length()) + resp.getPayTime().substring(10) + builder.br();
            String payType = resp.getTransName().replace(context.getString(R.string.pay_tag), "").replace(context.getString(R.string.revoke_tag), "").trim();
            if ("Wechat".equals(payType)) {
                payType = "Wechat Pay";
            } else if (payType.contains("Union")) {
                payType = "Union Pay QR";
            }
            String printType = context.getString(R.string.print_type);
            printString += printType + multipleSpaces(32 - printType.getBytes("GBK").length - payType.length()) + payType + builder.br();
            String thirdTransOrder = resp.getThirdTradeNo();
            if (!TextUtils.isEmpty(thirdTransOrder)) {
                printString += context.getString(R.string.print_trans) + builder.br();
                printString += multipleSpaces(32 - thirdTransOrder.length()) + thirdTransOrder + builder.br();
            }
            String acctName = resp.getThirdExtName();
            if (!TextUtils.isEmpty(acctName)) {
                String printAcctName = context.getString(R.string.print_acctName);
                printString += printAcctName + multipleSpaces(32 - printAcctName.getBytes("GBK").length - acctName.getBytes("GBK").length) + acctName + builder.br();
            }
            String acct = resp.getThirdExtId();
            if (!TextUtils.isEmpty(acct)) {
                String printAcct = context.getString(R.string.print_acct);
                printString += printAcct + multipleSpaces(32 - printAcct.getBytes("GBK").length - acct.getBytes("GBK").length) + acct + builder.br();
            }
            printString += builder.br();
            printString += builder.center(builder.bold(context.getString(R.string.print_approved)));
            printString += builder.br() + builder.nBr();
            printString += builder.center(builder.bold(context.getString(R.string.print_customer_copy)));
            printString += builder.center(builder.bold(context.getString(R.string.print_important)));
            printString += builder.center(builder.bold(context.getString(R.string.print_hint1)));
            printString += builder.center(builder.bold(context.getString(R.string.print_hint2)));
            printString += builder.branch() + builder.endPrint();
            controller.print(printString);
            controller.cutPaper();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void reprintMerchantRefund(DailyDetailResp resp) {
        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
            reprintRefundByBitmap(resp, context.getString(R.string.print_merchant_copy));
            return;
        }
        try {
            PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
            Q1PrintBuilder builder = new Q1PrintBuilder();
            String printString = "";
            String merchant = AppConfigHelper.getConfig(AppConfigDef.merchantName);
            printString += builder.center(builder.bold(merchant + context.getString(R.string.print_reprint)));
            String address = AppConfigHelper.getConfig(AppConfigDef.merchantAddr);
            if (!TextUtils.isEmpty(address)) {
                if (address.getBytes("GBK").length <= 32) {
                    printString += builder.center(address);
                } else if (address.getBytes("GBK").length <= 64) {
                    printString += builder.center(address.substring(0, 32));
                    printString += builder.center(address.substring(32));
                } else if (address.getBytes("GBK").length <= 96) {
                    printString += builder.center(address.substring(0, 32));
                    printString += builder.center(address.substring(32, 64));
                    printString += builder.center(address.substring(64));
                }
            }
            String tel = AppConfigHelper.getConfig(AppConfigDef.merchantTel);
            if (!TextUtils.isEmpty(tel)) {
                printString += builder.center(tel);
            }
            String merchantId = AppConfigHelper.getConfig(AppConfigDef.mid);
            printString += context.getString(R.string.print_merchant_id) + merchantId + builder.br();
            String terminalId = AppConfigHelper.getConfig(AppConfigDef.sn);
            printString += context.getString(R.string.print_terminal_id) + terminalId + builder.br();
            String cahierId = AppConfigHelper.getConfig(AppConfigDef.operatorTrueName);
            printString += context.getString(R.string.print_cashier_id) + cahierId + builder.br();
            printString += builder.br() + builder.nBr();
            printString += builder.center(builder.bold(context.getString(R.string.refund_uppercase))) + builder.br() + builder.nBr();
            printString += "Total:" + multipleSpaces(25 - Calculater.formotFen(resp.getSingleAmount().replace("-", "").trim()).length()) + "$" + Calculater.formotFen(resp.getSingleAmount().replace("-", "").trim()) + builder.br();
            String exchangeRate = resp.getExchangeRate();
            if (TextUtils.isEmpty(exchangeRate)) {
                exchangeRate = "1";
            }
            String cnyAmount = Calculater.formotFen(resp.getCnyAmount()).replace("-", "").trim();
            if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
                cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(Calculater.formotFen(resp.getSingleAmount().replace("-", "").trim()), exchangeRate)));
            }
            printString += multipleSpaces(28 - cnyAmount.length()) + "CNY " + cnyAmount + builder.br();
            printString += builder.br() + builder.nBr();
            String tranlogId = Tools.deleteMidTranLog(resp.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
            String printRecepit = context.getString(R.string.print_receipt);
            printString += printRecepit + "#" + multipleSpaces(31 - printRecepit.getBytes("GBK").length - tranlogId.length()) + tranlogId + builder.br();
            printString += resp.getPayTime().substring(0, 10) + multipleSpaces(22 - resp.getPayTime().substring(10).length()) + resp.getPayTime().substring(10) + builder.br();
            String payType = resp.getTransName().replace(context.getString(R.string.pay_tag), "").replace(context.getString(R.string.revoke_tag), "").trim();
            if ("Wechat".equals(payType)) {
                payType = "Wechat Pay";
            } else if (payType.contains("Union")) {
                payType = "Union Pay QR";
            }
            String printType = context.getString(R.string.print_type);
            printString += printType + multipleSpaces(32 - printType.getBytes("GBK").length - payType.length()) + payType + builder.br();
            String thirdTransOrder = resp.getThirdTradeNo();
            if (!TextUtils.isEmpty(thirdTransOrder)) {
                printString += context.getString(R.string.print_trans) + builder.br();
                printString += multipleSpaces(32 - thirdTransOrder.length()) + thirdTransOrder + builder.br();
            }
            String acctName = resp.getThirdExtName();
            if (!TextUtils.isEmpty(acctName)) {
                String printAcctName = context.getString(R.string.print_acctName);
                printString += printAcctName + multipleSpaces(32 - printAcctName.getBytes("GBK").length - acctName.getBytes("GBK").length) + acctName + builder.br();
            }
            String acct = resp.getThirdExtId();
            if (!TextUtils.isEmpty(acct)) {
                String printAcct = context.getString(R.string.print_acct);
                printString += printAcct + multipleSpaces(32 - printAcct.getBytes("GBK").length - acct.getBytes("GBK").length) + acct + builder.br();
            }
            printString += builder.br() + builder.nBr();
            printString += builder.center(builder.bold(context.getString(R.string.print_approved)));
            printString += builder.br() + builder.nBr();
            printString += builder.center(builder.bold(context.getString(R.string.print_merchant_copy)));
            printString += builder.center(builder.bold(context.getString(R.string.print_important)));
            printString += builder.center(builder.bold(context.getString(R.string.print_hint1)));
            printString += builder.center(builder.bold(context.getString(R.string.print_hint2)));
            printString += builder.branch() + builder.endPrint();
            controller.print(printString);
            controller.cutPaper();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * webView Bitmap方式补打
     *
     * @param resp
     */
    private void reprintRefundByBitmap(DailyDetailResp resp, String customerOrMerchant) {
        HTMLPrintModel model = new HTMLPrintModel();
        List<HtmlLine> lines = new ArrayList<>();

        lines.add(new HTMLPrintModel.SimpleLine(AppConfigHelper.getConfig(AppConfigDef.merchantName) + context.getString(R.string.print_reprint), true, true));
        String address = AppConfigHelper.getConfig(AppConfigDef.merchantAddr);
        if (!TextUtils.isEmpty(address)) {
            try {
                if (address.getBytes("GBK").length <= 32) {
                    lines.add(new HTMLPrintModel.SimpleLine(address, false, true));
                } else if (address.getBytes("GBK").length <= 64) {
                    lines.add(new HTMLPrintModel.SimpleLine(address.substring(0, 32), false, true));
                    lines.add(new HTMLPrintModel.SimpleLine(address.substring(32), false, true));
                } else if (address.getBytes("GBK").length <= 96) {
                    lines.add(new HTMLPrintModel.SimpleLine(address.substring(0, 32), false, true));
                    lines.add(new HTMLPrintModel.SimpleLine(address.substring(32, 64), false, true));
                    lines.add(new HTMLPrintModel.SimpleLine(address.substring(64), false, true));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String tel = AppConfigHelper.getConfig(AppConfigDef.merchantTel);
        if (!TextUtils.isEmpty(tel)) {
            lines.add(new HTMLPrintModel.SimpleLine(tel, false, true));
        }
        String merchantId = AppConfigHelper.getConfig(AppConfigDef.mid);
        lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_merchant_id) + merchantId));
        String terminalId = AppConfigHelper.getConfig(AppConfigDef.sn);
        lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_terminal_id) + terminalId));
        String cahierId = AppConfigHelper.getConfig(AppConfigDef.operatorTrueName);
        lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_cashier_id) + cahierId));
        lines.add(new HTMLPrintModel.EmptyLine());
        lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.refund_uppercase), true, true));
        lines.add(new HTMLPrintModel.LeftAndRightLine("Total:", "$" + Calculater.formotFen(resp.getSingleAmount().replace("-", "").trim())));
        String exchangeRate = resp.getExchangeRate();
        if (TextUtils.isEmpty(exchangeRate)) {
            exchangeRate = "1";
        }
        String cnyAmount = Calculater.formotFen(resp.getCnyAmount()).replace("-", "").trim();
        if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
            cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(Calculater.formotFen(resp.getSingleAmount().replace("-", "").trim()), exchangeRate)));
        }
        lines.add(new HTMLPrintModel.LeftAndRightLine("", "CNY " + cnyAmount));
        lines.add(new HTMLPrintModel.EmptyLine());
        String tranlogId = Tools.deleteMidTranLog(resp.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
        String printRecepit = context.getString(R.string.print_receipt);
        lines.add(new HTMLPrintModel.LeftAndRightLine(printRecepit + "#", tranlogId));
        lines.add(new HTMLPrintModel.LeftAndRightLine(resp.getPayTime().substring(0, 10), resp.getPayTime().substring(10)));
        String payType = resp.getTransName().replace(context.getString(R.string.pay_tag), "").replace(context.getString(R.string.revoke_tag), "").trim();
        if ("Wechat".equals(payType)) {
            payType = "Wechat Pay";
        }
        String printType = context.getString(R.string.print_type);
        lines.add(new HTMLPrintModel.LeftAndRightLine(printType, payType));

        String thirdTransOrder = resp.getThirdTradeNo();
        if (!TextUtils.isEmpty(thirdTransOrder)) {
            lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_trans)));
            lines.add(new HTMLPrintModel.LeftAndRightLine("", thirdTransOrder));
        }
        String acctName = resp.getThirdExtName();
        if (!TextUtils.isEmpty(acctName)) {
            String printAcctName = context.getString(R.string.print_acctName);
            lines.add(new HTMLPrintModel.LeftAndRightLine(printAcctName, acctName));
        }
        String acct = resp.getThirdExtId();
        if (!TextUtils.isEmpty(acct)) {
            String printAcct = context.getString(R.string.print_acct);
            lines.add(new HTMLPrintModel.LeftAndRightLine(printAcct, acct));
        }
        lines.add(new HTMLPrintModel.EmptyLine());
        lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_approved), true, true));
        lines.add(new HTMLPrintModel.EmptyLine());
        lines.add(new HTMLPrintModel.SimpleLine(customerOrMerchant, true, true));
        lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_important), true, true));
        lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_hint1), true, true));
        lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_hint2), true, true));
        model.setLineList(lines);
        WebPrintHelper.getInstance().print(model);
//        PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), model));
    }

    public void reprintMerchantSale(DailyDetailResp resp) {
        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
            reprintSaleByBitmap(resp, context.getString(R.string.print_merchant_copy));
            return;
        }
        try {
            PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
            Q1PrintBuilder builder = new Q1PrintBuilder();
            String printString = "";
            String merchant = AppConfigHelper.getConfig(AppConfigDef.merchantName);
            printString += builder.center(builder.bold(merchant + context.getString(R.string.print_reprint)));
            String address = AppConfigHelper.getConfig(AppConfigDef.merchantAddr);
            if (!TextUtils.isEmpty(address)) {
                if (address.getBytes("GBK").length <= 32) {
                    printString += builder.center(address);
                } else if (address.getBytes("GBK").length <= 64) {
                    printString += builder.center(address.substring(0, 32));
                    printString += builder.center(address.substring(32));
                } else if (address.getBytes("GBK").length <= 96) {
                    printString += builder.center(address.substring(0, 32));
                    printString += builder.center(address.substring(32, 64));
                    printString += builder.center(address.substring(64));
                }
            }
            String tel = AppConfigHelper.getConfig(AppConfigDef.merchantTel);
            if (!TextUtils.isEmpty(tel)) {
                printString += builder.center(tel);
            }
            String merchantId = AppConfigHelper.getConfig(AppConfigDef.mid);
            printString += context.getString(R.string.print_merchant_id) + merchantId + builder.br();
            String terminalId = AppConfigHelper.getConfig(AppConfigDef.sn);
            printString += context.getString(R.string.print_terminal_id) + terminalId + builder.br();
            String cahierId = AppConfigHelper.getConfig(AppConfigDef.operatorTrueName);
            printString += context.getString(R.string.print_cashier_id) + cahierId + builder.br();
            printString += builder.br();
            printString += builder.center(builder.bold(context.getString(R.string.print_sale))) + builder.br();
            String totalAmount = resp.getSingleAmount();
            String tipsAmount = resp.getTipAmount();
            String printPurchase = context.getString(R.string.print_purchase);
            if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
                String purchaseAmount = Calculater.formotFen(Calculater.subtract(totalAmount, tipsAmount));
                printString += printPurchase + multipleSpaces(31 - printPurchase.getBytes("GBK").length - purchaseAmount.length()) + "$" + purchaseAmount + builder.br();
            } else {
                printString += printPurchase + multipleSpaces(31 - printPurchase.getBytes("GBK").length - Calculater.formotFen(resp.getSingleAmount()).length()) + "$" + Calculater.formotFen(resp.getSingleAmount()) + builder.br();
            }
            if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
                String printTip = context.getString(R.string.print_tip);
                printString += printTip + multipleSpaces(31 - printTip.getBytes("GBK").length - Calculater.formotFen(tipsAmount).length()) + "$" + Calculater.formotFen(tipsAmount) + builder.br();
            }
            printString += "Total:" + multipleSpaces(25 - Calculater.formotFen(resp.getSingleAmount()).length()) + "$" + Calculater.formotFen(resp.getSingleAmount()) + builder.br();
            String exchangeRate = resp.getExchangeRate();
            if (TextUtils.isEmpty(exchangeRate)) {
                exchangeRate = "1";
            }
            String cnyAmount = Calculater.formotFen(resp.getCnyAmount()).replace("-", "").trim();
            if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
                cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(Calculater.formotFen(resp.getSingleAmount()), exchangeRate)));
            }
            printString += multipleSpaces(28 - cnyAmount.length()) + "CNY " + cnyAmount + builder.br();
            String showCNY = "CAD 1.00=CNY " + Calculater.multiply("1", exchangeRate);
            String printFx = context.getString(R.string.print_fx_rate);
            printString += printFx + multipleSpaces(32 - printFx.getBytes("GBK").length - showCNY.length()) + showCNY + builder.br();
            printString += builder.br();
            String tranlogId = Tools.deleteMidTranLog(resp.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
            String printRecepit = context.getString(R.string.print_receipt);
            printString += printRecepit + "#" + multipleSpaces(31 - printRecepit.getBytes("GBK").length - tranlogId.length()) + tranlogId + builder.br();
            printString += resp.getPayTime().substring(0, 10) + multipleSpaces(22 - resp.getPayTime().substring(10).length()) + resp.getPayTime().substring(10) + builder.br();
            String payType = resp.getTransName().replace(context.getString(R.string.pay_tag), "").replace(context.getString(R.string.revoke_tag), "").trim();
            if ("Wechat".equals(payType)) {
                payType = "Wechat Pay";
            } else if (payType.contains("Union")) {
                payType = "Union Pay QR";
            }
            String printType = context.getString(R.string.print_type);
            printString += printType + multipleSpaces(32 - printType.getBytes("GBK").length - payType.getBytes("GBK").length) + payType + builder.br();
            String thirdTransOrder = resp.getThirdTradeNo();
            if (!TextUtils.isEmpty(thirdTransOrder)) {
                printString += context.getString(R.string.print_trans) + builder.br();
                printString += multipleSpaces(32 - thirdTransOrder.getBytes("GBK").length) + thirdTransOrder + builder.br();
            }
            String acctName = resp.getThirdExtName();
            if (!TextUtils.isEmpty(acctName)) {
                String printAcctName = context.getString(R.string.print_acctName);
                printString += printAcctName + multipleSpaces(32 - printAcctName.getBytes("GBK").length - acctName.getBytes("GBK").length) + acctName + builder.br();
            }
            String acct = resp.getThirdExtId();
            if (!TextUtils.isEmpty(acct)) {
                String printAcct = context.getString(R.string.print_acct);
                printString += printAcct + multipleSpaces(32 - printAcct.getBytes("GBK").length - acct.getBytes("GBK").length) + acct + builder.br();
            }
            printString += builder.br();
            printString += builder.center(builder.bold(context.getString(R.string.print_approved)));
            printString += builder.br();
            printString += builder.center(builder.bold(context.getString(R.string.print_merchant_copy)));
            printString += builder.center(builder.bold(context.getString(R.string.print_important)));
            printString += builder.center(builder.bold(context.getString(R.string.print_hint1)));
            printString += builder.center(builder.bold(context.getString(R.string.print_hint2)));
            printString += builder.branch() + builder.endPrint();
            controller.print(printString);
            controller.cutPaper();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reprintCustomerSale(DailyDetailResp resp) {
        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
            reprintSaleByBitmap(resp, context.getString(R.string.print_customer_copy));
            return;
        }
        try {
            PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
            Q1PrintBuilder builder = new Q1PrintBuilder();
            String printString = "";
            String merchant = AppConfigHelper.getConfig(AppConfigDef.merchantName);
            printString += builder.center(builder.bold(merchant + context.getString(R.string.print_reprint)));
            String address = AppConfigHelper.getConfig(AppConfigDef.merchantAddr);
            if (!TextUtils.isEmpty(address)) {
                if (address.getBytes("GBK").length <= 32) {
                    printString += builder.center(address);
                } else if (address.getBytes("GBK").length <= 64) {
                    printString += builder.center(address.substring(0, 32));
                    printString += builder.center(address.substring(32));
                } else if (address.getBytes("GBK").length <= 96) {
                    printString += builder.center(address.substring(0, 32));
                    printString += builder.center(address.substring(32, 64));
                    printString += builder.center(address.substring(64));
                }
            }
            String tel = AppConfigHelper.getConfig(AppConfigDef.merchantTel);
            if (!TextUtils.isEmpty(tel)) {
                printString += builder.center(tel);
            }
            String merchantId = AppConfigHelper.getConfig(AppConfigDef.mid);
            printString += context.getString(R.string.print_merchant_id) + merchantId + builder.br();
            String terminalId = AppConfigHelper.getConfig(AppConfigDef.sn);
            printString += context.getString(R.string.print_terminal_id) + terminalId + builder.br();
            String cahierId = AppConfigHelper.getConfig(AppConfigDef.operatorTrueName);
            printString += context.getString(R.string.print_cashier_id) + cahierId + builder.br();
            printString += builder.br();
            printString += builder.center(builder.bold(context.getString(R.string.print_sale))) + builder.br();
            String totalAmount = resp.getSingleAmount();
            String tipsAmount = resp.getTipAmount();
            String printPurchase = context.getString(R.string.print_purchase);
            if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
                String purchaseAmount = Calculater.formotFen(Calculater.subtract(totalAmount, tipsAmount));
                printString += printPurchase + multipleSpaces(31 - printPurchase.getBytes("GBK").length - purchaseAmount.length()) + "$" + purchaseAmount + builder.br();
            } else {
                printString += printPurchase + multipleSpaces(31 - printPurchase.getBytes("GBK").length - Calculater.formotFen(resp.getSingleAmount()).length()) + "$" + Calculater.formotFen(resp.getSingleAmount()) + builder.br();
            }
            if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
                String printTip = context.getString(R.string.print_tip);
                printString += printTip + multipleSpaces(31 - printTip.getBytes("GBK").length - Calculater.formotFen(tipsAmount).length()) + "$" + Calculater.formotFen(tipsAmount) + builder.br();
            }
            printString += "Total:" + multipleSpaces(25 - Calculater.formotFen(resp.getSingleAmount()).length()) + "$" + Calculater.formotFen(resp.getSingleAmount()) + builder.br();
            String exchangeRate = resp.getExchangeRate();
            if (TextUtils.isEmpty(exchangeRate)) {
                exchangeRate = "1";
            }
            String cnyAmount = Calculater.formotFen(resp.getCnyAmount()).replace("-", "").trim();
            if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
                cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(Calculater.formotFen(resp.getSingleAmount()), exchangeRate)));
            }
            printString += multipleSpaces(28 - cnyAmount.length()) + "CNY " + cnyAmount + builder.br();
            String showCNY = "CAD 1.00=CNY " + Calculater.multiply("1", exchangeRate);
            String printFx = context.getString(R.string.print_fx_rate);
            printString += printFx + multipleSpaces(32 - printFx.getBytes("GBK").length - showCNY.length()) + showCNY + builder.br();
            printString += builder.br();
            String tranlogId = Tools.deleteMidTranLog(resp.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
            String printRecepit = context.getString(R.string.print_receipt);
            printString += printRecepit + "#" + multipleSpaces(31 - printRecepit.getBytes("GBK").length - tranlogId.length()) + tranlogId + builder.br();
            printString += resp.getPayTime().substring(0, 10) + multipleSpaces(22 - resp.getPayTime().substring(10).length()) + resp.getPayTime().substring(10) + builder.br();
            String payType = resp.getTransName().replace(context.getString(R.string.pay_tag), "").replace(context.getString(R.string.revoke_tag), "").trim();
            if ("Wechat".equals(payType)) {
                payType = "Wechat Pay";
            } else if (payType.contains("Union") || payType.contains("UNS")) {
                payType = "Union Pay QR";
            }
            String printType = context.getString(R.string.print_type);
            printString += printType + multipleSpaces(32 - printType.getBytes("GBK").length - payType.getBytes("GBK").length) + payType + builder.br();
            String thirdTransOrder = resp.getThirdTradeNo();
            if (!TextUtils.isEmpty(thirdTransOrder)) {
                printString += context.getString(R.string.print_trans) + builder.br();
                printString += multipleSpaces(32 - thirdTransOrder.getBytes("GBK").length) + thirdTransOrder + builder.br();
            }
            String acctName = resp.getThirdExtName();
            if (!TextUtils.isEmpty(acctName)) {
                String printAcctName = context.getString(R.string.print_acctName);
                printString += printAcctName + multipleSpaces(32 - printAcctName.getBytes("GBK").length - acctName.getBytes("GBK").length) + acctName + builder.br();
            }
            String acct = resp.getThirdExtId();
            if (!TextUtils.isEmpty(acct)) {
                String printAcct = context.getString(R.string.print_acct);
                printString += printAcct + multipleSpaces(32 - printAcct.getBytes("GBK").length - acct.getBytes("GBK").length) + acct + builder.br();
            }
            printString += builder.br();
            printString += builder.center(builder.bold(context.getString(R.string.print_approved)));
            printString += builder.br();
            printString += builder.center(builder.bold(context.getString(R.string.print_customer_copy)));
            printString += builder.center(builder.bold(context.getString(R.string.print_important)));
            printString += builder.center(builder.bold(context.getString(R.string.print_hint1)));
            printString += builder.center(builder.bold(context.getString(R.string.print_hint2)));
            printString += builder.branch() + builder.endPrint();
            controller.print(printString);
            controller.cutPaper();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * webView Bitmap方式补打
     *
     * @param resp
     */
    private void reprintSaleByBitmap(DailyDetailResp resp, String customerOrMerchant) {
        HTMLPrintModel model = new HTMLPrintModel();
        List<HtmlLine> lines = new ArrayList<>();
        lines.add(new HTMLPrintModel.SimpleLine(AppConfigHelper.getConfig(AppConfigDef.merchantName) + context.getString(R.string.print_reprint), true, true));
        String address = AppConfigHelper.getConfig(AppConfigDef.merchantAddr);
        if (!TextUtils.isEmpty(address)) {
            try {
                if (address.getBytes("GBK").length <= 32) {
                    lines.add(new HTMLPrintModel.SimpleLine(address, false, true));
                } else if (address.getBytes("GBK").length <= 64) {
                    lines.add(new HTMLPrintModel.SimpleLine(address.substring(0, 32), false, true));
                    lines.add(new HTMLPrintModel.SimpleLine(address.substring(32), false, true));
                } else if (address.getBytes("GBK").length <= 96) {
                    lines.add(new HTMLPrintModel.SimpleLine(address.substring(0, 32), false, true));
                    lines.add(new HTMLPrintModel.SimpleLine(address.substring(32, 64), false, true));
                    lines.add(new HTMLPrintModel.SimpleLine(address.substring(64), false, true));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String tel = AppConfigHelper.getConfig(AppConfigDef.merchantTel);
        if (!TextUtils.isEmpty(tel)) {
            lines.add(new HTMLPrintModel.SimpleLine(tel, false, true));
        }
        String merchantId = AppConfigHelper.getConfig(AppConfigDef.mid);
        lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_merchant_id) + merchantId));
        String terminalId = AppConfigHelper.getConfig(AppConfigDef.sn);
        lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_terminal_id) + terminalId));
        String cahierId = AppConfigHelper.getConfig(AppConfigDef.operatorTrueName);
        lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_cashier_id) + cahierId));
        lines.add(new HTMLPrintModel.EmptyLine());
        lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_sale), true, true));
        String totalAmount = resp.getSingleAmount();
        String tipsAmount = resp.getTipAmount();
        String printPurchase = context.getString(R.string.print_purchase);
        if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
            String purchaseAmount = Calculater.formotFen(Calculater.subtract(totalAmount, tipsAmount));
            lines.add(new HTMLPrintModel.LeftAndRightLine(printPurchase, "$" + purchaseAmount));
        } else {
            lines.add(new HTMLPrintModel.LeftAndRightLine(printPurchase, "$" + Calculater.formotFen(resp.getSingleAmount())));
        }
        if (!TextUtils.isEmpty(tipsAmount) && !tipsAmount.equals("0")) {
            String printTip = context.getString(R.string.print_tip);
            lines.add(new HTMLPrintModel.LeftAndRightLine(printTip, "$" + Calculater.formotFen(tipsAmount)));
        }
        lines.add(new HTMLPrintModel.LeftAndRightLine("Total:", "$" + Calculater.formotFen(resp.getSingleAmount())));
        String exchangeRate = resp.getExchangeRate();
        if (TextUtils.isEmpty(exchangeRate)) {
            exchangeRate = "1";
        }
        String cnyAmount = Calculater.formotFen(resp.getCnyAmount()).replace("-", "").trim();
        if (TextUtils.isEmpty(cnyAmount) || "0.00".equals(cnyAmount)) {
            cnyAmount = String.format("%.2f", Float.parseFloat(Calculater.multiply(Calculater.formotFen(resp.getSingleAmount()), exchangeRate)));
        }
        lines.add(new HTMLPrintModel.LeftAndRightLine("", "CNY " + cnyAmount));
        String showCNY = "CAD 1.00=CNY " + Calculater.multiply("1", exchangeRate);
        String printFx = context.getString(R.string.print_fx_rate);
        lines.add(new HTMLPrintModel.LeftAndRightLine(printFx, showCNY));
        lines.add(new HTMLPrintModel.EmptyLine());
        String tranlogId = Tools.deleteMidTranLog(resp.getTranLogId(), AppConfigHelper.getConfig(AppConfigDef.mid));
        String printRecepit = context.getString(R.string.print_receipt);
        lines.add(new HTMLPrintModel.LeftAndRightLine(printRecepit + "#", tranlogId));
        lines.add(new HTMLPrintModel.LeftAndRightLine(resp.getPayTime().substring(0, 10), resp.getPayTime().substring(10)));
        String payType = resp.getTransName().replace(context.getString(R.string.pay_tag), "").replace(context.getString(R.string.revoke_tag), "").trim();
        if ("Wechat".equals(payType)) {
            payType = "Wechat Pay";
        }
        String printType = context.getString(R.string.print_type);
        lines.add(new HTMLPrintModel.LeftAndRightLine(printType, payType));
        String thirdTransOrder = resp.getThirdTradeNo();
        if (!TextUtils.isEmpty(thirdTransOrder)) {
            lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_trans)));
            lines.add(new HTMLPrintModel.LeftAndRightLine("", thirdTransOrder));
        }
        String acctName = resp.getThirdExtName();
        if (!TextUtils.isEmpty(acctName)) {
            String printAcctName = context.getString(R.string.print_acctName);
            lines.add(new HTMLPrintModel.LeftAndRightLine(printAcctName, acctName));
        }
        String acct = resp.getThirdExtId();
        if (!TextUtils.isEmpty(acct)) {
            String printAcct = context.getString(R.string.print_acct);
            lines.add(new HTMLPrintModel.LeftAndRightLine(printAcct, acct));
        }
        lines.add(new HTMLPrintModel.EmptyLine());
        lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_approved), true, true));
        lines.add(new HTMLPrintModel.EmptyLine());
        lines.add(new HTMLPrintModel.SimpleLine(customerOrMerchant, true, true));
        lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_important), true, true));
        lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_hint1), true, true));
        lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_hint2), true, true));
        model.setLineList(lines);
        WebPrintHelper.getInstance().print(model);
//        PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), model));
    }

    /**
     * 打印空格
     *
     * @param n
     * @return
     */
    public String multipleSpaces(int n) {
        String output = "";
        for (int i = 0; i < n; i++)
            output += " ";
        return output;
    }

    /**
     * 极简收款 今日汇总打印
     */
    public void printTodaySum() {
        List<TodayDetailBean> todayDetailBeans = dailyRes.getList();
        if (todayDetailBeans == null || todayDetailBeans.size() < 1) {
            return;
        }
        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
            HTMLPrintModel model = new HTMLPrintModel();
            List<HtmlLine> lines = new ArrayList<>();
            lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_daily_summary_title) + "(" + getShowCurrentTime() + ")", true, true));
            lines.add(new HTMLPrintModel.BranchLine());
            lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_terminal_id) + AppConfigHelper.getConfig(AppConfigDef.sn, "")));
            lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_merchant_id) + AppConfigHelper.getConfig(AppConfigDef.mid, "")));
            lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_merchant_name) + AppConfigHelper.getConfig(AppConfigDef.merchantName, "")));
            lines.add(new HTMLPrintModel.LeftAndRightLine(DateUtil.format(new Date(), DateUtil.P1), DateUtil.format(new Date(), DateUtil.P12)));
            lines.add(new HTMLPrintModel.BranchLine());
            lines.add(new HTMLPrintModel.LeftAndRightLine(context.getString(R.string.print_daily_total), "$" + Calculater.formotFen(dailyRes.getTransTotalAmount())));
            lines.add(new HTMLPrintModel.LeftAndRightLine(context.getString(R.string.print_daily_total_transactions), dailyRes.getTransTotalAcount()));
            if (TextUtils.isEmpty(dailyRes.getRefundAmount())) {
                lines.add(new HTMLPrintModel.LeftAndRightLine(context.getString(R.string.print_daily_refund), "$0.00"));
            } else {
                lines.add(new HTMLPrintModel.LeftAndRightLine(context.getString(R.string.print_daily_refund), "$" + Calculater.formotFen(dailyRes.getRefundAmount())));
            }
            String count = "0";
            if (!TextUtils.isEmpty(dailyRes.getRefundCount())) {
                count = dailyRes.getRefundCount();
            }
            lines.add(new HTMLPrintModel.LeftAndRightLine(context.getString(R.string.print_daily_refund_transactions), count));
            lines.add(new HTMLPrintModel.BranchLine());
            lines.add(new HTMLPrintModel.ThreeStrLine(context.getString(R.string.print_payType), context.getString(R.string.print_transactions), context.getString(R.string.print_amount)));
            for (TodayDetailBean detail : todayDetailBeans) {
                lines.add(new HTMLPrintModel.ThreeStrLine(detail.getDetailName(), detail.getCount() + "", "$" + Calculater.formotFen(detail.getAmount())));
            }
            lines.add(new HTMLPrintModel.BranchLine());
            model.setLineList(lines);
            WebPrintHelper.getInstance().print(model);
//            PaymentApplication.getInstance().startActivity(WebPrintActivity.getStartIntent(PaymentApplication.getInstance(), model));
            return;
        }
        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
        Q1PrintBuilder builder = new Q1PrintBuilder();
        String printString = "";
//        printString += builder.center(builder.bold("今日汇总" + "(" + getShowCurrentTime() + ")"));
//        printString += builder.branch();
//        printString += "终端设备号：" + AppConfigHelper.getConfig(AppConfigDef.sn, "") + builder.br();
//        printString += "慧商户号：" + AppConfigHelper.getConfig(AppConfigDef.mid, "") + builder.br();
//        printString += "商户名称：" + AppConfigHelper.getConfig(AppConfigDef.merchantName, "") + builder.br();
//        printString += builder.branch();
//        //交易-----------------------------------------------------------------
//        printString += "交易总金额：" + Calculater.formotFen(dailyRes.getTransTotalAmount()) + "元" + builder.br();
//        printString += "交易总笔数：" + dailyRes.getTransTotalAcount() + builder.br();
//        if (TextUtils.isEmpty(dailyRes.getRefundAmount())) {
//            printString += "撤销总金额：" + "0.00" + "元" + builder.br();
//        } else {
//            printString += "撤销总金额：" + Calculater.formotFen(dailyRes.getRefundAmount()) + "元" + builder.br();
//        }
//        String count = "0";
//        if (!TextUtils.isEmpty(dailyRes.getRefundCount())) {
//            count = dailyRes.getRefundCount();
//        }
//        printString += "撤销总笔数：" + count + builder.br();
//        printString += builder.branch() + builder.br() + builder.br() + builder.branch();
//        // -----------------------------------------------------------------
//        for (TodayDetailBean detail : todayDetailBeans) {
//            printString += "交易类型：" + detail.getDetailName() + builder.br();
//            printString += "笔    数：" + detail.getCount() + builder.br();
//            printString += "交易金额：" + Calculater.formotFen(detail.getAmount()) + "元" + builder.br();
//            printString += builder.branch();
//        }
//        // -----------------------------------------------------------------
        printString += builder.center(builder.bold(context.getString(R.string.print_daily_summary_title) + "(" + getShowCurrentTime() + ")"));
        printString += builder.branch();
        printString += context.getString(R.string.print_terminal_id) + AppConfigHelper.getConfig(AppConfigDef.sn, "") + builder.br();
        printString += context.getString(R.string.print_merchant_id) + AppConfigHelper.getConfig(AppConfigDef.mid, "") + builder.br();
        printString += context.getString(R.string.print_merchant_name) + AppConfigHelper.getConfig(AppConfigDef.merchantName, "") + builder.br();
        printString += DateUtil.format(new Date(), DateUtil.P1) + multipleSpaces(22 - DateUtil.format(new Date(), DateUtil.P12).length()) + DateUtil.format(new Date(), DateUtil.P12) + builder.br();
        printString += builder.branch();
        //交易-----------------------------------------------------------------
        printString += "Total              :" + "$" + Calculater.formotFen(dailyRes.getTransTotalAmount()) + builder.br();
        printString += "Total Transactions :" + dailyRes.getTransTotalAcount() + builder.br();
        if (TextUtils.isEmpty(dailyRes.getRefundAmount())) {
            printString += "Refund             ：" + "$0.00" + builder.br();
        } else {
            printString += "Refund             ：" + "$" + Calculater.formotFen(dailyRes.getRefundAmount()) + builder.br();
        }
        String count = "0";
        if (!TextUtils.isEmpty(dailyRes.getRefundCount())) {
            count = dailyRes.getRefundCount();
        }
        printString += "Refund Transactions：" + count + builder.br();
        printString += builder.branch();
        // -----------------------------------------------------------------
//        for (TodayDetailBean detail : todayDetailBeans) {
//            printString += "交易类型：" + detail.getDetailName() + builder.br();
//            printString += "笔    数：" + detail.getCount() + builder.br();
//            printString += "交易金额：" + Calculater.formotFen(detail.getAmount()) + "元" + builder.br();
//            printString += builder.branch();
//        }
        printString += context.getString(R.string.print_payType) + multipleSpaces(3) + context.getString(R.string.print_transactions) + multipleSpaces(4) + context.getString(R.string.print_amount) + builder.br();
        for (TodayDetailBean detail : todayDetailBeans) {
            printString += detail.getDetailName() + multipleSpaces(15 - detail.getDetailName().length()) +
                    detail.getCount() + multipleSpaces(16 - String.valueOf(detail.getCount()).length() - Calculater.formotFen(detail.getAmount()).length()) + "$" + Calculater.formotFen(detail.getAmount()) + builder.br();
            printString += builder.branch();
        }
        printString += builder.endPrint();
        controller.print(printString);
        controller.cutPaper();
    }

    public void printTodaySumPlus(TranLogVo tranLogVo) {
        if (AppConfigHelper.getConfig(AppConfigDef.SWITCH_LANGUAGE).equals("fr")) {
            HTMLPrintModel model = new HTMLPrintModel();
            List<HtmlLine> lines = new ArrayList<>();
            lines.add(new HTMLPrintModel.SimpleLine("DAILY REPORT", true, true));
            lines.add(new HTMLPrintModel.SimpleLine("SALES", true, true));
            lines.add(new HTMLPrintModel.SimpleLine(AppConfigHelper.getConfig(AppConfigDef.merchantName, ""), false, true));
            lines.add(new HTMLPrintModel.SimpleLine(AppConfigHelper.getConfig(AppConfigDef.merchantAddr, ""), false, true));
            lines.add(new HTMLPrintModel.SimpleLine(DateUtil.formatInternationalDate(new Date()), false, true));
            lines.add(new HTMLPrintModel.SimpleLine("Summary Period:", false, false));
            lines.add(new HTMLPrintModel.LeftAndRightLine("", tranLogVo.getBeginTime()));
            lines.add(new HTMLPrintModel.LeftAndRightLine("", tranLogVo.getEndTime()));
            lines.add(new HTMLPrintModel.EmptyLine());

            lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_merchant_id) + AppConfigHelper.getConfig(AppConfigDef.mid, "")));
            lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_emplayee) + AppConfigHelper.getConfig(AppConfigDef.operatorTrueName)));
            lines.add(new HTMLPrintModel.SimpleLine(context.getString(R.string.print_terminal_id) + AppConfigHelper.getConfig(AppConfigDef.sn, "")));
            lines.add(new HTMLPrintModel.EmptyLine());

            lines.add(new HTMLPrintModel.SimpleLine("SUMMARY", true, true));
            lines.add(new HTMLPrintModel.LeftAndRightLine("Gross Sales x " + tranLogVo.getGrossSalesNumber(), "$" + Tools.formatFen(tranLogVo.getGrossSalesAmount())));
            lines.add(new HTMLPrintModel.LeftAndRightLine("Refunds x " + tranLogVo.getRefundsNumber(), "$-" + Tools.formatFen(tranLogVo.getRefundsAmount())));
            lines.add(new HTMLPrintModel.LeftAndRightLine("Net Sales x " + tranLogVo.getNetSalesNumber(), "$" + Tools.formatFen(tranLogVo.getNetSalesAmount())));
            lines.add(new HTMLPrintModel.LeftAndRightLine("Tips x " + tranLogVo.getTipsNumber(), "$" + Tools.formatFen(tranLogVo.getTipsAmount())));
            lines.add(new HTMLPrintModel.LeftAndRightLine("Total Collected", "$" + Tools.formatFen(tranLogVo.getTotalCollected())));

            lines.add(new HTMLPrintModel.EmptyLine());

            lines.add(new HTMLPrintModel.SimpleLine("WECHAT PAY SUMMARY", true, true));
            lines.add(new HTMLPrintModel.LeftAndRightLine("Net Sales x " + tranLogVo.getWechatNetSalesNumber(), "$" + Tools.formatFen(tranLogVo.getWechatNetSalesAmount())));
            lines.add(new HTMLPrintModel.EmptyLine());

            lines.add(new HTMLPrintModel.SimpleLine("ALIPAY SUMMARY", true, true));
            lines.add(new HTMLPrintModel.LeftAndRightLine("Net Sales x " + tranLogVo.getAlipayNetSalesNumber(), "$" + Tools.formatFen(tranLogVo.getAlipayNetSalesAmount())));
            lines.add(new HTMLPrintModel.EmptyLine());

            lines.add(new HTMLPrintModel.SimpleLine("Union Pay QR", true, true));
            lines.add(new HTMLPrintModel.LeftAndRightLine("Net Sales x " + tranLogVo.getUnionPayNetSalesNumber(), "$" + Tools.formatFen(tranLogVo.getUnionPayNetSalesAmount())));
            lines.add(new HTMLPrintModel.EmptyLine());

            lines.add(new HTMLPrintModel.SimpleLine("END OF REPORT", false, true));

            lines.add(new HTMLPrintModel.BranchLine());
            model.setLineList(lines);
            WebPrintHelper.getInstance().print(model);
            return;
        }
        PrintServiceControllerProxy controller = new PrintServiceControllerProxy(context);
        Q1PrintBuilder builder = new Q1PrintBuilder();
        String printString = "";
        printString += builder.center(builder.bold("DAILY REPORT"));
        printString += builder.center(builder.bold("SALES"));
        printString += builder.center(AppConfigHelper.getConfig(AppConfigDef.merchantName, ""));
        printString += builder.center(AppConfigHelper.getConfig(AppConfigDef.merchantAddr, ""));
        printString += builder.center(DateUtil.formatInternationalDate(new Date()));
        printString += "Summary Period:" + builder.br();
        printString += "" + multipleSpaces(32 - tranLogVo.getBeginTime().length()) + tranLogVo.getBeginTime() + builder.br();
        printString += "" + multipleSpaces(32 - tranLogVo.getEndTime().length()) + tranLogVo.getEndTime() + builder.br();
        printString += builder.br();

        printString += context.getString(R.string.print_merchant_id) + AppConfigHelper.getConfig(AppConfigDef.mid, "") + builder.br();
        printString += context.getString(R.string.print_emplayee) + "All" + builder.br();
        if ("4".equals(getConfig(AppConfigDef.authFlag))) {
            printString += context.getString(R.string.print_terminal_id) + AppConfigHelper.getConfig(AppConfigDef.sn, "") + builder.br();
        } else {
            printString += context.getString(R.string.print_terminal_id) + "All" + builder.br();
        }


        printString += builder.br();

        printString += builder.center(builder.bold("SUMMARY"));
        printString += "Gross Sales x " + tranLogVo.getGrossSalesNumber() + multipleSpaces(31 - (("Gross Sales x " + tranLogVo.getGrossSalesNumber()) + Tools.formatFen(tranLogVo.getGrossSalesAmount())).length()) + ("$" + Tools.formatFen(tranLogVo.getGrossSalesAmount())) + builder.br();
        printString += "Refunds x " + tranLogVo.getRefundsNumber() + multipleSpaces(31 - (("Refunds x " + tranLogVo.getRefundsNumber()) + Tools.formatFen(tranLogVo.getRefundsAmount())).length()) + "$" + Tools.formatFen(tranLogVo.getRefundsAmount()) + builder.br();
        printString += "Net Sales x " + tranLogVo.getNetSalesNumber() + multipleSpaces(31 - (("Net Sales x " + tranLogVo.getNetSalesNumber()) + Tools.formatFen(tranLogVo.getNetSalesAmount())).length()) + ("$" + Tools.formatFen(tranLogVo.getNetSalesAmount())) + builder.br();
        printString += "Tips x " + tranLogVo.getTipsNumber() + multipleSpaces(31 - (("Tips x " + tranLogVo.getTipsNumber()) + Tools.formatFen(tranLogVo.getTipsAmount())).length()) + ("$" + Tools.formatFen(tranLogVo.getTipsAmount())) + builder.br();
        printString += "Total Collected" + multipleSpaces(16 - Tools.formatFen(tranLogVo.getTotalCollected()).length()) + ("$" + Tools.formatFen(tranLogVo.getTotalCollected())) + builder.br();
        printString += builder.br();

        printString += builder.center(builder.bold("WECHAT PAY SUMMARY"));
        printString += "Net Sales x " + tranLogVo.getWechatNetSalesNumber() + multipleSpaces(32 - (("Net Sales x " + tranLogVo.getWechatNetSalesNumber()) + "$" + Tools.formatFen(tranLogVo.getWechatNetSalesAmount())).length()) + ("$" + Tools.formatFen(tranLogVo.getWechatNetSalesAmount())) + builder.br();
        printString += builder.br();

        printString += builder.center(builder.bold("ALIPAY SUMMARY"));
        printString += "Net Sales x " + tranLogVo.getAlipayNetSalesNumber() + multipleSpaces(32 - (("Net Sales x " + tranLogVo.getAlipayNetSalesNumber()) + "$" + Tools.formatFen(tranLogVo.getAlipayNetSalesAmount())).length()) + ("$" + Tools.formatFen(tranLogVo.getAlipayNetSalesAmount())) + builder.br();
        printString += builder.br();

        printString += builder.center(builder.bold("Union Pay QR SUMMARY"));
        printString += "Net Sales x " + tranLogVo.getUnionPayNetSalesNumber() + multipleSpaces(32 - (("Net Sales x " + tranLogVo.getUnionPayNetSalesNumber()) + "$" + Tools.formatFen(tranLogVo.getUnionPayNetSalesAmount())).length()) + ("$" + Tools.formatFen(tranLogVo.getUnionPayNetSalesAmount())) + builder.br();
        printString += builder.br();

        printString += builder.center("END OF REPORT") + builder.br();
        printString += builder.branch();

        printString += builder.endPrint();
        controller.print(printString);
        controller.cutPaper();
    }

    private String getShowCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date(System.currentTimeMillis());
        return format.format(currentDate);
    }

    public static String getFormatedDateTime(String pattern, long dateTime) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date(dateTime + 0));
    }


    /**
     * @param transType  交易类型，可传空(//1 充值  2撤销 3消费)
     * @param startTime  起始时间
     * @param pageNumber 当前页数
     * @param endTime    截至时间
     * @param rechargeOn //0不含充值 1含充值 不传返回全部
     * @param timeRange  //0 今天 1 昨天 2本周 3上周 4本月 5上月 6时间段
     * @param listent
     */
    public void getQueryDetail(String timeRange, String rechargeOn, String transType, String startTime, String pageNumber, String endTime, String tranLogId, String tag, final ResponseListener listent) {//极简版收款根据时间范围查询交易@hong[20160325]
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("transType", transType);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("pageNo", pageNumber);
        params.put("rechargeOn", rechargeOn);
        params.put("timeRange", timeRange);
        if (!TextUtils.isEmpty(tranLogId) && (tranLogId.startsWith("P") || tranLogId.startsWith("p"))) {
            params.put("tranLogId", "P" + AppConfigHelper.getConfig(AppConfigDef.mid) + tranLogId.substring(1));
            params.remove("transType");
            params.remove("timeRange");
        }
        NetRequest.getInstance().addRequest(Constants.SC_924_TRAN_DETAIL, params, tag, new ResponseListener() {

            @Override
            public void onSuccess(Response response) {
                JSONObject detailResult = (JSONObject) response.getResult();
//                JSONObject detailLog = (JSONObject) detailResult.get("logs");
                List<TransDetailResp> list = JSONArray.parseArray(detailResult.get("logs").toString(), TransDetailResp.class);
                listent.onSuccess(new Response(0, "交易成功", list));
            }

            @Override
            public void onFaild(Response response) {
                listent.onFaild(response);
            }
        });
    }
}
