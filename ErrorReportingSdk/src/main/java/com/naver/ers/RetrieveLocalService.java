package com.naver.ers;

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

/**
 * Service that checks if there is any log in the local DB that has not yet been sent to the server
 * if so, try sending to the server via {@link HttpSender}
 */
public class RetrieveLocalService extends Service {
    private static final String LOG_TAG = RetrieveLocalService.class.getSimpleName();

    private ScheduledExecutorService executor;

    /**
     * search the log in local db every 30 seconds using ScheduledExecutorService
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(executor != null && !executor.isShutdown()) {
            Log.d(LOG_TAG, "RetrieveLocalService has already been running.");
            return START_NOT_STICKY;
        }
        executor = Executors.newScheduledThreadPool(1);
        Log.d(LOG_TAG, "start to retrieve Error logs in DB.");

        DBTask dbTask = new DBTask(this);
        executor.scheduleAtFixedRate(dbTask, 1, 30, TimeUnit.SECONDS);

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Runnable object for ScheduledExecutorService
     */
    private class DBTask implements Runnable {
        Context context;

        private DBTask(Context context) {
            this.context = context;
        }

        /**
         * search and extract DB
         * if there is any log, execute {@link HttpSender} for sending it to server
         */
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
