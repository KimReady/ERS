package com.naver.error_reporting_sdk;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class RetrieveLocalService extends Service {
    private static final String LOG_TAG = RetrieveLocalService.class.getSimpleName();

    private ScheduledExecutorService executor;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(executor != null && !executor.isShutdown()) {
            Log.d(LOG_TAG, "RetrieveLocalService has already been running.");
            return START_REDELIVER_INTENT;
        }
        executor = Executors.newScheduledThreadPool(1);
        Log.d(LOG_TAG, "start to retrieve Error logs in DB.");

        DBTask dbTask = new DBTask(this);
        executor.scheduleAtFixedRate(dbTask, 1, 30, TimeUnit.SECONDS);

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
            if(!Reporter.hasDiffTime()) {
                Reporter.setDifferentTimeFromServer(context);
            }

            LogDatabase db = Room.databaseBuilder(context, LogDatabase.class, LogDatabase.DB_NAME).build();
            ErrorLogDao dao = db.errorLogDao();

            List<ErrorLog> errorLogs = dao.selectAllErrorLogs();
            for (ErrorLog log : errorLogs) {
                Log.d(LOG_TAG, "retrieve and send the local data : " + log.toString());
            }
            dao.deleteLogs();
            db.close();

            if (errorLogs.isEmpty()) {
                Log.d(LOG_TAG, "Retrieve Service shut down.");
                executor.shutdownNow();
                stopSelf();
                return;
            }

            new HttpSender(context).send(errorLogs);
        }
    }
}
