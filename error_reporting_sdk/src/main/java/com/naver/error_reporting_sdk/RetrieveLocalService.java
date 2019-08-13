package com.naver.error_reporting_sdk;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.room.Room;

import com.naver.error_reporting_sdk.db.ErrorLog;
import com.naver.error_reporting_sdk.db.ErrorLogDao;
import com.naver.error_reporting_sdk.db.LogDatabase;
import com.naver.error_reporting_sdk.sender.HttpSender;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RetrieveLocalService extends Service {
    private static final String LOG_TAG = RetrieveLocalService.class.getSimpleName();

    private ScheduledExecutorService executor;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(executor != null && !executor.isTerminated()) {
            return START_REDELIVER_INTENT;
        }

        if (executor == null) {
            executor = Executors.newScheduledThreadPool(1);
        }
        Log.d(LOG_TAG, "start to retrieve Error logs in DB.");

        DBTask task = new DBTask(this);

        if (!Reporter.hasDiffTime()) {
            Reporter.setDifferentTimeFromServer(this);
        }

        executor.scheduleAtFixedRate(task, 5, 30, TimeUnit.SECONDS);

        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class DBTask implements Runnable {
        Context context;

        private DBTask(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            LogDatabase db = Room.databaseBuilder(context, LogDatabase.class, LogDatabase.DB_NAME).build();
            ErrorLogDao dao = db.errorLogDao();

            List<ErrorLog> errorLogs = dao.selectAllErrorLogs();
            for (ErrorLog log : errorLogs) {
                Log.d(LOG_TAG, "try to load and send local data : " + log.toString());
            }
            dao.deleteLogs();
            db.close();

            if (errorLogs.isEmpty()) {
                Log.d(LOG_TAG, "Retrieve Service shut down.");
                executor.shutdownNow();
                return;
            }

            new HttpSender(context).send(errorLogs);
        }
    }
}
