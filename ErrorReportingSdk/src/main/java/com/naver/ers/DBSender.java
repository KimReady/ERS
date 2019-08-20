package com.naver.ers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.room.Room;

import java.util.List;

/**
 * when failed to send the log to server, stores it in local DB using Room
 */
class DBSender implements Sender {
    private static final String LOG_TAG = DBSender.class.getSimpleName();
    private final Context context;
    private LogDatabase db;
    private ErrorLogDao dao;

    DBSender(Context context) {
        this.context = context;
        try {
            db = Room.databaseBuilder(context, LogDatabase.class, LogDatabase.DB_NAME).build();
            dao = db.errorLogDao();
        } catch(RuntimeException e) {
            Log.e(LOG_TAG, "occurred exception while calling Room DB.");
        }
    }

    /**
     * store a log
     */
    @Override
    public void send(ErrorLog errorLog) {
        dao.insertErrorLog(errorLog);
        Log.d(LOG_TAG, "success to save the error.");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RetrieveJobScheduler.setUpdateJob(context);
        } else {
            Intent intent = new Intent(context, RetrieveLocalService.class);
            ComponentName service = context.startService(intent);
            if (service == null) {
                Log.w(LOG_TAG, "Failed to start the RetrieveLocalService.");
            }
        }

        db.close();
    }

    /**
     * store many logs
     */
    @Override
    public void send(List<ErrorLog> errorLogs) {
        dao.insertErrorLogs(errorLogs);
        Log.d(LOG_TAG, "success to save the errors.");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RetrieveJobScheduler.setUpdateJob(context);
        } else {
            Intent intent = new Intent(context, RetrieveLocalService.class);
            ComponentName service = context.startService(intent);
            if (service == null) {
                Log.w(LOG_TAG, "Failed to start the RetrieveLocalService.");
            }
        }

        db.close();
    }
}

