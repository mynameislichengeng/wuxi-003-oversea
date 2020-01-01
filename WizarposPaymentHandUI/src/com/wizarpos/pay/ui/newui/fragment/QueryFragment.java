package com.wizarpos.pay.ui.newui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.wizarpos.pay.common.DialogHelper;
import com.wizarpos.pay.common.base.BaseLogicAdapter;
import com.wizarpos.pay.common.base.ViewHolder;
import com.wizarpos.pay.common.device.DeviceManager;
import com.wizarpos.pay.recode.hisotory.activitylist.constants.TransRecordConstants;
import com.wizarpos.pay.ui.newui.util.ItemDataUtils;
import com.wizarpos.pay.ui.widget.CommonToastUtil;
import com.wizarpos.pay.view.ArrayItem;
import com.wizarpos.pay2.lite.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Song
 */
public class QueryFragment extends Fragment implements View.OnClickListener {

    private final int DEFAULT_TRANTYPE_INDEX = 0;//类型，默认位置
    private final int DEFAULT_TIME_RANGE_INDEX = 0;//默认位置

    //右侧抽屉相关数据
    private View view = null;
    private GridView gvTranType, gvTimeRange;
    /**
     * @param tranType 消费 3 撤销 2
     * @param timeRange 0 今天 1 昨天 2本周 3上周 4本月 5上月 6时间段
     * @param tranTypeIndex index
     * @param timeRangeIndex index
     */
    private int tranType, timeRange, tranTypeIndex = DEFAULT_TRANTYPE_INDEX, timeRangeIndex = DEFAULT_TIME_RANGE_INDEX;
    private BaseLogicAdapter<ArrayItem> tranTypeAdapter, timeRangeAdapter;
    private EditText etStartTime, etEndTime, etTranLogId;

    private QueryFragmentListener mListener;

    public QueryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QueryFragment.
     */
    public static QueryFragment newInstance() {
        QueryFragment fragment = new QueryFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.partial_query_right, container, false);
        gvTimeRange = (GridView) view.findViewById(R.id.gvTimeRange);
        gvTranType = (GridView) view.findViewById(R.id.gvTranType);
        tranTypeAdapter = new BaseLogicAdapter<ArrayItem>(getContext(), ItemDataUtils.getTranTypes(), R.layout.item_check_choose) {
            @Override
            public void convert(ViewHolder helper, final ArrayItem item, final int position) {
                TextView tvArrayItem = helper.getView(R.id.tvArrayItem);
                tvArrayItem.setText(item.getShowValue());
                tvArrayItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tranType = item.getRealValue();
                        tranTypeIndex = position;
                        tranTypeAdapter.notifyDataSetChanged();
                    }
                });
                if (position == tranTypeIndex) {
                    tvArrayItem.setBackgroundResource(R.drawable.shape_btn_blue);
                    tvArrayItem.setText("√" + item.getShowValue());
                    tvArrayItem.setTextColor(0xff0074e1);
                } else {
                    tvArrayItem.setBackgroundResource(R.drawable.shape_btn_gray);
                    tvArrayItem.setTextColor(0xff333333);
                }
            }
        };
        gvTranType.setAdapter(tranTypeAdapter);
        timeRangeAdapter = new BaseLogicAdapter<ArrayItem>(getContext(), ItemDataUtils.getTimeRanges(), R.layout.item_check_choose) {
            @Override
            public void convert(ViewHolder helper, final ArrayItem item, final int position) {
                TextView tvArrayItem = helper.getView(R.id.tvArrayItem);
                tvArrayItem.setText(item.getShowValue());
                tvArrayItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeRange = item.getRealValue();
                        timeRangeIndex = position;
                        timeRangeAdapter.notifyDataSetChanged();
                        if (item.getRealValue() == 6) {
                            //显示时间起始选择
                            view.findViewById(R.id.llTimeChoose).setVisibility(View.VISIBLE);
                        } else {
                            view.findViewById(R.id.llTimeChoose).setVisibility(View.GONE);
                        }
                    }
                });
                if (position == timeRangeIndex) {
                    tvArrayItem.setBackgroundResource(R.drawable.shape_btn_blue);
                    tvArrayItem.setText("√" + item.getShowValue());
                    tvArrayItem.setTextColor(0xff0074e1);
                } else {
                    tvArrayItem.setBackgroundResource(R.drawable.shape_btn_gray);
                    tvArrayItem.setTextColor(0xff333333);
                }
            }
        };
        gvTimeRange.setAdapter(timeRangeAdapter);
        view.findViewById(R.id.tvResetQuery).setOnClickListener(this);
        view.findViewById(R.id.tvQuery).setOnClickListener(this);
        etStartTime = (EditText) view.findViewById(R.id.etStartTime);
        etStartTime.setInputType(InputType.TYPE_NULL);
        etEndTime = (EditText) view.findViewById(R.id.etEndTime);
        etEndTime.setInputType(InputType.TYPE_NULL);
        etTranLogId = (EditText) view.findViewById(R.id.etTranLogId);
        if (DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_Q1
                || DeviceManager.getInstance().getDeviceType() == DeviceManager.DEVICE_TYPE_WIZARHAND_M0) {
            etTranLogId.setInputType(InputType.TYPE_NULL);
        }
        etStartTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogHelper.showDateDialog(getContext(), etStartTime);
            }
        });
        etStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogHelper.showDateDialog(getContext(), etStartTime);
                }
            }
        });
        etEndTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogHelper.showDateDialog(getContext(), etEndTime);
            }
        });
        etEndTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogHelper.showDateDialog(getContext(), etEndTime);
                }
            }
        });
        view.findViewById(R.id.llDrawer).setOnClickListener(this);//防止点击穿透
        doQueryReset();
        return view;
    }

    private void onQuery() {
        if (mListener != null) {
            Map<String, String> params = new HashMap<>();
            params.put("tranType", "" + tranType);
            params.put("timeRange", "" + timeRange);
            if (timeRange == 6) {
                String startDate = etStartTime.getText().toString();
                String endDate = etEndTime.getText().toString();
                try {
                    if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
                        SimpleDateFormat dateformat = new SimpleDateFormat("yy-MM-dd");
                        Date startTime = null, endTime = null;
                        try {
                            startTime = dateformat.parse(startDate);
                            endTime = dateformat.parse(endDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (startTime.after(endTime)) {
                            CommonToastUtil.showMsgBelow(getContext(), CommonToastUtil.LEVEL_WARN, getResources().getString(R.string.data_query_error));
//                            UIHelper.ToastMessage(getContext(), "起始日期大于结束日期");
                            return;
                        }
                        params.put("startDate", startDate);
                        params.put("endDate", endDate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            String tranlogId = "";
            if (etTranLogId.getText() != null && !TextUtils.isEmpty(etTranLogId.getText().toString())) {
                tranlogId = "P" + etTranLogId.getText().toString();
            }
            mListener.onQuery(timeRange + "", tranType + "", params.get("startDate"), params.get("endDate"), tranlogId);
            doQueryReset();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof QueryFragmentListener) {
            mListener = (QueryFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement QueryFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * 重置查询条件 Song
     */
    public void doQueryReset() {
        tranTypeIndex = DEFAULT_TRANTYPE_INDEX;
        tranType = Integer.valueOf(TransRecordConstants.TransType.ALL.getType());
        timeRange = Integer.valueOf(TransRecordConstants.TimeRange.TODAY.getType());
        timeRangeIndex = DEFAULT_TIME_RANGE_INDEX;
        etStartTime.setText("");
        etEndTime.setText("");
        etTranLogId.setText("");
        tranTypeAdapter.notifyDataSetChanged();
        timeRangeAdapter.notifyDataSetChanged();
        view.findViewById(R.id.llTimeChoose).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvResetQuery:
                doQueryReset();
                break;
            case R.id.tvQuery:
                onQuery();
                break;
            default:
                break;
        }
    }

    public interface QueryFragmentListener {
        /**
         * @param timeRange
         * @param tranType
         * @param startDate
         * @param endDate
         */
        void onQuery(String timeRange, String tranType, String startDate, String endDate, String tranlogId);
    }
}
