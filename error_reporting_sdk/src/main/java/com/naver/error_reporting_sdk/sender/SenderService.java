package com.naver.error_reporting_sdk.sender;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.room.Room;

import com.naver.error_reporting_sdk.ReportInfo;
import com.naver.error_reporting_sdk.db.ErrorLog;
import com.naver.error_reporting_sdk.db.ErrorLogDao;
import com.naver.error_reporting_sdk.db.LogDatabase;

import java.util.List;

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
                    LogDatabase db = Room.databaseBuilder(
                            reportInfo.getContext(), LogDatabase.class, LogDatabase.DB_NAME)
                            .build();
                    ErrorLogDao dao = db.errorLogDao();

                    List<ErrorLog> localLogs = dao.selectAllErrorLogs();
                    dao.deleteLogs();
                    db.close();

                    new HttpSender(reportInfo.getContext(), localLogs).send(reportInfo);

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
