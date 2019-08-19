package com.naver.ers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.room.Room;

import java.util.List;

/**
 * when failed to send the log to server, stores it in local DB using Room
 */
class DBSender implements Sender {
    private static final String LOG_TAG = DBSender.class.getSimpleName();
    private final Context context;
    private final LogDatabase db;
    private final ErrorLogDao dao;

    DBSender(Context context) {
        this.context = context;
        db = Room.databaseBuilder(context, LogDatabase.class, LogDatabase.DB_NAME).build();
        dao = db.errorLogDao();
    }

    /**
     * store a log
     */
    @Override
    public void send(ErrorLog errorLog) {
        dao.insertErrorLog(errorLog);
        Log.d(LOG_TAG, "success to save the error.");

        Intent intent = new Intent(context, RetrieveLocalService.class);
        context.startService(intent);

        db.close();
    }

    /**
     * store many logs
     */
    @Override
    public void send(List<ErrorLog> errorLogs) {
        dao.insertErrorLogs(errorLogs);
        Log.d(LOG_TAG, "success to save the errors.");

        Intent intent = new Intent(context, RetrieveLocalService.class);
        context.startService(intent);

        db.close();
    }
}

