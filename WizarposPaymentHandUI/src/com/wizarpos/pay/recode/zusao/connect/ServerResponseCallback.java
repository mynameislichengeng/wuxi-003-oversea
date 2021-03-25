package com.wizarpos.pay.recode.zusao.connect;

import android.util.Log;

import com.amobilepayment.android.tmslibrary.DownloadStatus;
import com.amobilepayment.android.tmslibrary.FirmwareUpdateStatus;
import com.amobilepayment.android.tmslibrary.IServerResponse;
import com.amobilepayment.android.tmslibrary.STATE;
import com.amobilepayment.android.tmslibrary.TMS_FILE_TYPE;

import java.util.ArrayList;

public class ServerResponseCallback implements IServerResponse {

    private final String TAG = ServerResponseCallback.class.getSimpleName();

    @Override
    public void onJobError(STATE state, String s, String s1) {

    }

    @Override
    public void onJobCompleted() {

    }

    @Override
    public void onDownloadStatus(String s, String s1, DownloadStatus downloadStatus) {

    }

    @Override
    public void onDownloadProgress(String s, int i, long l, long l1) {

    }

    @Override
    public void onDownloadJobNumber(int i, int i1) {

    }

    @Override
    public void onFirmwareUpgradeStatus(int i) {

    }

    @Override
    public void onKeyInjectionReturnCode(int i) {

    }

    @Override
    public void onRequestedFileNotFound(TMS_FILE_TYPE tms_file_type) {

    }

    @Override
    public void onAdditionalFilesReceived() {

    }

    @Override
    public void onAdditionalFilesHashReceived(String s) {

    }

    @Override
    public void onConfigurationParametersReceived() {

    }

    @Override
    public void onConfigurationParametersHashReceived(String s) {

    }

    @Override
    public void onAppInstallCompleted(String s, int i) {

    }

    @Override
    public void onLocationDetected(String s) {

    }

    @Override
    public void onSilentInstallAllLastDownloadsFinished(FirmwareUpdateStatus firmwareUpdateStatus, int i, int i1, ArrayList<String> arrayList) {

    }

    @Override
    public void onCellularDownloadSet(boolean b) {

    }

    @Override
    public void onSetConfigurationParameterStatus(int i, String s) {
        Log.d("tagtagtag", TAG + "i:" + i + ", s:" + s);


    }

    @Override
    public void onDeleteInternalStorageFinished(boolean b, ArrayList<String> arrayList) {

    }
}
