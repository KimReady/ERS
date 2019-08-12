package com.naver.error_reporting_sdk.sender;

import android.content.Context;
import android.util.Log;

import com.naver.error_reporting_sdk.R;
import com.naver.error_reporting_sdk.ReportInfo;
import com.naver.error_reporting_sdk.Reporter;
import com.naver.error_reporting_sdk.db.ErrorLog;
import com.naver.httpclientlib.CallTask;
import com.naver.httpclientlib.HttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class HttpSender {
    private static final String LOG_TAG = HttpSender.class.getSimpleName();
    private static final String REPORT_URL = "/report";
    Context context;
    List<ErrorLog> errorLogs;

    HttpSender(Context context, List<ErrorLog> localLogs) {
        this.context = context;
        this.errorLogs = localLogs != null ? localLogs : new ArrayList<ErrorLog>();
    }

    void send(ReportInfo reportInfo) {
        errorLogs.add(new ErrorLog(reportInfo));

        HttpClient httpClient = new HttpClient.Builder().build();
        HttpService httpService = httpClient.create(HttpService.class);

        List<ErrorLog> failedLogs = new ArrayList<>();
        final String report_url = context.getResources().getString(R.string.server_url) + REPORT_URL;

        if(!Reporter.hasDiffTime()) {
            Reporter.setDifferentTimeFromServer(context);
        }

        for(ErrorLog localLog : errorLogs) {
            if(!localLog.isCorrectDate()) {
                localLog.addDiffTimeWithServer();
            }

            CallTask<ErrorLog> callTask = httpService.postLog(report_url, localLog);
            try {
                if(!callTask.execute().isSuccessful()) {
                    throw new IllegalStateException();
                }

                Log.d(LOG_TAG, "Succeed to send the Error-Report.");
            } catch(IOException | IllegalStateException e) {
                Log.w(LOG_TAG, "failed to connect with server.");
                failedLogs.add(localLog);
            }
        }

        if(!failedLogs.isEmpty()) {
            new DBSender(context).send(failedLogs);
        }
    }
}
