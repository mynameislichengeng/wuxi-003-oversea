package com.wizarpos.pay.common.device;

import android.util.Log;

import com.cloudpos.jniinterface.SmartCardEvent;
import com.cloudpos.jniinterface.SmartCardInterface;
import com.wizarpos.base.net.Response;
import com.wizarpos.log.util.LogEx;
import com.wizarpos.pay.common.base.BasePresenter;

/**
 * Created by wu on 15/12/11.
 */
public class DefaultSamrtCardListenerImpl implements SmartCardListener {

    private int slotIndex = 0;
    private boolean isOpened;
    protected static final int EVENT_ID_CANCEL = -1;
    private static boolean isRun = false;
    private int handle = 0;
    private BasePresenter.ResultListener listener;

    @Override
    public void setSmartCardListener(BasePresenter.ResultListener listener) {
        this.listener = listener;
        try {
            if (isOpened) {
                Log.d("IC", "检测到重复打开");
                return;
            }
            int result = SmartCardInterface.open(slotIndex);
            if (result < 0) {
                LogEx.d("IC", "打开失败:" + result);
                listener.onFaild(new Response(1, "识别IC 卡" + result));
            } else {
                isOpened = true;
                handle = result;
                LogEx.d("IC", "打开成功:" + result);
                CallBackThread presentThread = new CallBackThread(SmartCardInterface.objPresent);
                presentThread.start();
                CallBackThread absentThread = new CallBackThread(SmartCardInterface.objAbsent);
                absentThread.start();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            listener.onFaild(new Response(1, "打开失败"));
        }
    }


    public void close() {
        if(!isOpened){
            LogEx.d("IC","设备未打开");
            return;
        }
        cancelRequest();
        isOpened = false;
        int result = SmartCardInterface.close(handle);
        if (result < 0) {
            LogEx.d("IC", "关闭失败" + result);
        } else {
            LogEx.d("IC", "关闭成功" + result);
        }

    }

    public void cancelRequest() {
        if (isRun) {
            synchronized (SmartCardInterface.objPresent) {
                SmartCardInterface.event = new SmartCardEvent();
                SmartCardInterface.event.nEventID = EVENT_ID_CANCEL;
                SmartCardInterface.objPresent.notifyAll();
            }
            synchronized (SmartCardInterface.objAbsent) {
                SmartCardInterface.event = new SmartCardEvent();
                SmartCardInterface.event.nEventID = EVENT_ID_CANCEL;
                SmartCardInterface.objAbsent.notifyAll();
            }
        }
    }

    class CallBackThread extends Thread {
        private Object object;

        public CallBackThread(Object object) {
            this.object = object;
        }

        @Override
        public void run() {
            isRun = true;
            while (isRun) {
                synchronized (object) {
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                if (SmartCardInterface.event.nEventID == EVENT_ID_CANCEL) {
                    break;
                } else if (SmartCardInterface.event.nEventID == SmartCardEvent.SMART_CARD_EVENT_INSERT_CARD) {
                    String msg = String.format("SlotIndex : %d Event : %s",
                            SmartCardInterface.event.nSlotIndex, "Inserted");
                    Log.d("IC", msg);
                    if (listener != null) {
                        listener.onSuccess(new Response(0, "Inserted"));
                    }
                } else if (SmartCardInterface.event.nEventID == SmartCardEvent.SMART_CARD_EVENT_REMOVE_CARD) {
                    String msg = String.format("SlotIndex : %d Event : %s", SmartCardInterface.event.nSlotIndex, "Removed");
                    Log.d("IC", msg);
//                    if (listener != null) {
//                        listener.onSuccess(new Response(1, "Removed"));
//                    }
                }
            }
            isRun = false;
        }
    }

}
