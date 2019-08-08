package com.naver.error_reporting_sdk.sender;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.naver.error_reporting_sdk.ReportInfo;
import com.naver.error_reporting_sdk.Reporter;
import com.naver.error_reporting_sdk.db.ErrorLog;
import com.naver.error_reporting_sdk.db.ErrorLogDao;
import com.naver.error_reporting_sdk.db.LogDatabase;

import java.util.List;

public class DBSender implements Sender {
    private static final String DB_NAME = Reporter.class.getSimpleName();
    private final Context context;

    public DBSender(Context context) {
        this.context = context;
    }

    @Override
    public void send(final ReportInfo reportInfo) {
        LogDatabase db = Room.databaseBuilder(context, LogDatabase.class, DB_NAME).build();
        ErrorLogDao dao = db.errorLogDao();

        dao.insertErrorLogs(new ErrorLog(reportInfo));
        List<ErrorLog> errorLogs = dao.selectAllErrorLogs();
        for (ErrorLog log : errorLogs) {
            Log.d(Reporter.LOG_TAG, log.toString());
        }
    }
}

