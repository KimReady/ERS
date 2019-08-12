package com.naver.error_reporting_sdk.sender;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.naver.error_reporting_sdk.db.ErrorLog;
import com.naver.error_reporting_sdk.db.ErrorLogDao;
import com.naver.error_reporting_sdk.db.LogDatabase;

import java.util.List;

class DBSender {
    static final String LOG_TAG = DBSender.class.getSimpleName();
    private final Context context;

    DBSender(Context context) {
        this.context = context;
    }

    void send(List<ErrorLog> errorLog) {
        LogDatabase db = Room.databaseBuilder(context, LogDatabase.class, LogDatabase.DB_NAME).build();
        ErrorLogDao dao = db.errorLogDao();

        dao.insertErrorLogs(errorLog);
        List<ErrorLog> errorLogs = dao.selectAllErrorLogs();
        for (ErrorLog log : errorLogs) {
            Log.d(LOG_TAG, log.toString());
        }
        db.close();
    }
}

