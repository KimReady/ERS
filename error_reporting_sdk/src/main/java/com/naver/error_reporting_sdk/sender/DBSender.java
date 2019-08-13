package com.naver.error_reporting_sdk.sender;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.room.Room;

import com.naver.error_reporting_sdk.RetrieveLocalService;
import com.naver.error_reporting_sdk.db.ErrorLog;
import com.naver.error_reporting_sdk.db.ErrorLogDao;
import com.naver.error_reporting_sdk.db.LogDatabase;

import java.util.List;

class DBSender implements Sender {
    static final String LOG_TAG = DBSender.class.getSimpleName();
    private final Context context;
    private final LogDatabase db;
    private final ErrorLogDao dao;

    DBSender(Context context) {
        this.context = context;
        db = Room.databaseBuilder(context, LogDatabase.class, LogDatabase.DB_NAME).build();
        dao = db.errorLogDao();
    }

    @Override
    public void send(ErrorLog errorLog) {
        dao.insertErrorLog(errorLog);
        Log.d(LOG_TAG, "success to save the error.");

        Intent intent = new Intent(context, RetrieveLocalService.class);
        context.startService(intent);

        db.close();
    }

    @Override
    public void send(List<ErrorLog> errorLogs) {
        dao.insertErrorLogs(errorLogs);
        Log.d(LOG_TAG, "success to save the errors.");

        Intent intent = new Intent(context, RetrieveLocalService.class);
        context.startService(intent);

        db.close();
    }
}

