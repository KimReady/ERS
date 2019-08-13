package com.naver.error_reporting_sdk.sender;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.naver.error_reporting_sdk.ReportInfo;
import com.naver.error_reporting_sdk.Reporter;
import com.naver.error_reporting_sdk.db.ErrorLog;

public final class SenderService extends Service {
    static final String LOG_TAG = SenderService.class.getSimpleName();

    private ReportInfo reportInfo;

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if(intent != null) {
            Bundle bundleReportInfo = intent.getBundleExtra(ReportInfo.class.getSimpleName());
            reportInfo = new ReportInfo(bundleReportInfo);
        }
        if(reportInfo != null) {
            Log.d(LOG_TAG, "try to send!");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Sender sender = Reporter.hasDiffTime() ?
                            new HttpSender(SenderService.this) :
                            new DBSender(SenderService.this);

                    sender.send(new ErrorLog(reportInfo));

                    stopSelf();
                }
            }).start();
        }
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
