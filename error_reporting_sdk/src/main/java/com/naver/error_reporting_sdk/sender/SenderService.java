package com.naver.error_reporting_sdk.sender;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.naver.error_reporting_sdk.ReportInfo;
import com.naver.error_reporting_sdk.Reporter;

public class SenderService extends Service {
    private ReportInfo reportInfo;

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        reportInfo = intent.getParcelableExtra(ReportInfo.class.getSimpleName());
        Log.d(Reporter.LOG_TAG, "success to start service!");
        if(reportInfo != null) {
            Log.d(Reporter.LOG_TAG, "try to send!");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Sender sender = new DBSender(SenderService.this);
                    sender.send(reportInfo);
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
